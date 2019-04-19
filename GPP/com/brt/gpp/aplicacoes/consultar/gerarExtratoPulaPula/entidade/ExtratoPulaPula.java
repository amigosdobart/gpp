package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Cabecalho;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;

/**
 *	Entidade responsavel pelas informacoes do extrato Pula-Pula de um assinante.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		27/03/2008
 *	@modify		Primeira versao.
 */
public class ExtratoPulaPula 
{

	/**
	 *	Cabecalho do extrato.
	 */
	private Cabecalho cabecalho;
	
	/**
	 *	Detalhamento do extrato. Contem informacoes de ligacoes recebidas e bonus concedidos. No caso em que o 
	 *	status da promocao Pula-Pula do assinante nao permite a exibicao do extrato, a lista de detalhes contem 
	 *	somente um elemento, extraido do status.
	 */
	private Collection detalhes;
	
	/**
	 *	Sumarizacao das ligacoes do extrato.
	 */
	private Map sumarizacao;
	
	/**
	 *	Saldo Pula-Pula a ser concedido ao assinante.
	 */
	private SaldoPulaPula saldo;

	/**
	 *	Construtor da classe.
	 */
	public ExtratoPulaPula()
	{
		this.cabecalho		= null;
		this.detalhes		= new TreeSet();
		this.sumarizacao	= new TreeMap();
		this.saldo			= null;
	}
	
	/**
	 *	Retorna o cabecalho do extrato.
	 *
	 *	@return		Cabecalho do extrato.
	 */
	public Cabecalho getCabecalho()
	{
		return this.cabecalho;
	}
	
	/**
	 *	Retorna o detalhamento do extrato.
	 *
	 *	@return		Detalhamento do extrato.
	 */
	public Collection getDetalhes()
	{
		return this.detalhes;
	}
	
	/**
	 *	Retorna a sumarizacao das ligacoes do extrato.
	 *
	 *	@return		Sumarizacao das ligacoes do extrato.
	 */
	public Map getSumarizacao()
	{
		return this.sumarizacao;
	}
	
	/**
	 *	Retorna o saldo Pula-Pula a ser concedido ao assinante.
	 *
	 *	@return		Saldo Pula-Pula a ser concedido ao assinante.
	 */
	public SaldoPulaPula getSaldo()
	{
		return this.saldo;
	}
	
	/**
	 *	Atribui o cabecalho do extrato.
	 *
	 *	@param		cabecalho				Cabecalho do extrato.
	 */
	public void setCabecalho(Cabecalho cabecalho)
	{
		this.cabecalho = cabecalho;
	}
	
	/**
	 *	Atribui o detalhamento do extrato.
	 *
	 *	@param		detalhes				Detalhamento do extrato.
	 */
	public void setDetalhes(Collection detalhes)
	{
		this.detalhes = detalhes;
	}
	
	/**
	 *	Atribui a sumarizacao das ligacoes do extrato.
	 *
	 *	@param		sumarizacao				Sumarizacao das ligacoes do extrato.
	 */
	public void setSumarizacao(Map sumarizacao)
	{
		this.sumarizacao = sumarizacao;
	}
	
	/**
	 *	Atribui o saldo Pula-Pula a ser concedido ao assinante.
	 *
	 *	@param		saldo					Saldo Pula-Pula a ser concedido ao assinante.
	 */
	public void setSaldo(SaldoPulaPula saldo)
	{
		this.saldo = saldo;
	}

}
