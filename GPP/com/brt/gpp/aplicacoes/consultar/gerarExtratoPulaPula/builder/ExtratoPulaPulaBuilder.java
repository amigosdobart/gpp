package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.builder;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Cabecalho;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Detalhe;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.ExtratoPulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.bonificadorPulaPula.BonificadorPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.SelecaoExtratoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Classe responsavel pela construcao de cabecalhos de extratos Pula-Pula.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/03/2008
 *	@modify		Primeira versao.
 */
public class ExtratoPulaPulaBuilder 
{

	/**
	 *	Conexao com o banco de dados.
	 */
	private PREPConexao conexaoPrep;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 */
	public ExtratoPulaPulaBuilder(PREPConexao conexaoPrep)
	{
		this.conexaoPrep = conexaoPrep;
	}
	
	/**
	 *	Constroi e retorna a lista de chamadas do extrato Pula-Pula.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataIni					Data de inicio da consulta.
	 *	@param		dataFim					Data final da consulta.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@throws		GPPInternalErrorException, SQLException
	 */
	public ExtratoPulaPula newExtratoPulaPula(AssinantePulaPula	pAssinante,
											  Date				dataIni,
											  Date				dataFim,
											  boolean			isConsultaCheia) throws GPPInternalErrorException,
											  											SQLException
	{
		ExtratoPulaPula	result		= new ExtratoPulaPula();
		TreeSet			detalhes	= new TreeSet();
		TreeMap			sumarizacao	= new TreeMap();
		
		//Criando o cabecalho. 
		result.setCabecalho(this.newCabecalho(pAssinante, dataIni, dataFim));
		
		//Criando os detalhes e a sumarizacao de chamadas.
		if(pAssinante.getAssinante().getRetorno() == Definicoes.RET_OPERACAO_OK)
		{
			if(pAssinante.getStatus().isDisponivelConsulta() || isConsultaCheia)
			{
				detalhes	= this.newChamadas(pAssinante, dataIni, dataFim, isConsultaCheia);
				sumarizacao	= this.newSumarizacao(detalhes);
				
				//Adicionando os bonus recebidos as chamadas.
				detalhes.addAll(Detalhe.extractAll(pAssinante.getBonusConcedidos()));
			}
			else
			{
				pAssinante.bloquearConsulta();
				detalhes.add(Detalhe.extract(pAssinante.getStatus()));
			}
		}
		
		result.getDetalhes().addAll(detalhes);
		result.getSumarizacao().putAll(sumarizacao);
		result.setSaldo(pAssinante.getSaldo());
		
		return result;
	}
	
	/**
	 *	Constroi e retorna o cabecalho do extrato Pula-Pula.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataIni					Data de inicio da consulta.
	 *	@param		dataFim					Data final da consulta.
	 */
	private Cabecalho newCabecalho(AssinantePulaPula pAssinante, Date dataIni, Date dataFim)
	{
		Cabecalho result = new Cabecalho();
		
		result.setRetorno(pAssinante.getAssinante().getRetorno());
		result.setMsisdn(pAssinante.getIdtMsisdn());
		result.setPromocao(pAssinante.getPromocao().getIdtPromocao());
		result.setDataIni(dataIni);
		result.setDataFim(dataFim);
		result.setMensagem("Extrato Pula-Pula gerado com sucesso.");
		
		return result;
	}

	/**
	 *	Constroi e retorna a lista de chamadas do extrato Pula-Pula.
	 *	
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataIni					Data de inicio da consulta.
	 *	@param		dataFim					Data final da consulta.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@throws		GPPInternalErrorException, SQLException
	 */
	private TreeSet newChamadas(AssinantePulaPula	pAssinante, 
							    Date 				dataIni, 
							    Date				dataFim, 
							    boolean				isConsultaCheia) throws GPPInternalErrorException, SQLException
	{
		TreeSet					result			= new TreeSet();
		SelecaoExtratoPulaPula	selecao			= new SelecaoExtratoPulaPula(this.conexaoPrep);
		BonusPulaPula			bonusCn			= pAssinante.getBonusCn();
		TreeMap					acumuladores	= new TreeMap();
		
		//Inicializando os acumuladores de bonus para verificacao do limite de cada tipo de bonificacao.
		for(Iterator iterator = pAssinante.getTiposBonificacao().iterator(); iterator.hasNext();)
			acumuladores.put(iterator.next(), new Double(0.0));
		
		try
		{
			selecao.execute(pAssinante.getIdtMsisdn(), 
							pAssinante.getPromocao().getIdtPromocao(), 
							dataIni, 
							dataFim, 
							isConsultaCheia);
			
			for(Detalhe chamada = null; (chamada = selecao.next()) != null;)
			{
				//Calculando o valor de bonus Pula-Pula para a ligacao.
				chamada.setBonus(BonificadorPulaPula.calcularBonusPulaPula(chamada, bonusCn, isConsultaCheia));
				
				//Caso a consulta nao seja cheia, e necessario verificar nos acumuladores se o limite foi 
				//atingido ou nao. Caso seja, nao e necessario fazer esta verificacao.
				if(!isConsultaCheia)
				{
					//Obtendo a bonificacao Pula-Pula aplicavel a chamada.
					BonificacaoPulaPula bonificacao = 
						pAssinante.getSaldo().getBonificacao(chamada.getDesconto());
					
					//Caso a consulta nao seja cheia e o acumulador nao estiver definido, nao deve ser inserida 
					//a chamada na lista do extrato.
					if(acumuladores.get(bonificacao.getTipoBonificacao()) != null)
					{
						this.marcarLimiteAtingido(chamada, bonificacao, bonusCn, acumuladores);
						result.add(chamada);
					}
					
					//Caso a lista de acumuladores estiver vazia, e necessario parar a construcao do extrato.
					if(acumuladores.isEmpty())
						break;
				}
				else
					result.add(chamada);
			}
		}
		finally
		{
			selecao.close();
		}
		
		return result;
	}

	/**
	 *	Analisa se o limite para o tipo de bonificacao foi atingido e marca o detalhe do extrato com o evento.
	 *	
	 *	@param		chamada					Chamada recebida pelo assinante.
	 *	@param		bonificacao				Bonus Pula-Pula a ser concedido ao assinante.
	 *	@param		bonusCn					Informacoes de bonus por minuto para a promocao do assinante.
	 *	@param		acumuladores			Lista de acumuladores por tipo de bonificacao.
	 */
	private void marcarLimiteAtingido(Detalhe				chamada,
									  BonificacaoPulaPula	bonificacao, 
									  BonusPulaPula			bonusCn,
									  TreeMap				acumuladores)
	{
		//Obtendo o objeto acumulador de bonus Pula-Pula. Caso o acumulador nao esteja definido, e porque 
		//o limite da bonificacao em questao ja foi atingido.
		Double acumulador = (Double)acumuladores.get(bonificacao.getTipoBonificacao());

		//Caso o valor do bonus tenha ultrapassado o limite da bonificacao, e necessario ajustar  
		//o valor do bonus e sua duracao para adequar-se ao limite.
		if((bonificacao.isLimiteUltrapassado()) && 
		   (chamada.getBonus() + acumulador.doubleValue() >= bonificacao.getLimite()))
		{
			chamada.setIndEvento(true);
			double novoBonus = (bonificacao.getLimite() > acumulador.doubleValue()) ?
									bonificacao.getLimite() - acumulador.doubleValue() : 0.0;
			chamada.setBonus(novoBonus);
			chamada.setDuracao(BonificadorPulaPula.calcularDuracao(chamada, bonusCn, false));
			chamada.setDescricao("Limite de bonus Pula-Pula atingido: " + 
	        					 bonificacao.getTipoBonificacao().getNomTipoBonificacao() +
	        					 " (R$ " + bonificacao.format(BonificacaoPulaPula.LIMITE) + ")");
			acumuladores.remove(bonificacao.getTipoBonificacao());
		}
		else
			acumuladores.put(bonificacao.getTipoBonificacao(), 
							 new Double(acumulador.doubleValue() + chamada.getBonus()));
	}
	
	/**
	 *	Constroi e retorna a sumarizacao do extrato Pula-Pula.
	 *	
	 *	@param		chamadas				Lista de chamadas bonificaveis ou nao bonificaveis.
	 */
	private TreeMap newSumarizacao(TreeSet chamadas)
	{
		TreeMap result = new TreeMap();
		
		for(Iterator iterator = chamadas.iterator(); iterator.hasNext();)
		{
			Detalhe	chamada		= (Detalhe)iterator.next();			
			Detalhe	sumarizacao	= (Detalhe)result.get(chamada.getDesconto());
			
			if(sumarizacao == null)
			{
				sumarizacao = new Detalhe();
				sumarizacao.setDesconto(chamada.getDesconto());
				result.put(chamada.getDesconto(), sumarizacao);
			}
			
			sumarizacao.addDuracao(chamada.getDuracao());
			sumarizacao.addBonus(chamada.getBonus());
		}
		
		return result;
	}
	
}
