package com.brt.gpp.aplicacoes.promocao.entidade;

import java.io.Serializable;

/**
 *	Entidade responsavel pela definicao do tipo de bonificacao de promocoes.
 *
 *	@version	1.0		14/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class PromocaoTipoBonificacao implements Comparable, Serializable
{

	/**
	 *	Constante referente ao tipo de bonificacao unico. Somente um bonus e aplicado por concessao.
	 */
	public static final short PULA_PULA_UNICO = 0;

	/**
	 *	Constante referente ao tipo de bonificacao associado a ligacoes on-net.
	 */
	public static final short PULA_PULA_ONNET = 1;

	/**
	 *	Constante referente ao tipo de bonificacao associado a ligacoes off-net.
	 */
	public static final short PULA_PULA_OFFNET = 2;

	/**
	 *	Constante referente ao tipo de bonificacao em funcao de recargas realizadas pelo assinante.
	 */
	public static final short PULA_PULA_RECARGA = 3;

	/**
	 *	Identificador do tipo de bonificacao.
	 */
	private short idTipoBonificacao;

	/**
	 *	Nome do tipo de bonificacao.
	 */
	private String nomTipoBonificacao;

	/**
	 *	Construtor da classe.
	 */
	public PromocaoTipoBonificacao()
	{
		this.idTipoBonificacao	= -1;
		this.nomTipoBonificacao	= null;
	}

	/**
	 *	Retorna o identificador do tipo de bonificacao.
	 *
	 *	@return		Identificador do tipo de bonificacao.
	 */
	public short getIdTipoBonificacao()
	{
		return this.idTipoBonificacao;
	}

	/**
	 *	Retorna o nome do tipo de bonificacao.
	 *
	 *	@return		Nome do tipo de bonificacao.
	 */
	public String getNomTipoBonificacao()
	{
		return this.nomTipoBonificacao;
	}

	/**
	 *	Atribui o identificador do tipo de bonificacao.
	 *
	 *	@param		idTipoBonificacao		Identificador do tipo de bonificacao.
	 */
	public void setIdTipoBonificacao(short idTipoBonificacao)
	{
		this.idTipoBonificacao = idTipoBonificacao;
	}

	/**
	 *	Atribui o nome do tipo de bonificacao.
	 *
	 *	@param		nomTipoBonificacao		Nome do tipo de bonificacao.
	 */
	public void setNomTipoBonificacao(String nomTipoBonificacao)
	{
		this.nomTipoBonificacao = nomTipoBonificacao;
	}

	/**
	 *	@see		java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;

		return (this.hashCode() == obj.hashCode());
	}

	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return (this.getClass().getName() + "||" + this.getIdTipoBonificacao()).hashCode();
	}

	/**
	 *	@see		java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object obj)
	{
		return this.getIdTipoBonificacao() - ((PromocaoTipoBonificacao)obj).getIdTipoBonificacao();
	}

	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.getNomTipoBonificacao();
	}

}
