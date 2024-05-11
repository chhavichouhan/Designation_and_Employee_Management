import java.io.*;
import java.net.*;
class RequestProcessor extends Thread
{
private Socket socket;
RequestProcessor(Socket socket)
{
this.socket=socket;
start();
}

public void run()
{
try
{
String request,response;
int rollNumber;
String name,gender;
int x;
int c1,c2;
String p1,p2,p3;
InputStream is;
InputStreamReader isr;
OutputStream os;
OutputStreamWriter osw;
StringBuffer sb;
is=socket.getInputStream();
isr=new InputStreamReader(is);
sb=new StringBuffer();
while(true)
{
x=isr.read();
if(x==1)break;
if(x=='#')break;
sb.append((char)x);
}
request=sb.toString();
System.out.println("Request arrived : "+request);
c1=request.indexOf(",");
c2=request.indexOf(",",c1+1);
p1=request.substring(0,c1);
p2=request.substring(c1+1,c2);
p3=request.substring(c2+1);
rollNumber=Integer.parseInt(p1);
name=p2;
gender=p3;
System.out.printf("RollNumber : %d Name : %s Gender : %s\n",rollNumber,name,gender);
response="Data Stored#";
os=socket.getOutputStream();
osw=new OutputStreamWriter(os);
osw.write(response);
osw.flush();
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}


class Server2
{
private ServerSocket serverSocket;
Server2()
{
try
{
serverSocket=new ServerSocket(5500);
requestListening();
}
catch(Exception e)
{
System.out.println(e);
}
}

private void requestListening()
{
try
{
Socket socket;
while(true)
{
System.out.println("Socket is ready to accept the request");
socket=serverSocket.accept();
RequestProcessor rp=new RequestProcessor(socket);
}
}
catch(Exception e)
{
System.out.println(e);
}
}

public static void main(String[]gg)
{
Server2 server=new Server2();
}

}