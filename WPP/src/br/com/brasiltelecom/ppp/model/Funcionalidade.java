package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações sobre Funcionalidade
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Funcionalidade 
{
	private int idFuncionalidade;
	private String descFuncionalidade;
	private Perfil perfil;
	
	/**
	 * @roseuid 40437692031C
	 */
	public Funcionalidade() 
	{
		
	}
	
	/**
	 * Access method for the idFuncionalidade property.
	 * 
	 * @return   the current value of the idFuncionalidade property
	 */
	public int getIdFuncionalidade() 
	{
		return idFuncionalidade;
		}
	
	/**
	 * Sets the value of the idFuncionalidade property.
	 * 
	 * @param aIdFuncionalidade the new value of the idFuncionalidade property
	 */
	public void setIdFuncionalidade(int aIdFuncionalidade) 
	{
		idFuncionalidade = aIdFuncionalidade;
		}
	
	/**
	 * Access method for the descFuncionalidade property.
	 * 
	 * @return   the current value of the descFuncionalidade property
	 */
	public String getDescFuncionalidade() 
	{
		return descFuncionalidade;
		}
	
	/**
	 * Sets the value of the descFuncionalidade property.
	 * 
	 * @param aDescFuncionalidade the new value of the descFuncionalidade property
	 */
	public void setDescFuncionalidade(String aDescFuncionalidade) 
	{
		descFuncionalidade = aDescFuncionalidade;
		}
	
	/**
	 * Access method for the perfil property.
	 * 
	 * @return   the current value of the perfil property
	 */
	public Perfil getPerfil() 
	{
		return perfil;
		}
	
	/**
	 * Sets the value of the perfil property.
	 * 
	 * @param aPerfil the new value of the perfil property
	 */
	public void setPerfil(Perfil aPerfil) 
	{
		perfil = aPerfil;
		}
}
