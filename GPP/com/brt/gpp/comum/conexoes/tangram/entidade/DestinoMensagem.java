package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;
import java.util.Collection;

/**
 *	Classe que representa um destinatário de mensagem a ser 
 *  enviada pelo Tangram.  
 *  
 *  Essa entidade encapsula também alguns parâmetros de retorno gerados
 *  pelo Tangram no momento do envio da requisição.
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class DestinoMensagem implements Serializable
{
	private static final long serialVersionUID = 1452515319139239578L;

	/**
	 * MSISDN do destinatário.
	 */
	private String idtMsisdnDestino;
	
	/**
	 * Código de retorno do Tangram para este destinatário. 
	 * PARAMETRO GERADO PELO TANGRAM.
	 * Valores: 0 = sucesso, outros = falha
	 */
	private Short codRetorno;
	
	/**
	 * Identificador da sessão do destinatário da mensagens com a aplicação.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	private String idSessao;
	
	/**
	 * Identificador do pacote de mensagens para o destinatário. 
	 * Presente apenas se a requisição informar os dados do pacote. 
	 * Este identificar será informado na notificação do Tangram.
	 */
	private String idPacote;
	
	/**
	 * Lista de IDs de mensagem (SMS) gerada pelo Tangram para o 
	 * destinatário em questão.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	private Collection idsMensagem;

	/**
	 * Obtém o código de retorno do Tangram para este destinatário. 
	 * Valores: 0 = sucesso, outros = falha
	 */
	public Short getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * Define o código de retorno do Tangram para este destinatário.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	public void setCodRetorno(Short codRetorno) 
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * Obtém lista de IDs de mensagem (SMS) gerada pelo Tangram para o 
	 * destinatário em questão.
	 */
	public Collection getIdsMensagem() 
	{
		return idsMensagem;
	}

	/**
	 * Define a lista de IDs de mensagem (SMS) gerada pelo Tangram para o 
	 * destinatário em questão.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	public void setIdsMensagem(Collection idsMensagem) 
	{
		this.idsMensagem = idsMensagem;
	}

	/**
	 * Obtém o identificador do pacote de mensagens para o destinatário. 
	 * Presente apenas se a requisição informar os dados do pacote. 
	 * Este identificar será informado na notificação do Tangram.
	 */
	public String getIdPacote() 
	{
		return idPacote;
	}

	/**
	 * Define o identificador do pacote de mensagens para o destinatário. 
	 */
	public void setIdPacote(String idPacote) 
	{
		this.idPacote = idPacote;
	}

	/**
	 * Obtém identificador da sessão do destinatário da mensagens com a aplicação.
	 */
	public String getIdSessao() 
	{
		return idSessao;
	}

	/**
	 * Define identificador da sessão do destinatário da mensagens com a aplicação.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	public void setIdSessao(String idSessao) 
	{
		this.idSessao = idSessao;
	}

	/**
	 * Obtém MSISDN do destinatário.
	 */
	public String getIdtMsisdnDestino() 
	{
		return idtMsisdnDestino;
	}

	/**
	 * Define o MSISDN do destinatário.
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
