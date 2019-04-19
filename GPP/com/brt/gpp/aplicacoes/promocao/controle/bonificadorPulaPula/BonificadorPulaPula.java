package com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula;

import java.util.Date;

import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Detalhe;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;

/**
 *	Classe base responsavel pelo calculo das bonificacoes da promocao Pula-Pula.
 *
 *	@version	1.0		
 *	@date		18/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public abstract class BonificadorPulaPula 
{

	/**
	 *	Tipo de bonificacao associada ao bonificador.
	 */
	private PromocaoTipoBonificacao tipoBonificacao;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao associada ao bonificador.
	 */
	public BonificadorPulaPula(PromocaoTipoBonificacao tipoBonificacao)
	{
		this.tipoBonificacao = tipoBonificacao;
	}
	
	/**
	 *	Retorna o tipo de bonificacao associada ao bonificador.
	 *
	 *	@return		Tipo de bonificacao associada ao bonificador.
	 */
	protected PromocaoTipoBonificacao getTipoBonificacao()
	{
		return this.tipoBonificacao;
	}
	
	/**
	 *	Calcula e retorna o valor de bonus Pula-Pula.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.		
	 *	@param		dataReferencia			Data de recebimento das ligacoes. Corresponde ao mes anterior a data de 
	 *										concessao do bonus Pula-Pula.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@return		Informacoes da bonificacao que faz parte do bonus Pula-Pula do assinante.
	 *	@throws		Exception
	 */
	public abstract BonificacaoPulaPula calcularBonificacao(AssinantePulaPula	pAssinante,
															Date				dataReferencia,
															boolean				isConsultaCheia) throws Exception;
	
    /**
     *	Calcula o saldo de bonus Pula-Pula para a ligacao recebida.
     *
     *	@param		detalhe					Informacoes da ligacao recebida.
     *	@param		bonusCn					Bonus Pula-Pula por minuto do Codigo Nacional.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@return		Valor do bonus Pula-Pula para a ligacao.
     */
    public static double calcularBonusPulaPula(Detalhe detalhe, BonusPulaPula bonusCn, boolean isConsultaCheia)
    {
        if((detalhe != null) && (bonusCn != null))
        {
        	double bonusMinuto = 0.0;
        	
        	if(!isConsultaCheia)
	        	switch(detalhe.getDesconto().getIdDesconto())
	        	{
			    	case DescontoPulaPula.NORMAL:
			    	case DescontoPulaPula.MOVEL_OFFNET:
			    	case DescontoPulaPula.ESTORNO:
						bonusMinuto = (bonusCn.getVlrBonusMinuto() != null) ? bonusCn.getVlrBonusMinuto().doubleValue() : 0.0;
						return (detalhe.getDuracao()*bonusMinuto)/60;
			    	case DescontoPulaPula.ATH:
	        			bonusMinuto = (bonusCn.getVlrBonusMinutoFF() != null) ? bonusCn.getVlrBonusMinutoFF().doubleValue() : 0.0;
	                    return (detalhe.getDuracao()*bonusMinuto)/60;
			    	case DescontoPulaPula.NOTURNO:
	        			bonusMinuto = (bonusCn.getVlrBonusMinutoNoturno() != null) ? bonusCn.getVlrBonusMinutoNoturno().doubleValue() : 0.0;
	                    return (detalhe.getDuracao()*bonusMinuto)/60;
	        		case DescontoPulaPula.DIURNO:
	        			bonusMinuto = (bonusCn.getVlrBonusMinutoDiurno() != null) ? bonusCn.getVlrBonusMinutoDiurno().doubleValue() : 0.0;
	                    return (detalhe.getDuracao()*bonusMinuto)/60;
	        		case DescontoPulaPula.ATH2:
	        			bonusMinuto = (bonusCn.getVlrBonusMinutoATH() != null) ? bonusCn.getVlrBonusMinutoATH().doubleValue() : 0.0;
	                    return ((detalhe.getDuracao()*bonusMinuto))/60;
                    case DescontoPulaPula.CT:
                        bonusMinuto = (bonusCn.getVlrBonusMinutoCT() != null) ? bonusCn.getVlrBonusMinutoCT().doubleValue() : 0.0;
                        return (detalhe.getDuracao()*bonusMinuto)/60;
                    default: return 0.0;
	        	}

    		bonusMinuto = (bonusCn.getVlrBonusMinuto() != null) ? bonusCn.getVlrBonusMinuto().doubleValue() : 0.0;
			return (detalhe.getDuracao()*bonusMinuto)/60;
        }
        
        return 0.0;
    }
    
    /**
     *	Calcula o saldo de bonus Pula-Pula para a ligacao recebida.
     *
     *	@param		cdr						Informacoes da ligacao recebida.
     *	@param		bonusCn					Bonus Pula-Pula por minuto do Codigo Nacional.
     *	@return		Valor do bonus Pula-Pula para a ligacao.
     */
    public static double calcularBonusPulaPula(ArquivoCDR cdr, BonusPulaPula bonusCn)
    {
        if((cdr != null) && (bonusCn != null))
        {
        	double bonusMinuto = 0.0;
        	
        	switch(new Long(cdr.getFfDiscount()).intValue())
        	{
		    	case DescontoPulaPula.NORMAL:
		    	case DescontoPulaPula.MOVEL_OFFNET:
		    	case DescontoPulaPula.ESTORNO:
					bonusMinuto = (bonusCn.getVlrBonusMinuto() != null) ? bonusCn.getVlrBonusMinuto().doubleValue() : 0.0;
					return (cdr.getCallDuration()*bonusMinuto)/60;
        		case DescontoPulaPula.ATH:
        			bonusMinuto = (bonusCn.getVlrBonusMinutoFF() != null) ? bonusCn.getVlrBonusMinutoFF().doubleValue() : 0.0;
                    return (cdr.getCallDuration()*bonusMinuto)/60;
        		case DescontoPulaPula.NOTURNO:
        			bonusMinuto = (bonusCn.getVlrBonusMinutoNoturno() != null) ? bonusCn.getVlrBonusMinutoNoturno().doubleValue() : 0.0;
                    return (cdr.getCallDuration()*bonusMinuto)/60;
        		case DescontoPulaPula.DIURNO:
        			bonusMinuto = (bonusCn.getVlrBonusMinutoDiurno() != null) ? bonusCn.getVlrBonusMinutoDiurno().doubleValue() : 0.0;
                    return (cdr.getCallDuration()*bonusMinuto)/60;
        		case DescontoPulaPula.ATH2:
        			bonusMinuto = (bonusCn.getVlrBonusMinutoATH() != null) ? bonusCn.getVlrBonusMinutoATH().doubleValue() : 0.0;
                    return (cdr.getCallDuration()*bonusMinuto)/60;
                case DescontoPulaPula.CT:
                    bonusMinuto = (bonusCn.getVlrBonusMinutoCT() != null) ? bonusCn.getVlrBonusMinutoCT().doubleValue() : 0.0;
                    return (cdr.getCallDuration()*bonusMinuto)/60;
                default: return 0.0;
        	}
        }
        
        return 0.0;
    }
    
    /**
     *	Calcula a duracao em segundos da chamada da ligacao dado o bonus do Codigo Nacional e o valor recebido.
     *
     *	@param		detalhe					Informacoes da ligacao recebida.
     *	@param		bonusCn					Bonus Pula-Pula por minuto do Codigo Nacional.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@return		Duracao da ligacao recebida.
     */
    public static long calcularDuracao(Detalhe detalhe, BonusPulaPula bonusCn, boolean isConsultaCheia)
    {
        if((detalhe != null) && (bonusCn != null))
        {
        	double	valorBonus	= detalhe.getBonus();
        	double	bonusMinuto	= 0.0;
        	
        	if(!isConsultaCheia)
        	{
        		switch(detalhe.getDesconto().getIdDesconto())
        		{
        			case DescontoPulaPula.NORMAL:
        			case DescontoPulaPula.ESTORNO:
        			case DescontoPulaPula.MOVEL_OFFNET:
        				bonusMinuto = (bonusCn.getVlrBonusMinuto() != null) ? bonusCn.getVlrBonusMinuto().doubleValue() : 0.0;
        				return new Double((valorBonus*60)/bonusMinuto).longValue();
            		case DescontoPulaPula.ATH:
            			bonusMinuto = (bonusCn.getVlrBonusMinutoFF() != null) ? bonusCn.getVlrBonusMinutoFF().doubleValue() : 0.0;
    	                return new Double((valorBonus*60)/bonusMinuto).longValue();
            		case DescontoPulaPula.NOTURNO:
            			bonusMinuto = (bonusCn.getVlrBonusMinutoNoturno() != null) ? bonusCn.getVlrBonusMinutoNoturno().doubleValue() : 0.0;
    	                return new Double((valorBonus*60)/bonusMinuto).longValue();
            		case DescontoPulaPula.DIURNO:
            			bonusMinuto = (bonusCn.getVlrBonusMinutoDiurno() != null) ? bonusCn.getVlrBonusMinutoDiurno().doubleValue() : 0.0;
    	                return new Double((valorBonus*60)/bonusMinuto).longValue();
            		case DescontoPulaPula.ATH2:
            			bonusMinuto = (bonusCn.getVlrBonusMinutoATH() != null) ? bonusCn.getVlrBonusMinutoATH().doubleValue() : 0.0;
    	                return new Double((valorBonus*60)/bonusMinuto).longValue();
                    case DescontoPulaPula.CT:
                        bonusMinuto = (bonusCn.getVlrBonusMinutoCT() != null) ? bonusCn.getVlrBonusMinutoCT().doubleValue() : 0.0;
    	                return new Double((valorBonus*60)/bonusMinuto).longValue();
                    default: return 0;
        		}
        	}
        	
    		bonusMinuto = bonusCn.getVlrBonusMinuto().doubleValue();
            return new Double((valorBonus*60)/bonusMinuto).longValue();
        }
        
        return 0;
    }
    
}
