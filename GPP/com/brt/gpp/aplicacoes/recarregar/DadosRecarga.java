// Definicao do Pacote
package com.brt.gpp.aplicacoes.recarregar;

import java.text.DecimalFormat;

/**
  *
  * Este arquivo contem a definicao da classe de Dados de Recarga 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Daniel Cintra Abib
  * Data:               22/03/2002
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class DadosRecarga
{
	private String MSISDN;
	private String tipoTransacao;
	private String identificacaoRecarga; 
	private String tipoCredito; 
	private String dataHora;
	private String sistemaOrigem; 
	private String operador;
	private String nsuInstituicao;
	private String hashCC;
	private String codLoja;
	private String cpf;

	private double valorPrincipal;
	private double valorBonus;
	private double valorSm;
	private double valorDados;

	private int numDiasExpPrincipal;
	private int numDiasExpBonus;
	private int numDiasExpSm;
	private int numDiasExpDados;
	private int prioridade;
	
	private String  mensagem;
	private String  tipoSMS;
	private boolean enviaSMS;
	private boolean zerarSaldoBonus;
	private boolean zerarSaldoSms;
	private boolean zerarSaldoGprs;
    
    private String dataContabil;
    private String terminal;
    private String tipoTerminal;
		
	// O campo status da recarga e utilizado no processo de filas de recarga, sendo assumido os seguintes
	// valores:
	// 0 (zero) - A recarga foi inserida na fila e ainda nao foi processada
	// 1 (um)   - A recarga foi efetivada, porem ainda nao foi enviado o SMS vinculado
	// 2 (dois) - A recarga foi efetivada e o SMS foi enviado. Caso nao seja necessario enviar o SMS, a recarga
	//            tambem fica com este mesmo valor no status
	private int     statusRecarga;
	private int 	codigoErro;

	//
	// Metodo Getters
	//
	
	/**
	 * Metodo...: getMSISDN
	 * Descricao: Retorna o MSISDN para realizar a recarga
	 * @return 	String	MSISDN	MSISDN do Assinante
	 */
	public String getMSISDN() 
	{
		return this.MSISDN;
	}
	
	/**
	 * Metodo...: getMensagem
	 * Descricao: Retorna a mensagem do SMS relacionada a recarga
	 * @return 	String	Mensagem SMS a ser enviada juntamente com a recarga
	 */
	public String getMensagem()
	{
		return this.mensagem;
	}

	/**
	 * Metodo...: getStatusRecarga
	 * Descricao: Retorna o status da recarga
	 * @return 	int	Status da recarga
	 */
	public int getStatusRecarga()
	{
		return this.statusRecarga;
	}

	/**
	 * Metodo...: getCodigoErro
	 * Descricao: Retorna o Codigo de retorno da execucao da recarga
	 * @return 	int	Codigo de retorno da execucao da recarga
	 */
	public int getCodigoErro()
	{
		return this.codigoErro;
	}
	
	/**
	 * Metodo...: getPrioridade
	 * Descricao: Retorna o codigo de prioridade do envio de mensagem
	 * @return 	int	Codigo de prioridade do envio de mensagem
	 */
	public int getPrioridade()
	{
		return this.prioridade;
	}

	/**
	 * Metodo...: getTipoSMS
	 * Descricao: Retorna o tipo de SMS que sera enviado
	 * @return 	String	Tipo de SMS a ser enviado
	 */
	public String getTipoSMS()
	{
		return this.tipoSMS;
	}

	/**
	 * Metodo...: enviaSMS
	 * Descricao: Indica se deve ser enviada a mensagem juntamente com a recarga
	 * @return 	boolean - Indica se a mensagem SMS deve ser enviada
	 */
	public boolean enviaSMS()
	{
		return this.enviaSMS;
	}

	/**
	 * Metodo....:zerarSaldoBonus
	 * Descricao.:Retorna a flag que indica que o saldo de bonus deve ser zerado antes da concessao
	 * @return int - Flag indicando se o saldo de bonus deve ser zerado ou nao
	 */
	public boolean zerarSaldoBonus()
	{
		return this.zerarSaldoBonus;
	}

	/**
	 * Metodo....:zerarSaldoSms
	 * Descricao.:Retorna a flag que indica que o saldo de SMS deve ser zerado antes da concessao
	 * @return int - Flag indicando se o saldo de SMS deve ser zerado ou nao
	 */
	public boolean zerarSaldoSms()
	{
		return this.zerarSaldoSms;
	}

	/**
	 * Metodo....:zerarSaldoGprs
	 * Descricao.:Retorna a flag que indica que o saldo de dados deve ser zerado antes da concessao
	 * @return int - Flag indicando se o saldo dedados deve ser zerado ou nao
	 */
	public boolean zerarSaldoGprs()
	{
		return this.zerarSaldoGprs;
	}

	/**
	 * Metodo...: getDataHora
	 * Descricao: Retorna a data e hora da Recarga
	 * @return 	String	DataHora	Data/Hora da Recarga
	 */
	public String getDataHora() 
	{
		return this.dataHora;
	}

	/**
	 * Metodo...: getIdentificacaoRecarga
	 * Descricao: Retorna a identificacao da recarga
	 * @return 	String	IdentificacaoRecarga	Identificador da Recarga
	 */
	public String getIdentificacaoRecarga() 
	{
		return this.identificacaoRecarga;
	}


	/**
	 * Metodo...: getOperador 
	 * Descricao: Retorna o Operador que esta realizando a recarga
	 * @return 	String	Operador	Id do Operador
	 */
	public String getOperador() 
	{
		return this.operador;
	}

	/**
	 * Metodo...: getSistemaOrigem
	 * Descricao: Retorna o sistema de origem que esta realizando a recarga
	 * @return 	String	SistemaOrigem	Sistema de Origem da Recarga/Ajuste
	 */
	public String getSistemaOrigem() 
	{
		return this.sistemaOrigem;
	}

	/**
	 * Metodo...: getTipoCredito
	 * Descricao: Retorna o tipo de credito
	 * @return 	String	TipoCredito	Tipo de Crédito
	 */
	public String getTipoCredito() 
	{
		return this.tipoCredito;
	}

	/**
	 * Metodo...: getTipoTransacao
	 * Descricao: Retorna o tipo de transacao
	 * @return String	TipoTransacao	Transaction Type
	 */
	public String getTipoTransacao() 
	{
		return this.tipoTransacao;
	}

	/**
	 * Metodo...: getValorPrincipal
	 * Descricao: Retorna o valor da recarga no saldo principal
	 * @return double 	valor	Valor da Recarga do Saldo Principal
	 */
	public double getValorPrincipal() 
	{
		return this.valorPrincipal;
	}

	/**
	 * Metodo...: getCodLoja
	 * Descricao: Retorna o Código da Loja
	 * @return	String	Código da Loja
	 */
	public String getCodLoja() 
	{
		return this.codLoja;
	}

	/**
	 * Metodo...: getCpf
	 * Descricao: Retorna o cpf
	 * @return	String	Cpf
	 */
	public String getCpf() 
	{
		return this.cpf;
	}

	/**
	 * Metodo...: getHashCC
	 * Descricao: Retorna o Hash do Cartão de Crédito
	 * @return	String		Hash do Cartão de Crédito
	 */
	public String getHashCC() 
	{
		return this.hashCC;
	}

	/**
	 * Metodo...: getNsuInstituicao
	 * Descricao: Retorna o NSU da Instituicao solicitadora da Recarga
	 * @return	String	NSU da Instituição
	 */
	public String getNsuInstituicao() 
	{
		return this.nsuInstituicao;
	}

	/**
	 * Metodo....:getNumDiasExpDados
	 * Descricao.:Retorna o numero de dias de expiracao para a recarga no saldo de dados (GPRS)
	 * @return int - Numero de dias de expiracao para a recarga no saldo de dados (GPRS)
	 */
	public int getNumDiasExpDados() 
	{
		return this.numDiasExpDados;
	}

	/**
	 * Metodo....:getNumDiasExpPrincipal
	 * Descricao.:Retorna o numero de dias de expiracao para a recarga no saldo principal
	 * @return int - Numero de dias de expiracao para a recarga no saldo principal
	 */
	public int getNumDiasExpPrincipal()
	{
		return this.numDiasExpPrincipal;
	}

	/**
	 * Metodo....:getNumDiasExpSm
	 * Descricao.:Retorna o numero de dias de expiracao para a recarga no saldo de SMS
	 * @return int - Numero de dias de expiracao para a recarga no saldo de SMS
	 */
	public int getNumDiasExpSm()
	{
		return this.numDiasExpSm;
	}

	/**
	 * Metodo....:getNumDiasExpBonus
	 * Descricao.:Retorna o numero de dias de expiracao para a recarga no saldo de bonus
	 * @return int - Numero de dias de expiracao para a recarga no saldo de bonus
	 */
	public int getNumDiasExpBonus()
	{
		return this.numDiasExpBonus;
	}
	
	/**
	 * Metodo....:getValorBonus
	 * Descricao.:Retorna o valor da recarga no saldo de bonus
	 * @return double - Valor da recarga no saldo de bonus
	 */
	public double getValorBonus()
	{
		return this.valorBonus;
	}

	/**
	 * Metodo....:getValorDados
	 * Descricao.:Retorna o valor da recarga no saldo de dados
	 * @return double - Valor da recarga no saldo de dados
	 */
	public double getValorDados()
	{
		return this.valorDados;
	}

	/**
	 * Metodo....:getValorSm
	 * Descricao.:Retorna o valor da recarga no saldo de SMS
	 * @return double - Valor da recarga no saldo de SMS
	 */
	public double getValorSm()
	{
		return this.valorSm;
	}

	//
	// Setters
	//
	
	/**
	 * Metodo...: setDataHora
	 * Descricao: Atribui a data e hora da recarga
	 * @param 	String	aDataHora	Data/hora da Recarga
	 */
	public void setDataHora(String aDataHora) 
	{
		this.dataHora = aDataHora;
	}

	/**
	 * Metodo...: setIdentificacaoRecarga
	 * Descricao: Atribui a identificacao da recarga
	 * @param String 	aIdentificacaoRecarga	Identificador da Recarga
	 */
	public void setIdentificacaoRecarga(String aIdentificacaoRecarga) 
	{
		this.identificacaoRecarga = aIdentificacaoRecarga;
	}

	/**
	 * Metodo...: setMSISDN
	 * Descricao: Atribui o MSISDN que esta sendo recarregado
	 * @param 	String	aMSISDN	MSISDN do Assinante
	 */
	public void setMSISDN(String aMSISDN) 
	{
		this.MSISDN = aMSISDN;
	}

	/**
	 * Metodo...: setMensagem
	 * Descricao: Atribui a mensagem de SMS a ser enviada juntamente com a recarga
	 * @param 	String	aMensagem - Mensagem SMS a ser enviada
	 */
	public void setMensagem(String aMensagem) 
	{
		this.mensagem = aMensagem;
	}

	/**
	 * Metodo...: setTipoSMS
	 * Descricao: Atribui o Tipo do SMS a ser enviado
	 * @param 	String	aTipoSMS tipo de SMS a ser enviado
	 */
	public void setTipoSMS(String aTipoSMS) 
	{
		this.tipoSMS = aTipoSMS;
	}

	/**
	 * Metodo...: setStatusRecarga
	 * Descricao: Atribui o Status da recarga
	 * @param 	int	aStatusRecarga - Status da recarga
	 */
	public void setStatusRecarga(int aStatusRecarga) 
	{
		this.statusRecarga = aStatusRecarga;
	}

	/**
	 * Metodo...: setCodigoErro
	 * Descricao: Atribui o codigo de erro da execucao da recarga
	 * @param 	int	- codigo de erro da execucao da recarga
	 */
	public void setCodigoErro(int aCodigoErro) 
	{
		this.codigoErro = aCodigoErro;
	}
	
	/**
	 * Metodo...: setPrioridade
	 * Descricao: Atribui o codigo de prioridade do envio de mensagem
	 * @param 	int	- codigo de prioridade do envio de mensagem
	 */
	public void setPrioridade(int aPrioridade) 
	{
		this.prioridade = aPrioridade;
	}

	/**
	 * Metodo...: setEnviaSMS
	 * Descricao: Atribui o flag para indicar se a mensagem SMS deve ser enviada
	 * @param 	boolean - Indica se a mensagem SMS sera enviada
	 */
	public void setEnviaSMS(boolean aEnviaSMS) 
	{
		this.enviaSMS = aEnviaSMS;
	}

	/**
	 * Metodo....:setIndZerarSaldoBonus
	 * Descricao.:Define a flag indicando se o saldo de bonus deve ser zerado antes da concessao
	 * @param	int		Flag indicando se o saldo de bonus deve ser zerado.
	 */
	public void setZerarSaldoBonus(boolean zerarSaldoBonus)
	{
		this.zerarSaldoBonus = zerarSaldoBonus;
	}
	
	/**
	 * Metodo....:setIndZerarSaldoSms
	 * Descricao.:Define a flag indicando se o saldo de SMS deve ser zerado antes da concessao
	 * @param	int		Flag indicando se o saldo de SMS deve ser zerado.
	 */
	public void setZerarSaldoSms(boolean zerarSaldoSms)
	{
		this.zerarSaldoSms = zerarSaldoSms;
	}
	
	/**
	 * Metodo....:setIndZerarSaldoGprs
	 * Descricao.:Define a flag indicando se o saldo de dados deve ser zerado antes da concessao
	 * @param	int		Flag indicando se o saldo de dados deve ser zerado.
	 */
	public void setZerarSaldoGprs(boolean zerarSaldoGprs)
	{
		this.zerarSaldoGprs = zerarSaldoGprs;
	}
	
	/**
	 * Metodo...: setOperador
	 * Descricao: Atribui o operador que esta realizando a recarga
	 * @param 	String	aOperador	Operador
	 */
	public void setOperador(String aOperador) 
	{
		this.operador = aOperador;
	}

	/**
	 * Metodo...: setSistemaOrigem
	 * Descricao: Atribui o sistema que esta originando a recarga
	 * @param String	aSistemaOrigem	Sistema de Origem que solicitou a recarga/ajuste
	 */
	public void setSistemaOrigem(String aSistemaOrigem) 
	{
		this.sistemaOrigem = aSistemaOrigem;
	}

	/**
	 * Metodo...: setTipoCredito
	 * Descricao: Atribui o tipo de credito da recarga
	 * @param String	aTipoCredito	Tipo de Crédito
	 */
	public void setTipoCredito(String aTipoCredito) 
	{
		this.tipoCredito = aTipoCredito;
	}

	/**
	 * Metodo...: setTipoTransacao
	 * Descricao: Atribui o tipo de transacao da recarga
	 * @param String	aTipoTransacao	Transaction Type
	 */
	public void setTipoTransacao(String aTipoTransacao) 
	{
		this.tipoTransacao = aTipoTransacao;
	}

	/**
	 * Metodo...: setValorPrincipal
	 * Descricao: Atribui o valor da recarga do saldo principal
	 * @param aValor
	 */
	public void setValorPrincipal(double aValor) 
	{
		this.valorPrincipal = aValor;
	}
	

	/**
	 * Metodo...: setCodLoja
	 * Descricao: Seta o Código da Loja
	 * @param 	String	string	código da loja
	 */
	public void setCodLoja(String string) {
		this.codLoja = string;
	}

	/**
	 * Metodo...: setCpf
	 * Descricao: Seta o Cpf
	 * @param String 	string	CPF
	 */
	public void setCpf(String string) {
		this.cpf = string;
	}

	/**
	 * Metodo...: setHashCC
	 * Descricao: Seta Hash Cartão Crédito
	 * @param String	string	Hash CC
	 */
	public void setHashCC(String string) {
		this.hashCC = string;
	}

	/**
	 * Metodo...: setNsuInstituicao
	 * Descricao: Seta Nsu da Instituição
	 * @param String	string	Nsu da Instituição  
	 */
	public void setNsuInstituicao(String string) {
		this.nsuInstituicao = string;
	}

	/**
	 * Metodo....:setNumDiasExpDados
	 * Descricao.:Define o numero de dias de expiracao para a recarga no saldo de dados
	 * @param numDiasExpDados The numDiasExpDados to set.
	 */
	public void setNumDiasExpDados(int numDiasExpDados)
	{
		this.numDiasExpDados = numDiasExpDados;
	}

	/**
	 * Metodo....:setNumDiasExpPrincipal
	 * Descricao.:Define o numero de dias de expiracao para a recarga no saldo principal
	 * @param numDiasExpPrincipal The numDiasExpPrincipal to set.
	 */
	public void setNumDiasExpPrincipal(int numDiasExpPrincipal)
	{
		this.numDiasExpPrincipal = numDiasExpPrincipal;
	}

	/**
	 * Metodo....:setNumDiasExpSm
	 * Descricao.:Define o numero de dias de expiracao para a recarga no saldo de SMS
	 * @param numDiasExpSm The numDiasExpSm to set.
	 */
	public void setNumDiasExpSm(int numDiasExpSm)
	{
		this.numDiasExpSm = numDiasExpSm;
	}

	/**
	 * Metodo....:setNumDiasExpBonus
	 * Descricao.:Define o numero de dias de expiracao para a recarga no saldo de bonus
	 * @param numDiasExpBonus The numDiasExpBonus to set.
	 */
	public void setNumDiasExpBonus(int numDiasExpBonus)
	{
		this.numDiasExpBonus = numDiasExpBonus;
	}

	/**
	 * Metodo....:setValorBonus
	 * Descricao.:Define o valor da recarga no saldo de bonus
	 * @param setValorBonus The valorBonus to set.
	 */
	public void setValorBonus(double valorBonus)
	{
		this.valorBonus = valorBonus;
	}

	/**
	 * Metodo....:setValorDados
	 * Descricao.:Define o valor da recarga no saldo de dados
	 * @param setValorDados The valorDados to set.
	 */
	public void setValorDados(double valorDados)
	{
		this.valorDados = valorDados;
	}

	/**
	 * Metodo....:setValorSm
	 * Descricao.:Define o valor da recarga no saldo de SMS
	 * @param setValorSm The valorSm to set.
	 */
	public void setValorSm(double valorSm)
	{
		this.valorSm = valorSm;
	}
	
	/**
	 * @see hashCode
	 */
	public int hashCode()
	{
		return getIdentificacaoRecarga().hashCode();
	}

	public String getDataContabil()
    {
        return dataContabil;
    }

    public void setDataContabil(String dataContabil)
    {
        this.dataContabil = dataContabil;
    }

    public String getTerminal()
    {
        return terminal;
    }

    public void setTerminal(String terminal)
    {
        this.terminal = terminal;
    }

    public String getTipoTerminal()
    {
        return tipoTerminal;
    }

    public void setTipoTerminal(String tipoTerminal)
    {
        this.tipoTerminal = tipoTerminal;
    }

    /**
	 * @see equals
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof DadosRecarga) )
			return false;
		
		DadosRecarga aux = (DadosRecarga)obj;
		if ( this.getIdentificacaoRecarga().equals(aux.getIdentificacaoRecarga()) &&
			 this.getDataHora().equals(aux.getDataHora()) &&
			 this.getTipoTransacao().equals(aux.getTipoTransacao())
			)
			return true;
		
		return false;
	}

	/**
	 * @see toString
	 */
	public String toString()
	{
		DecimalFormat df = new DecimalFormat("##,##0.00");
		return "MSISDN: "    + getMSISDN()                 +
		       " TT: "       + getTipoTransacao()          +
		       " Status: "   + getStatusRecarga()          +
			   " Princ: " + df.format(getValorPrincipal()) +
			   " Bonus: " + df.format(getValorBonus())     +
			   " SMS: "   + df.format(getValorSm())        +
			   " Dados: " +df.format(getValorDados());
	}

}
