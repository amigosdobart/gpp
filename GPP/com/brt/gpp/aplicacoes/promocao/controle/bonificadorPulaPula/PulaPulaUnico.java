package com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteDinamico;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe base responsavel pelo calculo do bonus Pula-Pula com bonificacao unica. Utilizado pelas primeira versoes 
 *	da promocao Pula-Pula.
 *
 *	@version	1.0		
 *	@date		20/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public class PulaPulaUnico extends BonificadorPulaPula 
{

	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao associada ao bonificador.
	 */
	public PulaPulaUnico(PromocaoTipoBonificacao tipoBonificacao)
	{
		super(tipoBonificacao);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula#calcularBonificacao(AssinantePulaPula, Date, boolean)
	 */
	public BonificacaoPulaPula calcularBonificacao(AssinantePulaPula	pAssinante,
												   Date					dataReferencia,
												   boolean				isConsultaCheia)
	{
		BonificacaoPulaPula result = new BonificacaoPulaPula(super.getTipoBonificacao());		
        
		//Configurando a bonificacao para que seja creditada no Saldo de Bonus.
		result.setTipoSaldo(MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.BONUS));
		
        //Calculando o valor bruto.
        result.setValorBruto(this.calcularValorBruto(pAssinante, isConsultaCheia));
        
        //Calculando o limite da promocao.
        if(!isConsultaCheia)
            result.setLimite(this.calcularLimite(pAssinante, dataReferencia));
        
        //Obtendo o valor de concessao parcial recebido pelo assinante. OBS: O metodo tem como premissa que a 
        //data dos bonus ja concedidos estejam coerentes com a data de referencia das ligacoes recebidas. Vide
        //metodo consultaDetalhesPulaPula().
        double valorParcial = 0.0;
        
        Collection bonusConcedidos = pAssinante.getBonusConcedidos(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL);
        for(Iterator iterator = bonusConcedidos.iterator(); iterator.hasNext();)
        	valorParcial += ((Recarga)iterator.next()).getVlrCreditoBonus().doubleValue();
        
        result.setValorParcial(valorParcial);
        
		return result;
	}
	
	/**
	 *	Calcula o valor de bonus bruto, sem a aplicacao de limite.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta sem a aplicacao de descontos.
	 */
	private double calcularValorBruto(AssinantePulaPula pAssinante, boolean isConsultaCheia)
	{
        double				result		= 0.0;
        BonusPulaPula		bonusCn		= pAssinante.getBonusCn();
        TotalizacaoPulaPula	totalizacao	= pAssinante.getTotalizacao();
        
        //Definindo as variaveis necessarias para o calculo. Alteracao de variaveis referentes ao numero de
        //segundos recebidos pelo assinante para double devido a erro intermitente no calculo do bonus (apos 
        //varias execucoes o calculo do bonus diverge para cerca de -30 bilhoes).
        double	bonusMinuto				= 0.0;
        double	bonusMinutoFF			= 0.0;
        double	bonusMinutoNoturno		= 0.0;
        double	bonusMinutoDiurno		= 0.0;
        double	bonusMinutoATH			= 0.0;
        double  bonusMinutoCT           = 0.0;
        
        double	segundosTotal			= 0.0;
        double	segundosFF				= 0.0;
        double	segundosNoturno			= 0.0;
        double	segundosDiurno			= 0.0;
        double	segundosNaoBonificado	= 0.0;
        double	segundosDuracaoExcedida = 0.0;
        double	segundosExpurgoFraude	= 0.0;
        double	segundosTup				= 0.0;
        double	segundosAIgualB			= 0.0;
        double	segundosATH				= 0.0;
        double	segundosFaleGratis		= 0.0;
        double  segundosBonus			= 0.0;
        double  segundosCT              = 0.0;
        
        double	valorNormal				= 0.0;
        double	valorFF					= 0.0;
        double	valorNoturno			= 0.0;
        double	valorDiurno				= 0.0;
        double	valorATH				= 0.0;
        double  valorCT                 = 0.0;
        
        if((totalizacao != null) && (bonusCn != null))
        {
    	   //Calculando o valor bruto.
        	if(!isConsultaCheia)
        	{
	            bonusMinuto				= (bonusCn.getVlrBonusMinuto() 					!= null)	? bonusCn.getVlrBonusMinuto().doubleValue()					: 0.0;
	            bonusMinutoFF			= (bonusCn.getVlrBonusMinutoFF() 				!= null)	? bonusCn.getVlrBonusMinutoFF().doubleValue()				: 0.0;
	            bonusMinutoNoturno		= (bonusCn.getVlrBonusMinutoNoturno()			!= null)	? bonusCn.getVlrBonusMinutoNoturno().doubleValue()			: 0.0;
	            bonusMinutoDiurno		= (bonusCn.getVlrBonusMinutoDiurno()			!= null)	? bonusCn.getVlrBonusMinutoDiurno().doubleValue()			: 0.0;
	            bonusMinutoATH			= (bonusCn.getVlrBonusMinutoATH() 				!= null)	? bonusCn.getVlrBonusMinutoATH().doubleValue()				: 0.0;
                bonusMinutoCT           = (bonusCn.getVlrBonusMinutoCT()                != null)    ? bonusCn.getVlrBonusMinutoCT().doubleValue()               : 0.0;
                
	            segundosTotal			= (totalizacao.getNumSegundosTotal()			!= null)	? totalizacao.getNumSegundosTotal().doubleValue()			: 0.0;
	            segundosFF				= (totalizacao.getNumSegundosFF()				!= null)	? totalizacao.getNumSegundosFF().doubleValue()				: 0.0;
	            segundosNoturno			= (totalizacao.getNumSegundosNoturno()			!= null)	? totalizacao.getNumSegundosNoturno().doubleValue()			: 0.0;
	            segundosDiurno			= (totalizacao.getNumSegundosDiurno()			!= null)	? totalizacao.getNumSegundosDiurno().doubleValue()			: 0.0;
	            segundosNaoBonificado	= (totalizacao.getNumSegundosNaoBonificado()	!= null)	? totalizacao.getNumSegundosNaoBonificado().doubleValue()	: 0.0;
	            segundosDuracaoExcedida	= (totalizacao.getNumSegundosDuracaoExcedida()	!= null)	? totalizacao.getNumSegundosDuracaoExcedida().doubleValue()	: 0.0;
	            segundosExpurgoFraude	= (totalizacao.getNumSegundosExpurgoFraude()	!= null)	? totalizacao.getNumSegundosExpurgoFraude().doubleValue()	: 0.0;
	            segundosTup				= (totalizacao.getNumSegundosTup()				!= null)	? totalizacao.getNumSegundosTup().doubleValue()				: 0.0;
	            segundosAIgualB			= (totalizacao.getNumSegundosAIgualB()			!= null)	? totalizacao.getNumSegundosAIgualB().doubleValue()			: 0.0;
	            segundosATH				= (totalizacao.getNumSegundosATH()				!= null)	? totalizacao.getNumSegundosATH().doubleValue()				: 0.0;
	            segundosFaleGratis		= (totalizacao.getNumSegundosFaleGratis()		!= null)	? totalizacao.getNumSegundosFaleGratis().doubleValue()		: 0.0;
	            segundosBonus			= (totalizacao.getNumSegundosBonus()			!= null)	? totalizacao.getNumSegundosBonus().doubleValue()			: 0.0;
                segundosCT              = (totalizacao.getNumSegundosCT()               != null)    ? totalizacao.getNumSegundosCT().doubleValue()              : 0.0;
                
	            valorNormal		= 	(segundosTotal				- 
	                    			 segundosFF					- 
	                    			 segundosNoturno			- 
	                    			 segundosDiurno				- 
	                    			 segundosNaoBonificado		- 
	                    			 segundosDuracaoExcedida	- 
	                    			 segundosExpurgoFraude		- 
	                    			 segundosTup				- 
	                    			 segundosAIgualB			-
	                    			 segundosATH				-
	                    			 segundosFaleGratis			-
	                    			 segundosBonus              -
                                     segundosCT) * bonusMinuto;
	            valorFF			= segundosFF*bonusMinutoFF;
	            valorNoturno	= segundosNoturno*bonusMinutoNoturno;
	            valorDiurno		= segundosDiurno*bonusMinutoDiurno;
	            valorATH		= segundosATH*bonusMinutoATH;
                valorCT         = segundosCT*bonusMinutoCT;
	            
	            result = valorNormal + valorFF + valorNoturno + valorDiurno + valorATH + valorCT;
	            result /= 60;
        	}
        	else
        	{
        		bonusMinuto		= (bonusCn.getVlrBonusMinuto() 			!= null)	? bonusCn.getVlrBonusMinuto().doubleValue() 		: 0.0;
        		segundosTotal	= (totalizacao.getNumSegundosTotal()	!= null)	? totalizacao.getNumSegundosTotal().doubleValue()	: 0.0;
	            
        		result  = (segundosTotal)*bonusMinuto;
        		result /= 60;
        	}
        }

        return result;
	}
	
	/**
	 *	Calcula o limite do bonus a ser concedido ao assinante.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de recebimento das ligacoes. Corresponde ao mes anterior a data de 
	 *										concessao do bonus Pula-Pula.
	 */
	private double calcularLimite(AssinantePulaPula pAssinante, Date dataReferencia)
	{
        double result = -1.0;
        
        //Calculando o valor em funcao do limite da promocao e se o assinante e isento.
        if((pAssinante.temLimite(super.getTipoBonificacao(), dataReferencia)) && 
           (!pAssinante.isIsentoLimite(super.getTipoBonificacao(), dataReferencia)))
        {
            result = pAssinante.getLimite(super.getTipoBonificacao(), dataReferencia).getVlrMaxCreditoBonus();
            
            //Se o limite for dinamico, calcular o limite da promocao do assinante.
            if(pAssinante.temLimiteDinamico(dataReferencia))
            {
                PromocaoLimiteDinamico limiteDinamico = pAssinante.getLimiteDinamico(dataReferencia);
                double vlrRecargas = (pAssinante.getTotalRecargas() != null) ? 
                	pAssinante.getTotalRecargas().getVlrPago() : 0.0;
                    
                //Se o total de recargas ultrapassar o teto do calculo do limite, nivelar de acordo com o teto.
                if(vlrRecargas > limiteDinamico.getVlrThresholdSuperior())
                    vlrRecargas = limiteDinamico.getVlrThresholdSuperior();

                //Se o total de recargas efetuadas pelo assinante for maior que o piso do calculo do limite, 
                //adicionar o total de recargas ao limite.
                if (vlrRecargas >= limiteDinamico.getVlrThresholdInferior())
                    result += vlrRecargas;
            }
        }

        return result;
	}
	
}
