<%@ page import="java.util.Enumeration" contentType="text/xml"%>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE wml PUBLIC "-//SmartTrust//DTD WIG-WML 4.0//EN"
  "http://www.smarttrust.com/DTD/WIG-WML4.0.dtd">

<wml>
  <card id="Main">
    <p>
    
    <%
    	for(Enumeration it = request.getParameterNames(); it.hasMoreElements();){
    		String nome = (String) it.nextElement();	
    %>
    		<%= nome %>=<%= request.getParameter(nome) %><br/>
    <%
    	}
    %>
    
    </p>
  </card>
</wml>