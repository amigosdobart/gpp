
package com.brt.gpp.aplicacoes.contabilidade;


public class BonusRecarga 
{
	// Atributos da Classe
	String idtCodigoNacional;
	String idtPreHibrido;
	double vlrRecargasPeriodo;
	double vlrBonusPeriodo;
	double vlrConsumoTerceiros;
	double vlrConsumoBTSA;
	double vlrConsumoGSM;
	double vlrRecargasTotal;
	double vlrBonusTotal;
	double vlrSaldoRecargasFinal;
	double vlrSaldoBonusFinal;
	
	public BonusRecarga(String _idtCodigoNacional, String _idtPreHibrido, double _vlrRecargasPeriodo,
						double _vlrBonusPeriodo, double _vlrConsumoTerceiros, double _vlrConsumoBTSA,
						double _vlrConsumoGSM, double _vlrRecargasTotal, double _vlrBonusTotal,
						double _vlrSaldoRecargasFinal, double _vlrSaldoBonusFinal)
	{
		this.idtCodigoNacional = _idtCodigoNacional;
		this.idtPreHibrido = _idtPreHibrido;
		this.vlrBonusPeriodo = _vlrBonusPeriodo;
		this.vlrBonusTotal = _vlrBonusTotal;
		this.vlrConsumoBTSA = _vlrConsumoBTSA;
		this.vlrConsumoGSM = _vlrConsumoGSM;
		this.vlrConsumoTerceiros = _vlrConsumoTerceiros;
		this.vlrRecargasPeriodo = _vlrRecargasPeriodo;
		this.vlrRecargasTotal = _vlrRecargasTotal;
		this.vlrSaldoBonusFinal = _vlrSaldoBonusFinal;
		this.vlrSaldoRecargasFinal = _vlrSaldoRecargasFinal;
	}
	/**
	 * @return Returns the idtCodigoNacional.
	 */
	public String getIdtCodigoNacional() {
		return idtCodigoNacional;
	}
	/**
	 * @return Returns the idtPreHibrido.
	 */
	public String getIdtPreHibrido() {
		return idtPreHibrido;
	}
	/**
	 * @return Returns the vlrBonusPeriodo.
	 */
	public double getVlrBonusPeriodo() {
		return vlrBonusPeriodo;
	}
	/**
	 * @return Returns the vlrBonusTotal.
	 */
	public double getVlrBonusTotal() {
		return vlrBonusTotal;
	}
	/**
	 * @return Returns the vlrConsumoBTSA.
	 */
	public double getVlrConsumoBTSA() {
		return vlrConsumoBTSA;
	}
	/**
	 * @return Returns the vlrConsumoGSM.
	 */
	public double getVlrConsumoGSM() {
		return vlrConsumoGSM;
	}
	/**
	 * @return Returns the vlrConsumoTerceiros.
	 */
	public double getVlrConsumoTerceiros() {
		return vlrConsumoTerceiros;
	}
	/**
	 * @return Returns the vlrRecargasPeriodo.
	 */
	public double getVlrRecargasPeriodo() {
		return vlrRecargasPeriodo;
	}
	/**
	 * @return Returns the vlrRecargasTotal.
	 */
	public double getVlrRecargasTotal() {
		return vlrRecargasTotal;
	}
	/**
	 * @return Returns the vlrSaldoBonusFinal.
	 */
	public double getVlrSaldoBonusFinal() {
		return vlrSaldoBonusFinal;
	}
	/**
	 * @return Returns the vlrSaldoRecargasFinal.
	 */
	public double getVlrSaldoRecargasFinal() {
		return vlrSaldoRecargasFinal;
	}
	/**
	 * @param vlrBonusPeriodo The vlrBonusPeriodo to set.
	 */
	public void setVlrBonusPeriodo(double vlrBonusPeriodo) {
		this.vlrBonusPeriodo = vlrBonusPeriodo;
	}
	/**
	 * @param vlrBonusTotal The vlrBonusTotal to set.
	 */
	public void setVlrBonusTotal(double vlrBonusTotal) {
		this.vlrBonusTotal = vlrBonusTotal;
	}
	/**
	 * @param vlrConsumoBTSA The vlrConsumoBTSA to set.
	 */
	public void setVlrConsumoBTSA(double vlrConsumoBTSA) {
		this.vlrConsumoBTSA = vlrConsumoBTSA;
	}
}
