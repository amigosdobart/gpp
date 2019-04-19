/*
 * Created on 19/07/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * @author André Gonçalves
 * @since 19/07/2004
 */
public class StatusBloqueioAssinante {

	private String msisdn;
	private char evento;
	private Date dataEvento;
	private char statusProcessamento;
	private String servico;
	private String status;
	
	
	/**
	 * @return Returns the dataEvento.
	 */
	public Date getDataEvento() {
		return dataEvento;
	}
	/**
	 * @param dataEvento The dataEvento to set.
	 */
	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}
	/**
	 * @return Returns the evento.
	 */
	public char getEvento() {
		return evento;
	}
	/**
	 * @param evento The evento to set.
	 */
	public void setEvento(char evento) {
		this.evento = evento;
	}
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	/**
	 * @return Returns the servico.
	 */
	public String getServico() {
		return servico;
	}
	/**
	 * @param servico The servico to set.
	 */
	public void setServico(String servico) {
		this.servico = servico;
	}
	/**
	 * @return Returns the statusProcessamento.
	 */
	public char getStatusProcessamento() {
		return statusProcessamento;
	}
	/**
	 * @param statusProcessamento The statusProcessamento to set.
	 */
	public void setStatusProcessamento(char statusProcessamento) {
		this.statusProcessamento = statusProcessamento;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
