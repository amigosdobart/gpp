<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"  
    import = "java.net.*,java.io.*, javax.naming.*, java.util.*, java.text.*"
    errorPage=""
%><%!
	private Context ictx = null;

	public void jspInit()
	{
		try
		{
		  ictx = new InitialContext();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
%><%!
public String leURL(URL url) 
{
	try
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String retorno = br.readLine();
		br.close();
		return retorno==null?"Consulta indisponivel no momento":retorno;
	}
	catch(Exception e)
	{
		return "Consulta indisponivel no momento";
	}
}
%><%
	// Recebe o parâmetro do mes no formato yyyymm
	String mes = request.getParameter("mes");

	// Definição da variável que conterá o Prolog
	String proLog = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
					"<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"\n"+
					"\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">";

	URL portal = null;
	try
	{
		// Busca os valores de configuracao para acesso ao portal pre-pago
		String servidor = (String)config.getServletContext().getAttribute("ServidorPPP");
		String porta	= (String)config.getServletContext().getAttribute("PortaServidorPPP");
		String wigUser  = (String)config.getServletContext().getAttribute("UsuarioWIGPPP");
		String wigPass	= (String)config.getServletContext().getAttribute("SenhaWIGPPP");

		String consPulaPula = URLEncoder.encode("consPulaPula?msisdn="+request.getParameter("MSISDN")+"&mes="+mes,"UTF-8");
		portal = new URL("http://"+servidor+":"+porta+"/ppp/wigBridge?u="+wigUser+"&s="+wigPass+"&a="+consPulaPula);
	}
	catch(Exception e)
	{
%><%=proLog%>
        <wml>
          <card id="ERRO">	
          <p>Consulta indisponivel no momento</p>
          </card>
        </wml>
<%
	}
	String resultado = leURL(portal);

	// Recebe os valores recebido e a receber, concatenados com ";"
	String saldos = resultado.trim();
	
	// Separa os valores dos saldos
	// 0 - Codigo de retorno da consulta no GPP
	// 1 - Valor Total
	// 2 - Valor Parcial
	// 3 - Valor A Receber
	// 4 - Mensagem de retorno do GPP
	String valores[] = saldos.split(";");

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfmes = new SimpleDateFormat("yyyyMM");

	String mensagem = null;

	// Verifica o codigo de retorno da consulta PulaPula
	// 0000 - Consulta efetuada com sucesso
	// 0082 - Pula-Pula controle "safra antiga"
	if (!valores[0].equals("0000"))
	{
		if (valores[0].equals("0082"))
			mensagem = "Com o Pula-Pula, voce paga a conta num mes e nao paga no outro. " +
					   "Uma economia de ate 50% com celular ate 2008. Pague sempre sua conta em dia e aproveite!";
		else
		{
			mensagem = valores[4];
			
			int caracteresEspeciais[] = {193,194,195,201,202,205,211,212,213,218,225,226,227,233,234,237,243,244,245,250,199,231};
			char caracteresNormais[]  = {'A','A','A','E','E','I','O','O','O','U','a','a','a','e','e','i','o','o','o','u','C','c'};
			
			for (int j = 0; j < caracteresEspeciais.length; j++)
			{
				Character c = new Character((char)caracteresEspeciais[j]);
				mensagem = mensagem.replace(c.charValue(), caracteresNormais[j]);
			}
		}
	}
	else
	{
		// Verifica o parametro para identificar se a consulta foi realizada no mes atual ou mes anterior
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH,1);
		
		// Consulta com base no mês atual
		if (mes.equals(sdfmes.format(cal.getTime())))
		{
			if (valores[2].equals("0,00"))
				// Mensagem para clientes Pula-Pula I que não receberam antecipação
				// e clientes dos demais planos
				mensagem = "Seu saldo de Bonus acumulado ate esta data e de R$ " + valores[3] +
						   ". Lembre-se: Tenha creditos ativos para garantir seu bonus Pula Pula.";
			else
				// Mensagem para clientes Pula-Pula I com antecipaçao já recebida.
				mensagem = "Antecipacao Pula Pula de 1 a 15 R$ " + valores[2] +
						   ". Valor acumulado a partir do dia 16 R$ " + valores[3] +
						   ". Lembre-se: Tenha creditos ativos para garantir seu bonus Pula Pula." ;
		}
		// Consulta com base no mês anterior
		else
			mensagem = "Seu Saldo de Bonus Pula Pula e de R$ "+ valores[1] + ".";
	}

// Envia a mensagem já formatada para o cliente
%><%=proLog%>
		<wml>
			<card>
				<p><%=mensagem%></p>
			</card>
		</wml>
