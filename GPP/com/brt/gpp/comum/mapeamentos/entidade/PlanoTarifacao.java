package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>PlanoTarifacao</code>. Referência: TBL_TAR_PLANO_TARIFACAO
 *  
 * @author Bernardo Vergne Dias
 * Criado em: 14/02/2007
 * 
 * versao criada somente para relacionamento ao CD do Controle Total
 */
public class PlanoTarifacao implements Serializable 
{
	private int idPlanoTarifacao;
	private String desPlanoTarifacao;
	
	/**
	 * @return Descricao do Plano de Tarifacao
	 */
	public String getDesPlanoTarifacao() 
	{
		return desPlanoTarifacao;
	}
	
	/**
	 * @param desPlanoTarifacao Descricao do Plano de Tarifacao
	 */
	public void setDesPlanoTarifacao(String desPlanoTarifacao) 
	{
		this.desPlanoTarifacao = desPlanoTarifacao;
	}
	
	/**
	 * @return ID do Plano de Tarifacao
	 */
	public int getIdPlanoTarifacao() 
	{
		return idPlanoTarifacao;
	}
	
	/**
	 * @param idPlanoTarifacao ID do Plano de Tarifacao
	 */
	public void setIdPlanoTarifacao(int idPlanoTarifacao) 
	{
		this.idPlanoTarifacao = idPlanoTarifacao;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null)
		{
			return false;
		}
			
		if (!(obj instanceof PlanoTarifacao))
		{
			return false;
		}
		else
		{
			boolean equal = true;		
			equal &= (desPlanoTarifacao.equals(((PlanoTarifacao)obj).getDesPlanoTarifacao()));
			if (desPlanoTarifacao != null)
				equal &= (idPlanoTarifacao == ((PlanoTarifacao)obj).getIdPlanoTarifacao());
			return equal;
		}
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idPlanoTarifacao);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[PlanoTarifacao]");
		result.append("ID=" + this.idPlanoTarifacao);
		if (desPlanoTarifacao != null)
			result.append(";DESCRICAO=" + this.desPlanoTarifacao);
	
		return result.toString();
	}

}
