//Definicao do Pacote
package com.brt.gpp.aplicacoes.alterarStatusPreAtivo;

import java.util.Date;

/**
  *
  * Este arquivo contem a definicao da classe de AlteraPreAtivoVO
  * @version	1.0
  * @autor		Marcelo Alves Araujo
  * @since		11/11/2005
  *
  */

public class AlteraPreAtivoVO
{
	private String	msisdn;
	private int 	plano;
	private Date	data;
	
	
	/**
	 * Construtor da Classe
	 * @param String
	 * @param int
	 * @param Date
	 */
	public AlteraPreAtivoVO(String msisdn, int plano, Date data)
	{
		this.data = data;
		this.msisdn = msisdn;
		this.plano = plano;
	}

	/**
	 * @return Date
	 */
	public Date getData()
	{
		return data;
	}
	
	/**
	 * @return String
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	
	/**
	 * @return int
	 */
	public int getPlano()
	{
		return plano;
	}
	
	/**
	 * @param Date
	 */
	public void setData(Date data)
	{
		this.data = data;
	}
	
	/**
	 * @param String
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 * @param int
	 */
	public void setPlano(int plano)
	{
		this.plano = plano;
	}
}
