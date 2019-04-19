package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>TipoOperadora</code>. Referência: TBL_TAR_TIPO_OPERADORA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 11/04/2007
 */
public class TipoOperadora implements Serializable
{
	private int idTipoOperadora;
	private String desTipoOperadora;
		
	/**
	 * @return descrição do tipo da operadora
	 */
	public String getDesTipoOperadora() 
	{
		return desTipoOperadora;
	}

	/**
	 * @param desTipoOperadora descrição do tipo da operadora
	 */
	public void setDesTipoOperadora(String desTipoOperadora) 
	{
		this.desTipoOperadora = desTipoOperadora;
	}

	/**
	 * @return tipo da operadora
	 */
	public int getIdTipoOperadora() 
	{
		return idTipoOperadora;
	}

	/**
	 * @param tipOperadora tipo da operadora
	 */
	public void setIdTipoOperadora(int tipOperadora) 
	{
		this.idTipoOperadora = tipOperadora;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof TipoOperadora))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= (idTipoOperadora == ((TipoOperadora)obj).getIdTipoOperadora());
		return equal;
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idTipoOperadora);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[TipoOperadora]");
		result.append("TIPO=" + this.idTipoOperadora);
		if (desTipoOperadora != null) result.append(";DESCRICAO=" + this.desTipoOperadora);
	
		return result.toString();
	}

}
