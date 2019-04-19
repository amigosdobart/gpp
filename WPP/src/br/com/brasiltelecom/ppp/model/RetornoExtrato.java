/*
 * Created on 08/04/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

/**
 * Modela as informações sobre Retorno de extrato
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class RetornoExtrato {

	private Collection extratos;
	private Collection eventos;
	
	private String saldoInicialPrincipal;
	private String saldoFinalPrincipal;
	private String totalDebitosPrincipal;
	private String totalCreditosPrincipal;
	
	private String saldoInicialBonus;
	private String saldoFinalBonus;
	private String totalDebitosBonus;
	private String totalCreditosBonus;

	private String saldoInicialGPRS;
	private String saldoFinalGPRS;
	private String totalDebitosGPRS;
	private String totalCreditosGPRS;

	private String saldoInicialSMS;
	private String saldoFinalSMS;
	private String totalDebitosSMS;
	private String totalCreditosSMS;

	private String saldoInicialPeriodico;
	private String saldoFinalPeriodico;
	private String totalDebitosPeriodico;
	private String totalCreditosPeriodico;
	
	private String saldoInicialTotal;
	private String saldoFinalTotal;
	private String totalDebitosTotal;
	private String totalCreditosTotal;
	
	private String indAssinanteLigMix;
	private String planoPreco;
	
	private String dataAtivacao;
	private String plano;
	private DecimalFormat df = new DecimalFormat("##,##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
	private DecimalFormat dfBrasil = new DecimalFormat("##,##0.00", new DecimalFormatSymbols(new Locale("BR", "pt", "")));

	/**
	 * @return Returns the saldoFinalBonus.
	 */
	public String getSaldoFinalBonus() {
		return saldoFinalBonus;
	}
	/**
	 * @param saldoFinalBonus The saldoFinalBonus to set.
	 */
	public void setSaldoFinalBonus(String saldoFinalBonus) {
		this.saldoFinalBonus = saldoFinalBonus;
	}
	/**
	 * @return Returns the saldoFinalGPRS.
	 */
	public String getSaldoFinalGPRS() {
		return saldoFinalGPRS;
	}
	/**
	 * @param saldoFinalGPRS The saldoFinalGPRS to set.
	 */
	public void setSaldoFinalGPRS(String saldoFinalGPRS) {
		this.saldoFinalGPRS = saldoFinalGPRS;
	}
	/**
	 * @return Returns the saldoFinalPrincipal.
	 */
	public String getSaldoFinalPrincipal() {
		return saldoFinalPrincipal;
	}
	/**
	 * @param saldoFinalPrincipal The saldoFinalPrincipal to set.
	 */
	public void setSaldoFinalPrincipal(String saldoFinalPrincipal) {
		this.saldoFinalPrincipal = saldoFinalPrincipal;
	}
	/**
	 * @return Returns the saldoFinalSMS.
	 */
	public String getSaldoFinalSMS() {
		return saldoFinalSMS;
	}
	/**
	 * @param saldoFinalSMS The saldoFinalSMS to set.
	 */
	public void setSaldoFinalSMS(String saldoFinalSMS) {
		this.saldoFinalSMS = saldoFinalSMS;
	}
	/**
	 * @return Returns the saldoInicialBonus.
	 */
	public String getSaldoInicialBonus() {
		return saldoInicialBonus;
	}
	/**
	 * @param saldoInicialBonus The saldoInicialBonus to set.
	 */
	public void setSaldoInicialBonus(String saldoInicialBonus) {
		this.saldoInicialBonus = saldoInicialBonus;
	}
	/**
	 * @return Returns the saldoInicialGPRS.
	 */
	public String getSaldoInicialGPRS() {
		return saldoInicialGPRS;
	}
	/**
	 * @param saldoInicialGPRS The saldoInicialGPRS to set.
	 */
	public void setSaldoInicialGPRS(String saldoInicialGPRS) {
		this.saldoInicialGPRS = saldoInicialGPRS;
	}
	/**
	 * @return Returns the saldoInicialPrincipal.
	 */
	public String getSaldoInicialPrincipal() {
		return saldoInicialPrincipal;
	}
	/**
	 * @param saldoInicialPrincipal The saldoInicialPrincipal to set.
	 */
	public void setSaldoInicialPrincipal(String saldoInicialPrincipal) {
		this.saldoInicialPrincipal = saldoInicialPrincipal;
	}
	/**
	 * @return Returns the saldoInicialSMS.
	 */
	public String getSaldoInicialSMS() {
		return saldoInicialSMS;
	}
	/**
	 * @param saldoInicialSMS The saldoInicialSMS to set.
	 */
	public void setSaldoInicialSMS(String saldoInicialSMS) {
		this.saldoInicialSMS = saldoInicialSMS;
	}
	/**
	 * @return Returns the totalCreditosBonus.
	 */
	public String getTotalCreditosBonus() {
		return totalCreditosBonus;
	}
	/**
	 * @param totalCreditosBonus The totalCreditosBonus to set.
	 */
	public void setTotalCreditosBonus(String totalCreditosBonus) {
		this.totalCreditosBonus = totalCreditosBonus;
	}
	/**
	 * @return Returns the totalCreditosGPRS.
	 */
	public String getTotalCreditosGPRS() {
		return totalCreditosGPRS;
	}
	/**
	 * @param totalCreditosGPRS The totalCreditosGPRS to set.
	 */
	public void setTotalCreditosGPRS(String totalCreditosGPRS) {
		this.totalCreditosGPRS = totalCreditosGPRS;
	}
	/**
	 * @return Returns the totalCreditosPrincipal.
	 */
	public String getTotalCreditosPrincipal() {
		return totalCreditosPrincipal;
	}
	/**
	 * @param totalCreditosPrincipal The totalCreditosPrincipal to set.
	 */
	public void setTotalCreditosPrincipal(String totalCreditosPrincipal) {
		this.totalCreditosPrincipal = totalCreditosPrincipal;
	}
	/**
	 * @return Returns the totalCreditosSMS.
	 */
	public String getTotalCreditosSMS() {
		return totalCreditosSMS;
	}
	/**
	 * @param totalCreditosSMS The totalCreditosSMS to set.
	 */
	public void setTotalCreditosSMS(String totalCreditosSMS) {
		this.totalCreditosSMS = totalCreditosSMS;
	}
	/**
	 * @return Returns the totalDebitosBonus.
	 */
	public String getTotalDebitosBonus() {
		return totalDebitosBonus;
	}
	/**
	 * @param totalDebitosBonus The totalDebitosBonus to set.
	 */
	public void setTotalDebitosBonus(String totalDebitosBonus) {
		this.totalDebitosBonus = totalDebitosBonus;
	}
	/**
	 * @return Returns the totalDebitosGPRS.
	 */
	public String getTotalDebitosGPRS() {
		return totalDebitosGPRS;
	}
	/**
	 * @param totalDebitosGPRS The totalDebitosGPRS to set.
	 */
	public void setTotalDebitosGPRS(String totalDebitosGPRS) {
		this.totalDebitosGPRS = totalDebitosGPRS;
	}
	/**
	 * @return Returns the totalDebitosPrincipal.
	 */
	public String getTotalDebitosPrincipal() {
		return totalDebitosPrincipal;
	}
	/**
	 * @param totalDebitosPrincipal The totalDebitosPrincipal to set.
	 */
	public void setTotalDebitosPrincipal(String totalDebitosPrincipal) {
		this.totalDebitosPrincipal = totalDebitosPrincipal;
	}
	/**
	 * @return Returns the totalDebitosSMS.
	 */
	public String getTotalDebitosSMS() {
		return totalDebitosSMS;
	}
	/**
	 * @param totalDebitosSMS The totalDebitosSMS to set.
	 */
	public void setTotalDebitosSMS(String totalDebitosSMS) {
		this.totalDebitosSMS = totalDebitosSMS;
	}
	/**
	 * @return
	 */
	public Collection getEventos() {
		return eventos;
	}

	/**
	 * @return
	 */
	public Collection getExtratos() {
		return extratos;
	}


	/**
	 * @param collection
	 */
	public void setEventos(Collection collection) {
		eventos = collection;
	}

	/**
	 * @param collection
	 */
	public void setExtratos(Collection collection) {
		extratos = collection;
	}

	//***************************************************************
	public String getSaldoFinalPrincipalString()
	{
		if (saldoFinalPrincipal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoFinalPrincipal));
		    }
		    catch(ParseException e)
		    {
		        return saldoFinalPrincipal;
		    }
		} 
		else 
		{
			return saldoFinalPrincipal;
		}			
	}
	
	public String getSaldoInicialPrincipalString() 
	{
		if (saldoInicialPrincipal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoInicialPrincipal));
		    }
		    catch(ParseException e)
		    {
		        return saldoInicialPrincipal;
		    }
		} 
		else 
		{
			return saldoInicialPrincipal;
		}
	}
	
	public String getTotalCreditosPrincipalString() 
	{
		if (totalCreditosPrincipal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalCreditosPrincipal));
		    }
		    catch(ParseException e)
		    {
		        return totalCreditosPrincipal;
		    }
		} 
		else 
		{
			return totalCreditosPrincipal;
		}
	}
	
	public String getTotalDebitosPrincipalString() 
	{
		if (totalDebitosPrincipal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalDebitosPrincipal));
		    }
		    catch(ParseException e)
		    {
		        return totalDebitosPrincipal;
		    }
		} 
		else 
		{
			return totalDebitosPrincipal;
		}
	}

	//***************************************************************
	public String getSaldoFinalBonusString() 
	{
		if (saldoFinalBonus != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoFinalBonus));
		    }
		    catch(ParseException e)
		    {
		        return saldoFinalBonus;
		    }
		} 
		else 
		{
			return saldoFinalBonus;
		}			
	}
	
	public String getSaldoInicialBonusString() 
	{
		if (saldoInicialBonus != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoInicialBonus));
		    }
		    catch(ParseException e)
		    {
		        return saldoInicialBonus;
		    }
		} 
		else 
		{
			return saldoInicialBonus;
		}
	}
	
	public String getTotalCreditosBonusString() 
	{
		if (totalCreditosBonus != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalCreditosBonus));
		    }
		    catch(ParseException e)
		    {
		        return totalCreditosBonus;
		    }
		} 
		else 
		{
			return totalCreditosBonus;
		}
	}
	
	public String getTotalDebitosBonusString() 
	{
		if (totalDebitosBonus != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalDebitosBonus));
		    }
		    catch(ParseException e)
		    {
		        return totalDebitosBonus;
		    }
		} 
		else 
		{
			return totalDebitosBonus;
		}
	}

	//***************************************************************
	public String getSaldoFinalGPRSString()
	{
		if (saldoFinalGPRS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoFinalGPRS));
		    }
		    catch(ParseException e)
		    {
		        return saldoFinalGPRS;
		    }
		} 
		else 
		{
			return saldoFinalGPRS;
		}			
	}
	
	public String getSaldoInicialGPRSString() 
	{
		if (saldoInicialGPRS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoInicialGPRS));
		    }
		    catch(ParseException e)
		    {
		        return saldoInicialGPRS;
		    }
		} 
		else 
		{
			return saldoInicialGPRS;
		}
	}
	
	public String getTotalCreditosGPRSString()
	{
		if (totalCreditosGPRS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalCreditosGPRS));
		    }
		    catch(ParseException e)
		    {
		        return totalCreditosGPRS;
		    }
		} 
		else 
		{
			return totalCreditosGPRS;
		}
	}
	
	public String getTotalDebitosGPRSString()
	{
		if (totalDebitosGPRS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalDebitosGPRS));
		    }
		    catch(ParseException e)
		    {
		        return totalDebitosGPRS;
		    }
		} 
		else 
		{
			return totalDebitosGPRS;
		}
	}
	
	//***************************************************************
	public String getSaldoFinalSMSString()
	{
		if (saldoFinalSMS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoFinalSMS));
		    }
		    catch(ParseException e)
		    {
		        return saldoFinalSMS;
		    }
		} 
		else 
		{
			return saldoFinalSMS;
		}			
	}
	
	public String getSaldoInicialSMSString()
	{
		if (saldoInicialSMS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoInicialSMS));
		    }
		    catch(ParseException e)
		    {
		        return saldoInicialSMS;
		    }
		} 
		else 
		{
			return saldoInicialSMS;
		}
	}
	
	public String getTotalCreditosSMSString()
	{
		if (totalCreditosSMS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalCreditosSMS));
		    }
		    catch(ParseException e)
		    {
		        return totalCreditosSMS;
		    }
		} 
		else 
		{
			return totalCreditosSMS;
		}
	}
	
	public String getTotalDebitosSMSString()
	{
		if (totalDebitosSMS != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalDebitosSMS));
		    }
		    catch(ParseException e)
		    {
		        return totalDebitosSMS;
		    }
		} 
		else 
		{
			return totalDebitosSMS;
		}
	}
	//***************************************************************
	public String getSaldoFinalTotalString()
	{
		if (saldoFinalTotal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoFinalTotal));
		    }
		    catch(ParseException e)
		    {
		        return saldoFinalTotal;
		    }
		} 
		else 
		{
			return saldoFinalTotal;
		}			
	}
	
	public String getSaldoInicialTotalString()
	{
		if (saldoInicialTotal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoInicialTotal));
		    }
		    catch(ParseException e)
		    {
		        return saldoInicialTotal;
		    }
		} 
		else 
		{
			return saldoInicialTotal;
		}
	}
	
	public String getTotalCreditosTotalString()
	{
		if (totalCreditosTotal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalCreditosTotal));
		    }
		    catch(ParseException e)
		    {
		        return totalCreditosTotal;
		    }
		} 
		else 
		{
			return totalCreditosTotal;
		}
	}
	
	public String getTotalDebitosTotalString()
	{
		if (totalDebitosTotal != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalDebitosTotal));
		    }
		    catch(ParseException e)
		    {
		        return totalDebitosTotal;
		    }
		} 
		else 
		{
			return totalDebitosTotal;
		}
	}


	/**
	 * @return
	 */
	public String getDataAtivacao() {
		return dataAtivacao;
	}

	/**
	 * @return
	 */
	public String getPlano() {
		return plano;
	}

	/**
	 * @param string
	 */
	public void setDataAtivacao(String string) {
		dataAtivacao = string;
	}

	/**
	 * @param string
	 */
	public void setPlano(String string) {
		plano = string;
	}

	/**
	 * @return Returns the saldoFinalTotal.
	 */
	public String getSaldoFinalTotal() {
		return saldoFinalTotal;
	}
	/**
	 * @param saldoFinalTotal The saldoFinalTotal to set.
	 */
	public void setSaldoFinalTotal(String saldoFinalTotal) {
		this.saldoFinalTotal = saldoFinalTotal;
	}
	/**
	 * @return Returns the saldoInicialTotal.
	 */
	public String getSaldoInicialTotal() {
		return saldoInicialTotal;
	}
	/**
	 * @param saldoInicialTotal The saldoInicialTotal to set.
	 */
	public void setSaldoInicialTotal(String saldoInicialTotal) {
		this.saldoInicialTotal = saldoInicialTotal;
	}
	/**
	 * @return Returns the totalCreditosTotal.
	 */
	public String getTotalCreditosTotal() {
		return totalCreditosTotal;
	}
	/**
	 * @param totalCreditosTotal The totalCreditosTotal to set.
	 */
	public void setTotalCreditosTotal(String totalCreditosTotal) {
		this.totalCreditosTotal = totalCreditosTotal;
	}
	/**
	 * @return Returns the totalDebitosTotal.
	 */
	public String getTotalDebitosTotal() {
		return totalDebitosTotal;
	}
	/**
	 * @param totalDebitosTotal The totalDebitosTotal to set.
	 */
	public void setTotalDebitosTotal(String totalDebitosTotal) {
		this.totalDebitosTotal = totalDebitosTotal;
	}
	
	/**
	 * @return Returns the indAssinanteLigMix.
	 */
	public String getIndAssinanteLigMix() {
		return indAssinanteLigMix;
	}
	/**
	 * @param indAssinanteLigMix The indAssinanteLigMix to set.
	 */
	public void setIndAssinanteLigMix(String indAssinanteLigMix) {
		this.indAssinanteLigMix = indAssinanteLigMix;
	}
	/**
	 * @return the saldoFinalPeriodico
	 */
	public String getSaldoFinalPeriodico()
	{
		return saldoFinalPeriodico;
	}
	/**
	 * @param saldoFinalPeriodico the saldoFinalPeriodico to set
	 */
	public void setSaldoFinalPeriodico(String saldoFinalPeriodico)
	{
		this.saldoFinalPeriodico = saldoFinalPeriodico;
	}
	/**
	 * @return the saldoInicialPeriodico
	 */
	public String getSaldoInicialPeriodico()
	{
		return saldoInicialPeriodico;
	}
	/**
	 * @param saldoInicialPeriodico the saldoInicialPeriodico to set
	 */
	public void setSaldoInicialPeriodico(String saldoInicialPeriodico)
	{
		this.saldoInicialPeriodico = saldoInicialPeriodico;
	}
	/**
	 * @return the totalCreditosPeriodico
	 */
	public String getTotalCreditosPeriodico()
	{
		return totalCreditosPeriodico;
	}
	/**
	 * @param totalCreditosPeriodico the totalCreditosPeriodico to set
	 */
	public void setTotalCreditosPeriodico(String totalCreditosPeriodico)
	{
		this.totalCreditosPeriodico = totalCreditosPeriodico;
	}
	/**
	 * @return the totalDebitosPeriodico
	 */
	public String getTotalDebitosPeriodico()
	{
		return totalDebitosPeriodico;
	}
	/**
	 * @param totalDebitosPeriodico the totalDebitosPeriodico to set
	 */
	public void setTotalDebitosPeriodico(String totalDebitosPeriodico)
	{
		this.totalDebitosPeriodico = totalDebitosPeriodico;
	}
	
	public String getSaldoInicialPeriodicoString()
	{
		if (saldoInicialPeriodico != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoInicialPeriodico));
		    }
		    catch(ParseException e)
		    {
		        return saldoInicialPeriodico;
		    }
		} 
		else 
		{
			return saldoInicialPeriodico;
		}
	}
	public String getSaldoFinalPeriodicoString()
	{
		if (saldoFinalPeriodico != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(saldoFinalPeriodico));
		    }
		    catch(ParseException e)
		    {
		        return saldoFinalPeriodico;
		    }
		} 
		else 
		{
			return saldoFinalPeriodico;
		}
	}
	public String getTotalDebitosPeriodicoString()
	{
		if (totalDebitosPeriodico != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalDebitosPeriodico));
		    }
		    catch(ParseException e)
		    {
		        return totalDebitosPeriodico;
		    }
		} 
		else 
		{
			return totalDebitosPeriodico;
		}
	}
	public String getTotalCreditosPeriodicoString()
	{
		if (totalCreditosPeriodico != null)
		{
		    try
		    {
				return dfBrasil.format(df.parse(totalCreditosPeriodico));
		    }
		    catch(ParseException e)
		    {
		        return totalCreditosPeriodico;
		    }
		} 
		else 
		{
			return totalCreditosPeriodico;
		}
	}
	/**
	 * @return the planoPreco
	 */
	public String getPlanoPreco()
	{
		return planoPreco;
	}
	/**
	 * @param planoPreco the planoPreco to set
	 */
	public void setPlanoPreco(String planoPreco)
	{
		this.planoPreco = planoPreco;
	}
}
