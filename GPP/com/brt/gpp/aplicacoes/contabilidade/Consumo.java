package com.brt.gpp.aplicacoes.contabilidade;

public class Consumo 
{
	//Atributos da Classe
	String idtCodigoNacional;
	String idtPreHibrido;
	double vlrServicosBTSA;
	double vlrServicosGSM;
	double vlrServicosOutros;
	
	public Consumo(	String _idtCodigoNacional, String _idtPreHibrido,
					double _vlrServicosBTSA, double _vlrServicosGSM, double _vlrServicosOutros)
	{
		this.idtCodigoNacional = _idtCodigoNacional;
		this.idtPreHibrido = _idtPreHibrido;
		this.vlrServicosBTSA = _vlrServicosBTSA;
		this.vlrServicosGSM = _vlrServicosGSM;
		this.vlrServicosOutros = _vlrServicosOutros;
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
	 * @return Returns the vlrServicosBTSA.
	 */
	public double getVlrServicosBTSA() {
		return vlrServicosBTSA;
	}
	/**
	 * @return Returns the vlrServicosGSM.
	 */
	public double getVlrServicosGSM() {
		return vlrServicosGSM;
	}
	/**
	 * @return Returns the vlrServicosOutros.
	 */
	public double getVlrServicosOutros() {
		return vlrServicosOutros;
	}
	/**
	 * @param vlrServicosGSM The vlrServicosGSM to set.
	 */
	public void setVlrServicosGSM(double vlrServicosGSM) {
		this.vlrServicosGSM = vlrServicosGSM;
	}
	/**
	 * @param vlrServicosBTSA The vlrServicosBTSA to set.
	 */
	public void setVlrServicosBTSA(double vlrServicosBTSA) {
		this.vlrServicosBTSA = vlrServicosBTSA;
	}
	/**
	 * @param vlrServicosOutros The vlrServicosOutros to set.
	 */
	public void setVlrServicosOutros(double vlrServicosOutros) {
		this.vlrServicosOutros = vlrServicosOutros;
	}
}
