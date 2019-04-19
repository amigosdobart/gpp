package br.com.brasiltelecom.wig.action;

import java.io.BufferedReader;

import br.com.brasiltelecom.wig.entity.MultPartContent;

import com.brt.gpp.comum.produtorConsumidor.Produtor;

public class EnvioPushAssinanteProdutor implements Produtor
{
	private MultPartContent content;
	private BufferedReader buff;
	
	private String  url;
	private boolean isWml;
	private String  servidorOTA;
	private int     portaServidorOTA;
	private int     intervalo;
	private int     simultaneos;
	
	private int     enviados;
	
	public EnvioPushAssinanteProdutor(MultPartContent content, String url, boolean isWml, String servidor, int porta, int intervalo, int simultaneos)
	{
		this.content          = content;
		this.url              = url;
		this.isWml            = isWml;
		this.servidorOTA      = servidor;
		this.portaServidorOTA = porta;
		this.intervalo        = intervalo;
		this.simultaneos	  = simultaneos;
		
		this.enviados         = 0;
	}
	
	public void startup(String[] arg0) throws Exception
	{
		// No inicio do processo, eh feito a leitura do stream do arquivo
		// enviado para o servidor contendo a lista de todos os assinantes
		// que deverao ser enviados as mensagens WML (push).
		if (content != null)
			buff = content.getStreamArquivo();
	}

	public void handleException()
	{
	}

	public Object next() throws Exception
	{
		// Le a proxima linha do arquivo e envia
		// ao consumidor para envio do push.
		// Se a linha estiver nula entao o processo
		// eh interrompido
		String linhaArquivo = null;
		if (buff != null)
		{
			linhaArquivo = buff.readLine();
			// O produtor realiza uma contagem para saber se o numero de envio
			// simultaneos chegou no limite, se chegou entao realiza uma espera
			// no produtor para que as threads consumidoras fiquem um tempo
			// esperando o termino desta espera
			if ( enviados > 0 && ((enviados % simultaneos) == 0) )
				Thread.sleep(intervalo*1000);
			
			enviados++;
		}
		
		return linhaArquivo;
	}

	public void finish() throws Exception
	{
		// Fecha o buffer
		if (buff != null)
			buff.close();
	}

	public boolean isWml()
	{
		return isWml;
	}

	public int getPortaServidorOTA()
	{
		return portaServidorOTA;
	}

	public String getServidorOTA()
	{
		return servidorOTA;
	}

	public String getUrl()
	{
		return url;
	}
}
