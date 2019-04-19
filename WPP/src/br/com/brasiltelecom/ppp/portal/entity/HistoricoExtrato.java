/*
 * Created on 24/03/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Modela a tabela de historico de extrato
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class HistoricoExtrato {

	private int idHistoricoExtrato;
	private String msisdn;
	private String emissor;
	private int cobrado;
	private String usuario;
	private Date data;
	private TipoExtrato tipoExtrato;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * @return
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return
	 */
	public String getEmissor() {
		return emissor;
	}

	/**
	 * @return
	 */
	public int getIdHistoricoExtrato() {
		return idHistoricoExtrato;
	}

	/**
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param date
	 */
	public void setData(Date date) {
		data = date;
	}

	/**
	 * @param string
	 */
	public void setEmissor(String string) {
		emissor = string;
	}

	/**
	 * @param i
	 */
	public void setIdHistoricoExtrato(int i) {
		idHistoricoExtrato = i;
	}

	/**
	 * @param string
	 */
	public void setUsuario(String string) {
		usuario = string;
	}

	/**
	 * @return
	 */
	public TipoExtrato getTipoExtrato() {
		return tipoExtrato;
	}

	/**
	 * @param extrato
	 */
	public void setTipoExtrato(TipoExtrato extrato) {
		tipoExtrato = extrato;
	}

	public String getDataString(){
		return sdf.format(data);
	}
	/**
	 * @return
	 */
	public int getCobrado() {
		return cobrado;
	}

	/**
	 * @param i
	 */
	public void setCobrado(int i) {
		cobrado = i;
	}

	/**
	 * @return
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param string
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

}
