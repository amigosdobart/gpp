
package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações sobre Usuario
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Usuario 
{
	private int idUsuario;
	private String nome;
	private Perfil perfil;
	
	/**
	 * @roseuid 4043769203A9
	 */
	public Usuario() 
	{
		
	}
	
	/**
	 * Access method for the idUsuario property.
	 * 
	 * @return   the current value of the idUsuario property
	 */
	public int getIdUsuario() 
	{
		return idUsuario;
		}
	
	/**
	 * Sets the value of the idUsuario property.
	 * 
	 * @param aIdUsuario the new value of the idUsuario property
	 */
	public void setIdUsuario(int aIdUsuario) 
	{
		idUsuario = aIdUsuario;
		}
	
	/**
	 * Access method for the nome property.
	 * 
	 * @return   the current value of the nome property
	 */
	public String getNome() 
	{
		return nome;
		}
	
	/**
	 * Sets the value of the nome property.
	 * 
	 * @param aNome the new value of the nome property
	 */
	public void setNome(String aNome) 
	{
		nome = aNome;
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
