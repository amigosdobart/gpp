package com.brt.gpp.comum.fabricaDeRelatorio;

/**
 *	Excecao gerada por erros de execucao de relatorios
 * 
 *	@author		Daniel Ferreira
 *	@since		21/07/2005
 */
public class RelatorioException extends Exception
{
	
	/**
	 *	Construtor da classe
	 *
	 *	@param		String				mensagem					Mensagem detalhando o erro ocorrido
	 */	
	public RelatorioException(String mensagem)
	{
		super(mensagem);
	}
	
}
