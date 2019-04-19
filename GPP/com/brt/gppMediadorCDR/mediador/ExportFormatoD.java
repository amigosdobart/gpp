package com.brt.gppMediadorCDR.mediador;

/*
 * Created on 07/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import com.brt.gppMediadorCDR.formatoArquivos.*;
import java.io.*;

public class ExportFormatoD extends ExportArquivoCDR implements DataConverter{

	public ExportFormatoD(String fileName) throws IOException{
		super(fileName);
	}
	
	public void convertAndWriteFromFormatoA(ArquivoFormatoA forA) throws IOException{
		writeLine(convertFromFormatoA(forA));
	}

	public String convertFromFormatoA(ArquivoFormatoA forA){
		String linhaRetorno =   "" +
								forA.getSequenceNumber() 				+ getFieldSeparator() +
								forA.getSubId()							+ getFieldSeparator() +
								forA.getFormatTimestamp("dd/MM/yyyy") + " " + forA.getStartTime()	+ getFieldSeparator() +
								forA.getStartTime()						+ getFieldSeparator() +
								forA.getCallDuration()					+ getFieldSeparator() +
								forA.getServiceType()					+ getFieldSeparator() +
								forA.getImsi()							+ getFieldSeparator() +
								forA.getAccountNumber()					+ getFieldSeparator() +
								forA.getTransactionType()				+ getFieldSeparator() +
								forA.getAccountBalanceDelta()			+ getFieldSeparator() +
								forA.getCallId()						+ getFieldSeparator() +
								forA.getFinalAccountBalance()			+ getFieldSeparator() +
								forA.getCallOriginId()					+ getFieldSeparator() +
								forA.getSubClass()						+ getFieldSeparator() +
								forA.getProfileId()						+ getFieldSeparator() +
								forA.getFinalTariffPlanId()				+ getFieldSeparator() +
								forA.getExpiryType()					+ getFieldSeparator() +
								forA.getOrigCalledNumber()				+ getFieldSeparator() +
								forA.getCliOut()						+ getFieldSeparator() +
								forA.getRateName()						+ getFieldSeparator() +
								forA.getRateNameCSP()					+ getFieldSeparator() +
								forA.getRateNamePlano()					+ getFieldSeparator() +
								forA.getRateNameArea()					+ getFieldSeparator() +
								forA.getRateNameTipo()					+ getFieldSeparator() +
								forA.getRateNameAd()					+ getFieldSeparator() +
								forA.getRateNameModulacao()				+ getFieldSeparator() +
								forA.getDiscountType()					+ getFieldSeparator() +
								forA.getPercentDiscountApplied()		+ getFieldSeparator() +
								forA.getExternalTransactionType()		+ getFieldSeparator() +
								forA.getTariffingMethod()				+ getFieldSeparator() +
								forA.getOperatorId()					+ getFieldSeparator() +
								forA.getLocationId()					+ getFieldSeparator() +
								forA.getCellName()						+ getFieldSeparator() +
								forA.getCarrierPrefix()					+ getFieldSeparator() +
								forA.getSmDeliveryMethod()				+ getFieldSeparator() +
								forA.getSmPackageId()					+ getFieldSeparator() +
								forA.getAirtimeCost()					+ getFieldSeparator() +
								forA.getInterconnectionCost()			+ getFieldSeparator() +
								forA.getTax()							+ getFieldSeparator() +
								forA.getDestinationName()				+ getFieldSeparator() +
								forA.getFfDiscount()					+ getFieldSeparator() +
								forA.getCallReference()					+ getFieldSeparator() +
								forA.getAdditionalCallingPartynumber()	+ getFieldSeparator() +
								forA.getRedirectingNumber()				+ getFieldSeparator() +
								forA.getCallingPartyCategory()			+ getFieldSeparator() +
								forA.getHomeMscAddress()				+ getFieldSeparator() +
								forA.getVisitedMscAddress()				+ getFieldSeparator() +
								forA.getOrigSubId()						+ getFieldSeparator() +
								forA.getBonusType()						+ getFieldSeparator() +
								forA.getBonusPercentage()				+ getFieldSeparator() +
								forA.getBonusAmount()					+ getFieldSeparator() +
								forA.getPeriodicBalance()				+ getFieldSeparator() +
								forA.getPeriodicBalanceDelta()			+ getFieldSeparator() +
								forA.getBonusBalance()					+ getFieldSeparator() +
								forA.getBonusBalanceDelta()				+ getFieldSeparator() +
								forA.getSmBalance()						+ getFieldSeparator() +
								forA.getSmBalanceDelta()				+ getFieldSeparator() +
								forA.getDataBalance()					+ getFieldSeparator() +
								forA.getDataBalanceDelta()				+ getFieldSeparator() +
								forA.getCallTypeId()					+ getFieldSeparator() +
								"202"									+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+
								forA.getTipoCDR()						+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ 
								forA.getCost()							+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator();
								
		return linhaRetorno+ExportArquivoCDR.RETURN;
	}
	
	public void convertAndWriteFromFormatoB(ArquivoFormatoB forB) throws IOException{
		writeLine(convertFromFormatoB(forB));
	}

	public String convertFromFormatoB(ArquivoFormatoB forB){
		String linhaRetorno =   "" +
								forB.getSequenceNumber() 				+ getFieldSeparator() +
								forB.getSubscriberId()					+ getFieldSeparator() +
								forB.getFormatTimestamp("dd/MM/yyyy")+ " " + forB.getStartTime()	+ getFieldSeparator() +
								forB.getStartTime()						+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								forB.getTransactionType()				+ getFieldSeparator() +
								forB.getAccountBalanceDelta()			+ getFieldSeparator() +
								getFieldSeparator()						+
								forB.getAccountBalance()				+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+
								forB.getRateName()						+ getFieldSeparator() +
								forB.getRateNameCSP()					+ getFieldSeparator() +
								forB.getRateNamePlano()					+ getFieldSeparator() +
								forB.getRateNameArea()					+ getFieldSeparator() +
								forB.getRateNameTipo()					+ getFieldSeparator() +
								forB.getRateNameAd()					+ getFieldSeparator() +
								forB.getRateNameModulacao()				+ getFieldSeparator() +
								forB.getDiscountRule()					+ getFieldSeparator() +
								forB.getDiscount()						+ getFieldSeparator() +
								getFieldSeparator()						+
								forB.getTariffRule()					+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +		
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+
								forB.getDataBalance()					+ getFieldSeparator() +
								forB.getDataBalanceDelta()				+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								forB.getServiceKey()					+ getFieldSeparator() +
								forB.getCorrelationId()					+ getFieldSeparator() +
								forB.getTax()							+ getFieldSeparator() +
								forB.getPeakTime()						+ getFieldSeparator() +
								getFieldSeparator() 					+
								forB.getBillingEventId()				+ getFieldSeparator() +
								forB.getServiceProviderId()				+ getFieldSeparator() +
								forB.getMessageSize()					+ getFieldSeparator() +
								forB.getPrimaryMessageContentType()		+ getFieldSeparator() +
								forB.getMessageClass()					+ getFieldSeparator() +
								forB.getRecipientAddress()				+ getFieldSeparator() +
								forB.getRecipientType()					+ getFieldSeparator() +
								forB.getOriginatorType()				+ getFieldSeparator() +
								forB.getOriginatorVASTariffClass()      + getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								forB.getTipoCDR()						+ getFieldSeparator() +
								forB.getFormatDateSubmitedClient("dd/MM/yyyy") + getFieldSeparator() +
								forB.getTimeSubmitedClient()			+ getFieldSeparator() +
								forB.getAccountStatus()					+ getFieldSeparator() +
								forB.getCost()							+ getFieldSeparator() +
								forB.getNumberOfVouchers()				+ getFieldSeparator() +
								forB.getVttQueue()						+ getFieldSeparator() +
								forB.getVttTariffPlanId()				+ getFieldSeparator() +
								forB.getInitialAccountBalance()			+ getFieldSeparator() +
								forB.getInitialDataBalance()			+ getFieldSeparator();
								
		return linhaRetorno+ExportArquivoCDR.RETURN;
	}
	
	public void convertAndWriteFromFormatoC(ArquivoFormatoC forC) throws IOException{
		writeLine(convertFromFormatoC(forC));
	}

	public String convertFromFormatoC(ArquivoFormatoC forC){
		String linhaRetorno =   "" +
								forC.getSequenceNumber() 				+ getFieldSeparator() +
								forC.getSubscriberId()					+ getFieldSeparator() +
								forC.getFormatTimestamp("dd/MM/yyyy")+ " " + forC.getStartTime()	+ getFieldSeparator() +
								forC.getStartTime()						+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								forC.getTransactionType()				+ getFieldSeparator() +
								forC.getAccountBalanceDelta()			+ getFieldSeparator() +
								forC.getSequenceNumber()				+ getFieldSeparator() +
								forC.getAccountBalance()				+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+
								forC.getRateName()						+ getFieldSeparator() +
								forC.getRateNameCSP()					+ getFieldSeparator() +
								forC.getRateNamePlano()					+ getFieldSeparator() +
								forC.getRateNameArea()					+ getFieldSeparator() +
								forC.getRateNameTipo()					+ getFieldSeparator() +
								forC.getRateNameAd()					+ getFieldSeparator() +
								forC.getRateNameModulacao()				+ getFieldSeparator() +
								forC.getDiscountRule()					+ getFieldSeparator() +
								forC.getDiscount()						+ getFieldSeparator() +
								getFieldSeparator()						+
								forC.getTariffRule()					+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +		
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+
								forC.getDataBalance()					+ getFieldSeparator() +
								forC.getDataBalanceDelta()				+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								forC.getServiceKey()					+ getFieldSeparator() +
								forC.getCorrelationId()					+ getFieldSeparator() +
								forC.getTax()							+ getFieldSeparator() +
								forC.getPeakTime()						+ getFieldSeparator() +
								forC.getSMApplicationType()				+ getFieldSeparator() +
								forC.getBillingEventId()				+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								getFieldSeparator()						+ getFieldSeparator() +
								forC.getTipoCDR()						+ getFieldSeparator() +
								forC.getFormatDateSubmitedClient("dd/MM/yyyy") + getFieldSeparator() +
								forC.getTimeSubmitedClient()			+ getFieldSeparator() +
								forC.getAccountStatus()					+ getFieldSeparator() +
								forC.getCost()							+ getFieldSeparator() +
								forC.getNumberOfVouchers()				+ getFieldSeparator() +
								forC.getVttQueue()						+ getFieldSeparator() +
								forC.getVttTariffPlanId()				+ getFieldSeparator() +
								forC.getInitialAccountBalance()			+ getFieldSeparator() +
								forC.getInitialDataBalance();

		return linhaRetorno+ExportArquivoCDR.RETURN;
	}
}
