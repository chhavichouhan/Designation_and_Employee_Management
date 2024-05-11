import java.net.*;
import java.io.*;
class Client2
{
public static void main(String[]gg)
{
try
{
String request,response;
int rollNumber=Integer.parseInt(gg[0]);
String name=gg[1];
String gender=gg[2];
OutputStream os;
OutputStreamWriter osw;
InputStream is;
InputStreamReader isr;
int x;
request=rollNumber+","+name+","+gender+"#";
Socket socket=new Socket("localhost",5500);
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
osw.write(request);
osw.flush();
is=socket.getInputStream();
isr=new InputStreamReader(is);
StringBuffer sb=new StringBuffer();
while(true)
{
x=isr.read();
if(x==-1)break;
if(x=='#')break;
sb.append((char)x);
}
response=sb.toString();
System.out.println("Response is : "+response);
socket.close();
}
catch(Exception e)
{
System.out.println(e);
}
}
}