/*
 * Created on 23/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.*;

/**
 * Modela a tabela de motivo de contestacao
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class MotivoContestacao {

	private long idMotivoContestacao; 
	private String desMotivoContestacao;
	private String idUsuario;
	private Date idtDatCadastro;
	private int indAtivo; 
	private char[] dicas;
	
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
	
	public int getIndAtivo()
	{
		return indAtivo;
	}
	public char[] getDicas()
	{
		return dicas;
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

	public void setIndAtivo(int indAtivo)
	{
		this.indAtivo = indAtivo;
	}
	
	public void setDicas(char[] dicas)
	{
		this.dicas = dicas;
	}
}
