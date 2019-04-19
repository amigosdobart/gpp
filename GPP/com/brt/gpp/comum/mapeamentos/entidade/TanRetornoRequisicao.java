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
	 * Obtém o código de retorno da requisicao.
	 */
	public int getCodRetorno() 
	{
		return codRetorno;
	}

	/**
	 * Define o código de retorno da requisicao.
	 */
	public void setCodRetorno(int codRetorno) 
	{
		this.codRetorno = codRetorno;
	}
	
	/**
	 * Obtém a descricao do código de retorno da requisição.
	 */
	public String getDesRetorno() 
	{
		return desRetorno;
	}

	/**
	 * Define a descricao do código de retorno da requisição.
	 */
	public void setDesRetorno(String desRetorno) 
	{
		this.desRetorno = desRetorno;
	}
}