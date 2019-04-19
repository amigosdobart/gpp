package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Entidade <code>NomeDestino</code>. Referência: TBL_GER_RATING_DESTINATION
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class NomeDestino 
{
 
	private String idDestino;
	private String desDestino;
	
	/**
	 * @return Descrição do destino
	 */
	public String getDesDestino() 
	{
		return desDestino;
	}
	
	/**
	 * @param desDestino Descrição do destino
	 */
	public void setDesDestino(String desDestino) 
	{
		this.desDestino = desDestino;
	}
	
	/**
	 * @return ID do destino
	 */
	public String getIdDestino() 
	{
		return idDestino;
	}
	
	/**
	 * @param idDestino ID do destino
	 */
	public void setIdDestino(String idDestino) 
	{
		this.idDestino = idDestino;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof NomeDestino))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= (idDestino == ((NomeDestino)obj).getIdDestino());
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
		result.append(this.idDestino);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[NomeDestino]");
		if (idDestino != null)	result.append("ID=" + this.idDestino);
		if (desDestino != null)	result.append(";DESCRICAO=" + this.desDestino);
	
		return result.toString();
	}
}
 
