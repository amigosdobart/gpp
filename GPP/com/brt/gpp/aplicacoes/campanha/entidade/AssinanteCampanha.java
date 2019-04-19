package com.brt.gpp.aplicacoes.campanha.entidade;

import java.sql.Date;
import java.util.Map;

import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;

/**
 * Esta classe armazena as informacoes da inscricao de assinantes em campanhas 
 * promocionais. Estas informacoes serao utilizadas para tratamento da concessao de 
 * creditos.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class AssinanteCampanha 
{
	private String msisdn;
	private Date dataInclusao;
	private Date dataRetiradaCampanha;
	private Date dataUltimoSMS;
	private Campanha campanha;
	private Map parametros;
	
	/**
	 * Metodo....:getparametros
	 * Descricao.:Retorna o valor de parametros
	 * @return parametros.
	 */
	public Map getParametros()
	{
		return parametros;
	}

	/**
	 * Metodo....:setparametros
	 * Descricao.:Define o valor de parametros
	 * @param parametros o valor a ser definido para parametros
	 */
	public void setParametros(Map parametros)
	{
		this.parametros = parametros;
	}

	/**
	 * Access method for the msisdn property.
	 * 
	 * @return   the current value of the msisdn property
	 */
	public String getMsisdn() 
	{
		return msisdn;    
	}
	
	/**
	 * Sets the value of the msisdn property.
	 * 
	 * @param aMsisdn the new value of the msisdn property
	 */
	public void setMsisdn(String aMsisdn) 
	{
		msisdn = aMsisdn;    
	}
	
	/**
	 * Access method for the dataInclusao property.
	 * 
	 * @return   the current value of the dataInclusao property
	 */
	public Date getDataInclusao() 
	{
		return dataInclusao;    
	}
	
	/**
	 * Sets the value of the dataInclusao property.
	 * 
	 * @param aDataInclusao the new value of the dataInclusao property
	 */
	public void setDataInclusao(Date aDataInclusao) 
	{
		dataInclusao = aDataInclusao;    
	}
	
	/**
	 * Sets the value of the dataUltimoSMS property.
	 * 
	 * @param aDataUltimoSMS the new value of the dataInclusao property
	 */
	public void setDataUltimoSMS(Date aDataUltimoSMS) 
	{
		dataUltimoSMS = aDataUltimoSMS;    
	}
	
	/**
	 * Access method for the dataUltimoSMS property.
	 * 
	 * @return   the current value of the dataUltimoSMS property
	 */
	public Date getDataUltimoSMS() 
	{
		return dataUltimoSMS;    
	}
	
	/**
	 * Access method for the dataRetiradaCampanha property.
	 * 
	 * @return   the current value of the dataRetiradaCampanha property
	 */
	public Date getDataRetiradaCampanha() 
	{
		return dataRetiradaCampanha;    
	}
	
	/**
	 * Sets the value of the dataRetiradaCampanha property.
	 * 
	 * @param aDataRetiradaCampanha the new value of the dataRetiradaCampanha property
	 */
	public void setDataRetiradaCampanha(Date aDataRetiradaCampanha) 
	{
		dataRetiradaCampanha = aDataRetiradaCampanha;    
	}
	
	/**
	 * Access method for the campanha property.
	 * 
	 * @return   the current value of the campanha property
	 */
	public Campanha getCampanha() 
	{
		return campanha;    
	}
	
	/**
	 * Sets the value of the campanha property.
	 * 
	 * @param aCampanha the new value of the campanha property
	 */
	public void setCampanha(Campanha aCampanha) 
	{
		campanha = aCampanha;
	}
}
