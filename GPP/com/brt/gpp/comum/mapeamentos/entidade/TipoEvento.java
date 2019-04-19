package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>TipoEvento</code>. Referência: TBL_TAR_TIPO_EVENTO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class TipoEvento implements Serializable
{
	private String idTipoEvento;
	private String desTipoEvento;
		
	/**
	 * @return ID do tipo de evento
	 */
	public String getIdTipoEvento() 
	{
		return idTipoEvento;
	}
	
	/**
	 * @param TipoEvento ID do tipo de evento
	 */
	public void setIdTipoEvento(String TipoEvento) 
	{
		this.idTipoEvento = TipoEvento;
	}
	
	/**
	 * @return Descricao do tipo de evento
	 */
	public String getDesTipoEvento() 
	{
		return desTipoEvento;
	}
	
	/**
	 * @param desTipoEvento Descricao do tipo de evento
	 */
	public void setDesTipoEvento(String desTipoEvento) 
	{
		this.desTipoEvento = desTipoEvento;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof TipoEvento))
			return false;
		
		if (obj == this)
			return true;
		
		return idTipoEvento == ((TipoEvento)obj).getIdTipoEvento();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idTipoEvento);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[TipoEvento]");
		if (idTipoEvento != null)  result.append("ID=" + this.idTipoEvento);
		if (desTipoEvento != null) result.append(";DESCRICAO=" + this.desTipoEvento);
	
		return result.toString();
	}

}
