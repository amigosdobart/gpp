/*
 * Created on 01/10/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Collection;


/**
 * @author Luciano Vilela
 *
 */
public class PlanoPreco {
	
	private String idPlano;
	private String descricao;
	private Categoria categoria;
	private Collection motivosContestacao;

	/**
	 * @return Returns the objCategoria.
	 */
	/**
	 * @return
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return
	 */
	public String getIdPlano() {
		return idPlano;
	}
	
	public Collection getMotivosContestacao()
	{
		return motivosContestacao;
	}
	/**
	 * @param string
	 */
	public void setCategoria(Categoria string) {
		categoria = string;
	}

	/**
	 * @param string
	 */
	public void setDescricao(String string) {
		descricao = string;
	}

	/**
	 * @param i
	 */
	public void setIdPlano(String i) {
		idPlano = i;
	}
	
	public void setMotivosContestacao(Collection motivosContestacao)
	{
		this.motivosContestacao = motivosContestacao;
	}

}
