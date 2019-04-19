package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;
import com.brt.gpp.comum.mapeamentos.MapTipoTransacaoCDR;
import com.brt.gpp.comum.mapeamentos.MapValoresRecarga;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
  *
  * Esta classe extende as funcionalidades da classe para o parse
  * do arquivo de cdr incluindo ainda mais os metodos necessarios
  * para retornar os dados necessarios para a inclusao dos dados
  * na tabela correspondente.
  *
  * Nesta implementacao a classe busca o objeto TipoMapeamentoCDR para descobrir
  * se o valor da linha do CDR contido nesta classe define uma recarga, evento
  * e ou ajuste. Portanto dependendo da configuracao mais de um comando SQL
  * sera criado para a mesma linha do CDR sendo que os vetores (comandos SQL e
  * parametros) sempre possuem o mesmo numero de elementos.
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

public class ArquivoCDRAprRec extends ArquivoCDR
{
	private MapTipoTransacaoCDR mapTransacao;
	private MapRecOrigem		mapRecOrigem;
	//private long				idProcesso;
	private boolean				possuiFirstTimeNormal;
	private SimpleDateFormat	formatadorId;
	private String				sqlInsertRecarga = "INSERT INTO TBL_REC_RECARGAS " + 
																	"( ID_RECARGA " +
																	 ",IDT_MSISDN " +
																	 ",TIP_TRANSACAO " +
																	 ",ID_TIPO_CREDITO " +
																	 ",ID_VALOR " +
																	 ",DAT_ORIGEM " +
																	 ",NOM_OPERADOR " +
																	 ",ID_TIPO_RECARGA " +
																	 ",IDT_CPF " +
																	 ",NUM_HASH_CC " +
																	 ",IDT_LOJA " +
																	 ",DAT_INCLUSAO " +
																	 ",DAT_CONTABIL " +
																	 ",IDT_TERMINAL " +
																	 ",TIP_TERMINAL " +
																	 ",IDT_NSU_INSTITUICAO " +
																	 ",ID_ORIGEM " +
																	 ",ID_SISTEMA_ORIGEM " + 
																	 ",ID_CANAL " +
																	 ",IDT_PLANO_PRECO " +
																	 ",VLR_PAGO " +
																	 ",VLR_CREDITO_PRINCIPAL " +
																	 ",VLR_CREDITO_PERIODICO " +
																	 ",VLR_CREDITO_BONUS " +
																	 ",VLR_CREDITO_SMS " +
																	 ",VLR_CREDITO_GPRS " +
																	 ",VLR_SALDO_FINAL_PRINCIPAL " +
																	 ",VLR_SALDO_FINAL_PERIODICO " +
																	 ",VLR_SALDO_FINAL_BONUS " +
																	 ",VLR_SALDO_FINAL_SMS " +
																	 ",VLR_SALDO_FINAL_GPRS " +
																	 ",NUM_DIAS_EXP_PRINCIPAL " +
																	 ",NUM_DIAS_EXP_PERIODICO " +
																	 ",NUM_DIAS_EXP_BONUS " +
																	 ",NUM_DIAS_EXP_SMS " +
																	 ",NUM_DIAS_EXP_GPRS" +
																	 ",DAT_RECARGA) " +
																"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

/*	private String				sqlInsertAjuste = "INSERT INTO TBL_REC_RECARGAS " + 
																	"( ID_RECARGA " +
																	 ",IDT_MSISDN " +
																	 ",TIP_TRANSACAO " +
																	 ",ID_TIPO_CREDITO " +
																	 ",ID_VALOR " +
																	 ",DAT_ORIGEM " +
																	 ",NOM_OPERADOR " +
																	 ",ID_TIPO_RECARGA " +
																	 ",IDT_CPF " +
																	 ",NUM_HASH_CC " +
																	 ",IDT_LOJA " +
																	 ",DAT_INCLUSAO " +
																	 ",DAT_CONTABIL " +
																	 ",IDT_TERMINAL " +
																	 ",TIP_TERMINAL " +
																	 ",IDT_NSU_INSTITUICAO " +
																	 ",ID_ORIGEM " +
																	 ",ID_SISTEMA_ORIGEM " + 
																	 ",ID_CANAL " +
																	 ",IDT_PLANO_PRECO " +
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
																	 ",NUM_DIAS_EXP_GPRS" +
																	 ",DAT_RECARGA) " +
																"VALUES (SEQ_RECARGA_ID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/

	private String				sqlInsertEvento = "INSERT INTO TBL_APR_EVENTOS " + 
																	"(DAT_APROVISIONAMENTO " +
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
																	",COD_RETORNO) " +
																"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private String				sqlInsertPlanoHib = "INSERT INTO TBL_APR_PLANO_HIBRIDO " +
												    "(IDT_MSISDN " +
													",VLR_CRED_FATURA " +
													",VLR_SALDO_INICIAL " +
													",VLR_CRED_CARRY_OVER " +
													",DAT_ULTIMA_RECARGA_PROCESSADA " +
													",IND_DROP) " +
													"SELECT ? AS IDT_MSISDN " +
													      ",? AS VLR_CRED_FATURA " +
														  ",? AS VLR_SALDO_INICIAL " +
														  ",? AS VLR_CRED_CARRY_OVER " +
														  ",? AS DAT_ULTIMA_RECARGA_PROCESSADA " +
														  ",0 AS IND_DROP " +
											          "FROM DUAL WHERE NOT EXISTS (SELECT 1 " +
													                                "FROM TBL_APR_PLANO_HIBRIDO " +
																				   "WHERE IDT_MSISDN = ?)";
	
	/**
	 * Metodo....:ArquivoCDRAprRec
	 * Descricao.:Construtor "override" da classe
	 * @param idProcesso - Id do processo
	 */	
	public ArquivoCDRAprRec(long idProcesso) 
	{
		super();
		//this.idProcesso			= idProcesso;
		mapTransacao 			= MapTipoTransacaoCDR.getInstance();
		possuiFirstTimeNormal 	= false;
		formatadorId			= new SimpleDateFormat("yyyyMMddHHmmss");
		try
		{
			mapRecOrigem = MapRecOrigem.getInstancia();
		}
		catch(GPPInternalErrorException ie)
		{
			mapRecOrigem = null;
		}
	}

	/**
	 * Metodo....:getInsertEvento
	 * Descricao.:Este metodo retorna o comando insert na tabela de eventos
	 *            caso esta combinacao de valores do transaction_type e external
	 *            transaction_type seja mapeado como um evento
	 * @param  tTransacao 	- Tipo de transacao a ser utilizado
	 * @return String		- Comando de insert do evento ou nulo caso nao seja um evento
	 */
	private String getInsertEvento(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isEvento())
			return sqlInsertEvento;
		return null;
	}
	
	/**
	 * Metodo....:getInsertRecarga
	 * Descricao.:Este metodo retorna o comando insert na tabela de recarga
	 *            caso esta combinacao de valores do transaction_type e external
	 *            transaction_type seja mapeado como uma recarga
	 * @param  tTransacao 	- Tipo de transacao a ser utilizado
	 * @return String		- Comando de insert da recarga ou nulo caso nao seja uma recarga
	 */
	private String getInsertRecarga(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isRecarga())
			return sqlInsertRecarga;
		return null;
	}

	/**
	 * Metodo....:getInsertBonusRecarga
	 * Descricao.:Este metodo retorna o comando insert na tabela de recarga
	 *            caso esta combinacao de valores do transaction_type e external
	 *            transaction_type seja mapeado como um bonus por recarga
	 * @param  tTransacao 	- Tipo de transacao a ser utilizado
	 * @return String		- Comando de insert da recarga ou nulo caso nao seja um bonus de recarga
	 */
	private String getInsertBonusRecarga(TipoTransacaoCDR tTransacao)
	{
		// Este metodo retorna a string de Ajuste devido a semelhanca e uso da sequence
		// sendo que a string de Recarga necessariamente precisa ter um ID_RECARGA unico
		if (tTransacao.isAjuste() && getBonusType() == 1 && getBonusAmount() != 0)
			return sqlInsertRecarga;
		return null;
	}

	/**
	 * Metodo....:getInsertAjuste
	 * Descricao.:Este metodo retorna o comando insert na tabela de recarga
	 *            caso esta combinacao de valores do transaction_type e external
	 *            transaction_type seja mapeado como um ajuste
	 * @param  tTransacao 	- Tipo de transacao a ser utilizado
	 * @return String		- Comando de insert da recarga ou nulo caso nao seja um ajuste
	 */
	private String getInsertAjuste(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isAjuste() && !tTransacao.isRecarga() &&
			( (getBonusType() == 0 || (getBonusType() == 1 && getBonusAmount() == 0)) && 
			  getExternalTransactionType()!= Definicoes.GPP_OPERATOR_ID))
			return sqlInsertRecarga;
		return null;
	}
	
	/**
	 * Metodo....:getInsertBonusNaRecarga
	 * Descricao.:Este metodo retorna o comando insert na tabela de recarga
	 *            caso seja identificado que existe um bonus na recarga a ser registrado
	 *            Para isso a classe utiliza o mapeamento de valores de recarga para
	 *            comparar os valores afim de identificar estes bonus
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return String		- Comando de insert da recarga ou nulo caso nao seja um bonus na recarga
	 */
	private String getInsertBonusNaRecarga(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isRecarga())
		{
			double fatorDivisao = new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
			//double accountBalanceDelta  = new Double(getAccountBalanceDelta() ).doubleValue()/fatorDivisao;
			//double periodicBalanceDelta = new Double(getPeriodicBalanceDelta()).doubleValue()/fatorDivisao;
			//double bonusBalanceDelta	= new Double(getBonusBalanceDelta()     ).doubleValue()/fatorDivisao;
			double smBalanceDelta  		= new Double(getSmBalanceDelta()        ).doubleValue()/fatorDivisao;
			//double dataBalanceDelta  	= new Double(getDataBalanceDelta()      ).doubleValue()/fatorDivisao;
			// Busca o objeto valor de recarga correspondente. Caso o valor especificado
			// para o bonus esteja configurado como valor 0 entao nenhum bonus sera
			// inserido na tabela de recargas
			//ValoresRecarga valor = getValorRecarga(accountBalanceDelta,bonusBalanceDelta,smBalanceDelta,dataBalanceDelta);
			//if ( valor != null && (valor.getSaldoBonus()     != 0 || 
			//		               valor.getValorBonusGPRS() != 0 || 
			//					   valor.getValorBonusSMS()  != 0) )
			
			// Implementacao temporaria feita por Joao Carlos (15/02/07) para separar
			// o bonus de SMS da Recarga nao utilizando o ValoresRecarga pois esta tabela
			// jah nao consta os valores de bonus de SMS e a compatibilidade com cartoes
			// antigos deve ser mantida.
			// A solucao definitiva deve ser implementada
			if (smBalanceDelta != 0)
				return sqlInsertRecarga;
		}
		return null;
	}

	/**
	 * Metodo....:getInsertAjusteAtivacao
	 * Descricao.:Este metodo retorna o comando insert na tabela de recarga
	 *            correspondente ao ajuste que deve ser realizado para quando
	 *            o evento de troca de status de FirstTime-Normal (Ativacao) seja
	 *            encontrado
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return	String		- Comando de insert na recarga para este ajuste
	 */
	private String getInsertAjusteAtivacao(TipoTransacaoCDR tTransacao)
	{
		// Verifica se o tipo de transacao e um evento e corresponde a troca de
		// status de FirstTime User para Normal User
		if (tTransacao.isEvento() && 
				Definicoes.TIPO_APR_STATUS_NORMAL.equals  (tTransacao.getTipoOperacao()) && 
				Definicoes.TIPO_APR_STATUS_FIRSTIME.equals(tTransacao.getIdtAntigoCampo()))
		{
			return sqlInsertRecarga;
		}
		return null;
	}

	/**
	 * Metodo....:getInsertPlanoHibrido
	 * Descricao.:Este metodo retorna o comando insert a ser realizado na tabela de assinantes
	 *            de plano hibrido. Esse lancamento deve se ao fato de que na pre-ativacao de
	 *            assinantes dessa natureza o sistema nao efetiva o lancamento para a contabilidade
	 *            sendo no ato da troca de status de FirstTime-Normal que esse valor e creditado
	 *            entao nesse momento realiza tambem o insert desse assinante na tabela
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return String		- Comando de insert na tabela de plano hibrido
	 */
	private String getInsertPlanoHibrido(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isEvento() && 
				Definicoes.TIPO_APR_STATUS_NORMAL.equals  (tTransacao.getTipoOperacao()) && 
				Definicoes.TIPO_APR_STATUS_FIRSTIME.equals(tTransacao.getIdtAntigoCampo()))
		{
			if (isPlanoHibrido(getProfileId()))
				return sqlInsertPlanoHib;
		}
		return null;
	}

	/**
	 * Metodo....:getParametrosRecarga
	 * Descricao.:Este metodo retorna os parametros (valores) a serem
	 *            inseridos quando este CDR for uma recarga
	 * OBS: O metodo define as variaveis devido ao CDR possui os valores Delta multiplicados
	 *      por 100000 e entao estas variaveis servem para facilitar as buscas e calculos
	 *      utilizando tais campos
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return Object[]		- Conjunto de valores a serem inseridos na tabela
	 */
	private Object[] getParametrosRecarga(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isRecarga())
		{
			// Define as variaveis como double para calculo correto da precisao
			// ao dividir os valores pelo fator tecnomen
			double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
			double accountBalanceDelta	= new Double(getAccountBalanceDelta ()).doubleValue()/fatorDivisao;
			double periodicBalanceDelta	= new Double(getPeriodicBalanceDelta()).doubleValue()/fatorDivisao;
			double bonusBalanceDelta	= new Double(getBonusBalanceDelta   ()).doubleValue()/fatorDivisao;
			double smBalanceDelta		= new Double(getSmBalanceDelta      ()).doubleValue()/fatorDivisao;
			double dataBalanceDelta		= new Double(getDataBalanceDelta    ()).doubleValue()/fatorDivisao;
			double bonusAmount			= new Double(getBonusAmount         ()).doubleValue()/fatorDivisao;

			double finalAccountBalance	= new Double(getFinalAccountBalance()).doubleValue()/fatorDivisao;
			double finalPeriodicBalance	= new Double(getPeriodicBalance    ()).doubleValue()/fatorDivisao;
			double finalBonusBalance	= new Double(getBonusBalance       ()).doubleValue()/fatorDivisao;
			double finalSmBalance		= new Double(getSmBalance          ()).doubleValue()/fatorDivisao;
			double finalDataBalance		= new Double(getDataBalance        ()).doubleValue()/fatorDivisao;

			int		profileId	= (int)super.getProfileId(); 
			Date	timestamp	= super.getTimestamp();
			
			// Busca o elemento ValorRecarga correspondente aos saldos (valores Delta) do CDR para identificar
			// os valores de recarga. Para os valores de saldo de SMS e GPRS sao considerados que estes podem
			// possuir bonus na recarga, portanto devem ser inseridos primeiramente somente os valores que 
			// modificaram os saldos e posteriormente o programa ira lancar os valores de bonus correspondente
			//ValoresRecarga valor = getValorRecarga(accountBalanceDelta,bonusBalanceDelta,smBalanceDelta,dataBalanceDelta);
			ValoresRecarga valor = getValorRecarga(accountBalanceDelta,0,0,0,0,profileId,timestamp);
			
			accountBalanceDelta  = accountBalanceDelta - bonusAmount;
			finalAccountBalance  = finalAccountBalance + bonusAmount;
			finalBonusBalance	 = finalBonusBalance   - bonusBalanceDelta;
			smBalanceDelta       = valor != null ? smBalanceDelta   - valor.getValorBonusSMS()  : smBalanceDelta;
			finalSmBalance       = valor != null ? finalSmBalance   - valor.getValorBonusSMS()  : finalSmBalance;
			dataBalanceDelta	 = valor != null ? dataBalanceDelta - valor.getValorBonusGPRS() : dataBalanceDelta;
			finalDataBalance	 = valor != null ? finalDataBalance - valor.getValorBonusGPRS() : finalDataBalance;

			//String tTrans = getTipoTransacaoRecarga(tTransacao.getTipoTransacaoRecarga(),getCallId());
			Object param[] = {getCallId()											// ID_RECARGA
							 ,getSubId()											// IDT_MSISDN
							 ,tTransacao.getTipoTransacaoRecarga()					// TIP_TRANSACAO
							 ,Definicoes.TIPO_CREDITO_REAIS							// ID_TIPO_CREDITO
							 ,valor != null ? new Double(valor.getIdValor()) : null	// ID_VALOR
							 ,new Timestamp(getTimestamp().getTime())				// DAT_ORIGEM
							 ,"ImportacaoTecnomen"									// NOM_OPERADOR
							 ,Definicoes.TIPO_RECARGA								// ID_TIPO_RECARGA
							 ,null													// IDT_CPF
							 ,null													// NUM_HASH_CC 
							 ,null													// IDT_LOJA
							 ,new Timestamp(new Date().getTime())					// DAT_INCLUSAO
							 ,null													// DAT_CONTABIL
							 ,null													// IDT_TERMINAL
							 ,null													// TIP_TERMINAL
							 ,null													// IDT_NSU_INSTITUICAO
							 ,tTransacao.getTipoTransacaoRecarga().substring(2,5)	// ID_ORIGEM
							 ,Definicoes.SO_TEC										// ID_SISTEMA_ORIGEM
							 ,tTransacao.getTipoTransacaoRecarga().substring(0,2)	// ID_CANAL
							 ,String.valueOf(getProfileId())						// IDT_PLANO_PRECO
							 ,valor != null ? new Double(valor.getValorEfetivoPago()) : new Double(accountBalanceDelta) //VLR_PAGO
							 ,new Double(accountBalanceDelta)						//VLR_CREDITO_PRINCIPAL
							 ,new Double(periodicBalanceDelta)						//VLR_CREDITO_PERIODICO
							 ,null													//VLR_CREDITO_BONUS
							 
							 // Alteracao temporaria feita por Joao Carlos (15/02/07). Essa implementacao
							 // retira os valores de SMS que possam estar registrados na recarga
							 // A solucao definitiva devera ser implementada considerando uma parametrizacao
							 //,new Double(smBalanceDelta)							//VLR_CREDITO_SMS
							 ,new Double(0)											//VLR_CREDITO_SMS
							 
							 ,new Double(dataBalanceDelta)							//VLR_CREDITO_GPRS
							 ,new Double(finalAccountBalance)						//VLR_SALDO_FINAL_PRINCIPAL
							 ,new Double(finalPeriodicBalance)						//VLR_SALDO_FINAL_PERIODICO
							 ,new Double(finalBonusBalance)							//VLR_SALDO_FINAL_BONUS
							 ,new Double(finalSmBalance)							//VLR_SALDO_FINAL_SMS
							 ,new Double(finalDataBalance)							//VLR_SALDO_FINAL_GPRS
							 ,valor != null ? new Long(valor.getNumDiasExpiracaoPrincipal()) : null	//NUM_DIAS_EXP_PRINCIPAL
							 ,valor != null ? new Long(valor.getNumDiasExpiracaoPeriodico()) : null	//NUM_DIAS_EXP_PERIODICO
					 		 ,null																	//NUM_DIAS_EXP_BONUS
			 				 ,valor != null ? new Long(valor.getNumDiasExpiracaoSMS()) 		 : null	//NUM_DIAS_EXP_SMS
			 				 ,valor != null ? new Long(valor.getNumDiasExpiracaoGPRS()) 	 : null	//NUM_DIAS_EXP_GPRS
			 				 ,new Timestamp(getTimestamp().getTime())				// DAT_RECARGA		 
			 				};

			return param;
		}
		return null;
	}

	/**
	 * Metodo....:getParametrosBonusRecarga
	 * Descricao.:Este metodo retorna os parametros (valores) a serem
	 *            inseridos quando este CDR for um bonus por recarga
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return Object[]		- Conjunto de valores a serem inseridos na tabela
	 */
	private Object[] getParametrosBonusRecarga(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isAjuste() && getBonusType() == 1 && getBonusAmount() != 0)
		{
			// Define as variaveis como double para calculo correto da precisao
			// ao dividir os valores pelo fator tecnomen
			double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
			//double accountBalanceDelta  = new Double(getAccountBalanceDelta()).doubleValue()/fatorDivisao;
			double bonusAmount 			= new Double(getBonusAmount()        ).doubleValue()/fatorDivisao;

			double finalAccountBalance	= new Double(getFinalAccountBalance()).doubleValue()/fatorDivisao;
			double finalPeriodicBalance	= new Double(getPeriodicBalance()    ).doubleValue()/fatorDivisao;
			double finalBonusBalance	= new Double(getBonusBalance()       ).doubleValue()/fatorDivisao;
			double finalSmBalance		= new Double(getSmBalance()          ).doubleValue()/fatorDivisao;
			double finalDataBalance		= new Double(getDataBalance()        ).doubleValue()/fatorDivisao;

			Object param[] = {getSequenceNumber()+formatadorId.format(getTimestamp()) // ID_RECARGA
							 ,getSubId()											// IDT_MSISDN
							 ,tTransacao.getTipoTransacaoAjuste()					// TIP_TRANSACAO
							 ,Definicoes.TIPO_CREDITO_REAIS							// ID_TIPO_CREDITO
							 ,null													// ID_VALOR
							 ,new Timestamp(getTimestamp().getTime())				// DAT_ORIGEM
							 ,"ImportacaoTecnomen"									// NOM_OPERADOR
							 ,Definicoes.TIPO_AJUSTE								// ID_TIPO_RECARGA
							 ,null													// IDT_CPF
							 ,null													// NUM_HASH_CC 
							 ,null													// IDT_LOJA
							 ,new Timestamp(new Date().getTime())					// DAT_INCLUSAO
							 ,null													// DAT_CONTABIL
							 ,null													// IDT_TERMINAL
							 ,null													// TIP_TERMINAL
							 ,null													// IDT_NSU_INSTITUICAO
							 ,tTransacao.getTipoTransacaoAjuste().substring(2,5)	// ID_ORIGEM
							 ,Definicoes.SO_TEC										// ID_SISTEMA_ORIGEM
							 ,tTransacao.getTipoTransacaoAjuste().substring(0,2)	// ID_CANAL
							 ,String.valueOf(getProfileId())						// IDT_PLANO_PRECO
							 ,new Double(bonusAmount)								//VLR_PAGO
							 ,new Double(bonusAmount)								//VLR_CREDITO_PRINCIPAL
							 ,null													//VLR_CREDITO_PERIODICO
							 ,null													//VLR_CREDITO_BONUS
							 ,null							 						//VLR_CREDITO_SMS
							 ,null													//VLR_CREDITO_GPRS
							 ,new Double(finalAccountBalance)						//VLR_SALDO_FINAL_PRINCIPAL
							 ,new Double(finalPeriodicBalance)						//VLR_SALDO_FINAL_PERIODICO
							 ,new Double(finalBonusBalance)							//VLR_SALDO_FINAL_BONUS
							 ,new Double(finalSmBalance)							//VLR_SALDO_FINAL_SMS
							 ,new Double(finalDataBalance)							//VLR_SALDO_FINAL_GPRS
							 ,null													//NUM_DIAS_EXP_PRINCIPAL
							 ,null													//NUM_DIAS_EXP_PERIODICO
					 		 ,null													//NUM_DIAS_EXP_BONUS
			 				 ,null													//NUM_DIAS_EXP_SMS
			 				 ,null													//NUM_DIAS_EXP_GPRS
			 				 ,new Timestamp(getTimestamp().getTime())				//DAT_RECARGA		 
				 			};
			return param;
		}
		return null;
	}

	/**
	 * Metodo....:getParametrosAjuste
	 * Descricao.:Este metodo retorna os parametros (valores) a serem
	 *            inseridos quando este CDR for um ajuste
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return Object[]		- Conjunto de valores a serem inseridos na tabela
	 */
	private Object[] getParametrosAjuste(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isAjuste() && !tTransacao.isRecarga() &&
		    ( (getBonusType() == 0 || (getBonusType() == 1 && getBonusAmount() == 0)) && 
		      getExternalTransactionType()!= Definicoes.GPP_OPERATOR_ID))
		{
			// Define as variaveis como double para calculo correto da precisao
			// ao dividir os valores pelo fator tecnomen
			double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
			double accountBalanceDelta  = new Double(getAccountBalanceDelta() ).doubleValue()/fatorDivisao;
			double periodicBalanceDelta	= new Double(getPeriodicBalanceDelta()).doubleValue()/fatorDivisao;
			double bonusBalanceDelta	= new Double(getBonusBalanceDelta()   ).doubleValue()/fatorDivisao;
			double smBalanceDelta  		= new Double(getSmBalanceDelta()      ).doubleValue()/fatorDivisao;
			double dataBalanceDelta  	= new Double(getDataBalanceDelta()    ).doubleValue()/fatorDivisao;
			double bonusAmount 			= new Double(getBonusAmount()         ).doubleValue()/fatorDivisao;

			double finalAccountBalance	= new Double(getFinalAccountBalance()).doubleValue()/fatorDivisao;
			double finalPeriodicBalance	= new Double(getPeriodicBalance()    ).doubleValue()/fatorDivisao;
			double finalBonusBalance	= new Double(getBonusBalance()       ).doubleValue()/fatorDivisao;
			double finalSmBalance		= new Double(getSmBalance()          ).doubleValue()/fatorDivisao;
			double finalDataBalance		= new Double(getDataBalance()        ).doubleValue()/fatorDivisao;

			// Devido aos valores na tabela de recarga/ajustes quando relativo a debito, estarem com valores
			// negativos, entao o sistema temporariamente ira avaliar o tipo de transacao a ser lancado e caso
			// este seja para debito entao o valor sera negativo, senao positivo.
			int fatorMultiplicador = 1;
			if (mapRecOrigem != null && mapRecOrigem.getMapTipLancamentoRecOrigem(tTransacao.getTipoTransacaoAjuste()).equals(Definicoes.TIPO_AJUSTE_DEBITO))
				fatorMultiplicador = -1;

			// Caso o tipo de transacao do mapeamento indica que o valor do saldo final
			// deve ser lancado como valor do ajuste, entao o valor do saldo e o valor do
			// CDR. Este caso representa principalmente eventos onde o saldo final do cliente
			// foi perdido entao deve ser lancado um ajuste retirando tais creditos
			Object param[] = {getSequenceNumber()+formatadorId.format(getTimestamp()) // ID_RECARGA
							 ,getSubId()											// IDT_MSISDN
							 ,tTransacao.getTipoTransacaoAjuste()					// TIP_TRANSACAO
							 ,Definicoes.TIPO_CREDITO_REAIS							// ID_TIPO_CREDITO
							 ,null													// ID_VALOR
							 ,new Timestamp(getTimestamp().getTime())				// DAT_ORIGEM
							 ,"ImportacaoTecnomen"									// NOM_OPERADOR
							 ,Definicoes.TIPO_AJUSTE								// ID_TIPO_RECARGA
							 ,null													// IDT_CPF
							 ,null													// NUM_HASH_CC 
							 ,null													// IDT_LOJA
							 ,new Timestamp(new Date().getTime())					// DAT_INCLUSAO
							 ,null													// DAT_CONTABIL
							 ,null													// IDT_TERMINAL
							 ,null													// TIP_TERMINAL
							 ,null													// IDT_NSU_INSTITUICAO
							 ,tTransacao.getTipoTransacaoAjuste().substring(2,5)	// ID_ORIGEM
							 ,Definicoes.SO_TEC										// ID_SISTEMA_ORIGEM
							 ,tTransacao.getTipoTransacaoAjuste().substring(0,2)	// ID_CANAL
							 ,String.valueOf(getProfileId())						// IDT_PLANO_PRECO
							 ,tTransacao.inverterSaldoPrincipal() 	? new Double(Math.abs(finalAccountBalance) *fatorMultiplicador) : new Double(Math.abs(accountBalanceDelta-bonusAmount)*fatorMultiplicador)	//VLR_PAGO
							 ,tTransacao.inverterSaldoPrincipal() 	? new Double(Math.abs(finalAccountBalance) *fatorMultiplicador) : new Double(Math.abs(accountBalanceDelta-bonusAmount)*fatorMultiplicador)	//VLR_CREDITO_PRINCIPAL
							 ,tTransacao.inverterSaldoPeriodico() 	? new Double(Math.abs(finalPeriodicBalance)*fatorMultiplicador) : new Double(Math.abs(periodicBalanceDelta)           *fatorMultiplicador)	//VLR_CREDITO_PERIODICO
							 ,tTransacao.inverterSaldoBonus() 		? new Double(Math.abs(finalBonusBalance)   *fatorMultiplicador) : new Double(Math.abs(bonusBalanceDelta)              *fatorMultiplicador)	//VLR_CREDITO_BONUS
							 ,tTransacao.inverterSaldoSm() 			? new Double(Math.abs(finalSmBalance)      *fatorMultiplicador) : new Double(Math.abs(smBalanceDelta)                 *fatorMultiplicador)	//VLR_CREDITO_SMS
							 ,tTransacao.inverterSaldoDados() 		? new Double(Math.abs(finalDataBalance)    *fatorMultiplicador) : new Double(Math.abs(dataBalanceDelta)               *fatorMultiplicador)	//VLR_CREDITO_GPRS
							 ,tTransacao.inverterSaldoPrincipal() 	? new Double(accountBalanceDelta-bonusAmount) : new Double(finalAccountBalance)	//VLR_SALDO_FINAL_PRINCIPAL
							 ,tTransacao.inverterSaldoPeriodico() 	? new Double(periodicBalanceDelta)            : new Double(finalPeriodicBalance)//VLR_SALDO_FINAL_PERIODICO
							 ,tTransacao.inverterSaldoBonus() 		? new Double(bonusBalanceDelta)               : new Double(finalBonusBalance)	//VLR_SALDO_FINAL_BONUS
							 ,tTransacao.inverterSaldoSm() 			? new Double(smBalanceDelta)	              : new Double(finalSmBalance)		//VLR_SALDO_FINAL_SMS
							 ,tTransacao.inverterSaldoDados() 		? new Double(dataBalanceDelta)                : new Double(finalDataBalance)	//VLR_SALDO_FINAL_GPRS
							 ,null													//NUM_DIAS_EXP_PRINCIPAL
							 ,null													//NUM_DIAS_EXP_PERIODICO
					 		 ,null													//NUM_DIAS_EXP_BONUS
			 				 ,null													//NUM_DIAS_EXP_SMS
			 				 ,null													//NUM_DIAS_EXP_GPRS
			 				 ,new Timestamp(getTimestamp().getTime())				//DAT_RECARGA		 
				 			};
			return param;
		}
		return null;
	}

	/**
	 * Metodo....:getParametrosEvento
	 * Descricao.:Este metodo retorna os parametros (valores) a serem
	 *            inseridos quando este CDR for um evento
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return Object[]		- Conjunto de valores a serem inseridos na tabela
	 */
	private Object[] getParametrosEvento(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isEvento())
		{
			// Define as variaveis como double para calculo correto da precisao
			// ao dividir os valores pelo fator tecnomen
			double finalBalance= new Double(getFinalAccountBalance()).doubleValue();
			double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();

			Object param[] = {new java.sql.Timestamp(getTimestamp().getTime())							//DAT_APROVISIONAMENTO
							 ,getSubId()																//IDT_MSISDN
							 ,tTransacao.getTipoOperacao()												//TIP_OPERACAO
							 ,getImsi()																	//IDT_IMSI
							 ,new Long(getProfileId())													//IDT_PLANO_PRECO
							 ,getFinalAccountBalance() != 0 ? new Double(finalBalance/fatorDivisao) : null	//VLR_CREDITO_INICIAL}
							 ,null																		//IDT_IDIOMA
							 ,tTransacao.getIdtAntigoCampo()											//IDT_ANTIGO_CAMPO
							 ,tTransacao.getIdtNovoCampo()												//IDT_NOVO_CAMPO
							 ,null																		//IDT_TARIFA
							 ,null																		//DES_LISTA_FF
							 ,null																		//IDT_MOTIVO
							 ,"ImportacaoTecnomen"														//NOM_OPERADOR
							 ,"SUCESSO"																	//DES_STATUS
							 ,new Integer(0)};															//COD_RETORNO

			return param;
		}
		return null;
	}

	/**
	 * Metodo....:getParametrosBonusNaRecarga
	 * Descricao.:Este metodo retorna os parametros do comando sql a ser utilizado
	 *            para inserir bonus na recarga de uma recarga via voucher
	 * 
	 * OBS: O tipo de transacao definido para Bonus na Recarga (devido ser um procedimento temporario
	 *      ate a implementacao do multiplos saldos) entao este valor esta definido no arquivo Definicoes
	 *      do sistema nao sendo utilizado o objeto de mapeamento somente para esse caso
	 * @param tTransacao	- Tipo de Transacao a ser utilizado
	 * @return Object[] 	- Conjunto de valores a serem inseridos na tabela
	 */
	private Object[] getParametrosBonusNaRecarga(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isRecarga())
		{
			double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
			double accountBalanceDelta  = new Double(getAccountBalanceDelta ()).doubleValue()/fatorDivisao;
			double periodicBalanceDelta = new Double(getPeriodicBalanceDelta()).doubleValue()/fatorDivisao;
			double bonusBalanceDelta	= new Double(getBonusBalanceDelta   ()).doubleValue()/fatorDivisao;
			double smBalanceDelta  		= new Double(getSmBalanceDelta      ()).doubleValue()/fatorDivisao;
			double dataBalanceDelta  	= new Double(getDataBalanceDelta    ()).doubleValue()/fatorDivisao;
			//double bonusAmount 			= new Double(getBonusAmount()        ).doubleValue()/fatorDivisao;

			double finalAccountBalance	= new Double(getFinalAccountBalance()).doubleValue()/fatorDivisao;
			double finalPeriodicBalance	= new Double(getPeriodicBalance()    ).doubleValue()/fatorDivisao;
			double finalBonusBalance	= new Double(getBonusBalance()       ).doubleValue()/fatorDivisao;
			double finalSmBalance		= new Double(getSmBalance()          ).doubleValue()/fatorDivisao;
			double finalDataBalance		= new Double(getDataBalance()        ).doubleValue()/fatorDivisao;

			int		profileId	= (int)super.getProfileId();
			Date	timestamp	= super.getTimestamp();
			
			// Busca o elemento ValorRecarga correspondente aos saldos (valores Delta) do CDR para identificar
			// os valores de recarga. Para os valores de saldo de SMS e GPRS sao considerados que estes podem
			// possuir bonus na recarga, portanto devem ser inseridos primeiramente somente os valores que 
			// modificaram os saldos e posteriormente o programa ira lancar os valores de bonus correspondente
			//ValoresRecarga valor = getValorRecarga(accountBalanceDelta,bonusBalanceDelta,smBalanceDelta,dataBalanceDelta);

			// Se os valores de bonus possuirem valores maiores que zero entao os valores de bonus
			// serao inseridos
			//if ( valor != null && (valor.getSaldoBonus()     != 0 || 
		    //           			   valor.getValorBonusGPRS() != 0 || 
			//					   valor.getValorBonusSMS()  != 0) )
				
			// Implementacao temporaria feita por Joao Carlos (15/02/07) para separar
			// o bonus de SMS da Recarga nao utilizando o ValoresRecarga pois esta tabela
			// jah nao consta os valores de bonus de SMS e a compatibilidade com cartoes
			// antigos deve ser mantida.
			// A solucao definitiva deve ser implementada
			ValoresRecarga valor = this.getValorRecarga(accountBalanceDelta,0,0,0,0,profileId,timestamp);
			if (smBalanceDelta != 0)
			{
				Object param[] = {
						  getSequenceNumber()+formatadorId.format(getTimestamp()) // ID_RECARGA
						 ,getSubId()											// IDT_MSISDN
						 ,Definicoes.AJUSTE_BONUS_NA_RECARGA					// TIP_TRANSACAO
						 ,Definicoes.TIPO_CREDITO_REAIS							// ID_TIPO_CREDITO
						 ,null													// ID_VALOR
						 ,new Timestamp(getTimestamp().getTime())				// DAT_ORIGEM
						 ,"ImportacaoTecnomen"									// NOM_OPERADOR
						 ,Definicoes.TIPO_AJUSTE								// ID_TIPO_RECARGA
						 ,null													// IDT_CPF
						 ,null													// NUM_HASH_CC 
						 ,null													// IDT_LOJA
						 ,new Timestamp(new Date().getTime())					// DAT_INCLUSAO
						 ,null													// DAT_CONTABIL
						 ,null													// IDT_TERMINAL
						 ,null													// TIP_TERMINAL
						 ,null													// IDT_NSU_INSTITUICAO
						 ,Definicoes.AJUSTE_BONUS_NA_RECARGA.substring(2,5)		// ID_ORIGEM
						 ,Definicoes.SO_TEC										// ID_SISTEMA_ORIGEM
						 ,Definicoes.AJUSTE_BONUS_NA_RECARGA.substring(0,2)		// ID_CANAL
						 ,String.valueOf(getProfileId())						// IDT_PLANO_PRECO
						 ,null 													//VLR_PAGO
						 ,null													//VLR_CREDITO_PRINCIPAL
						 ,new Double(periodicBalanceDelta)						//VLR_CREDITO_PERIODICO
						 ,new Double(bonusBalanceDelta)							//VLR_CREDITO_BONUS
						 ,new Double(smBalanceDelta)							//VLR_CREDITO_SMS
						 ,new Double(dataBalanceDelta)							//VLR_CREDITO_GPRS
						 ,new Double(finalAccountBalance)						//VLR_SALDO_FINAL_PRINCIPAL
						 ,new Double(finalPeriodicBalance)						//VLR_SALDO_FINAL_PERIODICO
						 ,new Double(finalBonusBalance)							//VLR_SALDO_FINAL_BONUS
						 ,new Double(finalSmBalance)							//VLR_SALDO_FINAL_SMS
						 ,new Double(finalDataBalance)							//VLR_SALDO_FINAL_GPRS
						 ,null 													//NUM_DIAS_EXP_PRINCIPAL
				 		 ,new Long(valor.getNumDiasExpiracaoPeriodico()) 	 	//NUM_DIAS_EXP_PERIODICO
				 		 ,new Long(valor.getNumDiasExpiracaoBonus()) 	 		//NUM_DIAS_EXP_BONUS
		 				 ,new Long(valor.getNumDiasExpiracaoSMS()) 				//NUM_DIAS_EXP_SMS
		 				 ,new Long(valor.getNumDiasExpiracaoGPRS())				//NUM_DIAS_EXP_GPRS
		 				 ,new Timestamp(getTimestamp().getTime())				// DAT_RECARGA		 
			 			};
				
				return param;
			}
		}
		
		return null;
	}

	/**
	 * Metodo....:getParametrosAjusteAtivacao
	 * Descricao.:Este metodo retorna os parametros (valores) a serem
	 *            inseridos quando este CDR for um evento de troca de status de FirstTime-Normal
	 *            e entao deve ser criado um ajuste correspondente
	 * @param tTransacao	- Tipo de transacao a ser utilizado
	 * @return Object[]		- Conjunto de valores a serem inseridos na tabela
	 */
	private Object[] getParametrosAjusteAtivacao(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isEvento() && 
				Definicoes.TIPO_APR_STATUS_NORMAL.equals  (tTransacao.getTipoOperacao()) && 
				Definicoes.TIPO_APR_STATUS_FIRSTIME.equals(tTransacao.getIdtAntigoCampo()))
		{
			// Define as variaveis como double para calculo correto da precisao
			// ao dividir os valores pelo fator tecnomen. O valor da chamada fica positivo, adiante o 
			// sinal podera ser trocado dependendo do tipo de transacao
			double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
			double accountBalanceDelta  = new Double(getAccountBalanceDelta() ).doubleValue()/fatorDivisao;
			double periodicBalanceDelta = new Double(getPeriodicBalanceDelta()).doubleValue()/fatorDivisao;
			double bonusBalanceDelta	= new Double(getBonusBalanceDelta()   ).doubleValue()/fatorDivisao;
			double smBalanceDelta  		= new Double(getSmBalanceDelta()      ).doubleValue()/fatorDivisao;
			double dataBalanceDelta  	= new Double(getDataBalanceDelta()    ).doubleValue()/fatorDivisao;
			//double bonusAmount 			= new Double(getBonusAmount()        ).doubleValue()/fatorDivisao;

			double finalAccountBalance	= new Double(getFinalAccountBalance()).doubleValue()/fatorDivisao;
			double finalPeriodicBalance	= new Double(getPeriodicBalance()    ).doubleValue()/fatorDivisao;
			double finalBonusBalance	= new Double(getBonusBalance()       ).doubleValue()/fatorDivisao;
			double finalSmBalance		= new Double(getSmBalance()          ).doubleValue()/fatorDivisao;
			double finalDataBalance		= new Double(getDataBalance()        ).doubleValue()/fatorDivisao;
			
			// Devido aos valores na tabela de recarga/ajustes quando relativo a debito, estarem com valores
			// negativos, entao o sistema temporariamente ira avaliar o tipo de transacao a ser lancado e caso
			// este seja para debito entao o valor sera negativo, senao positivo.
			int fatorMultiplicador = 1;
			if (mapRecOrigem != null && mapRecOrigem.getMapTipLancamentoRecOrigem(Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL).equals(Definicoes.TIPO_AJUSTE_DEBITO))
				fatorMultiplicador = -1;

			// Os valores de credito a serem lancados deverao ser a subtracao do valor delta ao valor do saldo final
			// do assinante. No caso de valores debitados do cliente entao o valor vira negativo sendo que entao o
			// valor de credito do ajuste sera a soma do saldo final e o valor delta. No caso de valores de credito
			// (sinal positivo) entao o valor de ajuste sera a subtracao do valor delta do saldo correspondente
			double vlrCredPrincipal	= finalAccountBalance  - accountBalanceDelta;
			double vlrCredPeriodico	= finalPeriodicBalance - periodicBalanceDelta;
			double vlrCredBonus		= finalBonusBalance    - bonusBalanceDelta;
			double vlrCredSm		= finalSmBalance       - smBalanceDelta;
			double vlrCredData		= finalDataBalance     - dataBalanceDelta;

            // Retrocede 1 segundo da data do evento que gerou o credito inicial para que este seja
            // registrado primeiro para visualizacao correta no extrato de chamadas e recargas
	        Calendar datRecarga = Calendar.getInstance();
	        datRecarga.setTime(getTimestamp());
	        datRecarga.add(Calendar.SECOND,-1);

			Object param[] = {
					  getSequenceNumber()+formatadorId.format(getTimestamp())// ID_RECARGA
					 ,getSubId()											// IDT_MSISDN
					 ,Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL			// TIP_TRANSACAO
					 ,Definicoes.TIPO_CREDITO_REAIS							// ID_TIPO_CREDITO
					 ,null													// ID_VALOR
					 ,new Timestamp(datRecarga.getTimeInMillis())			// DAT_ORIGEM
					 ,"ImportacaoTecnomen"									// NOM_OPERADOR
					 ,Definicoes.TIPO_AJUSTE								// ID_TIPO_RECARGA
					 ,null													// IDT_CPF
					 ,null													// NUM_HASH_CC 
					 ,null													// IDT_LOJA
					 ,new Timestamp(new Date().getTime())					// DAT_INCLUSAO
					 ,null													// DAT_CONTABIL
					 ,null													// IDT_TERMINAL
					 ,null													// TIP_TERMINAL
					 ,null													// IDT_NSU_INSTITUICAO
					 ,Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL.substring(2,5)	// ID_ORIGEM
					 ,Definicoes.SO_TEC											// ID_SISTEMA_ORIGEM
					 ,Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL.substring(0,2)	// ID_CANAL
					 ,String.valueOf(getProfileId())							// IDT_PLANO_PRECO
					 ,new Double(Math.abs(vlrCredPrincipal)*fatorMultiplicador) //VLR_PAGO
					 ,new Double(Math.abs(vlrCredPrincipal)*fatorMultiplicador)	//VLR_CREDITO_PRINCIPAL
					 ,new Double(Math.abs(vlrCredPeriodico)*fatorMultiplicador)	//VLR_CREDITO_PERIODICO
					 ,new Double(Math.abs(vlrCredBonus)    *fatorMultiplicador)	//VLR_CREDITO_BONUS
					 ,new Double(Math.abs(vlrCredSm)       *fatorMultiplicador)	//VLR_CREDITO_SMS
					 ,new Double(Math.abs(vlrCredData)     *fatorMultiplicador)	//VLR_CREDITO_GPRS
					 ,new Double(Math.abs(vlrCredPrincipal)*fatorMultiplicador)	//VLR_SALDO_FINAL_PRINCIPAL
					 ,new Double(Math.abs(vlrCredPeriodico)*fatorMultiplicador)	//VLR_SALDO_FINAL_PERIODICO
					 ,new Double(Math.abs(vlrCredBonus)    *fatorMultiplicador)	//VLR_SALDO_FINAL_BONUS
					 ,new Double(Math.abs(vlrCredSm)       *fatorMultiplicador)	//VLR_SALDO_FINAL_SMS
					 ,new Double(Math.abs(vlrCredData)     *fatorMultiplicador)	//VLR_SALDO_FINAL_GPRS
					 ,null													//NUM_DIAS_EXP_PRINCIPAL
					 ,null													//NUM_DIAS_EXP_PERIODICO
			 		 ,null													//NUM_DIAS_EXP_BONUS
	 				 ,null													//NUM_DIAS_EXP_SMS
	 				 ,null													//NUM_DIAS_EXP_GPRS
	 				 ,new Timestamp(datRecarga.getTimeInMillis())			//DAT_RECARGA		 
		 			};

			return param;
		}
		return null;
	}

	/**
	 * Metodo....:getParametrosPlanoHibrido
	 * Descricao.:Este metodo retorna os parametros utilizados pelo insert na tabela de
	 *            plano hibridos
	 * @param tTransacao	- Tipo de transacao Utilizado
	 * @return Object[] 	- Lista de objetos a serem utilizados como parametros do comando
	 */
	private Object[] getParametrosPlanoHibrido(TipoTransacaoCDR tTransacao)
	{
		if (tTransacao.isEvento() && 
				Definicoes.TIPO_APR_STATUS_NORMAL.equals  (tTransacao.getTipoOperacao()) && 
				Definicoes.TIPO_APR_STATUS_FIRSTIME.equals(tTransacao.getIdtAntigoCampo()))
		{
			if (isPlanoHibrido(getProfileId()))
			{
				// Define as variaveis como double para calculo correto da precisao
				// ao dividir os valores pelo fator tecnomen. O valor da chamada fica positivo, adiante o 
				// sinal podera ser trocado dependendo do tipo de transacao
				double fatorDivisao= new Double(Definicoes.TECNOMEN_MULTIPLICADOR).doubleValue();
				double accountBalanceDelta  = new Double(getAccountBalanceDelta()).doubleValue()/fatorDivisao;
				//double periodicBalanceDelta = new Double(getPeriodicBalanceDelta()).doubleValue()/fatorDivisao;
				//double bonusBalanceDelta	  = new Double(getBonusBalanceDelta()   ).doubleValue()/fatorDivisao;
				//double smBalanceDelta  	  = new Double(getSmBalanceDelta()      ).doubleValue()/fatorDivisao;
				//double dataBalanceDelta  	  = new Double(getDataBalanceDelta()    ).doubleValue()/fatorDivisao;
				//double bonusAmount 		  = new Double(getBonusAmount()         ).doubleValue()/fatorDivisao;

				double finalAccountBalance	= new Double(getFinalAccountBalance()).doubleValue()/fatorDivisao;
				//double finalPeriodicBalance = new Double(getPeriodicBalance()  ).doubleValue()/fatorDivisao;
				//double finalBonusBalance	  = new Double(getBonusBalance()     ).doubleValue()/fatorDivisao;
				//double finalSmBalance		  = new Double(getSmBalance()        ).doubleValue()/fatorDivisao;
				//double finalDataBalance	  = new Double(getDataBalance()      ).doubleValue()/fatorDivisao;
				
				// Devido aos valores na tabela de recarga/ajustes quando relativo a debito, estarem com valores
				// negativos, entao o sistema temporariamente ira avaliar o tipo de transacao a ser lancado e caso
				// este seja para debito entao o valor sera negativo, senao positivo.
				//int fatorMultiplicador = 1;
				//if (mapRecOrigem != null && mapRecOrigem.getMapTipLancamentoRecOrigem(Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL).equals(Definicoes.TIPO_AJUSTE_DEBITO))
					//fatorMultiplicador = -1;

				// Os valores de credito a serem lancados deverao ser a subtracao do valor delta ao valor do saldo final
				// do assinante. No caso de valores debitados do cliente entao o valor vira negativo sendo que entao o
				// valor de credito do ajuste sera a soma do saldo final e o valor delta. No caso de valores de credito
				// (sinal positivo) entao o valor de ajuste sera a subtracao do valor delta do saldo correspondente
				double vlrCredPrincipal	= finalAccountBalance - accountBalanceDelta;
				//double vlrCredPeriodico	= finalPeriodicBalance - periodicBalanceDelta;
				//double vlrCredBonus		= finalBonusBalance    - bonusBalanceDelta;
				//double vlrCredSm			= finalSmBalance       - smBalanceDelta;
				//double vlrCredData		= finalDataBalance     - dataBalanceDelta;
				
				Object param[] = {getSubId()														// IDT_MSISDN
						         ,new Double(vlrCredPrincipal)										// VLR_CRED_FATURA
								 ,new Double(vlrCredPrincipal)										// VLR_SALDO_INICIAL
								 ,null																// VLR_CRED_CARRY_OVER
								 ,new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis())	// DAT_ULTIMA_RECARGA
								 ,getSubId()															// IDT_MSISDN DO COMANDO "NOT EXISTS"
								 };
				return param;
			}
		}
		return null;
	}

	/**
	 * Metodo....:getValorRecarga
	 * Descricao.:Este metodo retorna o objeto valor de recarga correspondente
	 *            ao valor de recarga encontrado no cdr
	 * @param accountDelta	- Valor modificado no saldo principal
	 * @param bonusDelta	- Valor modificado no saldo de bonus
	 * @param smDelta		- Valor modificado no saldo de SMS
	 * @param dataDelta		- Valor modificado no saldo de GPRS
	 * @return ValoresRecarga	- Objeto contendo valores de recarga correspondente a somatoria
	 *                            do saldo que seja igual ao valor da recarga do CDR
	 */
	private ValoresRecarga getValorRecarga(double	accountDelta, 
									  	   double	periodicDelta,
										   double	bonusDelta,
										   double	smDelta,
										   double	dataDelta,
										   int		profileId,
										   Date		timestamp)
	{
		ValoresRecarga valor = null;
		try
		{
			// Busca a referencia para a classe que gerencia os mapeamentos
			// de valores de recarga
			MapValoresRecarga valoresRecarga = MapValoresRecarga.getInstancia();
			// Busca o valor correspondente aos valores do saldo
			valor = valoresRecarga.getValoresRecarga(accountDelta,
													 periodicDelta,
													 bonusDelta,
													 smDelta,
													 dataDelta,
													 profileId,
													 timestamp);
		}
		catch(Exception e)
		{
			valor = null;
		}
		return valor;
	}

	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.ArquivoCDR
	 */
	public String[] getComandosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		TipoTransacaoCDR tTransacao = mapTransacao.getTipoTransacao(getTransactionType(),getExternalTransactionType());
		if (tTransacao == null)
			throw new GPPInternalErrorException("Nenhum objeto de mapeamento de transacao foi encontrada para o CDR.");

		// Identifica se a transacao corresponde a um evento e se este evento e uma troca de status
		// de FirstTime User para Normal User. Caso afirmativo entao o programa ira pesquisar se o
		// assinante ja possui cadastrado um evento desses na base e ira definir uma propriedade que
		// sera utilizada para indicar se os lancamentos de evento e ajusteAtivacao deverao ser executados
		if (tTransacao.isEvento() &&
				Definicoes.TIPO_APR_STATUS_NORMAL.equals  (tTransacao.getTipoOperacao()) && 
				Definicoes.TIPO_APR_STATUS_FIRSTIME.equals(tTransacao.getIdtAntigoCampo()))
		{
			setPossuiFirstTimeNormal(prepConexao);
		}

		// Se o assinante nao for descartado entao retorna uma lista de comandos possiveis
		// para a inclusao do CDR senao retorna vazio
		if (!isAssinanteDescartado())
		{
			String comandos[] = {getInsertRecarga     (tTransacao)  ,getInsertAjuste(tTransacao),
				                 getInsertBonusRecarga(tTransacao)  ,!possuiFirstTimeNormal() ? getInsertEvento(tTransacao)         : null,
								 getInsertBonusNaRecarga(tTransacao),!possuiFirstTimeNormal() ? getInsertAjusteAtivacao(tTransacao) : null,
								 !possuiFirstTimeNormal() ? getInsertPlanoHibrido(tTransacao) : null
								};
			return comandos;
		}
		
		return new String[0];
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.ArquivoCDR
	 */
	public Object[][] getParametrosSQLInsert(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		TipoTransacaoCDR tTransacao = mapTransacao.getTipoTransacao(getTransactionType(),getExternalTransactionType());
		if (tTransacao == null)
			throw new GPPInternalErrorException("Nenhum objeto de mapeamento de transacao foi encontrada para o CDR.");

		// Se o assinante nao for descartado entao retorna uma lista de comandos possiveis
		// para a inclusao do CDR senao retorna vazio
		if (!isAssinanteDescartado())
		{
			Object parametros[][] = {getParametrosRecarga       (tTransacao)  ,getParametrosAjuste(tTransacao),
                	 				 getParametrosBonusRecarga  (tTransacao)  ,!possuiFirstTimeNormal() ? getParametrosEvento(tTransacao)         : null,
					                 getParametrosBonusNaRecarga(tTransacao)  ,!possuiFirstTimeNormal() ? getParametrosAjusteAtivacao(tTransacao) : null,
					                 !possuiFirstTimeNormal() ? getParametrosPlanoHibrido(tTransacao) : null
					                };
			return parametros;
		}
		
		return new Object[0][0];
	}
	
	/**
	 * Metodo....:possuiFirstTimeNormal
	 * Descricao.:Este metodo indica se o assinante ja possui uma troca de status de FirstTime
	 *            para Normal user utilizando uma propriedade que e definida no metodo que busca
	 *            os comandos de insert
	 * @return	boolean	- Indica se o assinante ja possui uma troca de status na 
	 *                    base de dados de firstTime - Normal
	 */
	public boolean possuiFirstTimeNormal()
	{
		return possuiFirstTimeNormal;
	}

	/**
	 * Metodo....:setPossuiFirstTimeNormal
	 * Descricao.:Este metodo pesquisa na base de dados se existe uma troca de status para o assinante
	 *            sendo o resultado armazenado em uma propriedade boleana a ser utilizada para definir
	 *            se o insert de eventos e/ou ajuste sera executado
	 * @throws GPPInternalErrorException
	 */
	public void setPossuiFirstTimeNormal(PREPConexao prepConexao) throws GPPInternalErrorException
	{
		String sql =    "SELECT 1 " +
						  "FROM TBL_APR_EVENTOS A " +
						  "WHERE IDT_MSISDN       = ? " +
						    "AND TIP_OPERACAO     = ? " +
						    "AND IDT_ANTIGO_CAMPO = ? " +
						    "AND DES_STATUS       = ? " +
						    "AND DAT_APROVISIONAMENTO > (SELECT NVL(MAX(DAT_APROVISIONAMENTO),TO_DATE('01/10/2004','DD/MM/YYYY')) " +
						                                  "FROM TBL_APR_EVENTOS B " +
						                                 "WHERE B.IDT_MSISDN   = A.IDT_MSISDN " +
						                                   "AND B.TIP_OPERACAO = ? " +
						                                   "AND B.DES_STATUS   = ?)";

		Object param[] = {getSubId()
						 ,Definicoes.TIPO_APR_STATUS_NORMAL
						 ,Definicoes.TIPO_APR_STATUS_FIRSTIME
						 ,Definicoes.TIPO_OPER_SUCESSO
						 ,Definicoes.TIPO_APR_ATIVACAO
						 ,Definicoes.TIPO_OPER_SUCESSO
						 };
		try
		{
			// Define como padrao que o assinante nao possui troca de status
			this.possuiFirstTimeNormal = false;
			// Se o objeto de conexao de banco de dados for diferente de nulo, entao executa a 
			// pesquisa, em caso contrario considera que o assinante nao possui a troca de status
			if (prepConexao != null)
			{
				ResultSet rs = prepConexao.executaPreparedQuery(sql,param,0);
				// Se o result set retornar verdadeiro entao significa que pelo menos uma linha do conjunto
				// do select foi encontrado, indicando entao que o assinante ja possui um evento de troca
				// de status FirstTime - Normal cadastrado. O valor default e falso
				this.possuiFirstTimeNormal = rs.next();
			}
		}
		catch(SQLException se)
		{
			throw new GPPInternalErrorException(se.getMessage());
		}
	}
	
	/**
	 * Metodo....:isPlanoHibrido
	 * Descricao.:Identifica se um determinado plano de preco passado como parametro e um plano
	 *            Hibrido ou nao
	 * @param profileId	- Plano de Preco a ser analisado
	 * @return boolean  - Indica se o plano e hibrido ou nao
	 */
	public boolean isPlanoHibrido(long profileId)
	{
		boolean retorno = false;
		MapPlanoPreco map = MapPlanoPreco.getInstancia();
		if ( "1".equals(map.getMapHibrido(String.valueOf(profileId))) )
			retorno = true;
		return retorno;
	}

	/**
	 * Metodo....:separaRecargaFisicaVirtual
	 * Descricao.:Identifica se a recarga eh virtual e se para das recargas fisica com um tipoTransacao diferente 
	 * @param profileId	- Plano de Preco a ser analisado
	 * @return boolean  - Indica se o plano e hibrido ou nao
	 * @throws GPPInternalErrorException 
	 */
/*	private String getTipoTransacaoRecarga(String tipoTransacaoRecarga, String numeroCartao)
	{
		String resultado = tipoTransacaoRecarga;
		PREPConexao conexaoPrep = null;
		
		String sql     =  " SELECT tip_cartao" +
						  "   FROM tbl_rec_voucher_pedido_item a, tbl_rec_voucher_pedido b" +
                          "  WHERE b.num_pedido = a.num_pedido" +
                          "	   AND a.num_caixa_lote_inicial <= ? " +
                          "	   AND a.num_caixa_lote_final   >= ?" ;
		
		Object param[] = {numeroCartao,numeroCartao};
		
		if (Definicoes.RECARGA_CARTAO_FISICO.equals(tipoTransacaoRecarga))
			try
			{
				conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
				
				ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,0);
				if (rs.next())
					if (Definicoes.TIPO_CARTAO_VIRTUAL.equals(rs.getString("tip_cartao")))
						resultado = Definicoes.RECARGA_CARTAO_VIRTUAL;	
			}
			catch(Exception e)
			{
			}
			finally
			{
				GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep, 0);
			}
			
		return resultado;
	}*/
	
}