package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author Marcelo Alves Araujo
 * @since  29/06/2005
 */

public class HistoricoBroadcastSMS 
{
	private Date	dataEnvio;
	private String	msisdn;
	private String	statusEnvio;
	private String	operador;
	private String	mensagem;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	// Métodos get
	
	/**
	 * @return
	 */
	public Date getDataEnvio() 
	{
		return this.dataEnvio;
	}

	/**
	 * @return
	 */
	public String getMsisdn() 
	{
		return this.msisdn;
	}

	/**
	 * @return Data Formatada
	 */
	public String getDataEnvioStr()
	{
		return this.sdf.format(getDataEnvio());
	}
	
	/**
	 * @return
	 */
	public String getStatusEnvio() 
	{
		return this.statusEnvio;
	}

	/**
	 * @return
	 */
	public String getOperador() 
	{
		return this.operador;
	}
	
	/**
	 * @return
	 */
	public String getMensagem() 
	{
		return this.mensagem;
	}

	// Métodos set
	
	/**
	 * @param Date
	 */
	public void setDataEnvio(Date aDataEnvio) 
	{
		this.dataEnvio = aDataEnvio;
	}

	/**
	 * @param String
	 */
	public void setMsisdn(String aMsisdn) 
	{
		this.msisdn = aMsisdn;
	}

	/**
	 * @param String
	 */
	public void setStatusEnvio(String aStatusEnvio) 
	{
		this.statusEnvio = aStatusEnvio;
	}

	/**
	 * @param String
	 */
	public void setOperador(String aOperador) 
	{
		this.operador = aOperador;
	}
	
	/**
	 * @param String
	 */
	public void setMensagem(String aMensagem) 
	{
		this.mensagem = aMensagem;
	}
}
