
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * TODO Descrição
 * @author Alex Pitacci Simões
 * @since 27/07/2004
 */
public class BloqueioServico {

	private String msisdn;
	private Date dataBloqueio;
	private String servico;
	private String usuario;
	
	
	
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
}
