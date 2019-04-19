package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>RegiaoLAC</code>. Referência: TBL_TAR_REGIAO_LAC
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 11/04/2007
 */
public class RegiaoLAC implements Serializable
{
	private int idRegiaoLAC;
	private ValorDeslocamento valorDeslocamento;
	private CodigoNacional codigoNacional;
	private CodigoNacional cnAbrangencia;
	private Pais pais;
	
	/**
	 * @return Instancia de <code>CodigoNacional</code>
	 */
	public CodigoNacional getCnAbrangencia() 
	{
		return cnAbrangencia;
	}

	/**
	 * @param cnAbrangencia Instancia de <code>CodigoNacional</code>
	 */
	public void setCnAbrangencia(CodigoNacional cnAbrangencia) 
	{
		this.cnAbrangencia = cnAbrangencia;
	}

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
	 * @return ID da Regiao LAC
	 */
	public int getIdRegiaoLAC() 
	{
		return idRegiaoLAC;
	}

	/**
	 * @param idRegiaoLAC ID da Regiao LAC
	 */
	public void setIdRegiaoLAC(int idRegiaoLAC) 
	{
		this.idRegiaoLAC = idRegiaoLAC;
	}

	/**
	 * @return Instancia de <code>Pais</code>
	 */
	public Pais getPais() 
	{
		return pais;
	}

	/**
	 * @param pais Instancia de <code>Pais</code>
	 */
	public void setPais(Pais pais) 
	{
		this.pais = pais;
	}

	/**
	 * @return Instancia de <code>ValorDeslocamento</code>
	 */
	public ValorDeslocamento getValorDeslocamento() 
	{
		return valorDeslocamento;
	}

	/**
	 * @param valorDeslocamento Instancia de <code>ValorDeslocamento</code>
	 */
	public void setValorDeslocamento(ValorDeslocamento valorDeslocamento) 
	{
		this.valorDeslocamento = valorDeslocamento;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof RegiaoLAC))
			return false;
		
		if (obj == this)
			return true;
		
		return this.idRegiaoLAC == ((RegiaoLAC)obj).getIdRegiaoLAC();
	}

	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idRegiaoLAC);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[RegiaoLAC]");
		result.append("ID=" + this.idRegiaoLAC);
		
		if ( (pais != null) && (pais.getNomePais() != null)) 
			result.append(";PAIS=" + this.pais.getNomePais());

		if ( (codigoNacional != null) && (codigoNacional.getIdtUf() != null))
			result.append(";CN=" + this.codigoNacional.getIdtCodigoNacional());
			
		if ( (cnAbrangencia != null) && (cnAbrangencia.getIdtUf() != null))
			result.append(";CN_ABRANGECIA=" + this.cnAbrangencia.getIdtCodigoNacional());
		
		if ( (valorDeslocamento != null) && (valorDeslocamento.getDesDeslocamento() != null))
			result.append(";VALOR_DESLOCAMENTO=" + this.valorDeslocamento.getDesDeslocamento());

		return result.toString();
	}

	
}
