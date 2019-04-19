package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Classe entidade que representa a tabela TBL_TAN_CONTEUDO_MENSAGEM.  
 *
 *  @author Jorge Abreu
 *  Criado em: 23/10/2007
 */

public class TanConteudoMensagem implements Serializable
{
	private static final long serialVersionUID = -3175843347266132008L;

	/**
	 * Chave prim�ria sequencial da entidade.
	 */
	private int idConteudo;
	
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
	 * Entidade Requisicao proprietaria deste conteudo. 
	 */
	private TanRequisicao requisicao;
	
	
	/**
	 * Obt�m o ID do conteudo.
	 */
	public int getIdConteudo() 
	{
		return idConteudo;
	}

	/**
	 * Define o ID do conteudo.
	 */
	public void setIdConteudo(int idConteudo) 
	{
		this.idConteudo = idConteudo;
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
	
	/**
	 * Obt�m a Requisicao correspondente a este conteudo.
	 */
	public TanRequisicao getRequisicao() 
	{
		return requisicao;
	}

	/**
	 * Define a requisicao proprietaria deste conteudo.
	 */
	public void setRequisicao(TanRequisicao requisicao) 
	{
		this.requisicao = requisicao;
	}
}
