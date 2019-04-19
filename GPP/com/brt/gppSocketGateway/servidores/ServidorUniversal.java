package com.brt.gppSocketGateway.servidores;

import com.brt.gppSocketGateway.comum.ServidorSocket;
import com.brt.gppSocketGateway.logServer.LogGPPServer;

/**
  * Este arquivo refere-se a classe ServidorUniversal, responsavel por abrir uma thread
  * para cada servidor perif�rico ao GPP
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				25/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class ServidorUniversal 
{
//	private boolean state = true;		// indica se a thread encontra-se ativa
//	private ServidorSocket sock;			// socket usado para a comunica��o
//	private String porta;
//	private LogGPPServer log;

	/**
	 * Metodo...: ServidorUniversal
	 * Descricao: Construtor que abrir� um socket gen�rico para um servidor
	 * @param 	String	aPorta	Porta em que o servidor ouvir� as requisi��es
	 */
	public ServidorUniversal(String aPorta, int aTipoServidor) 
	{
		// Pega instancia de log
		LogGPPServer.getInstancia();
		
		// Guarda a porta do host
//		this.porta = aPorta;
		
		// Instancia um socket servidor
		new Thread(new ServidorSocket(aPorta, aTipoServidor, 0)).start();
	}
}
