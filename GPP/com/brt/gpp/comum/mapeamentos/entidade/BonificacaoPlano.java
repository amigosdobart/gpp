package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>BonificacaoPlano</code>. Referência: TBL_TAR_BONIFICACAO_PLANO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class BonificacaoPlano implements Serializable
{
	private PlanoPreco planoPreco;
	private GrupoBonificacao grupoBonificacao;
		
	/**
	 * @return Instancia de <code>PlanoPreco</code>
	 */
	public PlanoPreco getPlanoPreco() 
	{
		return planoPreco;
	}

	/**
	 * @param planoPreco Instancia de <code>PlanoPreco</code>
	 */
	public void setPlanoPreco(PlanoPreco planoPreco) 
	{
		this.planoPreco = planoPreco;
	}

	/**
	 * @return Instancia de <code>GrupoBonificacao</code>
	 */
	public GrupoBonificacao getGrupoBonificacao() 
	{
		return grupoBonificacao;
	}

	/**
	 * @param grupoBonificacao Instancia de <code>GrupoBonificacao</code>
	 */
	public void setGrupoBonificacao(GrupoBonificacao grupoBonificacao) 
	{
		this.grupoBonificacao = grupoBonificacao;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof BonificacaoPlano))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.grupoBonificacao, ((BonificacaoPlano)obj).getGrupoBonificacao());
		equal &= isEqual(this.planoPreco, 		((BonificacaoPlano)obj).getPlanoPreco());
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
		if (grupoBonificacao != null) result.append(this.grupoBonificacao.getIdGrupoBonificacao());
		if (planoPreco != null) result.append(this.planoPreco.getIdtPlanoPreco());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[BonificacaoPlano]");
		
		if (planoPreco != null && planoPreco.getDesPlanoPreco() != null) 
			result.append("PLANO_PRECO=" + this.planoPreco.getDesPlanoPreco());
		
		if (grupoBonificacao != null && grupoBonificacao.getDesGrupoBonificacao() != null) 
			result.append(";GRUPO_BONIFICACAO=" + this.grupoBonificacao.getDesGrupoBonificacao());
		
		return result.toString();
	}

}
