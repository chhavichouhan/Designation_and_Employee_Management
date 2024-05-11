package com.thinking.machines.hr.server;
import com.thinking.machines.network.server.*;
import com.thinking.machines.network.common.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.managers.*;
public class RequestHandler implements RequestHandlerInterface
{
private DesignationManagerInterface designationManager;
public RequestHandler()
{
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
}
}

public Response process(Request request)
{
Response response=new Response();
String manager=request.getManager();
String action=request.getAction();
Object []arguments=request.getArguments();
if(manager.equals("DesignationManager"))
{
if(designationManager==null)
{
//will implement later
}
if(action.equals("getDesignations"))
{
Object result=designationManager.getDesignations();
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
/*
if(action.equals("addDesignation"))
{
Object result=designationManager.addDesignation(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("updateDesignation"))
{
Object result=designationManager.updateDesignation(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("removeDesignation"))
{
Object result=designationManager.removeDesignation(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("getDesignationByCode"))
{
Object result=designationManager.getDesignationByCode(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("getDesignationByTitle"))
{
Object result=designationManager.getDesignationByTitle(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("designationCodeExists"))
{
Object result=designationManager.designationCodeExists(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
if(action.equals("designationTitleExists"))
{
Object result=designationManager.designationTitleExists(arguments);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
*/
if(action.equals("getDesignationCount"))
{
Object result=designationManager.getDesignationCount();
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}
}
return response;
}
}