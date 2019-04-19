/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author BT024318
 *
 */
public class PromocaoPlanoPreco {
	private int idPromocao;
	private int idPlanoPreco;
	/**
	 * @return
	 */
	public int getIdPlanoPreco() {
		return idPlanoPreco;
	}

	/**
	 * @return
	 */
	public int getIdPromocao() {
		return idPromocao;
	}

	/**
	 * @param i
	 */
	public void setIdPlanoPreco(int i) {
		idPlanoPreco = i;
	}

	/**
	 * @param i
	 */
	public void setIdPromocao(int i) {
		idPromocao = i;
	}

}
