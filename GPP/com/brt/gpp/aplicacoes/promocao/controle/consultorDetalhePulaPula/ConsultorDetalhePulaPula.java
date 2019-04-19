package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Classe responsavel pela consulta de informacoes especificas da promocao Pula-Pula.
 *
 *	@version	1.0		14/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public abstract class ConsultorDetalhePulaPula extends Aplicacoes
{

	/**
	 *	DAO para consulta de informacoes e configuracoes das promocoes Pula-Pula.
	 */
	private Consulta consulta;
	
	/**
	 *	Construtor da classe.
	 *	
	 *	@param		nomeClasse				Nome da classe de aplicacao.
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorDetalhePulaPula(String nomeClasse, long idProcesso)
	{
		super(idProcesso, nomeClasse);
		
		this.consulta = new Consulta(idProcesso);
	}
	
	/**
	 *	Retorna o DAO para consulta de informacoes e configuracoes das promocoes Pula-Pula.
	 *
	 *	@return		DAO para consulta de informacoes e configuracoes das promocoes Pula-Pula.
	 */
	protected Consulta getConsulta()
	{
		return this.consulta;
	}
	
	/**
	 *	Consulta e atualiza a promocao Pula-Pula do assinante com as informacoes desejadas.
	 *
	 *	@param		pAssinante				Informacoes da promocao Pula-Pula do assinante.
	 *	@param		dataReferencia			Data de referencia para consulta das informacoes do assinante.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
	 *	@param		conexaoPrep				Conexao com o banco de dados.
	 *	@throws		Exception
	 */
	public abstract void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
												  Date				dataReferencia,
												  boolean			isConsultaCheia,
												  PREPConexao		conexaoPrep) throws Exception;
	
}
