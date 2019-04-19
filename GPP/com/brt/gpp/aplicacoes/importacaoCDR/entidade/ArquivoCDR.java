package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapAssinantesDescartar;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
  *
  * Esta classe representa os campos existentes no arquivo de CDR
  * Tanto os CDRs contendo informacoes de Dados e Voz quanto os
  * CDRs de Aprovisionamento e Recarga possuem o mesmo formato
  * sendo a diferenca somente de alguns campos serem preenchidos
  * de acordo com o tipo.
  * 
  * Obs: Esta classe e um singleton e nao deve ser interpretada
  * como um classe contendo a informacao completa do arquivo de 
  * CDR. Esta possui dados somente de uma unica linha que esta
  * sendo processada na leitura do arquivo. Varios objetos poderiam
  * significar uma performance ruim para o processo de importacao. 
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				09/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public abstract class ArquivoCDR implements ArquivoDados
{
	private static Pattern patternCallId;
	
	private long 	sequenceNumber;
	private String	subId;
	private Date	timestamp;
	private long	startTime;
	private long	callDuration;
	private int		serviceType;
	private String	imsi;
	private String	accountNumber;
	private long	transactionType;
	private long	accountBalanceDelta;
	private String	callId;
	private long	finalAccountBalance;
	private long	callOriginId;
	private long	subClass;
	private long	profileId;
	private long	finalTariffPlanId;
	private int		expiryType;
	private String	origCalledNumber;
	private String	cliOut;
	private String	rateName;
	private String	numCsp;
	private String	idtPlano;
	private String  idtArea;
	private	String	tipChamada;
	private String	tipDeslocamento;
	private String	idtModulacao;
	private String	discountType;
	private double	percenteDiscountApplied;
	private long	externalTransactionType;
	private String	tariffingMethod;
	private long	operatorId;
	private String	locationId;
	private String	cellName;
	private String	carrierPrefix;
	private int		smDeliveryMethod;
	private long	smPackageId;
	private long	airtimeCost;
	private long	interconnectionCost;
	private long	tax;
	private String	destinationName;
	private long	ffDiscount;
	private String	callReference;
	private String	additionalCallingPartyNumber;
	private String	redirectingNumber;
	private int		callingPartyCategory;
	private String	homeMscAddress;
	private String	visitedMscAddress;
	private String	origSubId;
	private int		bonusType;
	private int		bonusPercentage;
	private long	bonusAmount;
	private int		serviceKey;
	private String	correlationId;
	private double	taxPercent;
	private int		peakTime;
	private String	applicationType;
	private long	billingEventId;
	private long	serviceProviderId;
	private long	messageSize;
	private long	primaryMessageContentType;
	private long	messageClass;
	private String	recipientAddress;
	private long	recipientType;
	private int		originatorType;
	private long	originatorVasTariffClass;
	private String	idtOperadoraOrigem;
	private String	idtLocalidadeOrigem;
	private String 	idtOperadoraDestino;
	private String	idtLocalidadeDestino;
	private String	tipCdr;
	private Date	idtDataTransacaoCGW;
	private long	idtHoraTransacaoCGW;
	private long	accountStatus;
	private long	periodicBalance;
	private long	periodicBalanceDelta;
	private long	bonusBalance;
	private long	bonusBalanceDelta;
	private long	smBalance;
	private long 	smBalanceDelta;
	private long	dataBalance;
	private long	dataBalanceDelta;
	private long	callTypeId;
	private long	cost;
	private long	numberOfVouchers;
	private long	vttQueue;
	private long	vttTariffPlanId;
	private long	initialAccountBalance;
	private long	initialDataBalance;
	private Date	datImportacaoCdr;
	private long	motivoNaoBonificacao;
	
	static
	{
		patternCallId = Pattern.compile("^0?(\\d{10}).*$");
	}
	
	/**
	 * Metodo...:parse
	 * Descricao:Realiza o parse da linha passada como parametro
	 *           construindo as informacoes baseadas na ordem neste construtor
	 * @param String linhaArquivo	- String contendo a linha do arquivo de CDR a ser
	 */
	public void parse(String linhaArquivo)
	{
		// Faz a quebra da linha em varios campos do tipo String
		String campos[] = linhaArquivo.split(","); // Colocar no Definicoes.java
			
		// Realiza a definicao das informacoes na ordem abaixo. Cada campo
		// possui em seu metodo o validador da informacao. Em caso de erro
		// entao uma excecao IllegalArgumentException e retornada
		setSequenceNumber				(getValorCampo(campos,0));
		setSubId						(getValorCampo(campos,1));
		setTimestamp					(getValorCampo(campos,2));
		setStartTime					(getValorCampo(campos,3));
		setCallDuration					(getValorCampo(campos,4));
		//setServiceType				(getValorCampo(campos,5));
		//setImsi						(getValorCampo(campos,6));
		//setAccountNumber				(getValorCampo(campos,7));
		setTransactionType				(getValorCampo(campos,8));
		setAccountBalanceDelta			(getValorCampo(campos,9));
		setCallId						(getValorCampo(campos,10));
		setFinalAccountBalance			(getValorCampo(campos,11));
		//setCallOriginId				(getValorCampo(campos,12));
		//setSubClass					(getValorCampo(campos,13));
		setProfileId					(getValorCampo(campos,14));
		//setFinalTariffPlanId			(getValorCampo(campos,15));
		//setExpiryType					(getValorCampo(campos,16));
		setOrigCalledNumber				(getValorCampo(campos,17));
		//setCliOut						(getValorCampo(campos,18));
		setRateName						(getValorCampo(campos,19));
		setNumCsp						(getValorCampo(campos,20));
		setIdtPlano						(getValorCampo(campos,21));
		setIdtArea						(getValorCampo(campos,22));
		setTipChamada					(getValorCampo(campos,23));
		setTipDeslocamento				(getValorCampo(campos,24));
		setIdtModulacao					(getValorCampo(campos,25));
		setDiscountType					(getValorCampo(campos,26));
		setPercenteDiscountApplied		(getValorCampo(campos,27));
		setExternalTransactionType		(getValorCampo(campos,28));
		//setTariffingMethod			(getValorCampo(campos,29));
		setOperatorId					(getValorCampo(campos,30));
		setLocationId					(getValorCampo(campos,31));
		setCellName						(getValorCampo(campos,32));
		setCarrierPrefix				(getValorCampo(campos,33));
		//setSmDeliveryMethod			(getValorCampo(campos,34));
		//setSmPackageId				(getValorCampo(campos,35));
		setAirtimeCost					(getValorCampo(campos,36));
		setInterconnectionCost			(getValorCampo(campos,37));
		setTax							(getValorCampo(campos,38));
		setDestinationName				(getValorCampo(campos,39));
		setFfDiscount					(getValorCampo(campos,40));
		//setCallReference				(getValorCampo(campos,41));
		//setAdditionalCallingPartyNumber(getValorCampo(campos,42));
		setRedirectingNumber			(getValorCampo(campos,43));
		//setCallingPartyCategory		(getValorCampo(campos,44));
		setHomeMscAddress				(getValorCampo(campos,45));
		setVisitedMscAddress			(getValorCampo(campos,46));
		setOrigSubId					(getValorCampo(campos,47));
		setBonusType					(getValorCampo(campos,48));
		setBonusPercentage				(getValorCampo(campos,49));
		setBonusAmount					(getValorCampo(campos,50));
		setPeriodicBalance				(getValorCampo(campos,51));
		setPeriodicBalanceDelta			(getValorCampo(campos,52));
		setBonusBalance					(getValorCampo(campos,53));
		setBonusBalanceDelta			(getValorCampo(campos,54));
		setSmBalance					(getValorCampo(campos,55));
		setSmBalanceDelta				(getValorCampo(campos,56));
		setDataBalance					(getValorCampo(campos,57));
		setDataBalanceDelta				(getValorCampo(campos,58));
		setCallTypeId					(getValorCampo(campos,59));
		setServiceKey					(getValorCampo(campos,60));
		//setCorrelationId				(getValorCampo(campos,61));
		//setTaxPercent					(getValorCampo(campos,62));
		setPeakTime						(getValorCampo(campos,63));
		setApplicationType				(getValorCampo(campos,64));
		setBillingEventId				(getValorCampo(campos,65));
		setServiceProviderId			(getValorCampo(campos,66));
		setMessageSize					(getValorCampo(campos,67));
		setPrimaryMessageContentType	(getValorCampo(campos,68));
		setMessageClass					(getValorCampo(campos,69));
		setRecipientAddress				(getValorCampo(campos,70));
		setRecipientType				(getValorCampo(campos,71));
		//setOriginatorType				(getValorCampo(campos,72));
		//setOriginatorVasTariffClass	(getValorCampo(campos,73));
		setIdtOperadoraOrigem			(getValorCampo(campos,74));
		setIdtOperadoraDestino			(getValorCampo(campos,75));
		setIdtLocalidadeOrigem			(getValorCampo(campos,76));
		setIdtLocalidadeDestino			(getValorCampo(campos,77));
		setTipCdr						(getValorCampo(campos,78));
		//setIdtDataTransacaoCGW		(getValorCampo(campos,79));
		//setIdtHoraTransacaoCGW		(getValorCampo(campos,80));
		setAccountStatus				(getValorCampo(campos,81));
		setCost							(getValorCampo(campos,82));
		//setNumberOfVouchers			(getValorCampo(campos,83));
		//setVttQueue					(getValorCampo(campos,84));
		//setVttTariffPlanId			(getValorCampo(campos,85));
		//setInitialAccountBalance		(getValorCampo(campos,86));
		//setInitialDataBalance			(getValorCampo(campos,87));
		
		// Re-inicializa o motivo de nao bloqueio do CDR para 
		// a totalizacao pula-pula
		setMotivoNaoBonificacao(0);
	}

	/**
	 * Metodo....:getValorCampo
	 * Descricao.:Retorna o valor associado a posicao do vetor, porem caso a posicao desejada nao exista
	 *            na lista de valores entao o valor nulo e retornado
	 * @param valores	- Lista de valores
	 * @param posicao	- Posicao desejada do vetor
	 * @return String	- Valor no vetor da posicao informada ou nulo caso nao existir tal posicao
	 */
	private String getValorCampo(String valores[], int posicao)
	{
		// Devido o posicionamento do vetor comecar na posicao 0 entao
		// o teste e realizado como >=
		if (posicao >= valores.length)
			return null;
		else return valores[posicao];
	}
		
	/**
	 * SOMENTE USAR ESSE METODO NA TOTALIZACAO.
	 * <p>
	 * Retorna o CALL_ID com o prefixo "55" somente para os casos: <br>
	 *   a) callId = 0 + 10 numeros + qualquer coisa               <br>
	 *   b) callId = 10 numeros + qualquer coisa                    <br>
	 * Para demais casos, retorna o CALL_ID original.
	 * <p>
	 * Obs: o retorno é limitado para 12 digitos.
	 * 
	 * @return 
	 */
	public String getCallIdFormatado()
	{
		Matcher m = patternCallId.matcher(callId);
		
		if (m.matches() && m.groupCount() >= 1)
			return "55" + m.group(1);
		else
			return callId;		
	}

	/**
	 * @return
	 */
	public String getAccountNumber()
	{
		return accountNumber;
	}

	/**
	 * @return
	 */
	public String getAdditionalCallingPartyNumber()
	{
		return additionalCallingPartyNumber;
	}

	/**
	 * @return
	 */
	public long getAirtimeCost()
	{
		return airtimeCost;
	}

	/**
	 * @return
	 */
	public String getApplicationType()
	{
		return applicationType;
	}

	/**
	 * @return
	 */
	public long getBillingEventId()
	{
		return billingEventId;
	}

	/**
	 * @return
	 */
	public long getBonusAmount()
	{
		return bonusAmount;
	}

	/**
	 * @return
	 */
	public int getBonusPercentage()
	{
		return bonusPercentage;
	}

	/**
	 * @return
	 */
	public int getBonusType()
	{
		return bonusType;
	}

	/**
	 * @return
	 */
	public long getCallDuration()
	{
		return callDuration;
	}

	/**
	 * @return
	 */
	public String getCallId()
	{
		return callId;
	}

	/**
	 * @return
	 */
	public int getCallingPartyCategory()
	{
		return callingPartyCategory;
	}

	/**
	 * @return
	 */
	public long getCallOriginId()
	{
		return callOriginId;
	}

	/**
	 * @return
	 */
	public String getCallReference()
	{
		return callReference;
	}

	/**
	 * @return
	 */
	public long getAccountBalanceDelta()
	{
		return accountBalanceDelta;
	}

	/**
	 * @return
	 */
	public String getCarrierPrefix()
	{
		return carrierPrefix;
	}

	/**
	 * @return
	 */
	public String getCellName()
	{
		return cellName;
	}

	/**
	 * @return
	 */
	public String getCliOut()
	{
		return cliOut;
	}

	/**
	 * @return
	 */
	public String getCorrelationId()
	{
		return correlationId;
	}

	/**
	 * @return
	 */
	public String getDestinationName()
	{
		return destinationName;
	}

	/**
	 * @return
	 */
	public String getDiscountType()
	{
		return discountType;
	}

	/**
	 * @return
	 */
	public int getExpiryType()
	{
		return expiryType;
	}

	/**
	 * @return
	 */
	public long getExternalTransactionType()
	{
		return externalTransactionType;
	}

	/**
	 * @return
	 */
	public long getFfDiscount()
	{
		return ffDiscount;
	}

	/**
	 * @return
	 */
	public long getFinalAccountBalance()
	{
		return finalAccountBalance;
	}

	/**
	 * @return
	 */
	public long getFinalTariffPlanId()
	{
		return finalTariffPlanId;
	}

	/**
	 * @return
	 */
	public String getHomeMscAddress()
	{
		return homeMscAddress;
	}

	/**
	 * @return
	 */
	public String getIdtLocalidadeDestino()
	{
		return idtLocalidadeDestino;
	}

	/**
	 * @return
	 */
	public String getIdtLocalidadeOrigem()
	{
		return idtLocalidadeOrigem;
	}

	/**
	 * @return
	 */
	public String getIdtModulacao()
	{
		return idtModulacao;
	}

	/**
	 * @return
	 */
	public String getIdtOperadoraDestino()
	{
		return idtOperadoraDestino;
	}

	/**
	 * @return
	 */
	public String getIdtOperadoraOrigem()
	{
		return idtOperadoraOrigem;
	}

	/**
	 * @return
	 */
	public String getIdtPlano()
	{
		return idtPlano;
	}

	/**
	 * @return
	 */
	public String getIdtArea()
	{
		return idtArea;
	}

	/**
	 * @return
	 */
	public String getImsi()
	{
		return imsi;
	}

	/**
	 * @return
	 */
	public long getInterconnectionCost()
	{
		return interconnectionCost;
	}

	/**
	 * @return
	 */
	public String getLocationId()
	{
		return locationId;
	}

	/**
	 * @return
	 */
	public long getMessageClass()
	{
		return messageClass;
	}

	/**
	 * @return
	 */
	public long getMessageSize()
	{
		return messageSize;
	}

	/**
	 * @return
	 */
	public String getNumCsp()
	{
		return numCsp;
	}

	/**
	 * @return
	 */
	public long getOperatorId()
	{
		return operatorId;
	}

	/**
	 * @return
	 */
	public String getOrigCalledNumber()
	{
		return origCalledNumber;
	}

	/**
	 * @return
	 */
	public int getOriginatorType()
	{
		return originatorType;
	}

	/**
	 * @return
	 */
	public long getOriginatorVasTariffClass()
	{
		return originatorVasTariffClass;
	}

	/**
	 * @return
	 */
	public String getOrigSubId()
	{
		return origSubId;
	}

	/**
	 * @return
	 */
	public int getPeakTime()
	{
		return peakTime;
	}

	/**
	 * @return
	 */
	public double getPercenteDiscountApplied()
	{
		return percenteDiscountApplied;
	}

	/**
	 * @return
	 */
	public long getPrimaryMessageContentType()
	{
		return primaryMessageContentType;
	}

	/**
	 * @return
	 */
	public long getProfileId()
	{
		return profileId;
	}

	/**
	 * @return
	 */
	public String getRateName()
	{
		return rateName;
	}

	/**
	 * @return
	 */
	public String getRecipientAddress()
	{
		return recipientAddress;
	}

	/**
	 * @return
	 */
	public long getRecipientType()
	{
		return recipientType;
	}

	/**
	 * @return
	 */
	public String getRedirectingNumber()
	{
		return redirectingNumber;
	}

	/**
	 * @return
	 */
	public long getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * @return
	 */
	public int getServiceKey()
	{
		return serviceKey;
	}

	/**
	 * @return
	 */
	public long getServiceProviderId()
	{
		return serviceProviderId;
	}

	/**
	 * @return
	 */
	public int getServiceType()
	{
		return serviceType;
	}

	/**
	 * @return
	 */
	public int getSmDeliveryMethod()
	{
		return smDeliveryMethod;
	}

	/**
	 * @return
	 */
	public long getSmPackageId()
	{
		return smPackageId;
	}

	/**
	 * @return
	 */
	public long getStartTime()
	{
		return startTime;
	}

	/**
	 * @return
	 */
	public long getSubClass()
	{
		return subClass;
	}

	/**
	 * @return
	 */
	public String getSubId()
	{
		return subId;
	}

	/**
	 * @return
	 */
	public String getTariffingMethod()
	{
		return tariffingMethod;
	}

	/**
	 * @return
	 */
	public long getTax()
	{
		return tax;
	}

	/**
	 * @return
	 */
	public double getTaxPercent()
	{
		return taxPercent;
	}

	/**
	 * @return
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @return
	 */
	public String getTipCdr()
	{
		return tipCdr;
	}

	/**
	 * @return
	 */
	public String getTipChamada()
	{
		return tipChamada;
	}

	/**
	 * @return
	 */
	public String getTipDeslocamento()
	{
		return tipDeslocamento;
	}

	/**
	 * @return
	 */
	public long getTransactionType()
	{
		return transactionType;
	}

	/**
	 * @return
	 */
	public long getAccountStatus()
	{
		return accountStatus;
	}

	/**
	 * @return
	 */
	public Date getIdtDataTransacaoCGW()
	{
		return idtDataTransacaoCGW;
	}

	/**
	 * @return
	 */
	public long getIdtHoraTransacaoCGW()
	{
		return idtHoraTransacaoCGW;
	}

	/**
	 * @return
	 */
	public String getVisitedMscAddress()
	{
		return visitedMscAddress;
	}

	/**
	 * @return Returns the bonusBalance.
	 */
	public long getBonusBalance()
	{
		return bonusBalance;
	}
	
	/**
	 * @return Returns the bonusBalanceDelta.
	 */
	public long getBonusBalanceDelta()
	{
		return bonusBalanceDelta;
	}
	
	/**
	 * @return Returns the callTypeId.
	 */
	public long getCallTypeId()
	{
		return callTypeId;
	}
	
	/**
	 * @return Returns the cost.
	 */
	public long getCost()
	{
		return cost;
	}
	
	/**
	 * @return Returns the dataBalance.
	 */
	public long getDataBalance()
	{
		return dataBalance;
	}
	
	/**
	 * @return Returns the dataBalanceDelta.
	 */
	public long getDataBalanceDelta()
	{
		return dataBalanceDelta;
	}
	
	/**
	 * @return Returns the initialAccountBalance.
	 */
	public long getInitialAccountBalance()
	{
		return initialAccountBalance;
	}
	
	/**
	 * @return Returns the initialDataBalance.
	 */
	public long getInitialDataBalance()
	{
		return initialDataBalance;
	}
	
	/**
	 * @return Returns the numberOfVouchers.
	 */
	public long getNumberOfVouchers()
	{
		return numberOfVouchers;
	}
	
	/**
	 * @return Returns the periodicBalance.
	 */
	public long getPeriodicBalance()
	{
		return periodicBalance;
	}
	
	/**
	 * @return Returns the periodicBalanceDelta.
	 */
	public long getPeriodicBalanceDelta()
	{
		return periodicBalanceDelta;
	}
	
	/**
	 * @return Returns the smBalance.
	 */
	public long getSmBalance()
	{
		return smBalance;
	}
	
	/**
	 * @return Returns the smBalanceDelta.
	 */
	public long getSmBalanceDelta()
	{
		return smBalanceDelta;
	}
	
	/**
	 * @return Returns the vttQueue.
	 */
	public long getVttQueue()
	{
		return vttQueue;
	}
	
	/**
	 * @return Returns the vttTariffPlanId.
	 */
	public long getVttTariffPlanId()
	{
		return vttTariffPlanId;
	}
	
	/**
	 *	Retorna a data de importacao do CDR.
	 *
	 *	@return		Date					datImportacaoCdr			Data de importacao do CDR.
	 */
	public Date getDatImportacaoCdr()
	{
	    return this.datImportacaoCdr;
	}

	/**
	 * Retorna a identificacao do motivo de nao bonificacao do CDR (caso houver)
	 * 
	 * @return		Long					motivoNaoBonificacao		Identificador de motivo de nao bonificacao do CDR
	 */
	public long getMotivoNaoBonificacao()
	{
		return this.motivoNaoBonificacao;
	}
	
	/**
	 * @param string
	 */
	public void setAccountNumber(String string) 
	{
		accountNumber = string;
	}

	/**
	 * @param string
	 */
	public void setAdditionalCallingPartyNumber(String string) 
	{
		additionalCallingPartyNumber = string;
	}

	/**
	 * @param airTime
	 */
	public void setAirtimeCost(String airTime) 
	{
		airtimeCost = 0;
		if (airTime != null && !airTime.trim().equals(""))
			airtimeCost = Long.parseLong(airTime);
	}

	/**
	 * @param string
	 */
	public void setApplicationType(String string) 
	{
		applicationType = string;
	}

	/**
	 * @param billEvent
	 */
	public void setBillingEventId(String billEvent) 
	{
		billingEventId = 0;
		if (billEvent != null && !billEvent.trim().equals(""))
			billingEventId = Long.parseLong(billEvent);
	}

	/**
	 * @param bonAmount
	 */
	public void setBonusAmount(String bonAmount)
	{
		bonusAmount = 0;
		if (bonAmount != null && !bonAmount.trim().equals(""))
			bonusAmount = Long.parseLong(bonAmount);
	}

	/**
	 * @param bonPercent
	 */
	public void setBonusPercentage(String bonPercent)
	{
		bonusPercentage = 0;
		if (bonPercent != null && !bonPercent.trim().equals(""))
			bonusPercentage = Integer.parseInt(bonPercent);
	}

	/**
	 * @param bonType
	 */
	public void setBonusType(String bonType)
	{
		bonusType = 0;
		if (bonType != null && !bonType.trim().equals(""))
			bonusType = Integer.parseInt(bonType);
	}

	/**
	 * @param call
	 */
	public void setCallDuration(String call)
	{
		callDuration = 0;
		if (call != null && !call.trim().equals(""))
			callDuration = Long.parseLong(call);
	}

	/**
	 * @param string
	 */
	public void setCallId(String string) 
	{
		if (string == null || string.trim().equals(""))
			callId = "NaoInformado";//throw new IllegalArgumentException("Campo CallId nao pode ser nulo.");
		else callId = string;
	}

	/**
	 * @param callCategory
	 */
	public void setCallingPartyCategory(String callCategory)
	{
		callingPartyCategory = 0;
		if (callCategory != null && !callCategory.trim().equals(""))
			callingPartyCategory = Integer.parseInt(callCategory);
	}

	/**
	 * @param callOrigin
	 */
	public void setCallOriginId(String callOrigin) 
	{
		callOriginId = 0;
		if (callOrigin != null && !callOrigin.trim().equals(""))
			callOriginId = Long.parseLong(callOrigin);
	}

	/**
	 * @param string
	 */
	public void setCallReference(String string) 
	{
		callReference = string;
	}

	/**
	 * @param accountBalanceDelta
	 */
	public void setAccountBalanceDelta(String accountBalanceDelta) 
	{
		this.accountBalanceDelta = 0;
		if (accountBalanceDelta != null && !accountBalanceDelta.trim().equals(""))
			this.accountBalanceDelta = Long.parseLong(accountBalanceDelta);
	}

	/**
	 * @param string
	 */
	public void setCarrierPrefix(String string) 
	{
		carrierPrefix = string;
	}

	/**
	 * @param string
	 */
	public void setCellName(String string) 
	{
		cellName = string;
	}

	/**
	 * @param string
	 */
	public void setCliOut(String string) 
	{
		cliOut = string;
	}

	/**
	 * @param string
	 */
	public void setCorrelationId(String string) 
	{
		correlationId = string;
	}

	/**
	 * @param string
	 */
	public void setDestinationName(String string)
	{
		destinationName = string;
	}

	/**
	 * @param string
	 */
	public void setDiscountType(String string)
	{
		discountType = string;
	}

	/**
	 * @param expiry
	 */
	public void setExpiryType(String expiry)
	{
		expiryType = 0;
		if (expiry != null && !expiry.trim().equals(""))
			expiryType = Integer.parseInt(expiry);
	}

	/**
	 * @param extTransType
	 */
	public void setExternalTransactionType(String exTransType)
	{
		externalTransactionType = 0;
		if (exTransType != null && !exTransType.trim().equals(""))
			externalTransactionType = Long.parseLong(exTransType);
	}

	/**
	 * @param ffDis
	 */
	public void setFfDiscount(String ffDis)
	{
		ffDiscount = 0;
		if (ffDis != null && !ffDis.trim().equals(""))
			ffDiscount = Long.parseLong(ffDis);
	}

	/**
	 * @param finalAccount
	 */
	public void setFinalAccountBalance(String finalAccount)
	{
		finalAccountBalance = 0;
		if (finalAccount != null && !finalAccount.trim().equals(""))
			finalAccountBalance = Long.parseLong(finalAccount);
	}

	/**
	 * @param finalTariff
	 */
	public void setFinalTariffPlanId(String finalTariff)
	{
		finalTariffPlanId = 0;
		if (finalTariff != null && !finalTariff.trim().equals(""))
			finalTariffPlanId = Long.parseLong(finalTariff);
	}

	/**
	 * @param string
	 */
	public void setHomeMscAddress(String string)
	{
		homeMscAddress = string;
	}

	/**
	 * @param string
	 */
	public void setIdtLocalidadeDestino(String string)
	{
		idtLocalidadeDestino = string;
	}

	/**
	 * @param string
	 */
	public void setIdtLocalidadeOrigem(String string)
	{
		idtLocalidadeOrigem = string;
	}

	/**
	 * @param string
	 */
	public void setIdtModulacao(String string)
	{
		if (string == null)
			string = Definicoes.MODULACAO_DEFAULT_CDR;
		idtModulacao = string;
	}

	/**
	 * @param string
	 */
	public void setIdtOperadoraDestino(String string)
	{
		idtOperadoraDestino = string;
	}

	/**
	 * @param string
	 */
	public void setIdtOperadoraOrigem(String string)
	{
		idtOperadoraOrigem = string;
	}

	/**
	 * @param string
	 */
	public void setIdtPlano(String string)
	{
		idtPlano = string;
	}

	/**
	 * @param string
	 */
	public void setIdtArea(String string)
	{
		idtArea = string;
	}

	/**
	 * @param string
	 */
	public void setImsi(String string)
	{
		imsi = string;
	}

	/**
	 * @param conCost
	 */
	public void setInterconnectionCost(String conCost)
	{
		interconnectionCost = 0;
		if (conCost != null && !conCost.trim().equals(""))
			interconnectionCost = Long.parseLong(conCost);
	}

	/**
	 * @param string
	 */
	public void setLocationId(String string)
	{
		locationId = string;
	}

	/**
	 * @param mClass
	 */
	public void setMessageClass(String mClass)
	{
		messageClass = 0;
		if (mClass != null && !mClass.trim().equals(""))
			messageClass = Long.parseLong(mClass);
	}

	/**
	 * @param mSize
	 */
	public void setMessageSize(String mSize)
	{
		messageSize = 0;
		if (mSize != null && !mSize.trim().equals(""))
			messageSize = Long.parseLong(mSize);
	}

	/**
	 * @param string
	 */
	public void setNumCsp(String string)
	{
		numCsp = string;
	}

	/**
	 * @param opId
	 */
	public void setOperatorId(String opId)
	{
		operatorId = 0;
		if (opId != null && !opId.trim().equals(""))
			operatorId = Long.parseLong(opId);
	}

	/**
	 * @param string
	 */
	public void setOrigCalledNumber(String string)
	{
		origCalledNumber = string;
	}

	/**
	 * @param origType
	 */
	public void setOriginatorType(String origType)
	{
		originatorType = 0;
		if (origType != null && !origType.trim().equals(""))
			originatorType = Integer.parseInt(origType);
	}

	/**
	 * @param origTariff
	 */
	public void setOriginatorVasTariffClass(String origTariff)
	{
		originatorVasTariffClass = 0;
		if (origTariff != null && !origTariff.trim().equals(""))
			originatorVasTariffClass = Long.parseLong(origTariff);
	}

	/**
	 * @param string
	 */
	public void setOrigSubId(String string)
	{
		origSubId = string;
	}

	/**
	 * @param peak
	 */
	public void setPeakTime(String peak)
	{
		peakTime = 0;
		if (peak != null && !peak.trim().equals(""))
			peakTime = Integer.parseInt(peak);
	}

	/**
	 * @param percentDiscount
	 */
	public void setPercenteDiscountApplied(String percentDiscount)
	{
		percenteDiscountApplied = 0;
		if (percentDiscount != null && !percentDiscount.trim().equals(""))
			percenteDiscountApplied = Double.parseDouble(percentDiscount);
	}

	/**
	 * @param primMessage
	 */
	public void setPrimaryMessageContentType(String primMessage)
	{
		primaryMessageContentType = 0;
		if (primMessage != null && !primMessage.trim().equals(""))
			primaryMessageContentType = Long.parseLong(primMessage);
	}

	/**
	 * @param profId
	 */
	public void setProfileId(String profId)
	{
		profileId = 0;
		if (profId != null && !profId.trim().equals(""))
			profileId = Long.parseLong(profId);
	}

	/**
	 * @param string
	 */
	public void setRateName(String string)
	{
		rateName = string;
	}

	/**
	 * @param string
	 */
	public void setRecipientAddress(String string)
	{
		recipientAddress = string;
	}

	/**
	 * @param recType
	 */
	public void setRecipientType(String recType)
	{
		recipientType = 0;
		if (recType != null && !recType.trim().equals(""))
			recipientType = Long.parseLong(recType);
	}

	/**
	 * @param string
	 */
	public void setRedirectingNumber(String string)
	{
		redirectingNumber = string;
	}

	/**
	 * @param seqNumber
	 */
	public void setSequenceNumber(String seqNumber)
	{
		sequenceNumber = 0;
		if (seqNumber != null && !seqNumber.trim().equals(""))
			sequenceNumber = Long.parseLong(seqNumber);
	}

	/**
	 * @param servKey
	 */
	public void setServiceKey(String servKey)
	{
		serviceKey = 0;
		if (servKey != null && !servKey.trim().equals(""))
			serviceKey = Integer.parseInt(servKey);
	}

	/**
	 * @param servProvider
	 */
	public void setServiceProviderId(String servProvider)
	{
		serviceProviderId = 0;
		if (servProvider != null && !servProvider.trim().equals(""))
			serviceProviderId = Long.parseLong(servProvider);
	}

	/**
	 * @param servType
	 */
	public void setServiceType(String servType)
	{
		serviceType = 0;
		if (servType != null && !servType.trim().equals(""))
			serviceType = Integer.parseInt(servType);
	}

	/**
	 * @param smDelMethod
	 */
	public void setSmDeliveryMethod(String smDelMethod)
	{
		smDeliveryMethod = 0;
		if (smDelMethod != null && !smDelMethod.trim().equals(""))
			smDeliveryMethod = Integer.parseInt(smDelMethod);
	}

	/**
	 * @param smPacId
	 */
	public void setSmPackageId(String smPacId)
	{
		smPackageId = 0;
		if (smPacId != null && !smPacId.trim().equals(""))
			smPackageId = Long.parseLong(smPacId);
	}

	/**
	 * @param start
	 */
	public void setStartTime(String start) 
	{
		startTime = 0;
		if (start == null || start.trim().equals(""))
			throw new IllegalArgumentException("Campo StartTime nao pode ser nulo.");

		startTime = Long.parseLong(start);
	}

	/**
	 * @param sub
	 */
	public void setSubClass(String sub)
	{
		subClass = 0;
		if (sub != null && !sub.trim().equals(""))
			subClass = Long.parseLong(sub);
	}

	/**
	 * @param string
	 */
	public void setSubId(String string) 
	{
		if (string == null || string.trim().equals(""))
			throw new IllegalArgumentException("Campo SubId nao pode ser nulo.");

		subId = string;
	}

	/**
	 * @param string
	 */
	public void setTariffingMethod(String string)
	{
		tariffingMethod = string;
	}

	/**
	 * @param tax
	 */
	public void setTax(String tax)
	{
		this.tax = 0;
		if (tax != null && !tax.trim().equals(""))
			this.tax = Long.parseLong(tax.trim());
	}

	/**
	 * @param taxPercent
	 */
	public void setTaxPercent(String taxPercent)
	{
		this.taxPercent = 0;
		if (taxPercent != null && !taxPercent.trim().equals(""))
			this.taxPercent = Double.parseDouble(taxPercent.trim());
	}

	/**
	 * @param timeStamp
	 */
	public void setTimestamp(String timeStamp) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if (timeStamp == null || timeStamp.trim().equals(""))
			throw new IllegalArgumentException("Campo Timestamp nao pode ser nulo.");

		try
		{
			long segundosMeiaNoite = Long.parseLong(timeStamp.substring(11,timeStamp.length()).trim());
			timestamp = sdf.parse(timeStamp.substring(0,10).trim() + " " + getHoraMinutoSegundo(segundosMeiaNoite));
		}
		catch(ParseException e)
		{
			throw new IllegalArgumentException("Campo Timestamp esta no formato invalido:"+timeStamp);
		}
	}

	/**
	 *	Atribui o timestamp do CDR.
	 *
	 *	@param		Date					timestamp					Timestamp do CDR.
	 */
	public void setTimestamp(Date timestamp) 
	{
	    this.timestamp = timestamp;
	}

	private void acertaRateName(String appType)
	{
/*		long listaAppType[] = {	 111,521,522,531,532,541,542,6104,6105,6106,6107,6108,6109,6100,
								 6101,6102,6103,20001,20002,20003,20004,20005,20006,20007,20008,20009,20010,30001,
						         30002,30003,30004,30005,30006,30007,30008,30009,30010,40001,40002,40003,40004,40005,
						         40006,40007,40008,40009,40010,50001,50002,50003,50004,50005,50006,50007,50008,50009,
						         50010,50011,50012,50013,50014,50015,50016,50017,50018,50019,50020,50021,50022,50023,
						         50024,50025,50026,50027,50028,50029,50030,50031,50032,50033,50034,50040,50041,50042,
						         50043,50044,50101,50102,50103,50104,50105,50106,50107,50108,50109,50110,50111,27100,
						         27101,27102,27103,27104,27105,27106,27107,27108,27109,27110,27111,27112,27113,27114,
						         27115,27116,27117,27118,27119,27120,27121,27122,27123,27124,27125,27126,27127,27128,
						         27129,27130,27131,27132,27133,27134,27135,27136,27137,27138,27139,27140,27141,27142,
						         27143,27144,27145,27146,27147,27148,27149,27150,27151,27152,27153,27154,27155,27156,
						         27157,27158,27159,27160,27161,27162,27163,27164,27165,27166,27167,27168,27169,27170,
						         27171,27172,27173,27174,27175,27176,27177,27178,27179,27180,27181,27182,27183,27184,
						         27185,27186,27187,27188,27189,27190,27191,27192,27193,27194,27195,27196,27197,27198,
						         27199,88401,88402,88403,88404,88405,88406,88407,88408,88409,88420,88450,88460
							};*/
		long applicationType = 0;
		if (appType != null || !appType.equals(""))
		{
			applicationType = Long.parseLong(appType);
			setRateName			("------SM "+rPad(String.valueOf(applicationType),11,'-'));
			setTipChamada		("SM "+rPad(String.valueOf(applicationType),9,'-'));
			setTipDeslocamento	("-");
			setIdtModulacao		("-");
			setNumCsp			("--");
			setIdtArea			("--");
			setIdtPlano			("--");
		}

//		for (int i=0; i < listaAppType.length; i++)
//			if (applicationType == listaAppType[i])
//			{

//				break;
//			}
	}

	private String rPad(String origem, int tamanho, char caracter)
	{
		String resultado = origem.trim();
		while (resultado.length() < tamanho)
			resultado += caracter;

		return resultado;
	}

	/**
	 * @param string
	 */
	public void setTipCdr(String string)
	{
		tipCdr = string;
		if (string.equals("MT"))
			if (getRateName().length() < 20)
				acertaRateName(getApplicationType());
	}

	/**
	 * @param string
	 */
	public void setTipChamada(String string)
	{
		tipChamada = string;
	}

	/**
	 * @param string
	 */
	public void setTipDeslocamento(String string)
	{
		tipDeslocamento = string;
	}

	/**
	 * @param String transType
	 */
	public void setTransactionType(String transType)
	{
		if (transType == null || transType.trim().equals(""))
			throw new IllegalArgumentException("Campo TransactionType nao pode ser nulo.");

		if (transType != null && !transType.trim().equals(""))
			transactionType = Long.parseLong(transType);
	}

	/**
	 * @param accStatus
	 */
	public void setAccountStatus(String accStatus)
	{
		accountStatus = 0;
		if (accStatus != null && !accStatus.trim().equals(""))
			accountStatus = Long.parseLong(accStatus);
	}

	/**
	 * @param string
	 */
	public void setIdtDataTransacaoCGW(String string)
	{
		if (string != null && !string.trim().equals(""))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try
			{
				idtDataTransacaoCGW = sdf.parse(string);
			}
			catch(ParseException e)
			{
				throw new IllegalArgumentException("Campo Data de Transacao CGW esta no formato invalido:"+string);
			}
		}
		else idtDataTransacaoCGW = null;
	}

	/**
	 * @param horaCGW
	 */
	public void setIdtHoraTransacaoCGW(String horaCGW)
	{
		idtHoraTransacaoCGW = 0;
		if (horaCGW != null && !horaCGW.trim().equals(""))
			idtHoraTransacaoCGW = Long.parseLong(horaCGW);
	}

	/**
	 * @param string
	 */
	public void setVisitedMscAddress(String visitedMsc)
	{
		visitedMscAddress = null;
		if (visitedMsc != null && !visitedMsc.trim().equals(""))
			visitedMscAddress = visitedMsc;
	}
	
	/**
	 * @param bonusBalance The bonusBalance to set.
	 */
	public void setBonusBalance(String bonusBalance)
	{
		this.bonusBalance = 0;
		if (bonusBalance != null && !bonusBalance.trim().equals(""))
			this.bonusBalance = Long.parseLong(bonusBalance);
	}
	
	/**
	 * @param bonusBalanceDelta The bonusBalanceDelta to set.
	 */
	public void setBonusBalanceDelta(String bonusBalanceDelta)
	{
		this.bonusBalanceDelta = 0;
		if (bonusBalanceDelta != null && !bonusBalanceDelta.trim().equals(""))
			this.bonusBalanceDelta = Long.parseLong(bonusBalanceDelta);
	}
	
	/**
	 * @param callTypeId The callTypeId to set.
	 */
	public void setCallTypeId(String callTypeId)
	{
		this.callTypeId = 0;
		if (callTypeId != null && !callTypeId.trim().equals(""))
			this.callTypeId = Long.parseLong(callTypeId);
	}
	
	/**
	 * @param cost The cost to set.
	 */
	public void setCost(String cost)
	{
		this.cost = 0;
		if (cost != null && !cost.trim().equals(""))
			this.cost = Long.parseLong(cost);
	}
	
	/**
	 * @param dataBalance The dataBalance to set.
	 */
	public void setDataBalance(String dataBalance)
	{
		this.dataBalance = 0;
		if (dataBalance != null && !dataBalance.trim().equals(""))
			this.dataBalance = Long.parseLong(dataBalance);
	}
	
	/**
	 * @param dataBalanceDelta The dataBalanceDelta to set.
	 */
	public void setDataBalanceDelta(String dataBalanceDelta)
	{
		this.dataBalanceDelta = 0;
		if (dataBalanceDelta != null && !dataBalanceDelta.trim().equals(""))
			this.dataBalanceDelta = Long.parseLong(dataBalanceDelta);
	}

	/**
	 * @param initialAccountBalance The initialAccountBalance to set.
	 */
	public void setInitialAccountBalance(String initialAccountBalance)
	{
		this.initialAccountBalance = 0;
		if (initialAccountBalance != null && !initialAccountBalance.trim().equals(""))
			this.initialAccountBalance = Long.parseLong(initialAccountBalance);
	}

	/**
	 * @param initialDataBalance The initialDataBalance to set.
	 */
	public void setInitialDataBalance(String initialDataBalance)
	{
		this.initialDataBalance = 0;
		if (initialDataBalance != null && !initialDataBalance.trim().equals(""))
			this.initialDataBalance = Long.parseLong(initialDataBalance);
	}
	
	/**
	 * @param numberOfVouchers The numberOfVouchers to set.
	 */
	public void setNumberOfVouchers(String numberOfVouchers)
	{
		this.numberOfVouchers = 0;
		if (numberOfVouchers != null && !numberOfVouchers.trim().equals(""))
			this.numberOfVouchers = Long.parseLong(numberOfVouchers);
	}
	
	/**
	 * @param periodicBalance The periodicBalance to set.
	 */
	public void setPeriodicBalance(String periodicBalance)
	{
		this.periodicBalance = 0;
		if (periodicBalance != null && !periodicBalance.trim().equals(""))
			this.periodicBalance = Long.parseLong(periodicBalance);
	}

	/**
	 * @param periodicBalanceDelta The periodicBalanceDelta to set.
	 */
	public void setPeriodicBalanceDelta(String periodicBalanceDelta)
	{
		this.periodicBalanceDelta = 0;
		if (periodicBalanceDelta != null && !periodicBalanceDelta.trim().equals(""))
			this.periodicBalanceDelta = Long.parseLong(periodicBalanceDelta);
	}
	
	/**
	 * @param smBalance The smBalance to set.
	 */
	public void setSmBalance(String smBalance)
	{
		this.smBalance = 0;
		if (smBalance != null && !smBalance.trim().equals(""))
			this.smBalance = Long.parseLong(smBalance);
	}
	
	/**
	 * @param smBalanceDelta The smBalanceDelta to set.
	 */
	public void setSmBalanceDelta(String smBalanceDelta)
	{
		this.smBalanceDelta = 0;
		if (smBalanceDelta != null && !smBalanceDelta.trim().equals(""))
			this.smBalanceDelta = Long.parseLong(smBalanceDelta);
	}
	
	/**
	 * @param vttQueue The vttQueue to set.
	 */
	public void setVttQueue(String vttQueue)
	{
		this.vttQueue = 0;
		if (vttQueue != null && !vttQueue.trim().equals(""))
			this.vttQueue = Long.parseLong(vttQueue);
	}
	
	/**
	 * @param vttTariffPlanId The vttTariffPlanId to set.
	 */
	public void setVttTariffPlanId(String vttTariffPlanId)
	{
		this.vttTariffPlanId = 0;
		if (vttTariffPlanId != null && !vttTariffPlanId.trim().equals(""))
			this.vttTariffPlanId = Long.parseLong(vttTariffPlanId);
	}
	
	/**
	 *	Atribui a data de importacao do CDR.
	 *
	 *	@param		Date					datImportacaoCdr			Data de importacao do CDR.
	 */
	public void setDatImportacaoCdr(Date datImportacaoCdr)
	{
	    this.datImportacaoCdr = datImportacaoCdr;
	}

	/**
	 * 	Atribui o motivo de nao bonificacao do CDR caso existir
	 * 
	 * @param 		long					motivo						Motivo de nao bonificacao do CDR
	 */
	public void setMotivoNaoBonificacao(long motivo)
	{
		// Caso o CDR identifique uma chamada recebida. Este valor deve ser 
		// armazenado fisicamente no campo FF_DISCOUNT. Portanto o programa
		// trata este tipo de CDR para marcar este campo.
		this.motivoNaoBonificacao = motivo;
		if (getTransactionType() == 9 || getTransactionType() == 11)
			setFfDiscount(String.valueOf(motivo));
	}
	
	/**
	 * Metodo....:getHoraMinutoSegundo
	 * Descricao.:Retorna a hora, minuto e segundo no formato HH:mm:ss
	 *            baseando no parametro que indica o numero de segundos
	 *            desde a meia-noite
	 * @param segundosMeiaNoite	- Numero de segundos desde a meia-noite
	 * @return	String			- Hora, minuto e segundo formatados
	 */
	private String getHoraMinutoSegundo(long segundosMeiaNoite)
	{
		String hora 	= String.valueOf((int)segundosMeiaNoite/3600);
		String minuto 	= String.valueOf((int)(segundosMeiaNoite%3600)/60);
		String segundo	= String.valueOf((int)(segundosMeiaNoite%3600)%60);
		
		return hora+":"+minuto+":"+segundo;
	}

	/**
	 * Metodo....:isAssinanteDescartado
	 * Descricao.:Este metodo identifica se o CDR pertence a algum
	 *            assinante que esteja descartado de processamento
	 * @return boolean - Indica se o assinante deve ser descartado ou nao
	 */
	public boolean isAssinanteDescartado() throws GPPInternalErrorException
	{
		MapAssinantesDescartar mapAssinante = MapAssinantesDescartar.getInstance();
		return mapAssinante.existeAssinante(getSubId());
	}

	/**
	 * Metodo....:getComandosSQLInsert
	 * Descricao.:Retorna uma lista de comandos insert a serem executados por este CDR
	 * @return	String - Lista de comandos SQL a serem executados para insercao de dados
	 */
	public String[] getComandosSQLInsert() throws GPPInternalErrorException
	{
		return null;
	}
	
	/**
	 * Metodo....:getParametrosSQLInsert
	 * Descricao.:Busca os parametros dos comandos insert a serem executados
	 *            para insercao dos dados na tabela correspondente
	 * @return Object[] - Lista de parametros a ser efetivada juntamente com o
	 *                    comando SQL de Insert
	 */
	public Object[][] getParametrosSQLInsert() throws GPPInternalErrorException
	{
		return null;
	}
	
	/**
	 * Metodo....:getIdProcessoBatch
	 * Descricao.:Retorna o id do processo batch que sera registrado no historico
	 *            quando for importado arquivos deste tipo
	 * @return int - Id do processo batch para este arquivo de dados
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_IMPORTACAO_CDR;
	}

	/**
	 * Metodo....:deveTotalizarParaPromocao
	 * Descricao.:Indica se deve totalizar para a promocao as informacoes do CDR. Para isso
	 *            os metodos que indicam a promocao pula pula ou friends and family sao utilizados
	 * @return boolean - Flag indicando se deve retornar registro contendo informacoes da totalizacao
	 */
	public boolean deveTotalizarParaPromocao()
	{
		return false;
	}
	
	/**
	 * Metodo....:getDadosCDRPromocao
	 * Descricao.:Retorna o objeto contendo as informacoes relativas a totalizacao da promocao 
	 *            OBS: Nesse metodo o objeto SEMPRE retornar como valor de minutos o minuto somente
	 *                 do CDR sendo processado. A totalizacao de todos os valores eh realizado pela
	 *                 thread de importacao de dados.
	 */
	public TotalizacaoPulaPula getTotalizacaoPulaPula()
	{
		return null;
	}	
}
