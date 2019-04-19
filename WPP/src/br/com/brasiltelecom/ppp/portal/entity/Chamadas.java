/*
 * Created on 23/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.entity;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import br.com.brasiltelecom.ppp.util.PhoneNumberFormat;

/**
 * Modela a tabela de CDRs
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Chamadas {
	private long intSeqNo;
	private long duracaoChamada;
	//private String id_chamada;
	private Date data;
	private String msisdnOrigem;
	private long hora;
	private String tipoChamada;
	private int regiaoOrigem;
	private int tipoTransacao;
	private long valorChamada;
	private int tipoServico;
	private int finalBalanco;
	private long subClass;
	private int profileId;
	private int finalTariffPlanId;
	private int expire;
	private String msisdnDestino;
	private String imsi;
	private String numeroConta;
	private String cliOut;
	private int percentDiscountApplied;
	private int operatorTransType;
	private int tariffingMethod;
	private long operatorId;
	private String locationId;
	private String cellName;
	private String carrierId;
	private int smDeliveryMethod;
	private int packageId;
	private int airtimeCost;
	private int interconnectionCost;
	private int tax;
	private String destinationName;
	private int ffDiscount;
	private String callReference;
	private String additionalCallingPartyNumber;
	private String redirectNumber;
	private int callingPartyCategory;
	private String homeMscAddress;
	private String visitDmsAddress;
	private String discountType;
	private Modulacao modulacao;
	private Rating rating;
	  
	private SimpleDateFormat sdF = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	private SimpleDateFormat soData = new SimpleDateFormat("dd/MM/yyyy");
	private DecimalFormat df =  new DecimalFormat("00");
	private PhoneNumberFormat ph = new PhoneNumberFormat();
	
	public String getFdata() {	
		return sdF.format(data);
	}

	/**
	 * @return
	 */
	public String getAdditionalCallingPartyNumber() {
		return additionalCallingPartyNumber;
	}

	/**
	 * @return
	 */
	public int getAirtimeCost() {
		return airtimeCost;
	}

	/**
	 * @return
	 */
	public int getCallingPartyCategory() {
		return callingPartyCategory;
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
	public String getCarrierId() {
		return carrierId;
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
	public Date getData() {
		return data;
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
	public String getDiscountType() {
		return discountType;
	}

	/**
	 * @return
	 */
	public long getDuracaoChamada() {
		return duracaoChamada;
	}

	/**
	 * @return
	 */
	public int getExpire() {
		return expire;
	}

	/**
	 * @return
	 */
	public int getFfDiscount() {
		return ffDiscount;
	}

	/**
	 * @return
	 */
	public int getFinalBalanco() {
		return finalBalanco;
	}

	/**
	 * @return
	 */
	public int getFinalTariffPlanId() {
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
	public long getHora() {
		return hora;
	}

	/**
	 * @return
	 */
	/*public String getId_chamada() {
		return id_chamada;
	}*/

	/**
	 * @return
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @return
	 */
	public int getInterconnectionCost() {
		return interconnectionCost;
	}

	/**
	 * @return
	 */
	public long getIntSeqNo() {
		return intSeqNo;
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
	public String getMsisdnDestino() {
		return msisdnDestino;
	}

	/**
	 * @return
	 */
	public String getMsisdnOrigem() {
		return msisdnOrigem;
	}

	/**
	 * @return
	 */
	public String getNumeroConta() {
		return numeroConta;
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
	public int getOperatorTransType() {
		return operatorTransType;
	}

	/**
	 * @return
	 */
	public int getPackageId() {
		return packageId;
	}

	/**
	 * @return
	 */
	public int getPercentDiscountApplied() {
		return percentDiscountApplied;
	}

	/**
	 * @return
	 */
	public int getProfileId() {
		return profileId;
	}

	/**
	 * @return
	 */
	public String getRedirectNumber() {
		return redirectNumber;
	}

	/**
	 * @return
	 */
	public int getRegiaoOrigem() {
		return regiaoOrigem;
	}

	/**
	 * @return
	 */
	public int getSmDeliveryMethod() {
		return smDeliveryMethod;
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
	public int getTariffingMethod() {
		return tariffingMethod;
	}

	/**
	 * @return
	 */
	public int getTax() {
		return tax;
	}

	/**
	 * @return
	 */
	public String getTipoChamada() {
		return tipoChamada;
	}

	/**
	 * @return
	 */
	public int getTipoServico() {
		return tipoServico;
	}

	/**
	 * @return
	 */
	public int getTipoTransacao() {
		return tipoTransacao;
	}

	/**
	 * @return
	 */
	public long getValorChamada() {
		return valorChamada;
	}

	/**
	 * @return
	 */
	public String getVisitDmsAddress() {
		return visitDmsAddress;
	}

	/**
	 * @param string
	 */
	public void setAdditionalCallingPartyNumber(String string) {
		additionalCallingPartyNumber = string;
	}

	/**
	 * @param i
	 */
	public void setAirtimeCost(int i) {
		airtimeCost = i;
	}

	/**
	 * @param i
	 */
	public void setCallingPartyCategory(int i) {
		callingPartyCategory = i;
	}

	/**
	 * @param string
	 */
	public void setCallReference(String string) {
		callReference = string;
	}

	/**
	 * @param string
	 */
	public void setCarrierId(String string) {
		carrierId = string;
	}

	/**
	 * @param string
	 */
	public void setCellName(String string) {
		cellName = string;
	}

	/**
	 * @param string
	 */
	public void setCliOut(String string) {
		cliOut = string;
	}

	/**
	 * @param date
	 */
	public void setData(Date date) {
		data = date;
	}

	/**
	 * @param string
	 */
	public void setDestinationName(String string) {
		destinationName = string;
	}

	/**
	 * @param string
	 */
	public void setDiscountType(String string) {
		discountType = string;
	}

	/**
	 * @param l
	 */
	public void setDuracaoChamada(long l) {
		duracaoChamada = l;
	}

	/**
	 * @param i
	 */
	public void setExpire(int i) {
		expire = i;
	}

	/**
	 * @param i
	 */
	public void setFfDiscount(int i) {
		ffDiscount = i;
	}

	/**
	 * @param i
	 */
	public void setFinalBalanco(int i) {
		finalBalanco = i;
	}

	/**
	 * @param i
	 */
	public void setFinalTariffPlanId(int i) {
		finalTariffPlanId = i;
	}

	/**
	 * @param string
	 */
	public void setHomeMscAddress(String string) {
		homeMscAddress = string;
	}

	/**
	 * @param l
	 */
	public void setHora(long l) {
		hora = l;
	}

	/**
	 * @param string
	 */
	/*public void setId_chamada(String string) {
		id_chamada = string;
	}*/

	/**
	 * @param string
	 */
	public void setImsi(String string) {
		imsi = string;
	}

	/**
	 * @param i
	 */
	public void setInterconnectionCost(int i) {
		interconnectionCost = i;
	}

	/**
	 * @param l
	 */
	public void setIntSeqNo(long l) {
		intSeqNo = l;
	}

	/**
	 * @param string
	 */
	public void setLocationId(String string) {
		locationId = string;
	}

	/**
	 * @param string
	 */
	public void setMsisdnDestino(String string) {
		msisdnDestino = string;
	}

	/**
	 * @param string
	 */
	public void setMsisdnOrigem(String string) {
		msisdnOrigem = string;
	}

	/**
	 * @param string
	 */
	public void setNumeroConta(String string) {
		numeroConta = string;
	}

	/**
	 * @param l
	 */
	public void setOperatorId(long l) {
		operatorId = l;
	}

	/**
	 * @param i
	 */
	public void setOperatorTransType(int i) {
		operatorTransType = i;
	}

	/**
	 * @param i
	 */
	public void setPackageId(int i) {
		packageId = i;
	}

	/**
	 * @param i
	 */
	public void setPercentDiscountApplied(int i) {
		percentDiscountApplied = i;
	}

	/**
	 * @param i
	 */
	public void setProfileId(int i) {
		profileId = i;
	}

	/**
	 * @param string
	 */
	public void setRedirectNumber(String string) {
		redirectNumber = string;
	}

	/**
	 * @param i
	 */
	public void setRegiaoOrigem(int i) {
		regiaoOrigem = i;
	}

	/**
	 * @param i
	 */
	public void setSmDeliveryMethod(int i) {
		smDeliveryMethod = i;
	}

	/**
	 * @param l
	 */
	public void setSubClass(long l) {
		subClass = l;
	}

	/**
	 * @param i
	 */
	public void setTariffingMethod(int i) {
		tariffingMethod = i;
	}

	/**
	 * @param i
	 */
	public void setTax(int i) {
		tax = i;
	}

	/**
	 * @param string
	 */
	public void setTipoChamada(String string) {
		tipoChamada = string;
	}

	/**
	 * @param i
	 */
	public void setTipoServico(int i) {
		tipoServico = i;
	}

	/**
	 * @param i
	 */
	public void setTipoTransacao(int i) {
		tipoTransacao = i;
	}

	/**
	 * @param l
	 */
	public void setValorChamada(long l) {
		valorChamada = l;
	}

	/**
	 * @param string
	 */
	public void setVisitDmsAddress(String string) {
		visitDmsAddress = string;
	}

	/**
	 * @return
	 */
	public Modulacao getModulacao() {
		return modulacao;
	}

	/**
	 * @param modulacao
	 */
	public void setModulacao(Modulacao modulacao) {
		this.modulacao = modulacao;
	}

	public String getDataString(){
		return soData.format(data);
	}
	public String getHoraString(){
		long h = hora / 3600;
		long m = (hora % 3600) / 60;
		long s = (hora % 3600) % 60;
		
		return "" + df.format(h) + ":" + df.format(m) + ":" + df.format(s);
	}
	public String getMsisdnOrigemString(){
		if (msisdnOrigem != null && !msisdnOrigem.trim().equals("") && msisdnOrigem.length() == 12) {
			return "(" + msisdnOrigem.substring(2,4) + ") " + msisdnOrigem.substring(4,8) + "-" + msisdnOrigem.substring(8,12);
		} else {
			return msisdnOrigem;
		} 
	}
	public String getMsisdnDestinoString()
	{
		return ph.format(msisdnDestino);
	}
	/**
	 * @return
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @param rating
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	public String getCellNameString(){
		return cellName.substring(0,2) + "-" + cellName.substring(3,5);
	}
	
	public String getDuracaoChamadaString(){
		long h = duracaoChamada / 3600;
		long m = (duracaoChamada % 3600) / 60;
		long s = (duracaoChamada % 3600) % 60;
		
		return "" + df.format(h) + ":" + df.format(m) + ":" + df.format(s);
 
	}

}
