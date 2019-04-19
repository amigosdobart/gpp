package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

/**
 *	Entidade que representa o resultado da operacao fundTransfer do Payment Interface de Tecnomen.
 *
 *	@version		1.0		14/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 */
public class FundTransferResults
{

	/**
	 *	Status do assinante apos a operacao.
	 */
	private short statusAssinante;
	
	/**
	 *	Byte de opcoes do assinante (Sub Options) apos a operacao.
	 */
	private short subOptions;
	
	/**
	 *	Saldo Principal do assinante apos a operacao.
	 */
	private double saldoPrincipal;
	
	/**
	 *	Saldo Periodico do assinante apos a operacao.
	 */
	private double saldoPeriodico;
	
	/**
	 *	Saldo de Bonus do assinante apos a operacao.
	 */
	private double saldoBonus;
	
	/**
	 *	Saldo de Torpedos do assinante apos a operacao.
	 */
	private double saldoTorpedos;
	
	/**
	 *	Saldo de Dados do assinante apos a operacao.
	 */
	private double saldoDados;
	
	/**
	 *	Data de expiracao do Saldo Principal do assinante apos a operacao.
	 */
	private Date dataExpPrincipal;
	
	/**
	 *	Data de expiracao do Saldo Periodico do assinante apos a operacao.
	 */
	private Date dataExpPeriodico;
	
	/**
	 *	Data de expiracao do Saldo de Bonus do assinante apos a operacao.
	 */
	private Date dataExpBonus;
	
	/**
	 *	Data de expiracao do Saldo de Torpedos do assinante apos a operacao.
	 */
	private Date dataExpTorpedos;
	
	/**
	 *	Data de expiracao do Saldo de Dados do assinante apos a operacao.
	 */
	private Date dataExpDados;
	
	/**
	 *	Construtor da classe.
	 */
	public FundTransferResults()
	{
		this.statusAssinante	= 0;
		this.subOptions			= 0;
		this.saldoPrincipal		= 0.0;
		this.saldoPeriodico		= 0.0;
		this.saldoBonus			= 0.0;
		this.saldoTorpedos		= 0.0;
		this.saldoDados			= 0.0;
		this.dataExpPrincipal	= null;
		this.dataExpPeriodico	= null;
		this.dataExpBonus		= null;
		this.dataExpTorpedos	= null;
		this.dataExpDados		= null;
	}
	
	/**
	 *	Retorna o status do assinante apos a operacao.
	 *
	 *	@return		Status do assinante apos a operacao.
	 */
	public short getStatusAssinante()
	{
		return this.statusAssinante;
	}
	
	/**
	 *	Retorna o byte de opcoes do assinante apos a operacao.
	 *
	 *	@return		Byte de opcoes do assinante apos a operacao.
	 */
	public short getSubOptions()
	{
		return this.subOptions;
	}
	
	/**
	 *	Retorna o Saldo Principal do assinante apos a operacao.
	 *
	 *	@return		Saldo Principal do assinante apos a operacao.
	 */
	public double getSaldoPrincipal()
	{
		return this.saldoPrincipal;
	}
	
	/**
	 *	Retorna o Saldo Periodico do assinante apos a operacao.
	 *
	 *	@return		Saldo Periodico do assinante apos a operacao.
	 */
	public double getSaldoPeriodico()
	{
		return this.saldoPeriodico;
	}
	
	/**
	 *	Retorna o Saldo de Bonus do assinante apos a operacao.
	 *
	 *	@return		Saldo de Bonus do assinante apos a operacao.
	 */
	public double getSaldoBonus()
	{
		return this.saldoBonus;
	}
	
	/**
	 *	Retorna o Saldo de Torpedos do assinante apos a operacao.
	 *
	 *	@return		Saldo de Torpedos do assinante apos a operacao.
	 */
	public double getSaldoTorpedos()
	{
		return this.saldoTorpedos;
	}
	
	/**
	 *	Retorna o Saldo de Dados do assinante apos a operacao.
	 *
	 *	@return		Saldo de Dados do assinante apos a operacao.
	 */
	public double getSaldoDados()
	{
		return this.saldoDados;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo Principal do assinante apos a operacao.
	 *
	 *	@return		Data de expiracao do Saldo Principal do assinante apos a operacao.
	 */
	public Date getDataExpPrincipal()
	{
		return this.dataExpPrincipal;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo Periodico do assinante apos a operacao.
	 *
	 *	@return		Data de expiracao do Saldo Periodico do assinante apos a operacao.
	 */
	public Date getDataExpPeriodico()
	{
		return this.dataExpPeriodico;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Bonus do assinante apos a operacao.
	 *
	 *	@return		Data de expiracao do Saldo de Bonus do assinante apos a operacao.
	 */
	public Date getDataExpBonus()
	{
		return this.dataExpBonus;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Torpedos do assinante apos a operacao.
	 *
	 *	@return		Data de expiracao do Saldo de Torpedos do assinante apos a operacao.
	 */
	public Date getDataExpTorpedos()
	{
		return this.dataExpTorpedos;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Dados do assinante apos a operacao.
	 *
	 *	@return		Data de expiracao do Saldo de Dados do assinante apos a operacao.
	 */
	public Date getDataExpDados()
	{
		return this.dataExpDados;
	}

	
	/**
	 *	Atribui o status do assinante apos a operacao.
	 *
	 *	@param		statusAssinante			Status do assinante apos a operacao.
	 */
	public void setStatusAssinante(short statusAssinante)
	{
		 this.statusAssinante = statusAssinante;
	}
	
	/**
	 *	Atribui o byte de opcoes do assinante apos a operacao.
	 *
	 *	@param		Byte de opcoes do assinante apos a operacao.
	 */
	public void setSubOptions(short subOptions)
	{
		 this.subOptions = subOptions;
	}
	
	/**
	 *	Atribui o Saldo Principal do assinante apos a operacao.
	 *
	 *	@param		saldoPrincipal			Saldo Principal do assinante apos a operacao.
	 */
	public void setSaldoPrincipal(double saldoPrincipal)
	{
		 this.saldoPrincipal = saldoPrincipal;
	}
	
	/**
	 *	Atribui o Saldo Periodico do assinante apos a operacao.
	 *
	 *	@param		saldoPeriodico			Saldo Periodico do assinante apos a operacao.
	 */
	public void setSaldoPeriodico(double saldoPeriodico)
	{
		 this.saldoPeriodico = saldoPeriodico;
	}
	
	/**
	 *	Atribui o Saldo de Bonus do assinante apos a operacao.
	 *
	 *	@param		saldoBonus				Saldo de Bonus do assinante apos a operacao.
	 */
	public void setSaldoBonus(double saldoBonus)
	{
		 this.saldoBonus = saldoBonus;
	}
	
	/**
	 *	Atribui o Saldo de Torpedos do assinante apos a operacao.
	 *
	 *	@param		saldoTorpedos			Saldo de Torpedos do assinante apos a operacao.
	 */
	public void setSaldoTorpedos(double saldoTorpedos)
	{
		 this.saldoTorpedos = saldoTorpedos;
	}
	
	/**
	 *	Atribui o Saldo de Dados do assinante apos a operacao.
	 *
	 *	@param		saldoDados				Saldo de Dados do assinante apos a operacao.
	 */
	public void setSaldoDados(double saldoDados)
	{
		 this.saldoDados = saldoDados;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Principal do assinante apos a operacao.
	 *
	 *	@param		dataExpPrincipal		Data de expiracao do Saldo Principal do assinante apos a operacao.
	 */
	public void setDataExpPrincipal(Date dataExpPrincipal)
	{
		 this.dataExpPrincipal = dataExpPrincipal;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Periodico do assinante apos a operacao.
	 *
	 *	@param		dataExpPeriodico		Data de expiracao do Saldo Periodico do assinante apos a operacao.
	 */
	public void setDataExpPeriodico(Date dataExpPeriodico)
	{
		 this.dataExpPeriodico = dataExpPeriodico;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Bonus do assinante apos a operacao.
	 *
	 *	@param		dataExpBonus			Data de expiracao do Saldo de Bonus do assinante apos a operacao.
	 */
	public void setDataExpBonus(Date dataExpBonus)
	{
		 this.dataExpBonus = dataExpBonus;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Torpedos do assinante apos a operacao.
	 *
	 *	@param		dataExpTorpedos			Data de expiracao do Saldo de Torpedos do assinante apos a operacao.
	 */
	public void setDataExpTorpedos(Date dataExpTorpedos)
	{
		 this.dataExpTorpedos = dataExpTorpedos;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Dados do assinante apos a operacao.
	 *
	 *	@param		dataExpDados			Data de expiracao do Saldo de Dados do assinante apos a operacao.
	 */
	public void setDataExpDados(Date dataExpDados)
	{
		 this.dataExpDados = dataExpDados;
	}
	
	/**
	 *	Atualiza o assinante de acordo com o resultado da operacao de recarga/ajuste.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 */
	public void atualizarAssinante(Assinante assinante)
	{
		assinante.setStatusAssinante       (this.getStatusAssinante ());
		assinante.setSaldoCreditosPrincipal(this.getSaldoPrincipal  ());
		assinante.setSaldoCreditosPeriodico(this.getSaldoPeriodico  ());
		assinante.setSaldoCreditosBonus    (this.getSaldoBonus      ());
		assinante.setSaldoCreditosSMS      (this.getSaldoTorpedos   ());
		assinante.setSaldoCreditosDados    (this.getSaldoDados      ());
		assinante.setDataExpiracaoPrincipal(this.getDataExpPrincipal());
		assinante.setDataExpiracaoPeriodico(this.getDataExpPeriodico());
		assinante.setDataExpiracaoBonus    (this.getDataExpBonus    ());
		assinante.setDataExpiracaoSMS      (this.getDataExpTorpedos ());
		assinante.setDataExpiracaoDados    (this.getDataExpDados    ());
	}
	
}
