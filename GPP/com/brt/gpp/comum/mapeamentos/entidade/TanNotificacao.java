package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.Date;

/**
 *	Classe entidade que representa a tabela TBL_TAN_NOTIFICACAO
 *  
 *  @author Jorge Abreu
 *  Criado em: 23/10/2007
 */
public class TanNotificacao implements Serializable
{
	private static final long serialVersionUID = -9023986136731725620L;

	/**
	 * Chave primaria sequencial da entidade.
	 */
	private int idNotificacao;
	
	/**
	 * Codigo de status da notificação do Tangram.
	 */
	private Short idtStatusNotificacao;
	
	/**
	 * MSISDN do destinatário.
	 */
	private TanDestinoMensagem destinoMensagem;
	
	/**
	 * Data e hora da geração da notificação.
	 */
	private Date dataNotificacao;
	
	/**
	 * Código de retorno, gerado pela aplicação, relativo ao resultado 
	 * do processamento da notificação.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 * Valores: 0 = sucesso, outros = falha
	 */
	private int codRetornoAplicacao;
	

	/**
	 * Obtém o ID da notificação.
	 */
	public int getIdNotificacao() 
	{
		return idNotificacao;
	}

	/**
	 * Define o ID da notificação.
	 */
	public void setIdNotificacao(int idNotificacao) 
	{
		this.idNotificacao = idNotificacao;
	}

	/**
	 * Obtém data e hora da geração da notificação.
	 */
	public Date getDataNotificacao() 
	{
		return dataNotificacao;
	}

	/**
	 * Define data e hora da geração da notificação.
	 */
	public void setDataNotificacao(Date dataNotificacao) 
	{
		this.dataNotificacao = dataNotificacao;
	}

	/**
	 * Obtém a identificação da mensagem SMS no Tangram para 
	 * o destinatário em questao.
	 */
	public TanDestinoMensagem getDestinoMensagem() 
	{
		return destinoMensagem;
	}

	/**
	 * Define a identificação da mensagem SMS no Tangram para 
	 * o destinatário em questao.
	 */
	public void setDestinoMensagem(TanDestinoMensagem destinoMensagem) 
	{
		this.destinoMensagem = destinoMensagem;
	}

	/**
	 * Obtém o codigo de status da notificação do Tangram.
	 */
	public Short getIdtStatusNotificacao() 
	{
		return idtStatusNotificacao;
	}

	/**
	 * Define o codigo de status da notificação do Tangram.
	 */
	public void setIdtStatusNotificacao(Short idtStatusNotificacao) 
	{
		this.idtStatusNotificacao = idtStatusNotificacao;
	}

	/**
	 * Obtém o código de retorno, gerado pela aplicação, relativo ao resultado 
	 * do processamento da notificação.
	 * Valores: 0 = sucesso, outros = falha
	 */
	public int getCodRetornoAplicacao() 
	{
		return codRetornoAplicacao;
	}

	/**
	 * Define o código de retorno, gerado pela aplicação, relativo ao resultado 
	 * do processamento da notificação.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	public void setCodRetornoAplicacao(int codRetornoAplicacao) 
	{
		this.codRetornoAplicacao = codRetornoAplicacao;
	}	
	
}
