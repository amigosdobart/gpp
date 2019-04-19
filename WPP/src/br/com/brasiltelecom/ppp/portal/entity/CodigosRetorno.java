/*
 * Created on 27/09/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

/**
 * @author Henrique Canto
 */
public class CodigosRetorno implements Serializable
{
	private String idRetorno;
	private String vlrRetorno;
	private String descRetorno;


	public String getDescRetorno() {
		return descRetorno;
	}
	public void setDescRetorno(String descRetorno) {
		this.descRetorno = descRetorno;
	}
	public String getIdRetorno() {
		return idRetorno;
	}
	public void setIdRetorno(String idRetorno) {
		this.idRetorno = idRetorno;
	}
	public String getVlrRetorno() {
		return vlrRetorno;
	}
	public void setVlrRetorno(String vlrRetorno) {
		this.vlrRetorno = vlrRetorno;
	}
}
