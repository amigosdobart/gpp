package br.com.brasiltelecom.ppp.portal.entity;


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
	private int id;
	private Campanha campanha;
	private String mensagemSMS;
	private int diasPeriodicidade;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}
}
