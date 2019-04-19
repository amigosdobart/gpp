package com.brt.gpp.aplicacoes.contabil.consolidacaoContabil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *	Classe que representa o Value Object do Produtor-Consumidor da Consolidacao Contabil Diaria
 * 
 *	@author	Magno Batista Correa, Bernardo Vergne Dias, Daniel Ferreira
 *	Criado em 23/11/2007
 */
public class ConsolidacaoContabilDiariaVO
{
    private SimpleDateFormat    sdf = new SimpleDateFormat("dd/MM/yyyy");
    
	private	Date				dataAProcessar;   // Data a ser processada
	private String				periodoContabil;  // Assinante a ser processado

	/**
	 * Construtor Carregado
	 * 
	 * @param dataAProcessar
	 * @param periodoContabil
	 */
	public ConsolidacaoContabilDiariaVO(Date dataAProcessar, String periodoContabil)
	{
		this.reset();
		this.dataAProcessar   = dataAProcessar;
		this.periodoContabil  = periodoContabil;
	}

	public ConsolidacaoContabilDiariaVO()
	{
	    this.reset();
	}

	/**
	 *	Inicializa o objeto com valores vazios.
	 */
	public void reset()
	{
		this.dataAProcessar    = null;
		this.periodoContabil   = null;
	}
	
	public String toString()
	{
		return "dataAProcessar=" + sdf.format(this.dataAProcessar) + ";periodoContabil=" + this.periodoContabil;
	}

	public Date getDataAProcessar() 
    {
		return dataAProcessar;
	}

	public void setDataAProcessar(Date dataAProcessar) 
    {
		this.dataAProcessar = dataAProcessar;
	}

	public String getPeriodoContabil() 
    {
		return periodoContabil;
	}

	public void setPeriodoContabil(String periodoContabil) 
    {
		this.periodoContabil = periodoContabil;
	}
}