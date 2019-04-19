package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe que representa as informacoes referentes ao valor de bonus de um certo tipo de execucao e bonificacao da 
 *	Promocao Pula-Pula a ser concedido ao assinante.
 * 
 *	@version	1.0		
 *	@date		17/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public class BonificacaoPulaPula
{

	/**
	 *	Constante referente ao valor total bruto de bonus associado ao tipo de bonificacao.
	 */
	public static final int VALOR_BRUTO = 0;
	
	/**
	 *	Constante referente ao valor total de bonus associado ao tipo de bonificacao.
	 */
	public static final int VALOR_TOTAL = 1;
	
	/**
	 *	Constante referente ao valor de bonus associado ao tipo de bonificacao ja concedido ao assinante.
	 */
	public static final int VALOR_PARCIAL = 2;
	
	/**
	 *	Constante referente ao valor de bonus associado ao tipo de bonificacao que o assinante ainda deve receber.
	 */
	public static final int VALOR_A_RECEBER = 3;
	
	/**
	 *	Constante referente ao limite do bonus associado ao tipo de bonificacao.
	 */
	public static final int	LIMITE = 4;
	
	/**
	 *	Informacoes referentes ao tipo de bonificacao associado ao bonus.
	 */
	private PromocaoTipoBonificacao tipoBonificacao;
	
	/**
	 *	Saldo onde deve ser creditado o bonus.
	 */
	private TipoSaldo tipoSaldo;
	
	/**
	 *	Valor bruto de bonus associado ao tipo de bonificacao a ser concedido ao assinante.
	 */
	private double valorBruto;
	
	/**
	 *	Valor de bonus associado ao tipo de bonificacao ja concedido ao assinante.
	 */
	private double valorParcial;
	
	/**
	 *	Constante referente ao limite do bonus associado ao tipo de bonificacao.
	 */
	private double limite;
	
	/**
	 *	Formatador de valores utilizado internamente.
	 */
	private	DecimalFormat conversorDouble;
	
	/**
	 *	Construtor da classe.
	 */
	public BonificacaoPulaPula()
	{
		this.tipoBonificacao	= null;
		this.tipoSaldo			= null;
		this.valorBruto			= 0.0;
		this.valorParcial		= 0.0;
		this.limite				= -1.0;
		this.conversorDouble	= new DecimalFormat(Definicoes.MASCARA_DOUBLE, 
													new DecimalFormatSymbols(new Locale("pt", "BR", "")));
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao ao qual a bonificacao Pula-Pula se refere.
	 */
	public BonificacaoPulaPula(PromocaoTipoBonificacao tipoBonificacao)
	{
		this();
		
		this.setTipoBonificacao(tipoBonificacao);
	}
	
	/**
	 *	Retorna as informacoes referentes ao tipo de bonificacao associado ao bonus.
	 *
	 *	@return		Informacoes referentes ao tipo de bonificacao associado ao bonus.
	 */
	public PromocaoTipoBonificacao getTipoBonificacao()
	{
		return this.tipoBonificacao;
	}
	
	/**
	 *	Retorna o saldo onde deve ser creditado o bonus.
	 *
	 *	@return		Saldo onde deve ser creditado o bonus.
	 */
	public TipoSaldo getTipoSaldo()
	{
		return this.tipoSaldo;
	}
	
	/**
	 *	Retorna o valor bruto de bonus Pula-Pula, equivalente aos minutos de ligacoes que o assinante recebeu.
	 * 
	 *	@return		Valor bruto de bonus Pula-Pula.
	 */
	public double getValorBruto() 
	{
		return this.valorBruto;
	}
	
	/**
	 *	Retorna o valor de concessao de bonus Pula-Pula para o assinante.
	 * 
	 *	@return		Valor de concessao de bonus Pula-Pula.
	 */
	public double getValorTotal() 
	{
		double	valorBruto	= this.getValorBruto();
		double	limite		= this.getLimite();
		
		return ((limite != -1) && (valorBruto > limite)) ? limite : valorBruto;
	}
	
	/**
	 *	Retorna o valor de concessao de bonus Pula-Pula parcial concedido ao assinante.
	 * 
	 *	@return		Valor de concessao parcial de bonus Pula-Pula.
	 */
	public double getValorParcial() 
	{
		return this.valorParcial;
	}
	
	/**
	 *	Retorna o valor de concessao de bonus Pula-Pula restante a ser concedido ao assinante.
	 * 
	 *	@return		Valor de concessao de bonus Pula-Pula restante.
	 */
	public double getValorAReceber() 
	{
		double result = this.getValorTotal() - this.getValorParcial();
		
		return (result >= 0.0) ? result : 0.0;
	}
	
	/**
	 *	Retorna o limite da promocao Pula-Pula calculado para o assinante.
	 * 
	 *	@return		Limite calculado para o assinante.
	 */
	public double getLimite() 
	{
		return this.limite;
	}
	
	/**
	 *	Indica se o assinante ultrapassou o limite do bonus associado ao tipo de bonificacao da promocao.
	 * 
	 *	@return		True se o assinante ultrapassou o limite e false caso contrario.
	 */
	public boolean isLimiteUltrapassado()
	{
		return ((this.getLimite() != -1) && (this.getValorTotal() >= this.getLimite()));
	}
	
	/**
	 *	Atribui as informacoes referentes ao tipo de bonificacao associado ao bonus.
	 *
	 *	@param		tipoBonificacao			Informacoes referentes ao tipo de bonificacao associado ao bonus.
	 */
	public void setTipoBonificacao(PromocaoTipoBonificacao tipoBonificacao)
	{
		this.tipoBonificacao = tipoBonificacao;
	}
	
	/**
	 *	Atribui o saldo onde deve ser creditado o bonus.
	 *
	 *	@param		tipoSaldo				Saldo onde deve ser creditado o bonus.
	 */
	public void setTipoSaldo(TipoSaldo tipoSaldo)
	{
		this.tipoSaldo = tipoSaldo;
	}
	
	/**
	 *	Atribui o valor bruto de bonus Pula-Pula, equivalente aos minutos de ligacoes que o assinante recebeu.
	 * 
	 *	@param		valorBruto				Valor bruto de bonus Pula-Pula.
	 */
	public void setValorBruto(double valorBruto) 
	{
		this.valorBruto = valorBruto;
	}
	
	/**
	 *	Atribui o valor de concessao de bonus Pula-Pula parcial concedido ao assinante.
	 * 
	 *	@param		valorParcial			Valor de concessao parcial de bonus Pula-Pula.
	 */
	public void setValorParcial(double valorParcial) 
	{
		this.valorParcial = valorParcial;
	}
		
	/**
	 *	Atribui o limite da promocao Pula-Pula calculado para o assinante.
	 * 
	 *	@param		limite					Limite calculado para o assinante.
	 */
	public void setLimite(double limite) 
	{
		this.limite = limite;
	}
	
	/**
	 *	Indica se o assinante recebeu o valor parcial de bonus Pula-Pula.
	 * 
	 *	@return		True se o assinante recebeu valor parcial e false caso contrario.
	 */
	public boolean recebeuParcial()
	{
		return (this.valorParcial > 0.0);
	}
	
	/** 
	 *	Converte o valor para String, sem formatacao.
	 *
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor como String, sem formatacao.
	 */
	public String toString(int campo)
	{
	    switch(campo)
	    {
    		case BonificacaoPulaPula.VALOR_BRUTO:
    			return String.valueOf(this.getValorBruto());
	    	case BonificacaoPulaPula.VALOR_TOTAL:
	    	    return String.valueOf(this.getValorTotal());
	    	case BonificacaoPulaPula.VALOR_PARCIAL:
	    	    return String.valueOf(this.getValorParcial());
	    	case BonificacaoPulaPula.VALOR_A_RECEBER:
	    	    return String.valueOf(this.getValorAReceber());
	    	case BonificacaoPulaPula.LIMITE:
	    	    return (this.getLimite() != -1.0) ? String.valueOf(this.getLimite()) : "";
	    	default: return null;
	    }
	}
	
	/**
	 *	Retorna o valor em formato String.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor no formato String.
	 */
    public String format(int campo)
    {
	    switch(campo)
	    {
	    	case BonificacaoPulaPula.VALOR_BRUTO:
	    	    return this.conversorDouble.format(this.getValorBruto());
	    	case BonificacaoPulaPula.VALOR_TOTAL:
	    	    return this.conversorDouble.format(this.getValorTotal());
	    	case BonificacaoPulaPula.VALOR_PARCIAL:
	    	    return this.conversorDouble.format(this.getValorParcial());
	    	case BonificacaoPulaPula.VALOR_A_RECEBER:
	    	    return this.conversorDouble.format(this.getValorAReceber());
	    	case BonificacaoPulaPula.LIMITE:
	    	    return (this.limite != -1.0) ? this.conversorDouble.format(this.getLimite()) : "";
	    	default: return null;
	    }
    }
	
}