package com.thinking.machines.hr.pl.ui;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.enums.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*; //for table header
import javax.swing.ImageIcon;
import java.io.File;

//correct code of update employee

public class EmployeeUI extends JFrame implements DocumentListener,ListSelectionListener
{
private EmployeeModel employeeModel;
private JTable employeeTable;
private JScrollPane scrollPane;
private EmployeePanel employeePanel;//innerClass pointer for icons

private String title;
private JLabel designationLabel;
private JLabel designationCodeLabel;
private JLabel nameLabel;
private JLabel searchLabel;
private JLabel searchErrorLabel;
private JTextField searchTextField;
private JButton clearSearchFieldButton;
private Container container;
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
private int codeOfDesignation;
private String titleOfDesignation;
private SimpleDateFormat sdf;

public EmployeeUI(int codeOfDesignation,String titleOfDesignation)
{
this.codeOfDesignation=codeOfDesignation;
this.titleOfDesignation=titleOfDesignation;
initComponents(codeOfDesignation,titleOfDesignation);
setApperance();
addListeners();		//all event handling here
setViewMode();
employeePanel.setViewMode();
sdf=new SimpleDateFormat("dd/MM/yyyy");
}


public void initComponents(int codeOfDesignation,String titleOfDesignation)
{
employeeModel=new EmployeeModel(codeOfDesignation);
employeeTable=new JTable(employeeModel);
scrollPane=new JScrollPane(employeeTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
designationLabel=new JLabel("Designation : ");
designationCodeLabel=new JLabel(titleOfDesignation+" ("+codeOfDesignation+")");
nameLabel=new JLabel("Employees");
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

Font titleFont=new Font("Times New Roman",Font.BOLD,20);
Font captionFont=new Font("Verdana",Font.BOLD,15);
Font searchErrorFont=new Font("Verdana",Font.BOLD,12);
Font dataFont=new Font("Verdana",Font.PLAIN,15);
designationLabel.setFont(titleFont);
designationCodeLabel.setFont(titleFont);
nameLabel.setFont(titleFont);
searchLabel.setFont(captionFont);
searchErrorLabel.setFont(searchErrorFont);
searchErrorLabel.setForeground(Color.red);
searchTextField.setFont(dataFont);
employeeTable.setFont(dataFont);
employeeTable.setRowHeight(20);

//set col width
employeeTable.getColumnModel().getColumn(0).setPreferredWidth(85);
employeeTable.getColumnModel().getColumn(1).setPreferredWidth(380);

//making table header bold
Font columnHeaderFont=new Font("Verdana",Font.BOLD,15);
JTableHeader header=employeeTable.getTableHeader(); //table ka header get kra
header.setFont(columnHeaderFont);

//to stop reordering,cursor se column exchange ho skte
header.setReorderingAllowed(false);

//auto resize off kra(cursor k through strecth na ho ske table k col)
header.setResizingAllowed(false);

//single cell select na ho, pura row select ho
employeeTable.setRowSelectionAllowed(true);

//multiple row select na ho using clt,single row select ho
employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

container.setLayout(null); //by default borderLayout rhega
container.add(designationLabel);
container.add(designationCodeLabel);
container.add(nameLabel);
container.add(searchErrorLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(clearSearchFieldButton);
container.add(scrollPane);
employeePanel=new EmployeePanel();
container.add(employeePanel);

int width=700;
int height=700;
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
setLocation(((d.width/2)-(width/2)),((d.height/2)-(height/2)));
setSize(width,height);

/*to align components a.setBounds(leftAlign,fromTopAlign,widthOfComponents,heightOfComponent)*/
int left=0;
int top=0;
designationLabel.setBounds(left+10,top+5,400,25);
designationCodeLabel.setBounds(left+10+120+10,top+5,400,25);
nameLabel.setBounds(left+10,top+25,400,25);
searchErrorLabel.setBounds(left+10+20+280+200,top+20+28,400,10);
searchLabel.setBounds(left+10,top+20+40,300,20);
searchTextField.setBounds(left+10+70,top+20+40,500,20);
clearSearchFieldButton.setBounds(left+10+70+500+20,top+20+40,20,20);
scrollPane.setBounds(left+10,top+20+20+45,665,250);
employeePanel.setBounds(left+10,top+20+20+35+280,665,280);
}

private void addListeners()
{
employeeTable.getSelectionModel().addListSelectionListener(this);
searchTextField.getDocument().addDocumentListener(this);
clearSearchFieldButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
searchTextField.setText("");
searchTextField.requestFocus();
}
});
}

public void searchEmployee()
{
searchErrorLabel.setText("");
String title=searchTextField.getText().trim();
if(title.length()==0)return;
int rowIndex;
try
{
rowIndex=employeeModel.indexOfTitle(title,true);
}
catch(BLException be)
{
searchErrorLabel.setText("Not Found");
return;
}
employeeTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=employeeTable.getCellRect(rowIndex,0,true);
employeeTable.scrollRectToVisible(rectangle);
}

public void changedUpdate(DocumentEvent de)
{
searchEmployee();
}

public void removeUpdate(DocumentEvent de)
{
searchEmployee();
}

public void insertUpdate(DocumentEvent de)
{
searchEmployee();
}

public void valueChanged(ListSelectionEvent lse)
{
int selectedRowIndex=employeeTable.getSelectedRow();
try
{
EmployeeInterface employee=employeeModel.getEmployeeAt(selectedRowIndex);
employeePanel.setEmployee(employee);
}
catch(BLException blexception)
{
employeePanel.clearEmployee();
}
}

public void setViewMode()
{
this.mode=MODE.VIEW;
if(employeeModel.getRowCount()==0)
{
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
employeeTable.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
clearSearchFieldButton.setEnabled(true);
employeeTable.setEnabled(true);
}
}

public void setAddMode()
{
this.mode=MODE.ADD;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
employeeTable.setEnabled(false);
}

public void setEditMode()
{
this.mode=MODE.EDIT;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
employeeTable.setEnabled(false);
}

public void setDeleteMode()
{
this.mode=MODE.DELETE;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
employeeTable.setEnabled(false);
}

public void setExportToPDFMode()
{
this.mode=MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
clearSearchFieldButton.setEnabled(false);
employeeTable.setEnabled(false);
}


//innerClass
class EmployeePanel extends JPanel
{
private JLabel titleCaptionLabel;
private JLabel nameCaptionLabel;
private JLabel designationCaptionLabel;
private JLabel dateOfBirthCaptionLabel;
private JLabel genderCaptionLabel;
private JLabel isIndianCaptionLabel;
private JLabel basicSalaryCaptionLabel;
private JLabel panNumberCaptionLabel;
private JLabel aadharCardNumberCaptionLabel;

private JLabel employeeIdLabel;
private JLabel nameLabel;
private JLabel designationLabel;
private JLabel dateOfBirthLabel;
private JLabel genderLabel;
private JLabel isIndianLabel;
private JLabel basicSalaryLabel;
private JLabel panNumberLabel;
private JLabel aadharCardNumberLabel;

private JTextField nameField;
private JTextField designationField;
private JTextField dateOfBirthField;
private JTextField genderField;
private JTextField isIndianField;
private JTextField basicSalaryField;
private JTextField panNumberField;
private JTextField aadharCardNumberField;

private JButton clearTitleTextFieldButton;
private JButton addButton;
private JButton editButton;
private JButton cancelButton;
private JButton deleteButton;
private JButton exportToPDFButton;
private JPanel buttonsPanel;
private EmployeeInterface employee;

EmployeePanel()
{
setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
initComponents();
setApperance();
addListeners();
}

private void initComponents()
{
employee=null;
titleCaptionLabel=new JLabel("Employee");
nameCaptionLabel=new JLabel("Name : ");
designationCaptionLabel=new JLabel("Designation Code : ");
dateOfBirthCaptionLabel=new JLabel("Date of Birth : ");
genderCaptionLabel=new JLabel("Gender : ");
isIndianCaptionLabel=new JLabel("Is Indian : ");
basicSalaryCaptionLabel=new JLabel("Basic Salary : ");
panNumberCaptionLabel=new JLabel("PAN Number : ");
aadharCardNumberCaptionLabel=new JLabel("Aadharcard Number");

employeeIdLabel=new JLabel("");
nameLabel=new JLabel("");
designationLabel=new JLabel("");
dateOfBirthLabel=new JLabel("");
genderLabel=new JLabel("");
isIndianLabel=new JLabel("");
basicSalaryLabel=new JLabel("");
panNumberLabel=new JLabel("");
aadharCardNumberLabel=new JLabel("");

nameField=new JTextField();
designationField=new JTextField();
dateOfBirthField=new JTextField();
genderField=new JTextField();
isIndianField=new JTextField();
basicSalaryField=new JTextField();
panNumberField=new JTextField();
aadharCardNumberField=new JTextField();

clearTitleTextFieldButton=new JButton(crossIcon);
buttonsPanel=new JPanel();
addButton=new JButton(addIcon);
editButton=new JButton(editIcon);
cancelButton=new JButton(cancelIcon);
deleteButton=new JButton(deleteIcon);
exportToPDFButton=new JButton(pdfIcon);
}

private void setApperance()
{
Font captionFont=new Font("Verdana",Font.BOLD,16);
Font dataCaptionFont=new Font("Verdana",Font.BOLD,14);
Font dataFont=new Font("Verdana",Font.PLAIN,14);
titleCaptionLabel.setFont(captionFont);
nameCaptionLabel.setFont(dataCaptionFont);
designationCaptionLabel.setFont(dataCaptionFont);
dateOfBirthCaptionLabel.setFont(dataCaptionFont);
genderCaptionLabel.setFont(dataCaptionFont);
isIndianCaptionLabel.setFont(dataCaptionFont);
basicSalaryCaptionLabel.setFont(dataCaptionFont);
panNumberCaptionLabel.setFont(dataCaptionFont);
aadharCardNumberCaptionLabel.setFont(dataCaptionFont);

employeeIdLabel.setFont(dataFont);
nameLabel.setFont(dataFont);
nameField.setFont(dataFont);
designationLabel.setFont(dataFont);
designationField.setFont(dataFont);
dateOfBirthLabel.setFont(dataFont);
dateOfBirthField.setFont(dataFont);
genderLabel.setFont(dataFont);
genderField.setFont(dataFont);
isIndianLabel.setFont(dataFont);
isIndianField.setFont(dataFont);
basicSalaryLabel.setFont(dataFont);
basicSalaryField.setFont(dataFont);
panNumberLabel.setFont(dataFont);
panNumberField.setFont(dataFont);
aadharCardNumberLabel.setFont(dataFont);
aadharCardNumberField.setFont(dataFont);
setLayout(null);
int lm=0;
int tm=0;

titleCaptionLabel.setBounds(lm+10,tm+2,200,30);
nameCaptionLabel.setBounds(lm+10,tm+23,200,50);
employeeIdLabel.setBounds(lm+10+200+10+5,tm+5,400,20);
nameLabel.setBounds(lm+10+200+10,tm+38,400,20);
nameField.setBounds(lm+10+200+10+5,tm+40,400,17);

designationCaptionLabel.setBounds(lm+10,tm+43,200,50);
designationLabel.setBounds(lm+10+200+10,tm+59,400,20);
designationField.setBounds(lm+10+200+10+5,tm+60,400,17);

dateOfBirthCaptionLabel.setBounds(lm+10,tm+63,200,50);
dateOfBirthLabel.setBounds(lm+10+200+10,tm+80,400,20);
dateOfBirthField.setBounds(lm+10+200+10+5,tm+80,400,17);

genderCaptionLabel.setBounds(lm+10,tm+83,200,50);
genderLabel.setBounds(lm+10+200+10,tm+98,400,20);
genderField.setBounds(lm+10+200+10+5,tm+100,400,17);

isIndianCaptionLabel.setBounds(lm+10,tm+103,200,50);
isIndianLabel.setBounds(lm+10+200+10,tm+118,400,20);
isIndianField.setBounds(lm+10+200+10+5,tm+120,400,17);

basicSalaryCaptionLabel.setBounds(lm+10,tm+123,200,50);
basicSalaryLabel.setBounds(lm+10+200+10,tm+138,400,20);
basicSalaryField.setBounds(lm+10+200+10+5,tm+140,400,17);

panNumberCaptionLabel.setBounds(lm+10,tm+143,200,50);
panNumberLabel.setBounds(lm+10+200+10,tm+159,400,20);
panNumberField.setBounds(lm+10+200+10+5,tm+160,400,17);

aadharCardNumberCaptionLabel.setBounds(lm+10,tm+163,220,50);
aadharCardNumberLabel.setBounds(lm+10+200+10,tm+181,400,20);
aadharCardNumberField.setBounds(lm+10+200+10+5,tm+180,400,17);
clearTitleTextFieldButton.setBounds(lm+10+200+10+400+10,tm+181,15,15);

buttonsPanel.setBounds(150,tm+50+155,360,68);
buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
addButton.setBounds(15,9,50,50);
editButton.setBounds(15+50+20,9,50,50);
cancelButton.setBounds(15+50+20+50+20,9,50,50);
deleteButton.setBounds(15+50+20+50+20+50+20,9,50,50);
exportToPDFButton.setBounds(15+50+20+50+20+50+20+50+20,9,50,50);

buttonsPanel.setLayout(null);
buttonsPanel.add(addButton);
buttonsPanel.add(editButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(exportToPDFButton);
add(titleCaptionLabel);
add(nameCaptionLabel);
add(employeeIdLabel);
add(nameLabel);
add(nameField);

add(designationCaptionLabel);
add(designationLabel);
add(designationField);

add(dateOfBirthCaptionLabel);
add(dateOfBirthLabel);
add(dateOfBirthField);

add(genderCaptionLabel);
add(genderLabel);
add(genderField);

add(isIndianCaptionLabel);
add(isIndianLabel);
add(isIndianField);

add(basicSalaryCaptionLabel);
add(basicSalaryLabel);
add(basicSalaryField);

add(panNumberCaptionLabel);
add(panNumberLabel);
add(panNumberField);

add(aadharCardNumberCaptionLabel);
add(aadharCardNumberLabel);
add(clearTitleTextFieldButton);
add(aadharCardNumberField);

add(buttonsPanel);
}

public void setEmployee(EmployeeInterface employee)
{
this.employee=employee;
employeeIdLabel.setText(employee.getEmployeeId());
nameLabel.setText(employee.getName().toUpperCase());
designationLabel.setText(String.valueOf(employee.getDesignation().getCode()));
dateOfBirthLabel.setText(sdf.format(employee.getDateOfBirth()));
genderLabel.setText(String.valueOf(employee.getGender()));
isIndianLabel.setText(String.valueOf(employee.getIsIndian()));
basicSalaryLabel.setText(employee.getBasicSalary().toString());
panNumberLabel.setText(employee.getPANNumber().toUpperCase());
aadharCardNumberLabel.setText(employee.getAadharCardNumber().toUpperCase());
}

public void clearEmployee()
{
this.employee=null;
employeeIdLabel.setText("");
nameLabel.setText("");
designationLabel.setText("");
dateOfBirthLabel.setText("");
genderLabel.setText("");
isIndianLabel.setText("");
basicSalaryLabel.setText("");
panNumberLabel.setText("");
aadharCardNumberLabel.setText("");
}

void setViewMode()
{
EmployeeUI.this.setViewMode();
this.addButton.setIcon(addIcon);
this.editButton.setIcon(editIcon);

this.nameCaptionLabel.setVisible(true);
this.designationCaptionLabel.setVisible(true);
this.dateOfBirthCaptionLabel.setVisible(true);
this.genderCaptionLabel.setVisible(true);
this.isIndianCaptionLabel.setVisible(true);
this.basicSalaryCaptionLabel.setVisible(true);
this.panNumberCaptionLabel.setVisible(true);
this.aadharCardNumberCaptionLabel.setVisible(true);

this.nameField.setVisible(false);
this.designationField.setVisible(false);
this.dateOfBirthField.setVisible(false);
this.genderField.setVisible(false);
this.isIndianField.setVisible(false);
this.basicSalaryField.setVisible(false);
this.panNumberField.setVisible(false);
this.aadharCardNumberField.setVisible(false);

this.titleCaptionLabel.setText("Employee");

this.employeeIdLabel.setVisible(false);
this.nameLabel.setVisible(true);
this.designationLabel.setVisible(true);
this.dateOfBirthLabel.setVisible(true);
this.genderLabel.setVisible(true);
this.isIndianLabel.setVisible(true);
this.basicSalaryLabel.setVisible(true);
this.panNumberLabel.setVisible(true);
this.aadharCardNumberLabel.setVisible(true);

this.clearTitleTextFieldButton.setVisible(false);
this.addButton.setEnabled(true);
this.cancelButton.setEnabled(false);
if(employeeModel.getRowCount()<0)
{
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}
else
{
this.editButton.setEnabled(true);
this.deleteButton.setEnabled(true);
this.exportToPDFButton.setEnabled(true);
}
}

void setAddMode()
{
EmployeeUI.this.setAddMode();
this.nameField.setText("");
this.designationField.setText("");
this.dateOfBirthField.setText("");
this.genderField.setText("");
this.isIndianField.setText("");
this.basicSalaryField.setText("");
this.panNumberField.setText("");
this.aadharCardNumberField.setText("");

this.titleCaptionLabel.setText("Employee");

this.addButton.setIcon(saveIcon);
this.employeeIdLabel.setVisible(false);
this.nameLabel.setVisible(false);
this.nameField.setVisible(true);
this.designationField.setVisible(true);
this.designationLabel.setVisible(false);
this.dateOfBirthField.setVisible(true);
this.dateOfBirthLabel.setVisible(false);
this.genderField.setVisible(true);
this.genderLabel.setVisible(false);
this.isIndianField.setVisible(true);
this.isIndianLabel.setVisible(false);
this.basicSalaryField.setVisible(true);
this.basicSalaryLabel.setVisible(false);
this.panNumberField.setVisible(true);
this.panNumberLabel.setVisible(false);
this.aadharCardNumberField.setVisible(true);
this.aadharCardNumberLabel.setVisible(false);
this.clearTitleTextFieldButton.setVisible(true);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(true);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}

void setEditMode()
{
if(employeeTable.getSelectedRow()<0 || employeeTable.getSelectedRow()>=employeeModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select employee to edit");
return;
}
EmployeeUI.this.setEditMode();
this.nameField.setText(this.employee.getName());
this.designationField.setText(String.valueOf(this.employee.getDesignation().getCode()));
this.dateOfBirthField.setText(sdf.format(this.employee.getDateOfBirth()));
this.genderField.setText(String.valueOf(this.employee.getGender()));
this.isIndianField.setText(String.valueOf(this.employee.getIsIndian()));
this.basicSalaryField.setText(this.employee.getBasicSalary().toString());
this.panNumberField.setText(this.employee.getPANNumber());
this.aadharCardNumberField.setText(this.employee.getAadharCardNumber());

this.titleCaptionLabel.setText("Employee Id : ");

this.employeeIdLabel.setVisible(true);
this.nameLabel.setVisible(false);
this.designationLabel.setVisible(false);
this.dateOfBirthLabel.setVisible(false);
this.genderLabel.setVisible(false);
this.isIndianLabel.setVisible(false);
this.basicSalaryLabel.setVisible(false);
this.panNumberLabel.setVisible(false);
this.aadharCardNumberLabel.setVisible(false);

this.clearTitleTextFieldButton.setVisible(true);
this.nameField.setVisible(true);
this.designationField.setVisible(true);
this.dateOfBirthField.setVisible(true);
this.genderField.setVisible(true);
this.isIndianField.setVisible(true);
this.basicSalaryField.setVisible(true);
this.panNumberField.setVisible(true);
this.aadharCardNumberField.setVisible(true);
this.addButton.setEnabled(false);
this.cancelButton.setEnabled(true);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
this.editButton.setIcon(saveIcon);
}

void setDeleteMode()
{
if(employeeTable.getSelectedRow()<0 || employeeTable.getSelectedRow()>=employeeModel.getRowCount())
{
JOptionPane.showMessageDialog(this,"Select employee to delete");
return;
}
EmployeeUI.this.setDeleteMode();
this.addButton.setEnabled(false);
this.editButton.setEnabled(false);
this.cancelButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
removeEmployee();
EmployeeUI.this.setViewMode();
this.setViewMode();
}

private boolean addEmployee()
{
String name=nameField.getText().trim();
if(name.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee name required","Warning",JOptionPane.WARNING_MESSAGE);
nameField.requestFocus();
return false;
}

String designationCode=designationField.getText().trim();
if(designationCode.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee designation code required","Warning",JOptionPane.WARNING_MESSAGE);
designationField.requestFocus();
return false;
}
int codeOfDesignation;
try
{
codeOfDesignation=Integer.parseInt(designationCode);
}catch(NumberFormatException nfe)
{
JOptionPane.showMessageDialog(this,"Invalid designation code","Warning",JOptionPane.WARNING_MESSAGE);
designationField.requestFocus();
return false;
}

String dateOfBirthFieldText=dateOfBirthField.getText().trim();
if(dateOfBirthFieldText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee date of birth required","Warning",JOptionPane.WARNING_MESSAGE);
dateOfBirthField.requestFocus();
return false;
}
Date dateOfBirth;
try
{
dateOfBirth=sdf.parse(dateOfBirthFieldText);
}catch(ParseException parseException)
{
JOptionPane.showMessageDialog(this,"Invalid date of birth","Warning",JOptionPane.WARNING_MESSAGE);
dateOfBirthField.requestFocus();
return false;
}

String genderText=genderField.getText().trim();
if(genderText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee gender required","Warning",JOptionPane.WARNING_MESSAGE);
genderField.requestFocus();
return false;
}
char gender=genderText.charAt(0);

String isIndianText=isIndianField.getText().trim();
if(isIndianText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee is Indian?","Warning",JOptionPane.WARNING_MESSAGE);
isIndianField.requestFocus();
return false;
}
boolean isIndian=Boolean.parseBoolean(isIndianText);

String basicSalaryText=basicSalaryField.getText().trim();
if(basicSalaryText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee basic salary required","Warning",JOptionPane.WARNING_MESSAGE);
basicSalaryField.requestFocus();
return false;
}
BigDecimal basicSalary;
try
{
basicSalary=new BigDecimal(basicSalaryText);
}catch(NumberFormatException nfe)
{
JOptionPane.showMessageDialog(this,"Invalid basic salary","Warning",JOptionPane.WARNING_MESSAGE);
basicSalaryField.requestFocus();
return false;
}

String panNumber=panNumberField.getText().trim();
if(panNumber.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee PAN number required","Warning",JOptionPane.WARNING_MESSAGE);
panNumberField.requestFocus();
return false;
}
else 
{
if(panNumber.length()>10)
{
JOptionPane.showMessageDialog(this,"Invalid PAN Number(Length of PAN Number cannot be greater than 10)","Warning",JOptionPane.WARNING_MESSAGE);
panNumberField.requestFocus();
return false;
}
}

String aadharCardNumber=aadharCardNumberField.getText().trim();
if(aadharCardNumber.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee Aadhar Card number required","Warning",JOptionPane.WARNING_MESSAGE);
aadharCardNumberField.requestFocus();
return false;
}
else
{
if(aadharCardNumber.length()>10)
{
JOptionPane.showMessageDialog(this,"Invalid Aadhar Card Number(Length of Aadhar Card Number cannot be greater than 10)","Warning",JOptionPane.WARNING_MESSAGE);
aadharCardNumberField.requestFocus();
return false;
}
}

EmployeeInterface e=new Employee();
e.setName(name);
DesignationInterface designationInterface=new Designation();
designationInterface.setCode(codeOfDesignation);
e.setDesignation(designationInterface);
e.setDateOfBirth(dateOfBirth);
e.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(isIndian);
e.setBasicSalary(basicSalary);
e.setPANNumber(panNumber);
e.setAadharCardNumber(aadharCardNumber);
try
{
employeeModel.add(e);
int rowIndex=0;
try
{
rowIndex=employeeModel.indexOfEmployee(e);
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
employeeTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=employeeTable.getCellRect(rowIndex,0,true);
employeeTable.scrollRectToVisible(rectangle);
return true;
}
catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}
List<String> properties=blException.getProperties();
for(String property:properties)
{
JOptionPane.showMessageDialog(this,blException.getException(property));
}
nameField.requestFocus();
return false;
}
}

private boolean updateEmployee()
{
String employeeId=employeeIdLabel.getText().trim();

String name=nameField.getText().trim();
if(name.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee name required","Warning",JOptionPane.WARNING_MESSAGE);
nameField.requestFocus();
return false;
}

String designationCode=designationField.getText().trim();
if(designationCode.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee designation code required","Warning",JOptionPane.WARNING_MESSAGE);
designationField.requestFocus();
return false;
}
int codeOfDesignation;
try
{
codeOfDesignation=Integer.parseInt(designationCode);
}catch(NumberFormatException nfe)
{
JOptionPane.showMessageDialog(this,"Invalid designation code","Warning",JOptionPane.WARNING_MESSAGE);
designationField.requestFocus();
return false;
}

String dateOfBirthFieldText=dateOfBirthField.getText().trim();
if(dateOfBirthFieldText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee date of birth required","Warning",JOptionPane.WARNING_MESSAGE);
dateOfBirthField.requestFocus();
return false;
}
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
Date dateOfBirth;
try
{
dateOfBirth=sdf.parse(dateOfBirthFieldText);
}catch(ParseException parseException)
{
JOptionPane.showMessageDialog(this,"Invalid date of birth","Warning",JOptionPane.WARNING_MESSAGE);
dateOfBirthField.requestFocus();
return false;
}


String genderText=genderField.getText().trim();
if(genderText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee gender required","Warning",JOptionPane.WARNING_MESSAGE);
genderField.requestFocus();
return false;
}
char gender=genderText.charAt(0);

String isIndianText=isIndianField.getText().trim();
if(isIndianText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee is Indian?","Warning",JOptionPane.WARNING_MESSAGE);
isIndianField.requestFocus();
return false;
}
boolean isIndian=Boolean.parseBoolean(isIndianText);

String basicSalaryText=basicSalaryField.getText().trim();
if(basicSalaryText.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee basic salary required","Warning",JOptionPane.WARNING_MESSAGE);
basicSalaryField.requestFocus();
return false;
}
BigDecimal basicSalary;
try
{
basicSalary=new BigDecimal(basicSalaryText);
}catch(NumberFormatException nfe)
{
JOptionPane.showMessageDialog(this,"Invalid basic salary","Warning",JOptionPane.WARNING_MESSAGE);
basicSalaryField.requestFocus();
return false;
}

String panNumber=panNumberField.getText().trim();
if(panNumber.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee PAN number required","Warning",JOptionPane.WARNING_MESSAGE);
panNumberField.requestFocus();
return false;
}
else 
{
if(panNumber.length()>10)
{
JOptionPane.showMessageDialog(this,"Invalid PAN Number(Length of PAN Number cannot be greater than 10)","Warning",JOptionPane.WARNING_MESSAGE);
panNumberField.requestFocus();
return false;
}
}

String aadharCardNumber=aadharCardNumberField.getText().trim();
if(aadharCardNumber.length()==0)
{
JOptionPane.showMessageDialog(this,"Employee Aadhar Card number required","Warning",JOptionPane.WARNING_MESSAGE);
aadharCardNumberField.requestFocus();
return false;
}
else
{
if(aadharCardNumber.length()>10)
{
JOptionPane.showMessageDialog(this,"Invalid Aadhar Card Number(Length of Aadhar Card Number cannot be greater than 10)","Warning",JOptionPane.WARNING_MESSAGE);
aadharCardNumberField.requestFocus();
return false;
}
}

EmployeeInterface d=new Employee();
d.setEmployeeId(employeeId);
d.setName(name);
DesignationInterface designationInterface=new Designation();
designationInterface.setCode(codeOfDesignation);
d.setDesignation(designationInterface);
d.setDateOfBirth(dateOfBirth);
d.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
d.setIsIndian(isIndian);
d.setBasicSalary(basicSalary);
d.setPANNumber(panNumber);
d.setAadharCardNumber(aadharCardNumber);
try
{
employeeModel.update(d);
int rowIndex=0;
try
{
rowIndex=employeeModel.indexOfEmployee(d);
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
employeeTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle=employeeTable.getCellRect(rowIndex,0,true);
employeeTable.scrollRectToVisible(rectangle);
return true;
}
catch(BLException blException)
{
if(blException.hasGenericException())
{
JOptionPane.showMessageDialog(this,blException.getGenericException());
}
List<String> properties=blException.getProperties();
for(String property:properties)
{
JOptionPane.showMessageDialog(this,blException.getException(property));
}
nameField.requestFocus();
return false;
}
}

private void removeEmployee()
{
try
{
String name=this.employee.getName();
int selectedOption=JOptionPane.showConfirmDialog(this,"Delete "+ name+" ?","Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION)return;
employeeModel.remove(this.employee.getEmployeeId());
JOptionPane.showMessageDialog(this,name+" deleted");
//this.clearEmployee();
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
this.cancelButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
setViewMode();
}
});

this.addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
if(mode==MODE.VIEW)
{
setAddMode();
}
else 
{
if(addEmployee())
{
setViewMode();
}
}
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
if(updateEmployee())
{
setViewMode();
}
}
}
});

this.clearTitleTextFieldButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
nameField.setText("");
designationField.setText("");
dateOfBirthField.setText("");
genderField.setText("");
isIndianField.setText("");
basicSalaryField.setText("");
panNumberField.setText("");
aadharCardNumberField.setText("");
}
});

this.deleteButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
setDeleteMode();
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
int selectedOption=jfc.showSaveDialog(EmployeeUI.this);
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
JOptionPane.showMessageDialog(EmployeeUI.this,"Incorrect path : "+file.getAbsolutePath());
return;
}
employeeModel.exportToPDF(file);
JOptionPane.showMessageDialog(EmployeeUI.this,"File saved at path : "+file.getAbsolutePath());
}
catch(BLException blexception)
{
if(blexception.hasGenericException())
{
JOptionPane.showMessageDialog(EmployeeUI.this,blexception.getGenericException());
}
}
catch(Exception e)
{
System.out.println(e.getMessage());
}
}
}
});

}
}//inner class ends here

}




