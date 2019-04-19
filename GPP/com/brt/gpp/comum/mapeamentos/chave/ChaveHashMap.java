package com.brt.gpp.comum.mapeamentos.chave;

import com.brt.gpp.comum.mapeamentos.chave.Chave;

/**
 *	Chave para inclusao e recuperacao de registros em mapeamentos com containers da classe HashMap.
 * 
 *	@author		Daniel Ferreira
 *	@since		19/01/2007
 */
public class ChaveHashMap extends Chave
{

	/**
	 *	Construtor da classe.
	 */
	public ChaveHashMap(Object[] objs)
	{
		super(objs);
	}
	
	/**
	 *	@see	java.lang.Object#equals()
	 */
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return (this.hashCode() == obj.hashCode());
	}
	
	/**
	 *	@see	java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		Object[] objs = super.getObjects();
		
		if(objs != null)
		{
			StringBuffer buffer = new StringBuffer(this.getClass().getName());
			
			for(int i = 0; i < objs.length; i++)
				buffer.append("||" + ((objs[i] != null) ? String.valueOf(objs[i].hashCode()) : "NULL"));
			
			return buffer.toString().hashCode();
		}
	
		return -1;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Hash Code: " + String.valueOf(this.hashCode());
	}
	
}
