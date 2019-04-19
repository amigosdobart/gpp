package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>GrupoPais</code>. Referência: TBL_TAR_GRUPO_PAIS
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class GrupoPais implements Serializable
{
 
	private Pais pais;
	private Servico servico;
	private OperadoraLD operadoraLD;
	private int idGrupo;
	
	/**
	 * @return Instância de <code>OperadoraLD</code>
	 */
	public OperadoraLD getOperadoraLD() 
	{
		return operadoraLD;
	}
	
	/**
	 * @param operadoraLD Instância de <code>OperadoraLD</code>
	 */
	public void setOperadoraLD(OperadoraLD operadoraLD) 
	{
		this.operadoraLD = operadoraLD;
	}
	
	/**
	 * @return ID do grupo
	 */
	public int getIdGrupo() 
	{
		return idGrupo;
	}
	
	/**
	 * @param idGrupo ID do grupo
	 */
	public void setIdGrupo(int idGrupo) 
	{
		this.idGrupo = idGrupo;
	}
	
	/**
	 * @return Instância de <code>Pais</code>
	 */
	public Pais getPais() 
	{
		return pais;
	}
	
	/**
	 * @param pais Instância de <code>Pais</code>
	 */
	public void setPais(Pais pais) 
	{
		this.pais = pais;
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
	 
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof GrupoPais))
			return false;
		
		if (obj == this)
			return true;

		boolean equal = true;		
		equal &= isEqual(this.operadoraLD, 	((GrupoPais)obj).getOperadoraLD());
		equal &= isEqual(this.servico, 		((GrupoPais)obj).getServico());
		equal &= isEqual(this.pais, 		((GrupoPais)obj).getPais());
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
		result.append(this.pais);
		result.append(this.operadoraLD);
		result.append(this.servico);

		return result.toString().hashCode();		
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[GrupoPais]");
		result.append("ID_GRUPO=" + this.idGrupo);
		if(pais != null)			result.append(";PAIS=" + this.pais.getNomePais());
		if(servico != null)			result.append(";SERVICO=" + this.servico.getNomeServico());
		if(operadoraLD != null)		result.append(";CSP=" + this.operadoraLD.getNomeOperadora());
		return result.toString();

	}
}
 
