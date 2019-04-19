
package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações sobre Status de Contestação
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class StatusContestacao 
{
	private int idStatusContestacao;
	private String descStatusContestacao;
	
	/**
	 * @roseuid 404C66B60399
	 */
	public StatusContestacao() 
	{
		
	}
	
	/**
	 * Access method for the idStatusContestacao property.
	 * 
	 * @return   the current value of the idStatusContestacao property
	 */
	public int getIdStatusContestacao() 
	{
		return idStatusContestacao;
		}
	
	/**
	 * Sets the value of the idStatusContestacao property.
	 * 
	 * @param aIdStatusContestacao the new value of the idStatusContestacao property
	 */
	public void setIdStatusContestacao(int aIdStatusContestacao) 
	{
		idStatusContestacao = aIdStatusContestacao;
		}
	
	/**
	 * Access method for the descStatusContestacao property.
	 * 
	 * @return   the current value of the descStatusContestacao property
	 */
	public String getDescStatusContestacao() 
	{
		return descStatusContestacao;
		}
	
	/**
	 * Sets the value of the descStatusContestacao property.
	 * 
	 * @param aDescStatusContestacao the new value of the descStatusContestacao property
	 */
	public void setDescStatusContestacao(String aDescStatusContestacao) 
	{
		descStatusContestacao = aDescStatusContestacao;
		}
}

