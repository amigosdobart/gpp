
package br.com.brasiltelecom;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class Modelo extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private InitialContext ictx = null;
	public static final String SEPARADOR = "/";
	
	public void init(ServletConfig arg0) throws ServletException
	{
		try
		{
			ictx = new InitialContext();
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
			String url;
			String modelo = "";
			String Fabricante;

			url = query.substring(0,query.indexOf("&")) + "?" + query.substring(query.indexOf("&")+1);

			String msisdn = request.getParameter("MSISDN");

			//DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_PREPAGO");
			DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(
                    "select NO_MODELO, NO_FABRICANTE " +
					   "from hsid_cliente a, hsid_modelo b, hsid_fabricante c, hsid_modelo_tac d " +
					   "where d.co_modelo = b.co_modelo " +
					   "and b.co_fabricante = c.co_fabricante " +
                       "and d.co_tac = substr(a.nu_imei,1,8) " +
					   "and a.nu_msisdn = '"+msisdn+"' " +
					   "and a.co_modelo <> 0 "+
			        "order by no_modelo");

			String separador="";
			if(rs.next())
			{
				//do
				//{
				//	if (!rs.isFirst())
				//		separador = Modelo.SEPARADOR;
	
					modelo += separador+rs.getString(1);
					Fabricante=rs.getString(2);
				//}
				//while (rs.next());
				//modelo = URLEncoder.encode(modelo,"UTF-8");
				Fabricante = URLEncoder.encode(Fabricante,"UTF-8");
				response.sendRedirect(url+"&modelo=" + modelo + "&fabricante=" + Fabricante);
			}
			else
				response.sendRedirect(url+"&modelo=no&fabricante=no");
		} catch (NamingException e)
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
					rs.close();
				if( stmt != null )
					stmt.close();
				if(con != null )
					con.close();
			}
			catch(SQLException e)
			{}
		}
	}
}