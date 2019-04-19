package com.brt.gpp.aplicacoes.campanha.entidade;


/**
 * Esta classe armazena as informacoes necessarias para o envio de SMS para os 
 * assinantes de uma determinada campanha afim de avisa-los de como estes poderao 
 * estar recebendo o bonus e quais as condicoes devem ser satisfeitas.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class SMSCampanha 
{
	private String mensagemSMS;
	private int diasPeriodicidade;
	private String tipoSmsCampanha;
	
	/**
	 * Access method for the mensagemSMS property.
	 * 
	 * @return   the current value of the mensagemSMS property
	 */
	public String getMensagemSMS() 
	{
		return mensagemSMS;
	}
	
	/**
	 * Sets the value of the mensagemSMS property.
	 * 
	 * @param aMensagemSMS the new value of the mensagemSMS property
	 */
	public void setMensagemSMS(String aMensagemSMS) 
	{
		mensagemSMS = aMensagemSMS;
	}
	
	/**
	 * Access method for the diasPeriodicidade property.
	 * 
	 * @return   the current value of the diasPeriodicidade property
	 */
	public int getDiasPeriodicidade() 
	{
		return diasPeriodicidade;
	}
	
	/**
	 * Sets the value of the diasPeriodicidade property.
	 * 
	 * @param aDiasPeriodicidade the new value of the diasPeriodicidade property
	 */
	public void setDiasPeriodicidade(int aDiasPeriodicidade) 
	{
		diasPeriodicidade = aDiasPeriodicidade;
	}
	
	/**
	 * Metodo....:gettipoSmsCampanha
	 * Descricao.:Retorna o valor de tipoSmsCampanha
	 * @return tipoSmsCampanha.
	 */
	public String getTipoSmsCampanha()
	{
		return tipoSmsCampanha;
	}
	
	/**
	 * Metodo....:settipoSmsCampanha
	 * Descricao.:Define o valor de tipoSmsCampanha
	 * @param tipoSmsCampanha o valor a ser definido para tipoSmsCampanha
	 */
	public void setTipoSmsCampanha(String tipoSmsCampanha)
	{
		this.tipoSmsCampanha = tipoSmsCampanha;
	}
}
