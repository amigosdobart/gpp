package br.com.brasiltelecom.wig.servlet.mobileMkt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.dao.MobileMktDAO;
import br.com.brasiltelecom.wig.entity.MobileMktQuestionario;


public class ShowMobileMkt extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private String wigContainer;
	private Context initContext = null;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext = new InitialContext();
			wigContainer  = (String)arg0.getServletContext().getAttribute("VariavelWIGContainer");
		} catch (NamingException e)
		{
			e.printStackTrace();
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		Logger logger = Logger.getLogger(this.getClass());
		PrintWriter out = response.getWriter();
		try
		{
			// Busca no pool de recursos qual a conexao com o banco de dados deve ser utilizada
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/WPP_WIGC");
			
			// O programa para exibir o WML a ser enviado contendo a pesquisa de marketing
			// inicia realizando o parse dos parametros de qual pesquisa ser enviada
			// e utiliza uma classe DAO para realizar tal consulta
			int idPesquisa     = Integer.parseInt(request.getParameter("idPesquisa"));
			int idQuestionario = Integer.parseInt(request.getParameter("idQuestionario"));
			
			// Consulta para buscar o objeto da pesquisa de marketing e monta
			// o WML retornando para o assinante.
			MobileMktDAO mbmktDAO = MobileMktDAO.getInstance();
			MobileMktQuestionario quest = mbmktDAO.findQuestionarioByID(idPesquisa,idQuestionario,ds);
			
			// Redireciona a montagem do WML para um JSP
			// indicando o objeto questionario no objeto Request
			// da conexao HTTP
			request.setAttribute("questionarioMkt",quest);
			request.setAttribute("wigContainer"   ,wigContainer);
			request.getRequestDispatcher("ShowQuestionarioMkt.jsp").forward(request,response);
		}
		catch(Exception e)
		{
			logger.error("Erro ao retornar WML de pesquisa de marketing.",e);
			out.println(WIGWmlConstrutor.getWMLErro("Erro 1: Servico Temporariamente Indisponivel. Tente mais tarde em alguns instantes."));
		}
		finally
		{
			out.flush();
			out.close();
		}
	}
}
 
