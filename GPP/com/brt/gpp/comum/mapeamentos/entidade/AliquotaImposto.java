package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entidade <code>AliquotaImposto</code>. Referência: TBL_GER_ALIQUOTA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 14/02/2007 
 */
public class AliquotaImposto implements Serializable
{
	private String 		imposto;
	private BigDecimal	valorAliquota;
	private Date 		dataInicio;
	private Date 		dataFim;
	private Estado 		estado;

	/**
	 * @return Data final da validade da alíquota
	 */
	public Date getDataFim() 
	{
		return dataFim;
	}
	
	/**
	 * @param dataFinal Data final da validade da alíquota
	 */
	public void setDataFim(Date dataFinal) 
	{
		this.dataFim = dataFinal;
	}
	
	/**
	 * @return Data inicial da validade da alíquota
	 */
	public Date getDataInicio() 
	{
		return dataInicio;
	}
	
	/**
	 * @param dataInicial Data inicial da validade da alíquota
	 */
	public void setDataInicio(Date dataInicial) 
	{
		this.dataInicio = dataInicial;
	}
	
	/**
	 * @return Instancia da entidade <code>Estado</code>
	 */
	public Estado getEstado() 
	{
		return estado;
	}
	
	/**
	 * @param estado Instancia da entidade <code>Estado</code>
	 */
	public void setEstado(Estado estado) 
	{
		this.estado = estado;
	}
	
	/**
	 * @return Nome do imposto
	 */
	public String getImposto() 
	{
		return imposto;
	}
	
	/**
	 * @param imposto Nome do imposto
	 */
	public void setImposto(String imposto) 
	{
		this.imposto = imposto;
	}
	
	/**
	 * @return Valor da alíquota de imposto
	 */
	public BigDecimal getValorAliquota() 
	{
		return valorAliquota;
	}
	
	/**
	 * @param valor Valor da alíquota de imposto
	 */
	public void setValorAliquota(BigDecimal valor) 
	{
		this.valorAliquota = valor;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof AliquotaImposto))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;		
		equal &= isEqual(this.imposto, 			((AliquotaImposto)obj).getImposto());
		equal &= isEqual(this.dataInicio, 		((AliquotaImposto)obj).getDataInicio());
		equal &= isEqual(this.estado, 			((AliquotaImposto)obj).getEstado());
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
		result.append(this.imposto);
		result.append(this.dataInicio);
		if (estado != null)	
			result.append(this.estado.getUF());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[AliquotaImposto]");
		if (imposto != null)		result.append("IMPOSTO=" + this.imposto);
		if (valorAliquota != null)	result.append(";VALOR=" + this.valorAliquota);
		if (dataInicio != null) 	result.append(";DATA_INICIO=" + this.dataInicio);
		if (dataFim != null)		result.append(";DATA_FIM=" + this.dataFim);
		if (estado != null)			result.append(";UF=" + this.estado.getUF());

		return result.toString();
	}
	
}
