package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPulaFactory;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Classe responsavel pela consulta do saldo da promocao Pula-Pula do assinante.
 *
 *	@version	1.0		
 *	@date		19/03/2008
 *	@author		Daniel Ferreira
 *	@modify		Primeira versao.
 */
public class ConsultorSaldoPulaPula extends ConsultorDetalhePulaPula
{

	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorSaldoPulaPula(long idProcesso)
	{
		super("ConsultorSaldoPulaPula", idProcesso);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula#consultarDetalhePulaPula(AssinantePulaPula, java.util.Date, PREPConexao)
	 */
	public void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia,
										 boolean			isConsultaCheia,
										 PREPConexao		conexaoPrep) throws Exception
	{
		SaldoPulaPula saldo = new SaldoPulaPula();
		
		//Para o calculo da bonificacao Pula-Pula, e necessario informar a data de referencia para obtencao dos 
		//limites. Esta data corresponde ao mes anterior da concessao, ou seja, o mes de analise.
		Calendar calReferencia = Calendar.getInstance();
		calReferencia.setTime(dataReferencia);
		calReferencia.add(Calendar.MONTH, -1);
		
		//Obtendo os bonificadores da promocao do assinante.
		Collection bonificadores = BonificadorPulaPulaFactory.newBonificadoresPulaPula(pAssinante.getTiposBonificacao());
		//Obtendo as bonificacoes do assinante, calculadas pelos bonificadores presentes na lista.
		for(Iterator iterator = bonificadores.iterator(); iterator.hasNext();)
			saldo.addBonificacao(((BonificadorPulaPula)iterator.next()).calcularBonificacao(pAssinante,
																							calReferencia.getTime(),
																							isConsultaCheia));
		
        //Inclusao da lista das PromocoesPacoteBonus para a bonificacao extra em determinados saldos.
        //@since  05/11/2007
        //@author Galvagni
        saldo.setListaPromocoesPacoteBonus(pAssinante.getPromocao().getPromocoesPacoteBonus(pAssinante.getCodigoNacional().getIdtCodigoNacional(), 
        						   															Calendar.getInstance().getTime()));
		
		pAssinante.setSaldo(saldo);
	}
	
}
