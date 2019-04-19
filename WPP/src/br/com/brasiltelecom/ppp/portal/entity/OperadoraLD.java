package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Operadora de Longa Distancia
 * @author Daniel Ferreira 
 * @since 10/02/2005
 */
public class OperadoraLD 
{

	private int numCSP;
	private String nomOperadora;

	/**
	 * @return numCSP Codigo de Servico de Prestadora
	 */
	public int getNumCSP() 
	{
		return numCSP;
	}

	/**
	 * @return nomOperadora Nome da Operadora de Longa Distancia
	 */
	public String getNomOperadora() 
	{
		return nomOperadora;
	}

	/**
	 * @param numCSP Codigo de Servico de Prestadora
	 */
	public void setNumCSP(int numCSP) 
	{
		this.numCSP = numCSP;
	}

	/**
	 * @param nomOperadora Nome da Operadora de Longa Distancia
	 */
	public void setNomOperadora(String nomOperadora) 
	{
		this.nomOperadora = nomOperadora;
	}

}
