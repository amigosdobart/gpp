/*
 * Created on 14/07/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * @author André Gonçalves
 * @since 14/07/2004
 */
public class EventoAprovisionamento {

	private Date dataAprovisionamento;
	private String msisdn;
	private String tipoOperacao;
	private String idMsi;
	private String idPlanoPreco;
	private long valorCreditoInicial;
	private Integer idIdioma;
	private String idAntigoCampo;
	private String idNovoCampo;
	private String idTarifa;
	private String idDescricaoLista;
	private String idMotivo;
	private String nomeOperador;
	private String descricaoStatus;
	private Integer codigoRetorno;
	
	/**
	 * @return Returns the codigoRetorno.
	 */
	public Integer getCodigoRetorno() {
		return codigoRetorno;
	}
	/**
	 * @param codigoRetorno The codigoRetorno to set.
	 */
	public void setCodigoRetorno(Integer codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}
	/**
	 * @return Returns the dataAprovisionamento.
	 */
	public Date getDataAprovisionamento() {
		return dataAprovisionamento;
	}
	/**
	 * @param dataAprovisionamento The dataAprovisionamento to set.
	 */
	public void setDataAprovisionamento(Date dataAprovisionamento) {
		this.dataAprovisionamento = dataAprovisionamento;
	}
	/**
	 * @return Returns the descricaoStatus.
	 */
	public String getDescricaoStatus() {
		return descricaoStatus;
	}
	/**
	 * @param descricaoStatus The descricaoStatus to set.
	 */
	public void setDescricaoStatus(String descricaoStatus) {
		this.descricaoStatus = descricaoStatus;
	}
	/**
	 * @return Returns the idAntigoCampo.
	 */
	public String getIdAntigoCampo() {
		return idAntigoCampo;
	}
	/**
	 * @param idAntigoCampo The idAntigoCampo to set.
	 */
	public void setIdAntigoCampo(String idAntigoCampo) {
		this.idAntigoCampo = idAntigoCampo;
	}
	/**
	 * @return Returns the idDescricaoLista.
	 */
	public String getIdDescricaoLista() {
		return idDescricaoLista;
	}
	/**
	 * @param idDescricaoLista The idDescricaoLista to set.
	 */
	public void setIdDescricaoLista(String idDescricaoLista) {
		this.idDescricaoLista = idDescricaoLista;
	}
	/**
	 * @return Returns the idIdioma.
	 */
	public Integer getIdIdioma() {
		return idIdioma;
	}
	/**
	 * @param idIdioma The idIdioma to set.
	 */
	public void setIdIdioma(Integer idIdioma) {
		this.idIdioma = idIdioma;
	}
	/**
	 * @return Returns the idMotivo.
	 */
	public String getIdMotivo() {
		return idMotivo;
	}
	/**
	 * @param idMotivo The idMotivo to set.
	 */
	public void setIdMotivo(String idMotivo) {
		this.idMotivo = idMotivo;
	}
	/**
	 * @return Returns the idMsi.
	 */
	public String getIdMsi() {
		return idMsi;
	}
	/**
	 * @param idMsi The idMsi to set.
	 */
	public void setIdMsi(String idMsi) {
		this.idMsi = idMsi;
	}
	/**
	 * @return Returns the idNovoCampo.
	 */
	public String getIdNovoCampo() {
		return idNovoCampo;
	}
	/**
	 * @param idNovoCampo The idNovoCampo to set.
	 */
	public void setIdNovoCampo(String idNovoCampo) {
		this.idNovoCampo = idNovoCampo;
	}
	/**
	 * @return Returns the idPlanoPreco.
	 */
	public String getIdPlanoPreco() {
		return idPlanoPreco;
	}
	/**
	 * @param idPlanoPreco The idPlanoPreco to set.
	 */
	public void setIdPlanoPreco(String idPlanoPreco) {
		this.idPlanoPreco = idPlanoPreco;
	}
	/**
	 * @return Returns the idTarifa.
	 */
	public String getIdTarifa() {
		return idTarifa;
	}
	/**
	 * @param idTarifa The idTarifa to set.
	 */
	public void setIdTarifa(String idTarifa) {
		this.idTarifa = idTarifa;
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
	 * @return Returns the nomeOperador.
	 */
	public String getNomeOperador() {
		return nomeOperador;
	}
	/**
	 * @param nomeOperador The nomeOperador to set.
	 */
	public void setNomeOperador(String nomeOperador) {
		this.nomeOperador = nomeOperador;
	}
	/**
	 * @return Returns the tipoOperacao.
	 */
	public String getTipoOperacao() {
		return tipoOperacao;
	}
	/**
	 * @param tipoOperacao The tipoOperacao to set.
	 */
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	/**
	 * @return Returns the valorCreditoInicial.
	 */
	public long getValorCreditoInicial() {
		return valorCreditoInicial;
	}
	/**
	 * @param valorCreditoInicial The valorCreditoInicial to set.
	 */
	public void setValorCreditoInicial(long valorCreditoInicial) {
		this.valorCreditoInicial = valorCreditoInicial;
	}
}
