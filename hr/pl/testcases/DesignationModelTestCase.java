import java.awt.*;
import javax.swing.*;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
class DesignationModelTestCase extends JFrame
{
private JTable tb;
private DesignationModel designationModel;
private JScrollPane jsp;
private Container container;
DesignationModelTestCase()
{
designationModel=new DesignationModel();
tb=new JTable(designationModel);
jsp=new JScrollPane(tb,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(jsp);
setLocation(100,200);
setSize(500,400);
setVisible(true);
   	 	
}
public static void main(String gg[])
{
DesignationModelTestCase dmtc=new DesignationModelTestCase();
}
}


/*To Run :- java -classpath ..\..\dbdl\build\libs\dbdl.jar;..\..\netclient\build\libs\netclient.jar;..\..\netcommon\build\libs\netcommon.jar;..\..\common\dist\hr-common.jar;..\..\proxybl\build\libs\proxybl.jar;..\libs\barcodes-7.1.11.jar;..\libs\font-asian-7.1.11.jar;..\libs\forms-7.1.11.jar;..\libs\hyph-7.1.11.jar;..\libs\io-7.1.11.jar;..\libs\kernel-7.1.11.jar;..\libs\layout-7.1.11.jar;..\libs\log4j-1.2.16.jar;..\libs\pdfa-7.1.11.jar;..\libs\pdftest-7.1.11.jar;..\libs\sign-7.1.11.jar;..\libs\slf4j-log4j12-1.6.1.jar;..\libs\slf4j.api-1.6.1.jar;..\libs\styled-xml-parser-7.1.11.jar;..\libs\svg-7.1.11.jar;..\build\libs\pl.jar;. DesignationModelTestCase */