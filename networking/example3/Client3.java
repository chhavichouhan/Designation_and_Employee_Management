import java.io.*;
import java.net.*;
class City implements Serializable
{
public int code;
public String name;
}

class Student implements Serializable
{
public int rollNumber;
public String name;
public String gender;
public City city;
}

class Client3	
{
public static void main(String[]gg)
{
try
{
Socket socket=new Socket("localhost",5500);
int rollNumber=Integer.parseInt(gg[0]);
String name=gg[1];
String gender=gg[2];
int code=Integer.parseInt(gg[3]);
String cityName=gg[4];
Student s=new Student();
s.rollNumber=rollNumber;
s.name=name;
s.gender=gender;
City city=new City();
city.code=code;
city.name=cityName;
s.city=city;
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(s);
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
header=new byte[1024];
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
ack[0]=1;
os.write(ack,0,1);
os.flush();
j=0;
i=0;
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
ack[0]=1;
os.write(ack,0,1);
os.flush();
socket.close();
ByteArrayInputStream bais=new ByteArrayInputStream(response);
ObjectInputStream ois=new ObjectInputStream(bais);
String responseString=(String)ois.readObject();
System.out.println("Resource is : "+responseString);
}catch(Exception e)
{
System.out.println(e);
}
}
}


/*
write object 1
make header 1
write header 1
read Ack 1
write request in chunks 1
read Header 1
parse header 1
send ack 1
read response 1
send ack
print response
close

*/