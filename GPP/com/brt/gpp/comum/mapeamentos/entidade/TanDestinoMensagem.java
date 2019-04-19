package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Classe entidade que representa a tabela TBL_TAN_DESTINO_MENSAGEM
 *  
 *  @author Jorge Abreu
 *  Criado em: 23/10/2007
 */
public class TanDestinoMensagem implements Serializable
{
	private static final long serialVersionUID = 6163138722817889593L;

	/**
	 * Chave prim�ria sequencial da entidade.
	 */
	private int idDestinoMensagem;
	
	/**
	 * MSISDN do destinat�rio.
	 */
	private String idtMsisdnDestino;
	
	/**
     * ID da mensagem (dado gerado pelo Tangram)
	 */
	private String idMensagem;
	
	/**
	 * Entidade TanRequisicao.
	 */
	private TanRequisicao requisicao;

	
	/**
	 * Obt�m o ID da entidade.
	 */
	public int getIdDestinoMensagem() 
	{
		return idDestinoMensagem;
	}

	/**
	 * Define o ID da entidade.
	 */
	public void setIdDestinoMensagem(int idDestinoMensagem) 
	{
		this.idDestinoMensagem = idDestinoMensagem;
	}
	
	/**
	 * Obt�m MSISDN do destinat�rio.
	 */
	public String getIdtMsisdnDestino() 
	{
		return idtMsisdnDestino;
	}

	/**
	 * Define o MSISDN do destinat�rio.
	 */
	public void setIdtMsisdnDestino(String idtMsisdnDestino) 
	{
		this.idtMsisdnDestino = idtMsisdnDestino;
	}
	
	/**
	 * Obt�m o ID da Mensagem.
	 */
	public String getIdMensagem() 
	{
		return idMensagem;
	}

	/**
	 * Define o ID da Mensagem.
	 */
	public void setIdMensagem(String idMensagem) 
	{
		this.idMensagem = idMensagem;
	}
	
	/**
	 * Obt�m a entidade TanRequisicao.
	 */
	public TanRequisicao getRequisicao() 
	{
		return requisicao;
	}

	/**
	 * Define a entidade TanRequisicao.
	 */
	public void setRequisicao(TanRequisicao requisicao) 
	{
		this.requisicao = requisicao;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || ! (obj instanceof TanDestinoMensagem))
			return false;
		
		if (this == obj)
			return true;
		
		if (this.getIdtMsisdnDestino() != null && 
			((TanDestinoMensagem)obj).getIdtMsisdnDestino() != null)
		{
			return ((TanDestinoMensagem)obj).getIdtMsisdnDestino().equals(this.getIdtMsisdnDestino());
		}
		
		return false;
	}
	
}
