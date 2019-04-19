package com.brt.gpp.comum.mapeamentos.entidade;

import java.util.Date;

/**
 *	Entidade da tabela TBL_REC_LIMITE_CREDITO.
 *
 *	@version	1.0		06/05/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class RevendaVarejo 
{

	/**
	 *	Codigo de revenda.
	 */
	private String codRevenda;
	
	/**
	 *	Indicador de revenda bloqueada.
	 */
	private boolean indBloqueio;
	
	/**
	 *	Data da ultima atualizacao.
	 */
	private Date datAtualizacao;

	/**
	 *	Valor ja utilizado.
	 */
	private double vlrUtilizado;

	/**
	 *	Limite da revenda.
	 */
	private double vlrLimite;

	/**
	 *	Construtor da classe.
	 */
	public RevendaVarejo()
	{
		this.codRevenda		= null;
		this.indBloqueio	= false;
		this.datAtualizacao	= null;
		this.vlrUtilizado	= 0.0;
		this.vlrLimite		= 0.0;
	}
	
	/**
	 *	Retorna o codigo de revenda.
	 *
	 *	@return		Codigo de revenda.
	 */
	public String getCodRevenda()
	{
		return this.codRevenda;
	}
	
	/**
	 *	Indica se a de revenda esta bloqueada.
	 *
	 *	@return		True se estiver bloqueada e false caso contrario.
	 */
	public boolean isBloqueada()
	{
		return this.indBloqueio;
	}
	
	/**
	 *	Retorna a data da ultima atualizacao.
	 *
	 *	@return		Data da ultima atualizacao.
	 */
	public Date getDatAtualizacao()
	{
		return this.datAtualizacao;
	}

	/**
	 *	Retorna o valor ja utilizado.
	 *
	 *	@return		Valor ja utilizado.
	 */
	public double getVlrUtilizado()
	{
		return this.vlrUtilizado;
	}

	/**
	 *	Retorna o limite da revenda.
	 *
	 *	@return		Limite da revenda.
	 */
	public double getVlrLimite()
	{
		return this.vlrLimite;
	}

	/**
	 *	Atribui o codigo de revenda.
	 *
	 *	@param		codRevenda				Codigo de revenda.
	 */
	public void setCodRevenda(String codRevenda)
	{
		this.codRevenda = codRevenda;
	}
	
	/**
	 *	Indica se a de revenda esta bloqueada.
	 *
	 *	@param		isBloqueada				Indicador de bloqueio.
	 */
	public void setIndBloqueio(boolean indBloqueio)
	{
		this.indBloqueio = indBloqueio;
	}
	
	/**
	 *	Atribui a data da ultima atualizacao.
	 *
	 *	@param		datAtualizacao			Data da ultima atualizacao.
	 */
	public void setDatAtualizacao(Date datAtualizacao)
	{
		this.datAtualizacao = datAtualizacao;
	}

	/**
	 *	Atribui o valor ja utilizado.
	 *
	 *	@param		vlrUtilizado			Valor ja utilizado.
	 */
	public void setVlrUtilizado(double vlrUtilizado)
	{
		this.vlrUtilizado = vlrUtilizado;
	}

	/**
	 *	Atribui o limite da revenda.
	 *
	 *	@param		Limite da revenda.
	 */
	public void setVlrLimite(double vlrLimite)
	{
		this.vlrLimite = vlrLimite;
	}

}
