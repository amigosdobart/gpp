package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>RegiaoDestino</code>. Referência: TBL_TAR_REGIAO_DESTINO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class RegiaoDestino implements Serializable
{
	
	private int idRegiaoDestino; 
	private String desRegiaoDestino;
	private Servico servico;
	private CodigoNacional codigoNacional;
	private TipoDestino tipoDestino;
	private boolean indDestinoBrt;
	
	/**
	 * @return Instância de <code>CodigoNacional</code>
	 */
	public CodigoNacional getCodigoNacional() 
	{
		return codigoNacional;
	}
	
	/**
	 * @param codigoNacional Instância de <code>CodigoNacional</code>
	 */
	public void setCodigoNacional(CodigoNacional codigoNacional) 
	{
		this.codigoNacional = codigoNacional;
	}
	
	/**
	 * @return Descrição da região de destino
	 */
	public String getDesRegiaoDestino() 
	{
		return desRegiaoDestino;
	}
	
	/**
	 * @param desRegiaoDestino Descrição da região de destino
	 */
	public void setDesRegiaoDestino(String desRegiaoDestino) 
	{
		this.desRegiaoDestino = desRegiaoDestino;
	}
	
	/**
	 * @return ID da região de destino
	 */
	public int getIdRegiaoDestino() 
	{
		return idRegiaoDestino;
	}
	
	/**
	 * @param idRegiaoDestino ID da região de destino
	 */
	public void setIdRegiaoDestino(int idRegiaoDestino) 
	{
		this.idRegiaoDestino = idRegiaoDestino;
	}
	
	/**
	 * @return Indicador de destino BrT
	 */
	public boolean isIndDestinoBrt() 
	{
		return indDestinoBrt;
	}
	
	/**
	 * @param indDestinoBrt Indicador de destino BrT
	 */
	public void setIndDestinoBrt(boolean indDestinoBrt) 
	{
		this.indDestinoBrt = indDestinoBrt;
	}
	
	/**
	 * @return Instância de <code>Servico</code>
	 */
	public Servico getServico() 
	{
		return servico;
	}
	
	/**
	 * @param servico Instância de <code>Servico</code>
	 */
	public void setServico(Servico servico) 
	{
		this.servico = servico;
	}
	
	/**
	 * @return Instância de <code>TipoDestino</code>
	 */
	public TipoDestino getTipoDestino() 
	{
		return tipoDestino;
	}
	
	/**
	 * @param tipoDestino Instância de <code>TipoDestino</code>
	 */
	public void setTipoDestino(TipoDestino tipoDestino) 
	{
		this.tipoDestino = tipoDestino;
	}
	 
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof RegiaoDestino))
			return false;
		
		if (obj == this)
			return true;

		boolean equal = true;		
		equal &= idRegiaoDestino == ((RegiaoDestino)obj).getIdRegiaoDestino();
		equal &= isEqual(this.servico, 			((RegiaoDestino)obj).getServico());;
		return equal;
	}
	
	private boolean isEqual(Object obj1, Object obj2)
	{
		if (obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		if (obj1 == null && obj2 == null)
			return true;
		return false;
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idRegiaoDestino);
		if (servico != null) result.append(this.servico.getIdServico());

		return result.toString().hashCode();		
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[RegiaoDestino]");
		result.append("ID=" + this.idRegiaoDestino);
		result.append("BRT=" + (this.indDestinoBrt == true ? "Sim" : "Não"));
		if (desRegiaoDestino != null)	result.append(";DESCRICAO=" + this.desRegiaoDestino);
		if (servico != null)			result.append(";SERVICO=" + this.servico.getNomeServico());
		if (codigoNacional != null)		result.append(";COD_NACIONAL=" + this.codigoNacional.getIdtCodigoNacional());
		if (tipoDestino != null)		result.append(";TIPO_DESTINO=" + this.tipoDestino.getDesTipoDestino());
		
		return result.toString();

	}
}
 
