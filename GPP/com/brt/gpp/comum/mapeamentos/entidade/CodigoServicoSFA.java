package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Entidade <code>CodigoServicoSFA</code>. Referência: TBL_CON_CODIGO_SERVICO_SFA.
 * 
 *	@author		Bernardo Vergne Dias
 *  Data: 29/09/2007
 */
public class CodigoServicoSFA implements Serializable
{
	private static final long serialVersionUID = -2868549475716164976L;

	/**
	 *	Numero identificador do servico SFA.
	 */
	private int idtCodigoServicoSFA;
	
	/**
	 *	Descricao do servico SFA.
	 */
	private String desCodigoServico;
	
	/**
	 *	Identificador do tipo de servico SFA.
	 */
	private String idtTipoServico;
	
	/**
	 *	Categoria do servico SFA.
	 */
	private String idtCategoria;
	
	/**
	 *	Tipo de registro.
	 */
	private String tipoRegistro;
	
	/**
	 * @return Descricao do servico SFA.
	 */
	public String getDesCodigoServico() 
	{
		return desCodigoServico;
	}

	/**
	 * @param desCodigoServico Descricao do servico SFA.
	 */
	public void setDesCodigoServico(String desCodigoServico) 
	{
		this.desCodigoServico = desCodigoServico;
	}

	/**
	 * @return Categoria do servico SFA.
	 */
	public String getIdtCategoria() 
	{
		return idtCategoria;
	}

	/**
	 * @param idtCategoria Categoria do servico SFA.
	 */
	public void setIdtCategoria(String idtCategoria) 
	{
		this.idtCategoria = idtCategoria;
	}

	/**
	 * @return Numero identificador do servico SFA.
	 */
	public int getIdtCodigoServicoSFA() 
	{
		return idtCodigoServicoSFA;
	}

	/**
	 * @param idtCodigoServicoSFA Numero identificador do servico SFA.
	 */
	public void setIdtCodigoServicoSFA(int idtCodigoServicoSFA) 
	{
		this.idtCodigoServicoSFA = idtCodigoServicoSFA;
	}

	/**
	 * @return Identificador do tipo de servico SFA.
	 */
	public String getIdtTipoServico() 
	{
		return idtTipoServico;
	}

	/**
	 * @param idtTipoServico Identificador do tipo de servico SFA.
	 */
	public void setIdtTipoServico(String idtTipoServico) 
	{
		this.idtTipoServico = idtTipoServico;
	}

	/**
	 * @return Tipo de registro.
	 */
	public String getTipoRegistro() 
	{
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro Tipo de registro.
	 */
	public void setTipoRegistro(String tipoRegistro) 
	{
		this.tipoRegistro = tipoRegistro;
	}

	/**
	 *	@see		java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof CodigoServicoSFA))
			return false;
		
		if (obj == this)
			return true;

		return this.idtCodigoServicoSFA == ((CodigoServicoSFA)obj).getIdtCodigoServicoSFA();
	}
	
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return this.idtCodigoServicoSFA;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[CodigoServicoSFA]");
		result.append("CODIGO_SFA=" + this.idtCodigoServicoSFA);
		if(this.idtTipoServico != null)
			result.append(";TIPO_SERVICO=" + this.idtTipoServico);
		if(this.desCodigoServico != null)
			result.append(";DESCRICAO=" + this.desCodigoServico);
		if(this.idtCategoria != null)
			result.append(";CATEGORIA=" + this.idtCategoria);
		if(this.tipoRegistro != null)
			result.append(";TIPO_REGISTRO=" + this.tipoRegistro);
		
		return result.toString();
	}

}
