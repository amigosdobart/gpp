package br.com.brasiltelecom.wig.servlet.brtVantagens;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.dao.BrtVantagensDAO;
import br.com.brasiltelecom.wig.entity.BrtVantagem;
import br.com.brasiltelecom.wig.util.ResolvedorNomeServidor;

/**
 * 
 * @author	JOÃO PAULO GALVAGNI
 * @since	04/04/2006
 */
public class AmigosTodaHoraConsulta extends HttpServlet
{
	private Context initContext  = null;
	private String  wigControl	 = null;
	private int portaServidorWIG = 0;
	private String ipEntireX	 = null;
	private int portaEntireX	 = 0;
	private Logger logger 		 = Logger.getLogger(this.getClass());
	
	public synchronized void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext 	  = new InitialContext();
			wigControl    	  = (String)arg0.getServletContext().getAttribute("VariavelWIGControl");
			portaServidorWIG  = Integer.parseInt((String) arg0.getServletContext().getAttribute("PortaServidorWIG")); 
			ipEntireX		  = (String)arg0.getServletContext().getAttribute("ServidorEntireX");
			portaEntireX	  = Integer.parseInt((String)arg0.getServletContext().getAttribute("PortaServidorEntireX"));
		}
		catch (Exception e)
		{
			// Grava no LOG uma entrada ERROR
			logger.error(e);
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		//String maquinaWIG = request.getServerName()+":"+portaServidorWIG;
		String maquinaWIG = ResolvedorNomeServidor.getInstance().resolveNome(request.getServerName())+":"+portaServidorWIG;
		
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
			BrtVantagensDAO consultaATH = new BrtVantagensDAO();
			
			// Realizacao da consulta do cliente via EntireX
			BrtVantagem brtVantagemATH = consultaATH.getBrtVantagensByMsisdn(msisdn, ipEntireX, portaEntireX);
			
			// Nao ha distincao entre o Novo e Novissimo Brasil Vantagens
			switch (validaRegrasAssinante(brtVantagemATH))
			{
				// Todas as validacoes foram efetuadas e o assinante esta OK
				case 0:
				{
					// Setando o objeto brtVantagemATH como atributo 
					// de sessao para posterior utilizacao
					sessaoATH.setAttribute("brtVantagem",brtVantagemATH);
					sessaoATH.setAttribute("maquinaWIG",maquinaWIG);
					
					// Grava no LOG uma entrada DEBUG
					logger.debug("WML de retorno sendo enviado para o assinante " + msisdn);
					
					// Redireciona a requisicao para o ShowAmigosTodaHora / ShowNovissimoAmigosTodaHora
					// conforme servico do assinante
					if (brtVantagemATH.isNovissimoATH())
						request.getRequestDispatcher("/ShowNovoAmigosTodaHora.jsp").forward(request,response);
					else
						request.getRequestDispatcher("/ShowAmigosTodaHora.jsp").forward(request,response);
					break;
				}
				// Objeto brtVantagem null
				case 1:
				{
					// Mostra uma mensagem de erro ao cliente e a opcao de uma nova tentativa
					out.println(WIGWmlConstrutor.getWMLErro("Erro inesperado. Clique OK e tente novamente.\n" + wmlNovaTentativa));
					break;
				}
				// Assinante não esta ativo ou possui pendencias no CLY
				case 2:
				{
					// Grava no LOG uma entrada DEBUG
					logger.debug("Assinante " + msisdn + " com pendencia no sistema Clarify.");
					// Assinante com pendencia no sistema Clarify
					out.println(WIGWmlConstrutor.getWMLInfo("Telefone nao pode ser cadastrado. Favor entrar em contato com a Central de Relacionamento."));
					break;
				}
				// Quantidade de numeros para cadastro == 0
				case 3:
				{
					// Grava no LOG uma entrada DEBUG
					logger.debug("Assinante " + msisdn + " nao tem direito a cadastrar nenhum numero na promocao ATH.");
					// Assinante com problemas na quantidade de numeros na promocao
					out.println("Quantidade de Amigos Toda Hora invalida. Favor entrar em contato com a Central de Relacionamento.");
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
	 * Metodo....: validaRegrasAssinante
	 * Descricao.: Realiza as devidas validacoes necessarias para que o assinante
	 *             possa realizar a consulta, alteracao, cadastro ou exclusao de
	 *             acessos na promocao Amigos Toda Hora
	 * 
	 * @param brtVantagem	- Objeto ja com os valores do Clarify
	 * @return int			- Inteiro, dependente das validacoes
	 */
	public int validaRegrasAssinante (BrtVantagem brtVantagem)
	{
		// Valida se o objeto eh nulo
		if (brtVantagem == null)
			return 1;
		// Valida se o assinante esta ativo no CLY
		if (!brtVantagem.isAtivo())
			return 2;
		// Valida se o assinante tem direito a pelo menos 1 num na promocao ATH
		if (brtVantagem.getQtdeAmigosTodaHora() == 0)
			return 3;
		
		// Retorno 0 para validacoes OK
		return 0;
	}
}