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
	 * Número identificador da empresa.
	 */
	private int idEmpresa;
	
	/** 
	 * Número identificador do Serviço.
	 */
	private int idServico;
	
	/** 
	 * Número identificador do Canal do Serviço.  
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
	 * Coleção de <code>TanConteudoMensagem<code>.
	 */
	private Collection conteudosMensagem;
	
	/**
	 * Data e hora para envio programado da mensagem.
	 */
	private Date dataAgendamento;
	
	/**
	 * Indica se o agendamento é relativo.
	 */
	private Boolean indAgendamentoRelativo;

	/**
	 * Data e hora de envio da requisição para Tangram.
	 */
	private Date dataRequisicao;
	
	/**
	 * Indica o resultado da requisição. 
	 * PARAMETRO GERADO PELO TANGRAM.
	 * Valores: 0 = sucesso, outros = código de erro da falha encontrada
	 */
	private Short codRetorno;
	
	/**
	 * Texto livre a ser utilizado pela aplicação. 
	 */
	private String appSpecific;
	
	/**
	 * Identificador da transação gerado pela Aplicação de forma circular, 
	 * garantindo uma unicidade em dado intervalo de tempo. 
	 * Este identificador permite que o Tangram reconheça pedidos repetidos.
	 */
	private String appRequestId;
	
	/**
	 * Indica se o Proxy está ativo.
	 */
	private Boolean indProxyAtivo;
	
	/**
	 * Adiciona um conteúdo de mensagem do tipo Texto. 
	 * Não ultrapasse o limite de caracteres SMS da plataforma.
	 * 
	 * Para adicionar conteudos personalizados (do tipo binário e com 
	 * cabeçalho IDH, por exemplo) faça a inserção manual na 
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
	 * Obtém o ID da Requisicao
	 */
	public int getIdRequisicao() 
	{
		return idRequisicao;
	}

	/**
	 * Define o ID da Requisição. 
	 */
	public void setIdRequisicao(int idRequisicao) 
	{
		this.idRequisicao = idRequisicao;
	}
	
	/**
	 * Obtém data e hora para envio programado da mensagem.
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
	 * Obtém o identificador da transação. Campo gerado pela aplicação.
	 */
	public String getAppRequestId() 
	{
		return appRequestId;
	}

	/**
	 * Define o identificador da transação. Deve ser gerado pela aplicação 
	 * de forma circular, garantindo uma unicidade em dado intervalo de tempo. 
	 * Este identificador permite que o Tangram reconheça pedidos repetidos.
	 */
	public void setAppRequestId(String appRequestId) 
	{
		this.appRequestId = appRequestId;
	}

	/**
	 * Texto livre a ser utilizado pela aplicação. 
	 */
	public String getAppSpecific() 
	{
		return appSpecific;
	}

	/**
	 * Texto livre a ser utilizado pela aplicação. 
	 */
	public void setAppSpecific(String appSpecific) 
	{
		this.appSpecific = appSpecific;
	}

	/**
	 * Obtém o código de resultado da requisição. 
	 * Valores: 0 = sucesso, outros = código de erro da falha encontrada
	 */
	public Short getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * Define o código de resultado da requisição. 
	 */
	public void setCodRetorno(Short codRetorno) 
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * Obtém a lista de <code>ConteudoMensagemTangram<code>.
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
	 * Obtém a data e hora de envio da requisição para Tangram.
	 */
	public Date getDataRequisicao() 
	{
		return dataRequisicao;
	}

	/**
	 * Define a data e hora de envio da requisição para Tangram.
	 */
	public void setDataRequisicao(Date dataRequisicao) 
	{
		this.dataRequisicao = dataRequisicao;
	}

	/**
	 * Obtém o número identificador do Canal do Serviço. 
	 */
	public int getIdCanal() 
	{
		return idCanal;
	}

	/**
	 * Define o número identificador do Canal do Serviço. 
	 * OBRIGATÓRIO caso o serviço possua mais de um canal.
	 */
	public void setIdCanal(int idCanal) 
	{
		this.idCanal = idCanal;
	}

	/**
	 * Obtém o número identificador da empresa.
	 */
	public int getIdEmpresa() 
	{
		return idEmpresa;
	}

	/**
	 * Define o número identificador da empresa.
	 */
	public void setIdEmpresa(int idEmpresa) 
	{
		this.idEmpresa = idEmpresa;
	}

	/**
	 * Obtém o número identificador do Serviço.
	 */
	public int getIdServico() 
	{
		return idServico;
	}

	/**
	 * Define o número identificador do Serviço.
	 */
	public void setIdServico(int idServico) 
	{
		this.idServico = idServico;
	}

	/**
	 * Obtém o originador da Mensagem. 
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
	 * Obtém a url de notificacao. 
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
	 * Obtém o indicador se o agendamento é relativo.
	 * Agendamento relativo indica a quantidade de tempo que deverá ser 
	 * aguardada para que a mensagem seja entregue. Agendamento absoluto 
	 * indica a data e hora para envio da mensagem.
	 * 
	 */
	public Boolean getIndAgendamentoRelativo() 
	{
		return indAgendamentoRelativo;
	}

	/**
	 * Define se o indicador se o agendamento é relativo.
	 */
	public void setIndAgendamentoRelativo(Boolean indAgendamentoRelativo) 
	{
		this.indAgendamentoRelativo = indAgendamentoRelativo;
	}
	
	/**
	 * Obtém o indicador de Proxy Ativo.
	 */
	public Boolean getIndProxyAtivo() 
	{
		return indProxyAtivo;
	}

	/**
	 * Define se o Proxy estará ativo.
	 */
	public void setIndProxyAtivo(Boolean indProxyAtivo) 
	{
		this.indProxyAtivo = indProxyAtivo;
	}
		
}
