package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade;

import java.util.Date;

import com.brt.gpp.comum.Definicoes;

/**
 *	Entidade responsavel pelas informacoes do cabecalho do extrato Pula-Pula de um assinante.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@date		28/03/2008
 *	@modify		Primeira versao.
 */
public class Cabecalho 
{

	/**
	 *	Codigo de retorno da operacao.
	 */
	private short retorno;
	
	/**
	 *	MSISDN do assinante.
	 */
	private String msisdn;
	
	/**
	 *	Identificador da promocao do assinante.
	 */
	private int promocao;
	
	/**
	 *	Data inicial de consulta. 
	 */
	private Date dataIni;
	
	/**
	 *	Data final de consulta.
	 */
	private Date dataFim;
	
	/**
	 *	Mensagem informativa referente a construcao do extrato.
	 */
	private String mensagem;
	
	/**
	 *	Construtor da classe.
	 */
	public Cabecalho()
	{
		this.retorno	= Definicoes.RET_OPERACAO_OK;
		this.msisdn		= "";
		this.promocao	= -1;
		this.dataIni	= null;
		this.dataFim	= null;
		this.mensagem	= "";
	}
	
	/**
	 *	Retorna o codigo de retorno da operacao.
	 *
	 *	@return		Codigo de retorno da operacao.
	 */
	public short getRetorno()
	{
		return this.retorno;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		MSISDN do assinante.
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna o identificador da promocao do assinante.
	 *
	 *	@return		Identificador da promocao do assinante.
	 */
	public int getPromocao()
	{
		return this.promocao;
	}
	
	/**
	 *	Retorna a data inicial de consulta.
	 *
	 *	@return		Data inicial de consulta.
	 */
	public Date getDataIni()
	{
		return this.dataIni;
	}
	
	/**
	 *	Retorna a data final de consulta.
	 *
	 *	@return		Data final de consulta.
	 */
	public Date getDataFim()
	{
		return this.dataFim;
	}
	
	/**
	 *	Retorna a mensagem informativa referente a construcao do extrato.
	 *
	 *	@return		Mensagem informativa referente a construcao do extrato.
	 */
	public String getMensagem()
	{
		return this.mensagem;
	}
	
	/**
	 *	Atribui o codigo de retorno da operacao.
	 *
	 *	@param		retorno					Codigo de retorno da operacao.
	 */
	public void setRetorno(short retorno)
	{
		this.retorno = retorno;
	}
	
	/**
	 *	Atribui o MSISDN do assinante
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui o identificador da promocao do assinante.
	 *
	 *	@param		promocao				Identificador da promocao do assinante.
	 */
	public void setPromocao(int promocao)
	{
		this.promocao = promocao;
	}
	
	/**
	 *	Atribui a data inicial de consulta.
	 *
	 *	@param		dataIni					Data inicial de consulta.
	 */
	public void setDataIni(Date dataIni)
	{
		this.dataIni = dataIni;
	}
	
	/**
	 *	Atribui a data final de consulta.
	 *
	 *	@param		dataFim					Data final de consulta.
	 */
	public void setDataFim(Date dataFim)
	{
		this.dataFim = dataFim;
	}
	
	/**
	 *	Atribui a mensagem informativa referente a construcao do extrato.
	 *
	 *	@param		mensagem				Mensagem informativa referente a construcao do extrato.
	 */
	public void setMensagem(String mensagem)
	{
		this.mensagem = mensagem;
	}
	
}
