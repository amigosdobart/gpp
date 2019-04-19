/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author BT024318
 *
 */
public class PromocaoTransactionType {
	private int idPromocao;
	private int idTransactionType;
	
	/**
	 * @return
	 */
	public int getIdPromocao() {
		return idPromocao;
	}

	/**
	 * @return
	 */
	public int getIdTransactionType() {
		return idTransactionType;
	}

	/**
	 * @param i
	 */
	public void setIdPromocao(int i) {
		idPromocao = i;
	}

	/**
	 * @param i
	 */
	public void setIdTransactionType(int i) {
		idTransactionType = i;
	}

}
