package br.com.brasiltelecom.wig.servlet.brtVantagens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import br.com.brasiltelecom.wig.entity.AlteracaoBrtVantagem;
import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.RetornoAtualizacaoGPP;
import br.com.brasiltelecom.wig.util.Definicoes;
import br.com.brasiltelecom.wig.dao.BrtVantagensDAO;

/**
 * @author JOÃO PAULO GALVAGNI 
 * Data..: 04/04/2006
 * 
 */
public class NovoAmigosTodaHoraManutencao extends HttpServlet 
{
	private Context initContext 	= null;
	private String wigContainer 	= null;
	private String wigControl		= null;
	private String servidorEntireX	= null;
	private String wigUser			= null;
	private String wigPass			= null;
	private String servidorPPP		= null;
	private String portaServidorPPP = null;
	private String servidorWIG		= null;
	private int	portaServidorWIG 	= 0;
	private int portaEntireX		= 0;
	private Logger  logger = Logger.getLogger(this.getClass());
	public static final String REGRA_ATH = "ATH";
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
		Connection con = null;
		HttpSession sessaoATH = request.getSession(true);
		//String maquinaWIG = ResolvedorNomeServidor.getInstance().resolveNome(request.getServerName())+":"+portaServidorWIG;
		String maquinaWIG = servidorWIG + ":" + portaServidorWIG;
		
		String wmlNovaTentativa = "\t <do type=\"accept\">\n" +
								  "\t	<go href=\""+ wigControl +"?bts=2&amp;btc=7061\"/>\n" +
								  "\t </do>\n";
		
		try
		{
			request.setAttribute("ehApenasInclusao", new Boolean(true));
			request.setAttribute("listaATH", new String());
			
			String msisdn = request.getParameter("MSISDN");
			
			// Cria as listas de Celulares/Fixos Novos e Atuais
			Collection listaNumerosAtuais = new ArrayList();
			Collection listaNumerosNovos  = new ArrayList();
			int qtdeATH = 0;
			
			// Recebe o objeto BrtVantagem como parametro
			BrtVantagem brtVantagemATH = (BrtVantagem)sessaoATH.getAttribute("brtVantagem");
			
			// Lista dos numeros atualmente cadastrados no Amigos Toda Hora
			listaNumerosAtuais = (brtVantagemATH.getAmigosTodaHoraCelular() != null ? brtVantagemATH.getAmigosTodaHoraCelular() : null);
			
			// Quantidade de acessos permitido ao assinante
			qtdeATH = brtVantagemATH.getQtdeAmigosTodaHora();
			
			// Recebe os numeros cadastrados concatenados com ";"
			// Para o novissimo, celulares e fixos deverao constar
			// nos campos de celulares do Amigos Toda Hora
			String[] numerosNovos  = request.getParameter("cn").split(";");
			// Popula as listas de celulares novos
			for (int i = 0; i < numerosNovos.length; i++)
			{
				if ( ! "".equalsIgnoreCase(numerosNovos[i]) )
					listaNumerosNovos.add (numerosNovos[i]);
			}
			
			String cn = msisdn.substring(2, 4);
			
			// Popula as colecoes com os valores que diferem entre si
			Collection numerosAtuaisParaAlterar = AmigosTodaHoraManutencao.getNumParaAlterar(listaNumerosAtuais, listaNumerosNovos);
			Collection numerosNovosParaAlterar  = AmigosTodaHoraManutencao.getNumParaAlterar(listaNumerosNovos, listaNumerosAtuais);
			
			// Validacoes do Novissimo Amigos Toda Hora
			// VALIDACOES:
			// 0  - Validacoes OK
			// 1  - Eh apenas consulta
			// 10 - Nao eh fixo de qualquer operadora nem celular BrT do mesmo cn
			MensagemRetorno msgRetornoATH = new MensagemRetorno();
			msgRetornoATH.setCodRetornoNum(validaRegrasNovissimoATH(cn, numerosAtuaisParaAlterar, numerosNovosParaAlterar));
			
			// Apos as validacoes, as acoes serao tomadas de acordo como resultado
			switch (msgRetornoATH.getCodRetornoNum())
			{
				// Validacoes OK, segue fluxo normal
				case 0:
				{
					// Colecao de objetos AlteracaoBrtVantagem contendo as alteracoes a serem realizadas
					Collection alteracoesATH = verificaAlteracoes(numerosNovosParaAlterar, numerosAtuaisParaAlterar, request);
					
					BrtVantagensXMLParser xmlParser = new BrtVantagensXMLParser();
					
					boolean ehApenasInclusao = ((Boolean) request.getAttribute("ehApenasInclusao")).booleanValue();
					RetornoAtualizacaoGPP retornoGPP = new RetornoAtualizacaoGPP();
					retornoGPP.setCodRetorno(Definicoes.WIG_RET_GPP_OK);
					String resultado = "";
					String atualizarAmigosTodaHora = "";
					
					// Verifica o plano do assinante para montar a URL de atualizacao da lista de Amigos Toda Hora
					// Se Pre-pago, a atualizacao eh tarifada durante o processo de atualizacao
					// Se Pos-pago ou Controle, a atualizacao eh feita sem que haja cobranca nesse momento
					if ( Definicoes.WIG_PLANO_PREPAGO_CLY.equalsIgnoreCase(brtVantagemATH.getPlano()) || 
						 Definicoes.WIG_PLANO_CONTROLE_CLY.equalsIgnoreCase(brtVantagemATH.getPlano()))
					{
						// Se o CLY envia a flag para cobranca e o assinante estah
						// alterando ou excluindo os numeros na sua lista entao
						// esta atualizacao deverah ser cobrada.
						// Se o CLY envia a flag para cobranca e o assinante estah
						// apenas incluindo numeros (sem alteracao) na sua lista 
						// entao esta atualizacao NAO deverah ser cobrada, assim
						// com se o CLY enviar a flag para nao cobranca.
						if (brtVantagemATH.efetuaCobrancaATH() && !ehApenasInclusao)
						{
							// Atualizacao de Amigos Toda Hora para Pre-pago com cobranca
							atualizarAmigosTodaHora = URLEncoder.encode("atualizadorBrasilVantagens?MSISDN="+msisdn+
																		"&codServico="+brtVantagemATH.getCodServicoATH()+
																		"&operacao="+Definicoes.WIG_OPERACAO_DEBITO+
																		"&operador="+msisdn+
																		"&listaATH="+(String)request.getAttribute("listaATH")+
																		"&acao="+Definicoes.GPP_ACAO_ATUALIZACAO_ATH,
																		"UTF-8");
							
							// Montagem da URL completa para envio ao Portal
							URL portal = new URL("http://"+servidorPPP+":"+portaServidorPPP+"/ppp/wigBridge?u="+wigUser+"&s="+wigPass+"&a="+atualizarAmigosTodaHora);
							// Envio da requisicao ao Portal Pre-Pago e recebimento da resposta (String - XML)
							resultado = leURL(portal);
							// Realiza o parse do XML de retorno do GPP
							retornoGPP = xmlParser.parseXMLRetornoGPP(resultado);
						}
					}
					
					if (Definicoes.WIG_RET_GPP_OK.equals(retornoGPP.getCodRetorno()))
					{
						// Cria uma nova instancia das classes BrtVantagensDAO e BrtVantagensXMLParser
						// Dados para a conexao com o Banco de Dados
						String dataSourceWIG = (String)sctx.getAttribute("DataSourceWIG");
						DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+dataSourceWIG);
						
						BrtVantagensDAO brtVantagensDAO = new BrtVantagensDAO();
						
						// Faz a construcao do XML para alteracao no Clarify
						String xml = xmlParser.getXMLBrtVantagensAlt(msisdn,
																	 alteracoesATH, 
																	 null,
																	 null,
																	 null);
						
						logger.debug("Geracao do XML de alteracao do Amigos Toda Hora efetuada com sucesso: \nXML: " + xml);
						
						// Nova conexao com o Banco de Dados
						con = ds.getConnection();
						
						// Insercao do XML para geracao de OS no Clarify
						brtVantagensDAO.insereAlteracaoBrtVantagens(con,
																	Definicoes.WIG_XML_PROCESSO_OS,
																	Definicoes.WIG_XML_SISTEMA,
																	xml);
						
						logger.debug("Dados para alteracao do Novo Amigos Toda Hora inseridos no Banco de Dados.");
						// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
						request.getRequestDispatcher("/ShowBrtVantagens.jsp").forward(request,response);
						break;
					}
					else
					{
						logger.error("Falha na atualizacao da promocao Novissimo Amigos Toda Hora. RetornoGPP: " + retornoGPP.getDescRetorno());
						// Seta a mensagem de retorno que sera mostrada ao assinante
						msgRetornoATH.setMensagem( retiraCaracteresEspeciais(retornoGPP.getDescRetorno()) );
						BrtVantagem brtVantagem = new BrtVantagem();
						
						brtVantagem.setMsisdn(msisdn);
						brtVantagem.setQtdeAmigosTodaHora(qtdeATH);
						
						sessaoATH.setAttribute("brtVantagem",brtVantagem);
						sessaoATH.setAttribute("listaNumerosNovos",listaNumerosNovos);
						sessaoATH.setAttribute("msgRetorno",msgRetornoATH);
						sessaoATH.setAttribute("maquinaWIG",maquinaWIG);
						
						// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
						request.getRequestDispatcher("/ShowNovoAmigosTodaHora.jsp").forward(request,response);
						break;
					}
				}
				
				// Apenas consulta, sem alteracao / inclusao / exclusao
				case 1:
				{
					logger.debug("Assinante " + msisdn + " realizou apenas uma consulta.");
					out.println(WIGWmlConstrutor.getWMLInfo("Consulta realizada com sucesso. Clique ok para sair."));
					break;
				}
				
				// Algum numero nao eh fixo nem celular BrT
				// ou nao faz parte do mesmo CN
				case 10:
				{
					// Um novo formulario sera enviado ao cliente contendo os mesmos 
					// numeros ja digitados, porem com uma mensagem de erro/aviso
					BrtVantagem brtVantagem = new BrtVantagem();
					
					brtVantagem.setMsisdn(msisdn);
					brtVantagem.setQtdeAmigosTodaHora(qtdeATH);
					
					// Setando a mensagem de Retorno do SAC
					msgRetornoATH.setMensagem(Definicoes.mensagemSAC.get(new Integer(msgRetornoATH.getCodRetornoNum())).toString());
					
					sessaoATH.setAttribute("brtVantagem",brtVantagem);
					sessaoATH.setAttribute("listaNumerosNovos",listaNumerosNovos);
					sessaoATH.setAttribute("msgRetorno",msgRetornoATH);
					sessaoATH.setAttribute("maquinaWIG",maquinaWIG);
					
					// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
					request.getRequestDispatcher("/ShowNovoAmigosTodaHora.jsp").forward(request,response);
					break;
				}
			}
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro generico: " + e.getMessage() + ". Erro completo: ", e);
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
			} catch (SQLException e1)
			{
				logger.error("Erro na tentativa de fechar a conexao com o Banco. " + e1);
				// Nao mostra uma mensagem de erro ao cliente caso haja erro no fechamento da conexao com o Banco
				// out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
			}
		}
	}
	
	/**
	 * Metodo....: retiraCaracteresEspeciais
	 * Descricao.: Substitui os caracteres especiais pelos correspondentes
	 * 
	 * @param mensagem	- Mensagem original a ser alterada
	 * @return retorno	- Mensagem sem os caracteres especiais 
	 */
	public static String retiraCaracteresEspeciais(String mensagem)
	{
		String retorno = mensagem;
		int caracteresEspeciais[] = {193,194,195,201,202,205,211,212,213,218,225,226,227,233,234,237,243,244,245,250,199,231};
		char caracteresNormais[]  = {'A','A','A','E','E','I','O','O','O','U','a','a','a','e','e','i','o','o','o','u','C','c'};
		
		for (int j = 0; j < caracteresEspeciais.length; j++)
		{
			Character c = new Character((char)caracteresEspeciais[j]);
			retorno = mensagem.replace(c.charValue(), caracteresNormais[j]);
			// A mensagem deve ser atualizada para acompanhar a
			// alteracao dos caracteres especiais
			mensagem = retorno;
		}
		
		return retorno;
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
	 * Metodo....: isCelularBrt
	 * Descricao.: Valida se o numero eh da BrT e
	 * 		       pertence ao mesmo cn informado
	 * 
	 * @param msisdn	- Numero a ser validado
	 * @param cn		- Codigo Nacional informado
	 * @return boolean	- Resultado da validacao
	 */
	public boolean isCelularBrt(String msisdn, String cn)
	{
		return Pattern.matches("^"+cn+"84\\d*$", msisdn);
	}
	
	/**
	 * Metodo....: isFixo
	 * Descricao.: Valida se o numero eh um fixo, podendo
	 * 			   ser de qualquer operadora
	 * 
	 * @param msisdn	- Numero a ser validado
	 * @param cn		- Codigo Nacional informado
	 * @return boolean	- Resultado da validacao
	 */
	public boolean isFixo (String msisdn, String cn)
	{
		return Pattern.matches("^"+cn+"3\\d*$", msisdn);
	}
	
	/**
	 * Metodo....: isCelularBrt
	 * Descricao.: Valida se o numero informado eh da BrT
	 * 
	 * @param msisdn	- Numero a ser validado
	 * @return boolean	- Resultado da validacao
	 */
	public boolean isCelularBrt(String msisdn)
	{
		return Pattern.matches("^\\d\\d84\\d*$", msisdn);
	}
	
	/**
	 * Metodo....: verificaAlteracoes
	 * Descricao.: Varre os numeros Novos digitados pelo cliente e
	 * 			   os numeros atuais cadastrados, verificando se havera
	 * 			   alteracao, exclusao ou inclusao
	 * 
	 * @param listaNovosParaAlterar		- Lista dos numeros novos
	 * @param listaAtuaisParaAlterar	- Lista dos numeros atualmente cadastrados
	 * @return Collection				- Colecao contendo objetos do tipo AlteracaoBrtVantagem
	 */
	public Collection verificaAlteracoes(Collection listaNovosParaAlterar, Collection listaAtuaisParaAlterar, HttpServletRequest request)
	{
		Collection resultado = new ArrayList();
		
		for (Iterator i = listaNovosParaAlterar.iterator() ; i.hasNext() ; )
		{
			String numNovo = (String) i.next();
			String numAtual = null; 
			
			// Caso exista um valor Atual, esse sera utilizado 
			// para Alteracao e excluido da lista dos Atuais
			if (listaAtuaisParaAlterar.iterator().hasNext())
			{
				numAtual = (String) listaAtuaisParaAlterar.iterator().next();
				listaAtuaisParaAlterar.remove(numAtual);
			}
			resultado.add(getAlteracaoBrtVantagem(numAtual, numNovo, request));
		}
		// Varre os valores Atuais para ver se ha algum numero para excluir
		for (Iterator i = listaAtuaisParaAlterar.iterator() ; i.hasNext() ; )
		{
			resultado.add(getAlteracaoBrtVantagem((String) i.next(), null, request));
		}
		return resultado;
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
		// Cria uma instancia do Objeto AlteracaoBrtVantagem 
		// que contera os dados para alteracao
		AlteracaoBrtVantagem alteracaoBrtVantagem = new AlteracaoBrtVantagem();
		
		// Verifica se algum dos numeros esta nulo
		if (atual == null || novo == null || "".equals(atual) || "".equals(novo))
		{
			if (atual == null || "".equals(atual))
			{
				// Se o numero atual estiver nulo, a acao sera INCLUSAO
				alteracaoBrtVantagem.setAcao(Definicoes.CLY_ACAO_INCLUSAO);
				alteracaoBrtVantagem.setValorAntigo("");
				alteracaoBrtVantagem.setValorNovo(novo);
				agrupaListaATH(request, novo);
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
			agrupaListaATH(request, novo);
			request.setAttribute("ehApenasInclusao", new Boolean(false));
			return alteracaoBrtVantagem;
		}
	}
	
	public void agrupaListaATH (HttpServletRequest request, String numNovo)
	{
		String listaMsisdn = (String)request.getAttribute("listaATH");
		listaMsisdn.concat(formataMsisdn(numNovo)+";");
		request.setAttribute("listaATH", listaMsisdn);
	}
	
	public String formataMsisdn (String msisdn)
	{
		return "0"+msisdn.substring(2)+";";
	}
	
	/**
	 * Metodo....: validaRegrasNovissimoATH
	 * Descricao.: Realiza as validacoes para o fluxo
	 * 			   do Amigos Toda Hora
	 * 
	 * @param cn				- Codigo Nacional
	 * @param listaNumerosNovos	- Lista dos novos numeros para inclusao / alteracao 
	 * @return int				- Numero conforme a validacao
	 * 							  0 - OK
	 * 							  1 - Apenas consulta
	 * 							  10 - Erro, celular nao BrT ou nao Fixo
	 */
	public int validaRegrasNovissimoATH(String cn, Collection listaNumerosAtuais, Collection listaNumerosNovos)
	{
		// Verifica se o assinante realizou apenas uma consulta
		if ( (listaNumerosAtuais == null || listaNumerosAtuais.isEmpty()) &&
			 ( listaNumerosNovos == null || listaNumerosNovos.isEmpty()) )
			return Definicoes.ATH_RET_CONSULTA;
		
		// Verifica se os numeros informados sao de mesmo cn
		// e pertencem a um dos grupos abaixo:
		// 1) Celular da BrT
		// 2) Fixo de qualquer operadora
		String numero = "";
		for (Iterator i = listaNumerosNovos.iterator(); i.hasNext(); )
		{
			numero = (String)i.next();
			if ( (!isCelularBrt(numero, cn)) && (!isFixo(numero, cn)) )
				return Definicoes.ATH_RET_NUMERO_INVALIDO;
		}
		
		// Retorno OK
		return Definicoes.ATH_RET_OK;
	}
}
