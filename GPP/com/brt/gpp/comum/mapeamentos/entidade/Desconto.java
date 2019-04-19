package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>Desconto</code>. Referência: TBL_TAR_DESCONTO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class Desconto implements Serializable
{
	private int idDesconto;
	private String desDesconto;
		
	/**
	 * @return ID do desconto
	 */
	public int getIdDesconto() 
	{
		return idDesconto;
	}
	
	/**
	 * @param categoria ID do desconto
	 */
	public void setIdDesconto(int categoria) 
	{
		this.idDesconto = categoria;
	}
	
	/**
	 * @return Descricao do desconto
	 */
	public String getDesDesconto() 
	{
		return desDesconto;
	}
	
	/**
	 * @param desCategoria Descricao do desconto
	 */
	public void setDesDesconto(String desDesconto) 
	{
		this.desDesconto = desDesconto;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Desconto))
			return false;
		
		if (obj == this)
			return true;
		
		return idDesconto == ((Desconto)obj).getIdDesconto();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idDesconto);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Desconto]");
		result.append("ID=" + this.idDesconto);
		if (desDesconto != null) result.append(";DESCRICAO=" + this.desDesconto);
	
		return result.toString();
	}

}
