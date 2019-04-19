package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe que representa as informacoes referentes ao valor de bonus de um certo tipo de execucao e bonificacao da 
 *	Promocao Pula-Pula a ser concedido ao assinante.
 * 
 *	@version	1.0		
 *	@date		19/09/2005
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 *
 *	@version	1.1		
 *	@date		05/11/2007
 *	@author		Joao Paulo Galvagni
 *  @modify		Inclusao do atributo listaPromocoesPacoteBonus para bonificacoes extras, como os 30 SMS's para o 
 *  			Plano Controle de Natal 2007
 *  
 *  @version	2.0		
 *  @date		17/03/2008
 *  @author		Daniel Ferreira
 *  @modify		Inclusao de multiplas bonificacoes para o mesmo tipo de execucao do Pula-Pula.
 */
public class SaldoPulaPula
{

	/**
	 *	Constante referente ao valor bruto de bonus Pula-Pula.
	 */
	public static final int VALOR_BRUTO = 0;
	
	/**
	 *	Constante referente ao valor total de bonus Pula-Pula a ser concedido ao assinante.
	 */
	public static final int VALOR_TOTAL = 1;
	
	/**
	 *	Constante referente ao valor de bonus Pula-Pula ja concedido ao assinante.
	 */
	public static final int VALOR_PARCIAL = 2;
	
	/**
	 *	Constante referente ao valor de bonus Pula-Pula que o assinante ainda deve receber.
	 */
	public static final int VALOR_A_RECEBER = 3;
	
	/**
	 *	Constante referente ao limite do bonus Pula-Pula.
	 */
	public static final int	LIMITE = 4;
	
	/**
	 *	Lista de bonus a serem concedidos ao assinante, um para cada tipo de bonificacao.
	 */
	private Collection bonificacoes;
	
	/**
	 *	Lista de pacotes de bonus extras.
	 */
	private Collection listaPromocoesPacoteBonus;
	
	/**
	 *	Formatador de valores utilizado internamente.
	 */
	private DecimalFormat conversorDouble;
	
	/**
	 * Construtor da classe
	 */
	public SaldoPulaPula()
	{
		this.bonificacoes				= new ArrayList();
		this.listaPromocoesPacoteBonus	= new ArrayList();
		this.conversorDouble			= new DecimalFormat(Definicoes.MASCARA_DOUBLE, 
															new DecimalFormatSymbols(new Locale("pt", "BR")));
	}
	
	/**
	 *	Retorna a lista de bonus a serem concedidos ao assinante, um para cada tipo de bonificacao.
	 *
	 *	@return		Lista de bonus a serem concedidos ao assinante, um para cada tipo de bonificacao.
	 */
	public Collection getBonificacoes()
	{
		return this.bonificacoes;
	}
	
	/**
	 *	Retorna objeto contendo a bonificacao Pula-Pula do assinante de acordo com o tipo de bonificacao.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao.
	 *	@return		Bonificacao Pula-Pula do assinante de acordo com o tipo de bonificacao.
	 */
	public BonificacaoPulaPula getBonificacao(PromocaoTipoBonificacao tipoBonificacao)
	{
		for(Iterator iterator = this.bonificacoes.iterator(); iterator.hasNext();)
		{
			BonificacaoPulaPula bonificacao = (BonificacaoPulaPula)iterator.next();
			
			if(bonificacao.getTipoBonificacao().equals(tipoBonificacao))
				return bonificacao;
		}
		
		return null;
	}
	
	/**
	 *	Retorna objeto contendo a bonificacao Pula-Pula do assinante aplicavel ao tipo de desconto.
	 *
	 *	@param		desconto				Tipo de desconto Pula-Pula.
	 *	@return		Bonificacao Pula-Pula do assinante aplicavel ao tipo de desconto.
	 */
	public BonificacaoPulaPula getBonificacao(DescontoPulaPula desconto)
	{
		for(Iterator iterator = this.bonificacoes.iterator(); iterator.hasNext();)
		{
			BonificacaoPulaPula result = (BonificacaoPulaPula)iterator.next();
			
			if(desconto.matches(result.getTipoBonificacao()))
				return result;
		}
		
		return null;
	}
	
	/**
	 *	Retorna a lista de pacotes de bonus extras.
	 *
	 *	@return		Lista de pacotes de bonus extras.
	 */
	public Collection getListaPromocoesPacoteBonus()
	{
		return this.listaPromocoesPacoteBonus;
	}
	
	/**
	 *	Retorna o valor bruto de bonus Pula-Pula para o assinante, sendo o resultado do somatorio de todas as 
	 *	bonificacoes.
	 * 
	 *	@return		Valor bruto de bonus Pula-Pula.
	 */
	public double getValorBruto() 
	{
		double result = 0.0;
		
		for(Iterator iterator = this.getBonificacoes().iterator(); iterator.hasNext();)
			result += ((BonificacaoPulaPula)iterator.next()).getValorBruto();
		
		return result;
	}
	
	/**
	 *	Retorna o valor de concessao de bonus Pula-Pula para o assinante, sendo o resultado do somatorio de todas 
	 *	as bonificacoes.
	 * 
	 *	@return		Valor de concessao de bonus Pula-Pula.
	 */
	public double getValorTotal() 
	{
		double result = 0.0;
		
		for(Iterator iterator = this.getBonificacoes().iterator(); iterator.hasNext();)
			result += ((BonificacaoPulaPula)iterator.next()).getValorTotal();
		
		return result;
	}
	
	/**
	 *	Retorna o valor de bonus Pula-Pula ja concedido ao assinante, sendo o resultado do somatorio de todas as 
	 *	bonificacoes.
	 * 
	 *	@return		Valor de bonus Pula-Pula ja concedido.
	 */
	public double getValorParcial() 
	{
		double result = 0.0;
		
		for(Iterator iterator = this.getBonificacoes().iterator(); iterator.hasNext();)
			result += ((BonificacaoPulaPula)iterator.next()).getValorParcial();
		
		return result;
	}
	
	/**
	 *	Retorna o valor de bonus Pula-Pula ainda a ser concedido ao assinante, sendo o resultado do somatorio de 
	 *	todas as bonificacoes.
	 * 
	 *	@return		Valor de bonus Pula-Pula ainda a ser concedido.
	 */
	public double getValorAReceber() 
	{
		double result = 0.0;
		
		for(Iterator iterator = this.getBonificacoes().iterator(); iterator.hasNext();)
			result += ((BonificacaoPulaPula)iterator.next()).getValorAReceber();
		
		return result;
	}
	
	/**
	 *	Retorna o limite total da promocao Pula-Pula calculado para o assinante.
	 * 
	 *	@return		Limite total calculado para o assinante.
	 */
	public double getLimite() 
	{
		double result = -1.0;
		
		for(Iterator iterator = this.getBonificacoes().iterator(); iterator.hasNext();)
		{
			double limite = ((BonificacaoPulaPula)iterator.next()).getLimite();
			
			if(limite != -1.0)
				result = (result != -1.0) ? (result + limite) : limite;
		}
		
		return result;
	}
	
	/**
	 *	Indica se o assinante ultrapassou todos os limites de bonus Pula-Pula.
	 * 
	 *	@return		True se o assinante ultrapassou os limites e false caso contrario.
	 */
	public boolean isLimiteUltrapassado()
	{
		for(Iterator iterator = this.getBonificacoes().iterator(); iterator.hasNext();)
			if(!((BonificacaoPulaPula)iterator.next()).isLimiteUltrapassado())
				return false;
		
		return true;
	}
	
	/**
	 *	Adiciona uma bonificacao a lista de bonificacoes da promocao Pula-Pula do assinante.
	 *
	 *	@param		bonificacao				Bonus a ser concedido ao assinante, associado tipo de bonificacao.
	 */
	public void addBonificacao(BonificacaoPulaPula bonificacao)
	{
		this.bonificacoes.add(bonificacao);
	}
	
	/**
	 *	Atribui a lista de pacotes de bonus extras.
	 *
	 *	@param		listaPromocoesPacoteBonus Lista de pacotes de bonus extras.
	 */
	public void setListaPromocoesPacoteBonus(Collection listaPromocoesPacoteBonus)
	{
		this.listaPromocoesPacoteBonus = listaPromocoesPacoteBonus;
	}
	
	/**
	 *	Adiciona um pacote a lista de pacotes de bonus extras.
	 *
	 *	@param		promoPacoteBonus		Pacote de bonus.
	 *	@return		True se adicionou e false caso contrario.
	 */
	public boolean addListaPromocoesPacoteBonus(PromocaoPacoteBonus promoPacoteBonus)
	{
		return this.listaPromocoesPacoteBonus.add(promoPacoteBonus);
	}
	
	/**
	 *	Indica se o assinante recebeu o valor parcial de bonus Pula-Pula.
	 * 
	 *	@return		True se o assinante recebeu o valor parcial e false caso contrario.
	 */
	public boolean recebeuParcial()
	{
		return (this.getValorParcial() > 0);
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
	    	case SaldoPulaPula.VALOR_BRUTO:
	    	    return String.valueOf(this.getValorBruto());
	    	case SaldoPulaPula.VALOR_TOTAL:
	    	    return String.valueOf(this.getValorTotal());
	    	case SaldoPulaPula.VALOR_PARCIAL:
	    	    return String.valueOf(this.getValorParcial());
	    	case SaldoPulaPula.VALOR_A_RECEBER:
	    	    return String.valueOf(this.getValorAReceber());
	    	case SaldoPulaPula.LIMITE:
	    	    return (this.getLimite() >= 0.0) ? String.valueOf(this.getLimite()) : "";
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
	    	case SaldoPulaPula.VALOR_BRUTO:
	    	    return this.conversorDouble.format(this.getValorBruto());
	    	case SaldoPulaPula.VALOR_TOTAL:
	    	    return this.conversorDouble.format(this.getValorTotal());
	    	case SaldoPulaPula.VALOR_PARCIAL:
	    	    return this.conversorDouble.format(this.getValorParcial());
	    	case SaldoPulaPula.VALOR_A_RECEBER:
	    	    return this.conversorDouble.format(this.getValorAReceber());
	    	case SaldoPulaPula.LIMITE:
	    	    return (this.getLimite() >= -1.0) ? this.conversorDouble.format(this.getLimite()) : "";
	    	default: return null;
	    }
    }
	
}