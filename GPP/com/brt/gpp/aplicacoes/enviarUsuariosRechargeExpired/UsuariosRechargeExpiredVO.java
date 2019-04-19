package com.brt.gpp.aplicacoes.enviarUsuariosRechargeExpired;

/**
 * Este arquivo contem a definicao da classe
 * que define os dados passados sobre os usuários em Recharge Expired
 * 
 * @Autor:		Magno Batista Corrêa
 * @since:      11/04/2007
 * @version:    1.0
 */

public class UsuariosRechargeExpiredVO
{
	private String 	msisdn;
	private int		idtPlanoPreco;
	private int 	idtPromocao;
	private String 	data;
	
	/**
	 * Construtor Carregado
	 * @param msisdn
	 * @param idtPlanoPreco
	 * @param idtPromocao
	 * @param data
	 */
	public UsuariosRechargeExpiredVO(String msisdn, int idtPlanoPreco, int idtPromocao, String data)
	{
		this.msisdn = msisdn;
		this.idtPlanoPreco = idtPlanoPreco;
		this.idtPromocao = idtPromocao;
		this.data = data;
	}

	/**
	 * @return Retorna o idtPlanoPreco.
	 */
	public int getIdtPlanoPreco()
	{
		return idtPlanoPreco;
	}

	/**
	 * @return Retorna o idtPromocao.
	 */
	public int getIdtPromocao()
	{
		return idtPromocao;
	}

	/**
	 * @return Retorna o msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * @return Retorna a data.
	 */
	public String getData()
	{
		return data;
	}

	/**
	 * @param idtPlanoPreco O idtPlanoPreco para alterar.
	 */
	public void setIdtPlanoPreco(int idtPlanoPreco)
	{
		this.idtPlanoPreco = idtPlanoPreco;
	}

	/**
	 * @param idtPromocao O idtPromocao para alterar.
	 */
	public void setIdtPromocao(int idtPromocao)
	{
		this.idtPromocao = idtPromocao;
	}

	/**
	 * @param msisdn O msisdn para alterar.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	/**
	 * @param data O data para alterar.
	 */
	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 *  String informativa
	 */
	public String toString()
	{
		return "msisdn: " + this.msisdn + ";data: " + this.data + ";idtPlanoPreco: "+ this.idtPlanoPreco + ";idtPromocao: " + this.idtPromocao;
	}
}