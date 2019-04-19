/*
 * Created on 30/08/2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author Henrique Canto
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DadosCadastraisCrm {

	private String nomeUsuario ;
	private String numCpf;
	private String numRg;
	private String nomLogradouro;
	private String nomCidade;
	private String nomBairro;
	private long numCep;
	private String idUf;
	private Date datNascimento;
	private String numTelefContato;
	private long idAtividade;
	private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	

	
	/**
	 * @return Returns the datNascimento converted to String.
	 */
	public String getDatNascimentoString() {
		Date dateAux = getDatNascimento();
		if (dateAux!=null)
		{
			return formataData.format(dateAux).toString();
		}
		return "";
	}
	/**
	 * @return Returns the datNascimento.
	 */
	public Date getDatNascimento() {
		return datNascimento;
	}
	/**
	 * @param datNascimento The datNascimento to set.
	 */
	public void setDatNascimento(Date datNascimento) {
		this.datNascimento = datNascimento;
	}
	/**
	 * @return Returns the formataData.
	 */
	public SimpleDateFormat getFormataData() {
		return formataData;
	}
	/**
	 * @param formataData The formataData to set.
	 */
	public void setFormataData(SimpleDateFormat formataData) {
		this.formataData = formataData;
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
	 * @return Returns the idUf.
	 */
	public String getIdUf() {
		return idUf;
	}
	/**
	 * @param idUf The idUf to set.
	 */
	public void setIdUf(String idUf) {
		this.idUf = idUf;
	}
	/**
	 * @return Returns the nomBairro.
	 */
	public String getNomBairro() {
		return nomBairro;
	}
	/**
	 * @param nomBairro The nomBairro to set.
	 */
	public void setNomBairro(String nomBairro) {
		this.nomBairro = nomBairro;
	}
	/**
	 * @return Returns the nomCidade.
	 */
	public String getNomCidade() {
		return nomCidade;
	}
	/**
	 * @param nomCidade The nomCidade to set.
	 */
	public void setNomCidade(String nomCidade) {
		this.nomCidade = nomCidade;
	}
	/**
	 * @return Returns the nomeUsuario.
	 */
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	/**
	 * @param nomeUsuario The nomeUsuario to set.
	 */
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	/**
	 * @return Returns the nomLogradouro.
	 */
	public String getNomLogradouro() {
		return nomLogradouro;
	}
	/**
	 * @param nomLogradouro The nomLogradouro to set.
	 */
	public void setNomLogradouro(String nomLogradouro) {
		this.nomLogradouro = nomLogradouro;
	}
	/**
	 * @return Returns the numCep.
	 */
	public long getNumCep() {
		return numCep;
	}
	/**
	 * @param numCep The numCep to set.
	 */
	public void setNumCep(long numCep) {
		this.numCep = numCep;
	}
	/**
	 * @return Returns the numCpf.
	 */
	public String getNumCpf() {
		return numCpf;
	}
	/**
	 * @param numCpf The numCpf to set.
	 */
	public void setNumCpf(String numCpf) {
		this.numCpf = numCpf;
	}
	/**
	 * @return Returns the numRg.
	 */
	public String getNumRg() {
		return numRg;
	}
	/**
	 * @param numRg The numRg to set.
	 */
	public void setNumRg(String numRg) {
		this.numRg = numRg;
	}
	/**
	 * @return Returns the numTelefContato.
	 */
	public String getNumTelefContato() {
		return numTelefContato;
	}
	/**
	 * @param numTelefContato The numTelefContato to set.
	 */
	public void setNumTelefContato(String numTelefContato) {
		this.numTelefContato = numTelefContato;
	}
}
