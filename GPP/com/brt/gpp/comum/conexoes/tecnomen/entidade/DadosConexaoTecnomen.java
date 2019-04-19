package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import java.util.Date;

import com.brt.gpp.comum.conexoes.DadosConexao;

/**
 *	Classe para criacao de objetos contendo informacoes referentes a conexoes com os servicos da Tecnomen.
 *
 *	@author		Daniel Ferreira
 *	@since		04/10/2006
 */
public class DadosConexaoTecnomen extends DadosConexao
{
	
	/**
	 *	Identificador do servico da Tecnomen.
	 */
	private int idServico;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idServico				Identificador do servico da Tecnomen.
	 */
	public DadosConexaoTecnomen(int idServico)
	{
		super();
		
		this.idServico = idServico;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 *	@param		dataInicialUso			Data de obtencao da conexao pelo processo.
	 *	@param		idServico				Identificador do servico da Tecnomen.
	 */
	public DadosConexaoTecnomen(long idProcesso, Date dataInicialUso, int idServico)
	{
		super(idProcesso, dataInicialUso);
		
		this.idServico = idServico;
	}
	
	/**
	 *	Retorna o identificador do servico da Tecnomen.
	 *
	 *	@return		Identificador do servico da Tecnomen.
	 */
	public int getIdServico()
	{
		return this.idServico;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer(super.toString());
		
		result.append(" - ID servico Tecnomen: ");
		result.append(this.idServico);
		
		return result.toString();
	}
	
}