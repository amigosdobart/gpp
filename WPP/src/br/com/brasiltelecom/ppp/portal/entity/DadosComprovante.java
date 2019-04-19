/*
 * Created on 19/04/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * Modela a tabela de dados de comprovante
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class DadosComprovante {
	private Date datRequisicao;
	private String idtMsisdn;
	private Date datPeriodoInicial;
	private Date datPeriodoFinal;
	private String nomCliente;
	private String desEndereco;
	private String desNumero;
	private String desComplemento;
	private String desBairro;
	private String desCidade;
	private String desUf;
	private String desCep;
	private String idtStatusProcessamento;
	/**
	 * @return
	 */
	public Date getDatPeriodoFinal() {
		return datPeriodoFinal;
	}

	/**
	 * @return
	 */
	public Date getDatPeriodoInicial() {
		return datPeriodoInicial;
	}

	/**
	 * @return
	 */
	public Date getDatRequisicao() {
		return datRequisicao;
	}

	/**
	 * @return
	 */
	public String getDesBairro() {
		return desBairro;
	}

	/**
	 * @return
	 */
	public String getDesCep() {
		return desCep;
	}

	/**
	 * @return
	 */
	public String getDesCidade() {
		return desCidade;
	}

	/**
	 * @return
	 */
	public String getDesComplemento() {
		return desComplemento;
	}

	/**
	 * @return
	 */
	public String getDesEndereco() {
		return desEndereco;
	}

	/**
	 * @return
	 */
	public String getDesNumero() {
		return desNumero;
	}

	/**
	 * @return
	 */
	public String getDesUf() {
		return desUf;
	}

	/**
	 * @return
	 */
	public String getIdtMsisdn() {
		return idtMsisdn;
	}

	/**
	 * @return
	 */
	public String getNomCliente() {
		return nomCliente;
	}

	/**
	 * @param date
	 */
	public void setDatPeriodoFinal(Date date) {
		datPeriodoFinal = date;
	}

	/**
	 * @param date
	 */
	public void setDatPeriodoInicial(Date date) {
		datPeriodoInicial = date;
	}

	/**
	 * @param date
	 */
	public void setDatRequisicao(Date date) {
		datRequisicao = date;
	}

	/**
	 * @param string
	 */
	public void setDesBairro(String string) {
		desBairro = string;
	}

	/**
	 * @param string
	 */
	public void setDesCep(String string) {
		desCep = string;
	}

	/**
	 * @param string
	 */
	public void setDesCidade(String string) {
		desCidade = string;
	}

	/**
	 * @param string
	 */
	public void setDesComplemento(String string) {
		desComplemento = string;
	}

	/**
	 * @param string
	 */
	public void setDesEndereco(String string) {
		desEndereco = string;
	}

	/**
	 * @param string
	 */
	public void setDesNumero(String string) {
		desNumero = string;
	}

	/**
	 * @param string
	 */
	public void setDesUf(String string) {
		desUf = string;
	}

	/**
	 * @param string
	 */
	public void setIdtMsisdn(String string) {
		idtMsisdn = string;
	}

	/**
	 * @param string
	 */
	public void setNomCliente(String string) {
		nomCliente = string;
	}

	/**
	 * @return
	 */
	public String getIdtStatusProcessamento() {
		return idtStatusProcessamento;
	}

	/**
	 * @param string
	 */
	public void setIdtStatusProcessamento(String string) {
		idtStatusProcessamento = string;
	}

}
