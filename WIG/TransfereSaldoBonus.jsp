<%@ page contentType="text/html; charset=iso-8859-1" language="java" buffer="none"
 import = "java.net.URL,java.io.*,java.text.*,java.util.*,javax.rmi.*,javax.naming.*"
 errorPage="" 
%>

<%!
	private InitialContext ictx = null;

	public void jspInit(){
		try{
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
		return retorno==null?"Transferencia indisponivel no momento":retorno;
	}catch(Exception e)
	{
		return "Transferencia indisponivel no momento";
	}
}
%><%	
    DecimalFormat df = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(new Locale("pt","BR")));
	String servidor  = (String)config.getServletContext().getAttribute("ServidorPPP");
	String porta	 = (String)config.getServletContext().getAttribute("PortaServidorPPP");
	String variavel = (String)config.getServletContext().getAttribute("VariavelWIGContainer");
	
	URL portal = null;
	double sldBon = 0;
	if (request.getParameter("OP")==null) 
	{

	try
	{
		portal = new URL("http://"+servidor+":"+porta+"/ppp/transfBonus?msisdn="+request.getParameter("MSISDN")+"&OP=I&u=WIG&s=wig99");
	}
	catch(Exception e)
	{
%>
	<wml>
		<card id="ERRO">	
			<p>
			Transferencia indisponivel no momento 
			</p>
		</card>
	</wml>
<%
	}
	String saldos = leURL(portal);
	sldBon = df.parse(saldos.substring(saldos.indexOf("$")+1,saldos.indexOf("<")).trim()).doubleValue();
%>
        <wml>
          <card id="TranSaldo">	
             <p>
		Saldos:<br/><%=saldos%>
<%
		if (!saldos.equals("Transferencia indisponivel no momento"))
		{
%>
		<select title="Destino" name="sldDst">
			<option value="02">Torpedo</option>
            <option value="03">Dados</option>
            </select>
                <input title="Valor a transferir:" name="vlr" type="text" format="*N" maxlength="2" emptyok="false" />
				<go href="<%=variavel%>/TransfereSaldoBonus.jsp?sldOrg=01&amp;sldDst=$(sldDst)&amp;vlr=$(vlr)&amp;OP=T&amp;u=WIG&amp;s=wig99&amp;sldBon=<%=sldBon%>" />
<%
		}
%>                
             </p>
          </card>
        </wml>
<%
	}
	else 
	if ( request.getParameter("OP").equals("T"))
	{
		boolean haErros = false;
		try
		{
			// Resgata o valor do bonus para comparacao com o valor a transferir
    		sldBon = Double.parseDouble(request.getParameter("sldBon"));

			// Resgata o valor do saldo a transferir, para posterior comparacao
			String valor = request.getParameter("vlr");

			boolean caracteresNOk = true;
			// Checa se ha algum caracter invalido
			for (int i=0; i < valor.length();i++)
			{
				if (valor.charAt(i) != '0')
					caracteresNOk = false;
				if (valor.charAt(i) == '*' || valor.charAt(i) == '#' || valor.charAt(i) == '+' || 
						valor.charAt(i) == '-')
					caracteresNOk = true;
			}
			
			// Faz a verificacao se o valor eh 0
			if ((valor.equals("0")) || (valor.equals("00")) || (Double.parseDouble(valor) < 0d ))
				throw new Exception("Valor Incorreto");

			if (caracteresNOk)
				throw new Exception("Valor Incorreto");
	
			// Verifica se o valor a transferir eh superior ao saldo de bonus
			if (Double.parseDouble(valor) > sldBon)
				throw new Exception("Valor Excedente");
			 
			 // Requisição de transferencia ao Portal
			 portal = new URL("http://"+servidor+":"+porta+"/ppp/transfBonus?msisdn="+request.getParameter("MSISDN")+"&saldoOrigem=01&saldoDestino="+request.getParameter("sldDst")+"&valorTransferencia="+valor+"&OP=T&u="+request.getParameter("u")+"&s="+request.getParameter("s"));
		}
		catch(Exception e)
		{
			haErros = true;
			
			// Tratamento da excecao de valor incorreto, ou =zero
			if (e.getMessage().equals("Valor Incorreto"))
			{
%>
			        <wml>
			          <card id="ERRO">	
			             <p>
							Valor Incorreto ou igual a 0.
			             </p>
			                <input title="Valor a transferir:" name="vlr" type="text" format="*N" maxlength="2" emptyok="false" />
							<go href="<%=variavel%>/TransfereSaldoBonus.jsp?sldOrg=01&amp;sldDst=<%=request.getParameter("sldDst")%>&amp;vlr=$(vlr)&amp;OP=T&amp;u=WIG&amp;s=wig99&amp;sldBon=<%=sldBon%>" />
			          </card>
			        </wml>

<%
			}
			else
			{
				// Tratamento da excecao de valor > saldo de Bonus
				if (e.getMessage().equals("Valor Excedente"))
				{
	%>
				        <wml>
				          <card id="ERRO">	
							<p>
								Valor excede o valor do bonus. 
				            </p>
				            	<input title="Valor a transferir:" name="vlr" type="text" format="*N" maxlength="2" emptyok="false" />
				            	<go href="<%=variavel%>/TransfereSaldoBonus.jsp?sldOrg=01&amp;sldDst=<%=request.getParameter("sldDst")%>&amp;vlr=$(vlr)&amp;OP=T&amp;u=WIG&amp;s=wig99&amp;sldBon=<%=sldBon%>" />
				          </card>
				        </wml>
	<%
				}
				else
				{
	%>
				        <wml>
				          <card id="ERRO">	
				             <p>
						Transferencia indisponivel no momento 
				             </p>
				          </card>
				        </wml>
	<%
				}
			}
		}
		if (!haErros)
		{
%>
			<wml>
				<card id="RETORNO">
					<p>
						Resultado:<br/>
						<%=leURL(portal)%>
					</p>
				</card>
			</wml>
<%
		}
	}
%>