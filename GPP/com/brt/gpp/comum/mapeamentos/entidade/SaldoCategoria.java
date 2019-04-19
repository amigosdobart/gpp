package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;


/**
 *	Entidade <code>SaldoCategoria</code>. Referência: TBL_APR_SALDO_CATEGORIA
 * 
 *	@version	1.0
 *	@author		Vitor Murilo Dias
 *	@since		30/03/2008
 */
public class SaldoCategoria implements Serializable, Comparable
{

	private static final long serialVersionUID = 1L;
	private Categoria categoria;
	private TipoSaldo tipoSaldo;
	private String nomSaldo;

	/**
	 * Formata a representação dos objeto como String
	 */	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Categoria]");
		result.append("ID=" + this.categoria);
		result.append("[Saldo]");
		result.append("ID=" + this.tipoSaldo);
		result.append("[Nome Saldo]");
		result.append("Nome= " + this.nomSaldo);
	
		return result.toString();
	}

	/**
	 * Verifica se os objetos são iguais
	 */
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof SaldoCategoria))
			return false;
		
		if (obj == this)
			return true;

		return this.getCategoria() == ((SaldoCategoria)obj).getCategoria();
	}
	
	/**
	 * Compara dois objetos
	 */
	public int compareTo(Object obj)
	{
		if (!(obj instanceof Categoria))
			throw new IllegalArgumentException();
		
		return this.getCategoria().getIdCategoria() - ((SaldoCategoria)obj).getCategoria().getIdCategoria();
	}
	
	
	/**
	 * @return the categoria
	 */
	public Categoria getCategoria() {
		return categoria;
	}


	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}


	/**
	 * @return the nomSaldo
	 */
	public String getNomSaldo() {
		return nomSaldo;
	}


	/**
	 * @param nomSaldo the nomSaldo to set
	 */
	public void setNomSaldo(String nomSaldo) {
		this.nomSaldo = nomSaldo;
	}


	/**
	 * @return the tipoSaldo
	 */
	public TipoSaldo getTipoSaldo() {
		return tipoSaldo;
	}


	/**
	 * @param tipoSaldo the tipoSaldo to set
	 */
	public void setTipoSaldo(TipoSaldo tipoSaldo) {
		this.tipoSaldo = tipoSaldo;
	}
	
}
