package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidade <code>PropriedadePlano</code>. Referência: TBL_TAR_PROPRIEDADE_PLANO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class PropriedadePlano implements Serializable
{
	private Propriedade propriedade;
	private PlanoPreco planoPreco;
	private String valorPropriedade;
		
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
	 * @return Instancia de <code>Propriedade</code>
	 */
	public Propriedade getPropriedade() 
	{
		return propriedade;
	}

	/**
	 * @param propriedade Instancia de <code>Propriedade</code>
	 */
	public void setPropriedade(Propriedade propriedade) 
	{
		this.propriedade = propriedade;
	}

	/**
	 * @return Valor da propriedade
	 */
	public String getValorPropriedade() 
	{
		return valorPropriedade;
	}

	/**
	 * @param valorPropriedade Valor da propriedade
	 */
	public void setValorPropriedade(String valorPropriedade) 
	{
		this.valorPropriedade = valorPropriedade;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof PropriedadePlano))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.planoPreco, 	((PropriedadePlano)obj).getPlanoPreco());
		equal &= isEqual(this.propriedade, 	((PropriedadePlano)obj).getPropriedade());
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
		if (propriedade != null) result.append(this.propriedade.getIdPropriedade());
		if (planoPreco != null) result.append(this.planoPreco.getIdtPlanoPreco());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[PropriedadePlano]");
		if (propriedade != null) result.append("PROPRIEDADE=" + this.propriedade.getDesPropriedade());
		if (planoPreco != null) result.append(";PLANO_PRECO=" + this.planoPreco.getDesPlanoPreco());
		result.append(";VALOR_PROPRIEDADE=" + this.valorPropriedade);
		
		return result.toString();
	}

}
