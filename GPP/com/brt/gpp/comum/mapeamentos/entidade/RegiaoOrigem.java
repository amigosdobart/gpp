package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>RegiaoOrigem</code>. Referência: TBL_TAR_REGIAO_ORIGEM
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 15/02/2007
 */
public class RegiaoOrigem implements Serializable
{
	private int idRegiaoOrigem;
	private CodigoNacional codigoNacional;
	
	/**
	 * @return Instancia de <code>CodigoNacional</code>
	 */
	public CodigoNacional getCodigoNacional() 
	{
		return codigoNacional;
	}
	
	/**
	 * @param codigoNacional Instancia de <code>CodigoNacional</code>
	 */
	public void setCodigoNacional(CodigoNacional codigoNacional) 
	{
		this.codigoNacional = codigoNacional;
	}
	
	/**
	 * @return ID da região de origem
	 */
	public int getIdRegiaoOrigem() 
	{
		return idRegiaoOrigem;
	}
	
	/**
	 * @param idRegiaoOrigem ID da região de origem
	 */
	public void setIdRegiaoOrigem(int idRegiaoOrigem) 
	{
		this.idRegiaoOrigem = idRegiaoOrigem;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof RegiaoOrigem))
			return false;
		
		if (obj == this)
			return true;
		
		return idRegiaoOrigem == ((RegiaoOrigem)obj).getIdRegiaoOrigem();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idRegiaoOrigem);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[RegiaoOrigem]");
		result.append("ID=" + this.idRegiaoOrigem);
		if (codigoNacional != null)
			result.append(";CODIGO_NACIONAL=" + this.codigoNacional.getIdtCodigoNacional());
		
		return result.toString();
	}
}
