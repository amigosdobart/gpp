package com.brt.gpp.aplicacoes.promocao.automatizacaoRebarba;

//Imports Java.

import java.util.Date;

/**
 *	Classe que representa o Value Object do Produtor-Consumidor da Automatizacao da Rebarba.
 * Foi definido para abstrair a ordem dos elementos do update da tabela, facilitando a implementacao
 * da clausula de update.
 * 
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/19 (yyyy/mm/dd)
 */
public class AutomatizacaoRebarbaVO
{
    
	private String				msisdn;
    private	Date				datExpPrinipal;
    private	Date				datExpBonus;
    private	Date				datExpSMS;
    private	Date				datExpDados;

    //Construtores.
    /**
     * Construtor cheio da entidade. 
     * @param msisdn
     * @param prinipal
     * @param bonus
     * @param dados
     * @param expSMS
     */
	public AutomatizacaoRebarbaVO(String msisdn, Date prinipal, Date bonus, Date expSMS, Date dados)
	{
		this.datExpBonus		= bonus;
		this.datExpDados		= dados;
		this.datExpPrinipal		= prinipal;
		this.datExpSMS			= expSMS;
		this.msisdn				= msisdn;
	}

	/**
	 * Construtor da classe
	 */
	public AutomatizacaoRebarbaVO()
	{
	    this.reset();
	}

	/**
	 *	Inicializa o objeto com valores vazios.
	 */
	public void reset()
	{
	    this.msisdn				= null;
		this.datExpBonus		= null;
		this.datExpDados		= null;
		this.datExpPrinipal		= null;
		this.datExpSMS			= null;
	}
	
	//Getters E Setters

	/**
	 * Retorna a data de Expiração do saldo de Bonus.
	 * @return Retorna o datExpBonus.
	 */
	public Date getDatExpBonus()
	{
		return datExpBonus;
	}

	/**
	 * Retorna a data de Expiração do saldo de Dados.
	 * @return Retorna o datExpDados.
	 */
	public Date getDatExpDados()
	{
		return datExpDados;
	}

	/**
	 * Retorna a data de Expiração do saldo Prinipal. 
	 * @return Retorna o datExpPrinipal.
	 */
	public Date getDatExpPrinipal()
	{
		return datExpPrinipal;
	}

	/**
	 * Retorna a data de Expiração do saldo de SMS.
	 * @return Retorna o datExpSMS.
	 */
	public Date getDatExpSMS()
	{
		return datExpSMS;
	}

	/**
	 * Retorna o msisdn.
	 * @return Retorna o msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * Ajusta a data de Expiração do saldo de Bonus.
	 * @param datExpBonus O datExpBonus para alterar.
	 */
	public void setDatExpBonus(Date datExpBonus)
	{
		this.datExpBonus = datExpBonus;
	}

	/**
	 * Ajusta a data de Expiração do saldo de Dados.
	 * @param datExpDados O datExpDados para alterar.
	 */
	public void setDatExpDados(Date datExpDados)
	{
		this.datExpDados = datExpDados;
	}

	/**
	 * Ajusta a data de Expiração do saldo Prinipal. 
	 * @param datExpPrinipal O datExpPrinipal para alterar.
	 */
	public void setDatExpPrinipal(Date datExpPrinipal)
	{
		this.datExpPrinipal = datExpPrinipal;
	}

	/**
	 * Ajusta a data de Expiração do saldo de SMS.
	 * @param datExpSMS O datExpSMS para alterar.
	 */
	public void setDatExpSMS(Date datExpSMS)
	{
		this.datExpSMS = datExpSMS;
	}

	/**
	 * Ajusta o MSISDN. 
	 * @param msisdn O msisdn para alterar.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
}