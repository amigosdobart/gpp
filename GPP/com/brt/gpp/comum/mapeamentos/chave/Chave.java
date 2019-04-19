package com.brt.gpp.comum.mapeamentos.chave;

/**
 *	Chave para inclusao e recuperacao de registros em containers (Map's).
 * 
 *	@author		Daniel Ferreira
 *	@since		11/05/2007
 */
public abstract class Chave
{

	/**
	 *	Objetos que compoem a chave de um objeto do mapeamento.
	 */
	private Object[] objs;
	
	/**
	 *	Construtor da classe.
	 */
	public Chave(Object[] objs)
	{
		this.objs = objs;
	}
	
	/**
	 *	Retorna os objetos que compoem a chave de um objeto do mapeamento.
	 *
	 *	@return		Objetos que compoem a chave de um objeto do mapeamento.
	 */
	protected Object[] getObjects()
	{
		return this.objs;
	}
	
}
