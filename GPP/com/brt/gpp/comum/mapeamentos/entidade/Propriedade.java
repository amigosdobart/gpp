package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Collection;

/**
 * Entidade <code>Propriedade</code>. Referência: TBL_TAR_PROPRIEDADE
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class Propriedade implements Serializable
{
	private int idPropriedade;
	private String desPropriedade;
	
	/**
	 * @return ID da propriedade
	 */
	public int getIdPropriedade() 
	{
		return idPropriedade;
	}
	
	/**
	 * @param propriedade ID da Propriedade
	 */
	public void setIdPropriedade(int propriedade) 
	{
		this.idPropriedade = propriedade;
	}
	
	/**
	 * @return Descricao da propriedade
	 */
	public String getDesPropriedade() 
	{
		return desPropriedade;
	}
	
	/**
	 * @param desPropriedade Descricao da propriedade
	 */
	public void setDesPropriedade(String desPropriedade) 
	{
		this.desPropriedade = desPropriedade;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Propriedade))
			return false;
		
		if (obj == this)
			return true;
		
		return idPropriedade == ((Propriedade)obj).getIdPropriedade();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idPropriedade);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Propriedade]");
		result.append("ID=" + this.idPropriedade);
		if (desPropriedade != null) result.append(";DESCRICAO=" + this.desPropriedade);
	
		return result.toString();
	}

}
