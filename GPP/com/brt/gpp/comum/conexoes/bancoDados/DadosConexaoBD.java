package com.brt.gpp.comum.conexoes.bancoDados;

import java.util.Date;

import com.brt.gpp.comum.conexoes.DadosConexao;

/**
 *	Classe para criacao de objetos contendo informacoes referentes a conexoes com o banco de dados.
 *
 *	@author		Daniel Ferreira
 *	@since		04/10/2006
 */
public class DadosConexaoBD extends DadosConexao
{
	/**
	 *	Numero de Statements abertos pela conexao.
	 */
	private int numStatements;
	
	/**
	 *	Construtor da classe.
	 */
	public DadosConexaoBD()
	{
		super();
		
		this.numStatements = 0;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 *	@param		dataInicialUso			Identificador do processo.
	 *	@param		numStatements			Numero de statements abertos pela conexao.
	 */
	public DadosConexaoBD(long idProcesso, Date dataInicialUso, int numStatements)
	{
		super(idProcesso, dataInicialUso);
		
		this.numStatements	= numStatements;
	}
	
	/**
	 *	Retorna o numero de Statements abertos pela conexao.
	 *
	 *	@return		Numero de Statements abertos pela conexao.
	 */
	public int getNumStatements()
	{
		return this.numStatements;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer(super.toString());
		
		result.append(" - Statements: ");
		result.append(this.numStatements);
		
		return result.toString();
	}
	
}