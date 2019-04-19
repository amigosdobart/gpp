/*
 * Created on 21/07/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.brt.gpp.aplicacoes.promocao;
import java.util.Date;

/**
 * @author bt024318
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Promocao 
{
	private long id;
	private String nome;
	private String descricao;
	private Date inicioEntrada;
	private Date fimEntrada;
	//private boolean hibrido;
	private Date inicioValidade;
	private Date fimValidade;
	//private int tipo;
	//private double valorPorMinuto;
	//private boolean mesFechado;
	private int numDias;
	private double valorBonus;
	private int planoPreco;
	private int idtCategoria;
	private int indCadAssinanteExclusivo;
	private int idtCodigoNacional;
	private int indObrPrimeiraRecarga;

	/**
	 * @return
	 */
	public String getDescricao() 
	{
		return descricao;
	}

	/**
	 * @return
	 */
	public Date getFimEntrada() 
	{
		return fimEntrada;
	}

	/**
	 * @return
	 */
	public Date getFimValidade() 
	{
		return fimValidade;
	}

	/**
	 * @return
	 */
/*	public boolean isHibrido() 
	{
		return hibrido;
	}
*/
	/**
	 * @return
	 */
	public long getId() 
	{
		return id;
	}

	/**
	 * @return
	 */
	public Date getInicioEntrada() 
	{
		return inicioEntrada;
	}

	/**
	 * @return
	 */
	public Date getInicioValidade() 
	{
		return inicioValidade;
	}

	/**
	 * @return
	 */
	public String getNome() 
	{
		return nome;
	}

	/**
	 * @return
	 */
/*	public int getTipo() 
	{
		return tipo;
	}
*/
	public int getPlanoPreco()
	{
		return planoPreco;
	}
	
	public int getIdtCategoria()
	{
		return idtCategoria;
	}
	
	public int getIndCadAssinanteExclusivo()
	{
		return indCadAssinanteExclusivo;
	}
	
	/**
	 * @return Returns the indObrPrimeiraRecarga.
	 */
	public int getIndObrPrimeiraRecarga() 
	{
		return indObrPrimeiraRecarga;
	}
	
	/**
	 * @param indObrPrimeiraRecarga The indObrPrimeiraRecarga to set.
	 */
	public void setIndObrPrimeiraRecarga(int indObrPrimeiraRecarga) 
	{
		this.indObrPrimeiraRecarga = indObrPrimeiraRecarga;
	}
	
	/**
	 * @return Returns the idtCodigoNacional.
	 */
	public int getIdtCodigoNacional() 
	{
		return idtCodigoNacional;
	}
	
	/**
	 * @param string
	 */
	public void setDescricao(String string) 
	{
		descricao = string;
	}

	/**
	 * @param date
	 */
	public void setFimEntrada(Date date) {
		fimEntrada = date;
	}

	/**
	 * @param date
	 */
	public void setFimValidade(Date date) {
		fimValidade = date;
	}

	/**
	 * @param b
	 */
//	public void setHibrido(boolean b) {
//		hibrido = b;
//	}
//	public void setHibrido(int b) {
//		hibrido = b==0?false:true;
//	}


	/**
	 * @param l
	 */
	public void setId(long l) {
		id = l;
	}

	/**
	 * @param date
	 */
	public void setInicioEntrada(Date date) {
		inicioEntrada = date;
	}

	/**
	 * @param date
	 */
	public void setInicioValidade(Date date) {
		inicioValidade = date;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}


	/**
	 * @param i
	 */
//	public void setTipo(int i) {
//		tipo = i;
//	}

	/**
	 * @return
	 */
//	public double getValorPorMinuto() {
//		return valorPorMinuto;
//	}
//
//	/**
//	 * @param d
//	 */
//	public void setValorPorMinuto(double d) {
//		valorPorMinuto = d;
//	}

//	/**
//	 * @return
//	 */
//	public boolean isMesFechado() {
//		return mesFechado;
//	}

	/**
	 * @return
	 */
	public int getNumDias() {
		return numDias;
	}

	/**
	 * @param b
	 */
//	public void setMesFechado(boolean b) {
//		mesFechado = b;
//	}
//	public void setMesFechado(int b) {
//		mesFechado = b==0?false:true;
//	}


	/**
	 * @param i
	 */
	public void setNumDias(int i) {
		numDias = i;
	}

	/**
	 * @return Returns the valorBonus.
	 */
	public double getValorBonus() {
		return valorBonus;
	}
	/**
	 * @param valorBonus The valorBonus to set.
	 */
	public void setValorBonus(double valorBonus) {
		this.valorBonus = valorBonus;
	}
	
	public void setPlanoPreco(int planoPreco)
	{
		this.planoPreco = planoPreco;
	}
	
	public void setIdtCategoria(int idtCategoria)
	{
		this.idtCategoria = idtCategoria;
	}
	
	public void setIndCadAssinanteExclusivo(int indCadAssinanteExclusivo)
	{
		this.indCadAssinanteExclusivo = indCadAssinanteExclusivo;
	}
	
	/**
	 * @param idtCodigoNacional The idtCodigoNacional to set.
	 */
	public void setIdtCodigoNacional(int idtCodigoNacional) 
	{
		this.idtCodigoNacional = idtCodigoNacional;
	}
}
