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
<!-- incio cabe�alho -->
<div id="logo"><BR><img src="img/logo_brt.gif" width="469" height="44" alt="">
<BR>
</div>
<div id=topo><div id=dados><br><br></div>
</div>

 <!-- inicio principal -->
<div id="linhaverde">
	<table width="100%" border="0" height="100%" bordercolor="#000000" cellspacing="0">
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
					<a href="gerenteORBs.jsp">[alterar]</a>
				</td>
			</tr>
			</table>

		</td>
	</tr>

	<tr valign="top">
		<td width="204" valign="top" style="padding: 18x 0px 0px 0px">

	<%@ include file="./WEB-INF/templates/menu.htm" %>

<div id="Layer2" style="position:absolute; left:41px; top:127px; width:42px; height:12px; z-index:2;"><img src="img/transp.gif" width="49" height="12" border="0" usemap="#Map"></div>
	</td>
	<td style="padding: 3px 8px">