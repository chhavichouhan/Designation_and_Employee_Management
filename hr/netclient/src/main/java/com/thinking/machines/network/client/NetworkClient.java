package com.thinking.machines.network.client;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class NetworkClient
{
public Response send(Request request)throws NetworkException
{
try
{
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(request);
oos.flush();
byte[] objectBytes=baos.toByteArray();
int requestLength=objectBytes.length;
int x=requestLength;
byte header[]=new byte[1024];
int i=1023;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
Socket socket=new Socket(Configuration.getHost(),Configuration.getPort());
OutputStream os=socket.getOutputStream();
os.write(header,0,1024);
os.flush();
InputStream is=socket.getInputStream();
byte[] ack=new byte[1];
int readCount=0;
while(true)
{
readCount=is.read(ack);
if(readCount==-1)continue;
break;
}
int chunkSize=1024;
int j=0;
int bytesToSend=requestLength;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize)chunkSize=bytesToSend-j;
os.write(objectBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
j=0;
i=0;
byte []tmp=new byte[1024];
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
int responseLength=0;
j=1;
while(i>=0)
{
responseLength=responseLength+(header[i]*j);
j=j*10;
i--;
}
System.out.println("Header recieved : "+responseLength);
ack[0]=1;
os.write(ack,0,1);
os.flush();
System.out.println("Acknowlegement sent");
j=0;
i=0;
System.out.println("Now receving response");
byte response[]=new byte[responseLength];
bytesToRecieve=responseLength;
while(j<bytesToRecieve)
{
readCount=is.read(tmp);
if(readCount==-1)continue;
for(int k=0;k<readCount;k++)
{
response[i]=tmp[k];
i++;
}
j=j+readCount;
}
System.out.println("Response recieved");
ack[0]=1;
os.write(ack);
os.flush();
socket.close();
ByteArrayInputStream bais=new ByteArrayInputStream(response);
ObjectInputStream ois=new ObjectInputStream(bais);
Response responseObject=(Response)ois.readObject();
return responseObject;
}catch(Exception e)
{
throw new NetworkException(e.getMessage());
}
}
}