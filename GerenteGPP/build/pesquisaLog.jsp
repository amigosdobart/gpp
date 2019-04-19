<%@ page import="com.brt.gpp.comum.*"%>
<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style1 {font-size: medium}
-->
</style>
<script type="text/javascript">
<!--
function toggleBox(szDivID, iState) // 1 visible, 0 hidden
{
   var obj = document.layers ? document.layers[szDivID] :
   document.getElementById ?  document.getElementById(szDivID).style :
   document.all[szDivID].style;
   obj.visibility = document.layers ? (iState ? "show" : "hide") :
   (iState ? "visible" : "hidden");
}
function doBlink() {
  // Blink, Blink, Blink...
  var blink = document.all.tags("BLINK")
  for (var i=0; i < blink.length; i++)
    blink[i].style.visibility = blink[i].style.visibility == "" ? "hidden" : "" 
}

function startBlink() {
  // Make sure it is IE4
  if (document.all)
    setInterval("doBlink()",500)
}
window.onload = startBlink;

// -->
</script>

<form name="frm" action="/gppConsole/GPPLogServlet" method="post">
<input type="hidden" name="page" value="1" >
<input type="hidden" name="firstTime" value="<%=(String)request.getAttribute("firstTime")%>" >
<input type="hidden" name="fileName" value="<%=(String)request.getAttribute("fileName")%>" >

<div ID="aguarde" >
<center><blink><h6>   [Indexando pesquisa]</h6></blink>
<BLINK><Strong>&nbsp;&nbsp;&nbsp;[***** Aguarde ****] </Strong></BLINK>
</center>
</div>

<table width="100%"  border="0">
  <tr>
    <td height="23" colspan="6"><div align="center" class="style1">Parametros de Consulta ao Log GPP </div></td>
    </tr>
   <tr><td><br></td></tr>
  <tr>
    <td width="20%" height="23">&nbsp;</td>
    <td width="14%"><div align="right">IdProcesso:</div></td>
    <td width="19%"><input name="idProcesso" type="text" size="10" maxlength="10"></td>
    <td width="3%"><div align="right"></div></td>
    <td width="21%">&nbsp;</td>
    <td width="23%">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Componente:</div></td>
    <td><select name="componente">
	  <option selected></option>
      <option><%=Definicoes.CN_APROVISIONAMENTO%></option>
      <option><%=Definicoes.CN_CONSULTA%></option>
      <option><%=Definicoes.CN_RECARGA%></option>
      <option><%=Definicoes.CN_PROCESSOSBATCH%></option>
      <option><%=Definicoes.CN_GERENTEGPP%></option>
    </select></td>
    <td><div align="right"></div></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Classe:</div></td>
    <td><select name="nomeClasse">
  	    <option selected></option>
		<option><%=Definicoes.CL_GERENTE_BANCO_DADOS%></option>
		<option><%=Definicoes.CL_GERENTE_TECNOMEN%></option>
		<option><%=Definicoes.CL_CONSULTA_VOUCHER%></option>
		<option><%=Definicoes.CL_CONSULTA_ASSINANTE%></option>
		<option><%=Definicoes.CL_CONSULTA_HISTORICO_PROC_BATCH%></option>
		<option><%=Definicoes.CL_APROVISIONAR%></option>
		<option><%=Definicoes.CL_RECARREGAR%></option>
		<option><%=Definicoes.CL_AJUSTAR%></option>
		<option><%=Definicoes.CL_ENVIO_SMS%></option>
		<option><%=Definicoes.CL_PRODUTOR_SMS%></option>
		<option><%=Definicoes.CL_CONSUMIDOR_SMS%></option>
		<option><%=Definicoes.CL_CICLO_PLANO_HIBRIDO%></option>
		<option><%=Definicoes.CL_RECARGA_RECORRENTE%></option>
		<option><%=Definicoes.CL_ENVIO_USUARIO_SHUTDOWN%></option>
		<option><%=Definicoes.CL_ENVIO_INFOS_RECARGA%></option>
		<option><%=Definicoes.CL_ENVIO_BONUS_CSP14%></option>
		<option><%=Definicoes.CL_MAPEAMENTOS%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_PLANO_PRECO%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_STATUS_ASSINANTE%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_STATUS_SERVICO%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_MOTIVO_BLOQUEIO%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_TARIFA_TROCAMSISDN%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_SISTEMA_ORIGEM%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_CONFIGURACAO_GPP%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_REC_VALORES%></option>
		<option><%=Definicoes.CL_MAPEAMENTO_REC_ORIGEM%></option>
		<option><%=Definicoes.CL_ENVIO_REC_CONCILIACAO%></option>
		<option><%=Definicoes.CL_TRATAR_VOUCHER%></option>
		<option><%=Definicoes.CL_GERAR_EXTRATO%></option>
		<option><%=Definicoes.CL_IMPORTACAO_DADOS_CDR%></option>
		<option><%=Definicoes.CL_IMPORTACAO_ASSINANTES%></option>
		<option><%=Definicoes.CL_GERENTE_GPP%></option>
		<option><%=Definicoes.CL_CONTESTAR_COBRANCA%></option>
		<option><%=Definicoes.CL_COMPROVANTE_SERVICO%></option>
		<option><%=Definicoes.CL_DIAS_SEM_RECARGA%></option>
		<option><%=Definicoes.CL_IMPORTACAO_USUARIOS%></option>
		<option><%=Definicoes.CL_SUMARIZACAO_AJUSTES%></option>
		<option><%=Definicoes.CL_SUM_PRODUTO_PLANO%></option>
		<option><%=Definicoes.CL_CONEXAO_MIDDLEWARE_SMSC%></option>
    </select></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Severidade:</div></td>
    <td><select name="severidade">
	    <option selected></option>
        <option value="<%=Definicoes.LINFO%>">INFO</option>
        <option value="<%=Definicoes.LDEBUG%>">DEBUG</option>
        <option value="<%=Definicoes.LWARN%>">WARN</option>
        <option value="<%=Definicoes.LERRO%>">ERRO</option>
        <option value="<%=Definicoes.LFATAL%>">FATAL</option>
            </select></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Mensagem:</div></td>
    <td><input name="mensagem" type="text" size="40" maxlength="40"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td >&nbsp;</td>
    <td ><div align="right">Período:</div></td>
    <td >
    	<input type="text" size="19" name="dataInicial">&nbsp; a &nbsp;<input type="text"  size="19" name="dataFinal">
    </td>
  </tr>
  <tr>
    <td colspan=2 >
    	<div align="right">Tamanho da Página:</div>
    </td>
    <td><select name="pageSize">
      <option value="10">10 linhas</option>
      <option value="20" selected >20 linhas</option>
      <option value="50">50 linhas</option>
      <option value="150">150 linhas</option>
      <option value="200">200 linhas</option>
    </select></td>
    <td><div align="right"></div></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  
</table>
<br>
<br>
<table width="100%" border="0">
  <tr>
    <td width="409"><div align="right"><a href="javascript:enviaForm();"><img src="img/bt_pesquisar.gif" width="82" height="17"></a></div></td>
    <td width="129"><div align="right"><a href="javascript:limpaForm();"><img src="img/bt_limpar2.gif" width="82" height="17"></a></div></td>
    <td width="103"><div align="right"><a href="/gppConsole/escolheArquivo.jsp"><img src="img/bt_voltar.gif" width="49" height="17"></a></div></td>
    <td width="314">&nbsp;</td>
  </tr>
</table>


</form>
<script  language="javascript">

toggleBox('aguarde',0);

  function limpaForm()
  {
  	document.frm.idProcesso.value='';
  	document.frm.componente.value=0;
	document.frm.dataInicial.value='';
  	document.frm.dataFinal.value='';
	document.frm.mensagem.value='';
	document.frm.nomeClasse.value=0;
  	document.frm.severidade.value=0;
  }
  
  function validaEntrada()
  {
  	if (document.frm.dataInicial.value != "")
	{
		if (!validardata(document.frm.dataInicial.value))
			return false;
		if (document.frm.dataFinal.value == "")
		{
			alert("Favor informar a data final");
			return false;
		}
		else
			if (!validardata(document.frm.dataFinal.value))
				return false;
    }
    return true;
  }
    
  function enviaForm() 
  {
    var objF = document.frm;
	if (validaEntrada())
	{
	    toggleBox('aguarde',1);
	    document.frm.submit();
	}
  }  
  
function validardata(Wparam) 
{
	if (Wparam.length < 19)
	{
		alert("O formato correto da data é dd/MM/aaaa HH:mm:ss" );
		return false;
	}

		dia = Wparam.substring(0,2);
		mes = Wparam.substring(3,5);
		ano = Wparam.substring(6,10);
		hora= Wparam.substring(11,13);
		minu= Wparam.substring(14,16);
		segu= Wparam.substring(17);

		if (dia > 31 || dia < 1){
			alert("Dia digitado é inválido");
			return false;
		}
		if (mes > 12 || mes < 1){
			alert("Mês digitado é inválido");
			return false;
		}
		if (mes == 4 || mes == 6 || mes == 9 || mes == 11){
			if (dia > 30){
				alert("O mês "+mes+" possui 30 dias");
				return false;
			}
		}
		if (hora < 00 || hora > 23){
			alert("Hora inválida");
			return false;
		}
		if (minu < 00 || minu > 59){
			alert("Minuto inválido");
			return false;
		}
		if (segu < 00 || segu > 59){
			alert("Segundo inválido");
			return false;
		}
	return true;
}

</script>
