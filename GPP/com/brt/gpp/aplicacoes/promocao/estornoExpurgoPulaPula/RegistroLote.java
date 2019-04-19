package com.brt.gpp.aplicacoes.promocao.estornoExpurgoPulaPula;

import java.sql.Date;

/**
 * Representa uma linha do arquivo de lotes.
 * 
 * @author Bernardo Dias
 * @since 09/01/2007
 */
public class RegistroLote {

	private String tipo; 			// Tipo de origem
	private String msisdn;			// MSISDN do usuario
	private String telOrigem;		// Telefone de origem
	private Date dataReferencia;	// Data da ligação
	private String lote;			// Identificação do lote
	
	public Date getDataReferencia() {
		return dataReferencia;
	}
	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getTelOrigem() {
		return telOrigem;
	}
	public void setTelOrigem(String telOrigem) {
		this.telOrigem = telOrigem;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
