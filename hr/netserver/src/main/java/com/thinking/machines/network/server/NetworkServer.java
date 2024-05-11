package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.net.*;
public class NetworkServer
{
private RequestHandlerInterface requestHandler;
public NetworkServer(RequestHandlerInterface requestHandler)throws NetworkException
{
if(requestHandler==null)
{
throw new NetworkException("Request handler missing");
}
this.requestHandler=requestHandler;
}

public void start()throws NetworkException
{
ServerSocket serverSocket=null;
int port=Configuration.getPort();
try
{
serverSocket=new ServerSocket(port);
}catch(Exception exception)
{
throw new NetworkException("Unable to start server at port : "+port);
}
try
{
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
System.out.println("Server is ready to accept request on port : "+port);
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(socket,requestHandler);
}
}catch(Exception exception)
{
System.out.println(exception);
}
}


}