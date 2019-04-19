package com.brt.gpp.aplicacoes.promocao.entidade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe que representa a entidade da tabela TBL_PRO_LIMITE_PROMOCAO, contendo as informacoes referente ao limite
 *	de bonus das promocoes, assim como suas vigencias.
 * 
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		01/06/2006
 *	@modify		Primeira versao.
 *
 *	@version	2.0
 *	@author		Daniel Ferreira
 *	@date		18/03/2008
 *	@modify		Inclusao de multiplas bonificacoes, onde cada uma possui um limite.
 */
public class PromocaoLimite
{

	/**
	 *	Constante para data inicial de vigencia.
	 */
	public static final int DAT_INI_PERIODO = 0;
	
	/**
	 *	Constante para data final de vigencia.
	 */
	public static final int DAT_FIM_PERIODO = 1;
	
	/**
	 *	Constante para valor do limite.
	 */
	public static final int VLR_MAX_CREDITO_BONUS = 2;
	
	/**
	 *	Informacoes da promocao.
	 */
	private Promocao promocao;
	
	/**
	 *	Tipo de bonificacao ao qual o limite e aplicavel.
	 */
	private PromocaoTipoBonificacao tipoBonificacao;
	
	/**
	 *	Data inicial de vigencia.
	 */
	private Date datIniPeriodo;
	
	/**
	 *	Data final de vigencia.
	 */
	private Date datFimPeriodo;
	
	/**
	 *	Valor do limite.
	 */	
	private double vlrMaxCreditoBonus;
	
	/**
	 *	Indicador de permissao de isencao de limite.
	 */
	private boolean indPermiteIsencaoLimite;
	
	/**
	 *	Formatador de valor.
	 */
	private DecimalFormat conversorDouble;
	
	/**
	 *	Formatador de data.
	 */
	private SimpleDateFormat conversorDate;
	
	/**
	 *	Construtor da classe.
	 */
	public PromocaoLimite()
	{
		this.promocao					= null;
		this.tipoBonificacao			= null;
		this.datIniPeriodo				= null;
		this.datFimPeriodo				= null;
		this.vlrMaxCreditoBonus			= 0.0;
		this.indPermiteIsencaoLimite	= false;
		this.conversorDouble			= new DecimalFormat(Definicoes.MASCARA_DOUBLE, 
                											new DecimalFormatSymbols(new Locale("pt", "BR")));
		this.conversorDate				= new SimpleDateFormat(Definicoes.MASCARA_DATE);
	}
	
	/**
	 *	Retorna as informacoes da promocao.
	 * 
	 *	@return		Informacoes da promocao.
	 */
	public Promocao getPromocao() 
	{
		return this.promocao;
	}
	
	/**
	 *	Retorna o tipo de bonificacao ao qual o limite e aplicavel.
	 *
	 *	@return		Tipo de bonificacao ao qual o limite e aplicavel.
	 */
	public PromocaoTipoBonificacao getTipoBonificacao()
	{
		return this.tipoBonificacao;
	}
	
	/**
	 *	Retorna a data inicial de vigencia.
	 * 
	 *	@return		Data inicial de vigencia.
	 */
	public Date getDatIniPeriodo() 
	{
		return this.datIniPeriodo;
	}
	
	/**
	 *	Retorna a data final de vigencia.
	 * 
	 *	@return		Data final de vigencia.
	 */
	public Date getDatFimPeriodo() 
	{
		return this.datFimPeriodo;
	}
	
	/**
	 *	Retorna o valor do limite.
	 * 
	 *	@return		Valor do limite.
	 */
	public double getVlrMaxCreditoBonus()
	{
		return this.vlrMaxCreditoBonus;
	}
	
	/**
	 *	Indica a permissao da promocao para isencao de limite. Se este flag nao estiver ativado, o campo 
	 *	IND_ISENTO_LIMITE da TBL_PRO_ASSINANTE nao tem efeito.
	 *	
	 *	@return		True se a promocao permite isencao de limite e false caso contrario.
	 */
	public boolean permiteIsencaoLimite()
	{
		return this.indPermiteIsencaoLimite;
	}
	
	/**
	 *	Atribui as informacoes da promocao.
	 * 
	 *	@param		promocao				Informacoes da promocao.
	 */
	public void setPromocao(Promocao promocao) 
	{
		this.promocao = promocao;
	}
	
	/**
	 *	Atribui o tipo de bonificacao ao qual o limite e aplicavel.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao ao qual o limite e aplicavel.
	 */
	public void setTipoBonificacao(PromocaoTipoBonificacao tipoBonificacao)
	{
		this.tipoBonificacao = tipoBonificacao;
	}
	
	/**
	 *	Atribui a data inicial de vigencia.
	 * 
	 *	@param		datIniPeriodo			Data inicial de vigencia.
	 */
	public void setDatIniPeriodo(Date datIniPeriodo) 
	{
		this.datIniPeriodo = datIniPeriodo;
	}
	
	/**
	 *	Atribui a data final de vigencia.
	 * 
	 *	@param		datFimPeriodo			Data final de vigencia.
	 */
	public void setDatFimPeriodo(Date datFimPeriodo) 
	{
		this.datFimPeriodo = datFimPeriodo;
	}
	
	/**
	 *	Atribui o valor do limite.
	 * 
	 *	@return		vlrMaxCreditoBonus		Valor do limite.
	 */
	public void setVlrMaxCreditoBonus(double vlrMaxCreditoBonus)
	{
		this.vlrMaxCreditoBonus = vlrMaxCreditoBonus;
	}
	
	/**
	 *	Atribui o indicador de permissao da promocao para isencao de limite. Se este flag nao estiver ativado, o 
	 *	campo IND_ISENTO_LIMITE da TBL_PRO_ASSINANTE nao tem efeito.
	 * 
	 *	@param		indPermiteIsencaoLimite	Indicador de permissao de isencao de limite.
	 */
	public void setIndPermiteIsencaoLimite(boolean indPermiteIsencaoLimite) 
	{
		this.indPermiteIsencaoLimite = indPermiteIsencaoLimite;
	}
	
	/**
	 *	Retorna uma representacao em formato String do atributo.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Representacao em formato String do atributo.
	 */
	public String toString(int campo)
	{
	    switch(campo)
	    {
	    	case PromocaoLimite.DAT_INI_PERIODO:
	    	    return (this.datIniPeriodo != null) ? this.conversorDate.format(this.datIniPeriodo) : null;
	    	case PromocaoLimite.DAT_FIM_PERIODO:
	    	    return (this.datFimPeriodo != null) ? this.conversorDate.format(this.datFimPeriodo) : null;
	    	case PromocaoLimite.VLR_MAX_CREDITO_BONUS:
	    	    return String.valueOf(this.vlrMaxCreditoBonus);
	    	default: 
	    	    return null;
	    }
	}
	
	/**
	 *	Formata o valor do atributo de forma legivel. Se o valor for NULL, retorna NULL.
	 * 
	 *	@param		campo					Campo selecionado. Se o campo for invalido, retorna NULL.
	 *	@return		Valor formatado.
	 */
    public String format(int campo)
    {
	    switch(campo)
	    {
	    	case PromocaoLimite.DAT_INI_PERIODO:
	    	    return (this.datIniPeriodo != null) ? this.conversorDate.format(this.datIniPeriodo) : null;
	    	case PromocaoLimite.DAT_FIM_PERIODO:
	    	    return (this.datFimPeriodo != null) ? this.conversorDate.format(this.datFimPeriodo) : null;
	    	case PromocaoLimite.VLR_MAX_CREDITO_BONUS:
	    	    return this.conversorDouble.format(this.vlrMaxCreditoBonus);
	    	default: 
	    	    return null;
	    }
    }
	
	/**
	 *	Indica se o registro esta vigente ou nao.
	 * 
	 *	@return		True se esta vigente e false caso contrario.
	 */
    public boolean isVigente(Date datProcessamento)
    {
        if(datProcessamento == null)
            return false;
        
        if(this.datIniPeriodo == null)
            return false;
        
        if(this.datIniPeriodo.compareTo(datProcessamento) > 0)
            return false;
        
        if(this.datFimPeriodo == null)
            return true;
        
        return (this.datFimPeriodo.compareTo(datProcessamento) >= 0);
    }
    
	/**
	 *	@see		java.lang.Object#equals(Object)
	 */
    public boolean equals(Object obj)
    {
    	if(obj == null)
    		return false;
    	
    	return (this.hashCode() == obj.hashCode());
    }
    
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
    public int hashCode()
    {
    	return (super.getClass().getName().hashCode() + "||" +
    			this.getPromocao().hashCode() + "||" +
    			this.getTipoBonificacao().hashCode()).hashCode();
    }
    
}
