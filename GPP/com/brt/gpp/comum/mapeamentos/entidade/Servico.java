package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Entidade <code>Servico</code>. Referência: TBL_TAR_SERVICO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class Servico 
{
 
	private int idServico;
	private String nomeServico;
	
	/**
	 * @return ID do serviço
	 */
	public int getIdServico() 
	{
		return idServico;
	}
	
	/**
	 * @param idServico ID do serviço
	 */
	public void setIdServico(int idServico) 
	{
		this.idServico = idServico;
	}
	
	/**
	 * @return Nome do serviço
	 */
	public String getNomeServico() 
	{
		return nomeServico;
	}
	
	/**
	 * @param nomeServico Nome do serviço
	 */
	public void setNomeServico(String nomeServico) 
	{
		this.nomeServico = nomeServico;
	}
	 

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Servico))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= (idServico == ((Servico)obj).getIdServico());
		return equal;
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idServico);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Servico]");
		result.append("ID=" + this.idServico);
		if (nomeServico != null) result.append(";NOME=" + this.nomeServico);
	
		return result.toString();
	}

}
 
