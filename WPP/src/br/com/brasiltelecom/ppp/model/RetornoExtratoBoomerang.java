/*
 * Created on 22/11/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * @author HEnrique Canto
 */
public class RetornoExtratoBoomerang{
	
	private Collection extratos;
	private String totalBoomerang;
	
	private String numeroMinutos;
	private String numeroChamadas;
	private String indicieRecarga;
	
	private DecimalFormat df = new DecimalFormat("#0.00");
	//rivate SimpleDateFormat hdf = new SimpleDateFormat("H:mm:ss");
	
	
	
	
	
	/**
	 * @return Returns the indicieRecarga.
	 */
	public String getIndicieRecarga() {
		return indicieRecarga;
	}
	/**
	 * @param indicieRecarga The indicieRecarga to set.
	 */
	public void setIndicieRecarga(String indicieRecarga) {
		this.indicieRecarga = indicieRecarga;
	}
	/**
	 * @return String - Total de Bonus Boomerang no formato para apresentação ao usuário.
	 */
	public String getTotalBoomerangString() {
		if (totalBoomerang != null)
		{
			
			return df.format(Double.parseDouble(totalBoomerang));
		}else return totalBoomerang;
	}	
	/**
	 * @return Returns the numeroChamadas.
	 */
	public String getNumeroChamadas() {
		return numeroChamadas;
	}
	/**
	 * @param numeroChamadas The numeroChamadas to set.
	 */
	public void setNumeroChamadas(String numeroChamadas) {
		this.numeroChamadas = numeroChamadas;
	}
	/**
	 * @return Returns the numeroMinutos.
	 */
	public String getNumeroMinutos() {
		return numeroMinutos;
	}
	/**
	 * @return Returns the df.
	 */
	public DecimalFormat getDf() {
		return df;
	}
	/**
	 * @param df The df to set.
	 */
	public void setDf(DecimalFormat df) {
		this.df = df;
	}
	/**
	 * @return Returns the extratos.
	 */
	public Collection getExtratos() {
		return extratos;
	}
	/**
	 * @param extratos The extratos to set.
	 */
	public void setExtratos(Collection extratos) {
		this.extratos = extratos;
	}	
	/**
	 * @return Returns the totalBoomerang.
	 */
	public String getTotalBoomerang() {
		return totalBoomerang;
	}
	/**
	 * @param totalBoomerang The totalBoomerang to set.
	 */
	public void setTotalBoomerang(String totalBoomerang) {
		this.totalBoomerang = totalBoomerang;
	}
	
	/**
	 * @param numeroMinutos The numeroMinutos to set.
	 */
	public void setNumeroMinutos(String numeroMinutos) {
		this.numeroMinutos = numeroMinutos;
	}
}
