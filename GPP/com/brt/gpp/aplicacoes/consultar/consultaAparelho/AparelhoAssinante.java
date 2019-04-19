//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaAparelho;

import java.util.ArrayList;
import java.util.Collection;

/**
  *
  * Este arquivo contem a definição da classe AparelhoAssinante
  * Contém as características do aparelho do assinante
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
	// Variáveis Membro
	private String		msisdn;
	private String		marca;
	private String		modelo;
	private Collection	carac;
	
	/**
	 * <p><b>Método...:</b> Assinante
	 * <p><b>Descrição:</b> Construtor que inicializa todos seus atributos
	 */
	public AparelhoAssinante()
	{
		this.msisdn = null;
		this.marca	= null;
		this.modelo	= null;
		this.carac	= new ArrayList();
	}
	
	// Métodos set

	/**
	 * <p><b>Método...:</b> setMsisdn
	 * <p><b>Descrição:</b> Armazena o código do assinante
	 * @param <b>String</b>	aMsisdn	Código do Assinante
	 */
	public void setMsisdn (String aMsisdn)
	{
		this.msisdn = aMsisdn;
	}
	
	/**
	 * <p><b>Método...:</b> setMarca
	 * <p><b>Descrição:</b> Seta a Marca do Aparelho do Assinante
	 * @param <b>String</b>	aMarca	Marca do Aparelho do Assinante
	 */
	public void setMarca (String aMarca)
	{
		this.marca = aMarca;
	}
	
	/**
	 * <p><b>Método...:</b> setModelo
	 * <p><b>Descrição:</b> Seta o Modelo do Aparelho do Assinante
	 * @param <b>String</b>	aModelo	Modelo do Aparelho do Assinante
	 */
	public void setModelo(String aModelo) 
	{
		this.modelo = aModelo;
	}
	
	/**
	 * <p><b>Método...:</b> setCaracteristica
	 * <p><b>Descrição:</b> Inclui um Item na Lista de Características
	 * @param <b>String</b>	aCarac		Característica
	 */
	public void setCaracteristica(Caracteristica aCarac)
	{
		this.carac.add((Object)aCarac);	
	}	

	// Métodos get

	/**
	 * <p><b>Método...:</b> getMsisdn
	 * <p><b>Descrição:</b> Retorna o MSISDN do Assinante
	 * @return <b>String</b>	msisdn		MSISDN do Assinante
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 * <p><b>Método...:</b> getMarca
	 * <p><b>Descrição:</b> Retorna a Marca do Aparelho do Assinante
	 * @return <b>String</b>	marca	Marca do Aparelho do Assinante
	 */
	public String getMarca ()
	{
		return this.marca;
	}
	
	/**
	 * <p><b>Método...:</b> getModelo
	 * <p><b>Descrição:</b> Retorna o Modelo do Aparelho do Assinante
	 * @return <b>String</b>	modelo	Modelo do Aparelho do Assinante
	 */
	public String getModelo() 
	{
		return this.modelo;
	}
	
	/**
	 * <p><b>Método...:</b> getCaracteristica
	 * <p><b>Descrição:</b> Retorna Lista de Características do Aparelho
	 * @return <b>Iterator</b>	carac	Características do Aparelho
	 */
	public Collection getCaracteristica()
	{
		return this.carac;	
	}	
}