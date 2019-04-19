//Definicao do Pacote
package com.brt.gpp.comum.conexoes.sms;

// Arquivos de Imports do GPP
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolLog;

// Arquivos de Imports de Java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
  * 
  * Este arquivo inclui a definicao da classe de conexao com a plataforma de SMS.
  * Contem a definicao dos metodos de conexao, autenticacao e troca de senha. 
  *
  * <P> Versao:			1.0
  * 
  * @Autor: 			Camile Cardoso Couto
  * Date: 				08/04/2004
  *                       
  * Modificado Por:
  * Data:
  * Razao:
  */

public class ConexaoMiddlewareSMSC
{		
	// Atributos da classe
//	private long idProcesso;
//	private GerentePoolLog 		Log 			= null; // Gerente de LOG

	// Dados de conexao
	private String ipServidor = null;
	private String portaServico = null;
	private String mensagemURLSMSC = null;
	private String nomeUsuario = null;
	private String senhaUsuario = null;
	private String servicoAplicacaoSMSC = null;
	private String itemAplicacaoSMSC = null;
	private int    codigoTarifacaoSMSC = 0;
	private int    originadorSMSC = 0;

	// Atributos de conexao		
	private Socket sk = null;
	private PrintStream out = null;
	private BufferedReader in = null;
	
	/**
	 * Metodo...: ConexaoMiddlewareSMSC
	 * Descricao: Construtor 
	 * @param	aIdProcesso	- Numero indicador do processo 
	 * @return	
	 * @throws 	GPPInternalErrorException								
	 */
	public ConexaoMiddlewareSMSC ( long aIdProcesso ) throws GPPInternalErrorException											
	{
		// Obtem instancias de servantes
		ArquivoConfiguracaoGPP arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();
		
		// Armazena o ID do log 
//		this.idProcesso = aIdProcesso;
//		this.Log = 
		GerentePoolLog.getInstancia(this.getClass());
		
		// Obtem dados da plataforma de SMS no arquivo de configuracao do GPP
		this.ipServidor 			= arqConfiguracao.getEnderecoMaquinaSMSC();
		this.portaServico 			= arqConfiguracao.getPortaMaquinaSMSC();
		this.mensagemURLSMSC 		= arqConfiguracao.getMensagemURLSMSC();		
		this.nomeUsuario 			= arqConfiguracao.getNomeUsuarioSMSC();
		this.senhaUsuario 			= arqConfiguracao.getSenhaUsuarioSMSC();
		this.codigoTarifacaoSMSC	= arqConfiguracao.getCodigoTarifacaoSMSC();
		this.originadorSMSC 		= arqConfiguracao.getOriginadorSMSC();
		this.servicoAplicacaoSMSC 	= arqConfiguracao.getServicoAplicacaoSMSC();		
		this.itemAplicacaoSMSC 		= arqConfiguracao.getItemAplicacaoSMSC();		
	}    

	/**
	 * Metodo....:ConexaoMiddlewareSMSC
	 * Descricao.:Construtor alternativo para utilizacao do envio de SMS fora do sistema GPP
	 *
	 */
	public ConexaoMiddlewareSMSC(String ipServidor, String portaServico, String mensagemURL, String nomeUsuario,
			                     String senhaUsuario, int codigoTarifacao, int originador, String servicoAplicacao,
								 String itemAplicacao)
	{
		this.ipServidor 			= ipServidor;
		this.portaServico 			= portaServico;
		this.mensagemURLSMSC 		= mensagemURL;
		this.nomeUsuario 			= nomeUsuario;
		this.senhaUsuario 			= senhaUsuario;
		this.codigoTarifacaoSMSC	= codigoTarifacao;
		this.originadorSMSC 		= originador;
		this.servicoAplicacaoSMSC 	= servicoAplicacao;
		this.itemAplicacaoSMSC 		= itemAplicacao;
	}

   /**
	* Metodo...: enviaSMS
	* Descricao: Envia o SMS para o numero de destino 
	* @param 	aNumeroAssinante	- Numero do assinante que deve receber o SMS
	* @param 	aMensagem			- Mensagem que deve ser enviada
	* @return	boolean 			- TRUE se enviou SMS e FALSE caso contrario 
	* @throws 	GPPInternalErrorException
	*/
	public boolean enviaSMS(String aNumeroAssinante, String aMensagem) throws GPPInternalErrorException
	{	
		boolean retorno = false;
		String linha = null;

		// Obtém referência para arquivo de configuração
//		ArquivoConfiguracaoGPP arqConfiguracao = 
		ArquivoConfiguracaoGPP.getInstance();
		try
		{
			// Abre conexao SMSC
			sk = new Socket(this.ipServidor, Integer.parseInt(this.portaServico));
			out = new PrintStream(sk.getOutputStream());
			in = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			
			// Monta XML de envio
			StringBuffer query = montaXML (aNumeroAssinante, aMensagem);
			
			// Envia Mensagem
			//out.println("POST /fcgi-bin/echo HTTP/1.1");
			out.println("POST " + this.mensagemURLSMSC + " HTTP/1.1");
			out.println("Accept: text/html */*");
			out.println("Accept-Language: pt-br");
			out.println("Accept-Encoding: gzip, deflate");
			out.println("User-Agent: Mozilla/4.0 (compatible)");
			out.println("Content-Length: " + query.length());
			out.println("Keep-alive: true");
			out.println("Host: " + this.ipServidor + ":" + this.portaServico + "\n");
			out.println(query.toString()+"\n");
			
			out.flush();
			while((linha = in.readLine())!=null)
			{	
				if (linha != null)
				{
						// Verifica o retorno do POST procurando um token "HTTP/1.1"
						java.util.StringTokenizer st = new java.util.StringTokenizer( linha, " ", false );
						String token = st.nextToken();

						if(token.equals("HTTP/1.1"))
						{
								// Verifica se achou o token "200", indicando envio com sucesso
								token = st.nextToken();
								if(token.equals("200"))
								{
										retorno = true;
										break;
								}
								// Se nao encontrou o token "200", aconteceu um erro no envio de SMS
								else
								{
										retorno = false;
										break;
								}
						}
				}
			}			
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		finally
		{
			try
			{
				// Verifica se o socket foi aberto
				if (sk != null)
				{
					// Libera Conexao SMSC
					sk.close();
					out.close();
					in.close();
				}
			}
			catch (Exception e)
			{
				throw new GPPInternalErrorException ("Erro ao fechar socket: " + e);
			}
		}
		return retorno;
	}
	
	/**
	 * Metodo...: enviaSMS
	 * Descricao: Recebe os paramentros para envio do SMS e monta XML a ser enviado
	 * @param aNumeroAssinante 	- Numero do assinante que deve receber o SMS
	 * @param aMensagem			- Mensagem que deve ser enviada
	 * @return					- XML
	 * @throws
	 */	
	protected StringBuffer montaXML (String aNumeroAssinante, String aMensagem)
	{
		StringBuffer query = new StringBuffer ();

		query.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"); 
		query.append("<Operation> ");
		query.append(" <ApplicationContext>"); 
		query.append("   <ApplicationService>" + this.servicoAplicacaoSMSC + "</ApplicationService>"); 
		query.append("     <ApplicationItem>" + this.itemAplicacaoSMSC + "</ApplicationItem>");
		query.append("     <AcessControl>");
		query.append("		   <ApplicationIdentity>" + this.nomeUsuario + "</ApplicationIdentity>"); 
		query.append("		   <Authentication>");
		query.append("		       <Password>" + this.senhaUsuario + "</Password>"); 
		query.append("		   </Authentication> ");
		query.append("	   </AcessControl> ");
		query.append("	 </ApplicationContext> ");
		query.append("	  <SubmitRequest> ");
		query.append("	    <ShortMessage>"); 
		query.append("		  <Header> ");
		query.append("		    <Originator>" + this.originadorSMSC + "</Originator>"); 
		query.append("			<BillingCode>" + this.codigoTarifacaoSMSC + "</BillingCode> ");
		query.append("			<Destination> ");
		query.append("			  <Number>" + aNumeroAssinante + "</Number>"); 
		query.append("			</Destination> ");
		query.append("		  </Header> ");
		query.append("		  <Body> ");
		query.append("		    <Text><![CDATA[" + aMensagem + "]]></Text>");
		query.append("		  </Body> ");
		query.append("		</ShortMessage> ");
		query.append("	 </SubmitRequest> ");
		query.append("</Operation>     ");
		
		return query;
	}
}