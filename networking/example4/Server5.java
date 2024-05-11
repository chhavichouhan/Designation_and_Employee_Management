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
File file=new File("uploads"+File.separator+fileName);
if(file.exists())file.delete();
FileOutputStream fos=new FileOutputStream(file);
int chunkSize=4096;
byte[] bytes=new byte[chunkSize];
long m=0;
while(m<bytesToRecieve)	//reading request
{
readCount=is.read(bytes);
if(readCount==-1)continue;
fos.write(bytes,0,readCount);
fos.flush();
m=m+readCount;
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

class Server5
{
private ServerSocket serverSocket;
Server5()
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
Server5 server=new Server5();
}
}