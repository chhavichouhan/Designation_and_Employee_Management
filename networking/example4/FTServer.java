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
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
int readCount=0;
byte[] header=new byte[1024];
byte[] tmp=new byte[1024];
int i=0;
int j=0;
System.out.println("helo");
while(j<1024)	//reading header
{
readCount=is.read(tmp);
if(readCount==-1)continue;
for(int k=0;k<readCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+readCount;
}
System.out.println("hi");
i=0;
j=1;
int bytesToRecieve=0;
while(header[i]!=',')	//parsing header
{
bytesToRecieve=bytesToRecieve+(header[i]*j);
j=j*10;
i++;
}
i++;
System.out.println("File size "+bytesToRecieve);
StringBuffer sb=new StringBuffer();
while(i<1024)
{
sb.append((char)header[i]);
i++;
}
byte[] ack=new byte[1];
ack[0]=1;
os.write(ack,0,1);
os.flush();
String fileName=sb.toString().trim();
System.out.println("File name "+fileName);
System.out.println("Recieved file "+fileName +" of length"+bytesToRecieve);File file=new File("uploads"+File.separator+fileName);
if(file.exists())file.delete();
long fileSize=file.length();
FileOutputStream fos=new FileOutputStream(file);
int chunkSize=4096;
byte[] bytes=new byte[chunkSize];
while(j<fileSize)	//reading request
{
readCount=is.read(bytes);
if(readCount==-1)continue;
fos.write(bytes,0,readCount);
fos.flush();
j=j+readCount;
}
fos.close();
ack[0]=1;
os.write(ack,0,1);
os.flush();
System.out.println("File uploaded "+file.getAbsolutePath());
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}

}

class FTServer
{
private ServerSocket serverSocket;
FTServer()
{
try
{
serverSocket=new ServerSocket(5505);
startListening();
}catch(Exception e)
{
System.out.println(e);
}
}

public void startListening()
{
try
{
while(true)
{
Socket socket=serverSocket.accept();
RequestProcessor requestProcessor=new RequestProcessor(socket);
}
}catch(Exception e)
{
System.out.println(e);
}
}

public static void main(String[]gg)
{
FTServer server=new FTServer();
}
}