package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports Java.

import java.util.Collection;

/**
 *	Classe que representa as informacoes referentes aos valores de estorno de bonus Pula-Pula por fraude.
 * 
 *	@author	Daniel Ferreira
 *	@since	20/12/2005
 */
public class CalculoEstornoPulaPula
{

	private double				valorExpurgo;
	private double				valorExpurgoSaturado;
	private double				valorExpurgoAgendadoDefault;
	private double				valorExpurgoAgendadoParcial;
	private double				valorEstorno;
	private double				valorEstornoEfetivo;
	private TotalizacaoPulaPula	totalizacao;
	private Collection			listaCdrs;
	
	//Constantes internas.
	
	public static final int	EXPURGO						= 0;
	public static final int	EXPURGO_SATURADO			= 1;
	public static final int	EXPURGO_AGENDADO_DEFAULT	= 2;
	public static final int	EXPURGO_AGENDADO_PARCIAL	= 3;
	public static final int	ESTORNO						= 4;
	public static final int ESTORNO_EFETIVO				= 5;
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public CalculoEstornoPulaPula()
	{
		this.valorExpurgo					= 0.0;
		this.valorExpurgoSaturado			= 0.0;
		this.valorExpurgoAgendadoDefault	= 0.0;
		this.valorExpurgoAgendadoParcial	= 0.0;
		this.valorEstorno					= 0.0;
		this.totalizacao					= null;
		this.listaCdrs						= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito).
	 * 
	 *	@return		double					valorExpurgo				Valor de expurgo.
	 */
	public double getValorExpurgo() 
	{
		return this.valorExpurgo;
	}
	
	/**
	 *	Retorna o valor expurgado (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) devido
	 *	ao limite de bonus Pula-Pula ultrapassado para o assinante. Quando o assinante ultrapassa o limite de seu 
	 *	Pula-Pula, as suas ligacoes fraudulentas devem ser expurgadas ate que seja atingido o limite da promocao.
	 *	A partir deste ponto as ligacoes devem ser estornadas, uma vez que nao ha mais ligacoes legitimas que
	 *	"compensem" as ligacoes fraudulentas.
	 * 
	 *	@return		double					valorExpurgoSaturado		Valor expurgado por limite ultrapassado.
	 */
	public double getValorExpurgoSaturado() 
	{
		return this.valorExpurgoSaturado;
	}
	
	/**
	 *	Retorna o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) com 
	 *	deducao do valor no registro agendado na fila de recargas devido a concessao default.
	 * 
	 *	@return		double					valorExpurgoAgendadoDefault	Valor de expurgo default agendado na fila de recargas.
	 */
	public double getValorExpurgoAgendadoDefault() 
	{
		return this.valorExpurgoAgendadoDefault;
	}
	
	/**
	 *	Retorna o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) com 
	 *	deducao do valor no registro agendado na fila de recargas devido a concessao parcial.
	 * 
	 *	@return		double					valorExpurgoAgendadoParcial	Valor de expurgo parcial agendado na fila de recargas.
	 */
	public double getValorExpurgoAgendadoParcial() 
	{
		return this.valorExpurgoAgendadoParcial;
	}
	
	/**
	 *	Retorna o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito).
	 * 
	 *	@return		double					valorEstorno				Valor de estorno.
	 */
	public double getValorEstorno() 
	{
		return this.valorEstorno;
	}
	
	/**
	 *	Retorna o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito) efetivo.
	 * 
	 *	@return		double					valorEstornoEfetivo			Valor de estorno efetivo.
	 */
	public double getValorEstornoEfetivo() 
	{
		return this.valorEstornoEfetivo;
	}
	
	/**
	 *	Retorna as informacoes de sumarizacao de ligacoes recebidas e estornadas ou expurgadas por fraude.
	 * 
	 *	@return		TotalizacaoPulaPula		totalizacao					Sumarizacao de ligacoes recebidas.
	 */
	public TotalizacaoPulaPula getTotalizacao() 
	{
		return this.totalizacao;
	}
	
	/**
	 *	Retorna a lista de CDR's processados e marcados como estornados ou expurgados.
	 * 
	 *	@return		Collection				listaCdrs					Lista de CDR's de ligacoes recebidas pelo assinante.
	 */
	public Collection getListaCdrs()
	{
		return this.listaCdrs;
	}
	
	//Setters.
	
	/**
	 *	Atribui o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito).
	 * 
	 *	@param		double					valorExpurgo				Valor de expurgo.
	 */
	public void setValorExpurgo(double valorExpurgo) 
	{
		this.valorExpurgo = valorExpurgo;
	}
	
	/**
	 *	Atribui o valor expurgado (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) devido
	 *	ao limite de bonus Pula-Pula ultrapassado para o assinante. Quando o assinante ultrapassa o limite de seu 
	 *	Pula-Pula, as suas ligacoes fraudulentas devem ser expurgadas ate que seja atingido o limite da promocao.
	 *	A partir deste ponto as ligacoes devem ser estornadas, uma vez que nao ha mais ligacoes legitimas que
	 *	"compensem" as ligacoes fraudulentas.
	 * 
	 *	@param		double					valorExpurgoSaturado		Valor expurgado por limite ultrapassado.
	 */
	public void setValorExpurgoSaturado(double valorExpurgoSaturado) 
	{
		this.valorExpurgoSaturado = valorExpurgoSaturado;
	}
	
	/**
	 *	Atribui o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) com 
	 *	deducao do valor no registro agendado na fila de recargas devido a concessao default.
	 * 
	 *	@param		double					valorExpurgoAgendadoDefault	Valor de expurgo default agendado na fila de recargas.
	 */
	public void setValorExpurgoAgendadoDefault(double valorExpurgoAgendadoDefault) 
	{
		this.valorExpurgoAgendadoDefault = valorExpurgoAgendadoDefault;
	}
	
	/**
	 *	Atribui o valor de expurgo (cancelamento de Pula-Pula para CDR's sem execucao de ajuste de debito) com 
	 *	deducao do valor no registro agendado na fila de recargas devido a concessao parcial.
	 * 
	 *	@param		double					valorExpurgoAgendadoParcial	Valor de expurgo parcial agendado na fila de recargas.
	 */
	public void setValorExpurgoAgendadoParcial(double valorExpurgoAgendadoParcial) 
	{
		this.valorExpurgoAgendadoParcial = valorExpurgoAgendadoParcial;
	}
	
	/**
	 *	Atribui o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito).
	 * 
	 *	@param		double					valorEstorno				Valor de estorno.
	 */
	public void setValorEstorno(double valorEstorno) 
	{
		this.valorEstorno = valorEstorno;
	}
	
	/**
	 *	Atribui o valor de estorno (cancelamento de Pula-Pula para CDR's com execucao de ajuste de debito) efetivo.
	 * 
	 *	@param		double					valorEstornoEfetivo			Valor de estorno efetivo.
	 */
	public void setValorEstornoEfetivo(double valorEstornoEfetivo) 
	{
		this.valorEstornoEfetivo = valorEstornoEfetivo;
	}
	
	/**
	 *	Atribui as informacoes de sumarizacao de ligacoes recebidas e estornadas ou expurgadas por fraude.
	 * 
	 *	@param		TotalizacaoPulaPula		totalizacao					Sumarizacao de ligacoes recebidas.
	 */
	public void setTotalizacao(TotalizacaoPulaPula totalizacao) 
	{
		this.totalizacao = totalizacao;
	}
	
	/**
	 *	Atribui a lista de CDR's processados e marcados como estornados ou expurgados.
	 * 
	 *	@param		Collection				listaCdrs					Lista de CDR's de ligacoes recebidas pelo assinante.
	 */
	public void setListaCdrs(Collection listaCdrs)
	{
		this.listaCdrs = listaCdrs;
	}
	
	//Outros metodos.
	
	/**
	 *	Adiciona o campo com o valor. O metodo NAO adiciona o valor total.
	 *
	 *	@param		int						campo						Campo selecionado.
	 *	@param		double					value						Valor a ser adicionado.
	 */
	public void add(int campo, double value)
	{
	    switch(campo)
	    {
	    	case CalculoEstornoPulaPula.EXPURGO:
	    	{
	    	    this.valorExpurgo += value;
	    	    break;
	    	}
	    	case CalculoEstornoPulaPula.EXPURGO_SATURADO:
	    	{
	    	    this.valorExpurgoSaturado += value;
	    	    break;
	    	}
	    	case CalculoEstornoPulaPula.EXPURGO_AGENDADO_DEFAULT:
	    	{
	    	    this.valorExpurgoAgendadoDefault += value;
	    	    break;
	    	}
	    	case CalculoEstornoPulaPula.EXPURGO_AGENDADO_PARCIAL:
	    	{
	    	    this.valorExpurgoAgendadoParcial += value;
	    	    break;
	    	}
	    	case CalculoEstornoPulaPula.ESTORNO:
	    	{
	    	    this.valorEstorno += value;
	    	    break;
	    	}
	    	case CalculoEstornoPulaPula.ESTORNO_EFETIVO:
	    	{
	    	    this.valorEstornoEfetivo += value;
	    	    break;
	    	}
	    	default: break;
	    }
	}

}
