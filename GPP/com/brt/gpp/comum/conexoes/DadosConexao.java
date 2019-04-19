package com.brt.gpp.comum.conexoes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao;

/**
 *	Classe para criacao de objetos contendo informacoes referentes a conexoes.
 *
 *	@author		Daniel Ferreira
 *	@since		03/10/2006
 */
public class DadosConexao
{
	
	/**
	 *	ID do Processo utilizando a conexao.
	 */
	private long idProcesso;
	
	/**
	 *	Data de obtencao da conexao pelo processo.
	 */
	private Date dataInicialUso;
	
	/**
	 *	Construtor da classe.
	 */
	public DadosConexao()
	{
		this.idProcesso		= -1;
		this.dataInicialUso	= null;
	}
	
	/**
	 *	Construtor da classe. A utilizacao deste construtor indica a alocacao da conexao.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 *	@param		dataInicialUso			Data de obtencao da conexao pelo processo.
	 */
	public DadosConexao(long idProcesso, Date dataInicialUso)
	{
		this.idProcesso		= idProcesso;
		this.dataInicialUso	= dataInicialUso;
	}
	
	/**
	 *	Retorna o ID do Processo utilizando a conexao.
	 *
	 *	@return		ID do Processo utilizando a conexao.
	 */
	public long getIdProcesso()
	{
		return this.idProcesso;
	}
	
	/**
	 *	Retorna a data de obtencao da conexao pelo processo.
	 *
	 *	@return		Data de obtencao da conexao pelo processo.
	 */
	public Date getDataInicialUso()
	{
		return this.dataInicialUso;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		result.append("ID Processo: ");
		result.append(new DecimalFormat("000000").format(this.idProcesso));
		result.append(" - Inicio Uso: ");
		result.append((this.dataInicialUso != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.dataInicialUso) : "Disponivel");
		
		return result.toString();
	}
	
	/**
	 *	Retorna um objeto IdProcessoConexao com as informacoes da conexao.
	 *
	 *	@return		Objeto IdProcessoConexao com as informacoes da conexao.
	 */
	public IdProcessoConexao toIdProcessoConexao()
	{
		short idProcesso = new Long(this.idProcesso).shortValue();
		String dataInicialUso = (this.dataInicialUso != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.dataInicialUso) : "";
		
		return new IdProcessoConexao(idProcesso, dataInicialUso);
	}
	
}