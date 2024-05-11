package com.thinking.machines.hr.pl.model;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import java.util.*;
import javax.swing.table.*;
import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.image.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.borders.*;

public class DesignationModel extends AbstractTableModel
{
private java.util.List<DesignationInterface>designations;
private DesignationManagerInterface designationManager;
private String[] columnTitle;

public DesignationModel()
{
this.populateDataStructure();
}

private void populateDataStructure()
{
this.columnTitle=new String[2];
this.columnTitle[0]="S.No.";
this.columnTitle[1]="Designation";
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
}
Set<DesignationInterface>blDesignations=designationManager.getDesignations();
this.designations=new LinkedList<>();
for(DesignationInterface designation:blDesignations)
{
this.designations.add(designation);
}
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}

public int getRowCount()
{
return designations.size();
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
if(columnIndex==0) return rowIndex+1;
return this.designations.get(rowIndex).getTitle();
} 	 	 	
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0)return Integer.class;
return String.class;
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
} 

//application specific methods

public void add(DesignationInterface designation)throws BLException
{
designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}

public void update(DesignationInterface designation)throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}

public void remove(int code)throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> iterator=this.designations.iterator();
int index=0;
while(iterator.hasNext())
{
if(iterator.next().getCode()==code)break;
index++;
}
if(index==this.designations.size())
{
BLException blexception=new BLException();
blexception.setGenericException("Invalid designation code : "+code);
throw blexception;
}
this.designations.remove(index);
fireTableDataChanged();
}


public int indexOfDesignation(DesignationInterface designation)throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(d.equals(designation))return index;
index++;
}
BLException blexception=new BLException();
blexception.setGenericException("Invalid code : "+designation.getTitle());
throw blexception;
}

public int indexOfTitle(String title,boolean partialLeftSearch)throws BLException
{
Iterator<DesignationInterface> iterator=this.designations.iterator();
DesignationInterface d;
int index=0;
while(iterator.hasNext())
{
d=iterator.next();
if(partialLeftSearch)
{
if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))return index;
}
else
{
if(d.getTitle().equalsIgnoreCase(title))return index;
}
index++;
}
BLException blexception=new BLException();
blexception.setGenericException("Invalid title : "+title);
throw blexception;
}

public DesignationInterface getDesignationAt(int index)throws BLException
{
if(index<0 || index>=this.designations.size())
{
BLException blexception=new BLException();
blexception.setGenericException("Invalid index : "+index);
throw blexception;
}
return this.designations.get(index);
}

public void exportToPDF(File file)throws BLException
{
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

Paragraph reportTitlePara=new Paragraph("List of Designations");
PdfFont reportTitleFont=PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
reportTitlePara.setFont(reportTitleFont);
reportTitlePara.setFontSize(15);

Paragraph columnTitle1=new Paragraph("S.NO");
columnTitle1.setFont(titleFont);
columnTitle1.setFontSize(14);
Paragraph columnTitle2=new Paragraph("Designation");
columnTitle2.setFont(titleFont);
columnTitle2.setFontSize(14);

float topTableColumnWidth[]={2,5};
float dataTableColumnWidth[]={2,5};
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
int numberOfPages=this.designations.size()/pageSize;
if(this.designations.size()%pageSize!=0)numberOfPages++;
DesignationInterface designation;
boolean newPage=true;
while(r<this.designations.size())
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
cell=new Cell(1,2);
cell.add(reportTitlePara);
cell.setTextAlignment(TextAlignment.CENTER);
dataTable.addHeaderCell(cell);
dataTable.addHeaderCell(columnTitle1);
dataTable.addHeaderCell(columnTitle2);
newPage=false;
}
sNo++;
designation=this.designations.get(r);

cell=new Cell();
dataPara=new Paragraph(String.valueOf(sNo));
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
cell.setTextAlignment(TextAlignment.RIGHT);
dataTable.addHeaderCell(cell);

cell=new Cell();
dataPara=new Paragraph(designation.getTitle());
dataPara.setFont(dataFont);
dataPara.setFontSize(13);
cell.add(dataPara);
dataTable.addHeaderCell(cell);
r++;

if(sNo%pageSize==0 || r==this.designations.size())
{
document.add(dataTable);
footerPara=new Paragraph("Software by : Chhavi Chouhan");
footerPara.setFont(dataFont);
footerPara.setFontSize(12);
document.add(footerPara);
if(r<this.designations.size())
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
System.out.println("Export to pdf");
BLException blexception;
blexception=new BLException();
blexception.setGenericException(exception.getMessage());
throw blexception;
}
}
}