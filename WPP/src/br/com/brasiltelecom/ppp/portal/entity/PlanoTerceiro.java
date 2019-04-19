package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

/**
 * Entidade da <code>TBL_APR_PLANO_TERCEIRO</code>
 * 
 * @autor Lucas Mindello de Andrade
 * @since Jul 18, 2008
 */
public class PlanoTerceiro implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String planoTerceiro;
	private String descricao;
	
	public PlanoTerceiro()
	{
		
	}

	/**
	 * Recupera descrição do plano terceiro
	 * @return
	 */
	public String getDescricao()
	{
		return descricao;
	}

	/**
	 * Atribui descrição do plano terceiro.
	 * @param descricao
	 */
	public void setDescricao(String descricao) 
	{
		this.descricao = descricao;
	}

	/**
	 * Recupera identificador do plano terceiro.
	 * @return
	 */
	public String getPlanoTerceiro() {
		return planoTerceiro;
	}

	/**
	 * Atribui identificador do plano terceiro.
	 * @param planoTerceiro
	 */
	public void setPlanoTerceiro(String planoTerceiro)
	{
		this.planoTerceiro = planoTerceiro;
	}	
}
