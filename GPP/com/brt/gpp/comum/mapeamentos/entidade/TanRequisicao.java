package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *	Classe entidade que representa a tabela TBL_TAN_REQUISICAO. 
 * 
 *  @author Jorge Abreu
 *  Criado em: 23/10/2007
 */

public class TanRequisicao implements Serializable
{
	private static final long serialVersionUID = -1793993219037587669L;

	/** 
	 * Chave sequencial da entidade.
	 */
	private int idRequisicao;
	
	/** 
	 * N�mero identificador da empresa.
	 */
	private int idEmpresa;
	
	/** 
	 * N�mero identificador do Servi�o.
	 */
	private int idServico;
	
	/** 
	 * N�mero identificador do Canal do Servi�o.  
	 */
	private int idCanal;

	/** 
	 * Originador da Mensagem. 
	 */
	private String idtOrigem;
	
	/** 
	 * URL de notificacao da Mensagem. 
	 */
	private String urlNotificacao;
	
	/** 
	 * Texto(s) da mensagem a ser enviada. 
	 * Cole��o de <code>TanConteudoMensagem<code>.
	 */
	private Collection conteudosMensagem;
	
	/**
	 * Data e hora para envio programado da mensagem.
	 */
	private Date dataAgendamento;
	
	/**
	 * Indica se o agendamento � relativo.
	 */
	private Boolean indAgendamentoRelativo;

	/**
	 * Data e hora de envio da requisi��o para Tangram.
	 */
	private Date dataRequisicao;
	
	/**
	 * Indica o resultado da requisi��o. 
	 * PARAMETRO GERADO PELO TANGRAM.
	 * Valores: 0 = sucesso, outros = c�digo de erro da falha encontrada
	 */
	private Short codRetorno;
	
	/**
	 * Texto livre a ser utilizado pela aplica��o. 
	 */
	private String appSpecific;
	
	/**
	 * Identificador da transa��o gerado pela Aplica��o de forma circular, 
	 * garantindo uma unicidade em dado intervalo de tempo. 
	 * Este identificador permite que o Tangram reconhe�a pedidos repetidos.
	 */
	private String appRequestId;
	
	/**
	 * Indica se o Proxy est� ativo.
	 */
	private Boolean indProxyAtivo;
	
	/**
	 * Adiciona um conte�do de mensagem do tipo Texto. 
	 * N�o ultrapasse o limite de caracteres SMS da plataforma.
	 * 
	 * Para adicionar conteudos personalizados (do tipo bin�rio e com 
	 * cabe�alho IDH, por exemplo) fa�a a inser��o manual na 
	 * collection <code>conteudosMensagem</code>.
	 *  
	 * @param texto Texto da mensagem SMS.
	 */
	public void adicionarConteudo(String texto)
	{
		if (this.conteudosMensagem == null)
		{
			this.conteudosMensagem = new ArrayList();
		}

		TanConteudoMensagem conteudo = new TanConteudoMensagem();
		conteudo.setTextoConteudo(texto);
		conteudo.setIndBinario(Boolean.FALSE);
		conteudo.setIndTruncamento(Boolean.FALSE);
		this.conteudosMensagem.add(conteudo);
	}
	
	/**
	 * Obt�m o ID da Requisicao
	 */
	public int getIdRequisicao() 
	{
		return idRequisicao;
	}

	/**
	 * Define o ID da Requisi��o. 
	 */
	public void setIdRequisicao(int idRequisicao) 
	{
		this.idRequisicao = idRequisicao;
	}
	
	/**
	 * Obt�m data e hora para envio programado da mensagem.
	 */
	public Date getDataAgendamento() 
	{
		return dataAgendamento;
	}

	/**
	 * Define data e hora para envio programado da mensagem.
	 */
	public void setDataAgendamento(Date dataAgendamento) 
	{
		this.dataAgendamento = dataAgendamento;
	}

	/**
	 * Obt�m o identificador da transa��o. Campo gerado pela aplica��o.
	 */
	public String getAppRequestId() 
	{
		return appRequestId;
	}

	/**
	 * Define o identificador da transa��o. Deve ser gerado pela aplica��o 
	 * de forma circular, garantindo uma unicidade em dado intervalo de tempo. 
	 * Este identificador permite que o Tangram reconhe�a pedidos repetidos.
	 */
	public void setAppRequestId(String appRequestId) 
	{
		this.appRequestId = appRequestId;
	}

	/**
	 * Texto livre a ser utilizado pela aplica��o. 
	 */
	public String getAppSpecific() 
	{
		return appSpecific;
	}

	/**
	 * Texto livre a ser utilizado pela aplica��o. 
	 */
	public void setAppSpecific(String appSpecific) 
	{
		this.appSpecific = appSpecific;
	}

	/**
	 * Obt�m o c�digo de resultado da requisi��o. 
	 * Valores: 0 = sucesso, outros = c�digo de erro da falha encontrada
	 */
	public Short getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * Define o c�digo de resultado da requisi��o. 
	 */
	public void setCodRetorno(Short codRetorno) 
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * Obt�m a lista de <code>ConteudoMensagemTangram<code>.
	 */
	public Collection getConteudosMensagem() 
	{
		return conteudosMensagem;
	}

	/**
	 * Define a lista de <code>ConteudoMensagemTangram<code>.
	 */
	public void setConteudosMensagem(Collection conteudosMensagem) 
	{
		this.conteudosMensagem = conteudosMensagem;
	}

	/**
	 * Obt�m a data e hora de envio da requisi��o para Tangram.
	 */
	public Date getDataRequisicao() 
	{
		return dataRequisicao;
	}

	/**
	 * Define a data e hora de envio da requisi��o para Tangram.
	 */
	public void setDataRequisicao(Date dataRequisicao) 
	{
		this.dataRequisicao = dataRequisicao;
	}

	/**
	 * Obt�m o n�mero identificador do Canal do Servi�o. 
	 */
	public int getIdCanal() 
	{
		return idCanal;
	}

	/**
	 * Define o n�mero identificador do Canal do Servi�o. 
	 * OBRIGAT�RIO caso o servi�o possua mais de um canal.
	 */
	public void setIdCanal(int idCanal) 
	{
		this.idCanal = idCanal;
	}

	/**
	 * Obt�m o n�mero identificador da empresa.
	 */
	public int getIdEmpresa() 
	{
		return idEmpresa;
	}

	/**
	 * Define o n�mero identificador da empresa.
	 */
	public void setIdEmpresa(int idEmpresa) 
	{
		this.idEmpresa = idEmpresa;
	}

	/**
	 * Obt�m o n�mero identificador do Servi�o.
	 */
	public int getIdServico() 
	{
		return idServico;
	}

	/**
	 * Define o n�mero identificador do Servi�o.
	 */
	public void setIdServico(int idServico) 
	{
		this.idServico = idServico;
	}

	/**
	 * Obt�m o originador da Mensagem. 
	 */
	public String getIdtOrigem() 
	{
		return idtOrigem;
	}

	/**
	 * Define o originador da Mensagem. 
	 */
	public void setIdtOrigem(String idtOrigem) 
	{
		this.idtOrigem = idtOrigem;
	}
	
	/**
	 * Obt�m a url de notificacao. 
	 */
	public String getUrlNotificacao() 
	{
		return urlNotificacao;
	}

	/**
	 * Define a url de Notificacao. 
	 */
	public void setUrlNotificacao(String urlNotificacao) 
	{
		this.urlNotificacao = urlNotificacao;
	}

	/**
	 * Obt�m o indicador se o agendamento � relativo.
	 * Agendamento relativo indica a quantidade de tempo que dever� ser 
	 * aguardada para que a mensagem seja entregue. Agendamento absoluto 
	 * indica a data e hora para envio da mensagem.
	 * 
	 */
	public Boolean getIndAgendamentoRelativo() 
	{
		return indAgendamentoRelativo;
	}

	/**
	 * Define se o indicador se o agendamento � relativo.
	 */
	public void setIndAgendamentoRelativo(Boolean indAgendamentoRelativo) 
	{
		this.indAgendamentoRelativo = indAgendamentoRelativo;
	}
	
	/**
	 * Obt�m o indicador de Proxy Ativo.
	 */
	public Boolean getIndProxyAtivo() 
	{
		return indProxyAtivo;
	}

	/**
	 * Define se o Proxy estar� ativo.
	 */
	public void setIndProxyAtivo(Boolean indProxyAtivo) 
	{
		this.indProxyAtivo = indProxyAtivo;
	}
		
}
