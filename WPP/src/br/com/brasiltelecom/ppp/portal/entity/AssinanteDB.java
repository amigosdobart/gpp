package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
/**
 */
public class AssinanteDB {
	 private Date dataUltimaRecarga ;
	 private Date dataCongelamento ;
	 private String msisdn ;
	 private String imisi ;
	 private Date dataAtivacao ;
	 private PlanoPreco planoPreco;

	/**
	 * @return
	 */
	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	/**
	 * @return
	 */
	public Date getDataCongelamento() {
		return dataCongelamento;
	}

	/**
	 * @return
	 */
	public Date getDataUltimaRecarga() {
		return dataUltimaRecarga;
	}

	/**
	 * @return
	 */
	public String getImisi() {
		return imisi;
	}

	/**
	 * @return
	 */
	public String getMsisdn() {
		return msisdn;
	}

	public PlanoPreco getPlanoPreco()
	{
		return planoPreco;
	}
	
	/**
	 * @param date
	 */
	public void setDataAtivacao(Date date) {
		dataAtivacao = date;
	}

	/**
	 * @param date
	 */
	public void setDataCongelamento(Date date) {
		dataCongelamento = date;
	}

	/**
	 * @param date
	 */
	public void setDataUltimaRecarga(Date date) {
		dataUltimaRecarga = date;
	}

	/**
	 * @param string
	 */
	public void setImisi(String string) {
		imisi = string;
	}

	/**
	 * @param string
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}
	
	public void setPlanoPreco(PlanoPreco planoPreco)
	{
		this.planoPreco = planoPreco;
	}

}
