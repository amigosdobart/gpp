package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorProPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
  *
  * Esta classe extende as funcionalidades da classe para o parse
  * do arquivo de cdr incluindo ainda mais os metodos necessarios
  * para retornar os dados necessarios para a inclusao dos dados
  * na tabela correspondente.
  * 
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				10/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class ArquivoCDRDadosVoz extends ArquivoCDR
{
	private TotalizadorProPulaPula totPulaPula = new TotalizadorProPulaPula();
	private boolean duplicaCDRChamadaOriginada = false;
	
	private static 	String SQL_INSERT_CDR = 
        "INSERT INTO TBL_GER_CDR " +
		"(SEQUENCE_NUMBER               ,SUB_ID                      ,TIMESTAMP                ,START_TIME, " +
		" CALL_DURATION                 ,TRANSACTION_TYPE            ,ACCOUNT_BALANCE_DELTA    ,CALL_ID, " +
		" FINAL_ACCOUNT_BALANCE			,PROFILE_ID                  ,ORIG_CALLED_NUMBER       ,RATE_NAME, " +
		" DISCOUNT_TYPE					,PERCENT_DISCOUNT_APPLIED    ,EXTERNAL_TRANSACTION_TYPE,OPERATOR_ID, " +
		" LOCATION_ID             		,CELL_NAME                   ,CARRIER_PREFIX		   ,AIRTIME_COST, " +
		" INTERCONNECTION_COST			,TAX                         ,DESTINATION_NAME         ,FF_DISCOUNT, " +
		" REDIRECTING_NUMBER      		,HOME_MSC_ADDRESS			 ,VISITED_MSC_ADDRESS      ,ORIG_SUB_ID, " +
		" BONUS_TYPE               		,BONUS_PERCENTAGE		     ,BONUS_AMOUNT             ,SERVICE_KEY, " +
		" PEAK_TIME						,APPLICATION_TYPE            ,BILLING_EVENT_ID         ,SERVICE_PROVIDER_ID, " +
		" MESSAGE_SIZE					,PRIMARY_MESSAGE_CONTENT_TYPE,MESSAGE_CLASS        ,RECIPIENT_ADDRESS, " +
		" RECIPIENT_TYPE				,NUM_CSP					 ,IDT_PLANO                ,TIP_CHAMADA, " +
		" TIP_DESLOCAMENTO         		,IDT_MODULACAO				 ,TIP_CDR				   ,IDT_OPERADORA_ORIGEM, " +
		" IDT_LOCALIDADE_ORIGEM    		,IDT_OPERADORA_DESTINO		 ,IDT_LOCALIDADE_DESTINO   ,ACCOUNT_STATUS, " +
		" IDT_AREA				   		,BONUS_BALANCE				 ,BONUS_BALANCE_DELTA      ,SM_BALANCE, " +
		" SM_BALANCE_DELTA         		,DATA_BALANCE			 	 ,DATA_BALANCE_DELTA       ,PERIODIC_BALANCE," +
		" PERIODIC_BALANCE_DELTA        ,CALL_TYPE_ID				 ,COST) " +
		"VALUES " +
		"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
	     "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * Metodo....:getSQLInsertGerCDR
	 * Descricao.:Este metodo retorna o comando a ser utilizado para
	 *            inclusao na tabela tbl_ger_cdr
	 */
	public String getSQLInsertGerCDR()
	{
		return SQL_INSERT_CDR;
	}
	
	/**
	 * Metodo....:defineDuplicacaoCDRChamadaOriginada
	 * Descricao.:Define um atributo da classe para indicar que o 
	 *            CDR eh uma chamada originada e que deve ser duplicada
	 *            para o processo de pula-pula. Este metodo atualiza
	 *            um atributo devido ser reaproveitado em dois metodos:
	 *            um que retorna o select (SQL) e outro que retorna os
	 *            parametros a serem incluidos
	 */
	private void defineDuplicacaoCDRChamadaOriginada()
	{
		// O default eh nao duplicar CDR na chamada originada
		duplicaCDRChamadaOriginada = false;
		// Primeiro verifica se a chamada eh uma chamada originada
		if (this.getTransactionType() == 0 || this.getTransactionType() == 2)
			if (Definicoes.IND_CHAMADAS_VOZ.equals(this.getTipCdr()))
				if (this.getSubId().matches(Definicoes.MASCARA_GSM_BRT_REGEX))
					// Verifica agora se a chamada originada foi totalizada no pula-pula
					// somente CDRS com esta caracteristica devem ser duplicados, portanto
					// realiza a pesquisa dos totalizadores de CDR para realizar esta verificacao
					if ( totPulaPula.totalizaPulaPula(this) )
						duplicaCDRChamadaOriginada = true;
	}
	
	/**
	 * Metodo....:getSQLCDRDuplicado
	 * Descricao.:Retorna o SQL para inserir a linha da chamada originada duplicada
	 *            para o processo de pula-pula
	 * @return String - SQL para incluir o CDR duplicado
	 */
	public String getSQLCDRDuplicado()
	{
		// Executa o metodo para definir se a chamada originada deve ser duplicada. 
		// Este metodo eh executado neste metodo pois a instancia deste objeto eh reaproveitada
		// em outros CDRs onde a configuracao pode ser diferente
		defineDuplicacaoCDRChamadaOriginada();
		
		// Caso seja necessario duplicar o CDR entao retorna o comando SQL para realizar esta tarefa
		if (duplicaCDRChamadaOriginada)
			return SQL_INSERT_CDR;
		
		return null;
	}
	
	public Object[] getValoresSQLInsert()
	{
		Object param[] = {new Long(getSequenceNumber())				,getSubId()		 							,new java.sql.Timestamp(getTimestamp().getTime()),					
						  new Long(getStartTime())					,new Long(getCallDuration())				,new Long(getTransactionType()),
						  new Long(getAccountBalanceDelta())		,getCallId()								,new Long(getFinalAccountBalance()),
						  new Long(getProfileId())					,getOrigCalledNumber()						,getRateName(),
						  getDiscountType()							,new Double(getPercenteDiscountApplied())	,new Long(getExternalTransactionType()),
						  new Long(getOperatorId())					,getLocationId()							,getCellName(),
						  getCarrierPrefix()						,new Long(getAirtimeCost())					,new Long(getInterconnectionCost()),
						  new Long(getTax())						,getDestinationName()						,new Long(getFfDiscount()),
						  getRedirectingNumber()					,getHomeMscAddress()						,getVisitedMscAddress(),
						  getOrigSubId()							,new Integer(getBonusType())				,new Integer(getBonusPercentage()),
						  new Long(getBonusAmount())				,new Integer(getServiceKey())				,new Integer(getPeakTime()),
						  getApplicationType()						,new Long(getBillingEventId())				,new Long(getServiceProviderId()),
						  new Long(getMessageSize())				,new Long(getPrimaryMessageContentType())	,new Long(getMessageClass()),
						  getRecipientAddress()						,new Long(getRecipientType())				,getNumCsp(),
						  getIdtPlano()								,getTipChamada()							,getTipDeslocamento(),
						  getIdtModulacao()							,getTipCdr()								,getIdtOperadoraOrigem(),
						  getIdtLocalidadeOrigem()					,getIdtOperadoraDestino()					,getIdtLocalidadeDestino(),
						  new Long(getAccountStatus())				,getIdtArea()								,new Long(getBonusBalance()),
						  new Long(getBonusBalanceDelta())			,new Long(getSmBalance())					,new Long(getSmBalanceDelta()),
						  new Long(getDataBalance())				,new Long(getDataBalanceDelta())			,new Long(getPeriodicBalance()), 
						  new Long(getPeriodicBalanceDelta())		,new Long(getCallTypeId())					,new Long(getCost())};
		
		return param;
	}

	/**
	 * Metodo....:getValoresSQLCDRDuplicado
	 * Descricao.:Define os valores que serao inseridos para o CDR duplicado
	 * @return Object[] - Valores para serem utilizados no comando SQL
	 */
	public Object[] getValoresSQLCDRDuplicado()
	{
		// Caso seja necessario duplicar o CDR entao retorna o comando SQL para realizar esta tarefa
		if (duplicaCDRChamadaOriginada)
		{
			String callId = getSubId().substring(2);
			String subId  = getCallIdFormatado();
			
			Object param[] = {new Long(0)								,subId		 								,new java.sql.Timestamp(getTimestamp().getTime()),					
							  new Long(getStartTime())					,new Long(getCallDuration())				,new Long(9),
							  new Long(0)								,callId										,new Long(0),
							  new Long(0)								,null										,"14----RLOCAL--------",
							  null										,new Double(0)								,new Long(0),
							  new Long(0)								,null										,null,
							  null										,new Long(0)								,new Long(0),
							  new Long(0)								,null										,new Long(getMotivoNaoBonificacao()),
							  null										,null										,null,
							  null										,new Integer(0)								,new Integer(0),
							  new Long(0)								,new Integer(0)								,new Integer(0),
							  null										,new Long(0)								,new Long(0),
							  new Long(0)								,new Long(0)								,new Long(0),
							  null										,new Long(0)								,"14",
							  "--"										,"RLOCAL------"								,"-",
							  "-"										,"PP"										,getIdtOperadoraDestino(),
							  getIdtLocalidadeDestino()					,getIdtOperadoraOrigem()					,getIdtLocalidadeOrigem(),
							  new Long(0)								,"--"										,new Long(0),
							  new Long(0)								,new Long(0)								,new Long(0),
							  new Long(0)								,new Long(0)								,new Long(0),
							  new Long(0)								,new Long(0)								,new Long(0)};
	
			return param;
		}
		return null;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.ArquivoCDR
	 */
	public String[] getComandosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		// Nao envia o comando se o CDR for um cdr entrante originado da tecnomen
		String[] comandos = { (!isAssinanteDescartado()&&!isCDREntranteTecnomen()) ? getSQLInsertGerCDR() : null,
							  (!isAssinanteDescartado()&&!isCDREntranteTecnomen()) ? getSQLCDRDuplicado() : null
							};
		return comandos;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.ArquivoCDR
	 */
	public Object[][] getParametrosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		Object[][] params = {(!isAssinanteDescartado()&&!isCDREntranteTecnomen()) ? getValoresSQLInsert() 		: null,
							 (!isAssinanteDescartado()&&!isCDREntranteTecnomen()) ? getValoresSQLCDRDuplicado() : null
							};
		return params;
	}
	
	/**
	 * Metodo....:isCDREntranteTecnomen
	 * Descricao.:Retorna um indicador se o CDR eh um CDR entrante e se esse eh originado
	 *            da plataforma Tecnomen.
	 * @return
	 */
	public boolean isCDREntranteTecnomen()
	{
		// Com a modificacao no processo de negocio que os CDRs entrantes nao mais serao enviados
		// pela plataforma Tecnomen entao os registros indentificados serao rejeitados
		return (getTipChamada().equals("RLOCAL------") && getSequenceNumber() != 0);
	}
}
