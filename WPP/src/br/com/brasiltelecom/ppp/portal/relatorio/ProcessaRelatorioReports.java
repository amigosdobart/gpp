package br.com.brasiltelecom.ppp.portal.relatorio;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Thread para disparar o processamento de um relatorio via Oracle Reports
 * 
 * @author Bernardo Vergne Dias
 * Data: 18/10/2007
 */
public class ProcessaRelatorioReports implements Runnable
{
	private Logger logger;
	private ResultadoRelatorio resultado;
	private String queryString;
	private ServletContext context;
	private Map properties;
	private String localhost;
	
	public ProcessaRelatorioReports(ResultadoRelatorio resultado, String localhost, 
			String queryString,	ServletContext context, Map properties) 
	{
		this.logger = Logger.getLogger(this.getClass());
		this.resultado = resultado;
		this.queryString = queryString;
		this.context = context;
		this.localhost = localhost;
		this.properties = properties;
	}
	
	public void run() 
	{
		try
		{
			// A string queryString esta em um dos seguintes formatos:
			// Caso 1: "./relatoriosWPP?REPORTS+NOME_RELATORIO=Perfil.rdf+PARAM=2+&0.237293598"
			// Caso 2: "./relatoriosWPP?REPORTS+TEMPLATE_XSL=Perfil.xsl+NOME_RELATORIO=Perfil.rdf+XML+PARAM=2+&0.237293598"
			
			// Para ambos os casos, devemos gerar a url de requisicao ao reports no seguinte formato:
			// "http://host:porta/reports/rwservlet?ppp+Perfil.rdf+XML+PARAM=2"
			// Caso existir o parametro TEMPLATE_XSL, o resultado do reports é processado por um transform XSL
			
			String servReports = (String)context.getAttribute(Constantes.REPORTS_NOME_SERVIDOR);
			String portReports = (String)context.getAttribute(Constantes.REPORTS_PORTA_SERVIDOR);
			String urlReports = "http://" + servReports + ":" + portReports + "/reports/rwservlet?ppp+" + 
			                    queryString.substring(queryString.indexOf("NOME_RELATORIO=") + 15);
			
			if ( queryString.lastIndexOf('&') >= 0)
				urlReports = urlReports.substring(0, urlReports.lastIndexOf('&'));
			            
			// Conecta ao servidor Reports
			
			HttpURLConnection conn = (HttpURLConnection)((new URL(urlReports)).openConnection());
			DataInputStream input = new DataInputStream(conn.getInputStream());
			
			// Verifica se o processamento será pelo Transform XSL ou diretamente pelo UrlConnection
			
			if (properties.get("TEMPLATE_XSL") != null)
			{
				resultado.setContentType("application/vnd.ms-excel");
				resultado.setContentDisposition(null);
				
				FileOutputStream output = resultado.criarArquivo();
				URL xsl = new URL("http://" + localhost + "/ppp/" + (String)properties.get("TEMPLATE_XSL"));
				
				// Realiza transformação e grava o resultado em arquivo
				
				StreamSource xslSource = new StreamSource(xsl.openStream());
				Transformer tf = TransformerFactory.newInstance().newTransformer(xslSource);
				tf.transform(new StreamSource(input), new StreamResult(output));
			}
			else
			{
				resultado.setContentType(conn.getContentType());
				resultado.setContentDisposition("inline");
				
				// Faz a leitura direta do stream HTTP
				
				try
				{
					FileOutputStream output = resultado.criarArquivo();
					
					int length = 0;
					byte[] dados = new byte[1024];
					while ((length = input.read(dados)) >= 0)
						output.write(dados, 0, length);
					
					output.close();
				}
				catch (Exception e) 
				{
					if (!(e instanceof EOFException))
						throw e;
				}			
			}
			
			conn.disconnect();
			resultado.setCodStatus(ResultadoRelatorio.CONCLUIDO);
		}
		catch (Exception e)
		{
			logger.error("Erro em ProcessaRelatorioReports: " + e);
			resultado.setCodStatus(ResultadoRelatorio.ERRO);
			resultado.setDesStatus("Sistema indisponível. ");
		}		
	}
}