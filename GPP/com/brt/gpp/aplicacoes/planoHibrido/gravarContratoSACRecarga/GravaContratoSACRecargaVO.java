package com.brt.gpp.aplicacoes.planoHibrido.gravarContratoSACRecarga;

import java.sql.Timestamp;

public class GravaContratoSACRecargaVO
{
	private String 		msisdn;
	private long 		idContrato;
	private Timestamp 	dataOrigem;
	
	public void setMsisdn(String nuMsisdn)
	{
		this.msisdn = nuMsisdn;
	}
	
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	public void setIdContrato(long contrato)
	{
		this.idContrato = contrato;
	}
	
	public long getIdContrato()
	{
		return this.idContrato;
	}

	public Timestamp getDataOrigem()
	{
		return this.dataOrigem;
	}
	
	public void setDataOrigem(Timestamp dataOrigemGPP)
	{
		this.dataOrigem = dataOrigemGPP;
	}
}