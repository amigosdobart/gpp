/*
 * Created on 28/04/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de Configuração
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Configuracao {

	private String idConfiguracao;
	private String vlrConfiguracao;
	private String desConfiguracao;
	
	
	/**
	 * @return
	 */
	public String getDesConfiguracao() {
		return desConfiguracao;
	}

	/**
	 * @return
	 */
	public String getIdConfiguracao() {
		return idConfiguracao;
	}

	/**
	 * @return
	 */
	public String getVlrConfiguracao() {
		return vlrConfiguracao;
	}

	/**
	 * @param string
	 */
	public void setDesConfiguracao(String string) {
		desConfiguracao = string;
	}

	/**
	 * @param i
	 */
	public void setIdConfiguracao(String i) {
		idConfiguracao = i;
	}

	/**
	 * @param string
	 */
	public void setVlrConfiguracao(String string) {
		vlrConfiguracao = string;
	}

}
