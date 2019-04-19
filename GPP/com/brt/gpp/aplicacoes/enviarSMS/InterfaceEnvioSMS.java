package com.brt.gpp.aplicacoes.enviarSMS;

public interface InterfaceEnvioSMS {

	/**
	 * Metodo....: run
	 * Descricao.: Metodo de execucao da Thread
	 */
	public void run();

	/**
	 * Metodo...: enviaMensagemSMS
	 * Descricao: Le as mensagens que devem ser enviadas, 
	 * 			  faz uma comunicao com a plataforma de SMSC e envia o SMS
	 */
	public void enviaMensagemSMS();

	/**
	 * Metodo...: reiniciaConsumoSMS
	 * Descricao: Notifica a thread para "acordar" e continuar o consumo de SMS
	 *
	 */
	public void reiniciaConsumoSMS();

	/**
	 * Metodo...: close
	 * Descricao: Fecha as referencias para a conexao Middleware SMSC
	 *            e finaliza a execucao da thread
	 */
	public void close();
	
	public void start();

}