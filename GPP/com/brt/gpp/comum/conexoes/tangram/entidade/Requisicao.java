package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *	Classe que representa o conjunto de parâmetros necessários para uma
 *  requisição ao Tangram. 
 *  
 *  Os principais parâmetros de uma requisição Tangram são:
 *  
 *  - Ids da Empresa, Serviço e Canal
 *  - Número de Origem
 *  - Lista de destinatários (Msisdn)
 *  - Conjunto de conteúdos de mensagem
 *  - Parâmetros de notificação (callback assíncrono do Tangram)
 *  
 *  Cada destinatário é representado por uma entidade
 *  DestinoMensagemTangram. Essa entidade encapsula também alguns 
 *  parâmetros de retorno do Tangram no momento do envio da requisição.
 *  
 *  Cada conteúdo de mensagem, representado pela entidade
 *  ConteudoMensagemTangram, é na verdade um SMS que compõe
 *  a mensagem como um todo. Esse conteúdo pode ser uma parte do 
 *  texto ou mesmo de um arquivo binário.
 * 
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class Requisicao implements Serializable
{
	private static final long serialVersionUID = -6725744254037730944L;

	/**
	 * Data e hora de envio da requisição para Tangram.
	 * Após o envio da requisição, o Tangram informa o timestamp da reposta.
	 * Uma vez que requisição e resposta são síncronos, então essas datas são 
	 * muito próximas.
	 * OBRIGATÓRIO. 
	 */
	private Date dataEnvioRequisicao;
	
	/** 
	 * Número identificador da empresa.
	 * OBRIGATÓRIO. 
	 */
	private int idEmpresa;
	
	/** 
	 * Número identificador do Serviço.
	 * OBRIGATÓRIO.
	 */
	private int idServico;
	
	/** 
	 * Número identificador do Canal do Serviço. 
	 * OBRIGATÓRIO caso o serviço possua mais de um canal. 
	 */
	private Integer idCanal;

	/** 
	 * Originador da Mensagem. 
	 * OBRIGATÓRIO para uso da API do GPP. 
	 */
	private String idtOrigem;
	
	/** 
	 * Destinatários da Mensagem. 
	 * Coleção de <code>DestinoMensagemTangram<code>.
	 * OBRIGATÓRIO para uso da API do GPP. 
	 */
	private Collection destinosMensagem;
	
	/** 
	 * Texto(s) da mensagem a ser enviada. 
	 * Coleção de <code>ConteudoMensagemTangram<code>.
	 * OBRIGATÓRIO para uso da API do GPP. 
	 */
	private Collection conteudosMensagem;
	
	/**
	 * Conjunto de parâmetros de notificação. O Tangram utilizará
	 * essas informações para fazer o callback assíncrono contendo
	 * status de envio de cada conteúdo de mensagem. 
	 */
	private ParametrosNotificacao parametrosNotificacao;
	
	/**
	 * Conjunto de parâmetros de um pacote Tangram. 
	 */
	private ParametrosPacote parametrosPacote;
	
	/**
	 * Indica se a validade é relativa.
	 * Validade relativa indica a quantidade de tempo máxima para entrega 
	 * da mensagem antes que ela seja descartada. Validade absoluta 
	 * indica a data e hora para expirtação da mensagem.
	 */
	private Boolean indValidadeRelativa;
	
	/**
	 * Data e hora de validade da mensagem.
	 */
	private Date dataValidade;
	
	/**
	 * Indica se o agendamento é relativo.
	 * Agendamento relativo indica a quantidade de tempo que deverá ser 
	 * aguardada para que a mensagem seja entregue. Agendamento absoluto 
	 * indica a data e hora para envio da mensagem.
	 */
	private Boolean indAgendamentoRelativo;
	
	/**
	 * Data e hora para envio programado da mensagem.
	 */
	private Date dataAgendamento;
	
	/** 
	 * Determina se a mensagem a ser enviada iniciará uma sessão MO 
	 * entre a aplicação e o(s) destinatário(s).
	 */
	private Boolean indManterSessao;
	
	/**
	 * Número máximo de tentativas de entrega da mensagem.
	 */
	private Integer numTentativasEntrega;
	
	/**
	 * Número de minutos entre as tentativas de entrega da mensagem.
	 */
	private Integer intervaloTentativa;
	
	/**
	 * Identificador da mensagem MO que originou a mensagem a ser enviada.
	 */
	private String idMensagemMO;
	
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
	 * Indica o resultado da requisição. 
	 * PARAMETRO GERADO PELO TANGRAM.
	 * Valores: 0 = sucesso, outros = código de erro da falha encontrada
	 */
	private Short codRetorno;
	
	/**
	 * Data e hora da resposta (síncrona) do Tangram para essa requisição.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	private Date dataResposta;	
	
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

		ConteudoMensagem conteudo = new ConteudoMensagem();
		conteudo.setTextoConteudo(texto);
		conteudo.setIndBinario(Boolean.FALSE);
		conteudo.setIndTruncamento(Boolean.FALSE);
		this.conteudosMensagem.add(conteudo);
	}
	
	/**
	 * Adiciona um destinatário (MSISDN) para a mensagem.
	 * 
	 * @param Msisdn MSISDN de destino.
	 */
	public void adicionarDestino(String Msisdn)
	{
		if (this.destinosMensagem == null)
		{
			this.destinosMensagem = new ArrayList();
		}
		
		DestinoMensagem destino = new DestinoMensagem();
		destino.setIdtMsisdnDestino(Msisdn);
		this.destinosMensagem.add(destino);
	}
	
	/**
	 * Configura uma notificacao do tipo URL (retorno HTTP/POST) 
	 * para todos os tipos de eventos do Tangram.
	 * 
	 * @param URL URL da servlet responsável pela captura da notificação.
	 */
	public void setNotificacaoHttp(String URL)
	{
		ParametrosNotificacao notificacao = new ParametrosNotificacao();
		
		notificacao.setTiposEvento(new Short((short)(
				ParametrosNotificacao.EVENTO_SUCESSO_ENTREGA_SMSC 	|
				ParametrosNotificacao.EVENTO_FALHA_ENTREGA_SMSC 		|
				ParametrosNotificacao.EVENTO_SUCESSO_ENTREGA_CELULAR |
				ParametrosNotificacao.EVENTO_FALHA_ENTREGA_CELULAR 	|
				ParametrosNotificacao.EVENTO_NOTIFICACAO_COBRANCA )));
		
		notificacao.setTipoRetorno(ParametrosNotificacao.RETORNO_HTTP_POST);
		notificacao.setEnderecoRetorno(URL);
		
		this.parametrosNotificacao = notificacao;
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
	 * Obtém a data e hora da resposta (síncrona) do Tangram 
	 * para essa requisição.
	 */
	public Date getDataResposta() 
	{
		return dataResposta;
	}

	/**
	 * Define a data e hora da resposta (síncrona) do Tangram 
	 * para essa requisição.
	 */
	public void setDataResposta(Date dataResposta) 
	{
		this.dataResposta = dataResposta;
	}

	/**
	 * Obtém a lista de destinatários da Mensagem. 
	 * Coleção de <code>DestinoMensagemTangram<code>.
	 */
	public Collection getDestinosMensagem() 
	{
		return destinosMensagem;
	}

	/**
	 * Define a lista de destinatários da Mensagem. 
	 * Coleção de <code>DestinoMensagemTangram<code>.
	 */
	public void setDestinosMensagem(Collection destinosMensagem) 
	{
		this.destinosMensagem = destinosMensagem;
	}

	/**
	 * Obtém o número identificador do Canal do Serviço. 
	 */
	public Integer getIdCanal() 
	{
		return idCanal;
	}

	/**
	 * Define o número identificador do Canal do Serviço. 
	 * OBRIGATÓRIO caso o serviço possua mais de um canal.
	 */
	public void setIdCanal(Integer idCanal) 
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
	 * Obtém o identificador da mensagem MO.
	 */
	public String getIdMensagemMO() 
	{
		return idMensagemMO;
	}

	/**
	 * Define o identificador da mensagem MO.
	 */
	public void setIdMensagemMO(String idMensagemMO) 
	{
		this.idMensagemMO = idMensagemMO;
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
	 * Obtém o indicador se a mensagem a ser enviada iniciará uma sessão MO 
	 * entre a aplicação e o(s) destinatário(s).
	 */
	public Boolean getIndManterSessao() 
	{
		return indManterSessao;
	}

	/**
	 * Define se a mensagem a ser enviada iniciará uma sessão MO 
	 * entre a aplicação e o(s) destinatário(s). 
	 */
	public void setIndManterSessao(Boolean indManterSessao) 
	{
		this.indManterSessao = indManterSessao;
	}

	/**
	 * Obtém o indicador se a validade é relativa.
	 * Validade relativa indica a quantidade de tempo máxima para entrega 
	 * da mensagem antes que ela seja descartada. Validade absoluta 
	 * indica a data e hora para expirtação da mensagem.
	 */
	public Boolean getIndValidadeRelativa() 
	{
		return indValidadeRelativa;
	}

	/**
	 * Define se a validade é relativa.
	 */
	public void setIndValidadeRelativa(Boolean indValidadeRelativa) 
	{
		this.indValidadeRelativa = indValidadeRelativa;
	}

	/**
	 * Obtém o número de minutos entre as tentativas de entrega da mensagem.
	 */
	public Integer getIntervaloTentativa() 
	{
		return intervaloTentativa;
	}

	/**
	 * Define o número de minutos entre as tentativas de entrega da mensagem.
	 */
	public void setIntervaloTentativa(Integer intervaloTentativa) 
	{
		this.intervaloTentativa = intervaloTentativa;
	}

	/**
	 * Obtém o número máximo de tentativas de entrega da mensagem.
	 */
	public Integer getNumTentativasEntrega() 
	{
		return numTentativasEntrega;
	}

	/**
	 * Define o número máximo de tentativas de entrega da mensagem.
	 */
	public void setNumTentativasEntrega(Integer numTentativasEntrega) 
	{
		this.numTentativasEntrega = numTentativasEntrega;
	}

	/**
	 * Obtém o conjunto de parâmetros de notificação. 
	 */
	public ParametrosNotificacao getParametrosNotificacao() 
	{
		return parametrosNotificacao;
	}

	/**
	 * Define o conjunto de parâmetros de notificação. O Tangram utilizará
	 * essas informações para fazer o callback assíncrono contendo
	 * status de envio de cada conteúdo de mensagem.
	 */
	public void setParametrosNotificacao(
			ParametrosNotificacao parametrosNotificacao) 
	{
		this.parametrosNotificacao = parametrosNotificacao;
	}

	/**
	 * Obtém o conjunto de parâmetros de um pacote Tangram. 
	 */
	public ParametrosPacote getParametrosPacote() 
	{
		return parametrosPacote;
	}

	/**
	 * Define o conjunto de parâmetros de um pacote Tangram. 
	 */
	public void setParametrosPacote(ParametrosPacote parametrosPacote) 
	{
		this.parametrosPacote = parametrosPacote;
	}

	/**
	 * Obtém data e hora de validade da mensagem.
	 */
	public Date getDataValidade() 
	{
		return dataValidade;
	}

	/**
	 * Define data e hora de validade da mensagem.
	 */
	public void setDataValidade(Date dataValidade) 
	{
		this.dataValidade = dataValidade;
	}
		
}
