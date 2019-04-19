
package br.com.brasiltelecom;

import java.io.IOException;
//import java.io.PrintWriter;
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

public class Tsd extends HttpServlet
{
	private InitialContext ictx = null;
	private Logger log = null;
	private ServletContext ctx = null;
	
	public void init(ServletConfig config) throws ServletException
	{
		try
		{
			ictx = new InitialContext();
			log = Logger.getLogger(this.getClass());
			ctx = config.getServletContext();
		}
		catch (NamingException e)
		{
			e.printStackTrace();
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		String querystring = request.getQueryString();
		String msisdn = request.getParameter("MSISDN");	
		String iccid = request.getHeader("X-Wig-IccId");
		//String iccid = "12345";
		
		log.debug("MSISDN: "+ msisdn + ", iccid: " + iccid);
		
		String[] arrquerystring = querystring.split("&");
		String imei;
		
		if (arrquerystring[0].substring(2,3).equals("%") )
			imei = arrquerystring[0].substring(3,arrquerystring[0].length());
		else
			imei = arrquerystring[0].substring(2,arrquerystring[0].length());
		
		String[] arrayimei = imei.split("%");
		String imei_final = "";
		String imei_tmp;
		
		// Faz a verificacao do IMEI do assinante
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
		
		/* Aqui faz o redirect para o DM */  
		
		Connection con = null;
		try
		{
			DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_PREPAGO");
			con = ds.getConnection();
			CallableStatement procedure = con.prepareCall("begin hsidpac.HSID_ATUALIZA_CLIENTE(?,?,?,?,?); end;");
			procedure.setString(1,msisdn);
			procedure.setString(2,iccid);
			procedure.setString(3,imei_final);
			procedure.registerOutParameter(4, Types.NUMERIC);
			procedure.registerOutParameter(5, Types.VARCHAR);
			procedure.execute();
			int retorno = procedure.getInt(4);
			String mensagem = procedure.getString(5);
			
			procedure.close();
			
			String configDM = "";
			if(mensagem.indexOf("providelocalinfo") > 0)
				configDM = "S";
			else
				configDM = "N";
			
			CadPre cadpre = new CadPre();
            String clarifyCadastro = (String) ctx.getAttribute("ClarifyCadastro");
            
            // String cadastro = cadpre.cadastro(msisdn, "0", "", "", "", "", "", "", "", "", "", configDM, 0, clarifyCadastro, ictx, request);
            cadpre.cadastro(msisdn, "0", "", "", "", "", "", "", "", "", "", configDM, 0, clarifyCadastro, ictx, request);
            
            if(retorno == 0 || retorno == 2)
            	request.getRequestDispatcher("/cadastro_pre.jsp").forward(request, response);
            else
            	if(retorno == 1)
            		response.sendRedirect("http://" + ctx.getAttribute("ServidorOTA") + ":" + ctx.getAttribute("PortaEventAdapter") + "/httpeventadapter/tse?" + querystring);

		}
		catch(SQLException e)
		{
			log.error("Erro de SQL. Erro: ", e);
		}
		catch(Exception e)
		{
			log.error("Erro generico. Erro: ", e);
		}
		finally
		{
			try
			{
				if (con != null && !con.isClosed())
					con.close();
			}
			catch (SQLException e1)
			{
				log.error("Erro na tentativa de fechar a conexao com o Banco de Dados. Erro: ", e1);
			}
		}
	}
}