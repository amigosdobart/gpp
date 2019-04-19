/*
 * Created on 31/01/2005
 *
 */
package com.brt.gpp.aplicacoes.estornarRetirarCredito;

import java.sql.Timestamp;;

/**
 * 
 * Classe de Informações do Assinante para Estorno de Bônus sobre Bônus.
 *
  * <P> Versao:			1.0
 *  
 * @author Daniel Ferreira
 * Data:   31/01/2005
 */
public class InfoDescontoAssinante 
{

	private String msisdn;
	private double valorDesconto;
	private double saldoBonus;
	private Timestamp timestamp;
	
	public InfoDescontoAssinante()
	{
		msisdn = null;
		valorDesconto = 0;
		saldoBonus = 0;
		timestamp = null;
	}
	
	public String getMsisdn()
	{
		return msisdn;
	}
	
	public double getValorDesconto()
	{
		return valorDesconto;
	}
	
	public double getSaldoBonus()
	{
		return saldoBonus;
	}
	
	public Timestamp getTimestamp()
	{
		return timestamp;
	}
	
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	public void setValorDesconto(double valorDesconto)
	{
		this.valorDesconto = valorDesconto;
	}
	
	public void setSaldoBonus(double saldoBonus)
	{
		this.saldoBonus = saldoBonus;
	}
	
	public void setTimestamp(Timestamp timestamp)
	{
		this.timestamp = timestamp;
	}
	
}
