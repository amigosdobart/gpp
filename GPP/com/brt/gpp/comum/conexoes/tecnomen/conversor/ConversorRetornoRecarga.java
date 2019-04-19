package com.brt.gpp.comum.conexoes.tecnomen.conversor;

import TINC.FundTransferOpPackage.eFundTransferResult;

import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pela conversao dos codigos de retorno de recarga entre o GPP e a Tecnomen.
 *
 *	@author		Daniel Ferreira
 *	@since		05/03/2007
 */
public abstract class ConversorRetornoRecarga 
{

	/**
	 *	Operacao OK.
	 */
	public static final int OPERACAO_OK = eFundTransferResult._e_OK;
	
	/**
	 *	MSISDN nao existe.
	 */
	public static final int MSISDN_NAO_ATIVO = eFundTransferResult._e_SubscriberDoesNotExist;
	
	/**
	 *	Status de MSISDN invalido.
	 */
	public static final int STATUS_MSISDN_INVALIDO = eFundTransferResult._e_InvalidAccountState;
	
	/**
	 *	Valor de credito invalido.
	 */
	public static final int VALOR_CREDITO_INVALIDO = eFundTransferResult._e_InvalidAmount;
	
	/**
	 *	Parametro invalido.
	 */
	public static final int PARAMETRO_INVALIDO = eFundTransferResult._e_InvalidParamType;
	
	/**
	 *	MSISDN bloqueado.
	 */
	public static final int MSISDN_BLOQUEADO = eFundTransferResult._e_InvalidServiceState;
	
	/**
	 *	Saldo Principal insuficiente.
	 */
	public static final int SALDO_PRINCIPAL_INSUFICIENTE = eFundTransferResult._e_InvalidMainBalanceDelta;
	
	/**
	 *	Saldo Periodico insuficiente.
	 */
	public static final int SALDO_PERIODICO_INSUFICIENTE = eFundTransferResult._e_InvalidPeriodicBalanceDelta;
	
	/**
	 *	Saldo de Bonus insuficiente.
	 */
	public static final int SALDO_BONUS_INSUFICIENTE = eFundTransferResult._e_InvalidBonusBalanceDelta;
	
	/**
	 *	Saldo de Torpedos insuficiente.
	 */
	public static final int SALDO_TORPEDOS_INSUFICIENTE = eFundTransferResult._e_InvalidSmBalanceDelta;
	
	/**
	 *	Saldo de Dados insuficiente.
	 */
	public static final int SALDO_DADOS_INSUFICIENTE = eFundTransferResult._e_InvalidDataBalanceDelta;
	
	/**
	 *	Converte e retorna o codigo de retorno do GPP a partir do codigo de retorno da Tecnomen.
	 *
	 *	@param		retorno					Codigo de retorno da Tecnomen.
	 *	@return		Codigo de retorno do GPP.
	 */
	public static short toRetornoGPP(int retorno)
	{
		switch(retorno)
		{
			case ConversorRetornoRecarga.OPERACAO_OK:
				return Definicoes.RET_OPERACAO_OK;
			case ConversorRetornoRecarga.MSISDN_NAO_ATIVO:
				return Definicoes.RET_MSISDN_NAO_ATIVO;
			case ConversorRetornoRecarga.STATUS_MSISDN_INVALIDO:
				return Definicoes.RET_STATUS_MSISDN_INVALIDO;
			case ConversorRetornoRecarga.VALOR_CREDITO_INVALIDO:
			case ConversorRetornoRecarga.PARAMETRO_INVALIDO:
				return Definicoes.RET_VALOR_CREDITO_INVALIDO;
			case ConversorRetornoRecarga.MSISDN_BLOQUEADO:
				return Definicoes.RET_MSISDN_BLOQUEADO;
			case ConversorRetornoRecarga.SALDO_PRINCIPAL_INSUFICIENTE:
			case ConversorRetornoRecarga.SALDO_PERIODICO_INSUFICIENTE:
			case ConversorRetornoRecarga.SALDO_BONUS_INSUFICIENTE:
			case ConversorRetornoRecarga.SALDO_TORPEDOS_INSUFICIENTE:
			case ConversorRetornoRecarga.SALDO_DADOS_INSUFICIENTE:
				return Definicoes.RET_CREDITO_INSUFICIENTE;
			default: return Definicoes.RET_ERRO_GENERICO_TECNOMEN;
		}
	}
	
}
