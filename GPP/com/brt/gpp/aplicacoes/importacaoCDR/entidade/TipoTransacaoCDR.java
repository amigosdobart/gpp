package com.brt.gpp.aplicacoes.importacaoCDR.entidade;

/**
  * Esta classe contemplas as informacoes de tabela que definem a combinacao
  * do transaction type e do external transaction type para identificar se 
  * isto e uma recarga, evento, ajuste ou uma combinacao destes.
  * Esta classe existe por motivos de ganho de performance, pois o processo
  * de importacao de Recargas/Eventos deve consultar sempre entao ao inves de
  * sempre estar buscando dados na tabela busca os dados em um gerenciador
  * de mapeamento.
  * 
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos Lemgruber Junior
  * Data: 				10/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class TipoTransacaoCDR 
{
	private long 	transactionType;
	private long 	externalTransactionType;
	private boolean isRecarga;
	private boolean isEvento;
	private boolean isAjuste;
	private boolean inverterSaldoPrincipal;
	private boolean inverterSaldoPeriodico;
	private boolean inverterSaldoBonus;
	private boolean inverterSaldoSm;
	private boolean inverterSaldoDados;
	private String	tipoOperacao;
	private String 	idtAntigoCampo;
	private String 	idtNovoCampo;
	private String	tipoTransacaoRecarga;
	private String	tipoTransacaoAjuste;
	
	/**
	 * Metodo....:TipoTransacaoCDR
	 * Descricao.:Construtor da classe
	 * @param transType		- Transaction type
	 * @param extTransType	- External transaction type
	 */
	public TipoTransacaoCDR(long transType, long extTransType)
	{
		setTransactionType        (transType);
		setExternalTransactionType(extTransType);
	}
	
	/**
	 * Metodo....:getTipoOperacao
	 * Descricao.:Retorna o tipo de operacao para eventos
	 * @return String - Tipo de Operacao
	 */
	public String getTipoOperacao()
	{
		return tipoOperacao;
	}

	/**
	 * Metodo....:getIdtAntigoCampo
	 * Descricao.:Retorna o tipo de operacao do evento anterior (mudanca de status)
	 * @return String - Tipo de operacao anterior
	 */
	public String getIdtAntigoCampo()
	{
		return this.idtAntigoCampo;
	}

	/**
	 * Metodo....:getIdtNovoCampo
	 * Descricao.:Retorna o tipo de operacao referente ao novo status.
	 * @return String - Novo status.
	 */
	public String getIdtNovoCampo()
	{
		return this.idtNovoCampo;
	}

	/**
	 * Metodo....:getTipoTransacaoAjuste
	 * Descricao.:Retorna o tipo de transacao quando for um ajuste
	 * @return String - Tipo de transacao de Ajuste
	 */
	public String getTipoTransacaoAjuste()
	{
		return tipoTransacaoAjuste;
	}

	/**
	 * Metodo....:getTipoTransacaoRecarga
	 * Descricao.:Retorna o tipo de transacao quando for uma recarga
	 * @return String - Tipo de transacao de Recarga
	 */
	public String getTipoTransacaoRecarga()
	{
		return tipoTransacaoRecarga;
	}

	/**
	 * Metodo....:getTransactionType
	 * Descricao.:Retorna o transaction type definido
	 * @return long - Transaction Type
	 */
	public long getTransactionType()
	{
		return transactionType;
	}

	/**
	 * Metodo....:getExternalTransactionType
	 * Descricao.:Retorna o external transaction type definido
	 * @return long - External transaction type
	 */
	public long getExternalTransactionType()
	{
		return externalTransactionType;
	}

	/**
	 * Metodo....:isAjuste
	 * Descricao.:Identifica se a combinacao e um ajuste
	 * @return boolean - Indica se e um ajuste
	 */
	public boolean isAjuste()
	{
		return isAjuste;
	}

	/**
	 * Metodo....:isEvento
	 * Descricao.:Identifica se a combinacao e um evento
	 * @return boolean - Identifica se e um evento
	 */
	public boolean isEvento()
	{
		return isEvento;
	}

	/**
	 * Metodo....:isRecarga
	 * Descricao.:Identifica se a combinacao e uma recarga
	 * @return boolean - Identifica se e uma recarga
	 */
	public boolean isRecarga()
	{
		return isRecarga;
	}

	/**
	 * Metodo....:inverterSaldoPrincipal
	 * Descricao.:Retorna se e para lancar o saldo final principal do assinante no CDR como um valor no ajuste
	 * @return boolean - Identifica se e pra lancar o saldo principal como valor no ajuste
	 */
	public boolean inverterSaldoPrincipal()
	{
		return inverterSaldoPrincipal;
	}

	/**
	 *	Retorna se e para lancar o saldo final periodico do assinante no CDR como um valor no ajuste.
	 *
	 *	@return		Identifica se e pra lancar o saldo principal como valor no ajuste.
	 */
	public boolean inverterSaldoPeriodico()
	{
		return this.inverterSaldoPeriodico;
	}

	/**
	 * Metodo....:inverterSaldoBonus
	 * Descricao.:Retorna se e para lancar o saldo de bonus do assinante no CDR como um valor no ajuste
	 * @return boolean - Identifica se e pra lancar o saldo de bonus como valor no ajuste
	 */
	public boolean inverterSaldoBonus()
	{
		return inverterSaldoBonus;
	}

	/**
	 * Metodo....:inverterSaldoSm
	 * Descricao.:Retorna se e para lancar o saldo de SMS do assinante no CDR como um valor no ajuste
	 * @return boolean - Identifica se e pra lancar o saldo de SMS como valor no ajuste
	 */
	public boolean inverterSaldoSm()
	{
		return inverterSaldoSm;
	}

	/**
	 * Metodo....:inverterSaldoDados
	 * Descricao.:Retorna se e para lancar o saldo de dados do assinante no CDR como um valor no ajuste
	 * @return boolean - Identifica se e pra lancar o saldo de dados como valor no ajuste
	 */
	public boolean inverterSaldoDados()
	{
		return inverterSaldoDados;
	}

	/**
	 * Metodo....:setTipoOperacao
	 * Descricao.:Definie o tipo de operacao quando evento
	 * @param tipoOp - Tipo de operacao
	 */
	public void setTipoOperacao(String tipoOp)
	{
		tipoOperacao = tipoOp;
	}

	/**
	 * Metodo....:setIdtAntigoCampo
	 * Descricao.:Define o tipo de operacao quando mudanca de status (evento)
	 * @param idtAntigoCampo - Tipo de operacao anterior
	 */
	public void setIdtAntigoCampo(String idtAntigoCampo)
	{
		this.idtAntigoCampo = idtAntigoCampo;
	}

	/**
	 * Metodo....:setIdtNovoCampo
	 * Descricao.:Define o tipo de operacao referente ao novo status
	 * @param idtNovoCampo - Novo status
	 */
	public void setIdtNovoCampo(String idtNovoCampo)
	{
		this.idtNovoCampo = idtNovoCampo;
	}

	/**
	 * Metodo....:setTipoTransacaoAjuste
	 * Descricao.:Define o tipo de transacao para ajuste
	 * @param ttAjuste - Tipo de transacao de ajuste
	 */
	public void setTipoTransacaoAjuste(String ttAjuste)
	{
		tipoTransacaoAjuste = ttAjuste;
	}

	/**
	 * Metodo....:setTipoTransacaoRecarga
	 * Descricao.:Define o tipo de transacao para uma recarga
	 * @param ttRecarga - Tipo de transacao de recarga
	 */
	public void setTipoTransacaoRecarga(String ttRecarga)
	{
		tipoTransacaoRecarga = ttRecarga;
	}

	/**
	 * Metodo....:setTransactionType
	 * Descricao.:Define o tipo de transacao do objeto
	 * @param tt - Transaction Type
	 */
	public void setTransactionType(long tt)
	{
		transactionType = tt;
	}

	/**
	 * Metodo....:setExternalTransactionType
	 * Descricao.:Define o external transaction type
	 * @param ett - External transaction type
	 */
	public void setExternalTransactionType(long ett)
	{
		externalTransactionType = ett;
	}

	/**
	 * Metodo....:setAjuste
	 * Descricao.:Define se a combinacao e um ajuste
	 * @param int - 1 Verdadeiro 0 Falso
	 */
	public void setAjuste(int ajuste)
	{
		if (ajuste == 1)
			isAjuste = true;
		else isAjuste = false;
	}

	/**
	 * Metodo....:setEvento
	 * Descricao.:Define se a combinacao e um evento
	 * @param evento - 1 verdadeiro 0 falso
	 */
	public void setEvento(int evento)
	{
		if (evento == 1)
			isEvento = true;
		else isEvento = false;
	}

	/**
	 * Metodo....:setRecarga
	 * Descricao.:Define se uma recarga ou nao
	 * @param recarga - 1 verdadeiro 0 falso
	 */
	public void setRecarga(int recarga)
	{
		if (recarga == 1)
			isRecarga = true;
		else isRecarga = false;
	}

	/**
	 * Metodo....:setInverterSaldoPrincipal
	 * Descricao.:Define se o tipo de transacao lanca o saldo principal como valor no ajuste
	 * @param inverteSaldo - Indica se lanca o saldo principal como ajuste
	 */
	public void setInverterSaldoPrincipal(int inverterSaldo)
	{
		if (inverterSaldo == 1)
			inverterSaldoPrincipal = true;
		else inverterSaldoPrincipal = false;
	}

	/**
	 *	Define se e para lancar o saldo final periodico do assinante no CDR como um valor negativo no ajuste.
	 *
	 *	@param		inverterSaldoPeriodico	Identifica se e pra lancar o saldo principal como valor negativo no ajuste.
	 */
	public void setInverterSaldoPeriodico(int inverterSaldoPeriodico)
	{
		if(inverterSaldoPeriodico == 0)			
			this.inverterSaldoPeriodico = false;
		else
			this.inverterSaldoPeriodico = true;
	}

	/**
	 * Metodo....:setInverterSaldoBonus
	 * Descricao.:Define se o tipo de transacao lanca o saldo de bonus como valor no ajuste
	 * @param inverteSaldo - Indica se lanca o saldo de bonus como ajuste
	 */
	public void setInverterSaldoBonus(int inverterSaldo)
	{
		if (inverterSaldo == 1)
			inverterSaldoBonus = true;
		else inverterSaldoBonus = false;
	}

	/**
	 * Metodo....:setInverterSaldoSm
	 * Descricao.:Define se o tipo de transacao lanca o saldo de SMS como valor no ajuste
	 * @param inverteSaldo - Indica se lanca o saldo de SMS como ajuste
	 */
	public void setInverterSaldoSm(int inverterSaldo)
	{
		if (inverterSaldo == 1)
			inverterSaldoSm = true;
		else inverterSaldoSm = false;
	}

	/**
	 * Metodo....:setInverterSaldoDados
	 * Descricao.:Define se o tipo de transacao lanca o saldo de dados como valor no ajuste
	 * @param inverteSaldo - Indica se lanca o saldo de dados como ajuste
	 */
	public void setInverterSaldoDados(int inverterSaldo)
	{
		if (inverterSaldo == 1)
			inverterSaldoDados = true;
		else inverterSaldoDados = false;
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof TipoTransacaoCDR) )
			return false;
		
		TipoTransacaoCDR objCDR = (TipoTransacaoCDR)obj;
		if (objCDR.getTransactionType() != this.getTransactionType())
			return false;
		else if (objCDR.getExternalTransactionType() != this.getExternalTransactionType())
				return false;
		else return true;
	}
	
	public int hashCode()
	{
		String hash = String.valueOf(getTransactionType())+String.valueOf(getExternalTransactionType());
		return hash.hashCode();
	}
}
