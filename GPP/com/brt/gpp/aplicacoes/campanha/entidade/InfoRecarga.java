package com.brt.gpp.aplicacoes.campanha.entidade;

public class InfoRecarga
{
	private String msisdn;
	private long qtdeRecargas;
	private double valorRecargas;
	
	public String getMsisdn()
	{
		return msisdn;
	}
	
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	public long getQtdeRecargas()
	{
		return qtdeRecargas;
	}
	
	public void setQtdeRecargas(long qtdeRecargas)
	{
		this.qtdeRecargas = qtdeRecargas;
	}
	
	public double getValorRecargas()
	{
		return valorRecargas;
	}
	
	public void setValorRecargas(double valorRecargas)
	{
		this.valorRecargas = valorRecargas;
	}
	
	public int hashCode()
	{
		return getMsisdn().hashCode();
	}
	
	public String toString()
	{
		return getMsisdn()+" qtd:"+getQtdeRecargas()+" soma:"+getValorRecargas();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof InfoRecarga) )
			return false;
		
		if ( this.getMsisdn().equals(((InfoRecarga)obj).getMsisdn()) )
			return true;
		
		return false;
	}
}

