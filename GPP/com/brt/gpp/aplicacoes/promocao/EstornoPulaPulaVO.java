package com.brt.gpp.aplicacoes.promocao;

import java.util.Collection;

import com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula;

/**
 *	Value Object para consumo de registros do processo de Estorno de Bonus Pula-Pula por Fraude.
 * 
 *	@author		Daniel Ferreira
 *	@since		20/11/2006
 */
public class EstornoPulaPulaVO 
{

	/**
	 *	Lista de requisicoes de estorno.
	 */
	private Collection listaEstorno;
	
	/**
	 *	Objeto responsavel pelas operacoes no processo de Estorno de Bonus Pula-Pula por Fraude.
	 */
	private OperacoesEstornoPulaPula operacoes;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		listaEstorno			Lista de requisicoes de estorno.
	 *	@param		operacoes				Objeto responsavel pelas operacoes no processo de Estorno de Bonus Pula-Pula por Fraude.
	 */
	public EstornoPulaPulaVO(Collection listaEstorno, OperacoesEstornoPulaPula operacoes)
	{
		this.listaEstorno	= listaEstorno;
		this.operacoes		= operacoes;
	}
	
	/**
	 *	Retorna a lista de requisicoes de estorno.
	 *
	 *	@return		Lista de requisicoes de estorno.
	 */
	public Collection getListaEstorno()
	{
		return this.listaEstorno;
	}
	
	/**
	 *	Retorna o objeto responsavel pelas operacoes no processo de Estorno de Bonus Pula-Pula por Fraude.
	 *
	 *	@return		Objeto responsavel pelas operacoes no processo de Estorno de Bonus Pula-Pula por Fraude.
	 */
	public OperacoesEstornoPulaPula getOperacoes()
	{
		return this.operacoes;
	}
	
}
