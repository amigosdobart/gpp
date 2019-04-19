
package br.com.brasiltelecom.ppp.model;

import java.util.Date;

/**
 * Modela as informações sobre Perfil
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Perfil 
{
	private int idPerfil;
	private String descPerfil;
	private double maxValorCobranca;
	private Usuario usuario;
	private Date data;
	private java.util.Collection funcionalidades;
	private java.util.Collection usuarios;
	
	/**
	 * @roseuid 40437693006D
	 */
	public Perfil() 
	{
		
	}
	
	/**
	 * Access method for the idPerfil property.
	 * 
	 * @return   the current value of the idPerfil property
	 */
	public int getIdPerfil() 
	{
		return idPerfil;
		}
	
	/**
	 * Sets the value of the idPerfil property.
	 * 
	 * @param aIdPerfil the new value of the idPerfil property
	 */
	public void setIdPerfil(int aIdPerfil) 
	{
		idPerfil = aIdPerfil;
		}
	
	/**
	 * Access method for the descPerfil property.
	 * 
	 * @return   the current value of the descPerfil property
	 */
	public String getDescPerfil() 
	{
		return descPerfil;
		}
	
	/**
	 * Sets the value of the descPerfil property.
	 * 
	 * @param aDescPerfil the new value of the descPerfil property
	 */
	public void setDescPerfil(String aDescPerfil) 
	{
		descPerfil = aDescPerfil;
		}
	
	/**
	 * Access method for the maxValorCobranca property.
	 * 
	 * @return   the current value of the maxValorCobranca property
	 */
	public double getMaxValorCobranca() 
	{
		return maxValorCobranca;
		}
	
	/**
	 * Sets the value of the maxValorCobranca property.
	 * 
	 * @param aMaxValorCobranca the new value of the maxValorCobranca property
	 */
	public void setMaxValorCobranca(double aMaxValorCobranca) 
	{
		maxValorCobranca = aMaxValorCobranca;
		}
	
	/**
	 * Access method for the usuario property.
	 * 
	 * @return   the current value of the usuario property
	 */
	public Usuario getUsuario() 
	{
		return usuario;
		}
	
	/**
	 * Sets the value of the usuario property.
	 * 
	 * @param aUsuario the new value of the usuario property
	 */
	public void setUsuario(Usuario aUsuario) 
	{
		usuario = aUsuario;
		}
	
	/**
	 * Access method for the data property.
	 * 
	 * @return   the current value of the data property
	 */
	public java.util.Date getData() 
	{
		return data;
		}
	
	/**
	 * Sets the value of the data property.
	 * 
	 * @param aData the new value of the data property
	 */
	public void setData(java.util.Date aData) 
	{
		data = aData;
		}
	
	/**
	 * Access method for the funcionalidades property.
	 * 
	 * @return   the current value of the funcionalidades property
	 */
	public java.util.Collection getFuncionalidades() 
	{
		return funcionalidades;
		}
	
	/**
	 * Sets the value of the funcionalidades property.
	 * 
	 * @param aFuncionalidades the new value of the funcionalidades property
	 */
	public void setFuncionalidades(java.util.Collection aFuncionalidades) 
	{
		funcionalidades = aFuncionalidades;
		}
	
	/**
	 * Access method for the usuarios property.
	 * 
	 * @return   the current value of the usuarios property
	 */
	public java.util.Collection getUsuarios() 
	{
		return usuarios;
		}
	
	/**
	 * Sets the value of the usuarios property.
	 * 
	 * @param aUsuarios the new value of the usuarios property
	 */
	public void setUsuarios(java.util.Collection aUsuarios) 
	{
		usuarios = aUsuarios;
		}
}
