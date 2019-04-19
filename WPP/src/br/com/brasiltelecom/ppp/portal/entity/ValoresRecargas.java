package br.com.brasiltelecom.ppp.portal.entity;

public class ValoresRecargas implements Comparable
{
	private double idValor;
	private int valorFace;
	private double vlrPrincipal;
	private double vlrBonus;
	private double vlrSM;
	private double vlrDados;
	private int diasExpPrincipal;
	private int diasExpBonus;
	private int diasExpSM;
	private int diasExpDados;
	private double vlrEfetivoPago;
	private double vlrBonusSM;
	private double vlrBonusDados;
	
	public int getDiasExpBonus() {
		return diasExpBonus;
	}
	public void setDiasExpBonus(int diasExpBonus) {
		this.diasExpBonus = diasExpBonus;
	}
	public int getDiasExpDados() {
		return diasExpDados;
	}
	public void setDiasExpDados(int diasExpDados) {
		this.diasExpDados = diasExpDados;
	}
	public int getDiasExpPrincipal() {
		return diasExpPrincipal;
	}
	public void setDiasExpPrincipal(int diasExpPrincipal) {
		this.diasExpPrincipal = diasExpPrincipal;
	}
	public int getDiasExpSM() {
		return diasExpSM;
	}
	public void setDiasExpSM(int diasExpSM) {
		this.diasExpSM = diasExpSM;
	}
	public double getIdValor() {
		return idValor;
	}
	public void setIdValor(double idValor) {
		this.idValor = idValor;
	}
	public int getValorFace() {
		return valorFace;
	}
	public void setValorFace(int valorFace) {
		this.valorFace = valorFace;
	}
	public double getVlrBonus() {
		return vlrBonus;
	}
	public void setVlrBonus(double vlrBonus) {
		this.vlrBonus = vlrBonus;
	}
	public double getVlrBonusDados() {
		return vlrBonusDados;
	}
	public void setVlrBonusDados(double vlrBonusDados) {
		this.vlrBonusDados = vlrBonusDados;
	}
	public double getVlrBonusSM() {
		return vlrBonusSM;
	}
	public void setVlrBonusSM(double vlrBonusSM) {
		this.vlrBonusSM = vlrBonusSM;
	}
	public double getVlrDados() {
		return vlrDados;
	}
	public void setVlrDados(double vlrDados) {
		this.vlrDados = vlrDados;
	}
	public double getVlrEfetivoPago() {
		return vlrEfetivoPago;
	}
	public void setVlrEfetivoPago(double vlrEfetivoPago) {
		this.vlrEfetivoPago = vlrEfetivoPago;
	}
	public double getVlrPrincipal() {
		return vlrPrincipal;
	}
	public void setVlrPrincipal(double vlrPrincipal) {
		this.vlrPrincipal = vlrPrincipal;
	}
	public double getVlrSM() {
		return vlrSM;
	}
	public void setVlrSM(double vlrSM) {
		this.vlrSM = vlrSM;
	}
	
	public boolean isValorFace()
	{
		if (getValorFace()==1)
			return true;
		
		return false;
	}

	public int hashCode()
	{
		return new Double(this.getIdValor()).hashCode();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof ValoresRecargas) )
			return false;
		
		if ( ((ValoresRecargas)obj).getIdValor() == this.getIdValor())
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return "ID:"+this.getIdValor();
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof ValoresRecargas) )
			return -1;
		
		if ( this.getIdValor() > ((ValoresRecargas)obj).getIdValor() )
			return 1;
		
		if ( this.getIdValor() < ((ValoresRecargas)obj).getIdValor() )
			return -1;
		
		else return 0;
	}
}
