<%@ page import="java.util.*"%>
<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style1 {font-size: x-small}
-->
</style>
<%
   Map poolTipos    = (HashMap)session.getAttribute("poolTipos");
   Map poolConexoes = (HashMap)session.getAttribute("poolConexoes");
   Map poolDisponiveis = (HashMap)session.getAttribute("poolDisponiveis");
 %>
<table width="100%" border="1">
  <tr>
    <th width="259" scope="col"><span class="style1">Nome do Pool de Conex&otilde;es </span></th>
    <th width="147" scope="col"><span class="style1">N&uacute;mero de Conex&otilde;es </span></th>
    <th width="147" scope="col"><span class="style1">N&uacute;mero de Conex&otilde;es Disponíveis</span></th>
    <th width="78" scope="col"><span class="style1">&nbsp;</span></th>
    <th width="78" scope="col"><span class="style1">&nbsp;</span></th>
  </tr>
  
  <% for (Iterator i=poolTipos.entrySet().iterator(); i.hasNext();)
     {
	 	Map.Entry entry = (Map.Entry)i.next();
		String nomePool = (String)entry.getKey();
		short  tipoPool = ((Short)entry.getValue()).shortValue();
   %>
	  <tr>
		<td><span class="style1"></span><%=nomePool%></td>
		<td><span class="style1"></span><%=(Short)poolConexoes.get(nomePool)%></td>
		<td><span class="style1"></span><%=(Short)poolDisponiveis.get(nomePool)%></td>
		<td><span class="style1"><a href="/gppConsole/CriaConexoesServlet?nomePool=<%=nomePool%>&tipoPool=<%=tipoPool%>"><img src="img/bt_criarConexao.gif" width="82" height="17"></a></span></td>
		<td><span class="style1"><a href="/gppConsole/RemoveConexoesServlet?nomePool=<%=nomePool%>&tipoPool=<%=tipoPool%>"><img src="img/bt_removerConexao.gif" width="100" height="17"></a></span></td>
	  </tr>
  <% } %>
</table>
