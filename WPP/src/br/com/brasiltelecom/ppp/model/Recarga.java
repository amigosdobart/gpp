
package br.com.brasiltelecom.ppp.model;

import java.util.Date;

/**
 * Modela as informações sobre Recarga
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Recarga 
{
	private Date dataEmissao;
	private String emissor;
	private int msisdn;
	private String status;
	private double valor;
	private Financeira financeira;
	
	/**
	 * @roseuid 40437693034B
	 */
	public Recarga() 
	{
		
	}
	
	/**
	 * Access method for the dataEmissao property.
	 * 
	 * @return   the current value of the dataEmissao property
	 */
	public java.util.Date getDataEmissao() 
	{
		return dataEmissao;
		}
	
	/**
	 * Sets the value of the dataEmissao property.
	 * 
	 * @param aDataEmissao the new value of the dataEmissao property
	 */
	public void setDataEmissao(java.util.Date aDataEmissao) 
	{
		dataEmissao = aDataEmissao;
		}
	
	/**
	 * Access method for the emissor property.
	 * 
	 * @return   the current value of the emissor property
	 */
	public String getEmissor() 
	{
		return emissor;
		}
	
	/**
	 * Sets the value of the emissor property.
	 * 
	 * @param aEmissor the new value of the emissor property
	 */
	public void setEmissor(String aEmissor) 
	{
		emissor = aEmissor;
		}
	
	/**
	 * Access method for the msisdn property.
	 * 
	 * @return   the current value of the msisdn property
	 */
	public int getMsisdn() 
	{
		return msisdn;
		}
	
	/**
	 * Sets the value of the msisdn property.
	 * 
	 * @param aMsisdn the new value of the msisdn property
	 */
	public void setMsisdn(int aMsisdn) 
	{
		msisdn = aMsisdn;
		}
	
	/**
	 * Access method for the status property.
	 * 
	 * @return   the current value of the status property
	 */
	public String getStatus() 
	{
		return status;
		}
	
	/**
	 * Sets the value of the status property.
	 * 
	 * @param aStatus the new value of the status property
	 */
	public void setStatus(String aStatus) 
	{
		status = aStatus;
		}
	
	/**
	 * Access method for the valor property.
	 * 
	 * @return   the current value of the valor property
	 */
	public double getValor() 
	{
		return valor;
		}
	
	/**
	 * Sets the value of the valor property.
	 * 
	 * @param aValor the new value of the valor property
	 */
	public void setValor(double aValor) 
	{
		valor = aValor;
		}
	
	/**
	 * Access method for the financeira property.
	 * 
	 * @return   the current value of the financeira property
	 */
	public Financeira getFinanceira() 
	{
		return financeira;
		}
	
	/**
	 * Sets the value of the financeira property.
	 * 
	 * @param aFinanceira the new value of the financeira property
	 */
	public void setFinanceira(Financeira aFinanceira) 
	{
		financeira = aFinanceira;
		}
}
