package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *	Classe que representa o conjunto de par�metros necess�rios para uma
 *  requisi��o ao Tangram. 
 *  
 *  Os principais par�metros de uma requisi��o Tangram s�o:
 *  
 *  - Ids da Empresa, Servi�o e Canal
 *  - N�mero de Origem
 *  - Lista de destinat�rios (Msisdn)
 *  - Conjunto de conte�dos de mensagem
 *  - Par�metros de notifica��o (callback ass�ncrono do Tangram)
 *  
 *  Cada destinat�rio � representado por uma entidade
 *  DestinoMensagemTangram. Essa entidade encapsula tamb�m alguns 
 *  par�metros de retorno do Tangram no momento do envio da requisi��o.
 *  
 *  Cada conte�do de mensagem, representado pela entidade
 *  ConteudoMensagemTangram, � na verdade um SMS que comp�e
 *  a mensagem como um todo. Esse conte�do pode ser uma parte do 
 *  texto ou mesmo de um arquivo bin�rio.
 * 
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class Requisicao implements Serializable
{
	private static final long serialVersionUID = -6725744254037730944L;

	/**
	 * Data e hora de envio da requisi��o para Tangram.
	 * Ap�s o envio da requisi��o, o Tangram informa o timestamp da reposta.
	 * Uma vez que requisi��o e resposta s�o s�ncronos, ent�o essas datas s�o 
	 * muito pr�ximas.
	 * OBRIGAT�RIO. 
	 */
	private Date dataEnvioRequisicao;
	
	/** 
	 * N�mero identificador da empresa.
	 * OBRIGAT�RIO. 
	 */
	private int idEmpresa;
	
	/** 
	 * N�mero identificador do Servi�o.
	 * OBRIGAT�RIO.
	 */
	private int idServico;
	
	/** 
	 * N�mero identificador do Canal do Servi�o. 
	 * OBRIGAT�RIO caso o servi�o possua mais de um canal. 
	 */
	private Integer idCanal;

	/** 
	 * Originador da Mensagem. 
	 * OBRIGAT�RIO para uso da API do GPP. 
	 */
	private String idtOrigem;
	
	/** 
	 * Destinat�rios da Mensagem. 
	 * Cole��o de <code>DestinoMensagemTangram<code>.
	 * OBRIGAT�RIO para uso da API do GPP. 
	 */
	private Collection destinosMensagem;
	
	/** 
	 * Texto(s) da mensagem a ser enviada. 
	 * Cole��o de <code>ConteudoMensagemTangram<code>.
	 * OBRIGAT�RIO para uso da API do GPP. 
	 */
	private Collection conteudosMensagem;
	
	/**
	 * Conjunto de par�metros de notifica��o. O Tangram utilizar�
	 * essas informa��es para fazer o callback ass�ncrono contendo
	 * status de envio de cada conte�do de mensagem. 
	 */
	private ParametrosNotificacao parametrosNotificacao;
	
	/**
	 * Conjunto de par�metros de um pacote Tangram. 
	 */
	private ParametrosPacote parametrosPacote;
	
	/**
	 * Indica se a validade � relativa.
	 * Validade relativa indica a quantidade de tempo m�xima para entrega 
	 * da mensagem antes que ela seja descartada. Validade absoluta 
	 * indica a data e hora para expirta��o da mensagem.
	 */
	private Boolean indValidadeRelativa;
	
	/**
	 * Data e hora de validade da mensagem.
	 */
	private Date dataValidade;
	
	/**
	 * Indica se o agendamento � relativo.
	 * Agendamento relativo indica a quantidade de tempo que dever� ser 
	 * aguardada para que a mensagem seja entregue. Agendamento absoluto 
	 * indica a data e hora para envio da mensagem.
	 */
	private Boolean indAgendamentoRelativo;
	
	/**
	 * Data e hora para envio programado da mensagem.
	 */
	private Date dataAgendamento;
	
	/** 
	 * Determina se a mensagem a ser enviada iniciar� uma sess�o MO 
	 * entre a aplica��o e o(s) destinat�rio(s).
	 */
	private Boolean indManterSessao;
	
	/**
	 * N�mero m�ximo de tentativas de entrega da mensagem.
	 */
	private Integer numTentativasEntrega;
	
	/**
	 * N�mero de minutos entre as tentativas de entrega da mensagem.
	 */
	private Integer intervaloTentativa;
	
	/**
	 * Identificador da mensagem MO que originou a mensagem a ser enviada.
	 */
	private String idMensagemMO;
	
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
	 * Indica o resultado da requisi��o. 
	 * PARAMETRO GERADO PELO TANGRAM.
	 * Valores: 0 = sucesso, outros = c�digo de erro da falha encontrada
	 */
	private Short codRetorno;
	
	/**
	 * Data e hora da resposta (s�ncrona) do Tangram para essa requisi��o.
	 * PARAMETRO GERADO PELO TANGRAM.
	 */
	private Date dataResposta;	
	
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

		ConteudoMensagem conteudo = new ConteudoMensagem();
		conteudo.setTextoConteudo(texto);
		conteudo.setIndBinario(Boolean.FALSE);
		conteudo.setIndTruncamento(Boolean.FALSE);
		this.conteudosMensagem.add(conteudo);
	}
	
	/**
	 * Adiciona um destinat�rio (MSISDN) para a mensagem.
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
	 * @param URL URL da servlet respons�vel pela captura da notifica��o.
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
	 * Obt�m a data e hora da resposta (s�ncrona) do Tangram 
	 * para essa requisi��o.
	 */
	public Date getDataResposta() 
	{
		return dataResposta;
	}

	/**
	 * Define a data e hora da resposta (s�ncrona) do Tangram 
	 * para essa requisi��o.
	 */
	public void setDataResposta(Date dataResposta) 
	{
		this.dataResposta = dataResposta;
	}

	/**
	 * Obt�m a lista de destinat�rios da Mensagem. 
	 * Cole��o de <code>DestinoMensagemTangram<code>.
	 */
	public Collection getDestinosMensagem() 
	{
		return destinosMensagem;
	}

	/**
	 * Define a lista de destinat�rios da Mensagem. 
	 * Cole��o de <code>DestinoMensagemTangram<code>.
	 */
	public void setDestinosMensagem(Collection destinosMensagem) 
	{
		this.destinosMensagem = destinosMensagem;
	}

	/**
	 * Obt�m o n�mero identificador do Canal do Servi�o. 
	 */
	public Integer getIdCanal() 
	{
		return idCanal;
	}

	/**
	 * Define o n�mero identificador do Canal do Servi�o. 
	 * OBRIGAT�RIO caso o servi�o possua mais de um canal.
	 */
	public void setIdCanal(Integer idCanal) 
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
	 * Obt�m o identificador da mensagem MO.
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
	 * Obt�m o indicador se a mensagem a ser enviada iniciar� uma sess�o MO 
	 * entre a aplica��o e o(s) destinat�rio(s).
	 */
	public Boolean getIndManterSessao() 
	{
		return indManterSessao;
	}

	/**
	 * Define se a mensagem a ser enviada iniciar� uma sess�o MO 
	 * entre a aplica��o e o(s) destinat�rio(s). 
	 */
	public void setIndManterSessao(Boolean indManterSessao) 
	{
		this.indManterSessao = indManterSessao;
	}

	/**
	 * Obt�m o indicador se a validade � relativa.
	 * Validade relativa indica a quantidade de tempo m�xima para entrega 
	 * da mensagem antes que ela seja descartada. Validade absoluta 
	 * indica a data e hora para expirta��o da mensagem.
	 */
	public Boolean getIndValidadeRelativa() 
	{
		return indValidadeRelativa;
	}

	/**
	 * Define se a validade � relativa.
	 */
	public void setIndValidadeRelativa(Boolean indValidadeRelativa) 
	{
		this.indValidadeRelativa = indValidadeRelativa;
	}

	/**
	 * Obt�m o n�mero de minutos entre as tentativas de entrega da mensagem.
	 */
	public Integer getIntervaloTentativa() 
	{
		return intervaloTentativa;
	}

	/**
	 * Define o n�mero de minutos entre as tentativas de entrega da mensagem.
	 */
	public void setIntervaloTentativa(Integer intervaloTentativa) 
	{
		this.intervaloTentativa = intervaloTentativa;
	}

	/**
	 * Obt�m o n�mero m�ximo de tentativas de entrega da mensagem.
	 */
	public Integer getNumTentativasEntrega() 
	{
		return numTentativasEntrega;
	}

	/**
	 * Define o n�mero m�ximo de tentativas de entrega da mensagem.
	 */
	public void setNumTentativasEntrega(Integer numTentativasEntrega) 
	{
		this.numTentativasEntrega = numTentativasEntrega;
	}

	/**
	 * Obt�m o conjunto de par�metros de notifica��o. 
	 */
	public ParametrosNotificacao getParametrosNotificacao() 
	{
		return parametrosNotificacao;
	}

	/**
	 * Define o conjunto de par�metros de notifica��o. O Tangram utilizar�
	 * essas informa��es para fazer o callback ass�ncrono contendo
	 * status de envio de cada conte�do de mensagem.
	 */
	public void setParametrosNotificacao(
			ParametrosNotificacao parametrosNotificacao) 
	{
		this.parametrosNotificacao = parametrosNotificacao;
	}

	/**
	 * Obt�m o conjunto de par�metros de um pacote Tangram. 
	 */
	public ParametrosPacote getParametrosPacote() 
	{
		return parametrosPacote;
	}

	/**
	 * Define o conjunto de par�metros de um pacote Tangram. 
	 */
	public void setParametrosPacote(ParametrosPacote parametrosPacote) 
	{
		this.parametrosPacote = parametrosPacote;
	}

	/**
	 * Obt�m data e hora de validade da mensagem.
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
