package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Modelos das Tabelas de Hsid
 * @author Geraldo Palmeira
 * @since 17/04/2006
 */
public class Modelo implements Comparable
{

	private int codigoModelo;
	private String nomeModelo;
	private Fabricante fabricante;

	
	/**
	 * @return coModelo Codigo do Modelo
	 */
	public int getCodigoModelo() 
	{
		return codigoModelo;
	}
	
	/**
	 * @return noModelo Nome do Modelo
	 */
	public String getNomeModelo() 
	{
		return nomeModelo;
	}
	
	/**
	 * @return coFabricante Codigo do Fabricante
	 */
	public Fabricante getFabricante() 
	{
		return fabricante;
	}

	/**
	 * @param coModelo Codigo do Modelo
	 */
	public void setCodigoModelo(int coModelo) 
	{
		this.codigoModelo = coModelo;
	}
	
	/**
	 * @param noModelo Nome do Modelo
	 */
	public void setNomeModelo(String noModelo) 
	{
		this.nomeModelo = noModelo;
	}
	
	/**
	 * @param coFabricante Codigo do Fabricante
	 */
	public void setFabricante(Fabricante fabricante) 
	{
		this.fabricante = fabricante;
	}

	public int hashCode()
	{
		return this.codigoModelo;
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof Modelo) )
			return false;
		
		if ( ((Modelo)obj).getCodigoModelo() == this.codigoModelo )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.nomeModelo;
	}

	public int compareTo(Object obj)
	{
		if ( !(obj instanceof Modelo) )
			return 0;
		
		Modelo m = (Modelo)obj;
		int comparacao = this.getFabricante().compareTo(m.getFabricante());
		if (comparacao == 0)
			return this.getNomeModelo().compareTo( m.getNomeModelo() );

		return comparacao;
	}
}
