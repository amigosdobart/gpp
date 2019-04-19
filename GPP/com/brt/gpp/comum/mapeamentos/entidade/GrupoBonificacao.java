package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Collection;

/**
 * Entidade <code>GrupoBonificacao</code>. Referência: TBL_TAR_GRUPO_BONIFICACAO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class GrupoBonificacao implements Serializable
{
	private int idGrupoBonificacao;
	private String desGrupoBonificacao;
	private Collection bonificacoesChamada;
		
	
	/**
	 * @return Coleção de <code>BonificacaoChamada</code>
	 */
	public Collection getBonificacoesChamada() 
	{
		return bonificacoesChamada;
	}

	/**
	 * @param bonificacoesChamada Coleção de <code>BonificacaoChamada</code>
	 */
	public void setBonificacoesChamada(Collection bonificacoesChamada) 
	{
		this.bonificacoesChamada = bonificacoesChamada;
	}

	/**
	 * @return ID da categoria
	 */
	public int getIdGrupoBonificacao() 
	{
		return idGrupoBonificacao;
	}
	
	/**
	 * @param grupoBonificacao ID da categoria
	 */
	public void setIdGrupoBonificacao(int grupoBonificacao) 
	{
		this.idGrupoBonificacao = grupoBonificacao;
	}
	
	/**
	 * @return Descricao da categoria
	 */
	public String getDesGrupoBonificacao() 
	{
		return desGrupoBonificacao;
	}
	
	/**
	 * @param desGrupoBonificacao Descricao da categoria
	 */
	public void setDesGrupoBonificacao(String desGrupoBonificacao) 
	{
		this.desGrupoBonificacao = desGrupoBonificacao;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof GrupoBonificacao))
			return false;
		
		if (obj == this)
			return true;
		
		return idGrupoBonificacao == ((GrupoBonificacao)obj).getIdGrupoBonificacao();
	}

	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idGrupoBonificacao);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[GrupoBonificacao]");
		result.append("ID=" + this.idGrupoBonificacao);
		if (desGrupoBonificacao != null) result.append(";DESCRICAO=" + this.desGrupoBonificacao);
	
		return result.toString();
	}

}
