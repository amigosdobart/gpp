package com.brt.gppMediadorCDR.formatoArquivos;

/*
 * Created on 06/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.*;
import java.text.*;

public class ArquivoFormatoA implements FormatoDeArquivoCDR,Comparable
{	
	private long 	sequenceNumber;
	private String 	subId;
	private Date 	timestamp;
	private long 	startTime;
	private long 	callDuration;
	private long 	serviceType;
	private String 	imsi;
	private String 	accountNumber;
	private long 	transactionType;
	private long 	accountBalanceDelta;
	private String 	callId;
	private long 	finalAccountBalance;
	private long 	callOriginId;
	private long 	subClass;
	private long 	profileId;
	private long 	finalTariffPlanId;
	private long 	expiryType;
	private String 	origCalledNumber;
	private String	cliOut;
	private String 	rateName;
	private String  rateNameCSP;
	private String  rateNamePlano;
	private String  rateNameArea;
	private String  rateNameTipo;
	private String  rateNameAd;
	private String  rateNameModulacao;
	private long 	discountType;
	private long 	percentDiscountApplied;
	private long 	externalTransactionType;
	private long 	tariffingMethod;
	private long 	operatorId;
	private String 	locationId;
	private String 	cellName;
	private String 	carrierPrefix;
	private long 	smDeliveryMethod;
	private long 	smPackageId;
	private long 	airtimeCost;
	private long 	interconnectionCost;
	private long 	tax;
	private String 	destinationName;
	private long 	ffDiscount;
	private String 	callReference;
	private String 	additionalCallingPartynumber;
	private String 	redirectingNumber;
	private long 	callingPartyCategory;
	private String 	homeMscAddress;
	private String 	visitedMscAddress;
	private String 	origSubId;
	private long 	bonusType;
	private long 	bonusPercentage;
	private long 	bonusAmount;
	private String 	tipoCDR;
	private long	periodicBalance;
	private long	periodicBalanceDelta;
	private long	bonusBalance;
	private long	bonusBalanceDelta;
	private long	smBalance;
	private long	smBalanceDelta;
	private long	dataBalance;
	private long	dataBalanceDelta;
	private long	callTypeId;
	
	private static ArquivoFormatoA instance = new ArquivoFormatoA();
	private Map 	errors;
	private String 	linhaAtual;

	public ArquivoFormatoA(){
		errors = new HashMap();
	}

	public static ArquivoFormatoA getInstance(){
		return instance;
	}
	
	public Map getParseErrors(){
		return errors;
	}

	public String getLinhaAtual(){
		return linhaAtual;  
	}

	public boolean accept(){
		// Valida se o operador é o GPP se verdadeiro portanto a linha não deve ser exportada
		if (this.getOperatorId() == 1616 && this.bonusType == 0)
			errors.put(getLinhaAtual(), "Campo OperatorId é igual a 1616(GPP)");

		// Verifica se houve erros de validação de campos ou seleção
		if (getParseErrors().size() > 0){
			System.out.println(getParseErrors());
			return false;
		}
		return true;
	}

	public char getDestinationFormat(){
		long transTypeD[] = {0, 2, 7, 9, 11, 18, 19, 21, 22, 42, 43, 46, 48, 49, 81,
							 82, 83, 84, 85, 90, 91, 92, 93, 94, 95, 97, 98, 99, 100,
							 101, 107, 108, 109, 161, 162, 163, 164, 165, 170, 171};
				
		long transTypeE[] = {0, 1, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 23, 24, 25, 26,
			                 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 44,
			                 45, 47, 50, 51, 52, 53, 54, 62, 63, 68, 69, 71, 72, 73, 74,
			                 75, 76, 77, 78, 79, 94, 95, 98, 102, 103, 104, 201, 202};

		boolean isDFormat=false;
		boolean isEFormat=false;

		for (int i=0; i < transTypeD.length; i++)
			if (this.getTransactionType() == transTypeD[i])
			{
				isDFormat=true;
				break;
			}

		for (int i=0; i < transTypeE.length; i++)
			if (this.getTransactionType() == transTypeE[i])
			{
				isEFormat=true;
				break;
			}

		if (isDFormat && isEFormat)
			return 'B'; //Ambos os formatos
		else if (isDFormat)
				return 'D';
		else if (isEFormat)
				return 'E';
			
		// Retorna um tipo inválido caso nenhum dos Transactions Types for válido
		return 'N';
	}

	public Map parse(String linha){
		errors 		= new HashMap();
		linhaAtual 	= linha;
		String campos[] = linha.split(",");
		try{
			setSequenceNumber(campos[0]);
			setSubId(campos[1]);
			setTimestamp(campos[2]);
			setStartTime(campos[3]);
			setCallDuration(campos[4]);
			setServiceType(campos[5]);
			setImsi(campos[6]);
			setAccountNumber(campos[7]);
			setTransactionType(campos[8]);
			setAccountBalanceDelta(campos[9]);
			setCallId(campos[10]);
			setFinalAccountBalance(campos[11]);
			setCallOriginId(campos[12]);
			setSubClass(campos[13]);
			setProfileId(campos[14]);
			setFinalTariffPlanId(campos[15]);
			setExpiryType(campos[16]);
			setOrigCalledNumber(campos[17]);
			setCliOut(campos[18]);
			setRateName(campos[19]);
			setDiscountType(campos[20]);
			setPercentDiscountApplied(campos[21]);
			setExternalTransactionType(campos[22]);
			setTariffingMethod(campos[23]);
			setOperatorId(campos[24]);
			setLocationId(campos[25]);
			setCellName(campos[26]);
			setCarrierPrefix(campos[27]);
			setSmDeliveryMethod(campos[28]);
			setSmPackageId(campos[29]);
			setAirtimeCost(campos[30]);
			setInterconnectionCost(campos[31]);
			setTax(campos[32]);
			setDestinationName(campos[33]);
			setFfDiscount(campos[34]);
			setCallReference(campos[35]);
			setAdditionalCallingPartynumber(campos[36]);
			setRedirectingNumber(campos[37]);
			setCallingPartyCategory(campos[38]);
			setHomeMscAddress(campos[39]);
			setVisitedMscAddress(campos[40]);
			setOrigSubId(campos[41]);
			setBonusType(campos[42]);
			setBonusPercentage(campos[43]);
			setBonusAmount(campos[44]);
			setPeriodicBalance(campos[45]);
			setPeriodicBalanceDelta(campos[46]);
			setBonusBalance(campos[47]);
			setBonusBalanceDelta(campos[48]);
			setSmBalance(campos[49]);
			setSmBalanceDelta(campos[50]);
			setDataBalance(campos[51]);
			setDataBalanceDelta(campos[52]);
			setCallTypeId(campos[53]);
		}
		catch(IllegalArgumentException ie){
			errors.put(linha,ie.getMessage());
		}
		return errors;
	}
	
	/**
	 * @return
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @return
	 */
	public String getAdditionalCallingPartynumber() {
		return additionalCallingPartynumber;
	}

	/**
	 * @return
	 */
	public long getAirtimeCost() {
		return airtimeCost;
	}

	/**
	 * @return
	 */
	public long getBonusAmount() {
		return bonusAmount;
	}

	/**
	 * @return
	 */
	public long getBonusPercentage() {
		return bonusPercentage;
	}

	/**
	 * @return
	 */
	public long getBonusType() {
		return bonusType;
	}

	/**
	 * @return
	 */
	public long getCallDuration() {
		return callDuration;
	}

	/**
	 * @return
	 */
	public String getCallId() {
		return callId;
	}

	/**
	 * @return
	 */
	public long getCallingPartyCategory() {
		return callingPartyCategory;
	}

	/**
	 * @return
	 */
	public long getCallOriginId() {
		return callOriginId;
	}

	/**
	 * @return
	 */
	public String getCallReference() {
		return callReference;
	}

	/**
	 * @return
	 */
	public long getAccountBalanceDelta() {
		return accountBalanceDelta;
	}

	/**
	 * @return
	 */
	public String getCarrierPrefix() {
		return carrierPrefix;
	}

	/**
	 * @return
	 */
	public String getCellName() {
		return cellName;
	}

	/**
	 * @return
	 */
	public String getCliOut() {
		return cliOut;
	}

	/**
	 * @return
	 */
	public String getDestinationName() {
		return destinationName;
	}

	/**
	 * @return
	 */
	public long getDiscountType() {
		return discountType;
	}

	/**
	 * @return
	 */
	public long getExpiryType() {
		return expiryType;
	}

	/**
	 * @return
	 */
	public long getExternalTransactionType() {
		return externalTransactionType;
	}

	/**
	 * @return
	 */
	public long getFfDiscount() {
		return ffDiscount;
	}

	/**
	 * @return
	 */
	public long getFinalAccountBalance() {
		return finalAccountBalance;
	}

	/**
	 * @return
	 */
	public long getFinalTariffPlanId() {
		return finalTariffPlanId;
	}

	/**
	 * @return
	 */
	public String getHomeMscAddress() {
		return homeMscAddress;
	}

	/**
	 * @return
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @return
	 */
	public long getInterconnectionCost() {
		return interconnectionCost;
	}

	/**
	 * @return
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @return
	 */
	public long getOperatorId() {
		return operatorId;
	}

	/**
	 * @return
	 */
	public String getOrigCalledNumber() {
		return origCalledNumber;
	}

	/**
	 * @return
	 */
	public String getOrigSubId() {
		return origSubId;
	}

	/**
	 * @return
	 */
	public long getPercentDiscountApplied() {
		return percentDiscountApplied;
	}

	/**
	 * @return
	 */
	public long getProfileId() {
		return profileId;
	}

	/**
	 * @return
	 */
	public String getRateName() {
		return rateName;
	}

	/**
	 * @return
	 */
	public String getRedirectingNumber() {
		return redirectingNumber;
	}

	/**
	 * @return
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @return
	 */
	public long getServiceType() {
		return serviceType;
	}

	/**
	 * @return
	 */
	public long getSmDeliveryMethod() {
		return smDeliveryMethod;
	}

	/**
	 * @return
	 */
	public long getSmPackageId() {
		return smPackageId;
	}

	/**
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	public String getStartTimeFormatado(){
		long segundosMeiaNoite = getStartTime();
		DecimalFormat df = new DecimalFormat("00");
		String hora 	= df.format(segundosMeiaNoite/3600);
		String minuto 	= df.format(segundosMeiaNoite%3600/60);
		String segundo	= df.format((segundosMeiaNoite%3600)%60);
		
		return hora+":"+minuto+":"+segundo;
	}

	/**
	 * @return
	 */
	public long getSubClass() {
		return subClass;
	}

	/**
	 * @return
	 */
	public String getSubId() {
		return subId;
	}

	/**
	 * @return
	 */
	public long getTariffingMethod() {
		return tariffingMethod;
	}

	/**
	 * @return
	 */
	public long getTax() {
		return tax;
	}

	/**
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getFormatTimestamp(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(getTimestamp());
	}

	/**
	 * @return
	 */
	public long getTransactionType() {
		return transactionType;
	}

	/**
	 * @return
	 */
	public String getVisitedMscAddress() {
		return visitedMscAddress;
	}

	public long getCost(){
		return getAccountBalanceDelta()+getDataBalanceDelta()+getSmBalanceDelta()+getBonusBalanceDelta();
	}

	/**
	 * @param string
	 */
	public void setAccountNumber(String string) {
		if (string.length() > 24)
			throw new IllegalArgumentException("Campo AccountNumber maior que 24 posições");

		accountNumber = string;
	}

	/**
	 * @param string
	 */
	public void setAdditionalCallingPartynumber(String string) {
		if (string.length() > 16)
			throw new IllegalArgumentException("Campo AdditionalCallingPartynumber maior que 16 posições");

		additionalCallingPartynumber = string;
	}

	/**
	 * @param l
	 */
	public void setAirtimeCost(String air) {
		airtimeCost = Long.parseLong(air);
	}

	/**
	 * @param l
	 */
	public void setBonusAmount(String bon) {
		bonusAmount = Long.parseLong(bon);
	}

	/**
	 * @param l
	 */
	public void setBonusPercentage(String bon) {
		bonusPercentage = Long.parseLong(bon);
	}

	/**
	 * @param l
	 */
	public void setBonusType(String bon) {
		bonusType = Long.parseLong(bon);
	}

	/**
	 * @param l
	 */
	public void setCallDuration(String call) {
		callDuration = Long.parseLong(call);
	}

	/**
	 * @param string
	 */
	public void setCallId(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo CallId maior que 30 posições");

		callId = string;
	}

	/**
	 * @param l
	 */
	public void setCallingPartyCategory(String call) {
		callingPartyCategory = Long.parseLong(call);
	}

	/**
	 * @param l
	 */
	public void setCallOriginId(String callOrig) {
		callOriginId = Long.parseLong(callOrig);
	}

	/**
	 * @param string
	 */
	public void setCallReference(String string) {
		if (string.length() > 16)
			throw new IllegalArgumentException("Campo CallReference maior que 16 posições");
			
		callReference = string;
	}

	/**
	 * @param l
	 */
	public void setAccountBalanceDelta(String l) {
		accountBalanceDelta = Long.parseLong(l);
	}

	/**
	 * @param string
	 */
	public void setCarrierPrefix(String string) {
		if (string.length() > 2)
			throw new IllegalArgumentException("Campo CarrierPrefix maior que 2 posições");
			
		carrierPrefix = string;
	}

	/**
	 * @param string
	 */
	public void setCellName(String string) {
		if (string.length() > 20)
			throw new IllegalArgumentException("Campo CellName maior que 20 posições");

		cellName = string;
	}

	/**
	 * @param string
	 */
	public void setCliOut(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo CliOut maior que 30 posições");

		cliOut = string;
	}

	/**
	 * @param string
	 */
	public void setDestinationName(String string) {
		if (string.length() > 20)
			throw new IllegalArgumentException("Campo DestinationName maior que 20 posições");

		destinationName = string;
	}

	/**
	 * @param l
	 */
	public void setDiscountType(String dis) {
		discountType = Long.parseLong(dis);
	}

	/**
	 * @param l
	 */
	public void setExpiryType(String exp) {
		expiryType = Long.parseLong(exp);
	}

	/**
	 * @param l
	 */
	public void setExternalTransactionType(String tt) {
		externalTransactionType = Long.parseLong(tt);
	}

	/**
	 * @param l
	 */
	public void setFfDiscount(String ff) {
		ffDiscount = Long.parseLong(ff);
	}

	/**
	 * @param l
	 */
	public void setFinalAccountBalance(String fbal) {
		finalAccountBalance = Long.parseLong(fbal);
	}

	/**
	 * @param l
	 */
	public void setFinalTariffPlanId(String tariff) {
		finalTariffPlanId = Long.parseLong(tariff);
	}

	/**
	 * @param string
	 */
	public void setHomeMscAddress(String string) {
		if (string.length() > 16)
			throw new IllegalArgumentException("Campo HomeMSCAddress maior que 16 posições");

		homeMscAddress = string;
	}

	/**
	 * @param string
	 */
	public void setImsi(String string) {
		if (string == null || string.equals(""))
			throw new IllegalArgumentException("Campo Imsi nulo");
			
		if (string.length() > 24)
			throw new IllegalArgumentException("Campo Imsi maior que 24 posições");

		imsi = string;
	}

	/**
	 * @param formatoA
	 */
	public void setInstance(ArquivoFormatoA formatoA) {
		instance = formatoA;
	}

	/**
	 * @param l
	 */
	public void setInterconnectionCost(String inter) {
		interconnectionCost = Long.parseLong(inter);
	}

	/**
	 * @param string
	 */
	public void setLocationId(String string) {
		if (string.length() > 20)
			throw new IllegalArgumentException("Campo LocationId maior que 20 posições");
			
		locationId = string;
	}

	/**
	 * @param l
	 */
	public void setOperatorId(String op) {
		operatorId = Long.parseLong(op);
		if (operatorId == 226 && this.getTransactionType() == 15)
		{
			this.setTransactionType("47");
			this.setBonusType("0");
			this.setBonusAmount("0");
			this.setBonusPercentage("0");
		}
	}

	/**
	 * @param string
	 */
	public void setOrigCalledNumber(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo OrigCalledNumber maior que 30 posições");
			
		origCalledNumber = string;
	}

	/**
	 * @param string
	 */
	public void setOrigSubId(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo OrigSubId maior que 30 posições");

		origSubId = string;
	}

	/**
	 * @param l
	 */
	public void setPercentDiscountApplied(String perc) {
		percentDiscountApplied = Long.parseLong(perc);
	}

	/**
	 * @param l
	 */
	public void setProfileId(String prof) {
		profileId = Long.parseLong(prof);
	}

	/**
	 * @param string
	 */
	public void setRateName(String string) {
		if (string != null && !string.equals("") && string.length() != 20)
			throw new IllegalArgumentException("Campo RateName esta diferente de 20 posições");

		rateName = string;

		setRateNameCSP("");
		setRateNamePlano("");
		setRateNameArea("");
		setRateNameTipo("");
		setRateNameAd("");
		setRateNameModulacao("");

		if (string != null && !string.equals(""))
		{
			setRateNameCSP			(string.substring(0,2));
			setRateNamePlano		(string.substring(2,4));
			setRateNameArea			(string.substring(4,6));
			setRateNameTipo			(string.substring(6,18));
			setRateNameAd			(string.substring(18,19));
			setRateNameModulacao	(string.substring(19,20));
		}

		if (string.indexOf("VMAIL") > -1 && this.getCallId().equals("99100") && this.getTipoCDR().equals("PP"))
			setCallId("*100");
	}

	/**
	 * @param string
	 */
	public void setRedirectingNumber(String string) {
		if (string.length() > 16)
			throw new IllegalArgumentException("Campo RedirectingNumber maior que 16 posições");
			
		redirectingNumber = string;
	}

	/**
	 * @param l
	 */
	public void setSequenceNumber(String num) {
		if (num == null || num.equals(""))
			throw new IllegalArgumentException("Campo SequenceNumber nulo");
		
		sequenceNumber = Long.parseLong(num);
	}

	/**
	 * @param l
	 */
	public void setServiceType(String serv) {
		serviceType = Long.parseLong(serv);
	}

	/**
	 * @param l
	 */
	public void setSmDeliveryMethod(String sm) {
		smDeliveryMethod = Long.parseLong(sm);
	}

	/**
	 * @param l
	 */
	public void setSmPackageId(String sm) {
		smPackageId = Long.parseLong(sm);
	}

	/**
	 * @param l
	 */
	public void setStartTime(String start) {
		if (start == null || start.equals(""))
			throw new IllegalArgumentException("Campo StartTime nulo");
			
		startTime = Long.parseLong(start);
	}

	/**
	 * @param l
	 */
	public void setSubClass(String sub) {
		subClass = Long.parseLong(sub);
	}

	/**
	 * @param string
	 */
	public void setSubId(String string) {
		if (string == null || string.equals(""))
			throw new IllegalArgumentException("Campo SubId nulo");

		if (string.length() > 30)
			throw new IllegalArgumentException("Campo SubId maior que 30 posições");

		subId = string;
	}

	/**
	 * @param l
	 */
	public void setTariffingMethod(String tariff) {
		tariffingMethod = Long.parseLong(tariff);
	}

	/**
	 * @param l
	 */
	public void setTax(String tx) {
		tax = Long.parseLong(tx);
	}

	/**
	 * @param date
	 */
	public void setTimestamp(String date) {
		if (date == null || date.equals(""))
			throw new IllegalArgumentException("Campo Timestamp nulo");
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			timestamp = sdf.parse(date);
		}
		catch(ParseException e){
			throw new IllegalArgumentException("Campo Timestamp nao esta no formato YYYY-MM-DD");
		}
	}

	/**
	 * @param l
	 */
	public void setTransactionType(String tt) {
		if (tt == null || tt.equals(""))
			throw new IllegalArgumentException("Campo TransactionType nulo");

		transactionType = Long.parseLong(tt);
		long transTypePPMO[] = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 18, 
								19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 
								32, 33, 34, 35, 36, 39, 40, 42, 43, 44, 46, 47, 48, 
								49, 50, 51, 52, 53, 54, 62, 63, 68, 69, 71, 72, 73, 
								74, 75, 76, 77, 78, 79, 81, 82, 83, 84, 85, 90, 91, 
								92, 93, 94, 95, 97, 98, 99, 100, 101, 102, 103, 104, 
								107, 108, 109, 161, 162, 163, 164, 165, 170, 171, 201, 202};

		long transTypePP[] = {10, 17, 45, 64, 65, 90, 91, 92, 93, 94, 95, 96, 200};
		if (this.getTipoCDR().equals("PP"))
			for (int i=0; i<transTypePP.length; i++)
				if (transactionType == transTypePP[i])
					throw new IllegalArgumentException("TransactionType: " + transactionType + " invalido para o arquivo " + getTipoCDR());
				
		long transTypeMO[] = {10, 17, 41, 64, 65, 66, 67, 96, 200};			
		if (this.getTipoCDR().equals("MO"))
			for (int i=0; i<transTypeMO.length; i++)
				if (transactionType == transTypeMO[i])
					throw new IllegalArgumentException("TransactionType: " + transactionType + " invalido para o arquivo " + getTipoCDR());					
		
		boolean isValido = false;		
		for (int i=0; i<transTypePPMO.length; i++)
			if (transactionType == transTypePPMO[i])
			{
				isValido = true;
				break;
			}

		if (!isValido)
			throw new IllegalArgumentException("TransactionType: " + transactionType + " invalido para o arquivo " + getTipoCDR());
	}

	/**
	 * @param string
	 */
	public void setVisitedMscAddress(String string) {
		visitedMscAddress = string;
	}
	/**
	 * @return
	 */
	public String getRateNameAd() {
		return rateNameAd != null ? rateNameAd : "";
	}

	/**
	 * @return
	 */
	public String getRateNameCSP() {
		return rateNameCSP != null ? rateNameCSP : "";
	}

	/**
	 * @return
	 */
	public String getRateNameModulacao() {
		return rateNameModulacao != null ? rateNameModulacao : "";
	}

	/**
	 * @return
	 */
	public String getRateNamePlano() {
		return rateNamePlano != null ? rateNamePlano : "";
	}

	/**
	 * @return
	 */
	public String getRateNameTipo() {
		return rateNameTipo != null ? rateNameTipo : "";
	}

	/**
	 * @param string
	 */
	public void setRateNameAd(String string) {
		if (string != null && !string.equals("-"))
			if (string.equals("1") || string.equals("2") || string.equals("3"))
				string="AD"+string;

		rateNameAd = string;
	}

	/**
	 * @param string
	 */
	public void setRateNameCSP(String string) {
		rateNameCSP = string;
	}

	/**
	 * @param string
	 */
	public void setRateNameModulacao(String string) {
		if (string == null || string.equals(""))
			string = "-";

		rateNameModulacao = string;
	}

	/**
	 * @param string
	 */
	public void setRateNamePlano(String string) {
		rateNamePlano = string;
	}

	/**
	 * @param string
	 */
	public void setRateNameTipo(String string) {
		rateNameTipo = string;
	}

	/**
	 * @return Returns the rateNameArea.
	 */
	public String getRateNameArea() {
		return rateNameArea;
	}
	
	/**
	 * @param rateNameArea The rateNameArea to set.
	 */
	public void setRateNameArea(String rateNameArea) {
		this.rateNameArea = rateNameArea;
	}
	
	/**
	 * @return Returns the bonusBalance.
	 */
	public long getBonusBalance() {
		return bonusBalance;
	}
	/**
	 * @param bonusBalance The bonusBalance to set.
	 */
	public void setBonusBalance(String bonusBalance) {
		this.bonusBalance = Long.parseLong(bonusBalance);
	}
	/**
	 * @return Returns the bonusBalanceDelta.
	 */
	public long getBonusBalanceDelta() {
		return bonusBalanceDelta;
	}
	/**
	 * @param bonusBalanceDelta The bonusBalanceDelta to set.
	 */
	public void setBonusBalanceDelta(String bonusBalanceDelta) {
		this.bonusBalanceDelta = Long.parseLong(bonusBalanceDelta);
	}
	/**
	 * @return Returns the callTypeId.
	 */
	public long getCallTypeId() {
		return callTypeId;
	}
	/**
	 * @param callTypeId The callTypeId to set.
	 */
	public void setCallTypeId(String callTypeId) {
		this.callTypeId = Long.parseLong(callTypeId);
	}
	/**
	 * @return Returns the dataBalance.
	 */
	public long getDataBalance() {
		return dataBalance;
	}
	/**
	 * @param dataBalance The dataBalance to set.
	 */
	public void setDataBalance(String dataBalance) {
		this.dataBalance = Long.parseLong(dataBalance);
	}
	/**
	 * @return Returns the dataBalanceDelta.
	 */
	public long getDataBalanceDelta() {
		return dataBalanceDelta;
	}
	/**
	 * @param dataBalanceDelta The dataBalanceDelta to set.
	 */
	public void setDataBalanceDelta(String dataBalanceDelta) {
		this.dataBalanceDelta = Long.parseLong(dataBalanceDelta);
	}
	/**
	 * @return Returns the periodicBalance.
	 */
	public long getPeriodicBalance() {
		return periodicBalance;
	}
	/**
	 * @param periodicBalance The periodicBalance to set.
	 */
	public void setPeriodicBalance(String periodicBalance) {
		this.periodicBalance = Long.parseLong(periodicBalance);
	}
	/**
	 * @return Returns the periodicBalanceDelta.
	 */
	public long getPeriodicBalanceDelta() {
		return periodicBalanceDelta;
	}
	/**
	 * @param periodicBalanceDelta The periodicBalanceDelta to set.
	 */
	public void setPeriodicBalanceDelta(String periodicBalanceDelta) {
		this.periodicBalanceDelta = Long.parseLong(periodicBalanceDelta);
	}
	/**
	 * @return Returns the smBalance.
	 */
	public long getSmBalance() {
		return smBalance;
	}
	/**
	 * @param smBalance The smBalance to set.
	 */
	public void setSmBalance(String smBalance) {
		this.smBalance = Long.parseLong(smBalance);
	}
	/**
	 * @return Returns the smBalanceDelta.
	 */
	public long getSmBalanceDelta() {
		return smBalanceDelta;
	}
	/**
	 * @param smBalanceDelta The smBalanceDelta to set.
	 */
	public void setSmBalanceDelta(String smBalanceDelta) {
		this.smBalanceDelta = Long.parseLong(smBalanceDelta);
	}

	/**
	 * @return
	 */
	public String getTipoCDR() {
		return tipoCDR;
	}

	/**
	 * @param aTipCDR
	 */
	public void setTipoCDR(String aTipCDR) {
		if (aTipCDR == null || aTipCDR.equals(""))
			throw new IllegalArgumentException("Campo TipoCDR esta nulo.");

		tipoCDR = aTipCDR;
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof FormatoDeArquivoCDR))
			throw new ClassCastException("Classe do objeto invalido para comparacao");
		
		Date dataComparacao = ((FormatoDeArquivoCDR)obj).getTimestamp();
		Long startTimeComp  = new Long(((FormatoDeArquivoCDR)obj).getStartTime());
		if ( dataComparacao.compareTo(this.getTimestamp()) != 0 )
			return dataComparacao.compareTo(this.getTimestamp());
		else return new Long(this.getStartTime()).compareTo(startTimeComp); 
	}
	
	public int hashCode()
	{
		Long chave = new Long(String.valueOf(getTimestamp().getTime())+String.valueOf(getStartTime()));
		return chave.hashCode();
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof FormatoDeArquivoCDR))
			return false;
		return true;
	}
}
