package com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe base responsavel pelo calculo do bonus Pula-Pula a partir das ligacoes off-net recebidas.
 *
 *	@version	1.0		
 *	@date		20/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public class PulaPulaOffNet extends BonificadorPulaPula 
{

	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao associada ao bonificador.
	 */
	public PulaPulaOffNet(PromocaoTipoBonificacao tipoBonificacao)
	{
		super(tipoBonificacao);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula#calcularBonificacao(AssinantePulaPula, java.util.Date, boolean)
	 */
	public BonificacaoPulaPula calcularBonificacao(AssinantePulaPula	pAssinante, 
												   Date					dataReferencia,
												   boolean				isConsultaCheia)
	{
		BonificacaoPulaPula result = new BonificacaoPulaPula(super.getTipoBonificacao());		
        
		//Configurando a bonificacao para que seja creditada no Saldo Periodico.
		result.setTipoSaldo(MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.PERIODICO));
		
        //Calculando o valor bruto.
        result.setValorBruto(this.calcularValorBruto(pAssinante));
        
        //Calculando o limite da promocao.
        if(!isConsultaCheia)
            result.setLimite(this.calcularLimite(pAssinante, dataReferencia));
        
		return result;
	}
	
	/**
	 *	Calcula o valor de bonus bruto, sem a aplicacao de limite.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta sem a aplicacao de descontos.
	 */
	private double calcularValorBruto(AssinantePulaPula pAssinante)
	{
        BonusPulaPula		bonusCn		= pAssinante.getBonusCn();
        TotalizacaoPulaPula	totalizacao	= pAssinante.getTotalizacao();
        
        if((totalizacao != null) && (bonusCn != null))
        	return (totalizacao.getNumSegundosMovelNaoBrt().doubleValue()*bonusCn.getVlrBonusMinuto().doubleValue())/60;
        
        return 0.0;
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
        //Calculando o valor em funcao do limite da promocao e se o assinante e isento.
        if((pAssinante.temLimite(super.getTipoBonificacao(), dataReferencia)) && 
           (!pAssinante.isIsentoLimite(super.getTipoBonificacao(), dataReferencia)))
            return pAssinante.getLimite(super.getTipoBonificacao(), dataReferencia).getVlrMaxCreditoBonus();

        return -1.0;
	}
	
}
