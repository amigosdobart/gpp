/*
 * Created on 22/04/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de canal / origem de bs
 * @author Alberto Magno
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
	 * @param l
	 */
	public void setIdCanalOrigemBS(int l) {
		idCanalOrigemBS = l;
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
