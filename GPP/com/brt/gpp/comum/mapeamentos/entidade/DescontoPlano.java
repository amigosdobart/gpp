package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidade <code>DescontoPlano</code>. Referência: TBL_TAR_DESCONTO_PLANO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class DescontoPlano implements Serializable
{
	private Desconto desconto;
	private PlanoPreco planoPreco;
	private int horaInicio;
	private int horaFim;
	private BigDecimal valorDesconto;
		
	/**
	 * @return Instancia de <code>Desconto</code>
	 */
	public Desconto getDesconto() 
	{
		return desconto;
	}

	/**
	 * @param desconto Instancia de <code>Desconto</code>
	 */
	public void setDesconto(Desconto desconto) 
	{
		this.desconto = desconto;
	}

	/**
	 * @return Hora final
	 */
	public int getHoraFim() {
		return horaFim;
	}

	/**
	 * @param horaFim Hora final
	 */
	public void setHoraFim(int horaFim) {
		this.horaFim = horaFim;
	}

	/**
	 * @return Hora inicial
	 */
	public int getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio Hora final
	 */
	public void setHoraInicio(int horaInicio) 
	{
		this.horaInicio = horaInicio;
	}

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
	 * @return Valor do desconto
	 */
	public BigDecimal getValorDesconto() 
	{
		return valorDesconto;
	}

	/**
	 * @param valorDesconto Valor do desconto
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof DescontoPlano))
			return false;
		
		if (obj == this)
			return true;
				
		boolean equal = true;	
		equal &= isEqual(this.planoPreco, 	((DescontoPlano)obj).getPlanoPreco());
		equal &= isEqual(this.desconto, 	((DescontoPlano)obj).getDesconto());
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
		if (desconto != null) result.append(this.desconto.getIdDesconto());
		if (planoPreco != null) result.append(this.planoPreco.getIdtPlanoPreco());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[DescontoPlano]");
		if (desconto != null) result.append("DESCONTO=" + this.desconto.getDesDesconto());
		if (planoPreco != null) result.append(";PLANO_PRECO=" + this.planoPreco.getDesPlanoPreco());
		result.append(";VALOR_DESCONTO=" + this.valorDesconto);
		result.append(";HORA_INICIO=" + this.horaFim);
		result.append(";HORA_FIM=" + this.horaInicio);
		
		return result.toString();
	}

}
