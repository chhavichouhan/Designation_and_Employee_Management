package com.thinking.machines.network.client;
import javax.xml.xpath.*;
import java.io.*;
import org.xml.sax.*;
import com.thinking.machines.network.common.exceptions.*;
public class Configuration
{
private static String host="";
private static int port=-1;
private static boolean malformed=false;
private static boolean fileMissing=false;
static
{
try
{
File file=new File("server.xml");
if(file.exists())
{
InputSource inputSource=new InputSource("server.xml");
XPath xpath=XPathFactory.newInstance().newXPath();
String host=xpath.evaluate("//server/@host",inputSource);
String port=xpath.evaluate("//server/@port",inputSource);
Configuration.host=host;
Configuration.port=Integer.parseInt(port);
}
else
{
fileMissing=true;
}
}catch(Exception e)
{
malformed=true;
}
}

public static String getHost()throws NetworkException
{
if(fileMissing)throw new NetworkException("server.xml is missing,read documentation");
if(malformed)throw new NetworkException("server.xml is not configured according to documentation");
if(host==null || host.trim().length()==0)throw new NetworkException("server.xml contains invalid host,read documentation");
return host;
}

public static int getPort()throws NetworkException
{
if(fileMissing)throw new NetworkException("server.xml is missing,read documentation");
if(malformed)throw new NetworkException("server.xml is not configured according to documentation");
if(port<0 || port>49151)throw new NetworkException("server.xml contains invalid port,read documentation");
return port;
}
}