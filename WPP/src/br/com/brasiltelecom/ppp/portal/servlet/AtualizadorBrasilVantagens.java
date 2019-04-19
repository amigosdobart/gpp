package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.interfacegpp.AprovisionamentoGPP;
import br.com.brasiltelecom.ppp.interfacegpp.RecargaGPP;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

/**
 * @author JOAO PAULO GALVAGNI JR.
 * @since  16/08/2006
 * 
 * Descricao....: Classe responsavel pela tarifacao do Brasil Vantagens,
 * 				  abrangendo as promocoes Amigos Toda Hora, Bonus Todo Mes
 * 				  e Bumerangue 14 para acessos Pre-pago
 * 
 */
public class AtualizadorBrasilVantagens extends HttpServlet 
{
	public void init(ServletConfig scfg) throws ServletException 
	{
		servletContext = scfg.getServletContext();		
	}
	
	private Logger logger = Logger.getLogger(this.getClass());
	private ServletContext servletContext = null;
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		String msisdn = "";
		try 
		{
			// Verifica se o usuario foi autenticado, caso negativo entao dispara um erro
			HttpSession session = request.getSession(true);
			Usuario user = (Usuario)session.getAttribute(Constantes.USUARIO);
			if (user == null)
				throw new Exception("Usuario nao autenticado.");
			
			// Identifica os valores dos parametros para a tarifacao
			msisdn 		 = request.getParameter("MSISDN");
			String codigoServico = (request.getParameter("codServico") == null ? "" : request.getParameter("codServico"));
			String operacao 	 = (request.getParameter("operacao"	 ) == null ? "" : request.getParameter("operacao"  ));
			String operador 	 = (request.getParameter("operador"  ) == null ? "" : request.getParameter("operador"  ));
			String listaATH		 = (request.getParameter("listaATH"	 ) == null ? "" : request.getParameter("listaATH"  ));
			String acao 		 = (request.getParameter("acao"		 ) == null ? "" : request.getParameter("acao"      ));
			
			// Realiza a busca nas configuracoes do servidor de aplicacao para identificar
			// os valores a serem utilizados para a conexao com o sistema GPP
			String servidor  = (String) servletContext.getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta     = (String) servletContext.getAttribute(Constantes.GPP_PORTA_SERVIDOR);
			String xmlRetorno = null;
			
			int acaoBrasilVantagens = acaoBrasilVantagens(acao);
			
			switch(acaoBrasilVantagens)
			{
				// Cobranca apenas
				case 0:
				{
					logger.debug("Tarifacao do assinante " + msisdn + ".");
					xmlRetorno = RecargaGPP.cobraServico(msisdn, codigoServico, operacao, operador, servidor, porta);
					
					break;
				}
				
				// Atualizacao dos acessos do Amigos Toda Hora
				case 1:
				{
					logger.debug("Atualizacao dos Amigos Toda Hora para o assinante " + msisdn + ".");
					xmlRetorno = AprovisionamentoGPP.atualizaAmigosTodaHora(msisdn, listaATH, codigoServico, operacao, operador, servidor, porta);
					
					break;
				}
				
				// Ativacao do servico promocional Bumerangue14
				case 2:
				{
					logger.debug("Ativacao do servico promocional Bumerangue14 para o assinante " + msisdn + ".");
					xmlRetorno = AprovisionamentoGPP.cadastraBumerangue(msisdn, codigoServico, operacao, operador, servidor, porta);
					
					break;
				}
				
				case 3:
				{
					xmlRetorno = getXMLErro(msisdn);
					
					break;
				}
			}
			
			// Retorna os dados para a classe solicitante
			retornarDados(request, response, xmlRetorno);
		}		
		catch (Exception e)
		{
			logger.error("Erro na tarifacao/consulta do Brasil Vantagens");
				retornarDados(request, response, getXMLErro(msisdn));
		}
	}
	
	/**
	 * Metodo....: getXMLErro
	 * Descricao.: Construcao de um XML de erro generico
	 * 			   para retornar a classe solicitante
	 * 
	 * @param msisdn	- MSISDN do assinante
	 * @return xml		- String contendo o XML
	 */
	public static String getXMLErro (String msisdn)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		StringBuffer xml = new StringBuffer(Constantes.XML_PRO_LOG);
		xml.append("<mensagem>");
		xml.append("<cabecalho>");
		xml.append("<empresa>").append(Constantes.WIG_XML_EMPRESA).append("</empresa>");
		xml.append("<sistema>").append(Constantes.WIG_XML_SISTEMA).append("</sistema>");
		xml.append("<processo>").append(Constantes.WIG_XML_PROCESSO_GPP).append("</processo>");
		xml.append("<data>").append(sdf.format(Calendar.getInstance().getTime())).append("</data>");
		xml.append("<identificador_requisicao>").append(parseMsisdn(msisdn)).append("</identificador_requisicao>");
		xml.append("<codigo_erro>0</codigo_erro>");
		xml.append("<descricao_erro/>");
		xml.append("</cabecalho>");
		xml.append("<conteudo>");
		xml.append("<![CDATA[");
		xml.append(Constantes.XML_PRO_LOG);
		xml.append("<root>");
		xml.append("<GPPServico>");
		xml.append("<operacao>C</operacao>");
		xml.append("<codRetorno>").append(Constantes.WIG_XML_COD_ERRO_GENERICO).append("</codRetorno>");
		xml.append("<descRetorno>").append(Constantes.WIG_XML_ERRO_GENERICO).append("</descRetorno>");
		xml.append("<codServico/>");
		xml.append("<descServico/>");
		xml.append("<valor/>");
		xml.append("<assinante>");
		xml.append("<msisdn>").append(msisdn).append("</msisdn>");
		xml.append("<saldoPrincipal/>");
		xml.append("<saldoBonus/>");
		xml.append("<saldoSMS/>");
		xml.append("<saldoDados/>");
		xml.append("</assinante> ");
		xml.append("</GPPServico>");
		xml.append("</root>]]>");
		xml.append("</conteudo> ");
		xml.append("</mensagem>");
		
		return xml.toString();
	}
	
	/**
	 * Metodo....: parseMsisdn
	 * Descricao.: Realiza o parse do MSISDN, retirando os digitos 55 do inicio
	 * 
	 * @param msisdn	- MSISDN original com 10 ou 12 digitos
	 * @return String	- MSISDN formatado apenas com 10 digitos
	 */
	public static String parseMsisdn(String msisdn)
	{
		if (msisdn.length() == 12)
			return msisdn.substring(2);
		else
			return msisdn;
	}
	
	/**
	 * Metodo....: acaoBrasilVantagens
	 * Descricao.: Define a acao a ser tomada para o
	 * 			   assinante no GPP
	 * 
	 * @param acao	- Acao a ser tomada
	 * @return int	- Inteiro determinado pela acao
	 */
	private int acaoBrasilVantagens (String acao)
	{
		// Acao de cobranca (tarifacao) apenas
		if (Constantes.GPP_ACAO_COBRANCA_BRT_VANTAGENS.equalsIgnoreCase(acao))
			return 0;
		// Acao de atualizacao dos acessos do Amigos Toda Hora
		else if (Constantes.GPP_ACAO_ATUALIZACAO_ATH.equalsIgnoreCase(acao))
			return 1;
		// Acao de ativacao do Bumerangue 14
		else if (Constantes.GPP_ACAO_CADASTRO_BUMERANGUE.equalsIgnoreCase(acao))
			return 2;
		// Falha, caso a acao seja diferente das anteriores
		return 3;
	}
	
	/**
	 * Metodo....: retornarDados
	 * Descricao.: Retorna os dados para a classe solicitante
	 * 
	 * @param request	- HttpServletRequest
	 * @param response	- HttpServletResponse
	 * @param retorno	- String de retorno
	 */
	private void retornarDados(HttpServletRequest request, HttpServletResponse response, String retorno)
	{
		try 
		{
			ServletOutputStream out = response.getOutputStream();
			out.println(retorno);
			out.flush();
			out.close();
		} 
		catch (IOException e)
		{
			logger.error("Erro na escrita do resultado", e);
			new Exception("Erro de Comunicação");
		}
	}
}
