package com.brt.gpp.aplicacoes.recarregar;

//Imports Java.
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

//Imports GPP.
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe que representa a entidade da tabela TBL_REC_RECARGAS.
 * 
 *	@author	Daniel Ferreira
 *	@since	20/09/2005
 */
public class Recarga
{

    private Long				idRecarga;
	private String				idtMsisdn;
	private Integer				idtPlanoPreco;
	private	String				tipTransacao;
	private String				idTipoCredito;
	private Double				idValor;
	private	Timestamp			datRecarga;
	private Timestamp			datOrigem;
	private String				nomOperador;
	private String				idTipoRecarga;
	private String				idtCpf;
	private String				numHashCc;
	private String				idtLoja;
	private Timestamp			datInclusao;
	private String				datContabil;
	private String				idtTerminal;
	private String				idtNsuInstituicao;
	private String				idCanal;
	private String				idOrigem;
	private String				idSistemaOrigem;
	private Double				vlrPago;
	private Double				vlrCreditoPrincipal;
	private Double				vlrCreditoPeriodico;
	private Double				vlrCreditoBonus;
	private Double				vlrCreditoSms;
	private Double				vlrCreditoGprs;
	private Double				vlrSaldoFinalPrincipal;
	private Double				vlrSaldoFinalPeriodico;
	private Double				vlrSaldoFinalBonus;
	private Double				vlrSaldoFinalSms;
	private Double				vlrSaldoFinalGprs;
	private Integer				numDiasExpPrincipal;
	private Integer				numDiasExpPeriodico;
	private Integer				numDiasExpBonus;
	private Integer				numDiasExpSms;
	private Integer				numDiasExpGprs;
	private	String				desObservacao;
	private SimpleDateFormat	conversorTimestamp;

	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public Recarga()
	{
	    this.idRecarga				= null;
	    this.idtMsisdn				= null;
	    this.idtPlanoPreco			= null;
	    this.tipTransacao			= null;
	    this.idTipoCredito			= null;
	    this.idValor				= null;
	    this.datRecarga				= null;
	    this.datOrigem				= null;
	    this.nomOperador			= null;
	    this.idTipoRecarga			= null;
	    this.idtCpf					= null;
	    this.numHashCc				= null;
	    this.idtLoja				= null;
	    this.datInclusao			= null;
	    this.datContabil			= null;
	    this.idtTerminal			= null;
	    this.idtNsuInstituicao		= null;
	    this.idCanal				= null;
	    this.idOrigem				= null;
	    this.idSistemaOrigem		= null;
	    this.vlrPago				= null;
	    this.vlrCreditoPrincipal	= null;
	    this.vlrCreditoPeriodico	= null;
	    this.vlrCreditoBonus		= null;
	    this.vlrCreditoSms			= null;
	    this.vlrCreditoGprs			= null;
	    this.vlrSaldoFinalPrincipal	= null;
	    this.vlrSaldoFinalPeriodico	= null;
	    this.vlrSaldoFinalBonus		= null;
	    this.vlrSaldoFinalSms		= null;
	    this.vlrSaldoFinalGprs		= null;
	    this.numDiasExpPrincipal	= null;
	    this.numDiasExpPeriodico	= null;
	    this.numDiasExpBonus		= null;
	    this.numDiasExpSms			= null;
	    this.numDiasExpGprs			= null;
	    this.desObservacao			= null;
	    this.conversorTimestamp		= new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
	}
	
	//Getters.
	
	/**
	 *	Retorna o identificador da recarga.
	 * 
	 *	@return		Long					idRecarga					Identificador da recarga.
	 */
	public Long getIdRecarga() 
	{
		return this.idRecarga;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 * 
	 *	@return		String					idtMsisdn					MSISDN do assinante.
	 */
	public String getIdtMsisdn() 
	{
		return this.idtMsisdn;
	}
	
	/**
	 *	Retorna o identificador do plano de preco do assinante.
	 * 
	 *	@return		Integer					idtPlanoPreco				Identificador do plano de preco do assinante.
	 */
	public Integer getIdtPlanoPreco() 
	{
		return this.idtPlanoPreco;
	}
	
	/**
	 *	Retorna o Tipo de Transacao da recarga.
	 * 
	 *	@return		String					tipTransacao				Tipo de Transacao da recarga.
	 */
	public String getTipTransacao() 
	{
		return this.tipTransacao;
	}
	
	/**
	 *	Retorna o identificador do tipo de credito da recarga.
	 * 
	 *	@return		String					idTipoCredito				Identificador do tipo de credito da recarga.
	 */
	public String getIdTipoCredito() 
	{
		return this.idTipoCredito;
	}
	
	/**
	 *	Retorna o identificador do valor de credito da recarga.
	 * 
	 *	@return		Integer					idValor						Identificador do valor de credito da recarga.
	 */
	public Double getIdValor() 
	{
		return this.idValor;
	}
	
	/**
	 *	Retorna a data em que a Recarga entrou na Plataforma Tecnomen.
	 * 
	 *	@return		Timestamp				datRecarga					Data em que a Recarga entrou na Plataforma Tecnomen.
	 */
	public Timestamp getDatRecarga() 
	{
		return this.datRecarga;
	}
	
	/**
	 *	Retorna a data em que a Recarga foi paga/solicitada.
	 * 
	 *	@return		Timestamp				datOrigem					Data em que a Recarga foi paga/solicitada.
	 */
	public Timestamp getDatOrigem() 
	{
		return this.datOrigem;
	}
	
	/**
	 *	Retorna o nome do operador.
	 * 
	 *	@return		String					nomOperador					Nome do operador.
	 */
	public String getNomOperador() 
	{
		return this.nomOperador;
	}
	
	/**
	 *	Retorna o identificador do tipo de recarga.
	 * 
	 *	@return		String					idTipoRecarga				Identificador do tipo de recarga.
	 */
	public String getIdTipoRecarga() 
	{
		return this.idTipoRecarga;
	}
	
	/**
	 *	Retorna o CPF do assinante.
	 * 
	 *	@return		String					idtCpf						CPF do assinante.
	 */
	public String getIdtCpf() 
	{
		return this.idtCpf;
	}
	
	/**
	 *	Retorna o hash do cartao de credito.
	 * 
	 *	@return		String					numHashCc					Hash do cartao de credito.
	 */
	public String getNumHashCc() 
	{
		return this.numHashCc;
	}
	
	/**
	 *	Retorna o identificador da loja.
	 * 
	 *	@return		String					idtLoja						Identificador da loja.
	 */
	public String getIdtLoja() 
	{
		return this.idtLoja;
	}
	
	/**
	 *	Retorna a data em que a Recarga foi logada na tbl_rec_recargas.
	 * 
	 *	@return		Timestamp				datInclusao					Data em que a Recarga foi logada na tbl_rec_recargas.
	 */
	public Timestamp getDatInclusao() 
	{
		return this.datInclusao;
	}
	
	/**
	 *	Retorna a data contabil.
	 * 
	 *	@return		String					datContabil					Data contabil.
	 */
	public String getDatContabil() 
	{
		return this.datContabil;
	}
	
	/**
	 *	Retorna o identificador do terminal.
	 * 
	 *	@return		String					idtTerminal					Identificador do terminal.
	 */
	public String getIdtTerminal() 
	{
		return this.idtTerminal;
	}
	
	/**
	 *	Retorna o NSU da instituicao.
	 * 
	 *	@return		String					idtNsuInstituicao			NSU da instituicao.
	 */
	public String getIdtNsuInstituicao() 
	{
		return this.idtNsuInstituicao;
	}
	
	/**
	 *	Retorna o canal da recarga.
	 * 
	 *	@return		String					idCanal						Canal da recarga.
	 */
	public String getIdCanal() 
	{
		return this.idCanal;
	}
	
	/**
	 *	Retorna a origem da recarga.
	 * 
	 *	@return		String					idOrigem					Origem da recarga.
	 */
	public String getIdOrigem() 
	{
		return this.idOrigem;
	}
	
	/**
	 *	Retorna o sistema de origem da recarga.
	 * 
	 *	@return		String					idSistemaOrigem				Sistema de origem recarga.
	 */
	public String getIdSistemaOrigem() 
	{
		return this.idSistemaOrigem;
	}
	
	/**
	 *	Retorna o valor pago.
	 * 
	 *	@return		Double					vlrPago						Valor pago.
	 */
	public Double getVlrPago()
	{
		return this.vlrPago;
	}
	
	/**
	 *	Retorna o valor de credito no saldo principal.
	 * 
	 *	@return		Double					vlrCreditoPrincipal			Valor de credito no saldo principal.
	 */
	public Double getVlrCreditoPrincipal()
	{
		return this.vlrCreditoPrincipal;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo Periodico.
	 * 
	 *	@return		Valor de credito no Saldo Periodico.
	 */
	public Double getVlrCreditoPeriodico()
	{
		return this.vlrCreditoPeriodico;
	}
	
	/**
	 *	Retorna o valor de credito no saldo de bonus.
	 * 
	 *	@return		Double					vlrCreditoBonus				Valor de credito no saldo de bonus.
	 */
	public Double getVlrCreditoBonus()
	{
		return this.vlrCreditoBonus;
	}
	
	/**
	 *	Retorna o valor de credito no saldo de SMS.
	 * 
	 *	@return		Double					vlrCreditoSms				Valor de credito no saldo de SMS.
	 */
	public Double getVlrCreditoSms()
	{
		return this.vlrCreditoSms;
	}
	
	/**
	 *	Retorna o valor de credito no saldo de dados.
	 * 
	 *	@return		Double					vlrCreditoGprs				Valor de credito no saldo de dados.
	 */
	public Double getVlrCreditoGprs()
	{
		return this.vlrCreditoGprs;
	}
	
	/**
	 *	Retorna o saldo final principal.
	 * 
	 *	@return		Double					vlrSaldoFinalPrincipal		Saldo final principal.
	 */
	public Double getVlrSaldoFinalPrincipal()
	{
		return this.vlrSaldoFinalPrincipal;
	}
	
	/**
	 *	Retorna o saldo final principal.
	 * 
	 *	@return		Saldo final principal.
	 */
	public Double getVlrSaldoFinalPeriodico()
	{
		return this.vlrSaldoFinalPeriodico;
	}
	
	/**
	 *	Retorna o saldo final de bonus.
	 * 
	 *	@return		Double					vlrSaldoFinalBonus			Saldo final de bonus.
	 */
	public Double getVlrSaldoFinalBonus()
	{
		return this.vlrSaldoFinalBonus;
	}
	
	/**
	 *	Retorna o saldo final de SMS.
	 * 
	 *	@return		Double					vlrSaldoFinalSms			Saldo final de SMS.
	 */
	public Double getVlrSaldoFinalSms()
	{
		return this.vlrSaldoFinalSms;
	}
	
	/**
	 *	Retorna o saldo final de dados.
	 * 
	 *	@return		Double					vlrSaldoFinalGprs			Saldo final de dados.
	 */
	public Double getVlrSaldoFinalGprs()
	{
		return this.vlrSaldoFinalGprs;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do saldo principal.
	 * 
	 *	@return		Integer					numDiasExpPrincipal			Numero de dias de expiracao do saldo principal.
	 */
	public Integer getNumDiasExpPrincipal()
	{
		return this.numDiasExpPrincipal;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Periodico.
	 * 
	 *	@return		Numero de dias de expiracao do saldo principal.
	 */
	public Integer getNumDiasExpPeriodico()
	{
		return this.numDiasExpPeriodico;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do saldo de bonus.
	 * 
	 *	@return		Integer					numDiasExpBonus				Numero de dias de expiracao do saldo de bonus.
	 */
	public Integer getNumDiasExpBonus()
	{
		return this.numDiasExpBonus;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do saldo de SMS.
	 * 
	 *	@return		Integer					numDiasExpSms				Numero de dias de expiracao do saldo de SMS.
	 */
	public Integer getNumDiasExpSms()
	{
		return this.numDiasExpSms;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do saldo de dados.
	 * 
	 *	@return		Integer					numDiasExpGprs				Numero de dias de expiracao do saldo de dados.
	 */
	public Integer getNumDiasExpGprs()
	{
		return this.numDiasExpGprs;
	}
	
	/**
	 *	Retorna a mensagem informativa da recarga.
	 * 
	 *	@return		String					desObservacao				Mensagem informativa da recarga.
	 */
	public String getDesObservacao() 
	{
		return this.desObservacao;
	}
	
	//Setters.
	
	/**
	 *	Atribui o identificador da recarga.
	 * 
	 *	@param		Long					idRecarga					Identificador da recarga.
	 */
	public void setIdRecarga(Long idRecarga) 
	{
		this.idRecarga = idRecarga;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 * 
	 *	@param		String					idtMsisdn					MSISDN do assinante.
	 */
	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}
	
	/**
	 *	Atribui o identificador do plano de preco do assinante.
	 * 
	 *	@param		Integer					idtPlanoPreco				Identificador do plano de preco do assinante.
	 */
	public void setIdtPlanoPreco(Integer idtPlanoPreco) 
	{
		this.idtPlanoPreco = idtPlanoPreco;
	}
	
	/**
	 *	Atribui o Tipo de Transacao da recarga.
	 * 
	 *	@param		String					tipTransacao				Tipo de Transacao da recarga.
	 */
	public void setTipTransacao(String tipTransacao) 
	{
		this.tipTransacao = tipTransacao;
	}
	
	/**
	 *	Atribui o identificador do tipo de credito da recarga.
	 * 
	 *	@param		String					idTipoCredito				Identificador do tipo de credito da recarga.
	 */
	public void setIdTipoCredito(String idTipoCredito) 
	{
		this.idTipoCredito = idTipoCredito;
	}
	
	/**
	 *	Atribui o identificador do valor de credito da recarga.
	 * 
	 *	@param		Integer					idValor						Identificador do valor de credito da recarga.
	 */
	public void setIdValor(Double idValor) 
	{
		this.idValor = idValor;
	}
	
	/**
	 *	Atribui a data em que a Recarga entrou na Plataforma Tecnomen.
	 * 
	 *	@param		Timestamp				datRecarga					Data em que a Recarga entrou na Plataforma Tecnomen.
	 */
	public void setDatRecarga(Timestamp datRecarga) 
	{
		this.datRecarga = datRecarga;
	}
	
	/**
	 *	Atribui a data em que a Recarga foi paga/solicitada.
	 * 
	 *	@param		Timestamp				datOrigem					Data em que a Recarga foi paga/solicitada.
	 */
	public void setDatOrigem(Timestamp datOrigem) 
	{
		this.datOrigem = datOrigem;
	}
	
	/**
	 *	Atribui o nome do operador.
	 * 
	 *	@param		String					nomOperador					Nome do operador.
	 */
	public void setNomOperador(String nomOperador) 
	{
		this.nomOperador = nomOperador;
	}
	
	/**
	 *	Atribui o identificador do tipo de recarga.
	 * 
	 *	@param		String					idTipoRecarga				Identificador do tipo de recarga.
	 */
	public void setIdTipoRecarga(String idTipoRecarga) 
	{
		this.idTipoRecarga = idTipoRecarga;
	}
	
	/**
	 *	Atribui o CPF do assinante.
	 * 
	 *	@param		String					idtCpf						CPF do assinante.
	 */
	public void setIdtCpf(String idtCpf) 
	{
		this.idtCpf = idtCpf;
	}
	
	/**
	 *	Atribui o hash do cartao de credito.
	 * 
	 *	@param		String					numHashCc					Hash do cartao de credito.
	 */
	public void setNumHashCc(String numHashCc) 
	{
		this.numHashCc = numHashCc;
	}
	
	/**
	 *	Atribui o identificador da loja.
	 * 
	 *	@param		String					idtLoja						Identificador da loja.
	 */
	public void setIdtLoja(String idtLoja) 
	{
		this.idtLoja = idtLoja;
	}
	
	/**
	 *	Atribui a data em que a Recarga foi logada na tbl_rec_recargas.
	 * 
	 *	@param		Timestamp				datInclusao					Data em que a Recarga foi logada na tbl_rec_recargas.
	 */
	public void setDatInclusao(Timestamp datInclusao) 
	{
		this.datInclusao = datInclusao;
	}
	
	/**
	 *	Atribui a data contabil.
	 * 
	 *	@param		String					datContabil					Data contabil.
	 */
	public void setDatContabil(String datContabil) 
	{
		this.datContabil = datContabil;
	}
	
	/**
	 *	Atribui o identificador do terminal.
	 * 
	 *	@param		String					idtTerminal					Identificador do terminal.
	 */
	public void setIdtTerminal(String idtTerminal) 
	{
		this.idtTerminal = idtTerminal;
	}
	
	/**
	 *	Atribui o NSU da instituicao.
	 * 
	 *	@param		String					idtNsuInstituicao			NSU da instituicao.
	 */
	public void setIdtNsuInstituicao(String idtNsuInstituicao) 
	{
		this.idtNsuInstituicao = idtNsuInstituicao;
	}
	
	/**
	 *	Atribui o canal da recarga.
	 * 
	 *	@param		String					idCanal						Canal da recarga.
	 */
	public void setIdCanal(String idCanal) 
	{
		this.idCanal = idCanal;
	}
	
	/**
	 *	Atribui a origem da recarga.
	 * 
	 *	@param		String					idOrigem					Origem da recarga.
	 */
	public void setIdOrigem(String idOrigem) 
	{
		this.idOrigem = idOrigem;
	}
	
	/**
	 *	Atribui o sistema de origem da recarga.
	 * 
	 *	@param		String					idSistemaOrigem				Sistema de origem recarga.
	 */
	public void setIdSistemaOrigem(String idSistemaOrigem) 
	{
		this.idSistemaOrigem = idSistemaOrigem;
	}
	
	/**
	 *	Atribui o valor pago.
	 * 
	 *	@param		Double					vlrPago						Valor pago.
	 */
	public void setVlrPago(Double vlrPago)
	{
		this.vlrPago = vlrPago;
	}
	
	/**
	 *	Atribui o valor de credito no saldo principal.
	 * 
	 *	@param		Double					vlrCreditoPrincipal			Valor de credito no saldo principal.
	 */
	public void setVlrCreditoPrincipal(Double vlrCreditoPrincipal)
	{
		this.vlrCreditoPrincipal = vlrCreditoPrincipal;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo Periodico.
	 * 
	 *	@param		vlrCreditoPeriodico		Valor de credito no Saldo Periodico.
	 */
	public void setVlrCreditoPeriodico(Double vlrCreditoPeriodico)
	{
		this.vlrCreditoPeriodico = vlrCreditoPeriodico;
	}
	
	/**
	 *	Atribui o valor de credito no saldo de bonus.
	 * 
	 *	@param		Double					vlrCreditoBonus				Valor de credito no saldo de bonus.
	 */
	public void setVlrCreditoBonus(Double vlrCreditoBonus)
	{
		this.vlrCreditoBonus = vlrCreditoBonus;
	}
	
	/**
	 *	Atribui o valor de credito no saldo de SMS.
	 * 
	 *	@param		Double					vlrCreditoSms				Valor de credito no saldo de SMS.
	 */
	public void setVlrCreditoSms(Double vlrCreditoSms)
	{
		this.vlrCreditoSms = vlrCreditoSms;
	}
	
	/**
	 *	Atribui o valor de credito no saldo de dados.
	 * 
	 *	@param		Double					vlrCreditoGprs				Valor de credito no saldo de dados.
	 */
	public void setVlrCreditoGprs(Double vlrCreditoGprs)
	{
		this.vlrCreditoGprs = vlrCreditoGprs;
	}
	
	/**
	 *	Atribui o saldo final principal.
	 * 
	 *	@param		Double					vlrSaldoFinalPrincipal		Saldo final principal.
	 */
	public void setVlrSaldoFinalPrincipal(Double vlrSaldoFinalPrincipal)
	{
		this.vlrSaldoFinalPrincipal = vlrSaldoFinalPrincipal;
	}
	
	/**
	 *	Atribui o saldo final periodico.
	 * 
	 *	@param		vlrSaldoFinalPeriodico	Saldo final periodico.
	 */
	public void setVlrSaldoFinalPeriodico(Double vlrSaldoFinalPeriodico)
	{
		this.vlrSaldoFinalPeriodico = vlrSaldoFinalPeriodico;
	}
	
	/**
	 *	Atribui o saldo final de bonus.
	 * 
	 *	@param		Double					vlrSaldoFinalBonus			Saldo final de bonus.
	 */
	public void setVlrSaldoFinalBonus(Double vlrSaldoFinalBonus)
	{
		this.vlrSaldoFinalBonus = vlrSaldoFinalBonus;
	}
	
	/**
	 *	Atribui o saldo final de SMS.
	 * 
	 *	@param		Double					vlrSaldoFinalSms			Saldo final de SMS.
	 */
	public void setVlrSaldoFinalSms(Double vlrSaldoFinalSms)
	{
		this.vlrSaldoFinalSms = vlrSaldoFinalSms;
	}
	
	/**
	 *	Atribui o saldo final de dados.
	 * 
	 *	@param		Double					vlrSaldoFinalGprs			Saldo final de dados.
	 */
	public void setVlrSaldoFinalGprs(Double vlrSaldoFinalGprs)
	{
		this.vlrSaldoFinalGprs = vlrSaldoFinalGprs;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do saldo principal.
	 * 
	 *	@param		Integer					numDiasExpPrincipal			Numero de dias de expiracao do saldo principal.
	 */
	public void setNumDiasExpPrincipal(Integer numDiasExpPrincipal)
	{
		this.numDiasExpPrincipal = numDiasExpPrincipal;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Periodico.
	 * 
	 *	@param		numDiasExpPeriodico		Numero de dias de expiracao do Saldo Periodico.
	 */
	public void setNumDiasExpPeriodico(Integer numDiasExpPeriodico)
	{
		this.numDiasExpPeriodico = numDiasExpPeriodico;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do saldo de bonus.
	 * 
	 *	@param		Integer					numDiasExpBonus				Numero de dias de expiracao do saldo de bonus.
	 */
	public void setNumDiasExpBonus(Integer numDiasExpBonus)
	{
		this.numDiasExpBonus = numDiasExpBonus;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do saldo de SMS.
	 * 
	 *	@param		Integer					numDiasExpSms				Numero de dias de expiracao do saldo de SMS.
	 */
	public void setNumDiasExpSms(Integer numDiasExpSms)
	{
		this.numDiasExpSms = numDiasExpSms;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do saldo de dados.
	 * 
	 *	@param		Integer					numDiasExpGprs				Numero de dias de expiracao do saldo de dados.
	 */
	public void setNumDiasExpGprs(Integer numDiasExpGprs)
	{
		this.numDiasExpGprs = numDiasExpGprs;
	}
	
	/**
	 *	Atribui a mensagem informativa da recarga.
	 * 
	 *	@param		String					desObservacao				Mensagem informativa da recarga.
	 */
	public void setDesObservacao(String desObservacao) 
	{
		this.desObservacao = desObservacao;
	}
	
	//Outros metodos.

	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		Object					object						Objeto a ser comparado com o atual.
	 *	@return		boolean												True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof Recarga))
		{
			return false;
		}
		
		if(this.hashCode() != ((Recarga)object).hashCode())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		int													Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append((this.idtMsisdn != null) ? this.idtMsisdn : "NULL");
		result.append("||");
		result.append((this.tipTransacao != null) ? this.tipTransacao : "NULL");
		result.append("||");
		result.append((this.datRecarga != null) ? this.conversorTimestamp.format(this.datRecarga) : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto.
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append("MSISDN: ");
		result.append(this.idtMsisdn);
		result.append(" - ");
		result.append("Tipo de Transacao: ");
		result.append((this.tipTransacao != null) ? this.tipTransacao : "NULL");
		result.append(" - ");
		result.append("Data de cadastro: ");
		result.append((this.datRecarga != null) ? this.conversorTimestamp.format(this.datRecarga) : "NULL");
		
		return result.toString();
	}
	
}
