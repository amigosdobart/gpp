<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"  
 import = "java.io.*,java.util.*,java.text.*"
 errorPage=""
%><%
	// Identifica nos parametros do contexto da aplicacao qual o servidor
	// responde para as aplicacoes do WIG
	config = getServletConfig();
	String servidor = (String)config.getServletContext().getAttribute("ServidorWIG");
	String porta	= (String)config.getServletContext().getAttribute("PortaServidorWIG");
	String variavel = (String)config.getServletContext().getAttribute("VariavelWIGContainer");

	String proLog = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
					"<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"\n"+
					"\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	// Define variaveis para representarem os peridos de consulta da promocao
	// sendo que o mes anterior vai do dia primeiro ao ultimo dia do mes e o mes atual
	// vai do dia primeiro ate o dia atual
	Calendar mesAtu = Calendar.getInstance();
	Calendar mesAnt = Calendar.getInstance();
	mesAnt.add(Calendar.MONTH,-1);
	mesAnt.set(Calendar.DAY_OF_MONTH, mesAnt.getActualMaximum(Calendar.DAY_OF_MONTH));

	DecimalFormat df = new DecimalFormat("00");
	
	String opcaoAtual    = "01/"+df.format(mesAtu.get(Calendar.MONTH)+1)+" ate ontem ";
	String opcaoAnterior = "01/"+df.format(mesAnt.get(Calendar.MONTH)+1)+" a "+df.format(mesAnt.get(Calendar.DAY_OF_MONTH))+"/"+df.format(mesAnt.get(Calendar.MONTH)+1);
	// Altera as datas para o mes posterior para a chamada da api no portal pre-pago
	mesAtu.add(Calendar.MONTH,1);
	mesAnt.add(Calendar.MONTH,1);
	
	if (request.getParameter("MSISDN") == null)
	{
%><%=proLog%>
<wml> 
	<card id="Erro">
	<p>Numero do assinante esta incorreto: <%=request.getParameter("MSISDN")%></p>
	</card>
</wml>
<%
	}
	else
	{
%><%=proLog%>
<wml> 
	<card id="PulaPula">
	<p>
		<select title="PulaPula" name="mes">
			<option value="<%=sdf.format(mesAnt.getTime())%>"><%=opcaoAnterior%></option>
			<option value="<%=sdf.format(mesAtu.getTime())%>"><%=opcaoAtual%></option>
		</select>
		<do type="accept">
			<go href="<%=variavel%>/ConsultaSaldoPulaPulaAction.jsp?mes=$(mes)"/>
		</do>
	</p>
	</card>
</wml>
<%	}
%>