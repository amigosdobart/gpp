package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;

/**
 *	Classe que representa uma parte de uma mensagem a ser enviada pelo Tangram.  
 *
 *  Cada conte�do de mensagem � na verdade um SMS que comp�e a mensagem como 
 *  um todo. Suporta tipos TEXTO e BINARIO, cabe�alho IDH e truncamento.
 * 
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class ConteudoMensagem implements Serializable
{
	private static final long serialVersionUID = -3175843347266132008L;

	/**
	 * Texto da mensagem a ser enviada.
	 * No caso de mensagem bin�ria, os bytes devem ser enviados em hexadecimal.
	 * Por exemplo, para que sejam enviados os bytes 0x38, 0x76, 0x88, o 
	 * elemento textoConteudo deve estar na forma: "387688"
	 */
	private String textoConteudo;
	
	/**
	 * Indica se o conte�do do texto recebido deve ser enviado em forma 
	 * textual ou bin�ria. 
	 */
	private Boolean indBinario;
	
	/**
	 * Indica se o texto ser� cortado no limite de caracteres aceitos 
	 * pela plataforma da operadora. 
	 */
	private Boolean indTruncamento;
	
	/**
	 * Cabe�alho UserDataHeader do SMS.
	 */
	private String udh;

	/**
	 * Obt�m o cabe�alho UserDataHeader do SMS.
	 */
	public String getUdh() 
	{
		return udh;
	}

	/**
	 * Define o cabe�alho UserDataHeader do SMS.
	 */
	public void setUdh(String udh) 
	{
		this.udh = udh;
	}

	/**
	 * Obt�m o indicador se o conte�do do texto � bin�rio.
	 */
	public Boolean getIndBinario() 
	{
		return indBinario;
	}

	/**
	 * Define se o conte�do do texto � bin�rio.
	 */
	public void setIndBinario(Boolean indBinario) 
	{
		this.indBinario = indBinario;
	}

	/**
	 * Obt�m o indicador se o texto ser� cortado no limite de 
	 * caracteres aceitos pela plataforma da operadora. 
	 */
	public Boolean getIndTruncamento() 
	{
		return indTruncamento;
	}

	/**
	 * Define se o texto ser� cortado no limite de 
	 * caracteres aceitos pela plataforma da operadora. 
	 */
	public void setIndTruncamento(Boolean indTruncamento) 
	{
		this.indTruncamento = indTruncamento;
	}

	/**
	 * Obt�m o texto da mensagem a ser enviada. Se o SMS for bin�rio, o
	 * texto � a concatena��o dos valores em hexa. Exemplo: "387688"
	 * equivale aos bytes 0x38, 0x76, 0x88.
	 */
	public String getTextoConteudo() 
	{
		return textoConteudo;
	}

	/**
	 * Define o texto da mensagem a ser enviada.
	 * No caso de mensagem bin�ria, para que sejam enviados os bytes 
	 * 0x38, 0x76, 0x88, o elemento textoConteudo deve estar na forma: "387688"
	 */
	public void setTextoConteudo(String textoConteudo) 
	{
		this.textoConteudo = textoConteudo;
	}
	
	
}
