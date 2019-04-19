package com.brt.gpp.comum.interfaceEscrita;

/**
 * Interface utilizada para definir os metodos de escrita de dados em arquivos, streams,
 * banco de dados, sockets.<br>
 * Deve ser utilizada em conjunto com a InterfaceConfiguracao, sendo assim e aconselhavel
 * que se defina um construtor que receba como parametro a InterfaceCondiguracao correspondente.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 03/10/2007
 */
public interface InterfaceEscrita
{
	/**
	 * Abre a stream de escrita.
	 *
	 * @throws 		Exception Caso haja algum problema na hora da execucao
	 */
	public void abrir() throws Exception;
	/**
	 * Escreve os dados na stream.
	 *
	 * @param 		campos 	  Array de campos a serem escritos
	 * @throws 		Exception Caso haja algum problema na hora da execucao
	 */
	public void escrever(String[] campos) throws Exception;
	/**
	 * Fecha a stream de escrita
	 *
	 * @throws 		Exception Caso haja algum problema na hora da execucao
	 */
	public void fechar() throws Exception;
}
