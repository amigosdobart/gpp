<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style8 {font-size: 24px}
-->
</style>
<% String mensagem = (String)request.getParameter("msg"); %>
<table width="100%" border="0">
  <tr>
    <td><span class="style8"><%=mensagem%></span></td>
  </tr>
  <tr>
    <td><a href="/gppConsole/ConsultaConexoesServlet"><img src="img/bt_voltar.gif" width="49" height="17"></a></td>
  </tr>
</table>
