package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import java.util.Collection;
import java.util.Iterator;

import TINC.FundTransferOpPackage.eFundTransferOp;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.FundTransfer;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.ValorRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Entidade que representa os parametros para a operacao onlineFundTransfer do Payment Interface da Tecnomen.
 *
 *	@version		1.0		02/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 */
public class OnlineFundTransfer extends FundTransfer
{

	/**
	 *	Constante interna da Tecnomen referente a operacao de Recarga (Online Fund Transfer).
	 */
	public static final short OPERACAO = eFundTransferOp._op_OnlineFundTransfer;
	
	/**
	 *	Constante referente ao transaction type de recarga (Online Fund Transfer).
	 */
	public static final short TIPO_TRANSACAO = Definicoes.TECNOMEN_ONLINE_TIPO_TRANSACAO;
	
	/**
	 *	Numero de dias de expiracao do Saldo Principal.
	 */
	private short numDiasExpPrincipal;
	
	/**
	 *	Numero de dias de expiracao do Saldo Periodico.
	 */
	private short numDiasExpPeriodico;
	
	/**
	 *	Numero de dias de expiracao do Saldo de Bonus.
	 */
	private short numDiasExpBonus;
	
	/**
	 *	Numero de dias de expiracao do Saldo de Torpedos.
	 */
	private short numDiasExpTorpedos;
	
	/**
	 *	Numero de dias de expiracao do Saldo de Dados.
	 */
	private short numDiasExpDados;

	/**
	 *	Construtor da classe.
	 */
	public OnlineFundTransfer()
	{
		super();
		
		this.numDiasExpPrincipal	= 0;
		this.numDiasExpPeriodico	= 0;
		this.numDiasExpBonus		= 0;
		this.numDiasExpTorpedos		= 0;
		this.numDiasExpDados		= 0;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		valoresRecarga			Lista de valores de recarga.
	 */
	public OnlineFundTransfer(String msisdn, Collection valoresRecarga)
	{
		this();
		
		super.setMsisdn(msisdn);
		this.extrairValores(valoresRecarga);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		valoresRecarga			Objeto de valores de recarga.
	 */
	public OnlineFundTransfer(String msisdn, ValoresRecarga valoresRecarga)
	{
		this();
		
		super.setMsisdn(msisdn);
		this.extrairValores(valoresRecarga);
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Principal.
	 *
	 *	@return		Numero de dias de expiracao do Saldo Principal.
	 */
	public short getNumDiasExpPrincipal()
	{
		return this.numDiasExpPrincipal;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@return		Numero de dias de expiracao do Saldo Periodico.
	 */
	public short getNumDiasExpPeriodico()
	{
		return this.numDiasExpPeriodico;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Bonus.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Bonus.
	 */
	public short getNumDiasExpBonus()
	{
		return this.numDiasExpBonus;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Torpedos.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Torpedos.
	 */
	public short getNumDiasExpTorpedos()
	{
		return this.numDiasExpTorpedos;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Dados.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Dados.
	 */
	public short getNumDiasExpDados()
	{
		return this.numDiasExpDados;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Principal.
	 *
	 *	@param		numDiasExpPrincipal		Numero de dias de expiracao do Saldo Principal.
	 */
	public void setNumDiasExpPrincipal(short numDiasExpPrincipal)
	{
		this.numDiasExpPrincipal = numDiasExpPrincipal;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@param		numDiasExpPeriodico		Numero de dias de expiracao do Saldo Periodico.
	 */
	public void setNumDiasExpPeriodico(short numDiasExpPeriodico)
	{
		this.numDiasExpPeriodico = numDiasExpPeriodico;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Bonus.
	 *
	 *	@param		numDiasExpBonus			Numero de dias de expiracao do Saldo de Bonus.
	 */
	public void setNumDiasExpBonus(short numDiasExpBonus)
	{
		this.numDiasExpBonus = numDiasExpBonus;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Torpedos.
	 *
	 *	@param		numDiasExpTorpedos		Numero de dias de expiracao do Saldo de Torpedos.
	 */
	public void setNumDiasExpTorpedos(short numDiasExpTorpedos)
	{
		this.numDiasExpTorpedos = numDiasExpTorpedos;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Dados.
	 *
	 *	@param		numDiasExpDados			Numero de dias de expiracao do Saldo de Dados.
	 */
	public void setNumDiasExpDados(short numDiasExpDados)
	{
		this.numDiasExpDados = numDiasExpDados;
	}
	
	/**
	 *	Extrai os parametros para a operacao onlineFundTransfer a partir da lista de valores de recarga.
	 *
	 *	@param		valoresRecarga			Lista de valores de recarga.
	 */
	private void extrairValores(Collection valoresRecarga)
	{
		if(valoresRecarga != null)
			for(Iterator iterator = valoresRecarga.iterator(); iterator.hasNext();)
			{
				ValorRecarga valor = (ValorRecarga)iterator.next();
				
				switch(valor.getTipoSaldo().getIdtTipoSaldo())
				{
					case TipoSaldo.PRINCIPAL:
						super.setValorPrincipal(valor.getVlrCredito());
						this.setNumDiasExpPrincipal(valor.getNumDiasExpiracao());
						break;
					case TipoSaldo.PERIODICO:
						super.setValorPeriodico(valor.getVlrCredito());
						this.setNumDiasExpPeriodico(valor.getNumDiasExpiracao());
						break;
					case TipoSaldo.BONUS:
						super.setValorBonus(valor.getVlrCredito());
						this.setNumDiasExpBonus(valor.getNumDiasExpiracao());
						break;
					case TipoSaldo.TORPEDOS:
						super.setValorTorpedos(valor.getVlrCredito());
						this.setNumDiasExpTorpedos(valor.getNumDiasExpiracao());
						break;
					case TipoSaldo.DADOS:
						super.setValorDados(valor.getVlrCredito());
						this.setNumDiasExpDados(valor.getNumDiasExpiracao());
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
	private void extrairValores(ValoresRecarga valoresRecarga)
	{
		if(valoresRecarga != null)
		{
			//Extraindo os valores do Saldo Principal.
			super.setValorPrincipal(valoresRecarga.getSaldoPrincipal());
			this.setNumDiasExpPrincipal(valoresRecarga.getNumDiasExpPrincipal());
//Nao implementado pela Tecnomen			//Extraindo os valores do Saldo Periodico.
//Nao implementado pela Tecnomen			super.setValorPeriodico(valoresRecarga.getSaldoPeriodico());
//Nao implementado pela Tecnomen			this.setNumDiasExpPeriodico(valoresRecarga.getNumDiasExpPeriodico());
			//Extraindo os valores do Saldo de Bonus.
			super.setValorBonus(valoresRecarga.getSaldoBonus());
			this.setNumDiasExpBonus(valoresRecarga.getNumDiasExpBonus());
			//Extraindo os valores do Saldo de Torpedos.
			super.setValorTorpedos(valoresRecarga.getSaldoSMS());
			this.setNumDiasExpTorpedos(valoresRecarga.getNumDiasExpSMS());
			//Extraindo os valores do Saldo de Dados.
			super.setValorDados(valoresRecarga.getSaldoGPRS());
			this.setNumDiasExpDados(valoresRecarga.getNumDiasExpGPRS());
		}
	}
	
}
