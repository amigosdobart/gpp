package com.brt.gpp.comum.conexoes.tecnomen.conversor;

import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pela conversao entre os codigos de retorno do GPP e da Tecnomen.
 *
 *	@author		Daniel Ferreira
 *	@since		05/03/2007
 */
public abstract class ConversorRetornoAprovisionamento 
{

	/**
	 *	Operacao OK.
	 */
	public static final int OK = 0;
	
	/**
	 *	MSISDN nao existe.
	 */
	public static final int SUB_NOT_EXITS = 100;
	
	/**
	 *	Prepaid Server - IMSI ja em uso.
	 */
	public static final int PP_IMSI_EXISTS = 101;
	
	/**
	 *	MSISDN bloqueado.
	 */
	public static final int SUB_BLOCKED = 112;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao de usuario.
	 */
	public static final int CREATE_USER_STARTED = 1001;
	
	/**
	 *	Provisioning Server - MSISDN ja existe.
	 */
	public static final int LOGIN_EXITS = 1100;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao de assinante.
	 */
	public static final int ADD_PP_SERVICE_STARTED = 2001;
	
	/**
	 *	Provisioning Server - IMSI ja em uso.
	 */
	public static final int PROV_IMSI_EXISTS = 2103;
	
	/**
	 *	Provisioning Server - Identificador de idioma invalido.
	 */
	public static final int INVALID_LANGUAGE_ID = 2131;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao dos saldos de voz.
	 */
	public static final int ADD_MAIN_ACCOUNT_STARTED = 3001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao do Saldo de Torpedos.
	 */
	public static final int ADD_SM_ACCOUNT_STARTED = 4001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao do Saldo de Dados.
	 */
	public static final int ADD_DATA_ACCOUNT_STARTED = 5001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao dos servicos (todos).
	 */
	public static final int ADD_ALL_SERVICE_STARTED = 6001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao do servico SMMT.
	 */
	public static final int ADD_SMMT_SERVICE_STARTED = 7001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao do servico SMMO.
	 */
	public static final int ADD_SMMO_SERVICE_STARTED = 8001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao do servico MMS.
	 */
	public static final int ADD_MMS_SERVICE_STARTED = 9001;
	
	/**
	 *	Provisioning Server - Problemas de sincronizacao entre engine sincrono e assincrono na criacao do servico GPRS.
	 */
	public static final int ADD_GPRS_SERVICE_STARTED = 10001;
	
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
			case ConversorRetornoAprovisionamento.OK:
				return Definicoes.RET_OPERACAO_OK;
			case ConversorRetornoAprovisionamento.SUB_NOT_EXITS:
				return Definicoes.RET_MSISDN_NAO_ATIVO;
			case ConversorRetornoAprovisionamento.SUB_BLOCKED:
				return Definicoes.RET_MSISDN_BLOQUEADO;
			case ConversorRetornoAprovisionamento.LOGIN_EXITS:
				return Definicoes.RET_MSISDN_JA_ATIVO;
			case ConversorRetornoAprovisionamento.PROV_IMSI_EXISTS:
			case ConversorRetornoAprovisionamento.PP_IMSI_EXISTS:
				return Definicoes.RET_IMSI_JA_EM_USO;
			case ConversorRetornoAprovisionamento.INVALID_LANGUAGE_ID:
				return Definicoes.RET_DADOS_ASSINANTE_INVALIDOS;
			case ConversorRetornoAprovisionamento.CREATE_USER_STARTED:
			case ConversorRetornoAprovisionamento.ADD_PP_SERVICE_STARTED:
			case ConversorRetornoAprovisionamento.ADD_MAIN_ACCOUNT_STARTED:
			case ConversorRetornoAprovisionamento.ADD_SM_ACCOUNT_STARTED:
			case ConversorRetornoAprovisionamento.ADD_DATA_ACCOUNT_STARTED:
			case ConversorRetornoAprovisionamento.ADD_ALL_SERVICE_STARTED:
			case ConversorRetornoAprovisionamento.ADD_SMMT_SERVICE_STARTED:
			case ConversorRetornoAprovisionamento.ADD_SMMO_SERVICE_STARTED:
			case ConversorRetornoAprovisionamento.ADD_MMS_SERVICE_STARTED:
			case ConversorRetornoAprovisionamento.ADD_GPRS_SERVICE_STARTED:
				return Definicoes.RET_ERRO_SINCRONIZACAO_TECNOMEN;
			default: return Definicoes.RET_ERRO_GENERICO_TECNOMEN;
		}
	}
	
}
