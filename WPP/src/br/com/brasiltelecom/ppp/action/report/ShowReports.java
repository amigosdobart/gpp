/*
 */
package br.com.brasiltelecom.ppp.action.report;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 */
public class ShowReports extends ActionPortal {

	public ActionForward performPortal(
		ActionMapping actionMapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response,
		Database db)
		throws Exception {
			String repositorioReport = servlet.getServletContext().getRealPath("/WEB-INF/reports/");
			String report = repositorioReport  + "/"+ request.getParameter("rep");
			String tipo = request.getParameter("DESFORMAT");
			Context ctx = new InitialContext();
			
			DataSource ds = (DataSource) ctx.lookup((String)servlet.getServletContext().getAttribute(Constantes.DATASOURCE_NAME));
			Connection con = ds.getConnection();
			JasperReport js = JasperCompileManager.compileReport(report);
			JRParameter[] parametros = js.getParameters();
			//JasperPrint jp = JasperFillManager.fillReport(js, request.getParameterMap(), con);
			Map mapParametros = new HashMap();
			for(Enumeration e = request.getParameterNames(); e.hasMoreElements();){
				String nomeParametro = (String) e.nextElement();
				for(int i = 0; i < parametros.length; i++){
					if(nomeParametro.equals(parametros[i].getName())){
						mapParametros.put(nomeParametro, request.getParameter(nomeParametro));
					}
				}
			}
			JasperPrint jp = JasperFillManager.fillReport(js, mapParametros, con);
			
			if("html".equalsIgnoreCase(tipo)){
				File dirReport = new File(servlet.getServletContext().getRealPath("/reports/"));
				File tempFile = File.createTempFile("rep", ".html", dirReport);
				JasperExportManager.exportReportToHtmlFile(jp, tempFile.getAbsolutePath());
				
				//servlet.getServletContext().getRequestDispatcher("/reports/"+ tempFile.getName()).forward(request, response);
				response.sendRedirect("reports/" + tempFile.getName());
				
			}
			else if("pdf".equalsIgnoreCase(tipo)){
				response.setContentType("application/pdf");
				JasperExportManager.exportReportToPdfStream(jp, response.getOutputStream());
			}
			else if("xml".equalsIgnoreCase(tipo)){
				response.setContentType("text/xml");
				JasperExportManager.exportReportToXmlStream(jp, response.getOutputStream());
			}
			else if("xls".equalsIgnoreCase(tipo)) {
				response.setContentType("application/vnd.ms-excel");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				JRXlsExporter exporter = new JRXlsExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp); 
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos); 
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);	
				exporter.exportReport();
				byte[] bytes = baos.toByteArray();

				response.setContentLength(bytes.length);
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(bytes,0,bytes.length);
				outputStream.flush();
				outputStream.close();
			
			}
			
			con.close();
		
		
		
		return null;
	}

	public String getOperacao() {
		return null;
	}

}
