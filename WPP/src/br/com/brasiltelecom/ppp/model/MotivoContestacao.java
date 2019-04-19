/*
 * Created on 04/05/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

import java.util.Date;

/**
 * Modela as informa��es sobre Motivo de Contesta��o
 * @author Alex Pitacci Sim�es
 * @since 21/05/2004
 */
public class MotivoContestacao {

	private long idMotivoContestacao;
	private String desMotivoContestacao;
	private String idUsuario;
	private Date idtDatCadastro;

	/**
	 * @return
	 */
	public String getDesMotivoContestacao() {
		return desMotivoContestacao;
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
	public Date getIdtDatCadastro() {
		return idtDatCadastro;
	}

	/**
	 * @return
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param string
	 */
	public void setDesMotivoContestacao(String string) {
		desMotivoContestacao = string;
	}

	/**
	 * @param l
	 */
	public void setIdMotivoContestacao(long l) {
		idMotivoContestacao = l;
	}

	/**
	 * @param date
	 */
	public void setIdtDatCadastro(Date date) {
		idtDatCadastro = date;
	}

	/**
	 * @param string
	 */
	public void setIdUsuario(String string) {
		idUsuario = string;
	}

}
