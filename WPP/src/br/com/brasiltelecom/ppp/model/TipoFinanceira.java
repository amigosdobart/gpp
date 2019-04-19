
package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações sobre tipo de financeira
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class TipoFinanceira 
{
	private int idTipoFinanceira;
	private String descTipoFinanceira;
	
	/**
	 * @roseuid 4043769301F4
	 */
	public TipoFinanceira() 
	{
		
	}
	
	/**
	 * Access method for the idTipoFinanceira property.
	 * 
	 * @return   the current value of the idTipoFinanceira property
	 */
	public int getIdTipoFinanceira() 
	{
		return idTipoFinanceira;
		}
	
	/**
	 * Sets the value of the idTipoFinanceira property.
	 * 
	 * @param aIdTipoFinanceira the new value of the idTipoFinanceira property
	 */
	public void setIdTipoFinanceira(int aIdTipoFinanceira) 
	{
		idTipoFinanceira = aIdTipoFinanceira;
		}
	
	/**
	 * Access method for the descTipoFinanceira property.
	 * 
	 * @return   the current value of the descTipoFinanceira property
	 */
	public String getDescTipoFinanceira() 
	{
		return descTipoFinanceira;
		}
	
	/**
	 * Sets the value of the descTipoFinanceira property.
	 * 
	 * @param aDescTipoFinanceira the new value of the descTipoFinanceira property
	 */
	public void setDescTipoFinanceira(String aDescTipoFinanceira) 
	{
		descTipoFinanceira = aDescTipoFinanceira;
		}
}
