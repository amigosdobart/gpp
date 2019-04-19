//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.consultaAparelho;

/**
  *
  * Este arquivo define a classe do objeto que conterá a
  * descrição do aparelho
  * 
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

public class Caracteristica 
{
	private String	capacidade;
	private String	descricao;
	private String	valor;
	private String	adicionais;
	
	/**
	 * Método construtor 
	 */
	public Caracteristica(String capacidade, String descricao, String valor, String adicionais)
	{
	    this.capacidade = capacidade;
	    this.descricao = descricao;
	    this.valor = valor;
	    this.adicionais = adicionais;
	}
	
	// Métodos get
	
	/**
	 * @return capacidade
	 */
	public String getCapacidade() 
	{
		return this.capacidade;
	}

	/**
	 * @return descricao
	 */
	public String getDescricao() 
	{
		return this.descricao;
	}
	
	/**
	 * @return valor
	 */
	public String getValor() 
	{
		return this.valor;
	}

	/**
	 * @return adicionais
	 */
	public String getAdicionais() 
	{
		return this.adicionais;
	}

	// Métodos set

	/**
	 * @param capacidade
	 */
	public void setCapacidade(String capacidade) 
	{
		this.capacidade = capacidade;
	}

	/**
	 * @param descricao
	 */
	public void setDescricao(String descricao) 
	{
		this.descricao = descricao;
	}

	/**
	 * @param valor
	 */
	public void setValor(String valor) 
	{
		this.valor = valor;
	}

	/**
	 * @param adicionais
	 */
	public void setAdicionais(String adicionais) 
	{
		this.adicionais = adicionais;
	}			
}