package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>Modulacao</code>. Referência: TBL_GER_MODULACAO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/06/2007
 */
public class Modulacao implements Serializable
{
	private String idModulacao;
	private String desModulacao;
	
	/**
	 * @return ID da modulação
	 */
	public String getDesModulacao() 
	{
		return desModulacao;
	}

	/**
	 * @return Descrição da modulação
	 */
	public String getIdModulacao() 
	{
		return idModulacao;
	}

	/**
	 * @param string Descrição da modulação
	 */
	public void setDesModulacao(String string) 
	{
		desModulacao = string;
	}

	/**
	 * @param string ID da modulação
	 */
	public void setIdModulacao(String string) 
	{
		idModulacao = string;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Modulacao))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;		
		equal &= isEqual(this.idModulacao, 	((Modulacao)obj).getIdModulacao());
		return equal;
	}
	
	private boolean isEqual(Object obj1, Object obj2)
	{
		if (obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		if (obj1 == null && obj2 == null)
			return true;
		return false;
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		if(idModulacao != null)	result.append(this.idModulacao);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Modulacao]");
		if (idModulacao != null) result.append("ID=" + this.idModulacao);
		if (desModulacao != null) result.append("DESCRICAO=" + this.desModulacao);
		
		return result.toString();
	}

}
