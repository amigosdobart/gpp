/*
 * Created on 26/04/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

import java.util.*;

/**
 * Modela as informações sobre Item Contestação
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class ItemContestacao {

	private String subId;
	private Date timestamp;	
	private long startTime;
	private String callId;
	private int transactionType;	
	private String numeroBS;
	private long idItemContestacao;	
	private String operadorSfa;	
	private String desParecer;
	private Date datFechamento;	
	private double vlrAjustadoPrincipal;
	private double vlrAjustadoBonus;
	private double vlrAjustadoSms;
	private double vlrAjustadoDados;
	private double vlrContestadoPrincipal;
	private double vlrContestadoBonus;
	private double vlrContestadoSms;
	private double vlrContestadoDados;
	private String idtStatusItemContestacao;
    private String tipoTarifacao;
    private long codigoServico;
    private long Duracao;

	/**
	 * @return
	 */
	public String getCallId() {
		return callId;
	}

	/**
	 * @return
	 */
	public long getCodigoServico() {
		return codigoServico;
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
	public String getDesParecer() {
		return desParecer;
	}

	/**
	 * @return
	 */
	public long getDuracao() {
		return Duracao;
	}

	/**
	 * @return
	 */
	public long getIdItemContestacao() {
		return idItemContestacao;
	}

	/**
	 * @return
	 */
	public String getIdtStatusItemContestacao() {
		return idtStatusItemContestacao;
	}

	/**
	 * @return
	 */
	public String getNumeroBS() {
		return numeroBS;
	}

	/**
	 * @return
	 */
	public String getOperadorSfa() {
		return operadorSfa;
	}

	/**
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return
	 */
	public String getSubId() {
		return subId;
	}

	/**
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @return
	 */
	public String getTipoTarifacao() {
		return tipoTarifacao;
	}

	/**
	 * @return
	 */
	public int getTransactionType() {
		return transactionType;
	}

	
	
	/**
	 * @return Returns the vlrAjustadoBonus.
	 */
	public double getVlrAjustadoBonus() {
		return vlrAjustadoBonus;
	}
	/**
	 * @return Returns the vlrAjustadoDados.
	 */
	public double getVlrAjustadoDados() {
		return vlrAjustadoDados;
	}
	/**
	 * @return Returns the vlrAjustadoPrincipal.
	 */
	public double getVlrAjustadoPrincipal() {
		return vlrAjustadoPrincipal;
	}
	/**
	 * @return Returns the vlrAjustadoSms.
	 */
	public double getVlrAjustadoSms() {
		return vlrAjustadoSms;
	}
	/**
	 * @return Returns the vlrContestadoBonus.
	 */
	public double getVlrContestadoBonus() {
		return vlrContestadoBonus;
	}
	/**
	 * @return Returns the vlrContestadoDados.
	 */
	public double getVlrContestadoDados() {
		return vlrContestadoDados;
	}
	/**
	 * @return Returns the vlrContestadoPrincipal.
	 */
	public double getVlrContestadoPrincipal() {
		return vlrContestadoPrincipal;
	}
	/**
	 * @return Returns the vlrContestadoSms.
	 */
	public double getVlrContestadoSms() {
		return vlrContestadoSms;
	}

	/**
	 * @param string
	 */
	public void setCallId(String string) {
		callId = string;
	}

	/**
	 * @param l
	 */
	public void setCodigoServico(long l) {
		codigoServico = l;
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
	public void setDesParecer(String string) {
		desParecer = string;
	}

	/**
	 * @param l
	 */
	public void setDuracao(long l) {
		Duracao = l;
	}

	/**
	 * @param l
	 */
	public void setIdItemContestacao(long l) {
		idItemContestacao = l;
	}

	/**
	 * @param string
	 */
	public void setIdtStatusItemContestacao(String string) {
		idtStatusItemContestacao = string;
	}

	/**
	 * @param string
	 */
	public void setNumeroBS(String string) {
		numeroBS = string;
	}

	/**
	 * @param string
	 */
	public void setOperadorSfa(String string) {
		operadorSfa = string;
	}

	/**
	 * @param l
	 */
	public void setStartTime(long l) {
		startTime = l;
	}

	/**
	 * @param string
	 */
	public void setSubId(String string) {
		subId = string;
	}

	/**
	 * @param date
	 */
	public void setTimestamp(Date date) {
		timestamp = date;
	}

	/**
	 * @param string
	 */
	public void setTipoTarifacao(String string) {
		tipoTarifacao = string;
	}

	/**
	 * @param i
	 */
	public void setTransactionType(int i) {
		transactionType = i;
	}

	/**
	 * @param vlrAjustadoBonus The vlrAjustadoBonus to set.
	 */
	public void setVlrAjustadoBonus(double vlrAjustadoBonus) {
		this.vlrAjustadoBonus = vlrAjustadoBonus;
	}
	/**
	 * @param vlrAjustadoDados The vlrAjustadoDados to set.
	 */
	public void setVlrAjustadoDados(double vlrAjustadoDados) {
		this.vlrAjustadoDados = vlrAjustadoDados;
	}
	/**
	 * @param vlrAjustadoPrincipal The vlrAjustadoPrincipal to set.
	 */
	public void setVlrAjustadoPrincipal(double vlrAjustadoPrincipal) {
		this.vlrAjustadoPrincipal = vlrAjustadoPrincipal;
	}
	/**
	 * @param vlrAjustadoSms The vlrAjustadoSms to set.
	 */
	public void setVlrAjustadoSms(double vlrAjustadoSms) {
		this.vlrAjustadoSms = vlrAjustadoSms;
	}
	/**
	 * @param vlrContestadoBonus The vlrContestadoBonus to set.
	 */
	public void setVlrContestadoBonus(double vlrContestadoBonus) {
		this.vlrContestadoBonus = vlrContestadoBonus;
	}
	/**
	 * @param vlrContestadoDados The vlrContestadoDados to set.
	 */
	public void setVlrContestadoDados(double vlrContestadoDados) {
		this.vlrContestadoDados = vlrContestadoDados;
	}
	/**
	 * @param vlrContestadoPrincipal The vlrContestadoPrincipal to set.
	 */
	public void setVlrContestadoPrincipal(double vlrContestadoPrincipal) {
		this.vlrContestadoPrincipal = vlrContestadoPrincipal;
	}
	/**
	 * @param vlrContestadoSms The vlrContestadoSms to set.
	 */
	public void setVlrContestadoSms(double vlrContestadoSms) {
		this.vlrContestadoSms = vlrContestadoSms;
	}

}
