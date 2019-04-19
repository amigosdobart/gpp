package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.controle.GerenciadorDataExecucao;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Classe responsavel pela consulta de datas de credito do Pula-Pula.
 *
 *	@version	1.0		14/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ConsultorDataCredito extends ConsultorDetalhePulaPula
{

	/**
	 *	Gerenciador para calculo de datas de credito.
	 */
	private GerenciadorDataExecucao gerenciadorData;
	
	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorDataCredito(long idProcesso)
	{
		super("ConsultorDataCredito", idProcesso);
		
		this.gerenciadorData = new GerenciadorDataExecucao(idProcesso, super.getConsulta());
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula#consultarDetalhePulaPula(AssinantePulaPula, java.util.Date, PREPConexao)
	 */
	public void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia,
										 boolean			isConsultaCheia,
										 PREPConexao		conexaoPrep) throws Exception
	{
        pAssinante.setDatasCredito(this.gerenciadorData.calcularDatasCredito(pAssinante, dataReferencia));
	}
	
}
