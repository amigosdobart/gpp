package com.brt.gpp.aplicacoes.campanha.entidade;

import java.util.Collection;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

public class InscricaoAssinanteVO
{
	private ProdutorCampanha produtorCampanha;
	private Assinante assinante;
	private Collection listaCampanhas;
	
	/**
	 * Metodo....:getlistaCampanhas
	 * Descricao.:Retorna o valor de listaCampanhas
	 * @return listaCampanhas.
	 */
	public Collection getListaCampanhas()
	{
		return listaCampanhas;
	}

	/**
	 * Metodo....:setlistaCampanhas
	 * Descricao.:Define o valor de listaCampanhas
	 * @param listaCampanhas o valor a ser definido para listaCampanhas
	 */
	public void setListaCampanhas(Collection listaCampanhas)
	{
		this.listaCampanhas = listaCampanhas;
	}

	/**
	 * Metodo....:getassinante
	 * Descricao.:Retorna o valor de assinante
	 * @return assinante.
	 */
	public Assinante getAssinante()
	{
		return assinante;
	}
	
	/**
	 * Metodo....:setassinante
	 * Descricao.:Define o valor de assinante
	 * @param assinante o valor a ser definido para assinante
	 */
	public void setAssinante(Assinante assinante)
	{
		this.assinante = assinante;
	}
	
	/**
	 * Metodo....:getprodutorCampanha
	 * Descricao.:Retorna o valor de produtorCampanha
	 * @return produtorCampanha.
	 */
	public ProdutorCampanha getProdutorCampanha()
	{
		return produtorCampanha;
	}
	
	/**
	 * Metodo....:setprodutorCampanha
	 * Descricao.:Define o valor de produtorCampanha
	 * @param produtorCampanha o valor a ser definido para produtorCampanha
	 */
	public void setProdutorCampanha(ProdutorCampanha produtorCampanha)
	{
		this.produtorCampanha = produtorCampanha;
	}
}

