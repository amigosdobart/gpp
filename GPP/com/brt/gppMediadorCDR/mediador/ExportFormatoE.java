package com.brt.gppMediadorCDR.mediador;

/*
 * Created on 07/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import com.brt.gppMediadorCDR.formatoArquivos.*;
import java.io.*;

public class ExportFormatoE extends ExportArquivoCDR implements DataConverter{

	public ExportFormatoE(String fileName) throws IOException{
		super(fileName);
	}
	
	public void convertAndWriteFromFormatoA(ArquivoFormatoA forA) throws IOException{
		writeLine(convertFromFormatoA(forA));
	}

	public String convertFromFormatoA(ArquivoFormatoA forA){
		String linhaRetorno =   "" +
								forA.getSequenceNumber() 				+ getFieldSeparator() +
								forA.getSubId()							+ getFieldSeparator() +
								forA.getFormatTimestamp("dd/MM/yyyy")+ " " + forA.getStartTime()	+ getFieldSeparator() +
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
								forA.getDataBalance()					+ getFieldSeparator() +
								forA.getDataBalanceDelta()				+ getFieldSeparator() +
								forA.getCallTypeId()					+ getFieldSeparator() +
								forA.getBonusAmount()					+ getFieldSeparator() +
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
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator() 					+ getFieldSeparator() +
								getFieldSeparator();
								
		return linhaRetorno+ExportArquivoCDR.RETURN;
	}
	
	public void convertAndWriteFromFormatoB(ArquivoFormatoB forB) throws IOException{
		System.out.println("Método não disponível para formato de arquivo E");
	}

	public String convertFromFormatoB(ArquivoFormatoB forB){
		return null;
	}
	
	public void convertAndWriteFromFormatoC(ArquivoFormatoC forC) throws IOException{
		System.out.println("Método não disponível para formato de arquivo E");
	}

	public String convertFromFormatoC(ArquivoFormatoC forC){
		return null;
	}
}
