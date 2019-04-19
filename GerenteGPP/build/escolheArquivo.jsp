<%@ page import="com.brt.gpp.comum.*,java.io.*,java.text.*,java.util.*"%>
<jsp:include page="index.jsp" flush="true" />
<style type="text/css">
<!--
.style2 {font-size: x-small}
.style3 {font-size: medium}
-->
</style>
<% SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
   String dirLogs = (String)request.getParameter("diretorioLogs");
   if (dirLogs == null)
   {
   		dirLogs = (String)session.getAttribute("diretorioLogs");
		if (dirLogs == null)
	    	dirLogs = (String)config.getServletContext().getInitParameter("diretorioLogs");
   }
   session.setAttribute("diretorioLogs",dirLogs);
   File dirLog     = new File(dirLogs);
   File arquivos[] = dirLog.listFiles() != null ? dirLog.listFiles() : new File[0];
   Collection arquivosLog = new TreeSet();
   for (int i=0; i < arquivos.length; i++)
   	arquivosLog.add(arquivos[i]);
   	
   arquivos = (File[])arquivosLog.toArray(new File[0]);
%>
<table width="100%" border="0">
  <tr>
    <td width="686"><span class="style3">Arquivos de Log do GPP ( <%=dirLogs%> ) </span></td>
  </tr>
</table>

<table width="100%" border="0">
  <tr>
    <form action="/gppConsole/escolheArquivo.jsp" method="get" name="frm" class="style2">
      <td><span class="style2">Diret&oacute;rio:</span></td>
      <td><input name="diretorioLogs" type="text" size="50"></td>
      <td><a href="javascript:enviaForm();" class="style2"><img src="img/bt_ok.gif" width="25" height="17"></a></td>
    </form>
  </tr>
  <tr>
    <td width="89">&nbsp;</td>
    <td width="321">&nbsp;</td>
    <td width="565">&nbsp;</td>
  </tr>
</table>
<table width="100%" border="1">
  <tr>
    <th width="212" scope="col"><div align="left"><span class="style2">Nome Arquivo </span></div></th>
    <th width="192" scope="col"><div align="left"><span class="style2">Data Ult. Modifica&ccedil;&atilde;o </span></div></th>
    <th width="112" scope="col"><div align="left"><span class="style2">Tamanho (Kb)</span></div></th>
    <th width="166" scope="col">&nbsp;</th>
  </tr>
  <% for (int i=arquivos.length-1; i >= 0; i--)
     {
          if (arquivos[i].isFile())
          {
	 	String nomeArquivo = dirLogs + System.getProperty("file.separator") + arquivos[i].getName();
   %>
	  <tr>
		<td><span class="style2"></span><%=arquivos[i].getName()%></td>
		<td><span class="style2"></span><%=sdf.format(new Date(arquivos[i].lastModified()))%></td>
		<td><span class="style2"></span><%=arquivos[i].length()/1024%></td>
		<td><a href="/gppConsole/GPPLogParseArquivoServlet?nomeArquivo=<%=nomeArquivo%>"><img src="img/bt_pesquisar.gif" width="82" height="17"></a></td>
	  </tr>
   <%     }
     }%>
</table>
<script  language="javascript">
  function enviaForm() 
  {
    var objF = document.frm;
    document.frm.submit();
  }
</script>
