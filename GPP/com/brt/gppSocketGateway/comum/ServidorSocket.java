package com.brt.gppSocketGateway.comum;

import java.net.ServerSocket;
import java.io.IOException;

import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;
import com.brt.gppSocketGateway.logServer.LogGPPServer;

/**
  * Este arquivo refere-se a classe ServidorSocket, extensão da GSocket (classe de 
  * sockets genéricos) e implementadora de Runnable, por tratar-se da thread que, 
  * efetivamente, será a vida de cada servidor periférico ao GPP
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
public class ServidorSocket implements Runnable
{
	private int porta;
//	private ArqConfigGPPServer arqConfig;
	private LogGPPServer log;
	private int idProcesso;
	private int tipoServidor;

	/***
	 * Metodo...: ServidorSocket
	 * Descricao: Construtor
	 * @param 	String	aPorta		Porta em que o servidor servirá
	 * @param 	int		aIdProcesso	Identificador do processo para efeitos de log
	 */
	public ServidorSocket(String aPorta, int aTipoServidor, int aIdProcesso)
	{
		// Armazena a porta, o Id do Processo e o tipo do servidor 
		this.porta = new Integer(aPorta).intValue();
		this.idProcesso = aIdProcesso;
		this.tipoServidor = aTipoServidor;

		ArqConfigGPPServer.getInstance();
		log = LogGPPServer.getInstancia();
	}
	
	/**
	 * Metodo...: run
	 * Descricao: Processo que ficará ouvindo as mensagens que chegam à porta do servidor
	 * e as encaminhará para o Gerenciador de Mensagens
	 */
	public void run()
	{
//		int idChamada = 0;
//		final int timeOut = arqConfig.getTimeOut()*1000;
		synchronized(this)
		{
			log.log(idProcesso, Definicoes.DEBUG, "ServidorSocket", "run", "Instanciando um socket de servidor na porta " + porta);
			try
			{
				ServerSocket sSock = new ServerSocket(porta);
				while(true)
				{
					log.log(idProcesso, Definicoes.DEBUG, "ServidorSocket", "run", "Aguardando Accept na porta " + porta);
					// Cria uma nova instancia do GSocket para receber o socket recebido na porta definida
					// Este socket sera processado separadamente para cada processo evitando fila neste
					// processamento.
					GSocket socketCliente = new GSocket(sSock.accept());
					
					log.log(idProcesso, Definicoes.DEBUG, "ServidorSocket", "run", "Accept Retornado na porta " + porta);

					// Busca um novo Id de log
					long novoIdLog = log.getIdProcesso();
					
					// Cria uma instancia do gerenciador de mensagens para tratamento do socket recebido
					// Ao tratar o socket este gerenciador tambem escreve o acknoledge para identificar
					// ao requisitante que o processamento foi executado.
					GerenciadorMensagens auxGerenciador = new GerenciadorMensagens(socketCliente, this.tipoServidor, novoIdLog);
					auxGerenciador.start();
				}
			}
			catch (IOException ex)
			{
				log.log(idProcesso, Definicoes.ERRO, "ServidorSocket", "run", "IOException: " + ex);
			}			
		}
	}
}
