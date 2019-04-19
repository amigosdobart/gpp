package br.com.brasiltelecom.wig.servlet.brtVantagens;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import br.com.brasiltelecom.wig.entity.AlteracaoBrtVantagem;
import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.RetornoAtualizacaoGPP;
import br.com.brasiltelecom.wig.util.Definicoes;

/**
 * @author JOÃO PAULO GALVAGNI 
 * Data..: 04/04/2006
 * 
 */
public class BonusTodoMesManutencao extends HttpServlet 
{
	private Context initContext 	 = null;
	private String	wigContainer 	 = null;
	private String  wigControl		 = null;
	private String  servidorWIG		 = null;
	private int		portaServidorWIG = 0;
	private static String  servidorEntireX	 = null;
	private static int  	portaEntireX	 = 0;
	private String wigUser			= null;
	private String wigPass			= null;
	private String servidorPPP		= null;
	private String portaServidorPPP = null;
	private Logger  logger 		     = Logger.getLogger(this.getClass());
	ServletContext sctx =  null;
	
	public synchronized void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext 	 = new InitialContext();
			wigContainer  	 = (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
			wigControl    	 = (String)arg0.getServletContext().getAttribute("VariavelWIGControl");
			servidorWIG 	 = (String) arg0.getServletContext().getAttribute("ServidorWIG");
			portaServidorWIG = Integer.parseInt((String) arg0.getServletContext().getAttribute("PortaServidorWIG"));
			servidorEntireX	 = (String)arg0.getServletContext().getAttribute("ServidorEntireX");
			portaEntireX	 = Integer.parseInt((String)arg0.getServletContext().getAttribute("PortaServidorEntireX"));
			servidorPPP 	 = (String)arg0.getServletContext().getAttribute("ServidorPPP");
			portaServidorPPP = (String)arg0.getServletContext().getAttribute("PortaServidorPPP");
			wigUser  		 = (String)arg0.getServletContext().getAttribute("UsuarioWIGPPP");
			wigPass			 = (String)arg0.getServletContext().getAttribute("SenhaWIGPPP");
		}
		catch (NamingException e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error(e);
		}
		sctx = arg0.getServletContext();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		//String maquinaWIG = ResolvedorNomeServidor.getInstance().resolveNome(request.getServerName())+":"+portaServidorWIG;
		String maquinaWIG = servidorWIG + ":" + portaServidorWIG;
		Connection con = null;
		HttpSession sessaoBTM = request.getSession(true);
		String wmlNovaTentativa = "\t <do type=\"accept\">\n" +
								  "\t	<go href=\""+ wigControl +"?bts=2&amp;btc=7061\"/>\n" +
								  "\t </do>\n";
		try
		{
			// Recebe o MSISDN como parametro
			String msisdn = request.getParameter("MSISDN");
			String cn = msisdn.substring(2, 4);
			request.setAttribute("ehApenasInclusao", new Boolean(true));
			
			BrtVantagem brtVantagemBTM = (BrtVantagem) sessaoBTM.getAttribute("brtVantagem");
			
			// Recebe o fixo atual (fa) e o fixo novo (fn), caso exista
			String numAtual = (brtVantagemBTM.getMsisdnBonusTodoMes() != null ? brtVantagemBTM.getMsisdnBonusTodoMes() : "");
			String numNovo  = "";
			if (request.getParameter("fn") != null)
				numNovo = request.getParameter("fn");
			
			MensagemRetorno msgRetornoValidacao = validaBonusTodoMes(brtVantagemBTM, numNovo, cn);
			
			switch (msgRetornoValidacao.getCodRetornoNum())
			{
				// Validacoes OK, tarifacao ocorrera (Caso o Clarify "aprove")
				// seguida de abertura de OS
				case 0:
				{
					// Verifica a acao a ser tomada de acordo com os numeros atual e novo
					AlteracaoBrtVantagem alteracaoBTM = getAlteracaoBrtVantagem(numAtual, numNovo, request);
					boolean ehApenasInclusao = ((Boolean) request.getAttribute("ehApenasInclusao")).booleanValue();
					BrtVantagensXMLParser xmlParser = new BrtVantagensXMLParser();
					
					RetornoAtualizacaoGPP retornoGPP = new RetornoAtualizacaoGPP();
					retornoGPP.setCodRetorno(Definicoes.WIG_RET_GPP_OK);
					
					// Caso a tag de cobranca do Clarify seja S <<E>>
					// Caso o assinante nao esteja fazendo apenas uma Inclusao <<E>>
					// Caso o plano de preco do assinante seja (PRE-PAGO ou HIBRIDO)
					// Havera tarifacao do assinante no GPP
					if (brtVantagemBTM.efetuaCobrancaBTM() && 
						!ehApenasInclusao 				   && 
					   (Definicoes.WIG_PLANO_CONTROLE_CLY.equalsIgnoreCase(brtVantagemBTM.getPlano()) ||
					    Definicoes.WIG_PLANO_PREPAGO_CLY.equalsIgnoreCase(brtVantagemBTM.getPlano())) )
					{
						String atualizarBonusTodoMes = "";
						String resultado = "";
						// Atualizacao de Amigos Toda Hora para Pre-pago com cobranca
						atualizarBonusTodoMes = URLEncoder.encode("atualizadorBrasilVantagens?MSISDN="+msisdn+
																	"&codServico="+brtVantagemBTM.getCodServicoBTM()+
																	"&operacao="+Definicoes.WIG_OPERACAO_DEBITO+
																	"&operador="+msisdn+
																	"&acao="+Definicoes.GPP_ACAO_COBRANCA_BRT_VANTAGENS,
																	"UTF-8");
						
						// Montagem da URL completa para envio ao Portal
						URL portal = new URL("http://"+servidorPPP+":"+portaServidorPPP+"/ppp/wigBridge?u="+wigUser+"&s="+wigPass+"&a="+atualizarBonusTodoMes);
						// Envio da requisicao ao Portal Pre-Pago e recebimento da resposta (String - XML)
						resultado = NovoAmigosTodaHoraManutencao.leURL(portal);
						// Realiza o parse do XML de retorno do GPP
						retornoGPP = xmlParser.parseXMLRetornoGPP(resultado);
					}
					
					if ( Definicoes.WIG_RET_GPP_OK.equals(retornoGPP.getCodRetorno()) )
					{
						BrtVantagensDAO brtVantagensDAO = new BrtVantagensDAO();
						String xml = "";
						String dataSourceWIG = (String)sctx.getAttribute("DataSourceWIG");
						DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+dataSourceWIG);
						con = ds.getConnection();
						
						// Faz a construcao do XML para alteracao no Clarify
						// Ja contempla o Novissimo Brasil Vantagens RELEASE II
						xml = xmlParser.getXMLBrtVantagensAlt(msisdn, 
															  null, 
															  null, 
															  alteracaoBTM,
															  null);
						
						// Realizar o cadastro / alteracao / retirada do Bonus Todo Mes
						logger.debug("Geracao do XML de alteracao do Bonus Todo Mes efetuado com sucesso: \nXML: " + xml);
						
						// Gravacao do XML para abertura de OS junto ao Clarify
						brtVantagensDAO.insereAlteracaoBrtVantagens(con,
																	Definicoes.WIG_XML_PROCESSO_OS,
																	Definicoes.WIG_XML_SISTEMA,
																	xml);
						
						logger.debug("Dados para alteracao do Bonus Todo Mes inseridos no Banco de Dados.");
						
						// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
						request.getRequestDispatcher("/ShowBrtVantagens.jsp").forward(request,response);
						
						break;
					}
					else
					{
						logger.error("Falha na tarifacao do Bonus Todo Mes. RetornoGPP: " + retornoGPP.getDescRetorno());
						
						msgRetornoValidacao.setMensagem( NovoAmigosTodaHoraManutencao.retiraCaracteresEspeciais(retornoGPP.getDescRetorno()) );
						brtVantagemBTM.setMsisdn(msisdn);
						brtVantagemBTM.setMsisdnBonusTodoMes(numNovo);
						
						// Setando os atributos para serem utilizados no novo formulario
						sessaoBTM.setAttribute("msgRetorno",  msgRetornoValidacao);
						sessaoBTM.setAttribute("brtVantagem", brtVantagemBTM);
						sessaoBTM.setAttribute("maquinaWIG",  maquinaWIG);
						
						// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
						request.getRequestDispatcher("/ShowBonusTodoMes.jsp").forward(request,response);
						
						break;
					}
					
				}
				
				// Apenas consulta
				case 1:
				{
					logger.debug("Assinante " + msisdn + " realizou apenas uma consulta.");
					out.println(WIGWmlConstrutor.getWMLInfo("Consulta realizada com sucesso. Clique OK para sair."));
					
					break;
				}
				
				// Caso alguma validacao apresente falha, um novo formulario
				// sera enviado ao assinante contendo o mesmo numero digitado
				// anteriormente para que o mesmo possa visualizar e
				// altera-lo caso necessario
				default:
				{
					// Resposta default para a validacao do BTM
					brtVantagemBTM.setMsisdn(msisdn);
					brtVantagemBTM.setMsisdnBonusTodoMes(numNovo);
					
					// Setando os atributos para serem utilizados no novo formulario
					sessaoBTM.setAttribute("msgRetorno",  msgRetornoValidacao);
					sessaoBTM.setAttribute("brtVantagem", brtVantagemBTM);
					sessaoBTM.setAttribute("maquinaWIG",  maquinaWIG);
					
					// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
					request.getRequestDispatcher("/ShowBonusTodoMes.jsp").forward(request,response);
					
					break;
				}
			}
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro generico: " + e.getMessage() + "Erro completo: ", e);
			// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
			out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
		}
		finally
		{
			out.flush();
			out.close();
			try
			{
				if (con != null && !con.isClosed())
					con.close();
			}
			catch (SQLException e)
			{
				// Grava no LOG uma entrada ERROR
				logger.error("Erro na tentativa de fechar a conexao.", e);
				// Nao mostra uma mensagem de erro ao cliente caso haja erro no fechamento da conexao com o Banco
				// out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
			}
		}
	}
	
	/**
	 * Metodo....: validaBonusTodoMes
	 * Descricao.: Aplica as regras de negocio para a promocao Bonus Todo Mes
	 * 			   e seta a respectiva mensagem para mostrar ao assinante
	 * 
	 *  		   Possiveis erros:
	 * 			   	0 - Validacoes OK
	 * 			   	1 - Apenas consulta
	 * 			   	(outros) - Numero invalidado pelo SAC
	 * 
	 * @param brtVantagemBTM
	 * @param numNovo
	 * @param cn
	 * @return
	 * @throws Exception
	 */
	public static MensagemRetorno validaBonusTodoMes (BrtVantagem brtVantagemBTM, String numNovo, String cn) throws Exception
	{
		MensagemRetorno msgRetorno = new MensagemRetorno();
		// Codigo de Retorno OK por default
		
		// Assinante realizou apenas uma consulta
		if ( ("".equals(numNovo) && brtVantagemBTM.getMsisdnBonusTodoMes() == null) || numNovo.equals(brtVantagemBTM.getMsisdnBonusTodoMes()))
		{
			msgRetorno.setCodRetornoNum(Definicoes.BTM_RET_CONSULTA);
			msgRetorno.setMensagem((String) Definicoes.mensagemSAC.get(new Integer(msgRetorno.getCodRetornoNum()) ));
			return msgRetorno;
		}
		// Valida se o numero a ser inserido eh de mesmo CN
		else if (!"".equals(numNovo) && !isNumeroFixo(numNovo,cn))
		{
			msgRetorno.setCodRetornoNum(Definicoes.BTM_RET_CN_DIFERENTE);
			msgRetorno.setMensagem((String) Definicoes.mensagemSAC.get(new Integer(msgRetorno.getCodRetornoNum()) ));
			return msgRetorno;
		}
		// Assinante realizou apenas uma exclusao
		else if ( "".equals(numNovo) || numNovo == null )
		{
			msgRetorno.setCodRetornoNum(Definicoes.ATH_RET_OK);
			msgRetorno.setMensagem((String) Definicoes.mensagemSAC.get(new Integer(msgRetorno.getCodRetornoNum()) ));
			return msgRetorno;
		}
		else
		{
			// Valida o numero no SAC
			try
			{
				msgRetorno = validaNumSAC(numNovo, brtVantagemBTM.getPlano());
				// Evitar que o SAC envie o codigo de erro -1
				if ("-1".equals(msgRetorno.getCodRetorno()) )
					msgRetorno.setCodRetorno("10");
				msgRetorno.setMensagem((String) Definicoes.mensagemSAC.get(new Integer(msgRetorno.getCodRetornoNum()) ));
				return msgRetorno;
			}
			catch (Exception e)
			{
				msgRetorno.setCodRetornoNum(Definicoes.BTM_RET_NUMERO_INVALIDO);
				msgRetorno.setMensagem((String) Definicoes.mensagemSAC.get(new Integer(msgRetorno.getCodRetornoNum()) ));
				return msgRetorno;
			}
		}
	}
	
	/**
	 * Metodo....: getAlteracaoBrtVantagem
	 * Descricao.: Define qual o tipo da acao a ser tomada,
	 * 			   levando em consideracao os numeros recebidos
	 * 
	 * @param atual					- Numero cadastrado atualmente na promocao
	 * @param novo					- Numero que o cliente deseja cadastrar
	 * @return AlteracaoBrtVantagem	- Objeto contendo os numeros e a acao a ser tomada
	 */
	public AlteracaoBrtVantagem getAlteracaoBrtVantagem(String atual, String novo, HttpServletRequest request)
	{
		//		Cria uma instancia do Objeto AlteracaoBrtVantagem 
		// que contera os dados para alteracao
		AlteracaoBrtVantagem alteracaoBrtVantagem = new AlteracaoBrtVantagem();
		
		// Verifica se algum dos numeros esta nulo
		if (atual == null || novo == null || "".equals(atual) || "".equals(novo))
		{
			if (atual == null || "".equals(atual) )
			{
				// Se o numero atual estiver nulo, a acao sera INCLUSAO
				alteracaoBrtVantagem.setAcao(Definicoes.CLY_ACAO_INCLUSAO);
				alteracaoBrtVantagem.setValorAntigo("");
				alteracaoBrtVantagem.setValorNovo(novo);
				return alteracaoBrtVantagem;
			}
			else
			{
				// Se o numero novo estiver nulo, a acao sera EXCLUSAO
				alteracaoBrtVantagem.setAcao(Definicoes.CLY_ACAO_EXCLUSAO);
				alteracaoBrtVantagem.setValorAntigo(atual);
				alteracaoBrtVantagem.setValorNovo("");
				request.setAttribute("ehApenasInclusao", new Boolean(false));
				return alteracaoBrtVantagem;
			}
		}
		else
		{
			// Caso os numeros nao sejam nulo, a acao sera ALTERACAO
			alteracaoBrtVantagem.setAcao(Definicoes.CLY_ACAO_ALTERACAO);
			alteracaoBrtVantagem.setValorAntigo(atual);
			alteracaoBrtVantagem.setValorNovo(novo);
			request.setAttribute("ehApenasInclusao", new Boolean(false));
			return alteracaoBrtVantagem;
		}
	}
	
	/**
	 * Metodo....: validaNumSAC
	 * Descricao.: Valida o numero a ser inserido/alterado no SAC, 
	 * 			   caso a verificacao falhe o processo eh 
	 * 			   interrompido e o objeto retornado
	 * 
	 * @param numNovo 			- Numero a ser validado junto ao SAC
	 * @return MensagemRetorno	- Objeto contendo o resultado da consulta
	 * @throws Exception 
	 */
	public static MensagemRetorno validaNumSAC (String numNovo, String plano) throws Exception
	{
		try
		{
			BrtVantagensDAO brtVantagensDAO = new BrtVantagensDAO();
			MensagemRetorno msgRetorno = brtVantagensDAO.validaNumeroBrtVantagem(numNovo, Definicoes.SAC_REGRA_BTM, servidorEntireX, portaEntireX, plano);
			return msgRetorno;
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * Metodo....: isNumeroFixo
	 * Descricao.: Identifica se o numero passado como parametro possui a mascara de um numero fixo
	 *             e se o CN eh o mesmo
	 *            
	 * @param msisdn   - Numero a ser validado
	 * @param cn	   - Codigo Nacional pertencente
	 * @return boolean - true ou false de acordo com a validacao
	 */
	public static boolean isNumeroFixo(String msisdn, String cn)
	{
		return Pattern.matches("^"+cn+"3\\d*$", msisdn);
	}
}

