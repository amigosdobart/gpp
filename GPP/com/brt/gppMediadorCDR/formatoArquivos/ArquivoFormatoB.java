package com.brt.gppMediadorCDR.formatoArquivos;

/*
 * Created on 06/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.*;
import java.text.*;

public class ArquivoFormatoB implements FormatoDeArquivoCDR,Comparable
{
	private long sequenceNumber;
	private long serviceKey;
	private long transactionType;
	private String subscriberId;
	private Date timestamp;
	private long startTime;
	private Date dateSubmitedClient;
	private long timeSubmitedClient;
	private long cost;
	private String correlationId;
	private long initialAccountBalance;
	private long accountBalance;
	private long accountBalanceDelta;
	private long accountStatus;
	private String tariffRule;
	private String discountRule;
	private double discount;
	private double tax;
	private boolean peakTime;
	private long billingEventId;
	private long initialDataBalance;
	private long dataBalance;
	private long dataBalanceDelta;
	private long numberOfVouchers;
	private long vttQueue;
	private long vttTariffPlanId;
	private long serviceProviderId;
	private long messageSize;
	private long primaryMessageContentType;
	private long messageClass;
	private String recipientAddress;
	private long recipientType;
	private String originatorAddress;
	private long originatorVASTariffClass;
	private long originatorType;
	private String tipoCDR;
	private String 	rateName;
	private String  rateNameCSP;
	private String  rateNamePlano;
	private String  rateNameArea;
	private String  rateNameTipo;
	private String  rateNameAd;
	private String  rateNameModulacao;

		
	private static ArquivoFormatoB instance = new ArquivoFormatoB();
	private Map 	errors;
	private String 	linhaAtual;

	public ArquivoFormatoB(){
		errors = new HashMap();
	}

	public static ArquivoFormatoB getInstance(){
		return instance;
	}
	
	public Map getParseErrors(){
		return errors;
	}

	public String getLinhaAtual(){
		return linhaAtual;  
	}

	public boolean accept(){
		// Verifica se houve erros de validação de campos ou seleção
		if (getParseErrors().size() > 0){
			System.out.println(getParseErrors());
			return false;
		}
		return true;
	}

	public char getDestinationFormat(){
		return 'D';
	}

	public Map parse(String linha){
		errors 		= new HashMap();
		linhaAtual 	= linha;
		String campos[] = linha.split(",");
		try{
			setSequenceNumber(campos[0]);
			setServiceKey(campos[1]);
			setTransactionType(campos[2]);
			setSubscriberId(campos[3]);
			setTimestamp(campos[4]);
			setStartTime(campos[5]);
			setDateSubmitedClient(campos[6]);
			setTimeSubmitedClient(campos[7]);
			setCost(campos[8]);
			setCorrelationId(campos[9]);
			setInitialAccountBalance(campos[10]);
			setAccountBalance(campos[11]);
			setAccountBalanceDelta(campos[12]);
			setAccountStatus(campos[13]);
			setTariffRule(campos[14]);
			setDiscountRule(campos[15]);
			setDiscount(campos[16]);
			setTax(campos[17]);
			setPeakTime(campos[18]);
			setBillingEventId(campos[19]);
			setInitialDataBalance(campos[20]);
			setDataBalance(campos[21]);
			setDataBalanceDelta(campos[22]);
			setNumberOfVouchers(campos[23]);
			setVttQueue(campos[24]);
			setVttTariffPlanId(campos[25]);
			//setMessageSize(campos[18]);
			//setRecipientAddress(campos[19]);
			setServiceProviderId(campos[26]);
			setPrimaryMessageContentType(campos[28]);
			setMessageClass(campos[29]);
            setOriginatorType(campos[30]);
			setRecipientType(campos[31]);
			setOriginatorAddress(campos[32]);
			setOriginatorVASTariffClass(campos[33]);
			setRateName();
		}
		catch(IllegalArgumentException ie){
			errors.put(linha,ie.getMessage());
		}
		return errors;
	}
	
	/**
	 * @return
	 */
	public long getAccountBalance() {
		return accountBalance;
	}

	/**
	 * @return
	 */
	public long getBillingEventId() {
		return billingEventId;
	}

	/**
	 * @return
	 */
	public String getCorrelationId() {
		return correlationId;
	}

	/**
	 * @return
	 */
	public long getCost() {
		return cost;
	}

	/**
	 * @return
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @return
	 */
	public String getDiscountRule() {
		return discountRule;
	}

	/**
	 * @return
	 */
	public long getMessageClass() {
		return messageClass;
	}

	/**
	 * @return
	 */
	public long getMessageSize() {
		return messageSize;
	}

	/**
	 * @return
	 */
	public String getOriginatorAddress() {
		return originatorAddress;
	}

	/**
	 * @return
	 */
	public long getOriginatorType() {
		return originatorType;
	}

	/**
	 * @return
	 */
	public long getOriginatorVASTariffClass() {
		return originatorVASTariffClass;
	}

	/**
	 * @return
	 */
	public boolean isPeakTime() {
		return peakTime;
	}

	public long getPeakTime(){
		if (this.isPeakTime())
			return 1;

		return 0;
	}
	
	/**
	 * @return
	 */
	public long getPrimaryMessageContentType() {
		return primaryMessageContentType;
	}

	/**
	 * @return
	 */
	public String getRecipientAddress() {
		return recipientAddress;
	}

	/**
	 * @return
	 */
	public long getRecipientType() {
		return recipientType;
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
	public long getServiceKey() {
		return serviceKey;
	}

	/**
	 * @return
	 */
	public long getServiceProviderId() {
		return serviceProviderId;
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
	public String getSubscriberId() {
		return subscriberId;
	}

	/**
	 * @return
	 */
	public String getTariffRule() {
		return tariffRule;
	}

	/**
	 * @return
	 */
	public double getTax() {
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
	public Date getDateSubmitedClient() {
		return dateSubmitedClient;
	}

	public String getFormatDateSubmitedClient(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(getDateSubmitedClient());
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
	public long getTimeSubmitedClient() {
		return timeSubmitedClient;
	}

	/**
	 * @return
	 */
	public long getAccountStatus() {
		return accountStatus;
	}


	public String getRateName(){
		return rateName;
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
			string = "X";

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
	 */
	public void setRateName() {
		if (this.getBillingEventId() == 220 && this.getRecipientType() == 0)
			rateName = "------MM1-----------";
			
		if (this.getBillingEventId() == 220 && this.getRecipientType() == 1)
			rateName = "------MM1 email-----";

		if (this.getBillingEventId() == 220 && this.getRecipientType() == 4)
			rateName = "------MM7-----------" + this.getRecipientAddress();

		if (this.getBillingEventId() == 220 && this.getRecipientType() == 5)
			if (this.getRecipientAddress().startsWith("00") || this.getRecipientAddress().startsWith("+"))
				rateName = "------MM4 Exterior--";
			else
				rateName = "------MM4-----------";

		if (this.getBillingEventId() == 223 || this.getBillingEventId() == 245)
			rateName = "------MM7-----------" + this.getOriginatorVASTariffClass();
		
		setRateNameCSP("");
		setRateNamePlano("");
		setRateNameTipo("");
		setRateNameAd("");
		setRateNameModulacao("");

		setRateNameCSP			(getRateName().substring(0,1));
		setRateNamePlano		(getRateName().substring(2,3));
		setRateNameArea			(getRateName().substring(4,5));
		setRateNameTipo			(getRateName().substring(6,17));
		setRateNameAd			(getRateName().substring(18,18));
		setRateNameModulacao	(getRateName().substring(19,19));
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
	 * @param l
	 */
	public void setAccountBalance(String accBal) {
		accountBalance = Long.parseLong(accBal);
	}

	/**
	 * @param l
	 */
	public void setBillingEventId(String billing) {
		boolean isValid=false;
		long validValues[] = {217,220,223,245};
		for (int i=0; i < validValues.length; i++)
			if (Long.parseLong(billing) == validValues[i])
				isValid=true;
				
		if (!isValid)
			throw new IllegalArgumentException("Campo BillingEventId com valor inesperado: " + billing);

		billingEventId = Long.parseLong(billing);
	}

	/**
	 * @param string
	 */
	public void setCorrelationId(String string) {
		if (string.length() > 20)
			throw new IllegalArgumentException("Campo CorrelationId esta maior que 20 posições");

		correlationId = string;
	}

	/**
	 * @param l
	 */
	public void setCost(String cst) {
		cost = Long.parseLong(cst);
	}

	/**
	 * @param d
	 */
	public void setDiscount(String dis) {
		discount = Double.parseDouble(dis);
	}

	/**
	 * @param string
	 */
	public void setDiscountRule(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo DiscountRule esta maior que 30 posições");

		discountRule = string;
	}

	/**
	 * @param l
	 */
	public void setMessageClass(String msg) {
		messageClass = Long.parseLong(msg);
		if (messageClass < 0 || messageClass > 3)
			throw new IllegalArgumentException("Campo messageClass possui valor inválido: " + messageClass);
	}

	/**
	 * @param l
	 */
	public void setMessageSize(String s) {
		messageSize = Long.parseLong(s);
	}

	/**
	 * @param string
	 */
	public void setOriginatorAddress(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo OriginatorAddress esta maior que 30 posições");

		originatorAddress = string;
	}

	/**
	 * @param long
	 */
	public void setOriginatorType(String orType) {
		originatorType = Long.parseLong(orType);
		if (originatorType < 0 || originatorType > 8)
			throw new IllegalArgumentException("Campo OriginatorType possui valor invalido: " + orType);	
	}

	/**
	 * @param l
	 */
	public void setOriginatorVASTariffClass(String org) {
		originatorVASTariffClass = Long.parseLong(org);
		
		if (originatorVASTariffClass < 0 || originatorVASTariffClass > 32767)
			throw new IllegalArgumentException("Campo OriginatorVASTariffClass possui valor inválido: " + originatorVASTariffClass);
	}

	/**
	 * @param b
	 */
	public void setPeakTime(String peak) {
		int peakB = Integer.parseInt(peak);
		if (peakB == 0)
			peakTime = false;
		else
			peakTime = true;
	}

	/**
	 * @param l
	 */
	public void setPrimaryMessageContentType(String content) {
		primaryMessageContentType = Long.parseLong(content);
		
		if (primaryMessageContentType < 0 || primaryMessageContentType > 4)
			throw new IllegalArgumentException("Campo PrimaryMessageContentType possui valor inválido: " + primaryMessageContentType);
	}

	/**
	 * @param string
	 */
	public void setRecipientAddress(String string) {
		if (string.length() > 255)
			throw new IllegalArgumentException("Campo RecipientAddress esta maior que 255 posições");

		recipientAddress = string;
	}

	/**
	 * @param l
	 */
	public void setRecipientType(String rt) {
		recipientType = Long.parseLong(rt);
		
		if (recipientType < 0 || recipientType > 6)
			throw new IllegalArgumentException("Campo recipientType possui valor inválido: " + recipientType);
	}

	/**
	 * @param l
	 */
	public void setSequenceNumber(String seq) {
		if (seq == null || seq.equals(""))
			throw new IllegalArgumentException("Campo SequenceNumber esta nulo.");
			
		sequenceNumber = Long.parseLong(seq);
	}

	/**
	 * @param l
	 */
	public void setServiceKey(String key) {
		if (key == null || key.equals(""))
			throw new IllegalArgumentException("Campo ServiceKey esta nulo.");
			
		serviceKey = Long.parseLong(key);
	}

	/**
	 * @param l
	 */
	public void setServiceProviderId(String service) {
		serviceProviderId = Long.parseLong(service);
	}

	/**
	 * @param l
	 */
	public void setStartTime(String start) {
		if (start == null || start.equals(""))
			throw new IllegalArgumentException("Campo StartTime esta nulo.");
			
		startTime = Long.parseLong(start);
	}

	/**
	 * @param string
	 */
	public void setSubscriberId(String string) {
		if (string == null || string.equals(""))
			throw new IllegalArgumentException("Campo SubscriberId esta nulo.");
			
		if (string.length() > 15)
			throw new IllegalArgumentException("Campo SubscriberId esta maior que 15 posições.");

		subscriberId = string;
	}

	/**
	 * @param string
	 */
	public void setTariffRule(String string) {
		if (string.length() > 30)
			throw new IllegalArgumentException("Campo TariffRule esta maior que 30 posições");

		tariffRule = string;
	}

	/**
	 * @param d
	 */
	public void setTax(String tx) {
		tax = Double.parseDouble(tx);
	}

	/**
	 * @param date
	 */
	public void setTimestamp(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (data == null || data.equals(""))
			throw new IllegalArgumentException("Camo Timestamp esta nulo.");
		
		try{
			timestamp = sdf.parse(data);
		}
		catch(ParseException pe){
			throw new IllegalArgumentException("Campo Timestamp esta no formato inválido.");
		}
	}

	/**
	 * @param date
	 */
	public void setDateSubmitedClient(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (data == null || data.equals(""))
			throw new IllegalArgumentException("Camo DateSubmitedClient esta nulo.");
		
		try{
			dateSubmitedClient = sdf.parse(data);
		}
		catch(ParseException pe){
			throw new IllegalArgumentException("Campo DateSubmitedClient esta no formato inválido.");
		}
	}

	/**
	 * @param l
	 */
	public void setTimeSubmitedClient(String time) {
		if (time == null || time.equals(""))
			throw new IllegalArgumentException("Campo TimeSubmitedClient esta nulo.");
			
		timeSubmitedClient = Long.parseLong(time);
	}

	/**
	 * @param l
	 */
	public void setAccountStatus(String status) {
		accountStatus = Long.parseLong(status);
	}

	/**
	 * @param l
	 */
	public void setTransactionType(String tt) {
		if (tt == null || tt.equals(""))
			throw new IllegalArgumentException("Campo TransactionType esta nulo.");
			
		transactionType = Long.parseLong(tt);
		
		if (transactionType != 0 && transactionType != 1)
			throw new IllegalArgumentException("Campo TransactionType com valor invalido: " + transactionType);
	}

	/**
	 * @return Returns the accountBalanceDelta.
	 */
	public long getAccountBalanceDelta() {
		return accountBalanceDelta;
	}
	/**
	 * @param accountBalanceDelta The accountBalanceDelta to set.
	 */
	public void setAccountBalanceDelta(String accountBalanceDelta) {
		this.accountBalanceDelta = Long.parseLong(accountBalanceDelta);
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
	 * @return Returns the initialAccountBalance.
	 */
	public long getInitialAccountBalance() {
		return initialAccountBalance;
	}
	/**
	 * @param initialAccountBalance The initialAccountBalance to set.
	 */
	public void setInitialAccountBalance(String initialAccountBalance) {
		this.initialAccountBalance = Long.parseLong(initialAccountBalance);
	}
	/**
	 * @return Returns the initialDataBalance.
	 */
	public long getInitialDataBalance() {
		return initialDataBalance;
	}
	/**
	 * @param initialDataBalance The initialDataBalance to set.
	 */
	public void setInitialDataBalance(String initialDataBalance) {
		this.initialDataBalance = Long.parseLong(initialDataBalance);
	}
	/**
	 * @return Returns the numberOfVouchers.
	 */
	public long getNumberOfVouchers() {
		return numberOfVouchers;
	}
	/**
	 * @param numberOfVouchers The numberOfVouchers to set.
	 */
	public void setNumberOfVouchers(String numberOfVouchers) {
		this.numberOfVouchers = Long.parseLong(numberOfVouchers);
	}
	/**
	 * @return Returns the vttQueue.
	 */
	public long getVttQueue() {
		return vttQueue;
	}
	/**
	 * @param vttQueue The vttQueue to set.
	 */
	public void setVttQueue(String vttQueue) {
		this.vttQueue = Long.parseLong(vttQueue);
	}
	/**
	 * @return Returns the vttTariffPlanId.
	 */
	public long getVttTariffPlanId() {
		return vttTariffPlanId;
	}
	/**
	 * @param vttTariffPlanId The vttTariffPlanId to set.
	 */
	public void setVttTariffPlanId(String vttTariffPlanId) {
		this.vttTariffPlanId = Long.parseLong(vttTariffPlanId);
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
