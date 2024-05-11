package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import java.util.*;
import javax.swing.table.*;
import java.io.*;
import java.text.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.image.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.borders.*;

public class EmployeeModel extends AbstractTableModel
{
private java.util.List<EmployeeInterface>employees;
private EmployeeManagerInterface employeeManager;
private String[] columnTitle;
private int codeOfDesignation;


public EmployeeModel(int codeOfDesignation)
{
this.codeOfDesignation=codeOfDesignation;
this.populateDataStructure();
}

private void populateDataStructure()
{
this.columnTitle=new String[2];
this.columnTitle[0]="Employee Id";
this.columnTitle[1]="Employee";
Set<EmployeeInterface> blemployees=null;
try
{
employeeManager =EmployeeManager.getEmployeeManager();
blemployees=employeeManager.getEmployeesByDesignationCode(codeOfDesignation);
}catch(BLException blException)
{
System.out.println(blException.getMessage());
}
this.employees=new LinkedList<>();
for(EmployeeInterface Employee:blemployees)
{
this.employees.add(Employee);
}
Collections.sort(this.employees,new Comparator<EmployeeInterface>(){
public int compare(EmployeeInterface left,EmployeeInterface right)
{
return left.getEmployeeId().toUpperCase().compareTo(right.getEmployeeId().toUpperCase());
}
});
}

public int getRowCount()
{
return employees.size();
}
public int getColumnCount()
{
return this.columnTitle.length;
}
public String getColumnName(int columnIndex)
{
return columnTitle[columnIndex];
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0) return this.employees.get(rowIndex).getEmployeeId();
return this.employees.get(rowIndex).getName();
} 	 	 	
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0)return String.class;
return String.class;
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}
 
public int indexOfEmployee(EmployeeInterface employee)throws BLException
{
Iterator<EmployeeInterface> iterator=this.employees.iterator();
EmployeeInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(d.equals(employee))return index;
index++;
}
BLException blexception=new BLException();
blexception.setGenericException("Invalid employee Id : "+employee.getEmployeeId());
throw blexception;
}

public int indexOfTitle(String name,boolean partialLeftSearch)throws BLException
{
Iterator<EmployeeInterface> iterator=this.employees.iterator();
EmployeeInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(partialLeftSearch)
{
if(d.getName().toUpperCase().startsWith(name.toUpperCase()))return index;
}
else
{
if(d.getName().equalsIgnoreCase(name))return index;
}
index++;
}
BLException blexception=new BLException();
blexception.setGenericException("Invalid title : "+name);
throw blexception;
}

public EmployeeInterface getEmployeeAt(int index)throws BLException
{
if(index<0 || index>=this.employees.size())
{
BLException blexception=new BLException();
blexception.setGenericException("Invalid index : "+index);
throw blexception;
}
return this.employees.get(index);
}

//application specific methods
public void add(EmployeeInterface employee)throws BLException
{
employeeManager.addEmployee(employee);
this.employees.add(employee);
Collections.sort(this.employees,new Comparator<EmployeeInterface>(){
public int compare(EmployeeInterface left,EmployeeInterface right)
{
return left.getEmployeeId().toUpperCase().compareTo(right.getEmployeeId().toUpperCase());
}
});
fireTableDataChanged();
}

public void update(EmployeeInterface employee)throws BLException
{
employeeManager.updateEmployee(employee);
this.employees.remove(indexOfEmployee(employee));
this.employees.add(employee);
Collections.sort(this.employees,new Comparator<EmployeeInterface>(){
public int compare(EmployeeInterface left,EmployeeInterface right)
{
return left.getEmployeeId().toUpperCase().compareTo(right.getEmployeeId().toUpperCase());
}
});
fireTableDataChanged();
}

public void remove(String employeeId)throws BLException
{
employeeManager.deleteEmployee(employeeId);
Iterator<EmployeeInterface> iterator=this.employees.iterator();
int index=0;
while(iterator.hasNext())
{
if(iterator.next().getEmployeeId()==employeeId)break;
index++;
}
if(index==this.employees.size())
{
BLException blexception=new BLException();
blexception.setGenericException("Invalid employee Id : "+employeeId);
throw blexception;
}
this.employees.remove(index);
fireTableDataChanged();
}

public void exportToPDF(File file)throws BLException
{
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
try
{
if(file.exists())file.delete();
PdfWriter pdfWriter=new PdfWriter(file);
PdfDocument pdfDocument=new PdfDocument(pdfWriter);
Document document=new Document(pdfDocument);

Image logo=new Image(ImageDataFactory.create(this.getClass().getResource("/icons/logos.jpeg")));
logo.setWidth(35);
logo.setHeight(35);
Paragraph logoPara=new Paragraph();
logoPara.add(logo);
Paragraph companyNamePara=new Paragraph("JVSC Cooperation");
PdfFont companyNameFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
companyNamePara.setFont(companyNameFont);
companyNamePara.setFontSize(18);

PdfFont pageNumberFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont dataFont=PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
PdfFont titleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

Paragraph reportTitlePara=new Paragraph("List of employees");
PdfFont reportTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
reportTitlePara.setFont(reportTitleFont);
reportTitlePara.setFontSize(15);

Paragraph columnTitle1=new Paragraph("E.Id");
columnTitle1.setFont(titleFont);
columnTitle1.setFontSize(14);
Paragraph columnTitle2=new Paragraph("Employee");
columnTitle2.setFont(titleFont);
columnTitle2.setFontSize(14);
Paragraph columnTitle3=new Paragraph("Designation");
columnTitle3.setFont(titleFont);
columnTitle3.setFontSize(14);
Paragraph columnTitle4=new Paragraph("DOB");
columnTitle4.setFont(titleFont);
columnTitle4.setFontSize(14);
Paragraph columnTitle5=new Paragraph("Gender");
columnTitle5.setFont(titleFont);
columnTitle5.setFontSize(14);
Paragraph columnTitle6=new Paragraph("Indian");
columnTitle6.setFont(titleFont);
columnTitle6.setFontSize(14);
Paragraph columnTitle7=new Paragraph("Basic Salary");
columnTitle7.setFont(titleFont);
columnTitle7.setFontSize(14);
Paragraph columnTitle8=new Paragraph("PAN Number");
columnTitle8.setFont(titleFont);
columnTitle8.setFontSize(14);
Paragraph columnTitle9=new Paragraph("Aadhar Card Number");
columnTitle9.setFont(titleFont);
columnTitle9.setFontSize(14);

float topTableColumnWidth[]={2,5};
float dataTableColumnWidth[]={14,25,16,12, 10, 3, 15, 16, 16};
Paragraph dataPara;
Paragraph pageNumberPara;
Paragraph footerPara;
Table topTable=null;
Table dataTable=null;
Table pageNumberTable;
Cell cell=null;
int sNo=0;
int pageNumber=0;
int pageSize=10;
int r=0;
int numberOfPages=this.employees.size()/pageSize;
if(this.employees.size()%pageSize!=0)numberOfPages++;
EmployeeInterface Employee;
boolean newPage=true;
while(r<this.employees.size())
{
if(newPage==true)
{
//creating header
pageNumber++;
topTable=new Table(UnitValue.createPercentArray(topTableColumnWidth));
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(logoPara);
topTable.addCell(cell);
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(companyNamePara);
cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
topTable.addCell(cell);
document.add(topTable);

pageNumberTable=new Table(1);
pageNumberTable.setWidth(UnitValue.createPercentValue(100));
pageNumberPara=new Paragraph("Page : "+pageNumber+"/"+numberOfPages);
pageNumberPara.setFont(pageNumberFont);
pageNumberPara.setFontSize(13);
cell=new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(pageNumberPara);
cell.setTextAlignment(TextAlignment.RIGHT);
pageNumberTable.addCell(cell);
document.add(pageNumberTable);

dataTable=new Table(UnitValue.createPercentArray(dataTableColumnWidth));
dataTable.setWidth(UnitValue.createPercentValue(100));
cell=new Cell(1,9);
cell.add(reportTitlePara);
cell.setTextAlignment(TextAlignment.CENTER);
dataTable.addHeaderCell(cell);
dataTable.addHeaderCell(columnTitle1);
dataTable.addHeaderCell(columnTitle2);
dataTable.addHeaderCell(columnTitle3);
dataTable.addHeaderCell(columnTitle4);
dataTable.addHeaderCell(columnTitle5);
dataTable.addHeaderCell(columnTitle6);
dataTable.addHeaderCell(columnTitle7);
dataTable.addHeaderCell(columnTitle8);
dataTable.addHeaderCell(columnTitle9);
newPage=false;
}
sNo++;
Employee=this.employees.get(r);

cell=new Cell();
dataPara=new Paragraph(Employee.getEmployeeId());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
cell.setTextAlignment(TextAlignment.RIGHT);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(Employee.getName());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(Employee.getDesignation().getTitle());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(sdf.format(Employee.getDateOfBirth()));
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(String.valueOf(Employee.getGender()));
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(String.valueOf(Employee.getIsIndian()));
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(Employee.getBasicSalary().toString());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(Employee.getPANNumber());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(Employee.getAadharCardNumber());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);

r++;

if(sNo%pageSize==0 || r==this.employees.size())
{
document.add(dataTable);
footerPara=new Paragraph("Software by : Chhavi Chouhan");
footerPara.setFont(dataFont);
footerPara.setFontSize(12);
document.add(footerPara);
if(r<this.employees.size())
{
document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage=true;
}
}
}
document.close();
}
catch(Exception exception)
{
BLException blexception;
blexception=new BLException();
blexception.setGenericException(exception.getMessage());
throw blexception;
}
}
}