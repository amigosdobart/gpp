package br.com.brasiltelecom.wig.util;

import com.smarttrust.dp.sfmex.api.SfmexHandler;
import com.smarttrust.dp.sfmex.api.SfmexError;
import com.smarttrust.dp.sfmex.api.SfmexString;
import com.smarttrust.dp.sfmex.api.SfmexSendSmRequest;
import com.smarttrust.dp.sfmex.api.SfmexSubscriberAddress;
import com.smarttrust.dp.sfmex.api.SfmexNotificationType;
import com.smarttrust.dp.sfmex.api.SfmexUniString;

public class SenderSMBySFM
{
	private SfmexString servidor;
	private SfmexString	porta;
	private SfmexString	username;
	private SfmexString	password;
	
	private SfmexHandler	handler;
	private SfmexError		error;
	
	/**
	 * Metodo....:SenderSMBySFM
	 * Descricao.:Construtor da classe
	 * @param servidor	- Servidor da plataforma OTA
	 * @param porta		- Porta onde o servidor responde
	 * @param username	- Usuario a ser utilizado para o envio de SM
	 * @param password	- Senha do usuario
	 */
	public SenderSMBySFM(String servidor, String porta, String username, String password)
	{
		this.servidor 	= new SfmexString(servidor);
		this.porta		= new SfmexString(porta);
		this.username	= new SfmexString(username);
		this.password	= new SfmexString(password);
	}
	
	/**
	 * Metodo....:init
	 * Descricao.:Este metodo realiza a conexao com a plataforma
	 *
	 */
	private void init()
	{
		handler = new SfmexHandler();
		error   = new SfmexError();
		// Faz a tentativa de conexao com o servidor, caso o retorno seja
		// igual a zero (0) entao nao estah conectado e um erro deve ser
		// retornado
		if ( handler.init(servidor,porta,username,password,error) != 1)
			throw new IllegalStateException(error.getErrorCode()+"-"+error.getErrorDescription());
	}
	
	/**
	 * Metodo....:close
	 * Descricao.:Este metodo realiza a desconexao com a plataforma
	 *
	 */
	private void close()
	{
		if ( handler != null && handler.isInitialised()!=0 )
			handler.close();
	}
	
	/**
	 * Metodo....:sendSM
	 * Descricao.:Este metodo realiza o envio do SM pela plataforma realizando a conexao e desconexao
	 * @param subscriber	- Msisdn do assinante de destino da mensagem
	 * @param smText		- Mensagem a ser enviada para o assinante
	 * @return long			- Identificacao da requisicao do sm pela plataforma
	 * @throws Exception
	 */
	public long sendSM(String subscriber, String smText) throws Exception
	{
		// Realiza a conexao
		init();
		// Define a informacao do assinante nao qual o Sm serah enviado
		SfmexSubscriberAddress address = new SfmexSubscriberAddress();
		address.setDestinationAddress(new SfmexString(subscriber));

		// Define as informacoes do SM
		SfmexSendSmRequest request = new SfmexSendSmRequest();
		request.setSubscriberAddress	(address);
		request.setStatusNotification	(SfmexNotificationType.type_E_None);
		request.setRequestDescription	(new SfmexString("Sending SM"));
		request.setSmData				(new SfmexUniString(smText));
		
		// Nesse ponto realiza o envio da mensagem e prepara o envio do
		// download identifier retornado pelo OTA
		// Em caso de retorno igual a zero, entao um erro ocorreu. Esse
		// erro deve ser enviado para o chamador desse metodo porem deve
		// ser fechado a conexao independente de erro ou nao
		try
		{
			if ( handler.isInitialised() == 0 )
				throw new IllegalStateException("Nao inicializado a comunicacao com a plataforma OTA");
			
			if (handler.execute(request,error) == 0)
				throw new IllegalStateException(error.getErrorCode()+"-"+error.getErrorDescription());
		}
		catch(Exception e){
			throw e;
		}
		finally{
			close();
		}
		return request.getOtaRequestId();
	}
}
