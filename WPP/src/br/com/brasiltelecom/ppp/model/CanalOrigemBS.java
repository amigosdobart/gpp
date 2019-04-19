/*
 * Created on 04/05/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações de Canal de Origem
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class CanalOrigemBS {

	private int idCanalOrigemBS;
	private String desCanalOrigemBS;
	private String desMensagem;

	/**
	 * @return
	 */
	public String getDesCanalOrigemBS() {
		return desCanalOrigemBS;
	}

	/**
	 * @return
	 */
	public int getIdCanalOrigemBS() {
		return idCanalOrigemBS;
	}

	/**
	 * @param string
	 */
	public void setDesCanalOrigemBS(String string) {
		desCanalOrigemBS = string;
	}

	/**
	 * @param i
	 */
	public void setIdCanalOrigemBS(int i) {
		idCanalOrigemBS = i;
	}

	/**
	 * @return
	 */
	public String getDesMensagem() {
		return desMensagem;
	}

	/**
	 * @param string
	 */
	public void setDesMensagem(String string) {
		desMensagem = string;
	}

}
