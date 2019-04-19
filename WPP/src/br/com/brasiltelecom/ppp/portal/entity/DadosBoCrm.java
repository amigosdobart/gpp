package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author ex352341
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DadosBoCrm {

	private long idAtividade;
	private Date datBo;
	private String numBo;

	private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	
	public DadosBoCrm()
	{	
	}
	
	/**
	 * @return Returns the datBo.
	 */
	public Date getDatBo() {
		return datBo;
	}
	/**
	 * @return Returns the datBo.
	 */
	public String getDatBoString() {
		return formataData.format(getDatBo());
	}
	/**
	 * @param datBo The datBo to set.
	 */
	public void setDatBo(Date datBo) {
		this.datBo = datBo;
	}
	/**
	 * @return Returns the idAtividade.
	 */
	public long getIdAtividade() {
		return idAtividade;
	}
	/**
	 * @param idAtividade The idAtividade to set.
	 */
	public void setIdAtividade(long idAtividade) {
		this.idAtividade = idAtividade;
	}
	/**
	 * @return Returns the numBo.
	 */
	public String getNumBo() {
		return numBo;
	}
	/**
	 * @param numBo The numBo to set.
	 */
	public void setNumBo(String numBo) {
		this.numBo = numBo;
	}
}