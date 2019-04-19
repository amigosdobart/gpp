package com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Classe base responsavel pelo calculo do bonus baseado nas recargas efetuadas pela assinante.
 *
 *	@version	1.0		
 *	@date		20/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public class PulaPulaRecarga extends BonificadorPulaPula 
{

	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao associada ao bonificador.
	 */
	public PulaPulaRecarga(PromocaoTipoBonificacao tipoBonificacao)
	{
		super(tipoBonificacao);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula#calcularBonificacao(AssinantePulaPula, java.util.Date, boolean)
	 */
	public BonificacaoPulaPula calcularBonificacao(AssinantePulaPula	pAssinante,
												   Date					dataReferencia,
												   boolean				isConsultaCheia) throws GPPInternalErrorException
	{
		BonificacaoPulaPula result = new BonificacaoPulaPula(super.getTipoBonificacao());		
        
		//Configurando a bonificacao para que seja creditada no Saldo de Bonus.
		result.setTipoSaldo(MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.BONUS));
		
        //Calculando o valor bruto.
        result.setValorBruto(this.calcularValorBruto(pAssinante, isConsultaCheia));

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
	 *	@throws		GPPInternalErrorException
	 */
	private double calcularValorBruto(AssinantePulaPula pAssinante, boolean isConsultaCheia) throws GPPInternalErrorException
	{
		double valorPago = (pAssinante.getTotalRecargas() != null) ?
								pAssinante.getTotalRecargas().getVlrPago() : 0.0;
		
		double multiplicador = 
			Double.parseDouble(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("PULA_PULA_MULTIPLICADOR_RECARGA"));

		return valorPago*multiplicador;
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
        if((pAssinante.temLimite(super.getTipoBonificacao(), dataReferencia)) && 
           (!pAssinante.isIsentoLimite(super.getTipoBonificacao(), dataReferencia)))
        	return pAssinante.getLimite(super.getTipoBonificacao(), dataReferencia).getVlrMaxCreditoBonus();

        return -1.0;
	}
	
}
