package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações sobre financeira
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Financeira 
{
	private int idFinanceira;
	private String descFinanceira;
	private TipoFinanceira tipoFinanceira;
	
	/**
	 * @roseuid 404376930290
	 */
	public Financeira() 
	{
		
	}
	
	/**
	 * Access method for the idFinanceira property.
	 * 
	 * @return   the current value of the idFinanceira property
	 */
	public int getIdFinanceira() 
	{
		return idFinanceira;
		}
	
	/**
	 * Sets the value of the idFinanceira property.
	 * 
	 * @param aIdFinanceira the new value of the idFinanceira property
	 */
	public void setIdFinanceira(int aIdFinanceira) 
	{
		idFinanceira = aIdFinanceira;
		}
	
	/**
	 * Access method for the descFinanceira property.
	 * 
	 * @return   the current value of the descFinanceira property
	 */
	public String getDescFinanceira() 
	{
		return descFinanceira;
		}
	
	/**
	 * Sets the value of the descFinanceira property.
	 * 
	 * @param aDescFinanceira the new value of the descFinanceira property
	 */
	public void setDescFinanceira(String aDescFinanceira) 
	{
		descFinanceira = aDescFinanceira;
		}
	
	/**
	 * Access method for the tipoFinanceira property.
	 * 
	 * @return   the current value of the tipoFinanceira property
	 */
	public TipoFinanceira getTipoFinanceira() 
	{
		return tipoFinanceira;
		}
	
	/**
	 * Sets the value of the tipoFinanceira property.
	 * 
	 * @param aTipoFinanceira the new value of the tipoFinanceira property
	 */
	public void setTipoFinanceira(TipoFinanceira aTipoFinanceira) 
	{
		tipoFinanceira = aTipoFinanceira;
		}
}
