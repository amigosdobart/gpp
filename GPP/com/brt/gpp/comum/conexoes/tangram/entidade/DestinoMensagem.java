package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;
import java.util.Collection;

/**
 *	Classe que representa um destinat�rio de mensagem a ser 
 *  enviada pelo Tangram.  
 *  
 *  Essa entidade encapsula tamb�m alguns par�metros de retorno gerados
 *  pelo Tangram no momento do envio da requisi��o.
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class DestinoMensagem implements Serializable
{
	private static final long serialVersionUID = 1452515319139239578L;

	/**
	 * MSISDN do destinat�rio.
	 */
	private String idtMsisdnDestino;
	
	/**
	 * C�digo de retorno do Tangram para este destinat�rio. 
	 * PARAMETRO GERADO PELO TANGRAM.
	 * Valores: 0 = sucesso, outros = falha
	 */
	private Short codRetorno;
	
	/**
	 * Identificador da sess�o do destinat�rio da mensagens com a aplica��o.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	private String idSessao;
	
	/**
	 * Identificador do pacote de mensagens para o destinat�rio. 
	 * Presente apenas se a requisi��o informar os dados do pacote. 
	 * Este identificar ser� informado na notifica��o do Tangram.
	 */
	private String idPacote;
	
	/**
	 * Lista de IDs de mensagem (SMS) gerada pelo Tangram para o 
	 * destinat�rio em quest�o.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	private Collection idsMensagem;

	/**
	 * Obt�m o c�digo de retorno do Tangram para este destinat�rio. 
	 * Valores: 0 = sucesso, outros = falha
	 */
	public Short getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * Define o c�digo de retorno do Tangram para este destinat�rio.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	public void setCodRetorno(Short codRetorno) 
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * Obt�m lista de IDs de mensagem (SMS) gerada pelo Tangram para o 
	 * destinat�rio em quest�o.
	 */
	public Collection getIdsMensagem() 
	{
		return idsMensagem;
	}

	/**
	 * Define a lista de IDs de mensagem (SMS) gerada pelo Tangram para o 
	 * destinat�rio em quest�o.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	public void setIdsMensagem(Collection idsMensagem) 
	{
		this.idsMensagem = idsMensagem;
	}

	/**
	 * Obt�m o identificador do pacote de mensagens para o destinat�rio. 
	 * Presente apenas se a requisi��o informar os dados do pacote. 
	 * Este identificar ser� informado na notifica��o do Tangram.
	 */
	public String getIdPacote() 
	{
		return idPacote;
	}

	/**
	 * Define o identificador do pacote de mensagens para o destinat�rio. 
	 */
	public void setIdPacote(String idPacote) 
	{
		this.idPacote = idPacote;
	}

	/**
	 * Obt�m identificador da sess�o do destinat�rio da mensagens com a aplica��o.
	 */
	public String getIdSessao() 
	{
		return idSessao;
	}

	/**
	 * Define identificador da sess�o do destinat�rio da mensagens com a aplica��o.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	public void setIdSessao(String idSessao) 
	{
		this.idSessao = idSessao;
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
	
	public boolean equals(Object obj) 
	{
		if (obj == null || ! (obj instanceof DestinoMensagem))
			return false;
		
		if (this == obj)
			return true;
		
		if (this.getIdtMsisdnDestino() != null && 
			((DestinoMensagem)obj).getIdtMsisdnDestino() != null)
		{
			return ((DestinoMensagem)obj).getIdtMsisdnDestino().equals(this.getIdtMsisdnDestino());
		}
		
		return false;
	}
	
}
