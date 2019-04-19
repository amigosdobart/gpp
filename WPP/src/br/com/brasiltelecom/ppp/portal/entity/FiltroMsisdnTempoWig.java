package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Interface que contem os atributos da tabela WIGC_FILTRO_MSISDN_TEMPO
 * 
 * @author João Paulo Galvagni
 * @since  30/08/2007
 */
public class FiltroMsisdnTempoWig implements FiltroRespostaWig, Serializable
{
	private int 	codResposta;
	private String 	msisdn;
	private Date 	dataInicio;
	private Date 	dataFim;
	
	/**
	 * @return the codResposta
	 */
	public int getCodResposta()
	{
		return codResposta;
	}
	
	/**
	 * @param codResposta the codResposta to set
	 */
	public void setCodResposta(int codResposta)
	{
		this.codResposta = codResposta;
	}
	
	/**
	 * @return the dataFim
	 */
	public Date getDataFim()
	{
		return dataFim;
	}
	
	/**
	 * @param dataFim the dataFim to set
	 */
	public void setDataFim(Date dataFim)
	{
		this.dataFim = dataFim;
	}
	
	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio()
	{
		return dataInicio;
	}
	
	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio)
	{
		this.dataInicio = dataInicio;
	}
	
	/**
	 * @return the msisdn
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	public String getCabecalhoTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<th>Codigo da Resposta</th>");
		linha.append("<th>Msisdn</th>");
		linha.append("<th>Data inicio</th>");
		linha.append("<th>Data Fim</th>");
		linha.append("</tr>");
		
		return linha.toString();
	}
	
	public String getLinhaTabelaHTML()
	{
		StringBuffer linha = new StringBuffer("<tr>");
		linha.append("<td>"+getCodResposta()+"</td>");
		linha.append("<td>"+getMsisdn()+"</td>");
		linha.append("<td>"+getDataInicio()+"</td>");
		linha.append("<td>"+getDataFim()+"</td>");
		linha.append("</tr>");
		
		return linha.toString();
	}
}