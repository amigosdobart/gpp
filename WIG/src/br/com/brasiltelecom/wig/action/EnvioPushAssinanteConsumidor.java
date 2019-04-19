package br.com.brasiltelecom.wig.action;

import org.apache.log4j.Logger;

import com.brt.gpp.comum.produtorConsumidor.Consumidor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;

public class EnvioPushAssinanteConsumidor implements Consumidor
{
	private EnvioPushAssinanteProdutor produtor;
	private GerenciadorWIGPush gerWIGPush;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void startup() throws Exception
	{
		gerWIGPush = new GerenciadorWIGPush(produtor.getServidorOTA(),produtor.getPortaServidorOTA());
	}

	public void startup(Produtor produtor) throws Exception
	{
		this.produtor = (EnvioPushAssinanteProdutor)produtor;
		startup();
	}
	
	public void execute(Object obj) throws Exception
	{
		// O objeto sendo passado para o consumidor
		// eh uma linha do arquivo de assinantes que
		// estah sendo processado
		String linha = (String)obj;
		
		// Verifica se a linha do arquivo corresponde
		// a um numero valido para celular da BrasilTelecom
		if (linha != null && linha.length() == 12 && linha.startsWith("55") && linha.matches("....84......"))
			// Realiza o envio do PUSH para o assinante
			if (!gerWIGPush.enviarPost(linha, produtor.getUrl(), produtor.isWml(), 0))
				logger.info("Push nao enviado para o assinante " + linha+" atraves do envio por arquivo.");
	}

	public void finish()
	{
	}
}
