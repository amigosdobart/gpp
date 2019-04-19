package com.brt.gpp.aplicacoes.concederCreditoTerceiro;

public class ConcessaoCreditoTerceiroVO 
{
	String msisdn;
	String plano;
		
	public ConcessaoCreditoTerceiroVO(String msisdn, String plano) 
	{
		this.msisdn = msisdn;
		this.plano = plano;
	}
	public String getMsisdn() 
	{
		return msisdn;
	}
	public String getPlano() 
	{
		return plano;
	}
	public void setMsisdn(String msisdn) 
	{
		this.msisdn = msisdn;
	}
	public void setPlano(String plano) 
	{
		this.plano = plano;
	}
}