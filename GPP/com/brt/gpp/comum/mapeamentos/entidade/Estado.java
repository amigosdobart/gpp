package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>Estado</code>. Referência: TBL_GER_ESTADOS
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 14/02/2007
 */
public class Estado implements Serializable 
{
	private String UF;
	private String nomeEstado;

	/**
	 * @return Nome do estado.
	 */
	public String getNomeEstado() 
	{
		return nomeEstado;
	}

	/**
	 * @param nomeEstado Nome do estado
	 */
	public void setNomeEstado(String nomeEstado) 
	{
		this.nomeEstado = nomeEstado;
	}

	/**
	 * @return UF do estado.
	 */
	public String getUF() 
	{
		return UF;
	}

	/**
	 * @param UF UF do estado
	 */
	public void setUF(String UF) 
	{
		this.UF = UF;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Estado))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.UF, 	((Estado)obj).getUF());
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
		result.append(this.UF);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[Estado]");
		result.append("UF=" + this.UF);
		if (nomeEstado != null)
			result.append(";NOME_ESTADO=" + this.nomeEstado);
	
		return result.toString();
	}
}
