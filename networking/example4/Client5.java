import java.io.*;
import java.net.*;
class Client5
{
public static void main(String[]gg)
{
try
{
Socket socket=new Socket("localhost",5505);
String fileName=gg[0];
File file=new File(fileName);
if(file.exists()==false)
{
System.out.println(fileName+" not exists");
return;
}
if(file.isDirectory())
{
System.out.println("Not a file , is a directory");
return;
}
long fileSize=file.length();
String fileNameString=file.getName();
long x=fileSize;
byte []header=new byte[1024];
int i=0;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i++;
}
header[i]=(byte)',';
i++;
long y=fileNameString.length();
int j=0;
while(j<y)
{
header[i]=(byte)fileNameString.charAt(j);
i++;
j++;
}
while(i<1024)
{
header[i]=(byte)32;
i++;
}
OutputStream os=socket.getOutputStream();
os.write(header,0,1024);
os.flush();
int readCount=0;
byte[] ack=new byte[1];
InputStream is=socket.getInputStream();
while(true)
{
readCount=is.read(ack);
if(readCount==-1)continue;
break;
}
int chunkSize=4096;
long bytesToSend=fileSize;
FileInputStream fis=new FileInputStream(file);
byte[] bytes=new byte[chunkSize];
j=0;
while(j<bytesToSend)
{
readCount=fis.read(bytes);
os.write(bytes,0,readCount);
os.flush();
j=j+readCount;
}
fis.close();
while(true)
{
readCount=is.read(ack);
if(readCount==-1)continue;
break;
}
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}

}