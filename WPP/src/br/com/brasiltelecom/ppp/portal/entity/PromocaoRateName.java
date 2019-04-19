/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author Luciano Vilela
 *
 */
public class PromocaoRateName {
	private int idPromocao;
	private String rateName;
	/**
	 * @return
	 */
	public int getIdPromocao() {
		return idPromocao;
	}

	/**
	 * @return
	 */
	public String getRateName() {
		return rateName;
	}

	/**
	 * @param i
	 */
	public void setIdPromocao(int i) {
		idPromocao = i;
	}

	/**
	 * @param string
	 */
	public void setRateName(String string) {
		rateName = string;
	}

}
