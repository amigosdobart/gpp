package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPulaFactory;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 *	Classe responsavel pela consulta de bonus agendados ao assinante.
 *
 *	@version	1.0		17/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ConsultorBonusAgendado extends ConsultorDetalhePulaPula
{

	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorBonusAgendado(long idProcesso)
	{
		super("ConsultorBonusAgendado", idProcesso);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula#consultarDetalhePulaPula(AssinantePulaPula, java.util.Date, PREPConexao)
	 */
	public void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia,
										 boolean			isConsultaCheia,
										 PREPConexao		conexaoPrep) throws Exception
	{
		Assinante assinante = pAssinante.getAssinante();
		
		if(assinante != null)
		{
			if(pAssinante.getAssinante().isHibrido())
				this.consultarHibrido(pAssinante, dataReferencia,isConsultaCheia, conexaoPrep);
			else
				this.consultarPrePago(pAssinante, dataReferencia, conexaoPrep);
		}
	}
	
	/**
	 *	Consulta os bonus agendados de assinantes pre-pagos.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia para concessao do bonus.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	private void consultarPrePago(AssinantePulaPula pAssinante, Date dataReferencia, PREPConexao conexaoPrep) throws Exception
	{
	    //Inicializando a lista de bonus agendados ao assinante para cada tipo de execucao e tipo de bonificacao.
	    ArrayList bonusAgendados = new ArrayList();
	    
	    //Percorrendo a lista de tipos de transacao dos bonus concedidos atraves da promocao do assinante.
	    for(Iterator iterator = pAssinante.getLancamentos().iterator(); iterator.hasNext();)
	    {
	    	OrigemRecarga lancamento = (OrigemRecarga)iterator.next();
	    	
	    	if(lancamento.getTipLancamento().equals(OrigemRecarga.LANCAMENTO_CREDITO))
	    		bonusAgendados.addAll(super.getConsulta().getFilaRecargas(pAssinante.getIdtMsisdn(), 
            														  	  lancamento.getTipoTransacao(), 
            														  	  new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA), 
            														  	  conexaoPrep));
	    }
	    
        pAssinante.setBonusAgendados(bonusAgendados);
	}
	
	/**
	 *	Consulta os bonus agendados de assinantes hibridos.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia para concessao do bonus.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	private void consultarHibrido(AssinantePulaPula	pAssinante, 
								  Date				dataReferencia, 
								  boolean			isConsultaCheia, 
								  PREPConexao		conexaoPrep) throws Exception
	{
		//O bonus do Controle nao e agendado na Fila de Recargas, uma vez que e liberado pelo SAG. Desta forma,
		//a consulta da promocao Pula-Pula sem passagem de mes de referencia nao pode contar com a data de 
		//execucao porque para o Controle nao esta definida. Para resolver este problema e adotada a seguinte
		//regra:
		//	1. Na consulta sem passagem de mes de referencia, a data de referencia tera sempre o mes posterior 
		//	   ao mes da data do sistema.
		//	2. Caso o assinante ja tenha recebido o bonus do mes do sistema, nenhum bonus agendado sera demonstrado.
		//	3. Caso o assinante ainda nao tenha recebido o bonus, sao criados registros de bonus agendados 
		//	   correspondentes ao valor de bonus que o assinante tem para receber devido as ligacoes recebidas 
		//	   no mes anterior.
        Calendar calReferencia = Calendar.getInstance();
        calReferencia.setTime(dataReferencia);
    	calReferencia.add(Calendar.MONTH, -1);
    	
    	int[] detalhes =
        {
    		ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
            ControlePulaPula.TOTALIZACAO,
            ControlePulaPula.BONUS_PULA_PULA,
            ControlePulaPula.SALDO_PULA_PULA
        };

    	ConsultorDetalhePulaPulaFactory consultor = new ConsultorDetalhePulaPulaFactory(super.getIdLog());
    	AssinantePulaPula hibrido = consultor.consultarDetalhesPulaPula(pAssinante, 
    																	detalhes, 
    																	calReferencia.getTime(),
    																	false,
    																	conexaoPrep);
    	
    	if(hibrido.getBonusConcedidos(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT).isEmpty())
    		pAssinante.setBonusAgendados(hibrido.toFilaRecargas(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT));
	}
	
}
