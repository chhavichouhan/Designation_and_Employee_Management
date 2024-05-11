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
OutputStream os=socket.getOutputStream();
InputStream is=socket.getInputStream();
byte [] header=new byte[1024];
byte [] tmp=new byte[1024];
int readCount=0;
int i=0;
int j=0;
int bytesToRecieve=1024;
while(j<bytesToRecieve)
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
i=1023;
j=1;
int requestLength=0;
while(i>=0)
{
requestLength=requestLength+(header[i]*j);
j=j*10;
i--;
}
byte ack[]=new byte[1];
ack[0]=1;
os.write(ack,0,1);
os.flush();
i=0;
j=0;
byte[] request=new byte[requestLength];
while(j<requestLength)
{
readCount=is.read(tmp);
if(readCount==-1)continue;
for(int k=0;k<readCount;k++)
{
request[i]=tmp[k];
i++;
}
j=j+readCount;
}
ByteArrayInputStream bais=new ByteArrayInputStream(request);
ObjectInputStream ois=new ObjectInputStream(bais);
Student s=(Student)ois.readObject();
System.out.println("RollNumber : "+s.rollNumber);
System.out.println("Name : "+s.name);
System.out.println("Gender : "+s.gender);
System.out.println("City code : "+s.city.code);
System.out.println("City : "+s.city.name);
String responseString="Data Saved";
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(responseString);
oos.flush();
byte[]objectBytes=baos.toByteArray();
int responseLength=objectBytes.length;
header=new byte[1024];
i=1023;
int x=responseLength;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
os.write(header,0,1024);
os.flush();
while(true)
{
readCount=is.read(ack);
if(readCount==-1)continue;
break;
}
int chunkSize=1024;
j=0;
int bytesToSend=responseLength;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize)chunkSize=bytesToSend-j;
os.write(objectBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
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

/*
read header 1 
parse header 1
send ack 1
read request 1
save request 1
create header 1
send header 1
read ack 1
send response 1
read ack
close
*/

class Server3
{
ServerSocket serverSocket;
Server3()
{
try
{
serverSocket=new ServerSocket(5500);
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
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket);
}
}
catch(Exception e)
{
System.out.println(e);
}
}

public static void main(String[]gg)
{
Server3 server=new Server3();
}
}
