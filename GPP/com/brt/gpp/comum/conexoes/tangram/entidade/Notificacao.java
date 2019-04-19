package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;
import java.util.Date;

/**
 *	Classe que representa os dados gerados por uma notifica��o 
 *  (ass�ncrona) do Tangram.
 *  
 *  � gerada uma ou mais notifica��es para cada idMensagem (SMS) de
 *  uma determinada requisi��o. As poss�veis notifica��es est�o relacionadas
 *  com os eventos habilitados nos parametros de notificicao da requisi��o.
 *  
 *  Observe que a RequisicaoTangram possui um conjunto de conte�dos de mensagem
 *  (ConteudoMensagemTangram). Cada conte�do de mensagem representa um SMS a
 *  ser enviado para cada destinat�rio da requisi��o. Assim, a quantidade 
 *  total de SMS gerados no Tangram � igual ao produto: 
 *   
 *  qtd_SMS = qtd. de conteudos de mensagem X qtd. de destinatarios
 *  
 *  Considerando que cada SMS possui seu idMensagem, ent�o temos que ser�o
 *  geradas qtd_Notificacoes, onde:
 *  
 *  qtd_Notificacoes = qtd_SMS * tipos de eventos habilitados
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class Notificacao implements Serializable
{
	private static final long serialVersionUID = 8173702686349189021L;

	/**
	 * Codigo de status da notifica��o do Tangram.
	 */
	private Short statusNotificacao;
	
	/**
	 * Identificador do Dispatcher que enviou ou enviaria a mensagem SMS.
	 */
	private Integer idEntregador;
	
	/**
	 * Identifica��o da mensagem SMS no Tangram para o destinat�rio em questao.
	 */
	private String idMensagem;
	
	/**
	 * Identifica��o da mensagem SMS na plataforma.
	 */
	private String idMensagemSMSC;
	
	/**
	 * Originador da mensagem.
	 */
	private String idtOrigem;
	
	/**
	 * MSISDN do destinat�rio.
	 */
	private String idtMsisdnDestino;
	
	/**
	 * Data e hora de envio da requisi��o para Tangram.
	 */
	private Date dataEnvioRequisicao;
	
	/**
	 * Data e hora da gera��o da notifica��o.
	 */
	private Date dataNotificacao;
	
	/**
	 * Identificador do pacote da mensagem original.
	 */
	private String idPacote;
	
	/**
	 * �ndice da mensagem original no pacote.
	 */
	private Integer indicePacote;
	
	/**
	 * Texto livre utilizado pela aplica��o na mensagem original. 
	 */
	private String appSpecific;
	
	/**
	 * C�digo de retorno, gerado pela aplica��o, relativo ao resultado 
	 * do processamento da notifica��o.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 * Valores: 0 = sucesso, outros = falha
	 */
	private Integer codRetornoAplicacao;
	
	/**
	 * Mensagem descritiva, gerado pela aplica��o, sobre o resultado 
	 * do processamento da notifica��o.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	private String descRetornoAplicacao;
	
	/**
	 * Obt�m a data e hora de envio da requisi��o para Tangram.
	 */
	public Date getDataEnvioRequisicao() 
	{
		return dataEnvioRequisicao;
	}

	/**
	 * Define a data e hora de envio da requisi��o para Tangram.
	 */
	public void setDataEnvioRequisicao(Date dataEnvioRequisicao) 
	{
		this.dataEnvioRequisicao = dataEnvioRequisicao;
	}
	
	/**
	 * Obt�m o texto livre utilizado pela aplica��o na mensagem original. 
	 */
	public String getAppSpecific() 
	{
		return appSpecific;
	}

	/**
	 * Define o texto livre utilizado pela aplica��o na mensagem original. 
	 */
	public void setAppSpecific(String appSpecific) 
	{
		this.appSpecific = appSpecific;
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
	 * Obt�m o identificador do Dispatcher que enviou ou 
	 * enviaria a mensagem SMS.
	 */
	public Integer getIdEntregador() 
	{
		return idEntregador;
	}

	/**
	 * Define o identificador do Dispatcher que enviou ou 
	 * enviaria a mensagem SMS.
	 */
	public void setIdEntregador(Integer idEntregador) 
	{
		this.idEntregador = idEntregador;
	}

	/**
	 * Obt�m a identifica��o da mensagem SMS no Tangram para 
	 * o destinat�rio em questao.
	 */
	public String getIdMensagem() 
	{
		return idMensagem;
	}

	/**
	 * Define a identifica��o da mensagem SMS no Tangram para 
	 * o destinat�rio em questao.
	 */
	public void setIdMensagem(String idMensagem) 
	{
		this.idMensagem = idMensagem;
	}

	/**
	 * Obt�m a identifica��o da mensagem SMS na plataforma.
	 */
	public String getIdMensagemSMSC() 
	{
		return idMensagemSMSC;
	}

	/**
	 * Define a identifica��o da mensagem SMS na plataforma.
	 */
	public void setIdMensagemSMSC(String idMensagemSMSC) 
	{
		this.idMensagemSMSC = idMensagemSMSC;
	}

	/**
	 * Obt�m o MSISDN do destinat�rio.
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
	 * Obt�m o originador da mensagem.
	 */
	public String getIdtOrigem() 
	{
		return idtOrigem;
	}

	/**
	 * Define o originador da mensagem.
	 */
	public void setIdtOrigem(String idtOrigem) 
	{
		this.idtOrigem = idtOrigem;
	}

	/**
	 * Obt�m o codigo de status da notifica��o do Tangram.
	 */
	public Short getStatusNotificacao() 
	{
		return statusNotificacao;
	}

	/**
	 * Define o codigo de status da notifica��o do Tangram.
	 */
	public void setStatusNotificacao(Short statusNotificacao) 
	{
		this.statusNotificacao = statusNotificacao;
	}

	/**
	 * Obt�m o identificador do pacote da mensagem original.
	 */
	public String getIdPacote() 
	{
		return idPacote;
	}

	/**
	 * Define o identificador do pacote da mensagem original.
	 */
	public void setIdPacote(String idPacote) 
	{
		this.idPacote = idPacote;
	}

	/**
	 * Obt�m o �ndice da mensagem original no pacote.
	 */
	public Integer getIndicePacote() 
	{
		return indicePacote;
	}

	/**
	 * Define o �ndice da mensagem original no pacote.
	 */
	public void setIndicePacote(Integer indicePacote) 
	{
		this.indicePacote = indicePacote;
	}

	/**
	 * Obt�m o c�digo de retorno, gerado pela aplica��o, relativo ao resultado 
	 * do processamento da notifica��o.
	 * Valores: 0 = sucesso, outros = falha
	 */
	public Integer getCodRetornoAplicacao() 
	{
		return codRetornoAplicacao;
	}

	/**
	 * Define o c�digo de retorno, gerado pela aplica��o, relativo ao resultado 
	 * do processamento da notifica��o.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	public void setCodRetornoAplicacao(Integer codRetornoAplicacao) 
	{
		this.codRetornoAplicacao = codRetornoAplicacao;
	}

	/**
	 * Obt�m a mensagem descritiva, gerado pela aplica��o, sobre o resultado 
	 * do processamento da notifica��o.
	 */
	public String getDescRetornoAplicacao() 
	{
		return descRetornoAplicacao;
	}

	/**
	 * Define a mensagem descritiva, gerado pela aplica��o, sobre o resultado 
	 * do processamento da notifica��o.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	public void setDescRetornoAplicacao(String descRetornoAplicacao) 
	{
		this.descRetornoAplicacao = descRetornoAplicacao;
	}
	
	
}
