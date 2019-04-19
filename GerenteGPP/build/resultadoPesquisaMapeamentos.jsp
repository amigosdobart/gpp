<%@ page import="java.util.*"%>
<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style2 {font-size: small}
.style3 {font-size: x-small}
-->
</style>
<% Collection listaMapeamentos = (Collection)request.getSession(true).getAttribute("listaMapeamentos");
   String primeiraLinha[] = (String[])listaMapeamentos.iterator().next();
   int nroColunas = primeiraLinha.length;
 %>
 <table width="100%" border="1">
  <tr>
<%
   for (int i=0; i < nroColunas; i++)
   {
 %>
    <th scope="col"><span class="style2"><%= (i==0 ? "Chave" : "Valor") %></span></th>
<% } %>
  </tr>
  
<%
   for (Iterator i=listaMapeamentos.iterator(); i.hasNext();)
   {
   		String dados[] = (String[])i.next();
 %>
    <tr>
		<% for (int j=0; j < dados.length; j++)
		   {
		 %>
		   		<td><span class="style3"><%=dados[j]%></span></td>
		<% } %>
	</tr>
<% } %>
</table>

 <table width="100%"  border="0">
   <tr>
     <th scope="col"><div align="left"><a href="/gppConsole/gerenteMapeamentos.jsp"><img src="img/bt_voltar.gif" width="49" height="17"></a></div></th>
   </tr>
 </table>
 