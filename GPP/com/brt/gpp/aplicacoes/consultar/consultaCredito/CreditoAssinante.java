package com.brt.gpp.aplicacoes.consultar.consultaCredito;

import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

/**
 *
 *	Classe Vo que contém os saldos dos créditos de um assinante.
 *
 *	@version 	1.0
 *	@author 	Bernardo Pina
 *
 *	@version	1.1		10/04/2007
 *	@author		Daniel Ferreira
 */
public class CreditoAssinante 
{
	Assinante 	assinante;
	int			categoria;
	Date		dtUltimaRecarga;
	double		vlUltimaRecarga;
	String 		tipoTransacao;
	String		sistemaOrigem;
	long 		idProcesso = 0;
	
	public void setAssinante(Assinante assinante) 
	{
		this.assinante = assinante;
	}
	
	public Assinante getAssinante() 
	{
		return this.assinante;
	}
	
	public void setCategoria(int categoria) 
	{
		this.categoria = categoria;
	}
	
	public int getCategoria() 
	{
		return this.categoria;
	}
	
	public Date getDataAtivacao() 
	{
		return this.assinante.getDataAtivacao();
	}
	
	public double getCreditoPrincipal() 
	{
		return this.assinante.getCreditosPrincipal();
	}
	
	public String getDataExpiracaoCreditoPrincipal() 
	{
		return this.assinante.getDataExpiracaoPrincipal();
	}
	
	public double getCreditoPeriodico() 
	{
		return this.assinante.getCreditosPeriodico();
	}
	
	public String getDataExpiracaoCreditoPeriodico() 
	{
		return this.assinante.getDataExpiracaoPeriodico();
	}
	
	public double getCreditoSMS() 
	{
		return this.assinante.getCreditosSms();
	}
	
	public String getDataExpiracaoCreditoSMS() 
	{
		return this.assinante.getDataExpiracaoSms();
	}
	
	public double getCreditoBonus() 
	{
		return this.assinante.getCreditosBonus();
	}
	
	public String getDataExpiracaoCreditoBonus() 
	{
		return this.assinante.getDataExpiracaoBonus();
	}
	
	public double getCreditoDados() 
	{
		return this.assinante.getCreditosDados();
	}
	
	public String getDataExpiracaoCreditoDados() 
	{
		return this.assinante.getDataExpiracaoDados();
	}
	
	public double getSaldoCreditos() 
	{
		double saldoTotal = this.getCreditoBonus() + 
							this.getCreditoDados() + 
							this.getCreditoPrincipal() + 
							this.getCreditoPeriodico() + 
							this.getCreditoSMS();
		
		return saldoTotal;
	}
	
	public void setDataUltimaRecarga(Date dtUltimaRecarga) 
	{
		this.dtUltimaRecarga = dtUltimaRecarga;
	}
	
	public Date getDataUltimaRecarga() 
	{
		return this.dtUltimaRecarga;
	}
	
	public void setValorUltimaRecarga(double vlUltimaRecarga) 
	{
		this.vlUltimaRecarga = vlUltimaRecarga;
	}
	
	public double getValorUltimaRecarga() 
	{
		return this.vlUltimaRecarga;
	}
	
	public void setTipoTransacao(String tipoTransacao) 
	{
		this.tipoTransacao = tipoTransacao;
	}
	
	public String getTipoTransacao() 
	{
		return this.tipoTransacao;
	}
	
	public void setSistemaDeOrigem(String sistemaOrigem) 
	{
		this.sistemaOrigem = sistemaOrigem;
	}
	
	public String getSistemaDeOrigem() 
	{
		return this.sistemaOrigem;
	}
	
}
