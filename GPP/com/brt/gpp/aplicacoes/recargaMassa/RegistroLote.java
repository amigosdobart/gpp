package com.brt.gpp.aplicacoes.recargaMassa;


/**
 * Entidade que representa uma linha do arquivo de lotes.
 * 
 * @author Bernardo Dias
 * Date 09/08/2007
 */
public class RegistroLote 
{
	private String lote;			// Identificação do lote
	private String msisdn;			// MSISDN do usuario
	private Double vlrBonus;		// Valor do bonus
	private Double vlrSm;			// Valor do bonus
	private Double vlrDados;		// Valor do bonus
	private String mensagemSMS;		// Mensagem SMS
	
	/**
	 * @return the lote
	 */
	public String getLote() 
	{
		return lote;
	}
	
	/**
	 * @param lote the lote to set
	 */
	public void setLote(String lote) 
	{
		this.lote = lote;
	}
	
	/**
	 * @return the mensagemSMS
	 */
	public String getMensagemSMS() 
	{
		return mensagemSMS;
	}
	
	/**
	 * @param mensagemSMS the mensagemSMS to set
	 */
	public void setMensagemSMS(String mensagemSMS) 
	{
		this.mensagemSMS = mensagemSMS;
	}
	
	/**
	 * @return the msisdn
	 */
	public String getMsisdn() 
	{
		return msisdn;
	}
	
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	
	/**
	 * @return the vlrBonus
	 */
	public Double getVlrBonus() 
	{
		return vlrBonus;
	}
	
	/**
	 * @param vlrBonus the vlrBonus to set
	 */
	public void setVlrBonus(Double vlrBonus) 
	{
		this.vlrBonus = vlrBonus;
	}
	
	/**
	 * @return the vlrDados
	 */
	public Double getVlrDados() 
	{
		return vlrDados;
	}
	
	/**
	 * @param vlrDados the vlrDados to set
	 */
	public void setVlrDados(Double vlrDados) 
	{
		this.vlrDados = vlrDados;
	}
	
	/**
	 * @return the vlrSm
	 */
	public Double getVlrSm() 
	{
		return vlrSm;
	}
	
	/**
	 * @param vlrSm the vlrSm to set
	 */
	public void setVlrSm(Double vlrSm) 
	{
		this.vlrSm = vlrSm;
	}
}
