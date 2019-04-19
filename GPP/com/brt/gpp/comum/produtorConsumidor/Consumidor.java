package com.brt.gpp.comum.produtorConsumidor;

/**
 * @author Luciano Vilela
 * Adaptacao Joao Carlos
 *
 */
public interface Consumidor
{
	/**
	 * Metodo....:startup
	 * Descricao.:Este metodo realiza os procedimentos de inicializacao do consumidor
	 *            Os recursos sao inicializados (ex Banco de dados)
	 *
	 */
	public void startup() throws Exception;

	/**
	 * 
	 * @param produtor
	 * @throws Exception
	 */
	public void startup(Produtor produtor) throws Exception;

	/**
	 * Metodo....:execute
	 * Descricao.:Eh neste metodo que se concentra o principal objetivo do consumidor
	 *            O objeto passado como parametro contem valores necessarios para o 
	 *            processamento do consumidor
	 * @param obj - Objeto a ser processado (ValueObject)
	 */	
	public void execute(Object obj) throws Exception;
	/**
	 * Metodo....:finish
	 * Descricao.:Este metodo realiza os procedimentos de finalizacao do consumidor
	 *            Os recursos sao entao finalizados
	 *
	 */
	public void finish();
}
