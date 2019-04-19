/*
 * Created on 16/07/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * @author André Gonçalves
 * @since 16/07/2004
 */
public class AssinanteBloqueado {
	
	private String msisdn;
	private String servico;
	private Date dataBloqueio;
	private String usuario;
	private String motivo;
	private String status;


	/**
	 * @return Returns the dataBloqueio.
	 */
	public Date getDataBloqueio() {
		return dataBloqueio;
	}
	/**
	 * @param dataBloqueio The dataBloqueio to set.
	 */
	public void setDataBloqueio(Date dataBloqueio) {
		this.dataBloqueio = dataBloqueio;
	}
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
