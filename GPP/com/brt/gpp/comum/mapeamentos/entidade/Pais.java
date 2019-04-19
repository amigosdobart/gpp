package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Entidade <code>Pais</code>. Referência: TBL_TAR_PAIS
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class Pais 
{
 	private String nomePais;

	/**
	 * @return Nome do país
	 */
	public String getNomePais() 
	{
		return nomePais;
	}

	/**
	 * @param nomePais Nome do país
	 */
	public void setNomePais(String nomePais) 
	{
		this.nomePais = nomePais;
	}
	 

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Pais))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.nomePais, ((Pais)obj).getNomePais());
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
		result.append(this.nomePais);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[Pais]");
		if (nomePais != null) result.append("NOME=" + this.nomePais);
	
		return result.toString();
	}

}
 
