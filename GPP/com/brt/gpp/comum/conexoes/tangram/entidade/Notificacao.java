package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;
import java.util.Date;

/**
 *	Classe que representa os dados gerados por uma notificação 
 *  (assíncrona) do Tangram.
 *  
 *  É gerada uma ou mais notificações para cada idMensagem (SMS) de
 *  uma determinada requisição. As possíveis notificações estão relacionadas
 *  com os eventos habilitados nos parametros de notificicao da requisição.
 *  
 *  Observe que a RequisicaoTangram possui um conjunto de conteúdos de mensagem
 *  (ConteudoMensagemTangram). Cada conteúdo de mensagem representa um SMS a
 *  ser enviado para cada destinatário da requisição. Assim, a quantidade 
 *  total de SMS gerados no Tangram é igual ao produto: 
 *   
 *  qtd_SMS = qtd. de conteudos de mensagem X qtd. de destinatarios
 *  
 *  Considerando que cada SMS possui seu idMensagem, então temos que serão
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
	 * Codigo de status da notificação do Tangram.
	 */
	private Short statusNotificacao;
	
	/**
	 * Identificador do Dispatcher que enviou ou enviaria a mensagem SMS.
	 */
	private Integer idEntregador;
	
	/**
	 * Identificação da mensagem SMS no Tangram para o destinatário em questao.
	 */
	private String idMensagem;
	
	/**
	 * Identificação da mensagem SMS na plataforma.
	 */
	private String idMensagemSMSC;
	
	/**
	 * Originador da mensagem.
	 */
	private String idtOrigem;
	
	/**
	 * MSISDN do destinatário.
	 */
	private String idtMsisdnDestino;
	
	/**
	 * Data e hora de envio da requisição para Tangram.
	 */
	private Date dataEnvioRequisicao;
	
	/**
	 * Data e hora da geração da notificação.
	 */
	private Date dataNotificacao;
	
	/**
	 * Identificador do pacote da mensagem original.
	 */
	private String idPacote;
	
	/**
	 * Índice da mensagem original no pacote.
	 */
	private Integer indicePacote;
	
	/**
	 * Texto livre utilizado pela aplicação na mensagem original. 
	 */
	private String appSpecific;
	
	/**
	 * Código de retorno, gerado pela aplicação, relativo ao resultado 
	 * do processamento da notificação.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 * Valores: 0 = sucesso, outros = falha
	 */
	private Integer codRetornoAplicacao;
	
	/**
	 * Mensagem descritiva, gerado pela aplicação, sobre o resultado 
	 * do processamento da notificação.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	private String descRetornoAplicacao;
	
	/**
	 * Obtém a data e hora de envio da requisição para Tangram.
	 */
	public Date getDataEnvioRequisicao() 
	{
		return dataEnvioRequisicao;
	}

	/**
	 * Define a data e hora de envio da requisição para Tangram.
	 */
	public void setDataEnvioRequisicao(Date dataEnvioRequisicao) 
	{
		this.dataEnvioRequisicao = dataEnvioRequisicao;
	}
	
	/**
	 * Obtém o texto livre utilizado pela aplicação na mensagem original. 
	 */
	public String getAppSpecific() 
	{
		return appSpecific;
	}

	/**
	 * Define o texto livre utilizado pela aplicação na mensagem original. 
	 */
	public void setAppSpecific(String appSpecific) 
	{
		this.appSpecific = appSpecific;
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
	 * Obtém o identificador do Dispatcher que enviou ou 
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
	 * Obtém a identificação da mensagem SMS no Tangram para 
	 * o destinatário em questao.
	 */
	public String getIdMensagem() 
	{
		return idMensagem;
	}

	/**
	 * Define a identificação da mensagem SMS no Tangram para 
	 * o destinatário em questao.
	 */
	public void setIdMensagem(String idMensagem) 
	{
		this.idMensagem = idMensagem;
	}

	/**
	 * Obtém a identificação da mensagem SMS na plataforma.
	 */
	public String getIdMensagemSMSC() 
	{
		return idMensagemSMSC;
	}

	/**
	 * Define a identificação da mensagem SMS na plataforma.
	 */
	public void setIdMensagemSMSC(String idMensagemSMSC) 
	{
		this.idMensagemSMSC = idMensagemSMSC;
	}

	/**
	 * Obtém o MSISDN do destinatário.
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

	/**
	 * Obtém o originador da mensagem.
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
	 * Obtém o codigo de status da notificação do Tangram.
	 */
	public Short getStatusNotificacao() 
	{
		return statusNotificacao;
	}

	/**
	 * Define o codigo de status da notificação do Tangram.
	 */
	public void setStatusNotificacao(Short statusNotificacao) 
	{
		this.statusNotificacao = statusNotificacao;
	}

	/**
	 * Obtém o identificador do pacote da mensagem original.
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
	 * Obtém o índice da mensagem original no pacote.
	 */
	public Integer getIndicePacote() 
	{
		return indicePacote;
	}

	/**
	 * Define o índice da mensagem original no pacote.
	 */
	public void setIndicePacote(Integer indicePacote) 
	{
		this.indicePacote = indicePacote;
	}

	/**
	 * Obtém o código de retorno, gerado pela aplicação, relativo ao resultado 
	 * do processamento da notificação.
	 * Valores: 0 = sucesso, outros = falha
	 */
	public Integer getCodRetornoAplicacao() 
	{
		return codRetornoAplicacao;
	}

	/**
	 * Define o código de retorno, gerado pela aplicação, relativo ao resultado 
	 * do processamento da notificação.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	public void setCodRetornoAplicacao(Integer codRetornoAplicacao) 
	{
		this.codRetornoAplicacao = codRetornoAplicacao;
	}

	/**
	 * Obtém a mensagem descritiva, gerado pela aplicação, sobre o resultado 
	 * do processamento da notificação.
	 */
	public String getDescRetornoAplicacao() 
	{
		return descRetornoAplicacao;
	}

	/**
	 * Define a mensagem descritiva, gerado pela aplicação, sobre o resultado 
	 * do processamento da notificação.
	 * PARAMETRO A SER ENVIADO PARA O TANGRAM.
	 */
	public void setDescRetornoAplicacao(String descRetornoAplicacao) 
	{
		this.descRetornoAplicacao = descRetornoAplicacao;
	}
	
	
}
