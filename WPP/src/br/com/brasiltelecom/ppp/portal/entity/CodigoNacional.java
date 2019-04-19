package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Codigo Nacional
 * @author Alberto Magno 
 * @since 30/07/2004
 */
public class CodigoNacional {

	private int idtCodigoNacional;
	private String idtUF;
	private int idtRegiaoOrigem;
	private String idtFilial;
	private int indRegiaoBRT;
	private int idtFuso;

	/**
	 * @return
	 */
	public int getIdtCodigoNacional() {
		return idtCodigoNacional;
	}

	/**
	 * @return
	 */
	public String getIdtFilial() {
		return idtFilial;
	}

	/**
	 * @return
	 */
	public int getIdtFuso() {
		return idtFuso;
	}

	/**
	 * @return
	 */
	public int getIdtRegiaoOrigem() {
		return idtRegiaoOrigem;
	}

	/**
	 * @return
	 */
	public String getIdtUF() {
		return idtUF;
	}

	/**
	 * @return
	 */
	public int getIndRegiaoBRT() {
		return indRegiaoBRT;
	}

	/**
	 * @param i
	 */
	public void setIdtCodigoNacional(int i) {
		idtCodigoNacional = i;
	}

	/**
	 * @param string
	 */
	public void setIdtFilial(String string) {
		idtFilial = string;
	}

	/**
	 * @param i
	 */
	public void setIdtFuso(int i) {
		idtFuso = i;
	}

	/**
	 * @param i
	 */
	public void setIdtRegiaoOrigem(int i) {
		idtRegiaoOrigem = i;
	}

	/**
	 * @param string
	 */
	public void setIdtUF(String string) {
		idtUF = string;
	}

	/**
	 * @param i
	 */
	public void setIndRegiaoBRT(int i) {
		indRegiaoBRT = i;
	}
}
