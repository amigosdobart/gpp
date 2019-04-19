/*
 * Created on 27/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

/**
 * @author Alberto Magno
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultaXMLGPP extends HttpServlet
{
	Logger logger = Logger.getLogger(this.getClass());

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    {
            forneceXML(req, resp);
    }

    private void forneceXML(HttpServletRequest req, HttpServletResponse resp)
    {

        try
        {
            if (req.getParameter("S")!=null&&req.getParameter("S").equals("tot"))
            {
				imprimeXML(resp, req.getParameter("MS"), req.getParameter("DI"), req.getParameter("DF"));
            	
            }else
            if(req.getParameter("S")!=null&&req.getParameter("S").equalsIgnoreCase("pula")){
				imprimeXMLPulaPula(resp, req.getParameter("MS"), req.getParameter("DI"), req.getParameter("DF"));
            	
            }
            else
            {
				String msisdn = req.getParameter("obr_msisdn");
				String dataInicial = req.getParameter("dataInicial");
				String dataFinal = req.getParameter("dataFinal");
				imprimeXML(resp, msisdn, dataInicial, dataFinal);
			}
        }
        catch(IOException e)
        {
			logger.error("Erro de IO na geração do XML de extrato para relatorio (" +
							e.getMessage() + ")");
            e.printStackTrace();
        }
    }

	private void imprimeXML(HttpServletResponse resp, String msisdn, String dataInicial, String dataFinal)
	throws IOException
	{
		resp.setContentType("text/xml");
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"WINDOWS-1252\" ?>");
		if(msisdn.length() == 10)
			msisdn = "55" + msisdn;
		String servidor = (String)getServletContext().getAttribute("GPPNomeServidor");
		String porta = (String)getServletContext().getAttribute("GPPPortaServidor");
		try
		{
			out.println(ConsultaExtratoGPP.getXmlRelatorio(msisdn, dataInicial, dataFinal, servidor, porta));
		}
		catch(Exception e)
		{
			out.println("<ERROR>Erro contactando GPP. Consulta  MS:"+msisdn+" DI:"+dataInicial+" DF:"+dataFinal+"</ERROR>");
			logger.error("Erro contactando GPP para pegar XML de extrato (" +
							e.getMessage() + ")MS:"+msisdn+" DI:"+dataInicial+" DF:"+dataFinal);
			e.printStackTrace();
		}
			
	}
	private void imprimeXMLPulaPula(HttpServletResponse resp, String msisdn, String dataInicial, String dataFinal)
	throws IOException
	{
		resp.setContentType("text/xml");
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"WINDOWS-1252\" ?>");
		if(msisdn.length() == 10)
			msisdn = "55" + msisdn;
		String servidor = (String)getServletContext().getAttribute("GPPNomeServidor");
		String porta = (String)getServletContext().getAttribute("GPPPortaServidor");
		try
		{
			out.println(ConsultaExtratoPulaPulaGPP.getXml(msisdn, dataInicial, dataFinal, servidor, porta, false));
		}
		catch(Exception e)
		{
			out.println("<ERROR>Erro contactando GPP. Consulta  MS:"+msisdn+" DI:"+dataInicial+" DF:"+dataFinal+"</ERROR>");
			logger.error("Erro contactando GPP para pegar XML de extrato Pula-pula(" +
							e.getMessage() + ")MS:"+msisdn+" DI:"+dataInicial+" DF:"+dataFinal);
			e.printStackTrace();
		}
			
	}
}
