package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe que contem informacoes referentes a tabela
 * TBL_PRO_ASS_OFERTA_PCT_DADOS
 * 
 * @author João Paulo Galvagni
 * @since  31/08/2007
 */
public class AssinanteOfertaPacoteDados implements Serializable
{
	private String 				msisdn;
    private OfertaPacoteDados 	ofertaPacoteDados;
    private Date 				dataContratacao;
    private Date 				dataRetiradaOferta;
    private BigDecimal 			valorSaldoTorpedo;
    private BigDecimal 			valorSaldoDados;
    private boolean	   			assinanteSuspenso;
	
	/**
	 * @return the assinanteSuspenso
	 */
	public boolean isAssinanteSuspenso()
	{
		return assinanteSuspenso;
	}
	
	/**
	 * @param assinanteSuspenso the assinanteSuspenso to set
	 */
	public void setAssinanteSuspenso(boolean assinanteSuspenso)
	{
		this.assinanteSuspenso = assinanteSuspenso;
	}
	
	/**
	 * @return the dataContratacao
	 */
	public Date getDataContratacao()
	{
		return dataContratacao;
	}
	
	/**
	 * @param dataContratacao the dataContratacao to set
	 */
	public void setDataContratacao(Date dataContratacao)
	{
		this.dataContratacao = dataContratacao;
	}
	
	/**
	 * @return the dataRetiradaOferta
	 */
	public Date getDataRetiradaOferta()
	{
		return dataRetiradaOferta;
	}
	
	/**
	 * @param dataRetiradaOferta the dataRetiradaOferta to set
	 */
	public void setDataRetiradaOferta(Date dataRetiradaOferta)
	{
		this.dataRetiradaOferta = dataRetiradaOferta;
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
	
	/**
	 * @return the ofertaPacoteDados
	 */
	public OfertaPacoteDados getOfertaPacoteDados()
	{
		return ofertaPacoteDados;
	}
	
	/**
	 * @param ofertaPacoteDados the ofertaPacoteDados to set
	 */
	public void setOfertaPacoteDados(OfertaPacoteDados ofertaPacoteDados)
	{
		this.ofertaPacoteDados = ofertaPacoteDados;
	}
	
	/**
	 * @return the valorSaldoDados
	 */
	public BigDecimal getValorSaldoDados()
	{
		return valorSaldoDados;
	}
	
	/**
	 * @return the doubleValue of valorSaldoDados
	 */
	public double getValorSaldoDadosDouble()
	{
		return valorSaldoDados.doubleValue();
	}
	
	/**
	 * @param valorSaldoDados the valorSaldoDados to set
	 */
	public void setValorSaldoDados(BigDecimal valorSaldoDados)
	{
		this.valorSaldoDados = valorSaldoDados;
	}
	
	/**
	 * @return the valorSaldoTorpedo
	 */
	public BigDecimal getValorSaldoTorpedo()
	{
		return valorSaldoTorpedo;
	}
	
	/**
	 * @return the doubleValue of valorSaldoTorpedo
	 */
	public double getValorSaldoTorpedoDouble()
	{
		return valorSaldoTorpedo.doubleValue();
	}
	
	/**
	 * @param valorSaldoTorpedo the valorSaldoTorpedo to set
	 */
	public void setValorSaldoTorpedo(BigDecimal valorSaldoTorpedo)
	{
		this.valorSaldoTorpedo = valorSaldoTorpedo;
	}
}