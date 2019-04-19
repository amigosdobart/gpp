<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.io.*, java.net.*, java.util.*"
    %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Teste de conexão</title>
</head>
<body>
	Hora inicial = <% out.print(new Date()); %> </br>
	
<% 
	int t = (int) (Math.random() * 20000);
%>
	<%= t %></br>
<%
	synchronized(this){
		wait(t);
	}
%>
 
	Hora Final = <% out.print(new Date()); %> </br>
 
 
</body>
</html>