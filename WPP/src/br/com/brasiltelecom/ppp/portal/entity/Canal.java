/*
 * Created on 24/03/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de canal
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Canal {

	private String idCanal;
	private String descCanal;
	/**
	 * @return
	 */
	public String getDescCanal() {
		return descCanal;
	}

	/**
	 * @return
	 */
	public String getIdCanal() {
		return idCanal;
	}

	/**
	 * @param string
	 */
	public void setDescCanal(String string) {
		descCanal = string;
	}

	/**
	 * @param string
	 */
	public void setIdCanal(String string) {
		idCanal = string;
	}

}
