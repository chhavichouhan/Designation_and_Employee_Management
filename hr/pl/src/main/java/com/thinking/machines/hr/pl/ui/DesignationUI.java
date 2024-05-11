package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*; //for table header
import javax.swing.ImageIcon;
import java.io.File;

public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private DesignationModel designationModel;
private JTable designationTable;
private JScrollPane scrollPane;
private JLabel titleLabel;
private JLabel searchLabel;
private JLabel searchErrorLabel;
private JTextField searchTextField;
private JButton clearSearchFieldButton;
private Container container;
private DesignationPanel designationPanel;//innerClass pointer for icons
private enum MODE{ADD,EDIT,DELETE,VIEW,EXPORT_TO_PDF,EMPLOYEE_MODE};
private MODE mode;
private ImageIcon logoIcon;
private ImageIcon addIcon;
private ImageIcon editIcon;
private ImageIcon cancelIcon;
private ImageIcon deleteIcon;
private ImageIcon saveIcon;
private ImageIcon pdfIcon;
private ImageIcon crossIcon;
private ImageIcon employeeIcon;

public DesignationUI()
{
initComponents();	//object creation here
setApperance();		//all apperance related code here
addListeners();		//all event handling here
setViewMode();
designationPanel.setViewMode();
}

public void initComponents()
{
designationModel=new DesignationModel();
designationTable=new JTable(designationModel);
scrollPane=new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
titleLabel=new JLabel("Designations");
searchLabel=new JLabel("Search");
searchErrorLabel=new JLabel("");
searchTextField=new JTextField();
crossIcon=new ImageIcon(this.getClass().getResource("/icons/cross.jpeg"));
clearSearchFieldButton=new JButton(crossIcon);
container=getContentPane();
}

private void setApperance()
{
logoIcon=new ImageIcon(this.getClass().getResource("/icons/logos.jpeg"));
setIconImage(logoIcon.getImage());
addIcon=new ImageIcon(this.getClass().getResource("/icons/add.png"));
editIcon=new ImageIcon(this.getClass().getResource("/icons/edit.png"));
deleteIcon=new ImageIcon(this.getClass().getResource("/icons/delete.png"));
cancelIcon=new ImageIcon(this.getClass().getResource("/icons/cancel.png"));
saveIcon=new ImageIcon(this.getClass().getResource("/icons/save.png"));
pdfIcon=new ImageIcon(this.getClass().getResource("/icons/pdf.png"));
employeeIcon=new ImageIcon(this.getClass().getResource("/icons/employee.jpg"));

Font titleFont=new Font("Times New Roman",Font.BOLD,20);
Font captionFont=new Font("Verdana",Font.BOLD,15);
Font searchErrorFont=new Font("Verdana",Font.BOLD,12);
Font dataFont=new Font("Verdana",Font.PLAIN,15);
titleLabel.setFont(titleFont);
searchLabel.setFont(captionFont);
searchErrorLabel.setFont(searchErrorFont);
searchErrorLabel.setForeground(Color.red);
searchTextField.setFont(dataFont);
designationTable.setFont(dataFont);
designationTable.setRowHeight(20);

//set col width
designationTable.getColumnModel().getColumn(0).setPreferredWidth(85);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(380);

//making table header bold
Font columnHeaderFont=new Font("Verdana",Font.BOLD,15);
JTableHeader header=designationTable.getTableHeader(); //table ka header get kra
header.setFont(columnHeaderFont);

//to stop reordering,cursor se column exchange ho skte
header.setReorderingAllowed(false);

//auto resize off kra(cursor k through strecth na ho ske table k col)
header.setResizingAllowed(false);

//single cell select na ho, pura row select ho
designationTable.setRowSelectionAllowed(true);

//multiple row select na ho using clt,single row select ho
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

container.setLayout(null); //by default borderLayout rhega
container.add(titleLabel);
container.add(searchErrorLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(clearSearchFieldButton);
container.add(scrollPane);
designationPanel=new DesignationPanel();
container.add(designationPanel);

int width=500;
int height=650;
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation(((d.width/2)-(width/2)),((d.height/2)-(height/2)));
setSize(width,height);

/*to align components a.setBounds(leftAlign,fromTopAlign,widthOfComponents,heightOfComponent)*/
int left=0;
int top=0;
titleLabel.setBounds(left+10,top+25,200,25);
searchErrorLabel.setBounds(left+10+20+280,top+20+28,200,10);
searchLabel.setBounds(left+10,top+20+40,100,20);
searchTextField.setBounds(left+10+70,top+20+40,300,20);
clearSearchFieldButton.setBounds(left+10+70+310,top+20+40,20,20);
scrollPane.setBounds(left+10,top+20+20+45,465,350);
designationPanel.setBounds(left+10,top+20+20+35+370,465,150);

setDefaultCloseOperation(EXIT_ON_CLOSE);

}

private void addListeners()
{
designationTable.getSelectionModel().addListSelectionListener(this);
searchTextField.getDocument().addDocumentListener(this);
clearSearchFieldButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
searchTextField.setText("");
searchTextField.requestFocus();
}
});
}

public void searchDesignation()
{
searchErrorLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0)return;
int rowIndex;
try
{
rowIndex=designationModel.indexOfTitle(title,true);
}
catch(BLException be)
{
searchErrorLabel.setText("Not Found");
return;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
}

public void changedUpdate(DocumentEvent de)
{
searchDesignation();
}

public void removeUpdate(DocumentEvent de)
{
searchDesignation();
}

public void insertUpdate(DocumentEvent de)
{
searchDesignation();
}

public void valueChanged(ListSelectionEvent lse)
{
int selectedRowIndex=designationTable.getSelectedRow();
try
{
DesignationInterface designation=designationModel.getDesignationAt(selectedRowIndex);
designationPanel.setDesignation(designation);
}
catch(BLException blexception)
{
designationPanel.clearDesignation();
}
}

public void setViewMode()
{
this.mode=MODE.VIEW;
if(designationModel.getRowCount()==0)
{
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
clearSearchFieldButton.setEnabled(true);
designationTable.setEnabled(true);
}
}

public void setAddMode()
{
this.mode=MODE.ADD;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

public void setEditMode()
{
this.mode=MODE.EDIT;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

public void setDeleteMode()
{
this.mode=MODE.DELETE;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

public void setExportToPDFMode()
{
this.mode=MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

public void setEmployeeMode()
{
this.mode=MODE.EMPLOYEE_MODE;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}

//innerClass
class DesignationPanel extends JPanel
{
private JLabel titleCaptionLabel;
private JLabel titleLabel;
private JTextField titleTextField;
private JButton clearTitleTextFieldButton;
private JButton addButton;
private JButton editButton;
private JButton cancelButton;
private JButton deleteButton;
private JButton exportToPDFButton;
private JButton employeeButton;
private JPanel buttonsPanel;
private DesignationInterface designation;

DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
initComponents();
setApperance();
addListeners();
}

private void initComponents()
{
designation=null;
titleCaptionLabel=new JLabel("Designation");
titleLabel=new JLabel("");
titleTextField=new JTextField();
clearTitleTextFieldButton=new JButton(crossIcon);
buttonsPanel=new JPanel();
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
cancelButton=new JButton(cancelIcon);
deleteButton=new JButton(deleteIcon);
exportToPDFButton=new JButton(pdfIcon);
employeeButton=new JButton(employeeIcon);
}

private void setApperance()
{
Font captionFont=new Font("Verdana",Font.BOLD,16);
Font dataFont=new Font("Verdana",Font.PLAIN,16);
titleCaptionLabel.setFont(captionFont);
titleLabel.setFont(dataFont);
titleTextField.setFont(dataFont);
setLayout(null);
int lm=0;
int tm=0;
titleCaptionLabel.setBounds(lm+10,tm+5,110,30);
titleLabel.setBounds(lm+110+10,tm+5,400,30);
titleTextField.setBounds(lm+10+110+5,tm+7,270,30);
clearTitleTextFieldButton.setBounds(lm+10+110+5+200+75,tm+7,30,30);
buttonsPanel.setBounds(17,tm+50,430,80);
buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
addButton.setBounds(15,14,50,50);
editButton.setBounds(15+50+20,14,50,50);
cancelButton.setBounds(15+50+20+50+20,14,50,50);
deleteButton.setBounds(15+50+20+50+20+50+20,14,50,50);
exportToPDFButton.setBounds(15+50+20+50+20+50+20+50+20,14,50,50);
employeeButton.setBounds(15+50+20+50+20+50+20+50+20+50+20,14,50,50);

buttonsPanel.setLayout(null);
buttonsPanel.add(addButton);
buttonsPanel.add(editButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(exportToPDFButton);
buttonsPanel.add(employeeButton);
add(titleCaptionLabel);
add(titleLabel);
add(clearTitleTextFieldButton);
add(titleTextField);
add(buttonsPanel);
}

void setViewMode()
{
DesignationUI.this.setViewMode();
this.addButton.setIcon(addIcon);
this.editButton.setIcon(editIcon);
this.titleTextField.setVisible(false);
this.titleLabel.setVisible(true);
this.clearTitleTextFieldButton.setVisible(false);
this.addButton.setEnabled(true);
this.cancelButton.setEnabled(false);
if(designationModel.getRowCount()<0)
{
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.employeeButton.setEnabled(false);
}
else
{
this.editButton.setEnabled(true);
this.deleteButton.setEnabled(true);
this.exportToPDFButton.setEnabled(true);
this.employeeButton.setEnabled(true);
}
}

void setAddMode()
{
DesignationUI.this.setAddMode();
this.titleTextField.setText("");
this.addButton.setIcon(saveIcon);
this.titleLabel.setVisible(false);
this.clearTitleTextFieldButton.setVisible(true);
this.titleTextField.setVisible(true);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(true);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.employeeButton.setEnabled(false);
}

void setEditMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to edit");
return;
}
DesignationUI.this.setEditMode();
this.titleTextField.setText(this.designation.getTitle());
this.titleLabel.setVisible(false);
this.clearTitleTextFieldButton.setVisible(true);
this.titleTextField.setVisible(true);
this.addButton.setEnabled(false);
this.cancelButton.setEnabled(true);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.editButton.setIcon(saveIcon);
this.employeeButton.setEnabled(false);
}

void setDeleteMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to delete");
return;
}
DesignationUI.this.setDeleteMode();
this.addButton.setEnabled(false);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.employeeButton.setEnabled(false);
removeDesignation();
DesignationUI.this.setViewMode();
this.setViewMode();
}

void setExportToPDFMode()
{
DesignationUI.this.setExportToPDFMode();
this.addButton.setEnabled(false);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.employeeButton.setEnabled(false);
}

void setEmployeeMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select designation to see employee details");
return;
}
DesignationUI.this.setEmployeeMode();
this.addButton.setEnabled(false);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
employee();
DesignationUI.this.setViewMode();
this.setViewMode();
}

private boolean addDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation required","Warning",JOptionPane.WARNING_MESSAGE);
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setTitle(title);
try
{
designationModel.add(d);
int rowIndex=0;
try
{
rowIndex=designationModel.indexOfDesignation(d);
}
catch(BLException blexception)
{
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
return true;
}
catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
}
else
{
if(blexception.hasException("title"))
{
JOptionPane.showMessageDialog(this,blexception.getException("title"));
}
}
titleTextField.requestFocus();
return false;
}
}

private boolean updateDesignation()
{
String title=titleTextField.getText().trim();
if(title.length()==0)
{
JOptionPane.showMessageDialog(this,"Designation required","Warning",JOptionPane.WARNING_MESSAGE);
titleTextField.requestFocus();
return false;
}
DesignationInterface d=new Designation();
d.setCode(this.designation.getCode());
d.setTitle(title);
try
{
designationModel.update(d);
int rowIndex=0;
try
{
rowIndex=designationModel.indexOfDesignation(d);
}
catch(BLException blexception)
{
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
return true;
}
catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
}
else
{
if(blexception.hasException("title"))
{
JOptionPane.showMessageDialog(this,blexception.getException("title"));
}
}
titleTextField.requestFocus();
return false;
}
}


private void employee()
{
String title=(this.designation.getTitle().trim()).toUpperCase();
EmployeeUI employeeUI=new EmployeeUI(this.designation.getCode(),title);
employeeUI.setVisible(true);
/*
try
{
String title=this.designation.getTitle();
employeeUI.setVisible(true);
}
catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
}
else
{
if(blexception.hasException("title"))
{
JOptionPane.showMessageDialog(this,blexception.getException("title"));
}
}
}*/
}

private void removeDesignation()
{
try
{
String title=this.designation.getTitle();
int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+ title+" ?","Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION)return;
designationModel.remove(this.designation.getCode());
JOptionPane.showMessageDialog(this,title+" deleted");
//this.clearDesignation();
}
catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(this,blexception.getGenericException());
}
else
{
if(blexception.hasException("title"))
{
JOptionPane.showMessageDialog(this,blexception.getException("title"));
}
}
}
}

private void addListeners()
{
this.addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
if(mode==MODE.VIEW)
{
setAddMode();
}
else 
{
if(addDesignation())
{
setViewMode();
}
}
}
});

this.clearTitleTextFieldButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
titleTextField.setText("");
}
});

this.editButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
if(mode==MODE.VIEW)
{
setEditMode();
}
else
{
if(updateDesignation())
{
setViewMode();
}
}
}
});

this.deleteButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
setDeleteMode();
}
});

this.cancelButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
setViewMode();
}
});

this.employeeButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
setEmployeeMode();
}
});

this.exportToPDFButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
JFileChooser jfc=new JFileChooser();
jfc.setAcceptAllFileFilterUsed(false);
jfc.setCurrentDirectory(new File("."));
jfc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter(){

public boolean accept(File file)
{
if(file.getName().endsWith(".pdf"))return true;
return false;
}

public String getDescription()
{
return "PDF Files";
}
});
int selectedOption=jfc.showSaveDialog(DesignationUI.this);
if(selectedOption==JFileChooser.APPROVE_OPTION)
{
try
{
File selectedFile=jfc.getSelectedFile();
String pdfFile=selectedFile.getAbsolutePath();
if(pdfFile.endsWith("."))pdfFile+="pdf";
else if(pdfFile.endsWith(".pdf")==false)pdfFile+=".pdf";
File file=new File(pdfFile);
File parentFile=new File(file.getParent());
if(parentFile.exists()==false || parentFile.isDirectory()==false)
{
JOptionPane.showMessageDialog(DesignationUI.this,"Incorrect path : "+file.getAbsolutePath());
return;
}
designationModel.exportToPDF(file);
JOptionPane.showMessageDialog(DesignationUI.this,"File saved at path : "+file.getAbsolutePath());
}
catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(DesignationUI.this,blexception.getGenericException());
}
}
catch(Exception e)
{
}
}
}
});
}

public void setDesignation(DesignationInterface designation)
{
this.designation=designation;
titleLabel.setText(designation.getTitle().toUpperCase());
}

public void clearDesignation()
{
this.designation=null;
titleLabel.setText("");
}
}
}