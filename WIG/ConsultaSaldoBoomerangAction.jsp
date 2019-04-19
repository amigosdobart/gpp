<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"  
    import = "java.net.*,java.io.*,javax.rmi.*, javax.naming.*, java.util.*, java.text.*"
    errorPage=""
%>
<%!
	private InitialContext ictx = null;
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
%>
<%!
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
%>
<%
	// Define a data de credito do boomerang baseando na configuracao do dia
	// e do mes que foi pedido como parametro. Para isso se o mes eh definido
	// como o mes atual sendo que se na opcao foi escolhido o mes anterior ou
	// Recebido entao o mes deve ser o mes - 1.
	int mes         = Integer.parseInt(request.getParameter("mes"));
	int diaCredito	= Integer.parseInt((String)config.getServletContext().getAttribute("DiaCreditoBumerangue14"));
	Calendar dataCredito = Calendar.getInstance();
	dataCredito.set(Calendar.DAY_OF_MONTH,diaCredito);
	dataCredito.set(Calendar.MONTH,mes-1);

	URL portal = null;
	try
	{
		// Busca os valores de configuracao para acesso ao portal pre-pago
		String servidor = (String)config.getServletContext().getAttribute("ServidorPPP");
		String porta	= (String)config.getServletContext().getAttribute("PortaServidorPPP");
		String wigUser  = (String)config.getServletContext().getAttribute("UsuarioWIGPPP");
		String wigPass	= (String)config.getServletContext().getAttribute("SenhaWIGPPP");

		String consBoomerang = URLEncoder.encode("consBoomerang?msisdn="+request.getParameter("MSISDN")+"&mes="+mes,"UTF-8");
		portal = new URL("http://"+servidor+":"+porta+"/ppp/wigBridge?u="+wigUser+"&s="+wigPass+"&a="+consBoomerang);
	}
	catch(Exception e)
	{
%>
        <wml>
          <card id="ERRO">	
          <p>Consulta indisponivel no momento</p>
          </card>
        </wml>
<%
	}
	// Busca o resultado retornado pelo portal prepago e verifica se encontrou o caracter "@"
	// Esse caracter separa as informacoes, caso este nao seja encontrado entao algum erro
	// ocorreu e o mesmo e retornado no WML
	String resultado = leURL(portal);
	int pos = resultado.indexOf("@");
	if (pos == -1)
	{
%>
        <wml>
          <card id="ERRO">	
          <p><%=resultado%></p>
          </card>
        </wml>
<%
	}
	else
	{
		// Realiza o parse do resultado. O formato do resultado e o valor concedido na promocao
		// concatenado com um valor boleano indicando se este fez a recarga ou nao.
		// Ex: R$10,00@true onde R$10,00 e o valor recebido e "true" indica que este fez recarga
		String valores[] = resultado.split("@");
		String valorRecebido = valores[0];
		boolean fezRecarga = new Boolean(valores[1]).booleanValue();
		
		// Se o assinante nao fez a recarga entao o valor do bumerangue eh de R$0,00
		if (!fezRecarga)
			valorRecebido = "R$0,00";

		// Se a opcao for igual a M(Mes atual - A Receber) entao mostra a mensagem do valor calculado
		// ateh a data atual, senao o valor eh em relacao ao mes anterior (valor Recebido)
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM",new Locale("pt","BR"));
		String mensagem = null;
		if (mes == Calendar.getInstance().get(Calendar.MONTH)+1)
			mensagem = "Seu Saldo de Bonus Bumerangue14 acumulado ate esta data e de "+ valorRecebido +
			           ". Lembre-se: Recarregue seu celular mensalmente para garantir o seu bonus Bumerangue14";
		else
			mensagem = "Seu Saldo de Bonus Bumerangue14 do mes de " + sdf.format(dataCredito.getTime()) + " foi de "+ valorRecebido +".";
%>
		<wml>
			<card>
				<p><%=mensagem%></p>
			</card>
		</wml>
<%
	} // Fim do else
%>