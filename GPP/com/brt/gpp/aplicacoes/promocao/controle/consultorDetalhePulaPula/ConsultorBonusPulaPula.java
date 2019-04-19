package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Classe responsavel pela consulta de objetos com informacoes de bonus Pula-Pula por minuto de ligacoes 
 *	recebidas pelo assinante.
 *
 *	@version	1.0		14/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ConsultorBonusPulaPula extends ConsultorDetalhePulaPula
{

	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorBonusPulaPula(long idProcesso)
	{
		super("ConsultorBonusPulaPula", idProcesso);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula#consultarDetalhePulaPula(AssinantePulaPula, java.util.Date, PREPConexao)
	 */
	public void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia,
										 boolean			isConsultaCheia,
										 PREPConexao		conexaoPrep) throws Exception
	{
        //Obtendo o valor de bonus Pula-Pula por minuto.
	    Calendar calBonus = Calendar.getInstance();
	    calBonus.setTime(dataReferencia);
        calBonus.add(Calendar.MONTH, -1);
        
	    Date	dataBonus	= calBonus.getTime();
	    Integer	cn			= new Integer(pAssinante.getIdtMsisdn().substring(2, 4));
	    Integer	plano		= (pAssinante.getAssinante() != null) ? 
	    						new Integer(pAssinante.getAssinante().getPlanoPreco()) : null;
	    		
        pAssinante.setBonusCn(super.getConsulta().getBonusPulaPula(cn, plano, dataBonus));
	}
	
}
