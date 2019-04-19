

/*
 * Created on 27/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author Alberto Magno
 *
 * ESSA CLASSE NAO EH MAIS UTILIZADA. A CONVERSAO DE XML PARA EXCEL EH FEITA PELA CLASSE
 * br.com.brasiltelecom.ppp.portal.relatorio.ProcessaRelatorioReports
 * 
 * OBS: A CONVERSAO AINDA EH FEITA POR XSL E, PORTANTO, O RESULTADO DEPENDERA DO TEMPLATE.
 * NA PRATICA O SITEMA ESTA GERANDO HTML (E NAO EXCEL)
 * 
 */
public class REP2EXCEL extends HttpServlet
{
	private static final long serialVersionUID = 4758010253386783742L;
	Logger logger = Logger.getLogger(this.getClass());

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
            forneceXML(req, resp);
    }

    private void forneceXML(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
    	/*
        try
        {
        	
			PrintWriter out = resp.getWriter(); 
			
			//String uri = "http://"+req.getServerName()+":"+req.getServerPort()+req.getRequestURI();
			
			String uri = "http://localhost:7778"+req.getRequestURI();
			
			URL xml = new URL(uri+"/../"+req.getParameter("relatorioXML").replaceAll(" ","+"));
			URL xsl = new URL(uri+"/../"+req.getParameter("relatorioXSL"));

			URLConnection u = xml.openConnection();
			u.connect();
			resp.setContentType("application/vnd.ms-excel");
			resp.addHeader("Pragma", "private");
			
			//XML fonte que vem via Oracle Reports
			StreamSource xmlSource=new StreamSource(xml.openStream());

			//Objeto de saída.
			StreamResult  htmlResult=new StreamResult(out);

			//XSL fonte para transformar o XML em HTML apresentável do Excell
			StreamSource xslSource=new StreamSource(xsl.openStream());

			//Objeto Fabrica de aplicador do XSL
			TransformerFactory tf=TransformerFactory.newInstance();

			//Aplicador do XSL
			Transformer transformer=tf.newTransformer(xslSource);

			//Realiza transformação e manda para saída
			transformer.transform(xmlSource,htmlResult);
			
        }
        catch(IOException e)
        {
			logger.error("Erro de IO na geração relatorio para MS/Excel (" +
							e.getMessage() + ")");
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
			logger.error("Erro de IO na geração relatorio para MS/Excel (" +
					e.getMessage() + ")");
			e.printStackTrace();
		} catch (TransformerException e) {
			logger.error("Erro de IO na geração relatorio para MS/Excel (" +
					e.getMessage() + ")");
			e.printStackTrace();
		}
		*/
    }


}
