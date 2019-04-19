package br.com.brasiltelecom.wig.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import org.apache.log4j.Logger;

public class GerenciadorWIGPush
{
	int port;
	String hostName;
	private static final String SEP = "\r\n";
	private Logger logger;
	
	public GerenciadorWIGPush(String hostName, int port)
	{
		this.hostName = hostName;
		this.port = port;
		logger = Logger.getLogger(this.getClass());
	}
	
	/**
	 * Metodo....:enviarPost
	 * Descricao.:Enviar post utilizando uma origem que por default eh uma URL onde deve
	 *            ser buscado a informacao.
	 * @param msisdn  - Msisdn destino do push
	 * @param url	  - URL de origem do conteudo a ser enviado
	 * @param isWml	  - true se deve ser enviado um push somente com a referencia da URL
	 * @param tariff  - tariff a ser utilizado para cobranca do push
	 * @return boolean - Se conseguiu enviar o push ou nao
	 */
	public boolean enviarPost(String msisdn, String url, boolean isWml, int tariff)
	{
		boolean enviouPush = false;
		StringBuffer wml = new StringBuffer(WIGWmlConstrutor.PROLOGUE_WML + "\n");
		// Se o parametro WML for informado entao este eh utilizado ao inves de
		// ler as informacoes a partir de uma URL. Se os dois parametros URL e WML
		// forem utilizados, o WML sobrepoe a URL.
		if (isWml)
		{
			wml.append("<wml>\n");
				wml.append("<card>\n");
					wml.append("<p>\n");
					wml.append("<do type=\"accept\"><go enterwait=\"false\" href=\""+ url +"\"/></do>\n");
					wml.append("</p>");
				wml.append("</card>\n");
			wml.append("</wml>");
		}
		// Le as informacoes do WML retornado pela URL. Estes dados
		// serao enviados via POST desde que o retorno seja diferente
		// de nulo. OBS: Se a URL nao retornar um WML um erro serah
		// recebido pela plataforma, pois um codigo nao WML estah sendo
		// enviado no push
		String dadosWML = isWml ? wml.toString() : getDadosURL(url);
		if (dadosWML != null)
			enviouPush = enviarPost(msisdn, dadosWML, tariff);
		
		return enviouPush;
	}
	
	/**
	 * Metodo....:enviarPost
	 * Descricao.:Enviar Post de WML para o assinante podendo ateh mesmo cobrar o envio.
	 * @param msisdn	- Msisdn destino
	 * @param wml		- WML a ser enviado como push
	 * @param tariff	- tariff a ser aplicado para cobranca
	 * @return boolean	- Indica se foi possivel o envio ou nao do push
	 */
	public boolean enviarPost(String msisdn, String wml, int tariff)
	{
		boolean mensagemEntregue = false;
		Socket socket = null;
		BufferedWriter wr = null;
		BufferedReader rd = null;
		try
		{
	        // Create a socket to the host
	        InetAddress addr = InetAddress.getByName(hostName);
	        socket = new Socket(addr, port);
	        // Send header
	        StringBuffer dadosPost = getDadosPOST(msisdn,wml);
	        wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        wr.write("POST: /dummy.jsp HTTP/1.1" + SEP);
	        wr.write("Host: wigserverhost:5012" + SEP);
	        wr.write("Accept-Charset:  text/*" + SEP);
	        wr.write("Accept-Encoding: identity" + SEP);
	        wr.write("User-Agent: WDZ PushSender" + SEP);
	        wr.write("Connection: close" + SEP);
	        wr.write("Content-Type: multipart/related; boundary=\"asdlfkjiurwghasf\"; type=\"application/xml\"");
	        wr.write("Content-Length: "+dadosPost.length()+"" + SEP);
	        if (tariff != 0)
	        	wr.write("X-WAP-Payment-Info: content-value-class="+tariff+"" + SEP);
	        
	        wr.write("" + SEP);
	        // Send data
	        wr.write(dadosPost.toString());
	        wr.flush();
	
	        // Get response
	        rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        String line;
	        while ((line = rd.readLine()) != null) {
	        	if (line.indexOf("Push message OK")==0)
	        		mensagemEntregue = true;
	        }
	        wr.close();
	        rd.close();
		}
		catch(Exception e)
		{
			logger.error("Erro ao enviar POST para servidor:"+hostName,e);
		}
		finally
		{
			try{
				if (rd != null)
					rd.close();
				if (wr != null)
					wr.close();
			}
			catch(Exception e){};
		}
        return mensagemEntregue;
	}
	
	private StringBuffer getDadosPOST(String msisdn, String wml)
	{
        StringBuffer data = new StringBuffer("--asdlfkjiurwghasf" + SEP);
        data.append ("Content-Type: application/xml" + SEP);
        data.append ("<?xml version=\"1.0\"?>" + SEP);
        data.append ("<!DOCTYPE pap PUBLIC \"-//WAPFORUM//DTD PAP 2.0//EN\"" + SEP);
        data.append ("\"http://www.wapforum.org/DTD/pap_2.0.dtd\">" + SEP);
        data.append ("<pap>" + SEP);
        data.append ("<push-message" + SEP);
        data.append ("push-id=\"9fjeo39jf085@content-provider.com\"" + SEP);
        data.append ("ppg-notify-requested-to=\"http://10.61.102.122/main/servlet/Response?confirm=1\">" + SEP);
        data.append ("<address address-value=\"" + msisdn + "\"/>" + SEP);
        data.append ("<quality-of-service delivery-method=\"unconfirmed\"/>" + SEP);
        data.append ("</push-message>" + SEP);
        data.append("</pap>" + SEP);
        data.append("--asdlfkjiurwghasf" + SEP + SEP);
        data.append("Content-Type: text/vnd.wap.wml" + SEP);
        
        /*data.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>" + SEP);
        data.append("<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"" + SEP);
        data.append("\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">" + SEP);
        data.append("<wml><card><p>You have received a push message!</p></card></wml>");*/
        
        data.append(wml);
        data.append("--asdlfkjiurwghasf");
        
        return data;
	}
	
	/**
	 * Metodo....:getDadosURL
	 * Descricao.:Pega informacoes de WML retornado por uma URL
	 * @param urlStr  - String da URL a ser executada
	 * @return String - Dados do WML
	 */
	private String getDadosURL(String urlStr)
	{
		StringBuffer wmlRetorno = new StringBuffer();
		try
		{
			URL url = new URL(urlStr);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String linha;
			while ( (linha = br.readLine()) != null )
				wmlRetorno.append(linha+"\n");
			
			br.close();
		}
		catch(Exception e)
		{
			logger.error("Erro ao executar URL para dados a ser enviados no push.",e);
		}
		return wmlRetorno.toString();
	}
}
