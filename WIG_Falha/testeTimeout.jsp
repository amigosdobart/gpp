<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.io.*, java.net.*"
    %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Teste de conexão</title>
</head>
<body>

<form method="get" action="testPort.jsp">
	servidor:<input type="text" name="servidor" value="<%=request.getParameter("servidor")%>">
	porta:<input type="text" name="porta" value="<%=request.getParameter("porta")%>"><br/>
	<input type="submit" name="testa">
	<input type="reset" name="reset">
</form>

<% 
if(request.getParameter("servidor") != null){
	String servidor = request.getParameter("servidor");
	String port = request.getParameter("porta");
	try{
		Socket sk = new Socket(servidor, Integer.parseInt(port));
%>
		Servidor <b><%=servidor%></b> porta <b> <%= port %></b> conectado com sucesso 
<%
		sk.close();
	}
	catch(Exception e){
%>
		Erro ao conectar em <b><%=servidor%></b> porta <b> <%= port %></b> <br/>
		Erro : <%= e.getMessage() %>
<%
	}	
}	
%>

</body>
</html>