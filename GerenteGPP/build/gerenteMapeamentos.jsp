<style type="text/css">
<!--  ANDRE PASSOU AQUI FAZENDO TESTE DE BASELINE!!!
.style1 {font-size: medium}
-->
</style>

<jsp:include page="index.jsp" flush="true" />
<table width="100%" border="0">
<form name="frm" method="post" action="">
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="152"><select name="nomeMapeamento">
        <option value="1">Plano de Pre&ccedil;o</option>
        <option value="2">Status Assinante</option>
        <option value="3">Status Servi&ccedil;o</option>
        <option value="4">Sistema de Origem</option>
        <option value="5">Tarifa Troca de Senha</option>
        <option value="6">Configura&ccedil;&otilde;es GPP</option>
      </select>
    </td>
    <td width="90"><a href="#" onClick="window.location='/gppConsole/ConsultaMapeamentosServlet?tipoMapeamento='+document.frm.nomeMapeamento.value"><img src="img/bt_listar.gif" width="82" height="17"></a></td>
    <td width="444"><a href="#" onClick="window.location='/gppConsole/AtualizaMapeamentosServlet?tipoMapeamento='+document.frm.nomeMapeamento.value"><img src="img/bt_aplicar.gif" width="82" height="17"></a></td>
  </tr>
</form>
</table>
