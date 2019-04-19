package br.com.brasiltelecom.wig.servlet.brtVantagens;

import java.io.IOException;
import java.io.PrintWriter;
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
import br.com.brasiltelecom.wig.util.Definicoes;
import br.com.brasiltelecom.wig.dao.BrtVantagensDAO;

/**
 * @author JOÃO PAULO GALVAGNI 
 * Data..: 04/04/2006
 * 
 */
public class AmigosTodaHoraManutencao extends HttpServlet 
{
	private Context initContext 	 = null;
	private String	wigContainer 	 = null;
	private String  wigControl		 = null;
	private String  servidorWIG		 = null;
	private int		portaServidorWIG = 0;
	private String  servidorEntireX	 = null;
	private int  	portaEntireX	 = 0;
	private Logger  logger 			 = Logger.getLogger(this.getClass());
	private String  servidorPortal	 = null;
	private String  portaPortal		 = null;
	private String  wigUser			 = null;
	private String  wigPass			 = null;
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
			servidorEntireX  = (String)arg0.getServletContext().getAttribute("ServidorEntireX");
			portaEntireX     = Integer.parseInt((String)arg0.getServletContext().getAttribute("PortaServidorEntireX"));
			servidorPortal   = (String)arg0.getServletContext().getAttribute("ServidorPPP");
			portaPortal		 = (String)arg0.getServletContext().getAttribute("PortaServidorPPP");
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
		// String maquinaWIG = ResolvedorNomeServidor.getInstance().resolveNome(request.getServerName())+":"+portaServidorWIG;
		String maquinaWIG = servidorWIG + ":" + portaServidorWIG;
		
		String wmlNovaTentativa = "\t <do type=\"accept\">\n" +
		  						  "\t	<go href=\""+ wigControl +"?bts=2&amp;btc=7061\"/>\n" +
		  						  "\t </do>\n";
		
		try
		{
			String msisdn = request.getParameter("MSISDN");
			String cn = msisdn.substring(2, 4);
			request.setAttribute("ehApenasExclusao", new Boolean(true));
			
			// Cria as listas de Celulares/Fixos Novos
			Collection listaCelNovos  = new ArrayList();
			Collection listaFixNovos  = new ArrayList();
			
			BrtVantagem brtVantagemATH = (BrtVantagem)sessaoATH.getAttribute("brtVantagem");
			// Recebe o objeto BrtVantagem como parametro
			brtVantagemATH = (BrtVantagem) sessaoATH.getAttribute("brtVantagem");
			
			// Lista dos celulares atualmente cadastrados
			Collection listaCelAtuais = (brtVantagemATH.getAmigosTodaHoraCelular() != null ? brtVantagemATH.getAmigosTodaHoraCelular() : null);
			
			// Lista dos fixos atualmente cadastrados
			Collection listaFixAtuais = (brtVantagemATH.getAmigosTodaHoraFixo() != null ? brtVantagemATH.getAmigosTodaHoraFixo() : null) ;
			
			// Recebe a quantidade de Celulares e fixos da promocao ATH
			int qtdeCel = brtVantagemATH.getQtdeAmigosTodaHoraCelular();
			int qtdeFix = brtVantagemATH.getQtdeAmigosTodaHoraFixo();
			// Recebe os dias desde a ultima atualizacao e a
			// quantidade de dias permitido para a proxima
			int diasUltimaAtualizacao = brtVantagemATH.getDiasUltAlteracaoAmigosTodaHora();
			int diasAtualizacao 	  = brtVantagemATH.getDiasAtualizacaoAmigosTodaHora();
			
			// Recebe os seguintes parametros
			// ca e fa - Celulares atuais e Fixos atuais concatenados com ";"
			// cn e fn - Celulares novos e Fixos novos concatenados com ";"
			String[] celNovo  = request.getParameter("cn").split(";");
			String[] fixNovo  = request.getParameter("fn").split(";");
			
			// Popula as colecoes de Celulares e Fixos
			// desconsiderando os numeros em branco
			if (fixNovo != null)
			{
				for (int i = 0; i < fixNovo.length; i++)
				{
					if ( !"".equalsIgnoreCase(fixNovo[i]) )
						listaFixNovos.add (fixNovo[i]);
				}
			}
			if (celNovo != null)
			{
				for (int i = 0; i < celNovo.length; i++)
				{
					if ( ! "".equalsIgnoreCase(celNovo[i]) )
					listaCelNovos.add (celNovo[i]);
				}
			}
			
			// Popula as colecoes com os valores que diferem entre si
			Collection celAtuaisParaAlterar = getNumParaAlterar(listaCelAtuais, listaCelNovos);
			Collection celNovosParaAlterar  = getNumParaAlterar(listaCelNovos , listaCelAtuais);
			Collection fixAtuaisParaAlterar = getNumParaAlterar(listaFixAtuais, listaFixNovos);
			Collection fixNovosParaAlterar  = getNumParaAlterar(listaFixNovos , listaFixAtuais);
			
			// Parametro dos dias desde a ultima atualizacao
			int difDiasAtualizacao = diasUltimaAtualizacao - diasAtualizacao;
			
			// Parametro das alteracoes ou apenas exclusoes
			Collection alteracoes = new ArrayList();
			Collection alteracoesCelulares = verificaAlteracoes(celNovosParaAlterar, celAtuaisParaAlterar, request);
			Collection alteracoesFixos     = verificaAlteracoes(fixNovosParaAlterar, fixAtuaisParaAlterar, request);
			alteracoes.addAll(alteracoesCelulares);
			alteracoes.addAll(alteracoesFixos);
			
			boolean ehApenasExclusao = ((Boolean) request.getAttribute("ehApenasExclusao")).booleanValue();
			
			int validacaoATH = validaAmigosTodaHora(celNovosParaAlterar, cn, fixNovosParaAlterar, brtVantagemATH.getPlano(), ehApenasExclusao, difDiasAtualizacao, alteracoes);
			
			MensagemRetorno msgRetornoATH = new MensagemRetorno();
			
			if (validacaoATH == 9 || validacaoATH == 10 || validacaoATH == 11 || validacaoATH == 99)
			{
				msgRetornoATH.setCodRetorno(Integer.toString(validacaoATH));
				msgRetornoATH.setMensagem(Definicoes.mensagemSAC.get(new Integer(msgRetornoATH.getCodRetorno())).toString());
				validacaoATH = 1;
			}
			
			switch (validacaoATH)
			{
				// Validacoes OK e gravacao do XML para abertura de OS no Clarify 
				case 0:
				{
					// Dados para a conexao com o Banco de Dados
					String dataSourceWIG = (String)sctx.getAttribute("DataSourceWIG");
					DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/"+dataSourceWIG);
					con = ds.getConnection();
					
					// Cria uma nova instancia das classes BrtVantagensDAO e BrtVantagensXMLParser
					BrtVantagensDAO brtVantagensDAO = new BrtVantagensDAO();
					BrtVantagensXMLParser xmlParser = new BrtVantagensXMLParser();
					
					// Faz a construcao do XML para alteracao no Clarify
					// Ja contempla o Novissimo Brasil Vantagens RELEASE II
					String xml = xmlParser.getXMLBrtVantagensAlt(msisdn, alteracoesCelulares, alteracoesFixos, null, null);
					
					logger.debug("Geracao do XML de alteracao do Amigos Toda Hora efetuada com sucesso: \nXML: " + xml);
					// Realiza a insercao no Banco de Dados para futura geracao de OS
					brtVantagensDAO.insereAlteracaoBrtVantagens(con,
																Definicoes.WIG_XML_PROCESSO_OS,
																Definicoes.WIG_XML_SISTEMA,
																xml);
					
					logger.debug("Dados para alteracao do Amigos Toda Hora inseridos no Banco de Dados.");
					// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
					request.getRequestDispatcher("/ShowBrtVantagens.jsp").forward(request,response);
					
					break;
				}
				
				// Erro na validacao dos fixos no SAC ou dos celulares BrT
				case 1:
				{
					// Caso alguma validacao no SAC retorne um erro, um novo formulario
					// sera enviado ao cliente contendo os mesmos numeros ja
					// digitados, porem com a mensagem de erro do SAC
					BrtVantagem brtVantagem = new BrtVantagem();
					
					brtVantagem.setMsisdn(msisdn);
					brtVantagem.setQtdeAmigosTodaHoraCelular(qtdeCel);
					brtVantagem.setQtdeAmigosTodaHoraFixo(qtdeFix);
					brtVantagem.setQtdeAmigosTodaHora(qtdeCel+qtdeFix);
					brtVantagem.setAmigosTodaHoraCelular(listaCelAtuais);
					brtVantagem.setAmigosTodaHoraFixo(listaFixAtuais);
					
					sessaoATH.setAttribute("brtVantagem",brtVantagem);
					sessaoATH.setAttribute("msgRetorno",msgRetornoATH);
					sessaoATH.setAttribute("listaCelNovos",celNovosParaAlterar);
					sessaoATH.setAttribute("listaFixNovos",fixNovosParaAlterar);
					sessaoATH.setAttribute("maquinaWIG",maquinaWIG);
					
					// Redireciona a requisicao para o ShowAmigosTodaHoraConsulta para visualizacao do cliente
					request.getRequestDispatcher("/ShowAmigosTodaHora.jsp").forward(request,response);
					
					break;
				}
				
				// Erro de alteracao ja realizada no periodo determinado pela promocao do assinante
				case 3:
				{
					logger.debug("Assinante ja realizou alteracao do Amigos Toda Hora no mes corrente.");
					out.println(WIGWmlConstrutor.getWMLInfo("Voce ja realizou uma alteracao da promocao nesse mes."));
					
					break;
				}
				
				// Erro, sendo apenas uma consulta, nao necessitando a geracao de OS no Clarify
				case 4:
				{
					logger.debug("Assinante " + msisdn + " realizou apenas uma consulta.");
					out.println(WIGWmlConstrutor.getWMLInfo("Consulta realizada com sucesso. Clique OK para sair."));
					
					break;
				}
				
				// Excecao nas validacoes
				case 98:
				{
					// Grava no LOG uma entrada DEBUG
					logger.debug("Erro inesperado.");
					
					// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
					out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado.\n" + wmlNovaTentativa));
					
					break;
				}
				
				// Erro generico
				default:
				{
					// Grava no LOG uma entrada DEBUG
					logger.debug("Erro inesperado.");
					
					// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
					out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado.\n" + wmlNovaTentativa));
					
					break;
				}
			}
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error("Erro generico, mensagem: " + e.getMessage() + ". Erro completo: ", e);
			// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
			out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado.\n" + wmlNovaTentativa));
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
			catch (SQLException e1)
			{
				logger.error("Erro na tentativa de fechar a conexao com o Banco. " + e1);
				// Nao mostrar mensagem ao assinante caso houver erro no fechamento da conexao com o Banco
				// out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado.\n" + wmlNovaTentativa));
			}
		}
	}
	
	/**
	 * Metodo....: validaAmigosTodaHora
	 * Descricao.: Realiza a validacao das regras de negocio
	 * 			   para a promocao Amigos Toda Hora
	 * 
	 * @param celNovos			 - Lista contendo os celulares novos
	 * @param cn				 - Codigo Nacional do assinante
	 * @param fixNovos			 - Lista contendo os fixos novos
	 * @param plano				 - Plano do assinante
	 * @param ehApenasExclusao	 - Validador se eh apenas exclusao
	 * @param difDiasAtualizacao - Diferenca entre os dias de atualizacao
	 * @param alteracoes		 - Lista das alteracoes a serem realizadas
	 * @return int				 - Inteiro dependendo da validacao
	 */
	public int validaAmigosTodaHora (Collection celNovos, String cn, Collection fixNovos, String plano, boolean ehApenasExclusao, int difDiasAtualizacao, Collection alteracoes)
	{
		// Valida se todos os celulares digitados sao da BrT
		for (Iterator i = celNovos.iterator(); i.hasNext(); )
			if ( ! isCelularBrt( (String) i.next(), cn))
				return 11;
		
		try
		{
			// Valida os numeros fixos junto ao SAC
			MensagemRetorno msgRetornoSAC = new MensagemRetorno();
			msgRetornoSAC.setCodRetorno(Definicoes.WIG_RET_OK_STR);
			msgRetornoSAC = validaNumSAC(fixNovos, plano);
			
			if (msgRetornoSAC.houveErro())
				return Integer.parseInt(msgRetornoSAC.getCodRetorno());
		}
		catch (Exception e)
		{
			return 98;
		}
		
		// Valida se o assinante esta realizando apenas exclusoes ou
		// se o mesmo esta dentro do periodo determinado para alteracoes
		// de acordo com o seu plano
		if (! ehApenasExclusao || difDiasAtualizacao == 0)
			if ( !ehApenasExclusao )
				return 0;
			else
				return 3;
		
		// Valida se o assinante realizou apenas uma consulta
		if (alteracoes.isEmpty())
			return 4;
		
		// Se todas as validacoes estiverem OK, 
		// o retorno eh 0 para geracao da OS
		return 0;
	}
	
	/**
	 * Metodo....: isCelularBrt
	 * Descricao.: Valida se o numero eh um celular BrT e 
	 *  		   pertence ao mesmo cn informado
	 * 
	 * @param msisdn	- Numero a ser validado
	 * @param cn		- Codigo Nacional
	 * @return boolean	- true ou false de acordo com a validacao
	 */
	public boolean isCelularBrt(String msisdn, String cn)
	{
		return Pattern.matches("^"+cn+"84\\d*$", msisdn);
	}
	
	/**
	 * Metodo....: isCelularBrt
	 * Descricao.: Valida se o numero informado eh um celular BrT
	 * 
	 * @param msisdn	- Numero a ser validado
	 * @return boolean	- true ou false de acordo com a validacao
	 */
	public boolean isCelularBrt(String msisdn)
	{
		return Pattern.matches("^\\d\\d84\\d*$", msisdn);
	}
	
	/**
	 * Metodo....: isOutraOperadora
	 * Descricao.: Valida se o numero informado pertence
	 *  		   a outras operadoras que nao a BrT
	 * 
	 * @param msisdn	- Numero a ser validado
	 * @return			- true ou false de acordo com a validacao
	 */
	public boolean isOutraOperadora(String msisdn)
	{
		return Pattern.matches("^\\d\\d(9|8[^4])\\d*$", msisdn);
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
	 * Metodo....: validaNumSAC
	 * Descricao.: Valida a lista dos numeros que serao incluidos na 
	 * 			   promocao um por um no SAC, caso falhe alguma verificacao,
	 * 			   o processo eh interrompido e o objeto retornado
	 * 
	 * @param listaNovos 		- Lista contendo os numeros para validacao
	 * @return MensagemRetorno	- Objeto contendo o resultado da consulta
	 * @throws Exception 
	 */
	public MensagemRetorno validaNumSAC (Collection listaNovos, String plano) throws Exception
	{
		BrtVantagensDAO brtVantagensDAO = new BrtVantagensDAO();
		MensagemRetorno msgRetorno = new MensagemRetorno();
		msgRetorno.setCodRetorno(Definicoes.WIG_RET_OK_STR);
		// Varre a lista dos numeros a serem validados
		for (Iterator i = listaNovos.iterator() ; i.hasNext() ; )
		{
			String numero = (String) i.next();
			if ( !isCelularBrt(numero) && !isOutraOperadora(numero))
			{
				try
				{
					// O objeto msgRetorno recebe a validacao de 'cada' numero da lista
					msgRetorno = brtVantagensDAO.validaNumeroBrtVantagem(numero, Definicoes.SAC_REGRA_ATH, servidorEntireX, portaEntireX, plano);
				}
				catch (Exception e)
				{
					logger.error("Erro: ", e);
					throw e;
				}
			}
			else
				msgRetorno.setCodRetorno("99");
			// Se algum numero falhar na validacao, o processo eh interrompido
			if (msgRetorno.houveErro())
				return msgRetorno;
		}
		
		return msgRetorno;
	}
	
	/**
	 * Metodo....: getNumParaAlterar
	 * Descricao.: Verifica quais numeros estao em uma lista, mas
	 * 			   nao estao na outra, o que significa alteracao,
	 * 			   inclusao ou exclusao do numero
	 * 
	 * @param listaOrigem	- Lista contendo os numeros que serao comparados com a listaBase
	 * @param listaBase		- Lista dos numeros para serem comparado
	 * @return Collection	- Colecao dos numeros da listaOrigem que diferem da listaBase
	 */
	public static Collection getNumParaAlterar(Collection listaOrigem, Collection listaBase)
	{
		Collection result = new ArrayList();
		if (listaOrigem != null)
		{
			// Varre a lista de Origem
			for (Iterator i = listaOrigem.iterator(); i.hasNext();)
			{
				// Seleciona um numero da listaOrigem para comparacao 
				String num = (String) i.next();
				
				// Verifica se o numero NAO esta contido na listaBase e
				// o adiciona na lista dos resultados
				if (listaBase != null && ! listaBase.contains(num))
					result.add(num);
			}
		}
		
		// Retorno do resultado
		return result;
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
	public static AlteracaoBrtVantagem getAlteracaoBrtVantagem(String atual, String novo, HttpServletRequest request)
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
				request.setAttribute("ehApenasExclusao", new Boolean(false));
				return alteracaoBrtVantagem;
			}
			else
			{
				// Se o numero novo estiver nulo, a acao sera EXCLUSAO
				alteracaoBrtVantagem.setAcao(Definicoes.CLY_ACAO_EXCLUSAO);
				alteracaoBrtVantagem.setValorAntigo(atual);
				alteracaoBrtVantagem.setValorNovo("");
				return alteracaoBrtVantagem;
			}
		}
		else
		{
			// Caso os numeros nao sejam nulo, a acao sera ALTERACAO
			alteracaoBrtVantagem.setAcao(Definicoes.CLY_ACAO_ALTERACAO);
			alteracaoBrtVantagem.setValorAntigo(atual);
			alteracaoBrtVantagem.setValorNovo(novo);
			request.setAttribute("ehApenasExclusao", new Boolean(false));
			return alteracaoBrtVantagem;
		}
	}
}
