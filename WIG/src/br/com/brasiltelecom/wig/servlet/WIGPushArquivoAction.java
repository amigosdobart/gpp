package br.com.brasiltelecom.wig.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.comum.produtorConsumidor.ProdutorConsumidorDelegate;

import br.com.brasiltelecom.wig.action.EnvioPushAssinanteConsumidor;
import br.com.brasiltelecom.wig.action.EnvioPushAssinanteProdutor;
import br.com.brasiltelecom.wig.action.MultPartContentParser;
import br.com.brasiltelecom.wig.entity.MultPartContent;

/**
 * @author Joao Carlos
 * Data..: 02/06/2005
 *
 */
public class WIGPushArquivoAction extends HttpServlet
{
	private static final long serialVersionUID = 7526471155622776147L;
	private static boolean estahExecutando = false;
	
	private Logger logger = Logger.getLogger(this.getClass());
	private String otaServerName;
	private int otaPortNumber;
	
	public void init(ServletConfig arg0) throws ServletException
	{
		otaPortNumber = Integer.parseInt ((String)arg0.getServletContext().getAttribute("PortaServidorPush"));
		otaServerName = (String)arg0.getServletContext().getAttribute("ServidorPush");
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		if (!estahExecutando)
		{
			try
			{
				// Realiza o parse do MultPartContent que estah sendo enviado
				// para o servidor contendo as informacoes de arquivo da lista
				// de Msisdn's que devem ser enviado o push.
				MultPartContent content = MultPartContentParser.parse(request);
				
				// Busca tambem as outras informacoes do formulario que serao
				// utilizadas para o processo de envio
				String url     = content.getValorCampo("url");
				boolean wml    = new Boolean(content.getValorCampo("wml")).booleanValue();
				int numThreads = content.getValorCampo("threads")  != null ? Integer.parseInt(content.getValorCampo("threads")) : 0;
				int intervalo  = content.getValorCampo("intervalo")!= null ? Integer.parseInt(content.getValorCampo("intervalo")) : 0;

				// Chama o metodo para tratar o produtor consumidor.
				Produtor produtor = new EnvioPushAssinanteProdutor(content,url,wml,otaServerName,otaPortNumber,intervalo,numThreads);
				ProdutorConsumidorDelegate delegate = new ProdutorConsumidorDelegate();
				delegate.exec(numThreads, produtor, null, EnvioPushAssinanteConsumidor.class);
				
				out.println("Envio de push realizado para o arquivo de assinantes.");
			}
			catch(Exception e)
			{
				logger.error("Erro ao enviar push para o arquivo de assinantes.",e);
			}
		}
		else
			out.println("Envio de push por arquivo, jah estah em andamento. Por favor aguarde termino.");
		
		out.flush();
		out.close();
	}
}

