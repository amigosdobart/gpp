package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 *	Classe responsavel pela consulta de bonus concedidos ao assinante durante um periodo.
 *
 *	@version	1.0		14/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ConsultorBonusConcedidoPeriodo extends ConsultorDetalhePulaPula
{

	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorBonusConcedidoPeriodo(long idProcesso)
	{
		super("ConsultorBonusConcedidoPeriodo", idProcesso);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula#consultarDetalhePulaPula(AssinantePulaPula, java.util.Date, PREPConexao)
	 */
	public void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia,
										 boolean			isConsultaCheia,
										 PREPConexao		conexaoPrep) throws Exception
	{	
	    Calendar calReferencia = Calendar.getInstance();
	    calReferencia.setTime(dataReferencia);
	  
	    //Obtendo a data de entrada do assinante na promocao. E necessaria para verificar se o bonus concedido no 
	    //periodo e aplicavel ao titular atual ou nao, para evitar reclamacoes e expurgos/estornos indevidos.
	    Date dataEntrada = pAssinante.getDatEntradaPromocao();
	    
	    //Inicializando a lista de bonus concedidos ao assinante para cada tipo de execucao e tipo de bonificacao.
	    Collection bonusConcedidos = new ArrayList();
	
	    //Percorrendo a lista de tipos de transacao dos bonus concedidos atraves da promocao do assinante.
	    for(Iterator iterator = pAssinante.getLancamentos().iterator(); iterator.hasNext();)
	    {
	    	OrigemRecarga lancamento = (OrigemRecarga)iterator.next();
	    	
	    	if(lancamento.getTipLancamento().equals(OrigemRecarga.LANCAMENTO_CREDITO))
	    	{
		    	Calendar	calBonus	= (Calendar)calReferencia.clone();
		    	Date		dataIni		= null;
		    	Date		dataFim		= null;
		    	
		    	//Caso o tipo de transacao seja referente ao adiantamento parcial, e necessario que a data de 
		    	//referencia seja atrasada em um mes. Isto porque a data de referencia corresponde sempre a concessao  
		    	//default, que  e no mes posterior ao mes de analise. No caso da execucao parcial, a concessao ocorre 
		    	//no mesmo mes de analise.
		    	if(pAssinante.matches(lancamento, Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL))
		    		calBonus.add(Calendar.MONTH, -1);
		    	calBonus.set(Calendar.DAY_OF_MONTH, calBonus.getActualMinimum(Calendar.DAY_OF_MONTH));
		    	dataIni = (!calBonus.getTime().before(dataEntrada) || isConsultaCheia) ? 
		    					calBonus.getTime() : dataEntrada;
		    	calBonus.add(Calendar.MONTH, 1);
		    	dataFim = (!calBonus.getTime().before(dataEntrada) || isConsultaCheia) ? 
		    					calBonus.getTime() : dataEntrada;
		    	
	            bonusConcedidos.addAll(super.getConsulta().getRecargas(pAssinante.getIdtMsisdn(), 
	            													   lancamento.getTipoTransacao(), 
	            													   dataIni, 
	            													   dataFim, 
	            													   conexaoPrep));
	    	}
	    }
	    
        pAssinante.setBonusConcedidos(bonusConcedidos);
	}
	
}
