<style type="text/css">
<!--
.style1 {font-size: x-small}
-->
</style>
<%@ page import="java.util.*,com.brt.gppGerente.*,com.brt.gpp.comum.*,com.brt.gppLog.*"%>
<%@ page import="com.brt.clientes.interfacegpp.GerenteORB" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<html>
<head>
<title>Portal Web Pr&eacute; Pago (<%=(String)config.getServletContext().getAttribute("TIPO_DEPLOY")+"/v."+(String)config.getServletContext().getAttribute("VERSAO_DEPLOY")+"."+(String)config.getServletContext().getAttribute("BUILD_DEPLOY")%>)</title>
<link rel="STYLESHEET" type="text/css" href="dealer.css">
<script>
var timerID = null;
var timerRunning = false;

function stopclock()
{
    if(timerRunning)
        clearTimeout(timerID)
    timerRunning = false;
}
function startclock()
{
    stopclock();
    showtime();
}
function showtime()
{
    var now = new Date();
    var dataServidor = "<%SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");out.print(sdf.format(new Date()));%>";
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var seconds = now.getSeconds();
    var timeValue = hours;
    timeValue  += ((minutes < 10) ? ":0" : ":") + minutes;
    timeValue  += ((seconds < 10) ? ":0" : ":") + seconds;
    document.clock.face.value = dataServidor.substring(0,11)+timeValue;
    timerID = setTimeout("showtime()",1000);
    timerRunning = true;
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
<BR>
<div id="Layer1" style="position:absolute; left:583px; top:66px; width:183px; height:17px; z-index:1">
<form name="clock" onSubmit="0">
  <table>
  <tr>
	  <td valign="top">
  		<input type="text" name="face" size="19" disabled=true>
  	  </td>
  </tr>
  </table>
</form>
</div>
<script>startclock();</script>
<!-- incio cabeçalho -->
<div id="logo"><BR><img src="img/logo_brt.gif" width="469" height="44" alt="">
<BR>
</div>
<div id=topo><div id=dados><br><br></div>
</div>

 <!-- inicio principal -->
<div id="linhaverde">
	<table width="20%" border="0"  bordercolor="#000000" cellspacing="0">
	<tr valign="left">
		<td>
			<br>
			<table border="1" width="100%" bordercolor="#D6DBDE" cellspacing="0" cellpadding="0" style="border-collapse: collapse" bgcolor="#FFFFFF">
			<tr>
				<td bordercolor="#D6DBDE" width="17" align="right" bgcolor="#D6DBDE">&nbsp;
				</td>
				<td bordercolor="#C0E0AF" align="right" bgcolor="#CEEB00" class="fontverde3">
					<b>Servidor:</b>
				</td>
				<td  bordercolor="#C0E0AF" align="left" class="fontverde" colspan=2>
					&nbsp;<%=GerenteORB.getOrbServer(GerenteORB.getORBinUse())%>
				</td>
			</tr>
			<tr>
				<td bordercolor="#D6DBDE" border="0" align="right" bgcolor="#D6DBDE">&nbsp;
				</td>
				<td bordercolor="#C0E0AF" align="right" bgcolor="#CEEB00"  class="fontverde3">
					<b>Porta:</b>
				</td>
				<td bordercolor="#C0E0AF" align="left" class="fontverde" colspan=2>
					&nbsp;<%=GerenteORB.getOrbPort(GerenteORB.getORBinUse())%>
				</td>
			</tr>
			<tr>

				<td bordercolor="#D6DBDE" align="right" bgcolor="#D6DBDE">
				</td>
				<td bordercolor="#D6DBDE" align="right" bgcolor="#D6DBDE">
				</td>
				<td align="center" bordercolor="#C0E0AF" class="fontverde" colspan=2>
					<a href="/gppConsole/gerenteORBs.jsp">[alterar]</a>
				</td>
			</tr>
			</table>

		</td>
	</tr>
</table>
<div id="Layer2" style="position:absolute; left:41px; top:127px; width:42px; height:12px; z-index:2;"><img src="img/transp.gif" width="49" height="12" border="0" usemap="#Map"></div>
<% 
Collection listaLogs = (Collection)request.getSession(true).getAttribute("listaLogs"); 
int total = ((Integer)request.getSession(true).getAttribute("total")).intValue();

int pageSize =((Integer)request.getAttribute("pageSize")).intValue();

int pag = ((Integer)request.getAttribute("page")).intValue();

String pesquisa = "idProcesso="+((String)request.getAttribute("idProcesso"))+"&"+
"componente="+((String)request.getAttribute("componente"))+"&"+
"nomeClasse="+((String)request.getAttribute("nomeClasse"))+"&"+
"severidade="+((String)request.getAttribute("severidade"))+"&"+
"dataInicial="+((String)request.getAttribute("dataInicial"))+"&"+
"dataFinal="+((String)request.getAttribute("dataFinal"))+"&"+
"mensagem="+((String)request.getAttribute("mensagem"));

%>
<table width="710" border="0">
  <tr>
    <td align="left" colspan="2" >Página <b><%=pag%></b> de <b><%=(total/pageSize)+1%></b></td>
    <% if (pag>1){ %>
    	<td border=1><a href="/gppConsole/GPPLogServlet?page=<%=pag-1%>&pageSize=<%=pageSize%>&<%=pesquisa%>">Anterior</a></td>
    <%} if (pag<(total/pageSize)+1){ %>
    	<td border=1 ><a href="/gppConsole/GPPLogServlet?page=<%=pag+1%>&pageSize=<%=pageSize%>&<%=pesquisa%>">Próxima</a></td>
    <% } %>
    <td ><a href="/gppConsole/pesquisaLog.jsp?<%=pesquisa%>&pageSize=<%=pageSize%>"><img src="img/bt_nova_consulta.gif" width="82" height="17"></a></td>
  </tr>
</table>

<table width="710"  border="2">
  <tr>
    <th ></th>
    <th ></th>
    <th witdh=2>Id</th>
    <th witdh=100>Data/Hora </th>
    <th witdh=20>Sever.</th>
    <th witdh=200>Classe</th>
    <th witdh=200>Método</th>
    <th width=200>Mensagem</th>
  </tr>
  <% if (listaLogs != null)
  {
  		int p = 0;
  		for (Iterator i=listaLogs.iterator(); i.hasNext();)
		{
			GPPLog log = (GPPLog)i.next();
   %>
		  <tr>
		    <td><%=((pag-1)*pageSize+p)%></td>
		    <td><img src="<%=log.getNomeFiguraSeveridade() == null ? "&nbsp;" :log.getNomeFiguraSeveridade()%>"></span></td>
		    <td><%=log.getIdProcesso()%></td>
		    <td><%=log.getDataHoraFormatada("dd/MM/yyyy HH:mm:ss") == null ? "&nbsp;" : log.getDataHoraFormatada("dd/MM/yyyy HH:mm:ss")%></td>
		    <td><center><%=log.getSeveridade() == null ? "&nbsp;" :log.getSeveridade()%></center></td>
		    <td><%=log.getClasse() == null ? "&nbsp;" : log.getClasse()%></td>
		    <td><%=log.getMetodo() == null ? "&nbsp;" : log.getMetodo()%></td>
		    <td><%=log.getMensagem() == null ? "&nbsp;" : log.getMensagem()%></td>
		  </tr>
	<% p++; }} %>
</table>

