package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>Destino</code>. Refer�ncia: TBL_TAR_DESTINO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class Destino implements Serializable
{
 
	private String prefixoDestino;
	private NomeDestino nomeDestino;
	private RegiaoDestino regiaoDestino;
	private Pais pais;
	
	/**
	 * @return Inst�ncia de <code>Pais</code>
	 */
	public Pais getPais() 
	{
		return pais;
	}

	/**
	 * @param servico Inst�ncia de <code>Pais</code>
	 */
	public void setPais(Pais pais) 
	{
		this.pais = pais;
	}

	/**
	 * @return Inst�ncia de <code>NomeDestino</code>
	 */
	public NomeDestino getNomeDestino() 
	{
		return nomeDestino;
	}
	
	/**
	 * @param nomeDestino Inst�ncia de <code>NomeDestino</code>
	 */
	public void setNomeDestino(NomeDestino nomeDestino) 
	{
		this.nomeDestino = nomeDestino;
	}
	
	/**
	 * @return Prefixo de destino
	 */
	public String getPrefixoDestino() 
	{
		return prefixoDestino;
	}
	
	/**
	 * @param prefixoDestino Prefixo de destino
	 */
	public void setPrefixoDestino(String prefixoDestino) 
	{
		this.prefixoDestino = prefixoDestino;
	}
	
	/**
	 * @return Inst�ncia de <code>RegiaoDestino</code>
	 */
	public RegiaoDestino getRegiaoDestino() 
	{
		return regiaoDestino;
	}
	
	/**
	 * @param regiaoDestino Inst�ncia de <code>RegiaoDestino</code>
	 */
	public void setRegiaoDestino(RegiaoDestino regiaoDestino) 
	{
		this.regiaoDestino = regiaoDestino;
	}
	 
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Destino))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;		
		equal &= isEqual(this.prefixoDestino, 	((Destino)obj).getPrefixoDestino());
		equal &= isEqual(this.regiaoDestino, 	((Destino)obj).getRegiaoDestino());;
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
		result.append(this.prefixoDestino);
		result.append(this.regiaoDestino);

		return result.toString().hashCode();		
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[Destino]");
		if (prefixoDestino != null)	result.append("PREFIXO=" + this.prefixoDestino);
		if (nomeDestino != null)	result.append(";NOME=" + this.nomeDestino.getIdDestino());
		if (pais != null)			result.append(";PAIS=" + this.pais.getNomePais());
		if (regiaoDestino != null)
		{
			result.append(";BRT=" + ((this.regiaoDestino.isIndDestinoBrt() == true) ? "Sim" : "N�o"));
			if (regiaoDestino.getDesRegiaoDestino() != null)
				result.append(";DESCRICAO=" + this.regiaoDestino.getDesRegiaoDestino());
			if (regiaoDestino.getTipoDestino() != null)
				result.append(";TIPO=" + this.regiaoDestino.getTipoDestino().getDesTipoDestino());
			if (regiaoDestino.getServico() != null)
				result.append(";SERVICO=" + this.regiaoDestino.getServico().getNomeServico());
			if (regiaoDestino.getCodigoNacional() != null)
				result.append(";COD_NACIONAL=" + this.regiaoDestino.getCodigoNacional().getIdtCodigoNacional());
		}

		return result.toString();

	}
}
 
