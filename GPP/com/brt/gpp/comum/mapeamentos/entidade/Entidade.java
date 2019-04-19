package com.brt.gpp.comum.mapeamentos.entidade;

/**
 *	Interface para representacao de entidades de banco de dados mapeados como objetos em memoria.
 * 
 *	@author	Daniel Ferreira
 *	@since	23/11/2005
 *
 */
public interface Entidade extends Cloneable
{
    
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
    public Object clone();
    
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		Object					object						Objeto a ser comparado com o atual.
	 *	@return		boolean												True se for igual e false se for diferente.
	 */
    public boolean equals(Object object);
    
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		int													Hash do objeto.
	 */
	public int hashCode();
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString();
	
}
