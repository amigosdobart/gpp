package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidade <code>ValorDeslocamento</code>. Referência: TBL_TAR_VALOR_DESLOCAMENTO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class ValorDeslocamento implements Serializable
{
	private String idDeslocamento;
	private String desDeslocamento;
	private BigDecimal valorDeslocamento;
	private String tipoDeslocamento;

	/**
	 * @return Descrição do deslocamento
	 */
	public String getDesDeslocamento() 
	{
		return desDeslocamento;
	}

	/**
	 * @param desDeslocamento Descrição do deslocamento
	 */
	public void setDesDeslocamento(String desDeslocamento) 
	{
		this.desDeslocamento = desDeslocamento;
	}

	/**
	 * @return ID do deslocamento
	 */
	public  String getIdDeslocamento() 
	{
		return idDeslocamento;
	}

	/**
	 * @param idDeslocamento ID do deslocamento
	 */
	public void setIdDeslocamento( String idDeslocamento) 
	{
		this.idDeslocamento = idDeslocamento;
	}
	/**
	 * @return Tipo de deslocamento
	 */
	public String getTipoDeslocamento() 
	{
		return tipoDeslocamento;
	}

	/**
	 * @param tipoDeslocamento Tipo de deslocamento
	 */
	public void setTipoDeslocamento(String tipoDeslocamento) 
	{
		this.tipoDeslocamento = tipoDeslocamento;
	}

	/**
	 * @return Valor de deslocamento
	 */
	public BigDecimal getValorDeslocamento() 
	{
		return valorDeslocamento;
	}

	/**
	 * @param valorDeslocamento Valor de deslocamento
	 */
	public void setValorDeslocamento(BigDecimal valorDeslocamento) 
	{
		this.valorDeslocamento = valorDeslocamento;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof ValorDeslocamento))
			return false;
		
		if (obj == this)
			return true;
		
		return idDeslocamento == ((ValorDeslocamento)obj).getIdDeslocamento();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idDeslocamento);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[ValorDeslocamento]");
		result.append("ID=" + this.idDeslocamento);
		if (valorDeslocamento != null) result.append(";VALOR=" + this.valorDeslocamento);
		if (desDeslocamento != null) result.append(";DESCRICAO=" + this.desDeslocamento);
		if (tipoDeslocamento != null) result.append(";TIPO=" + this.tipoDeslocamento);
	
		return result.toString();
	}

}
