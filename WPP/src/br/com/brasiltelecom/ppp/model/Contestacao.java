package br.com.brasiltelecom.ppp.model;

import java.util.Date;

/**
 * Modela as informações de Contestação
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Contestacao {
	
	private String numeroBS;
	//private String status;
	private Date datAbertura;
	private Date datFechamento;
	private String idtMsisdn;
	private String idUsuario;
	private long idMotivoContestacao;
	private String desContestacao;
	private long idCanalOrigemBS;
	private String idtStatusAnalise;
	private ItemContestacao itemContestacao;
	private String vlrContestacao;
	private String vlrAjuste;
	
	/**
	 * @return
	 */
	public Date getDatAbertura() {
		return datAbertura;
	}

	/**
	 * @return
	 */
	public Date getDatFechamento() {
		return datFechamento;
	}

	/**
	 * @return
	 */
	public String getDesContestacao() {
		return desContestacao;
	}

	/**
	 * @return
	 */
	public long getIdCanalOrigemBS() {
		return idCanalOrigemBS;
	}

	/**
	 * @return
	 */
	public long getIdMotivoContestacao() {
		return idMotivoContestacao;
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
	public String getIdtStatusAnalise() {
		return idtStatusAnalise;
	}

	/**
	 * @return
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @return
	 */
	public String getNumeroBS() {
		return numeroBS;
	}

	/**
	 * @param date
	 */
	public void setDatAbertura(Date date) {
		datAbertura = date;
	}

	/**
	 * @param date
	 */
	public void setDatFechamento(Date date) {
		datFechamento = date;
	}

	/**
	 * @param string
	 */
	public void setDesContestacao(String string) {
		desContestacao = string;
	}

	/**
	 * @param l
	 */
	public void setIdCanalOrigemBS(long l) {
		idCanalOrigemBS = l;
	}

	/**
	 * @param l
	 */
	public void setIdMotivoContestacao(long l) {
		idMotivoContestacao = l;
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
	public void setIdtStatusAnalise(String string) {
		idtStatusAnalise = string;
	}

	/**
	 * @param string
	 */
	public void setIdUsuario(String string) {
		idUsuario = string;
	}

	/**
	 * @param string
	 */
	public void setNumeroBS(String string) {
		numeroBS = string;
	}

	/**
	 * @return
	 */
	public ItemContestacao getItemContestacao() {
		return itemContestacao;
	}

	/**
	 * @param contestacao
	 */
	public void setItemContestacao(ItemContestacao contestacao) {
		itemContestacao = contestacao;
	}

	/**
	 * @return
	 */
	public String getVlrAjuste() {
		return vlrAjuste;
	}

	/**
	 * @return
	 */
	public String getVlrContestacao() {
		return vlrContestacao;
	}

	/**
	 * @param string
	 */
	public void setVlrAjuste(String string) {
		vlrAjuste = string;
	}

	/**
	 * @param string
	 */
	public void setVlrContestacao(String string) {
		vlrContestacao = string;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		if(datFechamento != null) 
		   return "Fechado";
		else
		   return "Aberto";
	}

	/**
	 * @param string
	 */
	public void setStatus(String string) {
		//status = string;
	}

}
