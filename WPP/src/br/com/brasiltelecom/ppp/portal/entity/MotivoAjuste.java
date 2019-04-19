//Source file: C:\\PPP\\src\\br.com.brasiltelecom.ppp.model\\MotivoAjuste.java

package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * Modela a tabela de motivo de ajuste
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class MotivoAjuste 
{
	private int idMotivoAjuste;
	private String descMotivoAjuste;
	private int dataExpiracao;
	private String usuario;
	private Date data;
	private String tipoAjuste;
	
	/**
	 * @roseuid 404376940000
	 */
	public MotivoAjuste() 
	{
		
	}
	
	/**
	 * Access method for the idMotivoAjuste property.
	 * 
	 * @return   the current value of the idMotivoAjuste property
	 */
	public int getIdMotivoAjuste() 
	{
		return idMotivoAjuste;
		}
	
	/**
	 * Sets the value of the idMotivoAjuste property.
	 * 
	 * @param aIdMotivoAjuste the new value of the idMotivoAjuste property
	 */
	public void setIdMotivoAjuste(int aIdMotivoAjuste) 
	{
		idMotivoAjuste = aIdMotivoAjuste;
		}
	
	/**
	 * Access method for the descMotivoAjuste property.
	 * 
	 * @return   the current value of the descMotivoAjuste property
	 */
	public String getDescMotivoAjuste() 
	{
		return descMotivoAjuste;
		}
	
	/**
	 * Sets the value of the descMotivoAjuste property.
	 * 
	 * @param aDescMotivoAjuste the new value of the descMotivoAjuste property
	 */
	public void setDescMotivoAjuste(String aDescMotivoAjuste) 
	{
		descMotivoAjuste = aDescMotivoAjuste;
		}
	

	/**
	 * Access method for the usuario property.
	 * 
	 * @return   the current value of the usuario property
	 */
	public String getUsuario() 
	{
		return usuario;
		}
	
	/**
	 * Sets the value of the usuario property.
	 * 
	 * @param aUsuario the new value of the usuario property
	 */
	public void setUsuario(String aUsuario) 
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
	 * @return
	 */
	public String getTipoAjuste() {
		return tipoAjuste;
	}

	/**
	 * @param string
	 */
	public void setTipoAjuste(String string) {
		tipoAjuste = string;
	}

	/**
	 * @return
	 */
	public int getDataExpiracao() {
		return dataExpiracao;
	}

	/**
	 * @param i
	 */
	public void setDataExpiracao(int i) {
		dataExpiracao = i;
	}

}
