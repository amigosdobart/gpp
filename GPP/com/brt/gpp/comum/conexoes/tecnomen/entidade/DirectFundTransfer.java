package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import TINC.FundTransferOpPackage.eFundTransferOp;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.FundTransfer;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.ValorAjuste;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Entidade que representa os parametros para a operacao directFundTransfer do Payment Interface da Tecnomen.
 *
 *	@version		1.0		02/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 */
public class DirectFundTransfer extends FundTransfer
{

	/**
	 *	Constante interna da Tecnomen referente a operacao de Ajuste (Direct Fund Transfer).
	 */
	public static final short OPERACAO = eFundTransferOp._op_DirectFundTransfer;
	
	/**
	 *	Constante referente ao transaction type de ajuste (Direct Fund Transfer).
	 */
	public static final short TIPO_TRANSACAO = Definicoes.TECNOMEN_DIRECT_TIPO_TRANSACAO;
	
	/**
	 *	Data de expiracao do Saldo Principal.
	 */
	private Date dataExpPrincipal;
	
	/**
	 *	Data de expiracao do Saldo Periodico.
	 */
	private Date dataExpPeriodico;
	
	/**
	 *	Data de expiracao do Saldo de Bonus.
	 */
	private Date dataExpBonus;
	
	/**
	 *	Data de expiracao do Saldo de Torpedos.
	 */
	private Date dataExpTorpedos;
	
	/**
	 *	Data de expiracao do Saldo de Dados.
	 */
	private Date dataExpDados;

	/**
	 *	Construtor da classe.
	 */
	public DirectFundTransfer()
	{
		super();
		
		this.dataExpPrincipal	= null;
		this.dataExpPeriodico	= null;
		this.dataExpBonus		= null;
		this.dataExpTorpedos	= null;
		this.dataExpDados		= null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		valoresAjuste			Lista de valores de ajuste.
	 */
	public DirectFundTransfer(String msisdn, Collection valoresAjuste)
	{
		this();
		
		super.setMsisdn(msisdn);
		this.extrairValores(valoresAjuste);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		valoresAjuste			Objeto de valores de ajuste.
	 */
	public DirectFundTransfer(String msisdn, ValoresRecarga valoresAjuste)
	{
		this();
		
		super.setMsisdn(msisdn);
		this.extrairValores(valoresAjuste);
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo Principal.
	 *
	 *	@return		Data de expiracao do Saldo Principal.
	 */
	public Date getDataExpPrincipal()
	{
		return this.dataExpPrincipal;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo Periodico.
	 *
	 *	@return		Data de expiracao do Saldo Periodico.
	 */
	public Date getDataExpPeriodico()
	{
		return this.dataExpPeriodico;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Bonus.
	 *
	 *	@return		Data de expiracao do Saldo de Bonus.
	 */
	public Date getDataExpBonus()
	{
		return this.dataExpBonus;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Torpedos.
	 *
	 *	@return		Data de expiracao do Saldo de Torpedos.
	 */
	public Date getDataExpTorpedos()
	{
		return this.dataExpTorpedos;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Dados.
	 *
	 *	@return		Data de expiracao do Saldo de Dados.
	 */
	public Date getDataExpDados()
	{
		return this.dataExpDados;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Principal.
	 *
	 *	@param		dataExpPrincipal		Data de expiracao do Saldo Principal.
	 */
	public void setDataExpPrincipal(Date dataExpPrincipal)
	{
		this.dataExpPrincipal = dataExpPrincipal;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Periodico.
	 *
	 *	@param		dataExpPeriodico		Data de expiracao do Saldo Periodico.
	 */
	public void setDataExpPeriodico(Date dataExpPeriodico)
	{
		this.dataExpPeriodico = dataExpPeriodico;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Bonus.
	 *
	 *	@param		dataExpBonus			Data de expiracao do Saldo de Bonus.
	 */
	public void setDataExpBonus(Date dataExpBonus)
	{
		this.dataExpBonus = dataExpBonus;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Torpedos.
	 *
	 *	@param		dataExpTorpedos			Data de expiracao do Saldo de Torpedos.
	 */
	public void setDataExpTorpedos(Date dataExpTorpedos)
	{
		this.dataExpTorpedos = dataExpTorpedos;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Dados.
	 *
	 *	@param		dataExpDados			Data de expiracao do Saldo de Dados.
	 */
	public void setDataExpDados(Date dataExpDados)
	{
		this.dataExpDados = dataExpDados;
	}
	
	/**
	 *	Extrai os parametros para a operacao onlineFundTransfer a partir da lista de valores de recarga.
	 *
	 *	@param		valoresAjuste			Lista de valores de ajuste.
	 */
	private void extrairValores(Collection valoresAjuste)
	{
		if(valoresAjuste != null)
			for(Iterator iterator = valoresAjuste.iterator(); iterator.hasNext();)
			{
				ValorAjuste valor = (ValorAjuste)iterator.next();
				
				switch(valor.getTipoSaldo().getIdtTipoSaldo())
				{
					case TipoSaldo.PRINCIPAL:
						super.setValorPrincipal(valor.getVlrCredito());
						this.setDataExpPrincipal(valor.getDataExpiracao());
						break;
					case TipoSaldo.PERIODICO:
						super.setValorPeriodico(valor.getVlrCredito());
						this.setDataExpPeriodico(valor.getDataExpiracao());
						break;
					case TipoSaldo.BONUS:
						super.setValorBonus(valor.getVlrCredito());
						this.setDataExpBonus(valor.getDataExpiracao());
						break;
					case TipoSaldo.TORPEDOS:
						super.setValorTorpedos(valor.getVlrCredito());
						this.setDataExpTorpedos(valor.getDataExpiracao());
						break;
					case TipoSaldo.DADOS:
						super.setValorDados(valor.getVlrCredito());
						this.setDataExpDados(valor.getDataExpiracao());
						break;
					default: break;
				}
			}
	}
	
	/**
	 *	Extrai os parametros para a operacao onlineFundTransfer a partir do objeto de valores de recarga.
	 *
	 *	@param		valoresRecarga			Objeto de valores de recarga.
	 */
	private void extrairValores(ValoresRecarga valoresAjuste)
	{
		if(valoresAjuste != null)
		{
			//Extraindo os valores do Saldo Principal.
			super.setValorPrincipal(valoresAjuste.getSaldoPrincipal());
			this.setDataExpPrincipal(valoresAjuste.getDataExpPrincipal());
			//Extraindo os valores do Saldo Periodico.
			super.setValorPeriodico(valoresAjuste.getSaldoPeriodico());
			this.setDataExpPeriodico(valoresAjuste.getDataExpPeriodico());
			//Extraindo os valores do Saldo de Bonus.
			super.setValorBonus(valoresAjuste.getSaldoBonus());
			this.setDataExpBonus(valoresAjuste.getDataExpBonus());
			//Extraindo os valores do Saldo de Torpedos.
			super.setValorTorpedos(valoresAjuste.getSaldoSMS());
			this.setDataExpTorpedos(valoresAjuste.getDataExpSMS());
			//Extraindo os valores do Saldo de Dados.
			super.setValorDados(valoresAjuste.getSaldoGPRS());
			this.setDataExpDados(valoresAjuste.getDataExpGPRS());
		}
	}
	
}
