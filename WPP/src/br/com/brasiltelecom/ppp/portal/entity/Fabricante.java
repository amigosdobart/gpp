package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Fabricante das Tabelas de Hsid
 * @author Geraldo Palmeira
 * @since 17/04/2006
 */
public class Fabricante implements Comparable
{

	private int codigoFabricante;
	private String nomeFabricante;

	/**
	 * @return coFabricante Codigo do Fabricante
	 */
	public int getCodigoFabricante() 
	{
		return codigoFabricante;
	}

	/**
	 * @return noFabricante Nome do Fabricante
	 */
	public String getNomeFabricante() 
	{
		return nomeFabricante;
	}

	/**
	 * @param coFabricante Codigo do Fabricante
	 */
	public void setCodigoFabricante(int coFabricante) 
	{
		this.codigoFabricante = coFabricante;
	}

	/**
	 * @param noFabricante Nome do Fabricante
	 */
	public void setNomeFabricante(String noFabricante) 
	{
		this.nomeFabricante = noFabricante;
	}
	
	public int hashCode()
	{
		return this.codigoFabricante;
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Fabricante))
			return false;
		
		if ( ((Fabricante)obj).getCodigoFabricante() == this.codigoFabricante)
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getNomeFabricante();
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof Fabricante))
			return 0;
		
		return this.getNomeFabricante().compareTo( ((Fabricante)obj).getNomeFabricante() );
	}

}
