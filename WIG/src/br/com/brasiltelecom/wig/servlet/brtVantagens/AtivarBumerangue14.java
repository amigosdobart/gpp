package br.com.brasiltelecom.wig.servlet.brtVantagens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.action.BrtVantagensXMLParser;
import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.dao.BrtVantagensDAO;
import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.util.Definicoes;

/**
 * 
 * @author	JOÃO PAULO GALVAGNI
 * @since	04/04/2006
 */
public class AtivarBumerangue14 extends HttpServlet
{
	private Context initContext  	 = null;
	private String  wigControl	 	 = null;
	private String  servidorWIG		 = null;
	private int     portaServidorWIG = 0;
	private String  ipEntireX	 	 = null;
	private int     portaEntireX	 = 0;
	private String  wigUser			 = null;
	private String  wigPass			 = null;
	private String  servidorPPP		 = null;
	private String  portaServidorPPP = null;
	private Logger  logger 		 	 = Logger.getLogger(this.getClass());
	ServletContext  sctx 		 	 = null;
	
	public synchronized void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext 	  = new InitialContext();
			wigControl    	  = (String)arg0.getServletContext().getAttribute("VariavelWIGControl");
			servidorWIG 	  = (String) arg0.getServletContext().getAttribute("ServidorWIG");
			portaServidorWIG  = Integer.parseInt((String) arg0.getServletContext().getAttribute("PortaServidorWIG")); 
			ipEntireX		  = (String)arg0.getServletContext().getAttribute("ServidorEntireX");
			portaEntireX	  = Integer.parseInt((String)arg0.getServletContext().getAttribute("PortaServidorEntireX"));
			servidorPPP 	  = (String)arg0.getServletContext().getAttribute("ServidorPPP");
			portaServidorPPP  = (String)arg0.getServletContext().getAttribute("PortaServidorPPP");
			wigUser  		  = (String)arg0.getServletContext().getAttribute("UsuarioWIGPPP");
			wigPass			  = (String)arg0.getServletContext().getAttribute("SenhaWIGPPP");
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error(e);
		}
		sctx = arg0.getServletContext();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		//String maquinaWIG = request.getServerName()+":"+portaServidorWIG;
		// String maquinaWIG = ResolvedorNomeServidor.getInstance().resolveNome(request.getServerName())+":"+portaServidorWIG;
		String maquinaWIG = servidorWIG + ":" + portaServidorWIG;
		Connection con = null;
		
		String wmlNovaTentativa = "\t <do type=\"accept\">\n" +
								  "\t	<go href=\""+ wigControl +"?bts=2&amp;btc=7061\"/>\n" +
								  "\t </do>\n";
		
		PrintWriter out = response.getWriter();
		HttpSession sessaoATH = request.getSession(true);
		
		try
		{
			// Recebe o MSISDN como parametro
			String msisdn = request.getParameter("MSISDN");
			
			// Nova instancia do BrtVantagensDAO
			BrtVantagensDAO consultaB14 = new BrtVantagensDAO();
			
			// Realizacao da consulta do cliente via EntireX
			BrtVantagem brtVantagemB14 = consultaB14.getBrtVantagensByMsisdn(msisdn, ipEntireX, portaEntireX);
			MensagemRetorno msgRetorno = new MensagemRetorno();
			
			// Valida as regras do Bumerangue 14
			switch (validaRegrasAssinante(brtVantagemB14))
			{
				// Todas as validacoes foram efetuadas e o assinante esta OK
				case 0:
				{
					// variavel que recebera o retorno do GPP
					BrtVantagensXMLParser xmlParser = new BrtVantagensXMLParser();
					BrtVantagensDAO brtVantagensDAO = new BrtVantagensDAO();
					
					// CODIGO COMENTADO ABAIXO POR NAO HAVER COBRANCA DO BUMERANGUE14 NO GPP
					// ATRAVES DO SIMBROWSING
					/*RetornoAtualizacaoGPP retornoGPP = new RetornoAtualizacaoGPP();
					retornoGPP.setCodRetorno(Definicoes.WIG_RET_GPP_OK);
					// cobrarServico(String msisdn, String codigoServico, String operacao, String operador)
					// Para assinantes dos planos Pre-pago e Hibrido (Controle), ha a necessidade de ativar
					// a promocao Bumerangue14 no GPP
					if ( Definicoes.WIG_PLANO_PREPAGO_CLY.equalsIgnoreCase(brtVantagemB14.getPlano()) || 
						 Definicoes.WIG_PLANO_CONTROLE_CLY.equalsIgnoreCase(brtVantagemB14.getPlano()))
					{
							ativarBumerangue14 = URLEncoder.encode("atualizadorBrasilVantagens?MSISDN="+msisdn+
																   "&codServico="+Definicoes.WIG_ELM_NOVISSIMO_B14+
																   "&operacao="+Definicoes.WIG_OPERACAO_DEBITO+
																   "&operador="+msisdn+
																   "&acao="+Definicoes.GPP_ACAO_CADASTRO_BUMERANGUE,
																   "UTF-8");
						
						// Monta a URL para realizar uma requisicao ao Portal / GPP
						URL portal = new URL("http://"+servidorPPP+":"+portaServidorPPP+"/ppp/wigBridge?u="+wigUser+"&s="+wigPass+"&a="+ativarBumerangue14);
						
						// Envio da requisicao ao Portal Pre-Pago e recebimento da resposta (String - XML)
						resultado = leURL(portal);
						
						// Realiza o parse do XML de retorno do GPP
						retornoGPP = xmlParser.parseXMLRetornoGPP(resultado);
					}*/
					
					/*// Verifica se a ativacao do Bumerangue14 foi realizada com sucesso no GPP
					if (Definicoes.WIG_RET_GPP_OK.equals(retornoGPP.getCodRetorno()))
					{*/
					
					// Geracao do XML a ser enviado ao Clarify para Adicionar a 
					// promocao Bumerangue14 ao assinante
					String xml = xmlParser.getXMLBrtVantagensAlt(msisdn,
																 null,
																 null,
																 null,
																 Definicoes.CLY_ACAO_INCLUSAO);
					
					logger.debug("Geracao do XML de alteracao do Amigos Toda Hora efetuada com sucesso: \nXML: " + xml);
					
					// Dados para a conexao com o Banco de Dados
					String dataSourceWIG = (String)sctx.getAttribute("DataSourceWIG");
					DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+dataSourceWIG);
					
					try
					{
						con = ds.getConnection();
						
						// Realiza a insercao no Banco de Dados para futura geracao de OS
						brtVantagensDAO.insereAlteracaoBrtVantagens(con,
																	Definicoes.WIG_XML_PROCESSO_OS,
																	Definicoes.WIG_XML_SISTEMA,
																	xml);
					}
					catch (SQLException e)
					{
						logger.error("Erro na insercao dos dados no Banco: ", e);
					}
					finally
					{
						if (con != null && !con.isClosed())
							con.close();
					}
					
					// Envia uma resposta ao assinante
					request.getRequestDispatcher("/ShowBrtVantagens.jsp").forward(request, response);
					break;
						
					/*}*/
					/*else
					{
						// Seta a mensagem de retorno ao assinante
						msgRetorno.setMensagem(NovoAmigosTodaHoraManutencao.retiraCaracteresEspeciais(retornoGPP.getDescRetorno()));
						
						// Retorna a mensagem ao assinante
						retornaMsgAssinante(request, response, msgRetorno);
						
						break;
					}*/
					
				}
				// Objeto brtVantagem null
				case 1:
				{
					// Seta a mensagem de retorno ao assinante
					msgRetorno.setMensagem("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa);
					
					// Retorna a mensagem ao assinante
					retornaMsgAssinante(request, response, msgRetorno);
					
					break;
				}
				// Assinante não esta ativo ou possui pendencias no CLY
				case 2:
				{
					// Grava no LOG uma entrada DEBUG
					logger.debug("Assinante " + msisdn + " com pendencia no sistema Clarify.");
					
					// Seta a mensagem de falha
					msgRetorno.setMensagem("Telefone nao pode ativar a promocao Bumerangue14. Favor entrar em contato com a Central de Relacionamento. Clique OK para sair.");
					
					// Retorna a mensagem ao assinante
					retornaMsgAssinante(request, response, msgRetorno);
					
					break;
				}
				// O Bumerangue14 ja esta ativo para o assinante
				case 3:
				{
					// Grava no LOG uma entrada DEBUG
					logger.debug("Assinante " + msisdn + " ja esta ativo na promocao Bumerangue14.");
					
					// Seta a mensagem de falha
					msgRetorno.setMensagem("Voce ja esta cadastrado na promocao Bumerangue14. Clique OK para sair.");
					
					// Retorna a mensagem ao assinante
					retornaMsgAssinante(request, response, msgRetorno);
					
					break;
				}
			}
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro: ", e);
			
			// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
			out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
		}
		finally
		{
			out.flush();
			out.close();
		}
	}
	
	/**
	 * Metodo....: retornaMsgAssinante
	 * Descricao.: Retorna a msg para o assinante
	 * 
	 * @param request
	 * @param response
	 * @param msgRetorno
	 * @throws Exception
	 */
	public void retornaMsgAssinante(HttpServletRequest request, HttpServletResponse response, MensagemRetorno msgRetorno) throws Exception
	{
		request.setAttribute("mensagemRetorno", msgRetorno);
		
		// Redireciona a requisicao para mostrar a mensagem ao assinante
		try
		{
			request.getRequestDispatcher("/ShowMensagemInfo.jsp").forward(request, response);
		}
		catch ( Exception e )
		{
			throw e;
		}
	}
	
	/**
	 * Metodo....: leURL
	 * Descricao.: Realiza a leitura da URL informada e 
	 * 			   retorna o resultado ao solicitante
	 * 
	 * @param url		- Endereco WEB (URL)
	 * @return retorno  - String com o resultado da requisicao
	 */
	public static String leURL(URL url)
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String retorno = br.readLine();
			br.close();
			return retorno==null?"Problemas na atualizacao do Brasil Vantagens: ":retorno;
		}
		catch(Exception e)
		{
			return "Problemas na atualizacao do Brasil Vantagens.";
		}
	}
	
	/**
	 * Metodo....: validaRegrasAssinante
	 * Descricao.: Realiza as devidas validacoes necessarias para que o assinante
	 *             possa realizar a consulta, alteracao, cadastro ou exclusao de
	 *             acessos na promocao Amigos Toda Hora
	 * 
	 * @param brtVantagem	- Objeto ja com os valores do Clarify
	 * @return int			- Inteiro, dependente das validacoes
	 */
	public int validaRegrasAssinante (BrtVantagem brtVantagemB14)
	{
		// Valida se o objeto eh nulo
		if (brtVantagemB14 == null)
			return Definicoes.B14_RET_NULO;
		// Valida se o assinante esta ativo no CLY
		if (!brtVantagemB14.isAtivo())
			return Definicoes.B14_RET_PENDENTE_CLY;
		// Valida se o assinante possui o Bumerangue ja ativo
		if (brtVantagemB14.isBumerangueAtivo())
			return Definicoes.B14_RET_JA_ATIVO;
		
		// Retorno 0 para validacoes OK
		return Definicoes.WIG_RET_OK;
	}
}