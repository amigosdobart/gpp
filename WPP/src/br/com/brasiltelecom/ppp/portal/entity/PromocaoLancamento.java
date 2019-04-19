/*
 * Created on 02/08/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Collection;
import java.util.Date;


/**
 * @author Luciano Vilela
 *
 */
public class PromocaoLancamento {
	private int id;
	private String nome;
	private String descricao;
	private Date dataInicio;
	private Date dataFim;
	private int hibrido;
	private Date dataInicioValidade;
	private Date dataFimValidade;
	private String tipo;
	private double valorMinuto;
	private int mesFechado;
	private int quantidadeDias;
	
	private Collection planoPreco;
	private Collection rateName;
	private Collection transaction;

	/**
	 * @return
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @return
	 */
	public Date getDataFimValidade() {
		return dataFimValidade;
	}

	/**
	 * @return
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @return
	 */
	public Date getDataInicioValidade() {
		return dataInicioValidade;
	}

	/**
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return
	 */
	public int getHibrido() {
		return hibrido;
	}

	public boolean isHibrido() {
		return hibrido==0?false:true;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public int getMesFechado() {
		return mesFechado;
	}

	public boolean isMesFechado() {
		return mesFechado==0?false:true;
	}

	/**
	 * @return
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return
	 */
	public Collection getPlanoPreco() {
		return planoPreco;
	}

	/**
	 * @return
	 */
	public int getQuantidadeDias() {
		return quantidadeDias;
	}

	/**
	 * @return
	 */
	public Collection getRateName() {
		return rateName;
	}

	/**
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @return
	 */
	public Collection getTransaction() {
		return transaction;
	}

	/**
	 * @return
	 */
	public double getValorMinuto() {
		return valorMinuto;
	}

	/**
	 * @param date
	 */
	public void setDataFim(Date date) {
		dataFim = date;
	}

	/**
	 * @param date
	 */
	public void setDataFimValidade(Date date) {
		dataFimValidade = date;
	}

	/**
	 * @param date
	 */
	public void setDataInicio(Date date) {
		dataInicio = date;
	}

	/**
	 * @param date
	 */
	public void setDataInicioValidade(Date date) {
		dataInicioValidade = date;
	}

	/**
	 * @param string
	 */
	public void setDescricao(String string) {
		descricao = string;
	}

	/**
	 * @param b
	 */
	public void setHibrido(int b) {
		hibrido = b;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param b
	 */
	public void setMesFechado(int b) {
		mesFechado = b;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}

	/**
	 * @param collection
	 */
	public void setPlanoPreco(Collection collection) {
		planoPreco = collection;
	}

	/**
	 * @param i
	 */
	public void setQuantidadeDias(int i) {
		quantidadeDias = i;
	}

	/**
	 * @param collection
	 */
	public void setRateName(Collection collection) {
		rateName = collection;
	}

	/**
	 * @param i
	 */
	public void setTipo(String i) {
		tipo = i;
	}

	/**
	 * @param collection
	 */
	public void setTransaction(Collection collection) {
		transaction = collection;
	}

	/**
	 * @param d
	 */
	public void setValorMinuto(double d) {
		valorMinuto = d;
	}

}
