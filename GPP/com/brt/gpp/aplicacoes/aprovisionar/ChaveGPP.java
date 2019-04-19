// Definicao do Pacote
package com.brt.gpp.aplicacoes.aprovisionar;

import java.security.*; 

/**
  *
  * Este arquivo contem a definicao da classe de ChaveGPP 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Camile Cardoso Couto
  * Data:               06/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class ChaveGPP implements Key
{
	private static final long serialVersionUID = 7652671241206221464L;
	public byte[] encoded;	
	public String format;
	public String algorithm;

	/**
	 * Metodo...: getFormat
	 * Descricao: Retorna format
	 * @return	String
	 */
	public String getFormat ()
	{
		return this.format;
	}

	/**
	 * Metodo...: getAlgorithm
	 * Descricao: Retorna string algoritmo
	 * @return	String
	 */
	public String getAlgorithm ( )
	{
		return this.algorithm;
	}

	/**
	 * Metodo...: getEncoded
	 * Descricao: Retorna array Encoded
	 * @return	byte[]
	 */
	public byte[] getEncoded ( )
	{
		return this.encoded;
	}

	/**
	 * Metodo...: setEncoded
	 * Descricao: Seta array encoded
	 * @param 	byte[]	enc	Array de Codigos
	 */
	public void setEncoded ( byte[] enc )
	{
		this.encoded = enc;
	}

	/**
	 * Metodo...: setFormat
	 * Descricao: Seta string de formato
	 * @param	String	form	String de Formato
	 */
	public void setFormat ( String form )
	{
		this.format = form;
	}

	/**
	 * Metodo...: setAlgorthm
	 * Descricao: Seta string de algoritmo
	 * @param 	String	alg		String de algoritmo
	 */
	public void setAlgorithm ( String alg )
	{
		this.algorithm = alg;
	}
}