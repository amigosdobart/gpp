package com.brt.gpp.comum.mapeamentos.entidade;

/**
 * Entidade <code>TipoCategoria</code>. Referência: TBL_TAR_TIPO_CATEGORIA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007 
 */
public class TipoCategoria {

	private int idTipoCategoria;
	private String desTipoCategoria;
	
	/**
	 * @return Descrição do tipo de categoria
	 */
	public String getDesTipoCategoria() 
	{
		return desTipoCategoria;
	}

	/**
	 * @param desTipoCategoria Descrição do tipo de categoria
	 */
	public void setDesTipoCategoria(String desTipoCategoria) 
	{
		this.desTipoCategoria = desTipoCategoria;
	}

	/**
	 * @return Tipo de categoria
	 */
	public int getIdTipoCategoria() 
	{
		return idTipoCategoria;
	}

	/**
	 * @param tipoCategoria Tipo de categoria
	 */
	public void setIdTipoCategoria(int idTipoCategoria) 
	{
		this.idTipoCategoria = idTipoCategoria;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof TipoCategoria))
			return false;
		
		if (obj == this)
			return true;

		return this.idTipoCategoria == ((TipoCategoria)obj).getIdTipoCategoria();
	}

	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idTipoCategoria);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[TipoCategoria]");
		result.append("ID=" + this.idTipoCategoria);
		if (desTipoCategoria != null)	result.append(";DESCRICAO=" + this.desTipoCategoria);

		return result.toString();
	}
	
}
