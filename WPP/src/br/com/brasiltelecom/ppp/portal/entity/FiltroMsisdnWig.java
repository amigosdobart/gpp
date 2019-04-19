package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

public class FiltroMsisdnWig implements FiltroRespostaWig, Serializable
{
	private int codResposta;
	private String mascaraMsisdn;
	private boolean excludente;
	
	public int getCodResposta()
	{
		return codResposta;
	}
	
	public void setCodResposta(int codResposta)
	{
		this.codResposta = codResposta;
	}
	
	public boolean isExcludente()
	{
		return excludente;
	}
	
	public void setExcludente(boolean excludente)
	{
		this.excludente = excludente;
	}
	
	public String getMascaraMsisdn()
	{
		return mascaraMsisdn;
	}
	
	public void setMascaraMsisdn(String mascaraMsisdn)
	{
		this.mascaraMsisdn = mascaraMsisdn;
	}
	
	public String getCabecalhoTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<th>Msisdn</th>");
		linha.append("<th>Excludente</th>");
		linha.append("</tr>");
		
		return linha.toString();
	}
	
	public String getLinhaTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<td>"+getMascaraMsisdn()+"</td>");
		linha.append("<td>"+isExcludente()+"</td>");
		linha.append("</tr>");
		
		return linha.toString();
	}}
