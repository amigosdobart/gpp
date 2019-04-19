package com.brt.gpp.aplicacoes.exportacaoDW;

import java.util.*;

/**
  *
  * Este arquivo refere-se a classe DefinicoesSQLDW, responsavel 
  * pela definicao dos comandos sql a serem utilizados para a 
  * exportacao de dados para o DW. Alem da definicao dos comandos
  * a classe armazena a lista de todas as tabelas utilizadas no processo
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				10/06/2004
  *
  * Modificado por: Bernardo Dias
  * Data: 29-01-2008
  * Razao: Correções e melhoramentos diversos nos SQLs. A exportacao
  *        de recargas nao considerava aquelas recargas
  *        feitas durante o processamento dessa aplicacao.
  *
  */

public class DefinicoesSQLDW 
{
	private Map definicoesSQL;
	
	// A data de geração de registro está sendo colocada igual à data de importação
	// da tbl_apr_assinante_tecnomen. 
	private final String SQLDW_ASSINANTE = 
        "INSERT /*+APPEND*/ INTO tbl_int_dw_assinante " +
		"(dat_geracao_registro, sub_id, imsi, service_status, blocking_table_id, " +
		 "recharge_fraud_counter, total_recharge_error_counter, " +
		 "suspended_date, activation_date, account_balance, expiry, " +
		 "account_status, profile_id, sub_options, family_and_friends, frozen_date, " +
		 "bonus_balance, bonus_expiry, data_balance, data_expiry, " +
		 "sm_balance, sm_expiry " +
		") " +
		"SELECT dat_importacao, sub_id, imsi, service_status, blocking_table_id, " +
		       "recharge_fraud_counter, total_recharge_error_counter, " +
		       "suspended_date, activation_date, account_balance, expiry, " +
		       "account_status, profile_id, sub_options, family_and_friends, frozen_date, " +
		       "bonus_balance, bonus_expiry, data_balance, data_expiry, " +
		       "sm_balance, sm_expiry " +
		  "FROM tbl_apr_assinante_tecnomen a " +
		 "WHERE dat_importacao >= (SELECT NVL(MAX(DAT_PROCESSAMENTO),TO_DATE('01/01/1900','DD/MM/YYYY')) " +
									   "FROM TBL_EXT_DW_PARAMETRO_CARGA " +
									  "WHERE NOM_TABELA  = 'TBL_INT_DW_ASSINANTE' " +
										"AND IDT_SISTEMA = 'GPP' " +
									") " +
           "AND dat_importacao < to_date(?, 'dd/mm/yyyy hh24:mi:ss') " +
		   "AND NOT EXISTS (SELECT 1 " +
						     "FROM tbl_int_dw_assinante d " +
						    "WHERE d.dat_geracao_registro = a.dat_importacao " +
						      "AND d.sub_id               = a.sub_id " +
						   ") ";

	private final String SQLDW_CANAL =
	"INSERT /*+APPEND*/ INTO TBL_INT_DW_CANAL " +
	"(    DAT_GERACAO_REGISTRO " +
		",ID_CANAL " +
		",DES_CANAL " +
	") " +
	"SELECT   TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
			",ID_CANAL " +
			",DES_CANAL " +
	  "FROM TBL_REC_CANAL C " +
	 "WHERE NOT EXISTS (SELECT 1 " +
						 "FROM TBL_INT_DW_CANAL D " +
						"WHERE D.DAT_GERACAO_REGISTRO = TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
						  "AND D.ID_CANAL             = C.ID_CANAL " +
					  ")";

	private final String SQLDW_ORIGEM =
	"INSERT /*+APPEND*/ INTO TBL_INT_DW_ORIGEM " +
	"(    DAT_GERACAO_REGISTRO " +
		",ID_ORIGEM " +
		",DES_ORIGEM " +
		",IND_MODIFICAR_DATA_EXP " +
		",ID_CANAL " +
		",IND_ATIVO " +
	") " +
	"SELECT   TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
			",ID_ORIGEM " +
			",DES_ORIGEM " +
			",IND_MODIFICAR_DATA_EXP " +
			",ID_CANAL " +
			",IND_ATIVO " +
	  "FROM TBL_REC_ORIGEM O " +
	 "WHERE NOT EXISTS (SELECT 1 " +
						 "FROM TBL_INT_DW_ORIGEM D " +
						"WHERE D.DAT_GERACAO_REGISTRO = TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
						  "AND D.ID_CANAL             = O.ID_CANAL " +
						  "AND D.ID_ORIGEM            = O.ID_ORIGEM " +
					  ")";
	
	private final String SQLDW_SISTEMA_ORIGEM =
	"INSERT /*+APPEND*/ INTO TBL_INT_DW_SISTEMA_ORIGEM " +
	"(    DAT_GERACAO_REGISTRO " +
		",ID_SISTEMA_ORIGEM " +
		",DES_SISTEMA_ORIGEM " +
		",IND_VALIDA_SISTEMA_ORIGEM_CC " +
		",IND_VALIDA_SALDO_MAXIMO " +
	") " +
	"SELECT   TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
			",ID_SISTEMA_ORIGEM " +
			",DES_SISTEMA_ORIGEM " +
			",IND_VALIDA_SISTEMA_ORIGEM_CC " +
			",IND_VALIDA_SALDO_MAXIMO " +
	  "FROM TBL_REC_SISTEMA_ORIGEM S " +
	 "WHERE NOT EXISTS (SELECT 1 " +
						 "FROM TBL_INT_DW_SISTEMA_ORIGEM D " +
						"WHERE D.DAT_GERACAO_REGISTRO = TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
						  "AND D.ID_SISTEMA_ORIGEM    = S.ID_SISTEMA_ORIGEM " +
					  ")";

	private final String SQLDW_PLANO = 
	"INSERT /*+APPEND*/ INTO TBL_INT_DW_PLANO " +
	"(    DAT_GERACAO_REGISTRO " +
		",ID_CONFIGURACAO " +
		",DES_CONFIGURACAO " +
		",IND_HIBRIDO " +
	") " +
	"SELECT   TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
			",IDT_PLANO_PRECO " +
			",DES_PLANO_PRECO " +
			",IDT_CATEGORIA " +
	  "FROM TBL_GER_PLANO_PRECO C " +
	 "WHERE IDT_CATEGORIA < 2 " +
	   "AND NOT EXISTS (SELECT 1 " +
						 "FROM TBL_INT_DW_PLANO D " +
						"WHERE D.DAT_GERACAO_REGISTRO = TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
						  "AND D.ID_CONFIGURACAO      = C.IDT_CATEGORIA	 " +
					  ")";
	
	private final String SQLDW_EVENTO =
	"INSERT /*+APPEND*/ INTO TBL_INT_DW_EVENTO " +
	"(    DAT_GERACAO_REGISTRO " +
		",DAT_APROVISIONAMENTO " +
		",IDT_MSISDN " +
		",TIP_OPERACAO " +
		",IDT_IMSI " +
		",IDT_PLANO_PRECO " +
		",VLR_CREDITO_INICIAL " +
		",IDT_IDIOMA " +
		",IDT_ANTIGO_CAMPO " +
		",IDT_NOVO_CAMPO " +
		",IDT_TARIFA " +
		",DES_LISTA_FF " +
		",IDT_MOTIVO " +
		",NOM_OPERADOR " +
		",DES_STATUS " +
		",COD_RETORNO " +
	") " +
	"SELECT   TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
			",DAT_APROVISIONAMENTO " +
			",IDT_MSISDN " +
			",TIP_OPERACAO " +
			",IDT_IMSI " +
			",IDT_PLANO_PRECO " +
			",VLR_CREDITO_INICIAL " +
			",IDT_IDIOMA " +
			",IDT_ANTIGO_CAMPO " +
			",IDT_NOVO_CAMPO " +
			",IDT_TARIFA " +
			",DES_LISTA_FF " +
			",IDT_MOTIVO " +
			",NOM_OPERADOR " +
			",DES_STATUS " +
			",COD_RETORNO " +
	  "FROM TBL_APR_EVENTOS E " +
	 "WHERE E.DAT_APROVISIONAMENTO >= (SELECT NVL(MAX(DAT_PROCESSAMENTO),TO_DATE('01/01/1900','DD/MM/YYYY')) " +
									   "FROM TBL_EXT_DW_PARAMETRO_CARGA " +
									  "WHERE NOM_TABELA  = 'TBL_INT_DW_EVENTO' " +
										"AND IDT_SISTEMA = 'GPP' " +
									") " +
       "AND E.DAT_APROVISIONAMENTO < to_date(?, 'dd/mm/yyyy hh24:mi:ss') " +
	   "AND NOT EXISTS (SELECT 1 " +
						 "FROM TBL_INT_DW_EVENTO D " +
						"WHERE D.DAT_GERACAO_REGISTRO = TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
						  "AND D.IDT_MSISDN           = E.IDT_MSISDN " +
						  "AND D.DAT_APROVISIONAMENTO = E.DAT_APROVISIONAMENTO " +
					   ")";

	private final String SQLDW_RECARGA =
		"INSERT /*APPEND*/ INTO TBL_INT_DW_RECARGA " +
		"(DAT_GERACAO_REGISTRO " +
		",ID_RECARGA " +
		",DAT_RECARGA " +
		",IDT_MSISDN " +
		",TIP_TRANSACAO " +
		",ID_TIPO_CREDITO " +
		",ID_VALOR " +
		",NOM_OPERADOR " +
		",ID_TIPO_RECARGA " +
		",IDT_CPF " +
		",NUM_HASH_CC " +
		",IDT_LOJA " +
		",ID_ORIGEM " +
		",ID_SISTEMA_ORIGEM " +
		",ID_CANAL " +
		",VLR_PAGO " +
		",VLR_CREDITO_PRINCIPAL " +
		",VLR_CREDITO_BONUS " +
		",VLR_CREDITO_SMS " +
		",VLR_CREDITO_GPRS " +
		",VLR_SALDO_FINAL_PRINCIPAL " +
		",VLR_SALDO_FINAL_BONUS " +
		",VLR_SALDO_FINAL_SMS " +
		",VLR_SALDO_FINAL_GPRS " +
		",NUM_DIAS_EXP_PRINCIPAL " +
		",NUM_DIAS_EXP_BONUS " +
		",NUM_DIAS_EXP_SMS " +
		",NUM_DIAS_EXP_GPRS " +
		",IDT_CLASSIFICACAO_RECARGA " +
		",IDT_PLANO_PRECO " +
		") " +
		"SELECT   TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
			",ID_RECARGA " +
			",DAT_ORIGEM " +
			",IDT_MSISDN " +
			",TIP_TRANSACAO " +
			",ID_TIPO_CREDITO " +
			",ID_VALOR " +
			",NOM_OPERADOR " +
			",ID_TIPO_RECARGA " +
			",IDT_CPF " +
			",NUM_HASH_CC " +
			",IDT_LOJA " +
			",R.ID_ORIGEM " +
			",ID_SISTEMA_ORIGEM " +
			",R.ID_CANAL " +
			",VLR_PAGO " +
			",VLR_CREDITO_PRINCIPAL " +
			",VLR_CREDITO_BONUS " +
			",VLR_CREDITO_SMS " +
			",VLR_CREDITO_GPRS " +
			",VLR_SALDO_FINAL_PRINCIPAL " +
			",VLR_SALDO_FINAL_BONUS " +
			",VLR_SALDO_FINAL_SMS " +
			",VLR_SALDO_FINAL_GPRS " +
			",NUM_DIAS_EXP_PRINCIPAL " +
			",NUM_DIAS_EXP_BONUS " +
			",NUM_DIAS_EXP_SMS " +
			",NUM_DIAS_EXP_GPRS " +
			",O.IDT_CLASSIFICACAO_RECARGA " +
			",R.IDT_PLANO_PRECO " +
		  "FROM TBL_REC_RECARGAS R, " +
		       "TBL_REC_ORIGEM   O " +
		 "WHERE R.DAT_INCLUSAO >= (SELECT NVL(MAX(DAT_PROCESSAMENTO),TO_DATE('01/01/1900','DD/MM/YYYY')) " +
					  "FROM TBL_EXT_DW_PARAMETRO_CARGA " +
					 "WHERE NOM_TABELA  = 'TBL_INT_DW_RECARGA' " +
					   "AND IDT_SISTEMA = 'GPP' " +
					") " +
           "AND R.DAT_INCLUSAO < to_date(?, 'dd/mm/yyyy hh24:mi:ss') " +
		   "AND NOT EXISTS (SELECT 1 " +
				     "FROM TBL_INT_DW_RECARGA D " +
				    "WHERE D.DAT_GERACAO_REGISTRO = TRUNC(to_date(?, 'dd/mm/yyyy hh24:mi:ss')) " +
				      "AND D.ID_RECARGA           = R.ID_RECARGA " +
				      "AND D.TIP_TRANSACAO        = R.TIP_TRANSACAO " +
				  ") " +     
		   "AND O.ID_CANAL  = R.ID_CANAL " +
		   "AND O.ID_ORIGEM = R.ID_ORIGEM ";

	
	/**
	 * Metodo....: DefinicoesSQLDW
	 * Descricao.: Construtor da classe que inicia o mapeamento das tabelas nas quais
	 *             serao utilizadas para a exportacao indicando qual o comando a ser
	 *             efetuado para cada uma delas
	 *
	 */
	public DefinicoesSQLDW()
	{
		definicoesSQL = new TreeMap();
		definicoesSQL.put("TBL_INT_DW_ASSINANTE"     ,SQLDW_ASSINANTE);
		definicoesSQL.put("TBL_INT_DW_CANAL"         ,SQLDW_CANAL);
		definicoesSQL.put("TBL_INT_DW_EVENTO"        ,SQLDW_EVENTO); 
		definicoesSQL.put("TBL_INT_DW_ORIGEM"        ,SQLDW_ORIGEM);
		definicoesSQL.put("TBL_INT_DW_PLANO"         ,SQLDW_PLANO);
		definicoesSQL.put("TBL_INT_DW_RECARGA"       ,SQLDW_RECARGA);
		definicoesSQL.put("TBL_INT_DW_SISTEMA_ORIGEM",SQLDW_SISTEMA_ORIGEM);
	}
    
    /**
     * Metodo....: getParametros
     * Descricao.: Retorna um array de datas para ser usado como parametro do SQL
     */
    public String[] getParametros(String nomeTabela, String dataProcessamento)
    {
        int qdtParametros = 0;
        
        if (nomeTabela.equals("TBL_INT_DW_ASSINANTE"))     qdtParametros = 1;
        if (nomeTabela.equals("TBL_INT_DW_CANAL"))         qdtParametros = 2;
        if (nomeTabela.equals("TBL_INT_DW_EVENTO"))        qdtParametros = 3;
        if (nomeTabela.equals("TBL_INT_DW_ORIGEM"))        qdtParametros = 2;
        if (nomeTabela.equals("TBL_INT_DW_PLANO"))         qdtParametros = 2;
        if (nomeTabela.equals("TBL_INT_DW_RECARGA"))       qdtParametros = 3;
        if (nomeTabela.equals("TBL_INT_DW_SISTEMA_ORIGEM"))qdtParametros = 2;
        
        String[] params = new String[qdtParametros];
        Arrays.fill(params, 0, qdtParametros, dataProcessamento);
        
        return params;
    }
	
	/**
	 * Metodo....: getListaTabelasExportacao
	 * Descricao.: Lista as tabelas configuradas para a exportacao de dados
	 * @return String[] - Lista dos nomes das tabelas a serem exportadas 
	 */
	public String[] getListaTabelasExportacao()
	{
		return (String[])definicoesSQL.keySet().toArray(new String[0]);
	}
	
	/**
	 * Metodo....: getSQL
	 * Descricao.: Metodo que retorna o comando sql associado ao nome da tabela
	 *             que sera utilizado para a exportacao de dados
	 * @param nomeTabela - Nome da tabela desejada 
	 * @return String    - Comando SQL para exportacao dos dados
	 */
	public String getSQL(String nomeTabela)
	{
		return (String)definicoesSQL.get(nomeTabela);
	}
}
