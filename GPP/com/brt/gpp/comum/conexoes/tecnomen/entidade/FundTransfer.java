package com.brt.gpp.comum.conexoes.tecnomen.entidade;

/**
 *	Classe abstrata base para as entidades que representam os parametros para a operacao fundTransfer do Payment 
 *	Interface da Tecnomen.
 *
 *	@version		1.0		02/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 */
public abstract class FundTransfer 
{

	/**
	 *	MSISDN do assinante.
	 */
	private String msisdn;
	
	/**
	 *	Valor de credito no Saldo Principal.
	 */
	private double valorPrincipal;
	
	/**
	 *	Valor de credito no Saldo Periodico.
	 */
	private double valorPeriodico;
	
	/**
	 *	Valor de credito no Saldo de Bonus.
	 */
	private double valorBonus;
	
	/**
	 *	Valor de credito no Saldo de Torpedos.
	 */
	private double valorTorpedos;
	
	/**
	 *	Valor de credito no Saldo de Dados.
	 */
	private double valorDados;
	
	/**
	 *	Construtor da classe.
	 */
	public FundTransfer()
	{
		this.msisdn			= null;
		this.valorPrincipal = 0.0;
		this.valorPeriodico	= 0.0;
		this.valorBonus		= 0.0;
		this.valorTorpedos	= 0.0;
		this.valorDados		= 0.0;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		MSISDN do assinante.
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo Principal.
	 *
	 *	@return		Valor de credito no Saldo Principal.
	 */
	public double getValorPrincipal()
	{
		return this.valorPrincipal;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo Periodico.
	 *
	 *	@return		Valor de credito no Saldo Periodico.
	 */
	public double getValorPeriodico()
	{
		return this.valorPeriodico;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo de Bonus.
	 *
	 *	@return		Valor de credito no Saldo de Bonus.
	 */
	public double getValorBonus()
	{
		return this.valorBonus;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo de Torpedos.
	 *
	 *	@return		Valor de credito no Saldo de Torpedos.
	 */
	public double getValorTorpedos()
	{
		return this.valorTorpedos;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo de Dados.
	 *
	 *	@return		Valor de credito no Saldo de Dados.
	 */
	public double getValorDados()
	{
		return this.valorDados;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo Principal.
	 *
	 *	@param		valorPrincipal			Valor de credito no Saldo Principal.
	 */
	public void setValorPrincipal(double valorPrincipal)
	{
		this.valorPrincipal = valorPrincipal;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo Periodico.
	 *
	 *	@param		valorBonus				Valor de credito no Saldo Periodico.
	 */
	public void setValorPeriodico(double valorPeriodico)
	{
		this.valorPeriodico = valorPeriodico;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo de Bonus.
	 *
	 *	@param		valorBonus				Valor de credito no Saldo de Bonus.
	 */
	public void setValorBonus(double valorBonus)
	{
		this.valorBonus = valorBonus;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo de Torpedos.
	 *
	 *	@param		valorTorpedos			Valor de credito no Saldo de Torpedos.
	 */
	public void setValorTorpedos(double valorTorpedos)
	{
		this.valorTorpedos = valorTorpedos;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo de Dados.
	 *
	 *	@param		valorDados				Valor de credito no Saldo de Dados.
	 */
	public void setValorDados(double valorDados)
	{
		this.valorDados = valorDados;
	}
	
}
