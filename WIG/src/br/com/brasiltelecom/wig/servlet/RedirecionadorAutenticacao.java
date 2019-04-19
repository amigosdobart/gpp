package br.com.brasiltelecom.wig.servlet;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.action.Autenticador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Joao Carlos
 * Data..: 02/06/2005
 *
 */
public class RedirecionadorAutenticacao extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private String	wigContainer= null;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			wigContainer = (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
			// Busca a instancia do autenticador e define o tempo maximo de logon possivel
			// atraves da propriedade definido no web.xml
			long tempoExpiracaoLogon = Long.parseLong((String)arg0.getServletContext().getAttribute("TempoExpiracaoLogon"));
			Autenticador aut = Autenticador.getInstance();
			aut.setTempoMaximoExpiracao(tempoExpiracaoLogon);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		try
		{
			// Le todos os parametros esperados. O valor da querystring original 
			// sera lida como parametro e posteriormente redirecionada apos 
			// autenticado o assinante. O codigoServico eh o codigo de autenticacao
			String url		 	= request.getParameter("req");
			String msisdn 		= request.getParameter("MSISDN");
			int codigoAcesso  	= request.getParameter("cod") != null ? Integer.parseInt(request.getParameter("cod")) : 0;

			// Busca a instancia do programa que ira realizar a autenticacao
			Autenticador aut = Autenticador.getInstance();
			// Se o assinante jah estah autenticado entao realiza o redirecionamento
			if (aut.estaAutenticado(msisdn))
				// Redireciona para a URL
				response.sendRedirect(url);
			else
				// O codigo de acesso diferente de 0 indica que o assinante estah se identificando
				// para ser autenticado portanto se esse valor for diferente de zero realiza a 
				// autenticacao. Se ok entao redireciona para a URL senao retorna um wml de erro.
				if (codigoAcesso != 0)
					if (aut.autenticaUsuario(msisdn,codigoAcesso))
						// Redireciona para a url requisitada
						response.sendRedirect(url);
					else out.println(WIGWmlConstrutor.getWMLErro("Erro 10: Codigo de acesso invalido"));
				else
					// Se o assinante nao estah autenticado e o codigo de acesso possui valor 0 entao
					// um WML de resposta eh enviado para que o mesmo digite o seu codigo de acesso
					out.println(aut.getWMLAutenticacao(msisdn,wigContainer,url));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println(WIGWmlConstrutor.getWMLErro("Erro 1: Servico Temporariamente Indisponivel. Tente mais tarde em alguns instantes."));
		}
		out.flush();
		out.close();
	}
}
