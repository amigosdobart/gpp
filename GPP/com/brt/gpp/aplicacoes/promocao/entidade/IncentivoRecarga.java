package com.brt.gpp.aplicacoes.promocao.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * Classe responsavel por conter as informacoes de incentivo
 * de recargas
 * 
 * @author João Paulo Galvagni
 * @since  16/11/2007
 */
public class IncentivoRecarga implements Entidade
{
	private int			idIncentivoRecarga;
	private String		nomeIncentivoRecarga;
	private Date		dataInicioVigencia;
	private Date		dataFinalVigencia;
	private String 		idCanal;
	private String		idOrigem;
	private Collection	listaBonusIncentivo;
	
	public IncentivoRecarga(int novoIdIncentivoRecarga)
	{
		idIncentivoRecarga 	= novoIdIncentivoRecarga;
		nomeIncentivoRecarga= null;
		dataInicioVigencia 	= null;
		dataFinalVigencia 	= null;
		idCanal				= null;
		idOrigem			= null;
		listaBonusIncentivo = new ArrayList();
	}
	
	public Object clone()
	{
		IncentivoRecarga clone = new IncentivoRecarga(this.idIncentivoRecarga);
		
		clone.setNomeIncentivoRecarga(this.nomeIncentivoRecarga);
		clone.setDataInicioVigencia(this.dataInicioVigencia);
		clone.setDataFinalVigencia(this.dataFinalVigencia);
		clone.setIdCanal(this.idCanal);
		clone.setIdOrigem(this.idOrigem);
		clone.setListaBonusIncentivo(this.listaBonusIncentivo);
		
		return clone;
	}
	
	public String toString()
	{
		return "idIncentivo: " + idIncentivoRecarga + " - nomeIncentivo: " + nomeIncentivoRecarga;
	}
	
	/**
	 * @return the dataFinalVigencia
	 */
	public Date getDataFinalVigencia()
	{
		return dataFinalVigencia;
	}
	
	/**
	 * @param dataFinalVigencia the dataFinalVigencia to set
	 */
	public void setDataFinalVigencia(Date dataFinalVigencia)
	{
		this.dataFinalVigencia = dataFinalVigencia;
	}
	
	/**
	 * @return the dataInicioVigencia
	 */
	public Date getDataInicioVigencia()
	{
		return dataInicioVigencia;
	}
	
	/**
	 * @param dataInicioVigencia the dataInicioVigencia to set
	 */
	public void setDataInicioVigencia(Date dataInicioVigencia)
	{
		this.dataInicioVigencia = dataInicioVigencia;
	}
	
	/**
	 * @return the idIncentivoRecarga
	 */
	public int getIdIncentivoRecarga()
	{
		return idIncentivoRecarga;
	}
	
	/**
	 * @return the listaBonusIncentivo
	 */
	public Collection getListaBonusIncentivo()
	{
		return listaBonusIncentivo;
	}
	
	/**
	 * @param listaBonusIncentivo the listaBonusIncentivo to set
	 */
	public void setListaBonusIncentivo(Collection listaBonusIncentivo)
	{
		this.listaBonusIncentivo = listaBonusIncentivo;
	}
	
	/**
	 * @param bonusIncentivoRecarga the listaBonusIncentivo to add
	 */
	public void addListaBonusIncentivo(BonusIncentivoRecarga bonusIncentivoRecarga)
	{
		this.listaBonusIncentivo.add(bonusIncentivoRecarga);
	}
	
	/**
	 * @return the nomeIncentivoRecarga
	 */
	public String getNomeIncentivoRecarga()
	{
		return nomeIncentivoRecarga;
	}
	
	/**
	 * @param nomeIncentivoRecarga the nomeIncentivoRecarga to set
	 */
	public void setNomeIncentivoRecarga(String nomeIncentivoRecarga)
	{
		this.nomeIncentivoRecarga = nomeIncentivoRecarga;
	}
	
	/**
	 * @return the idCanal
	 */
	public String getIdCanal()
	{
		return idCanal;
	}
	
	/**
	 * @param idCanal the idCanal to set
	 */
	public void setIdCanal(String idCanal)
	{
		this.idCanal = idCanal;
	}
	
	/**
	 * @return the idOrigem
	 */
	public String getIdOrigem()
	{
		return idOrigem;
	}
	
	/**
	 * @param idOrigem the idOrigem to set
	 */
	public void setIdOrigem(String idOrigem)
	{
		this.idOrigem = idOrigem;
	}
	
	public void setTipTransacao(String novoIdCanal, String novoIdOrigem)
	{
		this.idCanal = novoIdCanal;
		this.idOrigem = novoIdOrigem;
	}
	
	public String getTipTransacao()
	{
		return this.idCanal + this.idOrigem;
	}
}