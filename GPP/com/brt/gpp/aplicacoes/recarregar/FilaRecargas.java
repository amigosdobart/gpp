package com.brt.gpp.aplicacoes.recarregar;

//Imports Java.

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

//Imports GPP.

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe que representa a entidade da tabela TBL_REC_FILA_RECARGAS.
 *
 *	@author	Daniel Ferreira
 *	@since	17/08/2005
 */
public class FilaRecargas
{
    private Integer             idRegistro;
	private String				idtMsisdn;
	private	String				tipTransacao;
	private	Timestamp			datCadastro;
	private Timestamp			datExecucao;
	private Timestamp			datProcessamento;
	private Double				vlrCreditoPrincipal;
	private Double				vlrCreditoPeriodico;
	private Double				vlrCreditoBonus;
	private Double				vlrCreditoSms;
	private Double				vlrCreditoGprs;
	private Integer				numDiasExpPrincipal;
	private Integer				numDiasExpPeriodico;
	private Integer				numDiasExpBonus;
	private Integer				numDiasExpSms;
	private Integer				numDiasExpGprs;
	private	String				desMensagem;
	private String				tipSms;
	private Integer				indEnviaSms;
	private Integer				numPrioridade;
	private Integer				idtStatusProcessamento;
	private Integer				idtCodigoRetorno;
	private Integer				indZerarSaldoPeriodico;
	private Integer				indZerarSaldoBonus;
	private Integer				indZerarSaldoSms;
	private Integer				indZerarSaldoGprs;
	private String				tipOperacao;
	private String				idtRecarga;
	private String				idtNsuInstituicao;
	private String				datContabil;
	private String				numHashCC;
	private String				idtCpf;
	private String				idtTerminal;
	private String				tipTerminal;
	private String				rowId;
	private Integer             idRegistroDependencia;
    
	private SimpleDateFormat	conversorTimestamp;
	private DecimalFormat		conversorDouble;

	//Constantes internas.

	public static final int DAT_CADASTRO			= 0;
	public static final int DAT_EXECUCAO			= 1;
	public static final int DAT_PROCESSAMENTO		= 2;
	public static final int VLR_CREDITO_PRINCIPAL	= 3;
	public static final int VLR_CREDITO_BONUS		= 4;
	public static final int VLR_CREDITO_SMS			= 5;
	public static final int VLR_CREDITO_GPRS		= 6;

	//Construtores.

	/**
	 * Construtor da classe.
	 */
	public FilaRecargas()
	{
	    this.conversorTimestamp		= new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
	    this.conversorDouble		= new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
	}

	//Getters.

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
	 *	Retorna o Tipo de Transacao da recarga.
	 *
	 *	@return		String					tipTransacao				Tipo de Transacao da recarga.
	 */
	public String getTipTransacao()
	{
		return this.tipTransacao;
	}

	/**
	 *	Retorna a data de cadastro na Fila de Recargas.
	 *
	 *	@return		Timestamp				datCadastro					Data de cadastro.
	 */
	public Timestamp getDatCadastro()
	{
		return this.datCadastro;
	}

	/**
	 *	Retorna a data de execucao na Fila de Recargas.
	 *
	 *	@return		Timestamp				datExecucao					Data de execucao.
	 */
	public Timestamp getDatExecucao()
	{
		return this.datExecucao;
	}

	/**
	 *	Retorna a data de processamento pela Fila de Recargas.
	 *
	 *	@return		Timestamp				datProcessamento			Data de processamento.
	 */
	public Timestamp getDatProcessamento()
	{
		return this.datProcessamento;
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
	 * Retorna o valor de credito no saldo periodico
	 *
	 * @return Double Valor de credito no saldo periodico
	 */
	public Double getVlrCreditoPeriodico()
	{
		return vlrCreditoPeriodico;
	}

	/**
	 *	Retorna o valor de credito no saldo especificado.
	 *
	 *	@param 		tipoSaldo				Tipo de saldo.
	 *	@return		Valor de credito no saldo especificado.
	 *	@throws		IllegalArgumentException Caso o tipo de saldo nao esteja definido.
	 */
	public double getValorCredito(TipoSaldo tipoSaldo)
	{
		Double result = null;
		
		switch(tipoSaldo.getIdtTipoSaldo())
		{
			case TipoSaldo.PRINCIPAL:
				result = this.getVlrCreditoPrincipal();
				break;
			case TipoSaldo.PERIODICO:
				result = this.getVlrCreditoPeriodico();
				break;
			case TipoSaldo.BONUS:
				result = this.getVlrCreditoBonus();
				break;
			case TipoSaldo.TORPEDOS:
				result = this.getVlrCreditoSms();
				break;
			case TipoSaldo.DADOS:
				result = this.getVlrCreditoGprs();
				break;
			default: throw new IllegalArgumentException("Tipo de Saldo nao definido" + tipoSaldo);
		}
		
		return (result != null) ? result.doubleValue() : 0.0;
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
	 *	Retorna o numero de dias de expiracao do saldo de dados.
	 *
	 *	@return		Integer					numDiasExpGprs				Numero de dias de expiracao do saldo de dados.
	 */
	public Integer getNumDiasExpPeriodico()
	{
		return this.numDiasExpPeriodico;
	}

	/**
	 *	Retorna a mensagem informativa da recarga.
	 *
	 *	@return		String					desMensagem					Mensagem informativa da recarga.
	 */
	public String getDesMensagem()
	{
		return this.desMensagem;
	}

	/**
	 *	Retorna o tipo de SMS a ser enviado.
	 *
	 *	@return		String					tipSms						Tipo de SMS.
	 */
	public String getTipSms()
	{
		return this.tipSms;
	}

	/**
	 *	Retorna o indicador de envio de SMS.
	 *
	 *	@return		Integer					indEnviaSms					Indicador de envio de SMS.
	 */
	public Integer getIndEnviaSms()
	{
		return this.indEnviaSms;
	}

	/**
	 *	Retorna a prioridade do envio do SMS.
	 *
	 *	@return		Integer					numPrioridade				Prioridade do envio de SMS.
	 */
	public Integer getNumPrioridade()
	{
		return this.numPrioridade;
	}

	/**
	 *	Retorna o status de processamento pela Fila de Recargas.
	 *
	 *	@return		Integer					idtStatusProcessamento		Status de processamento.
	 */
	public Integer getIdtStatusProcessamento()
	{
		return this.idtStatusProcessamento;
	}

	/**
	 *	Retorna o codigo de retorno do processamento pela Fila de Recargas.
	 *
	 *	@return		Integer					idtCodigoRetorno			Codigo de retorno do processamento.
	 */
	public Integer getIdtCodigoRetorno()
	{
		return this.idtCodigoRetorno;
	}

	/**
	 *	Retorna o indicador para zerar o Saldo Periodico antes da concessao.
	 *
	 *	@return		Indicador para zerar o Saldo Periodico.
	 */
	public Integer getIndZerarSaldoPeriodico()
	{
		return this.indZerarSaldoPeriodico;
	}

	/**
	 *	Retorna o indicador para zerar o saldo de bonus antes da concessao.
	 *
	 *	@return		Integer					indZerarSaldoBonus			Indicador para zerar o saldo de bonus.
	 */
	public Integer getIndZerarSaldoBonus()
	{
		return this.indZerarSaldoBonus;
	}

	/**
	 *	Retorna o indicador para zerar o saldo de SMS antes da concessao.
	 *
	 *	@return		Integer					indZerarSaldoBonus			Indicador para zerar o saldo de SMS.
	 */
	public Integer getIndZerarSaldoSms()
	{
		return this.indZerarSaldoSms;
	}

	/**
	 *	Retorna o indicador para zerar o saldo de SMS antes da concessao.
	 *
	 *	@return		Integer					indZerarSaldoBonus			Indicador para zerar o saldo de SMS.
	 */
	public Integer getIndZerarSaldoGprs()
	{
		return this.indZerarSaldoGprs;
	}

	/**
	 * Retorna o tipo de operacao (A - Ajuste, R - Recarga)
	 *
	 * @return String Tipo de operacao
	 */
	public String getTipOperacao()
	{
		return this.tipOperacao;
	}
	/**
	 * Retorna o numero identificador da recarga.
	 *
	 * @return String Itendificador da recarga.
	 */
	public String getIdtRecarga()
	{
		return this.idtRecarga;
	}

	/**
	 * Retorna o numero identificador da instituicao.
	 *
	 * @return String Itendificador da instituicao.
	 */
	public String getIdtNsuInstituicao()
	{
		return this.idtNsuInstituicao;
	}

	/**
	 * Retorna data Contabil da recarga
	 *
	 * @return String Data Contabil da recarga
	 */
	public String getDatContabil()
	{
		return this.datContabil;
	}

	/**
	 * Retorna nunero de Hash da Conta Corrente
	 *
	 * @return String Numero de hash da Conta Corrente
	 */
	public String getNumHashCC()
	{
		return this.numHashCC;
	}

	/**
	 * Retorna o numero do CPF
	 *
	 * @return String Numero do CPF
	 */
	public String getIdtCpf()
	{
		return this.idtCpf;
	}

	/**
	 * Retorna o numero identificador do Terminal
	 *
	 * @return String Numero identificador do Terminal
	 */
	public String getIdtTerminal()
	{
		return this.idtTerminal;
	}

	/**
	 * Retorna o tipo do Terminal
	 *
	 * @return String Tipo do Terminal
	 */
	public String getTipTerminal()
	{
		return this.tipTerminal;
	}


	/**
	 * Retorna o ROWID do registro no banco de dados
	 *
	 * @return String ROWID do registro no banco de dados
	 */
	public String getRowId()
	{
		return rowId;
	}

    /**
     * Retorna o ID do registro no banco de dados
     *
     * @return ID do registro no banco de dados
     */
    public Integer getIdRegistro()
    {
        return idRegistro;
    }

    /**
     * Retorna o ID do registro de dependencia. A execucao dessa recarga depende
     * da execucao previa da recarga identificada pelo numero retornado nesse metodo.
     *
     * @return ID do registro de dependencia
     */
    public Integer getIdRegistroDependencia()
    {
        return idRegistroDependencia;
    }
    
    
	//Setters.


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
	 *	Atribui o Tipo de Transacao da recarga.
	 *
	 *	@param		String					tipTransacao				Tipo de Transacao da recarga.
	 */
	public void setTipTransacao(String tipTransacao)
	{
		this.tipTransacao = tipTransacao;
	}

	/**
	 *	Atribui a data de cadastro na Fila de Recargas.
	 *
	 *	@param		Timestamp				datCadastro					Data de cadastro.
	 */
	public void setDatCadastro(Timestamp datCadastro)
	{
		this.datCadastro = datCadastro;
	}

	/**
	 *	Atribui a data de execucao na Fila de Recargas.
	 *
	 *	@param		Timestamp				datExecucao					Data de execucao.
	 */
	public void setDatExecucao(Timestamp datExecucao)
	{
		this.datExecucao = datExecucao;
	}

	/**
	 *	Atribui a data de processamento pela Fila de Recargas.
	 *
	 *	@param		Timestamp				datProcessamento			Data de processamento.
	 */
	public void setDatProcessamento(Timestamp datProcessamento)
	{
		this.datProcessamento = datProcessamento;
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
	 * Atribui o valor de credito no saldo periodico
	 *
	 * @param vlrCreditoPeriodico valor de credito no saldo periodico
	 */
	public void setVlrCreditoPeriodico(Double vlrCreditoPeriodico)
	{
		this.vlrCreditoPeriodico = vlrCreditoPeriodico;
	}

	/**
	 *	Atribui o valor de credito no saldo especificado.
	 *
	 *	@param 		tipoSaldo				Tipo de saldo.
	 *	@param		valorCredito			Valor de credito.
	 *	@throws		IllegalArgumentException Caso o tipo de saldo nao esteja definido.
	 */
	public void setValorCredito(TipoSaldo tipoSaldo, double valorCredito)
	{
		switch(tipoSaldo.getIdtTipoSaldo())
		{
			case TipoSaldo.PRINCIPAL:
				this.setVlrCreditoPrincipal(new Double(valorCredito));
				break;
			case TipoSaldo.PERIODICO:
				this.setVlrCreditoPeriodico(new Double(valorCredito));
				break;
			case TipoSaldo.BONUS:
				this.setVlrCreditoBonus(new Double(valorCredito));
				break;
			case TipoSaldo.TORPEDOS:
				this.setVlrCreditoSms(new Double(valorCredito));
				break;
			case TipoSaldo.DADOS:
				this.setVlrCreditoGprs(new Double(valorCredito));
				break;
			default: throw new IllegalArgumentException("Tipo de Saldo nao definido" + tipoSaldo);
		}
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
	 * Atribui o numero de dias de expiracao do saldo preiodico
	 *
	 * @param numDiasExpPeriodico numero de dias de expiracao do saldo preiodico
	 */
	public void setNumDiasExpPeriodico(Integer numDiasExpPeriodico)
	{
		this.numDiasExpPeriodico = numDiasExpPeriodico;
	}

	/**
	 *	Atribui a mensagem informativa da recarga.
	 *
	 *	@param		String					desMensagem					Mensagem informativa da recarga.
	 */
	public void setDesMensagem(String desMensagem)
	{
		this.desMensagem = desMensagem;
	}

	/**
	 *	Atribui o tipo de SMS a ser enviado.
	 *
	 *	@param		String					tipSms						Tipo de SMS.
	 */
	public void setTipSms(String tipSms)
	{
		this.tipSms = tipSms;
	}

	/**
	 *	Atribui o indicador de envio de SMS.
	 *
	 *	@param		Integer					indEnviaSms					Indicador de envio de SMS.
	 */
	public void setIndEnviaSms(Integer indEnviaSms)
	{
		this.indEnviaSms = indEnviaSms;
	}

	/**
	 *	Atribui a prioridade do envio do SMS.
	 *
	 *	@param		Integer					numPrioridade				Prioridade do envio de SMS.
	 */
	public void setNumPrioridade(Integer numPrioridade)
	{
		this.numPrioridade = numPrioridade;
	}

	/**
	 *	Atribui o status de processamento pela Fila de Recargas.
	 *
	 *	@param		Integer					idtStatusProcessamento		Status de processamento.
	 */
	public void setIdtStatusProcessamento(Integer idtStatusProcessamento)
	{
		this.idtStatusProcessamento = idtStatusProcessamento;
	}

	/**
	 *	Atribui o codigo de retorno do processamento pela Fila de Recargas.
	 *
	 *	@param		Integer					idtCodigoRetorno			Codigo de retorno do processamento.
	 */
	public void setIdtCodigoRetorno(Integer idtCodigoRetorno)
	{
		this.idtCodigoRetorno = idtCodigoRetorno;
	}

	/**
	 *	Atribui o indicador para zerar o Saldo Periodico antes da concessao.
	 *
	 *	@param		indZerarSaldoPeriodico	Indicador para zerar o Saldo Periodico.
	 */
	public void setIndZerarSaldoPeriodico(Integer indZerarSaldoPeriodico)
	{
		this.indZerarSaldoPeriodico = indZerarSaldoPeriodico;
	}

	/**
	 *	Atribui o indicador para zerar o saldo de bonus antes da concessao.
	 *
	 *	@param		Integer					indZerarSaldoBonus			Indicador para zerar o saldo de bonus.
	 */
	public void setIndZerarSaldoBonus(Integer indZerarSaldoBonus)
	{
		this.indZerarSaldoBonus = indZerarSaldoBonus;
	}

	/**
	 *	Atribui o indicador para zerar o saldo de SMS antes da concessao.
	 *
	 *	@param Integer					indZerarSaldoSms			Indicador para zerar o saldo de SMS.
	 */
	public void setIndZerarSaldoSms(Integer indZerarSaldoSms)
	{
		this.indZerarSaldoSms = indZerarSaldoSms;
	}

	/**
	 *	Atribui o indicador para zerar o saldo de dados antes da concessao.
	 *
	 *	@param		Integer					indZerarSaldoGprs			Indicador para zerar o saldo de dados.
	 */
	public void setIndZerarSaldoGprs(Integer indZerarSaldoGprs)
	{
		this.indZerarSaldoGprs = indZerarSaldoGprs;
	}

	/**
	 * Atribui o tipo de Operacao : A - Ajuste, R - Recarga
	 *
	 * @param tipOperacao Tipo da operacao
	 */
	public void setTipOperacao(String tipOperacao)
	{
		this.tipOperacao = tipOperacao;
	}

	/**
	 * Atribui o numero identificador da Recarga
	 *
	 * @param idtRecarga o numero identificador da Recarga
	 */
	public void setIdtRecarga(String idtRecarga)
	{
		this.idtRecarga = idtRecarga;
	}


	/**
	 * Atribui numero identificador da Instituicao
	 *
	 * @param idtNsuInstituicao o numero identificador da Instituicao
	 */
	public void setIdtNsuInstituicao(String idtNsuInstituicao)
	{
		this.idtNsuInstituicao = idtNsuInstituicao;
	}

	/**
	 * Atribui a data contabil da recarga
	 *
	 * @param datContabil a data contabil da recarga
	 */
	public void setDatContabil(String datContabil)
	{
		this.datContabil = datContabil;
	}

	/**
	 * Atribui o numero de hash da conta corrente
	 *
	 * @param numHashCC o numero de hash da conta corrente
	 */
	public void setNumHashCC(String numHashCC)
	{
		this.numHashCC = numHashCC;
	}

	/**
	 * Atribui o numero identificador do CPF
	 *
	 * @param idtCpf o numero identificador do CPF
	 */
	public void setIdtCpf(String idtCpf)
	{
		this.idtCpf = idtCpf;
	}

	/**
	 * Atribui o identificador do terminal
	 *
	 * @param idtTerminal o identificador do terminal
	 */
	public void setIdtTerminal(String idtTerminal)
	{
		this.idtTerminal = idtTerminal;
	}

	/**
	 * Atribui o tipo de terminal
	 *
	 * @param tipTerminal o tipo de terminal
	 */
	public void setTipTerminal(String tipTerminal)
	{
		this.tipTerminal = tipTerminal;
	}

	/**
	 * Atribui o ROWID
	 *
	 * @param rowId ROWID
	 */
	public void setRowId(String rowId)
	{
		this.rowId = rowId;
	}

    /**
     * Atribui o ID do registro
     */
	public void setIdRegistro(Integer idRegistro)
    {
        this.idRegistro = idRegistro;
    }

    /**
     * Retorna o ID do registro de dependencia. A execucao dessa recarga depende
     * da execucao previa da recarga identificada por esse numero.
     */
    public void setIdRegistroDependencia(Integer idRegistroDependencia)
    {
        this.idRegistroDependencia = idRegistroDependencia;
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

		if(!(object instanceof FilaRecargas))
		{
			return false;
		}

		if(this.hashCode() != ((FilaRecargas)object).hashCode())
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
		result.append((this.datCadastro != null) ? this.conversorTimestamp.format(this.datCadastro) : "NULL");

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
		result.append((this.datCadastro != null) ? this.conversorTimestamp.format(this.datCadastro) : "NULL");

		return result.toString();
	}

	/**
	 *	Converte o valor para String, sem formatacao. Se o valor for NULL, retorna o valor inicializado para valores
	 *	double e NULL para datas.
	 *
	 *	@param		int						campo						Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		String												Valor em String, sem formatacao.
	 */
	public String toString(int campo)
	{
	    switch(campo)
	    {
	    	case FilaRecargas.DAT_CADASTRO:
	    	{
	    	    return (this.datCadastro != null) ? this.datCadastro.toString() : null;
	    	}
	    	case FilaRecargas.DAT_EXECUCAO:
	    	{
	    	    return (this.datExecucao != null) ? this.datExecucao.toString() : null;
	    	}
	    	case FilaRecargas.DAT_PROCESSAMENTO:
	    	{
	    	    return (this.datProcessamento != null) ? this.datProcessamento.toString() : null;
	    	}
	    	case FilaRecargas.VLR_CREDITO_PRINCIPAL:
	    	{
	    	    return (this.vlrCreditoPrincipal != null) ? this.vlrCreditoPrincipal.toString() : new Double(0.0).toString();
	    	}
	    	case FilaRecargas.VLR_CREDITO_BONUS:
	    	{
	    	    return (this.vlrCreditoBonus != null) ? this.vlrCreditoBonus.toString() : new Double(0.0).toString();
	    	}
	    	case FilaRecargas.VLR_CREDITO_SMS:
	    	{
	    	    return (this.vlrCreditoSms != null) ? this.vlrCreditoSms.toString() : new Double(0.0).toString();
	    	}
	    	case FilaRecargas.VLR_CREDITO_GPRS:
	    	{
	    	    return (this.vlrCreditoGprs != null) ? this.vlrCreditoGprs.toString() : new Double(0.0).toString();
	    	}
	    	default: return null;
	    }
	}

	/**
	 *	Retorna o valor em formato String. Se o valor for NULL, retorna NULL.
	 *
	 *	@param		int						campo						Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		String												Valor no formato String.
	 */
	public String format(int campo)
	{
	    switch(campo)
	    {
	    	case FilaRecargas.DAT_CADASTRO:
	    	{
	    	    return (this.datCadastro != null) ? this.conversorTimestamp.format(this.datCadastro) : null;
	    	}
	    	case FilaRecargas.DAT_EXECUCAO:
	    	{
	    	    return (this.datExecucao != null) ? this.conversorTimestamp.format(this.datExecucao) : null;
	    	}
	    	case FilaRecargas.DAT_PROCESSAMENTO:
	    	{
	    	    return (this.datProcessamento != null) ? this.conversorTimestamp.format(this.datProcessamento) : null;
	    	}
	    	case FilaRecargas.VLR_CREDITO_PRINCIPAL:
	    	{
	    	    return (this.vlrCreditoPrincipal != null) ? this.conversorDouble.format(this.vlrCreditoPrincipal.doubleValue()) : null;
	    	}
	    	case FilaRecargas.VLR_CREDITO_BONUS:
	    	{
	    	    return (this.vlrCreditoBonus != null) ? this.conversorDouble.format(this.vlrCreditoBonus.doubleValue()) : null;
	    	}
	    	case FilaRecargas.VLR_CREDITO_SMS:
	    	{
	    	    return (this.vlrCreditoSms != null) ? this.conversorDouble.format(this.vlrCreditoSms.doubleValue()) : null;
	    	}
	    	case FilaRecargas.VLR_CREDITO_GPRS:
	    	{
	    	    return (this.vlrCreditoGprs != null) ? this.conversorDouble.format(this.vlrCreditoGprs.doubleValue()) : null;
	    	}
	    	default: return null;
	    }
	}

	/**
	 *	Informa se a recarga deve enviar SMS.
	 *
	 *	@return		boolean												True se deve enviar e false caso contrario.
	 */
	public boolean enviaSms()
	{
	    return ((this.indEnviaSms != null) && (this.indEnviaSms.intValue() == 1)) ? true : false;
	}

	/**
	 *	Indica se a recarga deve zerar o Saldo Periodico.
	 *
	 *	@return		True se deve zerar e false caso contrario.
	 */
	public boolean zerarSaldoPeriodico()
	{
	    return ((this.indZerarSaldoPeriodico != null) && (this.indZerarSaldoPeriodico.intValue() == 1)) ? true : false;
	}

	/**
	 *	Informa se a recarga deve zerar o saldo de bonus.
	 *
	 *	@return		boolean												True se deve zerar e false caso contrario.
	 */
	public boolean zerarSaldoBonus()
	{
	    return ((this.indZerarSaldoBonus != null) && (this.indZerarSaldoBonus.intValue() == 1)) ? true : false;
	}

	/**
	 *	Informa se a recarga deve zerar o saldo de SMS.
	 *
	 *	@return		boolean												True se deve zerar e false caso contrario.
	 */
	public boolean zerarSaldoSms()
	{
	    return ((this.indZerarSaldoSms != null) && (this.indZerarSaldoSms.intValue() == 1)) ? true : false;
	}

	/**
	 *	Informa se a recarga deve zerar o saldo de dados.
	 *
	 *	@return		boolean												True se deve zerar e false caso contrario.
	 */
	public boolean zerarSaldoGprs()
	{
	    return ((this.indZerarSaldoGprs != null) && (this.indZerarSaldoGprs.intValue() == 1)) ? true : false;
	}

}