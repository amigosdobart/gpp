package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

public class Rating implements Serializable
{
	public static final String KILOBYTES = "K";
	public static final String SEGUNDOS = "S";
	
	private String desOperacao = null;
	private String rateName = null;
	private String idtTarifavel = null;
	private String tipRate = null;
	private BigDecimal vlrFatorConversao = null;
	
	public Rating()
	{
		
	}

	public String getDesOperacao() 
	{
		return desOperacao;
	}

	public void setDesOperacao(String desOperacao) 
	{
		this.desOperacao = desOperacao;
	}

	public String getRateName() 
	{
		return rateName;
	}

	public void setRateName(String rateName) 
	{
		this.rateName = rateName;
	}

	public String isIdtTarifavel() 
	{
		return idtTarifavel;
	}

	public void setIdtTarifavel(String idtTarifavel) 
	{
		this.idtTarifavel = idtTarifavel;
	}

	public String getTipRate() 
	{
		return tipRate;
	}

	public void setTipRate(String tipRate) 
	{
		this.tipRate = tipRate;
	}

	public BigDecimal getVlrFatorConversao() 
	{
		return vlrFatorConversao;
	}

	public void setVlrFatorConversao(BigDecimal vlrFatorConversao) 
	{
		this.vlrFatorConversao = vlrFatorConversao;
	}
}
