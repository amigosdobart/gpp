<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"  
 import = "java.io.*,java.util.*,java.text.*"
 errorPage=""
%>
<%
	// Identifica nos parametros do contexto da aplicacao qual o servidor
	// responde para as aplicacoes do WIG
	config = getServletConfig();
	String servidor = (String)config.getServletContext().getAttribute("ServidorWIG");
	String porta	= (String)config.getServletContext().getAttribute("PortaServidorWIG");
	String variavel = (String)config.getServletContext().getAttribute("VariavelWIGContainer");
	
	Calendar mesAtu = Calendar.getInstance();
	Calendar mesAnt = Calendar.getInstance();
	mesAnt.roll(Calendar.MONTH,-1);
	
	SimpleDateFormat formatador = new SimpleDateFormat("MMMMM",new Locale("pt","BR"));
	if (request.getParameter("MSISDN") == null)
	{
%>
<wml> 
	<card id="Erro">
	<p>Numero do assinante esta incorreto: <%=request.getParameter("MSISDN")%></p>
	</card>
</wml>
<%
	}
	else
	{
%>
<wml> 
	<card id="saldo">
	<p>
	<select title="Bumerangue14" name="mes">
	  <option value="<%=mesAnt.get(Calendar.MONTH)+1%>"><%=formatador.format(mesAnt.getTime())%></option>
	  <option value="<%=mesAtu.get(Calendar.MONTH)+1%>"><%=formatador.format(mesAtu.getTime())%></option>
	</select>
	<do type="accept">
		<go href="<%=variavel%>/ConsultaSaldoBoomerangAction.jsp?mes=$(mes)"/>
	</do>
	</p>
	</card>
</wml>
<%	}
%>