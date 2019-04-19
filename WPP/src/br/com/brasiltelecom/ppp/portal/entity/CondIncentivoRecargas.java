package br.com.brasiltelecom.ppp.portal.entity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Esta classe representa a condicao de concessao de creditos para a campanha de 
 * incentivo de recargas, bonficando o assinante no saldo de bonus. Os criterios de 
 * 
 * 
 * 
 * concessao serah a analise se o assinante realizou uma recarga, sendo que para 
 * cada valor de recarga, um determinado valor de bonus serah aplicado.
 * @author Joao Carlos
 * @see CondicaoConcessao
 * @since 30-Janeiro-2006
 */
public class CondIncentivoRecargas implements Comparable,org.exolab.castor.jdo.TimeStampable
{
	private double valorRecarga;
	private double valorBonusSM;
	private double valorBonusDados;
	private double valorBonus;
	private int id;
	private Campanha campanha;
	private long jdoTimestamp;
	private boolean deveRemover;
	
	private DecimalFormat df = new DecimalFormat("##0.00",new DecimalFormatSymbols(new Locale("PT","BR")));
	
	/**
	 * Access method for the valorRecarga property.
	 * 
	 * @return   the current value of the valorRecarga property
	 */
	public double getValorRecarga() 
	{
		return valorRecarga;
	}
	
	/**
	 * Sets the value of the valorRecarga property.
	 * 
	 * @param aValorRecarga the new value of the valorRecarga property
	 */
	public void setValorRecarga(double aValorRecarga) 
	{
		valorRecarga = aValorRecarga;
	}
	
	/**
	 * Access method for the valorBonusSM property.
	 * 
	 * @return   the current value of the valorBonusSM property
	 */
	public double getValorBonusSM() 
	{
		return valorBonusSM;
	}
	
	/**
	 * Sets the value of the valorBonusSM property.
	 * 
	 * @param aValorBonusSM the new value of the valorBonusSM property
	 */
	public void setValorBonusSM(double aValorBonusSM) 
	{
		valorBonusSM = aValorBonusSM;
	}
	
	/**
	 * Access method for the valorBonusDados property.
	 * 
	 * @return   the current value of the valorBonusDados property
	 */
	public double getValorBonusDados() 
	{
		return valorBonusDados;
	}
	
	/**
	 * Sets the value of the valorBonusDados property.
	 * 
	 * @param aValorBonusDados the new value of the valorBonusDados property
	 */
	public void setValorBonusDados(double aValorBonusDados) 
	{
		valorBonusDados = aValorBonusDados;
	}
	
	/**
	 * Access method for the valorBonus property.
	 * 
	 * @return   the current value of the valorBonus property
	 */
	public double getValorBonus() 
	{
		return valorBonus;
	}
	
	/**
	 * Sets the value of the valorBonus property.
	 * 
	 * @param aValorBonus the new value of the valorBonus property
	 */
	public void setValorBonus(double aValorBonus) 
	{
		valorBonus = aValorBonus;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return (int)getValorRecarga();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "R$"+df.format(getValorRecarga());
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof CondIncentivoRecargas))
			return false;
		
		if ( ((CondIncentivoRecargas)obj).getValorRecarga()==this.getValorRecarga() )
			return true;
		
		return false;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if (!(obj instanceof CondIncentivoRecargas))
			throw new ClassCastException("Classe invalida:"+obj.getClass().getName());
		
		if ( this.getValorRecarga() > ((CondIncentivoRecargas)obj).getValorRecarga() )
			return 1;
		else if ( this.getValorRecarga() < ((CondIncentivoRecargas)obj).getValorRecarga() )
				return -1;

		return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}
	
	public boolean deveRemover() {
		return deveRemover;
	}

	public void setDeveRemover(boolean deveRemover) {
		this.deveRemover = deveRemover;
	}
	
	// Metodos necessarios para implementacao 
	public long jdoGetTimeStamp()
	{
		return jdoTimestamp;
	}
	
	public void jdoSetTimeStamp(long jdoTimestamp)
	{
		this.jdoTimestamp = jdoTimestamp;
	}
}
