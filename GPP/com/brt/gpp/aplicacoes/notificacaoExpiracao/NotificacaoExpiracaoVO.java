package com.brt.gpp.aplicacoes.notificacaoExpiracao;

import java.util.Date;


public class NotificacaoExpiracaoVO 
{
	
	private String msisdn; 
	private String notificacaoSMS;
	private int prioridade;
	private String smsStatus;
	private Date   dataExpiracaoPrincipal;
	private String valorSaldoPrincipal;
	private String valorSaldoBonus;
	private String valorSaldoSM;
	private String valorSaldoDados;
	private String nDias;
	

	public NotificacaoExpiracaoVO() 
	{
		super();
	}

	
	public Date getDataExpiracaoPrincipal()
	{
		return dataExpiracaoPrincipal;
	}

	
	public void setDataExpiracaoPrincipal(Date dataExpiracaoPrincipal) 
	{
		this.dataExpiracaoPrincipal = dataExpiracaoPrincipal;
	}

	
	public String getMsisdn() 
	{
		return msisdn;
	}

	
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	
	public String getNotificacaoSMS()
	{
		return notificacaoSMS;
	}

	
	public void setNotificacaoSMS(String notificacaoSMS)
	{
		this.notificacaoSMS = notificacaoSMS;
	}

	
	public int getPrioridade()
	{
		return prioridade;
	}

	
	public void setPrioridade(int prioridade)
	{
		this.prioridade = prioridade;
	}

	
	public String getSmsStatus()
	{
		return smsStatus;
	}

	
	public void setSmsStatus(String smsStatus)
	{
		this.smsStatus = smsStatus;
	}

	
	public String getValorSaldoBonus()
	{
		return valorSaldoBonus;
	}

	
	public void setValorSaldoBonus(String valorSaldoBonus)
	{
		this.valorSaldoBonus = valorSaldoBonus;
	}

	
	public String getValorSaldoDados()
	{
		return valorSaldoDados;
	}

	
	public void setValorSaldoDados(String valorSaldoDados) 
	{
		this.valorSaldoDados = valorSaldoDados;
	}

	
	public String getValorSaldoPrincipal()
	{
		return valorSaldoPrincipal;
	}

	
	public void setValorSaldoPrincipal(String valorSaldoPrincipal)
	{
		this.valorSaldoPrincipal = valorSaldoPrincipal;
	}

	
	public String getValorSaldoSM()
	{
		return valorSaldoSM;
	}

	
	public void setValorSaldoSM(String valorSaldoSM)
	{
		this.valorSaldoSM = valorSaldoSM;
	}
	
	
	public String getNDias()
	{
		return nDias;
	}

	
	public void setNDias(String dias) 
	{
		nDias = dias;
	}

}
