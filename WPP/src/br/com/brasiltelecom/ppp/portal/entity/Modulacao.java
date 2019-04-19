/*
 * Created on 29/04/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de modulacao
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Modulacao {

	private String idModulacao;
	private String desModulacao;
	
	/**
	 * @return
	 */
	public String getDesModulacao() {
		return desModulacao;
	}

	/**
	 * @return
	 */
	public String getIdModulacao() {
		return idModulacao;
	}

	/**
	 * @param string
	 */
	public void setDesModulacao(String string) {
		desModulacao = string;
	}

	/**
	 * @param string
	 */
	public void setIdModulacao(String string) {
		idModulacao = string;
	}

}
