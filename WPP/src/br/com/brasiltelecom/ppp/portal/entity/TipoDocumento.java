package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author Marcos Castelo Magalh�es
 * @since  10/10/2005
 */

public class TipoDocumento 
{
	private int  	tipDocumento ;
	private String	desDocumento ;
	private int		indAtivo ;
			
	// M�todos get
	
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
	
	// M�todos set
	
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
