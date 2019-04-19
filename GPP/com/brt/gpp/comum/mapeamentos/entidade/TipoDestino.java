package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Entidade <code>TipoDestino</code>. Refer�ncia: TBL_TAR_TIPO_DESTINO
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/03/2007 
 */
public class TipoDestino 
{
 
	private int idTipoDestino;
	private String desTipoDestino;
	
	/**
	 * @return Descri��o do tipo de destino
	 */
	public String getDesTipoDestino() 
	{
		return desTipoDestino;
	}
	
	/**
	 * @param desTipoDestino Descri��o do tipo de destino
	 */
	public void setDesTipoDestino(String desTipoDestino) 
	{
		this.desTipoDestino = desTipoDestino;
	}
	
	/**
	 * @return ID do tipo de destino
	 */
	public int getIdTipoDestino() 
	{
		return idTipoDestino;
	}
	
	/**
	 * @param idTipoDestino ID do tipo de destino
	 */
	public void setIdTipoDestino(int idTipoDestino) 
	{
		this.idTipoDestino = idTipoDestino;
	}
	

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof TipoDestino))
			return false;
		
		if (obj == this)
			return true;
		
		return idTipoDestino == ((TipoDestino)obj).getIdTipoDestino();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idTipoDestino);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[TipoDestino]");
		result.append("ID=" + this.idTipoDestino);
		if (desTipoDestino != null) result.append(";DESCRICAO=" + this.desTipoDestino);
	
		return result.toString();
	}

	
}
 
