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
	 * Codigo de status da notifica��o do Tangram.
	 */
	private Short idtStatusNotificacao;
	
	/**
	 * MSISDN do destinat�rio.
	 */
	private TanDestinoMensagem destinoMensagem;
	
	/**
	 * Data e hora da gera��o da notifica��o.
	 */
	private Date dataNotificacao;
	
	/**
	 * C�digo de retorno, gerado pela aplica��o, relativo ao resultado 
	 * do processamento da notifica��o.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 * Valores: 0 = sucesso, outros = falha
	 */
	private int codRetornoAplicacao;
	

	/**
	 * Obt�m o ID da notifica��o.
	 */
	public int getIdNotificacao() 
	{
		return idNotificacao;
	}

	/**
	 * Define o ID da notifica��o.
	 */
	public void setIdNotificacao(int idNotificacao) 
	{
		this.idNotificacao = idNotificacao;
	}

	/**
	 * Obt�m data e hora da gera��o da notifica��o.
	 */
	public Date getDataNotificacao() 
	{
		return dataNotificacao;
	}

	/**
	 * Define data e hora da gera��o da notifica��o.
	 */
	public void setDataNotificacao(Date dataNotificacao) 
	{
		this.dataNotificacao = dataNotificacao;
	}

	/**
	 * Obt�m a identifica��o da mensagem SMS no Tangram para 
	 * o destinat�rio em questao.
	 */
	public TanDestinoMensagem getDestinoMensagem() 
	{
		return destinoMensagem;
	}

	/**
	 * Define a identifica��o da mensagem SMS no Tangram para 
	 * o destinat�rio em questao.
	 */
	public void setDestinoMensagem(TanDestinoMensagem destinoMensagem) 
	{
		this.destinoMensagem = destinoMensagem;
	}

	/**
	 * Obt�m o codigo de status da notifica��o do Tangram.
	 */
	public Short getIdtStatusNotificacao() 
	{
		return idtStatusNotificacao;
	}

	/**
	 * Define o codigo de status da notifica��o do Tangram.
	 */
	public void setIdtStatusNotificacao(Short idtStatusNotificacao) 
	{
		this.idtStatusNotificacao = idtStatusNotificacao;
	}

	/**
	 * Obt�m o c�digo de retorno, gerado pela aplica��o, relativo ao resultado 
	 * do processamento da notifica��o.
	 * Valores: 0 = sucesso, outros = falha
	 */
	public int getCodRetornoAplicacao() 
	{
		return codRetornoAplicacao;
	}

	/**
	 * Define o c�digo de retorno, gerado pela aplica��o, relativo ao resultado 
	 * do processamento da notifica��o.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	public void setCodRetornoAplicacao(int codRetornoAplicacao) 
	{
		this.codRetornoAplicacao = codRetornoAplicacao;
	}	
	
}
