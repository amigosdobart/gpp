package br.com.brasiltelecom.wig.servlet;

import br.com.brasiltelecom.wig.dao.ServicoDAO;
import br.com.brasiltelecom.wig.dao.ConteudoDAO;
import br.com.brasiltelecom.wig.dao.RespostaDAO;
import br.com.brasiltelecom.wig.dao.RespostaFiltroDAO;
import br.com.brasiltelecom.wig.dao.MensagemPromocionalDAO;

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
public class GerenciadorCacheDados extends HttpServlet
{
	private static final long serialVersionUID = 7526471155622776147L;

	public void init(ServletConfig arg0) throws ServletException
	{
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		try
		{
			// Faz a limpeza dos caches
			ServicoDAO.getInstance().limpaCache();
			ConteudoDAO.getInstance().limpaCache();
			RespostaDAO.getInstance().limpaCache();
			RespostaFiltroDAO.getInstance().limpaCache();
			MensagemPromocionalDAO.getInstance().limpaCache();
			out.println("<html><body>Limpeza do cache efetuada</body></html>");
		}
		catch(Exception e)
		{
			out.println(e.getMessage());
		}
		out.flush();
		out.close();
	}
}
