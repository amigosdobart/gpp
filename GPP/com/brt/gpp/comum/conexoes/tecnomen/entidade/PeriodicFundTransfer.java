package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import java.util.Collection;
import java.util.Iterator;

import TINC.FundTransferOpPackage.eFundTransferOp;

import com.brt.gpp.comum.conexoes.tecnomen.entidade.FundTransfer;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.ValorRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Entidade que representa os parametros para a operacao periodicFundTransfer do Payment Interface da Tecnomen.
 *
 *	@version		1.0		14/05/2007		Primeira versao.
 *	@author			Daniel Ferreira
 */
public class PeriodicFundTransfer extends FundTransfer
{

	/**
	 *	Constante interna da Tecnomen referente a operacao de Recarga de Franquia (Periodic Fund Transfer).
	 */
	public static final short OPERACAO = eFundTransferOp._op_PeriodFundTransfer;
	
	/**
	 *	Numero de dias de expiracao do Saldo Periodico.
	 */
	private short numDiasExpPeriodico;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		valoresRecarga			Lista de valores de recarga.
	 */
	public PeriodicFundTransfer(String msisdn, Collection valoresRecarga)
	{
		super.setMsisdn(msisdn);
		this.extrairValores(valoresRecarga);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		valoresRecarga			Objeto de valores de recarga.
	 */
	public PeriodicFundTransfer(String msisdn, ValoresRecarga valoresRecarga)
	{
		super.setMsisdn(msisdn);
		this.extrairValores(valoresRecarga);
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
	 *	Atribui o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@param		numDiasExpPeriodico		Numero de dias de expiracao do Saldo Periodico.
	 */
	public void setNumDiasExpPeriodico(short numDiasExpPeriodico)
	{
		this.numDiasExpPeriodico = numDiasExpPeriodico;
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
				
				if(valor.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.PERIODICO)
				{
					super.setValorPeriodico(valor.getVlrCredito());
					this.setNumDiasExpPeriodico(valor.getNumDiasExpiracao());
					break;
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
			//Extraindo os valores do Saldo Periodico.
			super.setValorPeriodico(valoresRecarga.getSaldoPeriodico());
			this.setNumDiasExpPeriodico(valoresRecarga.getNumDiasExpPeriodico());
		}
	}
	
}
