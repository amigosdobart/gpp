package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Classe entidade que representa a tabela TBL_TAN_RET_REQUISICAO
 *  
 *  @author Jorge Abreu
 *  Criado em: 23/10/2007
 */
public class TanRetornoRequisicao implements Serializable
{
	private static final long serialVersionUID = -7679293526049641105L;

	/**
	 * Codigo de retorno da requisicao do Tangram.
	 */
	private int codRetorno;
	
	/**
	 * Descricao do retorno da requisicao.
	 */
	private String desRetorno;	

	/**
	 * Obt�m o c�digo de retorno da requisicao.
	 */
	public int getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * Define o c�digo de retorno da requisicao.
	 */
	public void setCodRetorno(int codRetorno) 
	{
		this.codRetorno = codRetorno;
	}
	
	/**
	 * Obt�m a descricao do c�digo de retorno da requisi��o.
	 */
	public String getDesRetorno() 
	{
		return desRetorno;
	}

	/**
	 * Define a descricao do c�digo de retorno da requisi��o.
	 */
	public void setDesRetorno(String desRetorno) 
	{
		this.desRetorno = desRetorno;
	}
}