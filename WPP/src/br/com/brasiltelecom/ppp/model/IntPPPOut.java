/*
 * Created on 27/04/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

import java.util.*;

/**
 * Modela as informações sobre IntPPPOut
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class IntPPPOut {

	private long idProcessamento;          
	private Date datCadastro;              
	private String idtEventoNegocio;      
	private char[] xmlDocument;            
	private String idtStatusProcessamento;

	/**
	 * @return
	 */
	public Date getDatCadastro() {
		return datCadastro;
	}

	/**
	 * @return
	 */
	public long getIdProcessamento() {
		return idProcessamento;
	}

	/**
	 * @return
	 */
	public String getIdtEventoNegocio() {
		return idtEventoNegocio;
	}

	/**
	 * @return
	 */
	public String getIdtStatusProcessamento() {
		return idtStatusProcessamento;
	}

	/**
	 * @return
	 */
	public char[] getXmlDocument() {
		return xmlDocument;
	}

	/**
	 * @param date
	 */
	public void setDatCadastro(Date date) {
		datCadastro = date;
	}

	/**
	 * @param l
	 */
	public void setIdProcessamento(long l) {
		idProcessamento = l;
	}

	/**
	 * @param string
	 */
	public void setIdtEventoNegocio(String string) {
		idtEventoNegocio = string;
	}

	/**
	 * @param string
	 */
	public void setIdtStatusProcessamento(String string) {
		idtStatusProcessamento = string;
	}

	/**
	 * @param cs
	 */
	public void setXmlDocument(char[] cs) {
		xmlDocument = cs;
	}

}
