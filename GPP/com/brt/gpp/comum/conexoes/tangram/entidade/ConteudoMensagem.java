package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;

/**
 *	Classe que representa uma parte de uma mensagem a ser enviada pelo Tangram.  
 *
 *  Cada conteúdo de mensagem é na verdade um SMS que compõe a mensagem como 
 *  um todo. Suporta tipos TEXTO e BINARIO, cabeçalho IDH e truncamento.
 * 
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class ConteudoMensagem implements Serializable
{
	private static final long serialVersionUID = -3175843347266132008L;

	/**
	 * Texto da mensagem a ser enviada.
	 * No caso de mensagem binária, os bytes devem ser enviados em hexadecimal.
	 * Por exemplo, para que sejam enviados os bytes 0x38, 0x76, 0x88, o 
	 * elemento textoConteudo deve estar na forma: "387688"
	 */
	private String textoConteudo;
	
	/**
	 * Indica se o conteúdo do texto recebido deve ser enviado em forma 
	 * textual ou binária. 
	 */
	private Boolean indBinario;
	
	/**
	 * Indica se o texto será cortado no limite de caracteres aceitos 
	 * pela plataforma da operadora. 
	 */
	private Boolean indTruncamento;
	
	/**
	 * Cabeçalho UserDataHeader do SMS.
	 */
	private String udh;

	/**
	 * Obtém o cabeçalho UserDataHeader do SMS.
	 */
	public String getUdh() 
	{
		return udh;
	}

	/**
	 * Define o cabeçalho UserDataHeader do SMS.
	 */
	public void setUdh(String udh) 
	{
		this.udh = udh;
	}

	/**
	 * Obtém o indicador se o conteúdo do texto é binário.
	 */
	public Boolean getIndBinario() 
	{
		return indBinario;
	}

	/**
	 * Define se o conteúdo do texto é binário.
	 */
	public void setIndBinario(Boolean indBinario) 
	{
		this.indBinario = indBinario;
	}

	/**
	 * Obtém o indicador se o texto será cortado no limite de 
	 * caracteres aceitos pela plataforma da operadora. 
	 */
	public Boolean getIndTruncamento() 
	{
		return indTruncamento;
	}

	/**
	 * Define se o texto será cortado no limite de 
	 * caracteres aceitos pela plataforma da operadora. 
	 */
	public void setIndTruncamento(Boolean indTruncamento) 
	{
		this.indTruncamento = indTruncamento;
	}

	/**
	 * Obtém o texto da mensagem a ser enviada. Se o SMS for binário, o
	 * texto é a concatenação dos valores em hexa. Exemplo: "387688"
	 * equivale aos bytes 0x38, 0x76, 0x88.
	 */
	public String getTextoConteudo() 
	{
		return textoConteudo;
	}

	/**
	 * Define o texto da mensagem a ser enviada.
	 * No caso de mensagem binária, para que sejam enviados os bytes 
	 * 0x38, 0x76, 0x88, o elemento textoConteudo deve estar na forma: "387688"
	 */
	public void setTextoConteudo(String textoConteudo) 
	{
		this.textoConteudo = textoConteudo;
	}
	
	
}
