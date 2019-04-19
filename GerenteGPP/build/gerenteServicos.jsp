<%@ page import="java.util.*"%>
<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style2 {font-size: small}
-->
</style>
<% Map poolServicos = (Map)session.getAttribute("poolServicos");
   Set valores = poolServicos.entrySet();
 %>
<table width="100%" border="1">
  <tr>
    <th width="250" scope="col"><span class="style2">Nome do Servi&ccedil;o</span></th>
    <th width="185" scope="col"><span class="style2">Status de Ativa&ccedil;&atilde;o </span></th>
    <th width="243" scope="col">&nbsp;</th>
  </tr>
  
  <% for (Iterator i=valores.iterator(); i.hasNext();)
     {
	 	Map.Entry entry = (Map.Entry) i.next();
		String nomeServico = (String)entry.getKey();
		boolean isAtivo    = ((Boolean)entry.getValue()).booleanValue();
   %>
	  <tr>
	    <td><span class="style2"><%=nomeServico%></span></td>
	    <td><span class="style2"><%= isAtivo ? "Ativo" : "Inativo"%></span></td>
		<% if (isAtivo) 
		   {
		 %>
			<td><a href="/gppConsole/DesativaServicosServlet?nomeServico=<%=nomeServico%>"><img src="img/bt_desativar.gif" width="70" height="17"></img></a></td>
		<% }
		   else 
		   { %>
		    <td><a href="/gppConsole/AtivaServicosServlet?nomeServico=<%=nomeServico%>"><img src="img/bt_ativar.gif" width="50" height="17"></img></a></td>
		<% } %>
	  </tr>
  <% }  %>
</table>

