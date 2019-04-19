package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Classe entidade que representa a tabela TBL_TAN_STATUS_NOTIFICACAO
 *  
 *  @author Jorge Abreu
 *  Criado em: 23/10/2007
 */
public class TanStatusNotificacao implements Serializable
{
	private static final long serialVersionUID = 2110734875140007873L;

	/**
	 * Codigo de status da notifica��o do Tangram.
	 */
	private int idtStatus;
	
	/**
	 * Descricao do Status da Notificacao.
	 */
	private String desStatus;	

	/**
	 * Obt�m o Status da notifica��o.
	 */
	public int getIdtStatus() 
	{
		return idtStatus;
	}

	/**
	 * Define o Status da notifica��o.
	 */
	public void setIdtStatus(int idtStatus) 
	{
		this.idtStatus = idtStatus;
	}
	
	/**
	 * Obt�m a descricao do Status da notifica��o.
	 */
	public String getDesStatus() 
	{
		return desStatus;
	}

	/**
	 * Define a descricao do Status da notifica��o.
	 */
	public void setDesStatus(String desStatus) 
	{
		this.desStatus = desStatus;
	}
}