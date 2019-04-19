package com.brt.gpp.aplicacoes.promocao.entidade;

import java.util.Date;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Classe responsavel por conter as informacoes do
 * assinante que ira receber o bonus (incentivo) apos
 * efetuar alguma recarga
 * 
 * @author João Paulo Galvagni
 * @since  16/11/2007
 */
public class AssinanteIncentivoRecarga implements Entidade
{
	private String 			 msisdn;
	private Date			 dataRecarga;
	private double 			 valorRecarga;
	private int				 diaExecucao;
	private IncentivoRecarga incentivoRecarga;
	
	public AssinanteIncentivoRecarga()
	{
		msisdn				= null;
		dataRecarga			= null;
		valorRecarga 		= 0.0;
		incentivoRecarga	= null;
	}
	
	public Object clone()
	{
		AssinanteIncentivoRecarga clone = new AssinanteIncentivoRecarga();
		
		clone.setMsisdn(this.msisdn);
		clone.setDataRecarga(this.dataRecarga);
		clone.setValorRecarga(this.valorRecarga);
		clone.setIncentivoRecarga(this.incentivoRecarga);
		
		return clone;
	}
	
	public String toString()
	{
		return "msisdn: " + msisdn + 
			   " - dataRecarga: " + dataRecarga +
			   " - valorRecarga: " + valorRecarga + 
			   " - diaExecucao: " + diaExecucao;
	}
	
	/**
	 * @return the dataRecarga
	 */
	public Date getDataRecarga()
	{
		return dataRecarga;
	}
	
	/**
	 * @param dataRecarga the dataRecarga to set
	 */
	public void setDataRecarga(Date dataRecarga)
	{
		this.dataRecarga = dataRecarga;
	}
	
	/**
	 * @return the incentivoRecarga
	 */
	public IncentivoRecarga getIncentivoRecarga()
	{
		return incentivoRecarga;
	}
	
	/**
	 * @param incentivoRecarga the incentivoRecarga to set
	 */
	public void setIncentivoRecarga(IncentivoRecarga incentivoRecarga)
	{
		this.incentivoRecarga = incentivoRecarga;
	}
	
	/**
	 * @return the msisdn
	 */
	public String getMsisdn()
	{
		return msisdn;
	}
	
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 * @return the valorRecarga
	 */
	public double getValorRecarga()
	{
		return valorRecarga;
	}
	
	/**
	 * @param valorRecarga the valorRecarga to set
	 */
	public void setValorRecarga(double valorRecarga)
	{
		this.valorRecarga = valorRecarga;
	}
	
	/**
	 * @return the diaExecucao
	 */
	public int getDiaExecucao()
	{
		return diaExecucao;
	}
	
	/**
	 * @param diaExecucao the diaExecucao to set
	 */
	public void setDiaExecucao(int diaExecucao)
	{
		this.diaExecucao = diaExecucao;
	}
}