/*
 * Created on 11/05/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela tabela de ratings
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Rating {

	private String idRateName;
	private String desOperacao;
	private String tipo;
	private char idtTarifavel;
	/**
	 * @return
	 */
	public String getDesOperacao() {
		return desOperacao;
	}

	/**
	 * @return
	 */
	public String getIdRateName() {
		return idRateName;
	}

	/**
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param string
	 */
	public void setDesOperacao(String string) {
		desOperacao = string;
	}

	/**
	 * @param string
	 */
	public void setIdRateName(String string) {
		idRateName = string;
	}

	/**
	 * @param string
	 */
	public void setTipo(String string) {
		tipo = string;
	}

	/**
	 * @return Returns the idtTarifavel.
	 */
	public char getIdtTarifavel() {
		return idtTarifavel;
	}
	/**
	 * @param idtTarifavel The idtTarifavel to set.
	 */
	public void setIdtTarifavel(char idtTarifavel) {
		this.idtTarifavel = idtTarifavel;
	}
}
