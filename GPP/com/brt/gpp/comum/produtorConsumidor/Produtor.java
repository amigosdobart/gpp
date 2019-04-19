package com.brt.gpp.comum.produtorConsumidor;

/**
 * @author Luciano Vilela
 * Adaptação Joao Carlos
 *
 */
public interface Produtor
{
	/**
	 * Metodo....:startup
	 * Descricao.:Este metodo eh a implementacao do inicio do programa produtor. 
	 *            Nesse metodo a conexao com recursos (ex Banco de dados) eh realizada e os
	 *            dados necessarios para o processamento sao lidos
	 */
	public void startup(String params[]) throws Exception;
	
	/**
	 * Metodo....:next
	 * Descricao.:Este metodo retorna um objeto a ser processado. Esse objeto pode
	 *            ser a linha de uma tabela de interface quanto um xml a ser processado
	 *            dependendo de como a implementacao ira desejar
	 */	
	public Object next() throws Exception;
	
	/**
	 * Metodo....:startup
	 * Descricao.:Este metodo finaliza o procedimento de producao. Neste metodo a conexao
	 *            de recursos eh finalizada
	 */
	public void finish() throws Exception;
	
	/**
	 * Metodo....:handleException
	 * Descricao.:Este metodo eh responsavel por executar certos procedimentos 
	 * 			  quando ocorrerem excecoes durante o processo. Ex.: desfazer transacoes (rollback).
	 */
	public void handleException();
}