/*
 * Created on 07/06/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author df070234
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConfigAssinante {

	private String tipo;
	private int idConfig;
	private String descricao;
	private int hibrido;
	/**
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return
	 */
	public int getHibrido() {
		return hibrido;
	}

	/**
	 * @return
	 */
	public int getIdConfig() {
		return idConfig;
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
	public void setDescricao(String string) {
		descricao = string;
	}

	/**
	 * @param i
	 */
	public void setHibrido(int i) {
		hibrido = i;
	}

	/**
	 * @param i
	 */
	public void setIdConfig(int i) {
		idConfig = i;
	}

	/**
	 * @param string
	 */
	public void setTipo(String string) {
		tipo = string;
	}

}
