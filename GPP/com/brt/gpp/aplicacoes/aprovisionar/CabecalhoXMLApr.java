package com.brt.gpp.aplicacoes.aprovisionar;

public class CabecalhoXMLApr 
{
	// Atributos do cabeçalho de um XML para o Aprovisionamento
	private String idOs;
	private String caseType;
	private String caseSubType;
	private String categoria;
	private String orderPriority;
	private String msisdn;
	private String codigoErro;
	private String statusProcessamento;

	// Construtor com valores
	public CabecalhoXMLApr(String aIdOs, String aCaseType, String aCaseSubType, String aCategoria, String aOrderPriority, String aMsisdn)
	{
		this.idOs = aIdOs;
		this.caseType = aCaseType;
		this.caseSubType = aCaseSubType;
		this.categoria  = aCategoria;
		this.orderPriority = aOrderPriority;
		this.msisdn = aMsisdn;
	}
	
	// Construtor Padrão
	public CabecalhoXMLApr()
	{
		// Não faz nada
	}

	/**
	 * @return
	 */
	public String getCaseType()
	{
		return caseType;
	}

	/**
	 * @return
	 */
	public String getCategoria()
	{
		return categoria;
	}

	/**
	 * @return
	 */
	public String getIdOs()
	{
		return idOs;
	}

	/**
	 * @return
	 */
	public String getOrderPriority()
	{
		return orderPriority;
	}

	/**
	 * @param string
	 */
	public void setCaseType(String string)
	{
		caseType = string;
	}

	/**
	 * @param string
	 */
	public void setCategoria(String string)
	{
		categoria = string;
	}

	/**
	 * @param string
	 */
	public void setIdOs(String string)
	{
		idOs = string;
	}

	/**
	 * @param string
	 */
	public void setOrderPriority(String string)
	{
		orderPriority = string;
	}

	/**
	 * @return
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * @param string
	 */
	public void setMsisdn(String string)
	{
		msisdn = string;
	}

	/**
	 * @return
	 */
	public String getCaseSubType()
	{
		return caseSubType;
	}

	/**
	 * @param string
	 */
	public void setCaseSubType(String string)
	{
		caseSubType = string;
	}
	
	/**
	 * @return Returns the codigoErro.
	 */
	public String getCodigoErro()
	{
		return codigoErro;
	}
	/**
	 * @param codigoErro The codigoErro to set.
	 */
	public void setCodigoErro(String codigoErro)
	{
		this.codigoErro = codigoErro;
	}
	/**
	 * @return Returns the statusProcessamento
	 */
	public String getStatusProcessamento()
	{
		return statusProcessamento;
	}
	/**
	 * @param statusProcessamento The statusProcessamento to set.
	 */
	public void setStatusProcessamento(String statusProcessamento)
	{
		this.statusProcessamento = statusProcessamento;
	}
}


