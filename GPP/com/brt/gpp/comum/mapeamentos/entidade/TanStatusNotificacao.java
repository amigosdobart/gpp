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
	 * Codigo de status da notificação do Tangram.
	 */
	private int idtStatus;
	
	/**
	 * Descricao do Status da Notificacao.
	 */
	private String desStatus;	

	/**
	 * Obtém o Status da notificação.
	 */
	public int getIdtStatus() 
	{
		return idtStatus;
	}

	/**
	 * Define o Status da notificação.
	 */
	public void setIdtStatus(int idtStatus) 
	{
		this.idtStatus = idtStatus;
	}
	
	/**
	 * Obtém a descricao do Status da notificação.
	 */
	public String getDesStatus() 
	{
		return desStatus;
	}

	/**
	 * Define a descricao do Status da notificação.
	 */
	public void setDesStatus(String desStatus) 
	{
		this.desStatus = desStatus;
	}
}