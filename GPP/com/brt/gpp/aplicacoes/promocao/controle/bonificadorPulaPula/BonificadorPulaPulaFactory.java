package com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.PulaPulaUnico;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoBonificacao;

/**
 *	Factory de objetos responsaveis pelo calculo das bonificacoes da promocao Pula-Pula.
 *
 *	@version	1.0		
 *	@date		18/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public abstract class BonificadorPulaPulaFactory 
{

	/**
	 *	Cria e retorna uma lista de objetos BonificadorPulaPula, responsaveis pelo calculo da bonificacao Pula-Pula 
	 *	de acordo com os tipos de bonificacao informados. A ordem dos bonificadores e determinada pela ordem dos 
	 *	tipos de bonificacao obtidos pelo iterator.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao da promocao.
	 *	@return		Objeto responsavel pelo calculo da bonificacao Pula-Pula.
	 *	@throws		IllegalArgumentException Caso o tipo de bonificacao ainda nao esteja definido.
	 */
	public static Collection newBonificadoresPulaPula(Collection tiposBonificacao)
	{
		ArrayList result = new ArrayList();
		
		for(Iterator iterator = tiposBonificacao.iterator(); iterator.hasNext();)
			result.add(BonificadorPulaPulaFactory.newBonificadorPulaPula((PromocaoTipoBonificacao)iterator.next()));
		
		return result;
	}
	
	/**
	 *	Cria e retorna objeto BonificadorPulaPula responsavel pelo calculo da bonificacao Pula-Pula de acordo com o 
	 *	tipo de bonificacao informado.
	 *
	 *	@param		tipoBonificacao			Tipo de bonificacao da promocao.
	 *	@return		Objeto responsavel pelo calculo da bonificacao Pula-Pula.
	 *	@throws		IllegalArgumentException Caso o tipo de bonificacao ainda nao esteja definido.
	 */
	public static BonificadorPulaPula newBonificadorPulaPula(PromocaoTipoBonificacao tipoBonificacao)
	{
		switch(tipoBonificacao.getIdTipoBonificacao())
		{
			case PromocaoTipoBonificacao.PULA_PULA_UNICO:
				return new PulaPulaUnico(tipoBonificacao);
			case PromocaoTipoBonificacao.PULA_PULA_RECARGA:
				return new PulaPulaRecarga(tipoBonificacao);
			case PromocaoTipoBonificacao.PULA_PULA_ONNET:
				return new PulaPulaOnNet(tipoBonificacao);
			case PromocaoTipoBonificacao.PULA_PULA_OFFNET:
				return new PulaPulaOffNet(tipoBonificacao);
			default: throw new IllegalArgumentException("Tipo de bonificacao nao definido: " + tipoBonificacao);
		}
	}
	
}
