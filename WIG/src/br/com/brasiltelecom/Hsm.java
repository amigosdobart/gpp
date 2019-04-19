
package br.com.brasiltelecom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class Hsm extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private InitialContext ictx = null;
	private ServletContext ctx = null;
	private Logger log = null;
	
	public void init(ServletConfig config) throws ServletException
	{
		try
		{
			ictx = new InitialContext();
			ctx  = config.getServletContext();
			log = Logger.getLogger(this.getClass());
		} catch (NamingException e)
		{
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
		String msisdn = request.getParameter("MSISDN");	
		String situacao = request.getParameter("sit");
		String querystring = request.getQueryString();
		String Imei = CalculaIMEI(querystring.substring(6));

		Connection con = null;
		String mensagem;
		int retorno;

		try
		{
			DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_PREPAGO");
			con = ds.getConnection();
			CallableStatement procedure = con.prepareCall("call hsidpac.HSM_RETORNO_TSD(?,?,?,?,?)");
			procedure.setString(1,msisdn);
			procedure.setString(2,situacao);
			procedure.setString(3,Imei);
			procedure.registerOutParameter(4, Types.NUMERIC);
			procedure.registerOutParameter(5, Types.VARCHAR);
			procedure.execute();
			retorno = procedure.getInt(4);
			mensagem = procedure.getString(5);
			procedure.close();
			PrintWriter out = response.getWriter();

			if (retorno == 1)
			{
				situacao = EnviaRequestDM(querystring.substring(6));
				procedure = con.prepareCall("call hsidpac.HSM_RETORNO_TSD(?,?,?,?,?)");
				procedure.setString(1,msisdn);
				procedure.setString(2,situacao);
				procedure.setString(3,Imei);
				procedure.registerOutParameter(4, Types.NUMERIC);
				procedure.registerOutParameter(5, Types.VARCHAR);
				procedure.execute();
				mensagem = procedure.getString(5);
				procedure.close();
			}
			out.print(mensagem);
		}
		catch(SQLException e)
		{}
		catch(Exception e)
		{}
		finally
		{
			try
			{
				con.close();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private String EnviaRequestDM(String queryargs)
	{
		try 
		{
			log.debug("http://" + ctx.getAttribute("ServidorOTA") + ":" + ctx.getAttribute("PortaEventAdapter") + "/httpeventadapter/tse?"+ queryargs);
			URL endereco = new URL("http://" + ctx.getAttribute("ServidorOTA") + ":" + ctx.getAttribute("PortaEventAdapter") + "/httpeventadapter/tse?"+ queryargs);
			URLConnection conn = endereco.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			String linha = "";
			while( (linha = in.readLine()) != null )
				buffer.append(linha);

			log.debug(buffer.toString());
			if (buffer.indexOf("OK") >= 0)
				return "5";
			else
				return "8";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "8";
		}
	}

	public String CalculaIMEI (String querystring)
	{
		String[] arrquerystring = querystring.split("&");
		String imei;
		if (arrquerystring[0].substring(2,3).equals("%") )
			imei = arrquerystring[0].substring(3,arrquerystring[0].length());
		else
			imei = arrquerystring[0].substring(2,arrquerystring[0].length());
		String[] arrayimei = imei.split("%");
		String imei_final = "";
		String imei_tmp;

		for (int i=0; i< arrayimei.length;i++)
		{
			if (arrayimei[i].length() == 1)
			{
				char[] arrimei_tmp = arrayimei[i].toCharArray();
				for (int n=0; n<arrimei_tmp.length;n++)
				{
					imei_tmp = Integer.toHexString(arrimei_tmp[n]);
					if ((imei_tmp.substring(1,2).equals("0")) || (imei_tmp.substring(1,2).equals("1")) || (imei_tmp.substring(1,2).equals("2")) || (imei_tmp.substring(1,2).equals("3"))  || (imei_tmp.substring(1,2).equals("4")) || (imei_tmp.substring(1,2).equals("5")) || (imei_tmp.substring(1,2).equals("6")) || (imei_tmp.substring(1,2).equals("7")) || (imei_tmp.substring(1,2).equals("8")) || (imei_tmp.substring(1,2).equals("9")) )
						imei_final=imei_final+imei_tmp.substring(1,2) + imei_tmp.substring(0,1);
					else
						imei_final=imei_final+"0" + imei_tmp.substring(0,1);
				}
			}

			if (arrayimei[i].length() == 2)
				imei_final=imei_final+arrayimei[i].substring(1,2) + arrayimei[i].substring(0,1);

			if (arrayimei[i].length() >= 3)
			{
				imei_final=imei_final+arrayimei[i].substring(1,2) + arrayimei[i].substring(0,1);
				char[] arrimei_tmp = arrayimei[i].substring(2,arrayimei[i].length()).toCharArray();
				for (int n=0; n<arrimei_tmp.length;n++)
				{
					imei_tmp = Integer.toHexString(arrimei_tmp[n]);
					if ((imei_tmp.substring(1,2).equals("0")) || (imei_tmp.substring(1,2).equals("1")) || (imei_tmp.substring(1,2).equals("2")) || (imei_tmp.substring(1,2).equals("3"))  || (imei_tmp.substring(1,2).equals("4")) || (imei_tmp.substring(1,2).equals("5")) || (imei_tmp.substring(1,2).equals("6")) || (imei_tmp.substring(1,2).equals("7")) || (imei_tmp.substring(1,2).equals("8")) || (imei_tmp.substring(1,2).equals("9")) )
						imei_final=imei_final+imei_tmp.substring(1,2) + imei_tmp.substring(0,1);
					else
						imei_final=imei_final+"0" + imei_tmp.substring(0,1);
				}
			}
		}

		if (   (imei_final.substring(0,1).equals("0")) || (imei_final.substring(0,1).equals("1")) || (imei_final.substring(0,1).equals("2")) || (imei_final.substring(0,1).equals("3")) || (imei_final.substring(0,1).equals("4")) || (imei_final.substring(0,1).equals("5")) || (imei_final.substring(0,1).equals("6")) || (imei_final.substring(0,1).equals("7")) || (imei_final.substring(0,1).equals("8")) || (imei_final.substring(0,1).equals("9")))
			imei_final = imei_final.substring(0,imei_final.length()-1);
		else
			imei_final = imei_final.substring(1,imei_final.length());

		/* Aqui entra a rotina de calcular o dig verificador */

		int totala = 0;
		int totalb = 0;
		int totaldigver;
		int somainda;
		int digver;
		char[] arrdigver = imei_final.toCharArray();

		for (int i=0; i<arrdigver.length;i++)
		{
			if (i==1 || i==3 || i==5 || i==7 || i==9 || i==11 || i==13)
			{
				somainda = 2 * Integer.parseInt(Character.toString(arrdigver[i]));
				if (somainda >= 10)
					totala = totala + somainda - 9;
				else
					totala = totala + somainda;
			}

			if (i==0 || i==2 || i==4 || i==6 || i==8 || i==10 || i==12)
				totalb = totalb + Integer.parseInt(Character.toString(arrdigver[i]));
		}
		totaldigver = totala + totalb;
		digver = 10 * ((totaldigver/10) + 1) - totaldigver; 
		if (digver == 10) digver=0;
		imei_final = imei_final.substring(0,14) + digver;
		return imei_final;
	}
}