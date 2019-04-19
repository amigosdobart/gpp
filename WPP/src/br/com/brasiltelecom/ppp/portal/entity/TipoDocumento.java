package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author Marcos Castelo Magalhães
 * @since  10/10/2005
 */

public class TipoDocumento 
{
	private int  	tipDocumento ;
	private String	desDocumento ;
	private int		indAtivo ;
			
	// Métodos get
	
	/**
	 * @return
	 */
	public int getTipDocumento() 
	{
		return this.tipDocumento;
	}

	/**
	 * @return
	 */
	public String getDesDocumento() 
	{
		return this.desDocumento;
	}
	/**
	 * @return
	 */
		
	public int getIndAtivo()
	{
		return this.indAtivo;
	}
	
	// Métodos set
	
	/**
	 * @param int
	 */
	public void setTipDocumento(int tipDocumento) 
	{
		this.tipDocumento = tipDocumento;
	}

	/**
	 * @param String
	 */
	public void setDesDocumento(String desDocumento) 
	{
		this.desDocumento = desDocumento;
	}

	/**
	 * @param int
	 */
	public void setIndAtivo(int indAtivo) 
	{
		this.indAtivo = indAtivo;
	}

}
