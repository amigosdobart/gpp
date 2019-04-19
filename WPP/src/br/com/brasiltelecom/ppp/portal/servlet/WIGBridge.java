package br.com.brasiltelecom.ppp.portal.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

import java.io.IOException;

/**
 * Esta classe tem a responsabilidade de redirecionar a execucao de alguma servlet no portal pre-pago
 * realizando primeiro a autenticacao do usuario.
 * Parametros
 * 		u = Usuario para autenticacao no prepago
 * 		s = Senha do usuario
 * 		a = Acao a ser executada (URL)
 * 
 * Ex: http://servidor:porta/ppp/WIGBridge?u=WIG&s=wig99&a="/transfBonus?msisdn=556184097799%26OP=I
 * 
 * OBS: Ao passar como parametro a acao se a servlet a ser redirecionada necessitar de mais de um
 *      parametro entao a separacao desses NAO pode ser feita atraves do caracter '&' e sim do %26
 *      que e a codificacao para este. Dica: utilizar URLEncoder para traduzir tal string
 * @author Joao Carlos
 * Data..: 25/04/2005
 * 
 */
public class WIGBridge extends HttpServlet 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private ServletContext servletContext = null;

	protected void service(HttpServletRequest request, HttpServletResponse response)
	{
		JDO jdo = null;
		Database db = null;
		try 
		{
			// Inicializa acesso as classes para acesso ao banco de dados (Castor)
			jdo = (JDO) servletContext.getAttribute(Constantes.JDO);
			db  = jdo.getDatabase();
			
			HttpSession session = request.getSession(true);
			// Realiza o logon buscando o nome do usuario e senha informados como parametro
			String logon = (String)request.getParameter("u");
			String senha = ((String)request.getParameter("s")).toLowerCase();
			if (logon==null || senha ==null)
				throw new Exception("Logon do usuário não informado.");

			// Realiza a pesquisa do usuario no banco de dados para validacao do logon
			db.begin();
			Usuario usuario = UsuarioHome.findByID(db,logon);
			if(db.isActive())
				db.commit();

			if(!db.isClosed())
				db.close();

			// Se a senha nao esta correta ou usuario nao encontrado entao o
			// logon nao e autenticado
			if (usuario==null||!senha.equals(usuario.getSenha()))
				throw new Exception("Logon inválido no PPP. Usuario:"+logon);
			
			// Define o usuario autenticado na sessao para futuro uso
			session.setAttribute(Constantes.USUARIO,usuario);
			
			// Realiza o redirecionamento da servlet a ser executada
			String urlQuery = "/"+(String)request.getParameter("a");
			RequestDispatcher dispatcher = servletContext.getRequestDispatcher(urlQuery);
			dispatcher.forward(request,response);
		}		
		catch (Exception e)
		{
			logger.error("Erro no processo de interconexao entre WIG e o portal PrePago");
			if (request.getAttribute(Constantes.MENSAGEM)!=null)
				retornarDados(request,response,(String)request.getAttribute(Constantes.MENSAGEM));
			else
				retornarDados(request,response,"Erro na interconexao entre WIG e o portal PrePago. Erro:"+e.getMessage());
		}
		finally
		{
			if(db != null && !db.isClosed())
			{
				try 
				{
					db.rollback();
					db.close();
				} 
				catch (PersistenceException e1) 
				{
				}
			}
		}
	}

	public void init(ServletConfig scfg) throws ServletException 
	{
		servletContext = scfg.getServletContext();		
	}
	
	private void retornarDados(HttpServletRequest request, HttpServletResponse response,String retorno)
	{
		try 
		{
			ServletOutputStream out = response.getOutputStream();
			out.println(retorno+"<br/>");
			out.flush();
	
		} catch (IOException e) {
			logger.error("Erro na escrita do resultado", e);
			new Exception("Erro de Comunicação");
		}
	}

}
