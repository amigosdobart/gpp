package com.brt.gpp.comum.mapeamentos.entidade;

/**
 *	Classe que representa a entidade da tabela TBL_REC_CANAL.
 * 
 *	@author	Geraldo Palmeira
 *	@since	19/06/2007
 */
public class Canal 
{
	private String idCanal;
	private String descCanal;
	
	public Canal(String idCanal, String descCanal) 
	{
		this.idCanal   = idCanal;
		this.descCanal = descCanal;
	}
	public Canal() 
	{
	}
	
	public boolean equals(Object obj) 
	{
		if(obj == null)
			return false;
		
		return (this.hashCode() == obj.hashCode());
	}

	public int hashCode() 
	{
		return (this.getClass().getName() + "||" + this.idCanal).hashCode();
	}

	public String toString() 
	{
		return ((this.idCanal != null) ? this.descCanal : "NULL");
	}

	/**
	 * @return the descCanal
	 */
	public String getDescCanal() 
	{
		return descCanal;
	}

	/**
	 * @param descCanal the descCanal to set
	 */
	public void setDescCanal(String descCanal) 
	{
		this.descCanal = descCanal;
	}

	/**
	 * @return the idCanal
	 */
	public String getIdCanal() 
	{
		return idCanal;
	}

	/**
	 * @param idCanal the idCanal to set
	 */
	public void setIdCanal(String idCanal) 
	{
		this.idCanal = idCanal;
	}
}

