//Source file: C:\\PPP\\src\\br.com.brasiltelecom.ppp.model\\TipoExtrato.java

package br.com.brasiltelecom.ppp.portal.entity;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Modela a tabela de tipo extrato
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class TipoExtrato 
{
	
	/**
	 * @since 01/03/2004
	 */
	private int idTipoExtrato;
	
	/**
	 * @since 01/03/2004
	 */
	private String descTipoExtrato;
	
	/**
	 * @since 01/03/2004
	 */
	private double valor;
	
	/**
	 * @since 01/03/2004
	 */
	private String usuario;
	
	/**
	 * @since 01/03/2004
	 */
	private Date data;
	
	private int ativo;
	
	/**
	 * @roseuid 404376930177
	 */
	public TipoExtrato() 
	{
		
	}
	
	/**
	 * Access method for the idTipoExtrato property.
	 * 
	 * @return   the current value of the idTipoExtrato property
	 */
	public int getIdTipoExtrato() 
	{
		return idTipoExtrato;
		}
	
	/**
	 * Sets the value of the idTipoExtrato property.
	 * 
	 * @param aIdTipoExtrato the new value of the idTipoExtrato property
	 */
	public void setIdTipoExtrato(int aIdTipoExtrato) 
	{
		idTipoExtrato = aIdTipoExtrato;
		}
	
	/**
	 * Access method for the descTipoExtrato property.
	 * 
	 * @return   the current value of the descTipoExtrato property
	 */
	public String getDescTipoExtrato() 
	{
		return descTipoExtrato;
		}
	
	/**
	 * Sets the value of the descTipoExtrato property.
	 * 
	 * @param aDescTipoExtrato the new value of the descTipoExtrato property
	 */
	public void setDescTipoExtrato(String aDescTipoExtrato) 
	{
		descTipoExtrato = aDescTipoExtrato;
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
	public int getAtivo() {
		return ativo;
	}

	/**
	 * @param i
	 */
	public void setAtivo(int i) {
		ativo = i;
	}

	public String getValorString(){
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(valor);
	}
}
