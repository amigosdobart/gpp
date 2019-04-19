/*
 * Created on 24/02/2005
 *
 */

package com.brt.gpp.aplicacoes.importacaoEstoqueSap;
import java.util.Date;

/**
 * @author Henrique Canto
 * @description: Classe que representa as informações que serão retiradas
 * do campo dados da TBL_INT_ETI_IN e inseridas na HSID_ESTOQUE_SAP. 
 */
public class DadosEstoqueSap {
	
	private Date dataImportacao;
	private String imei; 
	private String status;
	private String codigoLoja;
	private long idProcessamento;
	
	/**
	 * @return Returns the codigoLoja.
	 */
	public String getCodigoLoja() {
		return codigoLoja;
	}
	/**
	 * @param codigoLoja The codigoLoja to set.
	 */
	public void setCodigoLoja(String codigoLoja) {
		this.codigoLoja = codigoLoja;
	}
	/**
	 * @return Returns the dataImportacao.
	 */
	public Date getDataImportacao() {
		return dataImportacao;
	}
	/**
	 * @param dataImportacao The dataImportacao to set.
	 */
	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}
	/**
	 * @return Returns the imei.
	 */
	public String getImei() {
		return imei;
	}
	/**
	 * @param imei The imei to set.
	 */
	public void setImei(String imei) {
		this.imei = imei;
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
	/**
	 * @return Returns the codigoLoja.
	 */
	public long getIdProcessamento() {
		return idProcessamento;
	}
	/**
	 * @param codigoLoja The codigoLoja to set.
	 */
	public void setIdProcessamento(long idProcessamento) {
		this.idProcessamento = idProcessamento;
	}
}
