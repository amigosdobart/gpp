package com.brt.gpp.aplicacoes.conciliar;

import java.sql.Timestamp;

/**
* @Autor: 			Denys Oliveira
* Data: 				30/03/2004
*
* Modificado por:  Geraldo Palmeira
* Data:			   28/11/2005
* Razao:		   Essa classe foi adaptada ao modelo Produtor Consumidor.
*/
public class ConciliarVO 
{
	public ConciliarVO() 
	{		
	}
	
	private String    idRecarga;
	private String    idNsuInstituicao;
	private String    idtTerminal;
	private String    tipoTransacao;
	private String    nomeOperador;
	private String    idSistemaOrigem;
	private Timestamp dataRecarga;
	private double    vrlPago;
	private String    idtMsisdn;
	private String	  idEmpresa;
		
	/**
	 * Métodos Set
	 */
	
	public void setIdRecarga(String idRecarga) 
	{
		this.idRecarga = idRecarga;
	}


	public void setIdNsuInstituicao(String idNsuInstituicao) 
	{
		this.idNsuInstituicao = idNsuInstituicao;
	}


	public void setIdtTerminal(String idtTerminal) 
	{
		this.idtTerminal = idtTerminal;
	}


	public void setTipoTransacao(String tipoTransacao) 
	{
		this.tipoTransacao = tipoTransacao;
	}


	public void setNomeOperador(String nomeOperador) 
	{
		this.nomeOperador = nomeOperador;
	}


	public void setIdSistemaOrigem(String idSistemaOrigem) 
	{
		this.idSistemaOrigem = idSistemaOrigem;
	}

	public void setDataRecarga(Timestamp dataRecarga) 
	{
		this.dataRecarga = dataRecarga;
	}


	public void setVlrPago(double vrlPago) 
	{
		this.vrlPago = vrlPago;
	}


	public void setIdtMsisdn(String idtMsisdn) 
	{
		this.idtMsisdn = idtMsisdn;
	}

	public void setIdEmpresa(String idEmpresa)
	{
	    this.idEmpresa = idEmpresa;
	}
	
	
	/**
	 * Métodos Get
	 */
	
	public String getIdRecarga() 
	{
		return idRecarga;
	}
	
	
	public String getIdNsuInstituicao() 
	{
		return idNsuInstituicao;
	}


	public String getIdtTerminal() 
	{
		return idtTerminal;
	}


	public String getTipoTransacao() 
	{
		return tipoTransacao;
	}


	public String getNomeOperador() 
	{
		return nomeOperador;
	}


	public String getIdSistemaOrigem() 
	{
		return idSistemaOrigem;
	}

	public Timestamp getDataRecarga() 
	{
		return dataRecarga;
	}


	public double getVlrPago() 
	{
		return vrlPago;
	}


	public String getIdtMsisdn() 
	{
		return idtMsisdn;
	}
	
	public String getIdEmpresa()
	{
		return idEmpresa;
	}
	
}


