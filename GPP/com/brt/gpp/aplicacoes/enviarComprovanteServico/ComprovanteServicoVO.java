package com.brt.gpp.aplicacoes.enviarComprovanteServico;

import java.sql.Timestamp;

/**
* @Autor: 			Camile Cardoso Couto
* Data: 				19/04/2004
*
* Modificado por:   Geraldo Palmeira
* Data:			    28/11/2005
* Razao:		    Essa classe foi adaptada ao modelo Produtor Consumidor.
*/

public class ComprovanteServicoVO {
	
	private Timestamp dataRequisicao;
	private String msisdn;
	private String dataInicial;
	private String dataFinal;
	

	/**
	 * Métodos Set
	 */
	
	public void setDataRequisicao(Timestamp dataRequisicao) 
	{
		this.dataRequisicao = dataRequisicao;
	}


	public void setMsisdn(String msisnd) 
	{
		this.msisdn = msisnd;
	}


	public void setDataInicial(String dataInicial) 
	{
		this.dataInicial = dataInicial;
	}


	public void setDataFinal(String dataFinal) 
	{
		this.dataFinal = dataFinal;
	}
	
	
	/**
	 * Métodos Get
	 */
	
	public Timestamp getDataRequisicao() 
	{
		return dataRequisicao;
	}
	
	
	public String getMsisdn() 
	{
		return msisdn;
	}
	
	
	public String getDataInicial()
	{	
		return dataInicial;
	}
	
	
	public String getDataFinal()
	{		
		return dataFinal;
	}
	
}

