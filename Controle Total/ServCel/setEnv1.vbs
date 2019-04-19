'~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
'Created by: Rob Olson - Dudeworks 
'Created on: 10/17/2001 
'Purpose: Get Environment Variables. 
'~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

wscript.echo "Working with the Environment: Provided by www.dudeworks.net"&vbcrlf&vbcrlf&strval 

'// Create an instance of the wshShell object
set WshShell = CreateObject("WScript.Shell")
'Use the methods of the object
wscript.echo "Environment.item: "& WshShell.Environment.item("WINDIR")
wscript.echo "ExpandEnvironmentStrings: "& WshShell.ExpandEnvironmentStrings("%windir%")

'// add and remove environment variables
'// Specify the environment type ( System, User, Volatile, or Process )
set oEnv=WshShell.Environment("System")

wscript.echo "Adding ( TestVar=Windows Script Host ) to the System " _
& "type environment"
' add a var
oEnv("OSAGENT_ADDR")="10.61.176.111"
oEnv("OSAGENT_PORT")="13000"


