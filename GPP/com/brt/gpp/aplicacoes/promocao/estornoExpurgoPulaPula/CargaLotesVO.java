package com.brt.gpp.aplicacoes.promocao.estornoExpurgoPulaPula;

/**
 *	Value Object para carga de lote de Estorno de Bonus Pula-Pula.
 * 
 *	@author		Bernardo Vergne Dias
 *	@since		02/01/2007
 */
public class CargaLotesVO {
	
	private String registro;
	private int numLinha;
	
	public CargaLotesVO (String registro, int numLinha)
	{
		this.registro = registro;
		this.numLinha = numLinha;
	}
	
	public String getRegistro()
	{
		return registro;
	}

	public int getNumLinha()
	{
		return numLinha;
	}

}
