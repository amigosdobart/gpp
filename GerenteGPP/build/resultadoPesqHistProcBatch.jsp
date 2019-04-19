<%@ page import="java.util.*,java.text.*"%>
<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style4 {font-size: xx-small}
-->
</style>
<% Collection listaHistorico = (Collection)request.getSession(true).getAttribute("HistProcessosBatch"); 
   SimpleDateFormat formatoData     = new SimpleDateFormat("dd/MM/yyyy");
   SimpleDateFormat formatoDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
 %>
<table width="100%"  border="1">
  <tr>
    <th width="6%" scope="col"><span class="style4">Id</span></th>
    <th width="17%" scope="col"><span class="style4">NomeProcesso</span></th>
    <th width="16%" scope="col"><span class="style4">Data Inicial </span></th>
    <th width="16%" scope="col"><span class="style4">Data Final </span></th>
    <th width="10%" scope="col"><span class="style4">Status</span></th>
    <th width="25%" scope="col"><span class="style4">Observa&ccedil;&atilde;o</span></th>
    <th width="10%" scope="col"><span class="style4">Data Proc </span></th>
  </tr>
  <% for (Iterator i=listaHistorico.iterator(); i.hasNext();)
     {
	 	String colunas[] = (String[])i.next();
	%>
	  <tr>
  <%
		for (int pos=0; pos < colunas.length; pos++)
		{   %>
		   	<td><span class="style4"><%=colunas[pos]%></span></td>
  <%    }   %>
      </tr>   
  <% } %>

</table>
<table width="100%"  border="0">
  <tr>
    <td><a href="/gppConsole/ConsultaProcessosBatchServlet"><img src="img/bt_voltar.gif" width="49" height="17"></a></td>
  </tr>
</table>
