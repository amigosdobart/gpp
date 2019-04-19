package br.com.brasiltelecom.wig.servlet;

import br.com.brasiltelecom.wig.entity.MensagemPromocional;
import br.com.brasiltelecom.wig.dao.MensagemPromocionalDAO;
//import br.com.brasiltelecom.wig.util.SenderSMBySFM;
import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author Joao Carlos
 * Data..: 02/06/2005
 *
 */
public class GerenciadorMSGPromocional extends HttpServlet
{
	private Context initContext 	= null;
	private static final long serialVersionUID = 7526471155622776147L;
	
	//private String servidorOTA;
	//private String portaOTA;
	//private String usernameOTA;
	//private String passwordOTA;

	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			initContext = new InitialContext();
			//servidorOTA = (String)arg0.getServletContext().getAttribute("ServidorOTA");
			//portaOTA    = (String)arg0.getServletContext().getAttribute("PortaServidorOTA");
			//usernameOTA = (String)arg0.getServletContext().getAttribute("UsuarioOTA");
			//passwordOTA = (String)arg0.getServletContext().getAttribute("SenhaUsuarioOTA");
		} catch (NamingException e)
		{
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		Connection con = null;
		PrintWriter out = response.getWriter();
		try
		{
			// Define os parametros recebidos. MSISDN para indicar qual o assinante destino da Mensagem
			// e qual o codigo da promocao a ser utilizado para pesquisa da mensagem a ser enviada
			String msisdn   = request.getParameter("MSISDN");
			int codPromocao = Integer.parseInt(request.getParameter("pr"));
			int codServico  = Integer.parseInt(request.getParameter("bts"));
			int codConteudo = Integer.parseInt(request.getParameter("btc"));
			// Busca no pool de recursos qual a conexao com o banco de dados deve ser utilizada
			DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/WPP_WIGC");
			con = ds.getConnection();
			// Realiza a pesquisa da mensagem promocional a ser utilizada e usa a classe de envio
			// de SM para o destino que eh o assinante recebido como parametro
			MensagemPromocional msg = MensagemPromocionalDAO.getInstance().findByCodigo(codPromocao,codServico,codConteudo,con);
			String senhaPromo = MensagemPromocionalDAO.getInstance().getSenhaPromocional(msg,msisdn,con);
			
			// Prepara o envio de uma mensagem simples para indicar que o processamento foi Ok
			// enviando o WML plugin de envio do SM
			out.println(WIGWmlConstrutor.getWMLSM(msisdn,msg.getMensagemPromocional()+" "+(senhaPromo != null ? senhaPromo : "")));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println(WIGWmlConstrutor.getWMLErro("Erro 1: Servico Temporariamente Indisponivel. Tente mais tarde em alguns instantes."));
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
				out.println(WIGWmlConstrutor.getWMLErro("Erro 1: Servico Temporariamente Indisponivel. Tente mais tarde em alguns instantes."));
			}
		}
		out.flush();
		out.close();
	}
}

