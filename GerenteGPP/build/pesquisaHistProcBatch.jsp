<%@ page import="com.brt.gpp.comum.*,java.util.*"%>
<jsp:include page="index.jsp" flush="true" />
<% Map processosBatch = (Map)session.getAttribute("processosBatch"); %>
<style type="text/css">
<!--
.style1 {font-size: medium}
-->
</style>

<form name="frm" action="/gppConsole/ConsultaHistProcBatchServlet" method="post"><table width="100%"  border="0">
  <tr>
    <td height="23" colspan="5"><div align="center" class="style1">Parametros de Consulta ao Historico de Processos Batch </div></td>
    </tr>
  <tr>
    <td width="12%">&nbsp;</td>
    <td width="30%"><div align="right">Processo:</div></td>
    <td width="34%"><select name="nomeProcesso">
	  <% for (Iterator i=processosBatch.entrySet().iterator(); i.hasNext();)
	     {
		 	Map.Entry entry = (Map.Entry)i.next();
	   %>
	   		<option value="<%=(String)entry.getKey()%>"><%=(String)entry.getValue()%></option></option>
	  <% } %>
    </select></td>
    <td width="4%"><div align="right"></div></td>
    <td width="20%">&nbsp;</td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Status:</div></td>
    <td><select name="statusProcesso">
      <option selected></option>	
      <option><%=Definicoes.TIPO_OPER_ERRO%></option>
      <option><%=Definicoes.TIPO_OPER_SUCESSO%></option>
        </select></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Data Inicial: </div></td>
    <td><input type="text" name="dataInicial"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td><div align="right">Data Final:</div></td>
    <td><input type="text" name="dataFinal"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
</table>
<table width="100%" border="0">
  <tr>
    <td width="409"><div align="right"><a href="javascript:enviaForm();"><img src="img/bt_pesquisar.gif" width="82" height="17"></a></div></td>
    <td width="129"><div align="right"><a href="javascript:limpaForm();"><img src="img/bt_limpar2.gif" width="82" height="17"></a></div></td>
    <td width="103"><div align="right"><a href="/gppConsole/index.jsp"><img src="img/bt_voltar.gif" width="49" height="17"></a></div></td>
    <td width="314">&nbsp;</td>
  </tr>
</table>

</form>
<script  language="javascript">
  function limpaForm()
  {
  	document.frm.nomeProcesso.value=0;
	document.frm.dataInicial.value='';
  	document.frm.dataFinal.value='';
	document.frm.statusProcesso.value=0;
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
