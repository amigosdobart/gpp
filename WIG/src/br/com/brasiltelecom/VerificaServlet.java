
package br.com.brasiltelecom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class VerificaServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private InitialContext ictx = null;
	private String DESTINO=null;
	private String WIGMSISDN = null;
	private String CONNECTION_DS = null;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			ictx = new InitialContext();
			DESTINO = arg0.getInitParameter("DESTINO");
			WIGMSISDN = arg0.getInitParameter("WIGMSISDN");
			CONNECTION_DS = arg0.getInitParameter("CONNECTION_DS");
		} catch (NamingException e)
		{
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		response.setContentType("text/html; charset=iso-8859-1");
		try
		{
			String query = request.getQueryString();
			String BrtIPDest = request.getParameter(DESTINO);
			String url;

			if (query.indexOf("http://") == -1)
				url = "http://aclwireless10.com/wibsap?" + query;
			else
				url = query.substring(0,query.indexOf("&")) + "?" + query.substring(query.indexOf("&")+1);

			if (BrtIPDest != null) 
			{
				query = query.substring(10);
				url = query.substring(0,query.indexOf("&")) + "?" + query.substring(query.indexOf("&")+1);
			}

			String msisdn = request.getParameter(WIGMSISDN);
			// DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_DS");
			DataSource ds = (DataSource) ictx.lookup(CONNECTION_DS);
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT 1 "+ 
							  	   "FROM tbl_apr_bloqueio_servico "+ 
								   "where "+ 
								   "idt_msisdn = '" + msisdn + "' and "+ 
							  	   "id_servico = 'ELM_BLACK_LIST'");
			if(rs.next())
				mensagemdeErro(response);
			else
				response.sendRedirect(url);
		}
		catch (NamingException e)
		{
			e.printStackTrace();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs != null)
				{
					rs.close();
				}
				if( stmt != null )
				{
					stmt.close();
				}
				if(con != null ){
					con.close();
				}
			}
			catch(SQLException e)
			{}
		}
	}

	private void mensagemdeErro(HttpServletResponse response)throws IOException
	{
		ServletOutputStream out = response.getOutputStream();
		out.println("<wml>");
		out.println("<card>");
		out.println("<p>");
		out.println("Pre-pago sem credito para esta operacao.");
		out.println("</p>");
		out.println("</card>");
		out.println("</wml>");
	}
}