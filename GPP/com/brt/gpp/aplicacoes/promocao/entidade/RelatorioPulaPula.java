package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports Java.

import java.text.SimpleDateFormat;
import java.util.Date;

//Imports GPP.

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que representa a entidade da tabela TBL_REL_PULA_PULA.
 * 
 *	@author	Daniel Ferreira
 *	@since	17/04/2006
 *	@deprecated
 */
public class RelatorioPulaPula implements Entidade
{
    
    private	Date	datProcessamento;
    private	int		idtPromocao;
    private	int		idtCodigoNacional;
    private	Date	datInicialExecucao;
    private	Date	datFinalExecucao;
    private	int		qtdAssinantes;
    private	int		qtdStatus0;
    private	int		qtdStatus1;
    private	int		qtdStatus2;
    private	int		qtdStatus3;
    private	int		qtdStatus4;
    private	int		qtdStatus5;
    private	long	numSegundosTotal;
    private	long	numSegundosNormal;
    private	long	numSegundosFF;
    private	long	numSegundosPlanoNoturno;
    private	long	numSegundosPlanoDiurno;
    private long	numSegundosNaoBonificado;
    private	long	numSegundosDuracExcedida;
    private	long	numSegundosExpurgoFraude;
    private	long	numSegundosEstornoFraude;
    private long	numSegundosTup;
    private long	numSegundosAIgualB;
    private long	numSegundosATH;
    private long	numSegundosMovelNaoBrt;
    private long	numSegundosFaleGratis;
    private double	vlrRecargas;
    private	double	vlrBonusTotal;
    private	double	vlrBonusAdiantamento;
    private	double	vlrSaldoPrincipal;
    private double	vlrSaldoBonus;
    private	double	vlrSaldoSms;
    private	double	vlrSaldoGprs;
    private	double	vlrSaldoConcessaoFracionada;
    
    //Constantes internas.
    
    public static final int	QTD_ASSINANTES					=  0;
    public static final int	QTD_VALIDADOS					=  1;
    public static final int	QTD_NAO_VALIDADOS				=  2;
    public static final int	QTD_STATUS_0					=  3;
    public static final int	QTD_STATUS_1					=  4;
    public static final int	QTD_STATUS_2					=  5;
    public static final int	QTD_STATUS_3					=  6;
    public static final int	QTD_STATUS_4					=  7;
    public static final int	QTD_STATUS_5					=  8;
    public static final int	NUM_SEGUNDOS_TOTAL				=  9;
    public static final int	NUM_SEGUNDOS_NORMAL				= 10;
    public static final int	NUM_SEGUNDOS_FF					= 11;
    public static final int	NUM_SEGUNDOS_PLANO_NOTURNO		= 12;
    public static final int	NUM_SEGUNDOS_PLANO_DIURNO		= 13;
    public static final int	NUM_SEGUNDOS_NAO_BONIFICADO		= 14;
    public static final int	NUM_SEGUNDOS_DURAC_EXCEDIDA		= 15;
    public static final int	NUM_SEGUNDOS_EXPURGO_FRAUDE		= 16;
    public static final int	NUM_SEGUNDOS_ESTORNO_FRAUDE		= 17;
    public static final int	NUM_SEGUNDOS_TUP				= 18;
    public static final int	NUM_SEGUNDOS_AIGUALB			= 19;
    public static final int	VLR_RECARGAS					= 20;
    public static final int	VLR_BONUS_BRUTO					= 21;
    public static final int	VLR_BONUS_TOTAL					= 22;
    public static final int	VLR_BONUS_ADIANTAMENTO			= 23;
    public static final int	VLR_SALDO_PRINCIPAL				= 24;
    public static final int	VLR_SALDO_BONUS					= 25;
    public static final int	VLR_SALDO_SMS					= 26;
    public static final int	VLR_SALDO_GPRS					= 27;
    public static final int	VLR_SALDO_CONCESSAO_FRACIONADA	= 28;
    public static final int	NUM_SEGUNDOS_ATH				= 29;
    public static final int	NUM_SEGUNDOS_MOVEL_NAO_BRT		= 30;
    public static final int	NUM_SEGUNDOS_FALE_GRATIS		= 31;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public RelatorioPulaPula()
	{
	    this.reset();
	}
	
	//Getters.
	
	/**
	 *	Retorna a data de processamento.
	 *	
	 *	@return		Date					datProcessamento			Data de processamento.
	 */
	public Date getDatProcessamento() 
	{
		return this.datProcessamento;
	}
	
	/**
	 *	Retorna o identificador da promocao.
	 *	
	 *	@return		int						idtPromocao					Identificador da promocao.
	 */
	public int getIdtPromocao() 
	{
		return this.idtPromocao;
	}
	
	/**
	 *	Retorna o codigo nacional.
	 *	
	 *	@return		int						idtCodigoNacional			Codigo Nacional.
	 */
	public int getIdtCodigoNacional() 
	{
		return this.idtCodigoNacional;
	}
	
	/**
	 *	Retorna a data inicial de execucao do processo.
	 *	
	 *	@return		Date					datInicialExecucao			Data inicial de execucao.
	 */
	public Date getDatInicialExecucao() 
	{
		return this.datInicialExecucao;
	}
	
	/**
	 *	Retorna a data final de execucao do processo.
	 *	
	 *	@return		Date					datFinalExecucao			Data final de execucao.
	 */
	public Date getDatFinalExecucao() 
	{
		return this.datFinalExecucao;
	}
	
	/**
	 *	Retorna o numero de assinantes sumarizados.
	 *	
	 *	@return		int						qtdAssinantes				Numero de assinantes sumarizados.
	 */
	public int getQtdAssinantes() 
	{
		return this.qtdAssinantes;
	}
	
	/**
	 *	Retorna o numero de assinantes nao ativos na plataforma.
	 *	
	 *	@return		int						qtdStatus0					Numero de assinantes nao ativos na plataforma.
	 */
	public int getQtdStatus0() 
	{
		return this.qtdStatus0;
	}
	
	/**
	 *	Retorna o numero de assinantes em status First Time User na plataforma.
	 *	
	 *	@return		int						qtdStatus1					Numero de assinantes em First Time User.
	 */
	public int getQtdStatus1() 
	{
		return this.qtdStatus1;
	}
	
	/**
	 *	Retorna o numero de assinantes em status Normal User na plataforma.
	 *	
	 *	@return		int						qtdStatus2					Numero de assinantes em Normal User.
	 */
	public int getQtdStatus2() 
	{
		return this.qtdStatus2;
	}
	
	/**
	 *	Retorna o numero de assinantes em status Recharge Expired na plataforma.
	 *	
	 *	@return		int						qtdStatus3					Numero de assinantes em Recharge Expired.
	 */
	public int getQtdStatus3() 
	{
		return this.qtdStatus3;
	}
	
	/**
	 *	Retorna o numero de assinantes em status Disconnected na plataforma.
	 *	
	 *	@return		int						qtdStatus4					Numero de assinantes em Disconnected.
	 */
	public int getQtdStatus4() 
	{
		return this.qtdStatus4;
	}
	
	/**
	 *	Retorna o numero de assinantes em status Shutdown na plataforma.
	 *	
	 *	@return		int						qtdStatus5					Numero de assinantes em Shutdown.
	 */
	public int getQtdStatus5() 
	{
		return this.qtdStatus5;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas.
	 *	
	 *	@return		long					numSegundosTotal			Numero total de segundos.
	 */
	public long getNumSegundosTotal() 
	{
		return this.numSegundosTotal;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas com bonificacao normal.
	 *	
	 *	@return		long					numSegundosNormal			Numero de segundos com bonificacao normal.
	 */
	public long getNumSegundosNormal() 
	{
		return this.numSegundosNormal;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Amigos Toda Hora.
	 *	
	 *	@return		long					numSegundosFF				Numero de segundos com bonificacao reduzida: Amigos Toda Hora.
	 */
	public long getNumSegundosFF() 
	{
		return this.numSegundosFF;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Tarifacao Plano Noturno.
	 *	
	 *	@return		long					numSegundosPlanoNoturno		Numero de segundos com bonificacao reduzida: Tarifacao Plano Noturno.
	 */
	public long getNumSegundosPlanoNoturno() 
	{
		return this.numSegundosPlanoNoturno;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Tarifacao Plano Diurno.
	 *	
	 *	@return		long					numSegundosPlanoDiurno		Numero de segundos com bonificacao reduzida: Tarifacao Plano Diurno.
	 */
	public long getNumSegundosPlanoDiurno() 
	{
		return this.numSegundosPlanoDiurno;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas nao bonificadas: Black List.
	 *	
	 *	@return		long					numSegundosNaoBonificado	Numero de segundos nao bonificados: Black List.
	 */
	public long getNumSegundosNaoBonificado() 
	{
		return this.numSegundosNaoBonificado;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas nao bonificadas: Duracao Excedida.
	 *	
	 *	@return		long					numSegundosDuracExcedida	Numero de segundos nao bonificados: Duracao Excedida.
	 */
	public long getNumSegundosDuracExcedida() 
	{
		return this.numSegundosDuracExcedida;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas nao bonificadas: Expurgadas por Fraude.
	 *	
	 *	@return		long					numSegundosExpurgoFraude	Numero de segundos nao bonificados: Expurgadas por Fraude.
	 */
	public long getNumSegundosExpurgoFraude() 
	{
		return this.numSegundosExpurgoFraude;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas nao bonificadas: Estornadas por Fraude.
	 *	
	 *	@return		long					numSegundosEstornoFraude	Numero de segundos nao bonificados: Estornadas por Fraude.
	 */
	public long getNumSegundosEstornoFraude() 
	{
		return this.numSegundosEstornoFraude;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas nao bonificadas: TUP de duracao excedida.
	 *	
	 *	@return		long					numSegundosTup				Numero de segundos nao bonificados: TUP de duracao excedida.
	 */
	public long getNumSegundosTup() 
	{
		return this.numSegundosTup;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas nao bonificadas: Origem = Destino.
	 *	
	 *	@return		long					numSegundosAIgualB			Numero de segundos nao bonificados: Origem = Destino.
	 */
	public long getNumSegundosAIgualB() 
	{
		return this.numSegundosAIgualB;
	}
	
	/**
	 *	Retorna o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Novo Amigos Toda Hora.
	 *	
	 *	@return		long					numSegundosAIgualB			Numero de segundos nao bonificados: Origem = Destino.
	 */
	public long getNumSegundosATH() 
	{
		return this.numSegundosATH;
	}
	
	/**
	 * Retorna o numero total de segundos de ligacoes recebidas não bonificadas: Fale Grátis.
	 * 
     * @return 		long					numSegundosFaleGratis 		Numero de segundos nao bonificados: Fale Grátis.
     */
    public long getNumSegundosFaleGratis()
    {
    	return numSegundosFaleGratis;
    }

	/**
	 * Retorna o numero total de segundos de ligacoes recebidas de móvel não BrT.
	 * 
     * @return 		long					numSegundosMovelNaoBrt 		Numero de segundos recebidas de móvel não BrT.
     */
    public long getNumSegundosMovelNaoBrt()
    {
    	return numSegundosMovelNaoBrt;
    }

	/**
	 *	Retorna o total pago em recargas.
	 *	
	 *	@return		double					vlrRecargas					Total pago em recargas.
	 */
	public double getVlrRecargas() 
	{
		return this.vlrRecargas;
	}
	
	/**
	 *	Retorna o total de bonus liquido acumulado.
	 *	
	 *	@return		double					vlrBonusTotal				Total de bonus liquido.
	 */
	public double getVlrBonusTotal() 
	{
		return this.vlrBonusTotal;
	}
	
	/**
	 *	Retorna o total de bonus concedido como adiantamento.
	 *	
	 *	@return		double					vlrBonusAdiantamento		Total de adiantamento.
	 */
	public double getVlrBonusAdiantamento() 
	{
		return this.vlrBonusAdiantamento;
	}
	
	/**
	 *	Retorna o valor de creditos sumarizado no saldo principal.
	 *	
	 *	@return		double					vlrSaldoPrincipal			Valor sumarizado no saldo principal.
	 */
	public double getVlrSaldoPrincipal() 
	{
		return this.vlrSaldoPrincipal;
	}
	
	/**
	 *	Retorna o valor de creditos sumarizado no saldo de bonus.
	 *	
	 *	@return		double					vlrSaldoBonus				Valor sumarizado no saldo de bonus.
	 */
	public double getVlrSaldoBonus() 
	{
		return this.vlrSaldoBonus;
	}
	
	/**
	 *	Retorna o valor de creditos sumarizado no saldo de SMS.
	 *	
	 *	@return		double					vlrSaldoSms					Valor sumarizado no saldo de SMS.
	 */
	public double getVlrSaldoSms() 
	{
		return this.vlrSaldoSms;
	}
	
	/**
	 *	Retorna o valor de creditos sumarizado no saldo de dados.
	 *	
	 *	@return		double					vlrSaldoGprs				Valor sumarizado no saldo de dados.
	 */
	public double getVlrSaldoGprs() 
	{
		return this.vlrSaldoGprs;
	}
	
	/**
	 *	Retorna o valor de creditos sumarizado para concessao fracionada de bonus Pula-Pula.
	 *	
	 *	@return		double					vlrSaldoConcessaoFracionada	Valor sumarizado para concessao fracionada.
	 */
	public double getVlrSaldoConcessaoFracionada() 
	{
		return this.vlrSaldoConcessaoFracionada;
	}
	
	//Setters.

	/**
	 *	Atribui a data de processamento.
	 *	
	 *	@param		Date					datProcessamento			Data de processamento.
	 */
	public void setDatProcessamento(Date datProcessamento) 
	{
		this.datProcessamento = datProcessamento;
	}
	
	/**
	 *	Atribui o identificador da promocao.
	 *	
	 *	@param		int						idtPromocao					Identificador da promocao.
	 */
	public void setIdtPromocao(int idtPromocao) 
	{
		this.idtPromocao = idtPromocao;
	}
	
	/**
	 *	Atribui o codigo nacional.
	 *	
	 *	@param		int						idtCodigoNacional			Codigo Nacional.
	 */
	public void setIdtCodigoNacional(int idtCodigoNacional) 
	{
		this.idtCodigoNacional = idtCodigoNacional;
	}
	
	/**
	 *	Atribui a data inicial de execucao do processo.
	 *	
	 *	@param		Date					datInicialExecucao			Data inicial de execucao.
	 */
	public void setDatInicialExecucao(Date datInicialExecucao) 
	{
		this.datInicialExecucao = datInicialExecucao;
	}
	
	/**
	 *	Atribui a data final de execucao do processo.
	 *	
	 *	@param		Date					datFinalExecucao			Data final de execucao.
	 */
	public void setDatFinalExecucao(Date datFinalExecucao) 
	{
		this.datFinalExecucao = datFinalExecucao;
	}
	
	/**
	 *	Atribui o numero de assinantes sumarizados.
	 *	
	 *	@param		int						qtdAssinantes				Numero de assinantes sumarizados.
	 */
	public void setQtdAssinantes(int qtdAssinantes) 
	{
		this.qtdAssinantes = qtdAssinantes;
	}
	
	/**
	 *	Atribui o numero de assinantes nao ativos na plataforma.
	 *	
	 *	@param		int						qtdStatus0					Numero de assinantes nao ativos na plataforma.
	 */
	public void setQtdStatus0(int qtdStatus0) 
	{
		this.qtdStatus0 = qtdStatus0;
	}
	
	/**
	 *	Atribui o numero de assinantes em status First Time User na plataforma.
	 *	
	 *	@param		int						qtdStatus1					Numero de assinantes em First Time User.
	 */
	public void setQtdStatus1(int qtdStatus1) 
	{
		this.qtdStatus1 = qtdStatus1;
	}
	
	/**
	 *	Atribui o numero de assinantes em status Normal User na plataforma.
	 *	
	 *	@param		int						qtdStatus2					Numero de assinantes em Normal User.
	 */
	public void setQtdStatus2(int qtdStatus2) 
	{
		this.qtdStatus2 = qtdStatus2;
	}
	
	/**
	 *	Atribui o numero de assinantes em status Recharge Expired na plataforma.
	 *	
	 *	@param		int						qtdStatus3					Numero de assinantes em Recharge Expired.
	 */
	public void setQtdStatus3(int qtdStatus3) 
	{
		this.qtdStatus3 = qtdStatus3;
	}
	
	/**
	 *	Atribui o numero de assinantes em status Disconnected na plataforma.
	 *	
	 *	@param		int						qtdStatus4					Numero de assinantes em Disconnected.
	 */
	public void setQtdStatus4(int qtdStatus4) 
	{
		this.qtdStatus4 = qtdStatus4;
	}
	
	/**
	 *	Atribui o numero de assinantes em status Shutdown na plataforma.
	 *	
	 *	@param		int						qtdStatus5					Numero de assinantes em Shutdown.
	 */
	public void setQtdStatus5(int qtdStatus5) 
	{
		this.qtdStatus5 = qtdStatus5;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas.
	 *	
	 *	@param		long					numSegundosTotal			Numero total de segundos.
	 */
	public void setNumSegundosTotal(long numSegundosTotal) 
	{
		this.numSegundosTotal = numSegundosTotal;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas com bonificacao normal.
	 *	
	 *	@param		long					numSegundosNormal			Numero de segundos com bonificacao normal.
	 */
	public void setNumSegundosNormal(long numSegundosNormal) 
	{
		this.numSegundosNormal = numSegundosNormal;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Amigos Toda Hora.
	 *	
	 *	@param		long					numSegundosFF				Numero de segundos com bonificacao reduzida: Amigos Toda Hora.
	 */
	public void setNumSegundosFF(long numSegundosFF) 
	{
		this.numSegundosFF = numSegundosFF;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Tarifacao Plano Noturno.
	 *	
	 *	@param		long					numSegundosPlanoNoturno		Numero de segundos com bonificacao reduzida: Tarifacao Plano Noturno.
	 */
	public void setNumSegundosPlanoNoturno(long numSegundosPlanoNoturno) 
	{
		this.numSegundosPlanoNoturno = numSegundosPlanoNoturno;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Tarifacao Plano Diurno.
	 *	
	 *	@param		long					numSegundosPlanoDiurno		Numero de segundos com bonificacao reduzida: Tarifacao Plano Diurno.
	 */
	public void setNumSegundosPlanoDiurno(long numSegundosPlanoDiurno) 
	{
		this.numSegundosPlanoDiurno = numSegundosPlanoDiurno;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas nao bonificadas: Black List.
	 *	
	 *	@param		long					numSegundosNaoBonificado	Numero de segundos nao bonificados: Black List.
	 */
	public void setNumSegundosNaoBonificado(long numSegundosNaoBonificado) 
	{
		this.numSegundosNaoBonificado = numSegundosNaoBonificado;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas nao bonificadas: Duracao Excedida.
	 *	
	 *	@param		long					numSegundosDuracExcedida	Numero de segundos nao bonificados: Duracao Excedida.
	 */
	public void setNumSegundosDuracExcedida(long numSegundosDuracExcedida) 
	{
		this.numSegundosDuracExcedida = numSegundosDuracExcedida;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas nao bonificadas: Expurgadas por Fraude.
	 *	
	 *	@param		long					numSegundosExpurgoFraude	Numero de segundos nao bonificados: Expurgadas por Fraude.
	 */
	public void setNumSegundosExpurgoFraude(long numSegundosExpurgoFraude) 
	{
		this.numSegundosExpurgoFraude = numSegundosExpurgoFraude;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas nao bonificadas: Estornadas por Fraude.
	 *	
	 *	@param		long					numSegundosEstornoFraude	Numero de segundos nao bonificados: Estornadas por Fraude.
	 */
	public void setNumSegundosEstornoFraude(long numSegundosEstornoFraude) 
	{
		this.numSegundosEstornoFraude = numSegundosEstornoFraude;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas nao bonificadas: TUP de duracao excedida.
	 *	
	 *	@param		long					numSegundosTup				Numero de segundos nao bonificados: TUP de duracao excedida.
	 */
	public void setNumSegundosTup(long numSegundosTup) 
	{
		this.numSegundosTup = numSegundosTup;
	}
	
	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas nao bonificadas: Origem = Destino.
	 *	
	 *	@param		long					numSegundosAIgualB			Numero de segundos nao bonificados: Origem = Destino.
	 */
	public void setNumSegundosAIgualB(long numSegundosAIgualB) 
	{
		this.numSegundosAIgualB = numSegundosAIgualB;
	}

	/**
	 *	Atribui o numero total de segundos de ligacoes recebidas com bonificacao reduzida: Novo Amigos Toda Hora.
	 *	
	 *	@param		long					numSegundosATH			Numero de segundos de ligacoes recebidas com bonificacao reduzida: Novo Amigos Toda Hora.
	 */
	public void setNumSegundosATH(long numSegundosATH) 
	{
		this.numSegundosATH = numSegundosATH;
	}

	/**
	 * Atribui o numero total de segundos de ligacoes não bonificadas: Fale Grátis.
	 * 
     * @param		long					numSegundosFaleGratis	Numero de segundos de ligacoes não bonificadas: Fale Grátis.
     */
    public void setNumSegundosFaleGratis(long numSegundosFaleGratis)
    {
    	this.numSegundosFaleGratis = numSegundosFaleGratis;
    }

	/**
	 * Atribui o numero total de segundos de ligacoes recebidas de móvel não BrT.
	 * 
     * @param		long					numSegundosMovelNaoBrt	Numero de segundos de ligacoes recebidas de móvel não BrT.
     */
    public void setNumSegundosMovelNaoBrt(long numSegundosMovelNaoBrt)
    {
    	this.numSegundosMovelNaoBrt = numSegundosMovelNaoBrt;
    }
	
	/**
	 *	Atribui o total pago em recargas.
	 *	
	 *	@param		double					vlrRecargas					Total pago em recargas.
	 */
	public void setVlrRecargas(double vlrRecargas) 
	{
		this.vlrRecargas = vlrRecargas;
	}
	
	/**
	 *	Atribui o total de bonus liquido acumulado.
	 *	
	 *	@param		double					vlrBonusTotal				Total de bonus liquido.
	 */
	public void setVlrBonusTotal(double vlrBonusTotal) 
	{
		this.vlrBonusTotal = vlrBonusTotal;
	}
	
	/**
	 *	Atribui o total de bonus concedido como adiantamento.
	 *	
	 *	@param		double					vlrBonusAdiantamento		Total de adiantamento.
	 */
	public void setVlrBonusAdiantamento(double vlrBonusAdiantamento) 
	{
		this.vlrBonusAdiantamento = vlrBonusAdiantamento;
	}
	
	/**
	 *	Atribui o valor de creditos sumarizado no saldo principal.
	 *	
	 *	@param		double					vlrSaldoPrincipal			Valor sumarizado no saldo principal.
	 */
	public void setVlrSaldoPrincipal(double vlrSaldoPrincipal) 
	{
		this.vlrSaldoPrincipal = vlrSaldoPrincipal;
	}
	
	/**
	 *	Atribui o valor de creditos sumarizado no saldo de bonus.
	 *	
	 *	@param		double					vlrSaldoBonus				Valor sumarizado no saldo de bonus.
	 */
	public void setVlrSaldoBonus(double vlrSaldoBonus) 
	{
		this.vlrSaldoBonus = vlrSaldoBonus;
	}
	
	/**
	 *	Atribui o valor de creditos sumarizado no saldo de SMS.
	 *	
	 *	@param		double					vlrSaldoSms					Valor sumarizado no saldo de SMS.
	 */
	public void setVlrSaldoSms(double vlrSaldoSms) 
	{
		this.vlrSaldoSms = vlrSaldoSms;
	}
	
	/**
	 *	Atribui o valor de creditos sumarizado no saldo de dados.
	 *	
	 *	@param		double					vlrSaldoGprs				Valor sumarizado no saldo de dados.
	 */
	public void setVlrSaldoGprs(double vlrSaldoGprs) 
	{
		this.vlrSaldoGprs = vlrSaldoGprs;
	}
	
	/**
	 *	Atribui o valor de creditos sumarizado para concessao fracionada de bonus Pula-Pula.
	 *	
	 *	@param		double					vlrSaldoConcessaoFracionada	Valor sumarizado para concessao fracionada.
	 */
	public void setVlrSaldoConcessaoFracionada(double vlrSaldoConcessaoFracionada) 
	{
		this.vlrSaldoConcessaoFracionada = vlrSaldoConcessaoFracionada;
	}
	
	//Implementacao de Entidade.
	
	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
	    RelatorioPulaPula result = new RelatorioPulaPula();
	    
	    result.setDatProcessamento((this.datProcessamento != null) ? new Date(this.datProcessamento.getTime()) : null);
	    result.setIdtPromocao(this.idtPromocao);
	    result.setIdtCodigoNacional(this.idtCodigoNacional);
	    result.setDatInicialExecucao((this.datInicialExecucao != null) ? new Date(this.datInicialExecucao.getTime()) : null);
	    result.setDatFinalExecucao((this.datFinalExecucao != null) ? new Date(this.datFinalExecucao.getTime()) : null);
	    result.setQtdAssinantes(this.qtdAssinantes);
	    result.setQtdStatus0(this.qtdStatus0);
	    result.setQtdStatus1(this.qtdStatus1);
	    result.setQtdStatus2(this.qtdStatus2);
	    result.setQtdStatus3(this.qtdStatus3);
	    result.setQtdStatus4(this.qtdStatus4);
	    result.setQtdStatus5(this.qtdStatus5);
	    result.setNumSegundosTotal(this.numSegundosTotal);
	    result.setNumSegundosNormal(this.numSegundosNormal);
	    result.setNumSegundosFF(this.numSegundosFF);
	    result.setNumSegundosPlanoNoturno(this.numSegundosPlanoNoturno);
	    result.setNumSegundosPlanoDiurno(this.numSegundosPlanoDiurno);
	    result.setNumSegundosNaoBonificado(this.numSegundosNaoBonificado);
	    result.setNumSegundosDuracExcedida(this.numSegundosDuracExcedida);
	    result.setNumSegundosExpurgoFraude(this.numSegundosExpurgoFraude);
	    result.setNumSegundosEstornoFraude(this.numSegundosEstornoFraude);
	    result.setNumSegundosTup(this.numSegundosTup);
	    result.setNumSegundosAIgualB(this.numSegundosAIgualB);
	    result.setNumSegundosATH(this.numSegundosATH);
	    result.setNumSegundosMovelNaoBrt(this.numSegundosMovelNaoBrt);
	    result.setNumSegundosFaleGratis(this.numSegundosFaleGratis);
	    result.setVlrRecargas(this.vlrRecargas);
	    result.setVlrBonusTotal(this.vlrBonusTotal);
	    result.setVlrBonusAdiantamento(this.vlrBonusAdiantamento);
	    result.setVlrSaldoPrincipal(this.vlrSaldoPrincipal);
	    result.setVlrSaldoBonus(this.vlrSaldoBonus);
	    result.setVlrSaldoSms(this.vlrSaldoSms);
	    result.setVlrSaldoGprs(this.vlrSaldoGprs);
	    result.setVlrSaldoConcessaoFracionada(this.vlrSaldoConcessaoFracionada);
	    
	    return result;
	}
	
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
		
		if(!(object instanceof RelatorioPulaPula))
		{
			return false;
		}
		
		if(this.hashCode() != ((RelatorioPulaPula)object).hashCode())
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
		result.append((this.datProcessamento != null) ? this.datProcessamento.toString() : "NULL");
		result.append("||");
		result.append(String.valueOf(this.idtPromocao));
		result.append("||");
		result.append(String.valueOf(this.idtCodigoNacional));
		
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
	    SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);	    
	    
	    result.append("Data de Processamento: ");
	    result.append((this.datProcessamento != null) ? conversorDate.format(this.datProcessamento) : "NULL");
		result.append(" - ");
		result.append("Promocao: ");
		result.append(String.valueOf(this.idtPromocao));
		result.append(" - ");
		result.append("Codigo Nacional: ");
		result.append(String.valueOf(this.idtCodigoNacional));
	    
		return result.toString();
	}

	//Outros metodos.
	
	/**
	 *	Adiciona o valor ao campo informado.
	 * 
	 *	@param		int						field						Campo informado. Cada valor representa um
	 *																	campo ou propriedade do objeto. Se o campo
	 *																	for inválido, o processo nao faz nada.
	 *	@param		double					value		 				Valor a ser adicionado. Caso o campo a ser
	 *																	adicionado seja de um tipo de dados diferente
	 *																	de double, o metodo fara a conversao. Se o
	 *																	campo informado nao possuir casas decimais,
	 *																	o metodo ira truncar o valor.
	 */
	public void add(int field, double value)
	{
	    switch(field)
	    {
	    	case RelatorioPulaPula.QTD_ASSINANTES:
	    	{
	    	    this.qtdAssinantes += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.QTD_STATUS_0:
	    	{
	    	    this.qtdStatus0 += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.QTD_STATUS_1:
	    	{
	    	    this.qtdStatus1 += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.QTD_STATUS_2:
	    	{
	    	    this.qtdStatus2 += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.QTD_STATUS_3:
	    	{
	    	    this.qtdStatus3 += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.QTD_STATUS_4:
	    	{
	    	    this.qtdStatus4 += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.QTD_STATUS_5:
	    	{
	    	    this.qtdStatus5 += new Double(value).intValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_TOTAL:
	    	{
	    	    this.numSegundosTotal += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_NORMAL:
	    	{
	    	    this.numSegundosNormal += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_FF:
	    	{
	    	    this.numSegundosFF += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_PLANO_NOTURNO:
	    	{
	    	    this.numSegundosPlanoNoturno += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_PLANO_DIURNO:
	    	{
	    	    this.numSegundosPlanoDiurno += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_NAO_BONIFICADO:
	    	{
	    	    this.numSegundosNaoBonificado += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_DURAC_EXCEDIDA:
	    	{
	    	    this.numSegundosDuracExcedida += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE:
	    	{
	    	    this.numSegundosExpurgoFraude += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE:
	    	{
	    	    this.numSegundosEstornoFraude += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_TUP:
	    	{
	    	    this.numSegundosTup += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_AIGUALB:
	    	{
	    	    this.numSegundosAIgualB += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_ATH:
	    	{
	    	    this.numSegundosATH += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_MOVEL_NAO_BRT:
	    	{
	    	    this.numSegundosMovelNaoBrt += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.NUM_SEGUNDOS_FALE_GRATIS:
	    	{
	    	    this.numSegundosFaleGratis += new Double(value).longValue();
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_RECARGAS:
	    	{
	    	    this.vlrRecargas += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_BONUS_TOTAL:
	    	{
	    	    this.vlrBonusTotal += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_BONUS_ADIANTAMENTO:
	    	{
	    	    this.vlrBonusAdiantamento += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_SALDO_PRINCIPAL:
	    	{
	    	    this.vlrSaldoPrincipal += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_SALDO_BONUS:
	    	{
	    	    this.vlrSaldoBonus += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_SALDO_SMS:
	    	{
	    	    this.vlrSaldoSms += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_SALDO_GPRS:
	    	{
	    	    this.vlrSaldoGprs += value;
	    	    break;
	    	}
	    	case RelatorioPulaPula.VLR_SALDO_CONCESSAO_FRACIONADA:
	    	{
	    	    this.vlrSaldoConcessaoFracionada += value;
	    	    break;
	    	}
	    	default: break;
	    }
	}
	
	/**
	 *	Inicializa o objeto.
	 */
	public void reset()
	{
	    this.datProcessamento				= null;
	    this.idtPromocao					= -1;
	    this.idtCodigoNacional				= -1;
	    this.datInicialExecucao				= null;
	    this.datFinalExecucao				= null;
	    this.qtdAssinantes					= 0;
	    this.qtdStatus0						= 0;
	    this.qtdStatus1						= 0;
	    this.qtdStatus2						= 0;
	    this.qtdStatus3						= 0;
	    this.qtdStatus4						= 0;
	    this.qtdStatus5						= 0;
	    this.numSegundosTotal				= 0;
	    this.numSegundosNormal				= 0;
	    this.numSegundosFF					= 0;
	    this.numSegundosPlanoNoturno		= 0;
	    this.numSegundosPlanoDiurno			= 0;
	    this.numSegundosNaoBonificado		= 0;
	    this.numSegundosDuracExcedida		= 0;
	    this.numSegundosExpurgoFraude		= 0;
	    this.numSegundosEstornoFraude		= 0;
	    this.numSegundosTup					= 0;
	    this.numSegundosAIgualB				= 0;
	    this.numSegundosATH					= 0;
	    this.numSegundosMovelNaoBrt			= 0;
	    this.numSegundosFaleGratis			= 0;
	    this.vlrRecargas					= 0.0;
	    this.vlrBonusTotal					= 0.0;
	    this.vlrBonusAdiantamento			= 0.0;
	    this.vlrSaldoPrincipal				= 0.0;
	    this.vlrSaldoBonus					= 0.0;
	    this.vlrSaldoSms					= 0.0;
	    this.vlrSaldoGprs					= 0.0;
	    this.vlrSaldoConcessaoFracionada	= 0.0;
	}
	
}
