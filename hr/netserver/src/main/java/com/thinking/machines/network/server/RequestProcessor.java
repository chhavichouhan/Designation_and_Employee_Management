package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.io.*;
import java.net.*;
class RequestProcessor extends Thread
{
private Socket socket;
private RequestHandlerInterface requestHandler;
RequestProcessor(Socket socket,RequestHandlerInterface requestHandler)
{
this.socket=socket;
this.requestHandler=requestHandler;
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
i=1;
j=1023;
int bytesToRecieve=0;
while(j>=0)	//parsing header
{
bytesToRecieve=bytesToRecieve+(header[j]*i);
i=i*10;
j--;
}
byte[] ack=new byte[1];
ack[0]=1;
os.write(ack,0,1);	//writing ack
os.flush();
byte []bytes=new byte[bytesToRecieve];
j=0;
i=0;
while(j<bytesToRecieve)		//reading dataRequest		
{
readCount=is.read(tmp);
if(readCount==-1)continue;
for(int k=0;k<readCount;k++)
{
bytes[i]=tmp[k];
i++;
}
j=j+readCount;
}
ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
ObjectInputStream ois=new ObjectInputStream(bais);
Request request=(Request)ois.readObject();
Response response=requestHandler.process(request);
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(response);
oos.flush();
byte[] responseBytes=baos.toByteArray();
int bytesToSend=responseBytes.length;
int x=bytesToSend;
header=new byte[1024];
i=1023;
tmp=new byte[1024];
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
os.write(header,0,1024); 	//writing header
os.flush();
System.out.println("Response Header sent : "+bytesToSend);
readCount=0;
while(true)
{
readCount=is.read(ack);
if(readCount==-1)continue;
break;
}
System.out.println("Acknowledgement recieved");
int chunkSize=1024;
j=0;
while(j<bytesToSend)	//writing dataResponse
{
if((bytesToSend-j)<chunkSize)chunkSize=bytesToSend-j;
os.write(responseBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
readCount=0;
System.out.println("Response sent");
while(true)
{
readCount=is.read(ack);
if(readCount==-1)continue;
break;
}
System.out.println("acknowledgement recieved");
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}

}