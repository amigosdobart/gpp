package com.brt.gpp.comum.mapeamentos;

import TINC.*;
import java.util.Properties;

public class MapTVMCodRetorno
{
	private Properties codigosRetorno;
	private static MapTVMCodRetorno instancia;
	
	private MapTVMCodRetorno()
	{
		codigosRetorno = new Properties();
		populaCodigosRetorno();
	}
	
	/**
	 * Metodo....:
	 * Descricao.:
	 * @return MapTVMCodRetorno
	 */
	public static MapTVMCodRetorno getInstancia()
	{
		if (instancia == null)
			instancia = new MapTVMCodRetorno();
		
		return instancia;
	}

	/**
	 * Metodo....:getDescricao
	 * Descricao.:Retorna a descricao de um certo codigo de retorno
	 * @param codRetorno	- Codigo de retorno a ser realizado a pesquisa
	 * @return String		- Descricao associada ao codigo de retorno
	 */
	public String getDescricao(int codRetorno)
	{
		String descricao = (String)codigosRetorno.get(new Integer(codRetorno));
		if (descricao == null)
			descricao = "Unknown error message " + codRetorno;
		
		return descricao;
	}

	/**
	 * Metodo....:populaCodigosRetorno
	 * Descricao.:Popula as descricoes dos codigos de retorno da voucher management
	 *
	 */
	private void populaCodigosRetorno()
	{
		codigosRetorno.put(new Integer(0)																, "Sucesso");
		codigosRetorno.put(new Integer(1)																, "Em Andamento");
		codigosRetorno.put(new Integer(TVM_TEMPORARY_ERROR.value) 										, "Erro Temporario");
		codigosRetorno.put(new Integer(TVM_FATAL_ERROR.value) 											, "Erro Fatal");
		codigosRetorno.put(new Integer(TVM_PROGRAMMING_ERROR.value) 									, "Programming Error");
		codigosRetorno.put(new Integer(TVM_INVALID_NO_PARAMS.value) 									, "INVALID_NO_PARAMS ");
		codigosRetorno.put(new Integer(TVM_INVALID_COMMENT.value) 										, "INVALID_COMMENT ");
		codigosRetorno.put(new Integer(TVM_INVALID_STATUS.value) 										, "INVALID_STATUS ");
		codigosRetorno.put(new Integer(TVM_INVALID_EXPIRY_DATE.value) 									, "INVALID_EXPIRY_DATE ");
		codigosRetorno.put(new Integer(TVM_INVALID_TARIFF_PLAN_ID.value) 								, "INVALID_TARIFF_PLAN_ID ");
		codigosRetorno.put(new Integer(TVM_INVALID_ORIGIN_ID.value) 									, "INVALID_ORIGIN_ID.value) ");
		codigosRetorno.put(new Integer(TVM_INVALID_ORDER_NO.value) 										, "INVALID_ORDER_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_FILE_NAME.value) 									, "INVALID_FILE_NAME ");
		codigosRetorno.put(new Integer(TVM_INVALID_FILE_KEY.value) 										, "INVALID_FILE_KEY ");
		codigosRetorno.put(new Integer(TVM_INVALID_DUE_DATE.value) 										, "INVALID_DUE_DATE ");
		codigosRetorno.put(new Integer(TVM_INVALID_ORDERED_DATE.value) 									, "INVALID_ORDERED_DATE ");
		codigosRetorno.put(new Integer(TVM_INVALID_ORDERED_BY.value) 									, "INVALID_ORDERED_BY ");
		codigosRetorno.put(new Integer(TVM_INVALID_JOB_WINDOWS_START_TIME.value) 						, "INVALID_JOB_WINDOWS_START_TIME ");
		codigosRetorno.put(new Integer(TVM_INVALID_JOB_WINDOW_END_TIME.value) 							, "INVALID_JOB_WINDOW_END_TIME ");
		codigosRetorno.put(new Integer(TVM_INVALID_ITEM_NO.value) 										, "INVALID_ITEM_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_USER_LOGIN.value) 									, "INVALID_USER_LOGIN ");
		codigosRetorno.put(new Integer(TVM_INVALID_START_DATE.value) 									, "INVALID_START_DATE ");
		codigosRetorno.put(new Integer(TVM_INVALID_DAYS_DECREMENT.value) 								, "INVALID_DAYS_DECREMENT ");
		codigosRetorno.put(new Integer(TVM_INVALID_BOX_BEGIN.value) 									, "INVALID_BOX_BEGIN ");
		codigosRetorno.put(new Integer(TVM_INVALID_BOX_END.value) 										, "INVALID_BOX_END ");
		codigosRetorno.put(new Integer(TVM_INVALID_ORDER_NO_BEGIN.value) 								, "INVALID_ORDER_NO_BEGIN ");
		codigosRetorno.put(new Integer(TVM_INVALID_ORDER_NO_END.value) 									, "INVALID_ORDER_NO_END ");
		codigosRetorno.put(new Integer(TVM_INVALID_ISSUED_BY.value) 									, "INVALID_ISSUED_BY ");
		codigosRetorno.put(new Integer(TVM_INVALID_ISSUED_TO.value) 									, "INVALID_ISSUED_TO ");
		codigosRetorno.put(new Integer(TVM_INVALID_ITEM_QTY.value) 										, "INVALID_ITEM_QTY ");
		codigosRetorno.put(new Integer(TVM_INVALID_VOUCHER_TYPE.value) 									, "INVALID_VOUCHER_TYPE ");
		codigosRetorno.put(new Integer(TVM_INVALID_PER_BOX_QTY.value) 									, "INVALID_PER_BOX_QTY ");
		codigosRetorno.put(new Integer(TVM_INVALID_VOUCHER_NO.value) 									, "INVALID_VOUCHER_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_PER_BATCH_QTY.value) 								, "INVALID_PER_BATCH_QTY ");
		codigosRetorno.put(new Integer(TVM_INVALID_FACE_VALUE.value) 									, "INVALID_FACE_VALUE ");
		codigosRetorno.put(new Integer(TVM_INVALID_CURRENCY_CODE.value) 								, "INVALID_CURRENCY_CODE ");
		codigosRetorno.put(new Integer(TVM_INVALID_ART_CODE.value) 										, "INVALID_ART_CODE ");
		codigosRetorno.put(new Integer(TVM_INVALID_RECEIVED_BY.value) 									, "INVALID_RECEIVED_BY ");
		codigosRetorno.put(new Integer(TVM_INVALID_BOX_NO.value) 										, "INVALID_BOX_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_BATCH_NO.value) 										, "INVALID_BATCH_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_BATCH_BEGIN.value) 									, "INVALID_BATCH_BEGIN ");
		codigosRetorno.put(new Integer(TVM_INVALID_BATCH_END.value) 									, "INVALID_BATCH_END ");
		codigosRetorno.put(new Integer(TVM_INVALID_EXPIRED_DAYS_SINCE_TODAY.value)						, "INVALID_EXPIRED_DAYS_SINCE_TODAY ");
		codigosRetorno.put(new Integer(TVM_INVALID_USED_DAYS_SINCE_TODAY.value) 						, "INVALID_USED_DAYS_SINCE_TODAY ");
		codigosRetorno.put(new Integer(TVM_INVALID_ACCOUNT_TYPE.value) 									, "INVALID_ACCOUNT_TYPE ");
		codigosRetorno.put(new Integer(TVM_INVALID_CONTAINER_ID.value) 									, "INVALID_CONTAINER_ID ");
		codigosRetorno.put(new Integer(TVM_INVALID_PASSWORD.value) 										, "INVALID_PASSWORD ");
		codigosRetorno.put(new Integer(TVM_INVALID_START_NO.value)										, "INVALID_START_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_END_NO.value) 										, "INVALID_END_NO ");
		codigosRetorno.put(new Integer(TVM_INVALID_ITEM_TYPE.value) 									, "INVALID_ITEM_TYPE ");
		codigosRetorno.put(new Integer(TVM_INVALID_BONUS_FACE_VALUE.value)			  					, "INVALID_BONUS_FACE_VALUE ");
		codigosRetorno.put(new Integer(TVM_INVALID_BONUS_EXPIRY_DATE.value) 							, "INVALID_BONUS_EXPIRY_DATE ");
		codigosRetorno.put(new Integer(TVM_INVALID_DATA_FACE_VALUE.value) 								, "INVALID_DATA_FACE_VALUE ");
		codigosRetorno.put(new Integer(TVM_INVALID_DATA_EXPIRY_DATE.value) 								, "INVALID_DATA_EXPIRY_DATE");
		codigosRetorno.put(new Integer(TVM_INVALID_SM_FACE_VALUE.value) 								, "INVALID_SM_FACE_VALUE");
		codigosRetorno.put(new Integer(TVM_INVALID_SM_EXPIRY_DATE.value)								, "INVALID_SM_EXPIRY_DATE");
		codigosRetorno.put(new Integer(TVM_INVALID_JOB_NO.value) 										, "Invalido Numero de Job");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ERROR.value) 									, "Ordem Erro");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_DOES_NOT_EXIST.value) 						, "Ordem Nao Existente");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_ALREADY_EXISTS.value) 						, "Ordem Ja Existente");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_ALREADY_ACTIVATED.value)						, "Ordem Ja Ativada");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_CREATED.value) 								, "Ordem Criada");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_CHANGE_TO_THIS_STATUS_NOT_PERMITTED.value) 	, "Mudanca de Status da Ordem Nao Permitido");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_CANNOT_UPDATE_AFTER_CREATION.value) 			, "PRINT_ORDER_NO_CANNOT_UPDATE_AFTER_CREATION");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_MORE_ORDER_NOS.value) 						, "PRINT_ORDER_NO_MORE_ORDER_NOS");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_MORE_BOX_NOS.value) 							, "PRINT_ORDER_NO_MORE_BOX_NOS");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_MORE_BATCH_NOS.value) 						, "PRINT_ORDER_NO_MORE_BATCH_NOS");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_NO_MORE_VOUCHER_NOS.value) 						, "PRINT_ORDER_NO_MORE_VOUCHER_NOS");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_STATUS_PROHIBITS_REGENERATE.value) 				, "PRINT_ORDER_STATUS_PROHIBITS_REGENERATE");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_STATUS_PROHIBITS_DELETE.value) 					, "Status da Ordem Nao Permite Remocao");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ERROR_DURING_DELETE.value) 						, "Erro ao Remover Ordem");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_MISMATCH_OF_JOB_TO_ORDER_IN_DELETE.value) 		, "RINT_ORDER_MISMATCH_OF_JOB_TO_ORDER_IN_DELETE");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ERROR_CREATING_FILE.value) 						, "PRINT_ORDER_ERROR_CREATING_FILE");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_ERROR.value) 								, "PRINT_ORDER_ITEM_ERROR");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_NO_DOES_NOT_EXIST.value) 					, "PRINT_ORDER_ITEM_NO_DOES_NOT_EXIST");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_NO_ALREADY_EXISTS.value) 					, "PRINT_ORDER_ITEM_NO_ALREADY_EXISTS");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_ALREADY_ACTIVATED.value) 					, "PRINT_ORDER_ITEM_ALREADY_ACTIVATED");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_CHANGE_TO_THIS_STATUS_NOT_PERMITTED.value)	, "PRINT_ORDER_ITEM_CHANGE_TO_THIS_STATUS_NOT_PERMITTED");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_ALREADY_PRINTED.value) 						, "PRINT_ORDER_ITEM_ALREADY_PRINTED");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_MISSING.value) 								, "PRINT_ORDER_ITEM_MISSING");
		codigosRetorno.put(new Integer(TVM_PRINT_ORDER_ITEM_STATUS_PROHIBITS_REGENERATE.value)			, "PRINT_ORDER_ITEM_STATUS_PROHIBITS_REGENERATE");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BOX_ERROR.value) 									, "Box Erro");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BOX_DOES_NOT_EXIST.value) 							, "Box Nao Existente");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BOX_ALREADY_EXISTS.value) 							, "Box Ja Existente");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BOX_ALREADY_ACTIVATED.value) 						, "Box Ja Ativado");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BOX_CHANGE_TO_THIS_STATUS_NOT_PERMITTED.value) 		, "Mudanca de Status do Box Nao Permitido");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BATCH_ERROR.value) 									, "Batch Erro");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BATCH_DOES_NOT_EXIST.value) 							, "Batch Nao Existente");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BATCH_ALREADY_EXISTS.value) 							, "Batch Ja Existente");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BATCH_ALREADY_ACTIVATED.value) 						, "Batch Ja Ativado");
		codigosRetorno.put(new Integer(TVM_VOUCHER_BATCH_CHANGE_TO_THIS_STATUS_NOT_PERMITTED.value) 	, "Mudanca de Status do Batch Nao Permitido");
		codigosRetorno.put(new Integer(TVM_VOUCHER_ERROR.value) 										, "Voucher Erro");
		codigosRetorno.put(new Integer(TVM_VOUCHER_NO_DOES_NOT_EXIST.value) 							, "Voucher Nao Existente");
		codigosRetorno.put(new Integer(TVM_VOUCHER_NO_ALREADY_EXISTS.value) 							, "Voucher Ja Existente");
		codigosRetorno.put(new Integer(TVM_VOUCHER_NO_ALREADY_ACTIVATED.value) 							, "Voucher Ja Ativado");
		codigosRetorno.put(new Integer(TVM_VOUCHER_NO_CHANGE_TO_THIS_STATUS_NOT_PERMITTED.value) 		, "Mudanca de Status do Voucher Nao Permitido");
		codigosRetorno.put(new Integer(TVM_VOUCHER_NO_CANNOT_UPDATE_UNTIL_ACTIVATED.value) 				, "VOUCHER_NO_CANNOT_UPDATE_UNTIL_ACTIVATED");
		codigosRetorno.put(new Integer(TVM_VOUCHER_NO_NUMBERS_REMAINING.value) 							, "VOUCHER_NO_NUMBERS_REMAINING");
		codigosRetorno.put(new Integer(TVM_STOCK_DETAILS_ERROR.value) 									, "STOCK_DETAILS_ERROR");
		codigosRetorno.put(new Integer(TVM_STOCK_DETAILS_NO_STOCK.value)								, "STOCK_DETAILS_NO_STOCK");
		codigosRetorno.put(new Integer(TVM_STOCK_SUMMARY_ERROR.value) 									, "STOCK_SUMMARY_ERROR");
		codigosRetorno.put(new Integer(TVM_STOCK_SUMMARY_NO_STOCK.value) 								, "STOCK_SUMMARY_NO_STOCK");
		codigosRetorno.put(new Integer(TVM_JOB_ERROR.value) 											, "Job Erro");
		codigosRetorno.put(new Integer(TVM_JOB_CREATE_ERROR.value) 										, "Erro ao Criar o Job");
		codigosRetorno.put(new Integer(TVM_JOB_SUBMIT_ERROR.value) 										, "JOB_SUBMIT_ERROR");
		codigosRetorno.put(new Integer(TVM_ARCHIVE_VOUCHER_ERROR.value) 								, "ARCHIVE_VOUCHER_ERROR");
		codigosRetorno.put(new Integer(TVM_CLEANUP_VOUCHER_ERROR.value) 								, "CLEANUP_VOUCHER_ERROR");
		codigosRetorno.put(new Integer(TVM_RANGE_DETAILS_ERROR.value) 									, "RANGE_DETAILS_ERROR");
	}
}
