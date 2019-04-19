package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author Marcelo Alves Araujo
 * @since  29/06/2005
 */

public class ResultadosTesteSMS 
{
	private String	msisdn;
	private Date	dataProcessamento ;
	private int		idtStatusProcessamento ;
	private String	idMensagem ;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	// Métodos get
	
	/**
	 * @return
	 */
	public String getMsisdn() 
	{
		return this.msisdn;
	}

	/**
	 * @return
	 */
	public Date getDataProcessamento() 
	{
		return this.dataProcessamento;
	}

	public String getDataProcessamentoStr()
	{
		return sdf.format(getDataProcessamento());
	}
	
	/**
	 * @return
	 */
	public int getIdtStatusProcessamento() 
	{
		return this.idtStatusProcessamento;
	}

	/**
	 * @return
	 */
	public String getIdMensagem() 
	{
		return this.idMensagem;
	}

	// Métodos set
	
	/**
	 * @param String
	 */
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}

	/**
	 * @param date
	 */
	public void setDataProcessamento(Date dataProcessamento) 
	{
		this.dataProcessamento = dataProcessamento;
	}

	/**
	 * @param int
	 */
	public void setIdtStatusProcessamento(int statusProcessamento) 
	{
		this.idtStatusProcessamento = statusProcessamento;
	}

	/**
	 * @param String
	 */
	public void setIdMensagem(String idMensagem) 
	{
		this.idMensagem = idMensagem;
	}
}
