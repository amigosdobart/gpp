<%@ page import="com.brt.clientes.interfacegpp.GerenteORB" %>
<%@ page import="java.util.*"%>
<jsp:include page="index.jsp" flush="true" />
<% if (request.getParameter("ORBinUse")!=null)
{
	GerenteORB.ORBinUse = Integer.parseInt((String)request.getParameter("ORBinUse"));
}
%>

<style type="text/css">
<!--
.style1 {font-size: x-small}
-->
</style>
<br><br>
<table border="1" width="100%" bordercolor="#C0E0AF" cellspacing="0" cellpadding="5" style="border-collapse: collapse" bgcolor="#FFFFFF">
  <tr>
    <th width="259" scope="col" bgcolor="#E1EA99" class="fontverde3"><span class="style1">Endereço</span></th>
    <th width="147" scope="col" bgcolor="#E1EA99" class="fontverde3"><span class="style1">Porta</span></th>
    <th width="147" scope="col" bgcolor="#E1EA99" class="fontverde3" colspan=2><span class="style1">Status</span></th>
  </tr>
  
  <% for (int i=0;i<GerenteORB.getOrbsCount(); i++)
     {
   %>
	  <tr>
		<td><span class="style1"></span><%=GerenteORB.getOrbServer(i)%></td>
		<td align="center"><span class="style1"></span><%=GerenteORB.getOrbPort(i)%></td>
		<td><span class="style1"></span>
		<font color=<%if(GerenteORB.getOrbStatus(i).equals("Disponível")){out.print("blue");}else{out.print("red");}%> ><% if(GerenteORB.getORBinUse()==i){out.print("<font color=blue>Em Uso</font>");}else{out.print(GerenteORB.getOrbStatus(i));}%></font></td>
		<td><span class="style1">
			<%  if (GerenteORB.ORBinUse!=i&&!GerenteORB.getOrbStatus(i).equals("Indisponível"))
				{%>
					<a href="/gppConsole/AtualizaORBServlet?ORBinUse=<%=i%>"><img src="img/bt_aplicar.gif" width="82" height="17"></a></span>
				<%}%>
		</td>
	  </tr>
  <% } %>
</table>
