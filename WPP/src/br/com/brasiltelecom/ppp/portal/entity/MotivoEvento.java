package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author Marcos Castelo Magalh�es
 * @since  10/10/2005
 */

public class MotivoEvento 
{
	private int  	idtMotivo ;
	private String  nomMotivo ;
	private String	desMotivo ;
	private int		indDisponivel ;
			
	// M�todos get
	
	/**
	 * @return
	 */
	public int getIdtMotivo() 
	{
		return this.idtMotivo;
	}
	/**
	 * @return
	 */
	public String getNomMotivo() 
	{
		return this.nomMotivo;
	}
	/**
	 * @return
	 */
	public String getDesMotivo() 
	{
		return this.desMotivo;
	}
	/**
	 * @return
	 */
		
	public int getIndDisponivel()
	{
		return this.indDisponivel;
	}
	
	// M�todos set
	
	/**
	 * @param int
	 */
	public void setIdtMotivo(int idtMotivo) 
	{
		this.idtMotivo = idtMotivo;
	}

	/**
	 * @param String
	 */
	public void setNomMotivo(String nomMotivo) 
	{
		this.nomMotivo = nomMotivo;
	}
	/**
	 * @param String
	 */
	public void setDesMotivo(String desMotivo) 
	{
		this.desMotivo = desMotivo;
	}
	/**
	 * @param int
	 */
	public void setIndDisponivel(int indDisponivel) 
	{
		this.indDisponivel = indDisponivel;
	}

}
