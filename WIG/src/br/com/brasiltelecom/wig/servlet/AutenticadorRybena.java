package br.com.brasiltelecom.wig.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.brasiltelecom.ppp.util.LDAP.AssinanteNaoExistenteNDSException;
import br.com.brasiltelecom.ppp.util.LDAP.ConsultaAssinanteNDS;

/**
 * @author Joao Paulo Galvagni
 * Data..: 14/09/2005
 * 
 * @param MSISDN
 * @param SENHA
 * 
 * Classe para autenticar o cliente no NDS
 * 
 */

public class AutenticadorRybena extends HttpServlet
{
	private static final long serialVersionUID = 1L;


	private String servidorLDAP 	= null;
	private int    portaLDAP;
	private String dominioLDAP  	= null;
	private String passDomainLDAP	= null;
	private String baseSearch		= null;
	private String certificadoLDAP	= null;

	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			// Mapeia as informações necessárias para as conexoes
			servidorLDAP 	= (String)arg0.getServletContext().getAttribute("nomeServidorLDAP");
			portaLDAP	 	= Integer.parseInt((String)arg0.getServletContext().getAttribute("portaServidorLDAP"));
			dominioLDAP	 	= (String)arg0.getServletContext().getAttribute("nomeDomainLDAP");
			passDomainLDAP	= (String)arg0.getServletContext().getAttribute("passwordDomainLDAP");
			baseSearch		= (String)arg0.getServletContext().getAttribute("baseSearchLDAP");
			certificadoLDAP	= arg0.getServletContext().getRealPath((String)arg0.getServletContext().getAttribute("nomeArquivoCertificadoLDAP"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		// Recebe os parametros a serem validados
		String msisdn = null;
		String senha = "00000";

		if (request.getParameter("msisdn") != null)
			msisdn = request.getParameter("msisdn");
		if (request.getParameter("senha") != null)
			senha  = request.getParameter("senha");
		String retorno = "";

		// Retornos para a validação:
		// 700 - (USER_VALIDATION_OK)	- Autenticação realizada com sucesso
		// 701 - (USER_VALIDATION_NOK)	- Falha na autenticação, senha incorreta
		// 702 - (USER_NOT_FOUND)		- Falha na autenticação, usuário não encontrado
		// 703 - (INVALID_PASSWORD)		- Falha na autenticação, senha <> 6 dígitos
		// 704 - (INSUFFICIENT_BALANCE)	- Saldo insuficiente (Não implementado)

		if (senha == null || senha.length() < 6 || senha.length() > 6)
		{
			// Erro 703 - Senha <> 6 dígitos
			response.sendError(703);
		}
		else
		{
			ConsultaAssinanteNDS consultaAssinanteNDS = new ConsultaAssinanteNDS(servidorLDAP,portaLDAP,dominioLDAP,passDomainLDAP,baseSearch,certificadoLDAP);
			try
			{
				if (consultaAssinanteNDS.validaSenhaServicos(msisdn,senha))
				{
					// Retorno 700 - Usuário Validado com sucesso
					response.sendError(700);
				}
				else
				{
					// Erro 701 - Senha incorreta
					response.sendError(701);
				}
			}
			catch (AssinanteNaoExistenteNDSException e)
			{
				// Erro 702 - Usuário não encontrado
				response.sendError(702);
			}
			catch(Exception e)
			{
				out.println(e);
			}
		}
		// Retorna um WML como resposta secundaria (Primaria = sendError()
		out.println(retorno);
		out.flush();
		out.close();
	}
}



