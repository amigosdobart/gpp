package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

public class AssinanteControle
{
	String 	msisdn;
	double	valorCredito;
	int		numMes;
	Date	dataRecarga;
	Date 	dataAtivacaoGPP;
	String	numContrato;
	String	indNovoControle;
	
	// Gets
	public Date getDataAtivacaoGPP()
	{
		return dataAtivacaoGPP;
	}
	public Date getDataRecarga()
	{
		return dataRecarga;
	}
	public String getIndNovoControle()
	{
		return indNovoControle;
	}
	public String getMsisdn()
	{
		return msisdn;
	}
	public String getNumContrato()
	{
		return numContrato;
	}
	public int getNumMes()
	{
		return numMes;
	}
	public double getValorCredito()
	{
		return valorCredito;
	}
	
	// Sets
	public void setDataAtivacaoGPP(Date dataAtivacaoGPP)
	{
		this.dataAtivacaoGPP = dataAtivacaoGPP;
	}
	public void setDataRecarga(Date dataRecarga)
	{
		this.dataRecarga = dataRecarga;
	}
	public void setIndNovoControle(String indNovoControle)
	{
		this.indNovoControle = indNovoControle;
	}
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	public void setNumContrato(String numContrato)
	{
		this.numContrato = numContrato;
	}
	public void setNumMes(int numMes)
	{
		this.numMes = numMes;
	}
	public void setValorCredito(double valorCredito)
	{
		this.valorCredito = valorCredito;
	}		
}
