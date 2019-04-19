//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaAparelho;

import java.util.ArrayList;
import java.util.Collection;

/**
  *
  * Este arquivo contem a defini��o da classe AparelhoAssinante
  * Cont�m as caracter�sticas do aparelho do assinante
  * <P> Versao:        	1.0
  *
  * @Autor:            	Marcelo Alves Araujo
  * Data:               16/06/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class AparelhoAssinante
{
	// Vari�veis Membro
	private String		msisdn;
	private String		marca;
	private String		modelo;
	private Collection	carac;
	
	/**
	 * <p><b>M�todo...:</b> Assinante
	 * <p><b>Descri��o:</b> Construtor que inicializa todos seus atributos
	 */
	public AparelhoAssinante()
	{
		this.msisdn = null;
		this.marca	= null;
		this.modelo	= null;
		this.carac	= new ArrayList();
	}
	
	// M�todos set

	/**
	 * <p><b>M�todo...:</b> setMsisdn
	 * <p><b>Descri��o:</b> Armazena o c�digo do assinante
	 * @param <b>String</b>	aMsisdn	C�digo do Assinante
	 */
	public void setMsisdn (String aMsisdn)
	{
		this.msisdn = aMsisdn;
	}
	
	/**
	 * <p><b>M�todo...:</b> setMarca
	 * <p><b>Descri��o:</b> Seta a Marca do Aparelho do Assinante
	 * @param <b>String</b>	aMarca	Marca do Aparelho do Assinante
	 */
	public void setMarca (String aMarca)
	{
		this.marca = aMarca;
	}
	
	/**
	 * <p><b>M�todo...:</b> setModelo
	 * <p><b>Descri��o:</b> Seta o Modelo do Aparelho do Assinante
	 * @param <b>String</b>	aModelo	Modelo do Aparelho do Assinante
	 */
	public void setModelo(String aModelo) 
	{
		this.modelo = aModelo;
	}
	
	/**
	 * <p><b>M�todo...:</b> setCaracteristica
	 * <p><b>Descri��o:</b> Inclui um Item na Lista de Caracter�sticas
	 * @param <b>String</b>	aCarac		Caracter�stica
	 */
	public void setCaracteristica(Caracteristica aCarac)
	{
		this.carac.add((Object)aCarac);	
	}	

	// M�todos get

	/**
	 * <p><b>M�todo...:</b> getMsisdn
	 * <p><b>Descri��o:</b> Retorna o MSISDN do Assinante
	 * @return <b>String</b>	msisdn		MSISDN do Assinante
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 * <p><b>M�todo...:</b> getMarca
	 * <p><b>Descri��o:</b> Retorna a Marca do Aparelho do Assinante
	 * @return <b>String</b>	marca	Marca do Aparelho do Assinante
	 */
	public String getMarca ()
	{
		return this.marca;
	}
	
	/**
	 * <p><b>M�todo...:</b> getModelo
	 * <p><b>Descri��o:</b> Retorna o Modelo do Aparelho do Assinante
	 * @return <b>String</b>	modelo	Modelo do Aparelho do Assinante
	 */
	public String getModelo() 
	{
		return this.modelo;
	}
	
	/**
	 * <p><b>M�todo...:</b> getCaracteristica
	 * <p><b>Descri��o:</b> Retorna Lista de Caracter�sticas do Aparelho
	 * @return <b>Iterator</b>	carac	Caracter�sticas do Aparelho
	 */
	public Collection getCaracteristica()
	{
		return this.carac;	
	}	
}