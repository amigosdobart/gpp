package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Entidade <code>RecargaServico</code>. Referência: TBL_CON_RECARGA_SERVICO.
 * 
 *	@author		Bernardo Vergne Dias
 *  Data: 29/09/2007
 */
public class RecargaServico implements Serializable
{
	private static final long serialVersionUID = -6107103803872411421L;

	/**
	 *	Origem de recarga.
	 */
	private OrigemRecarga origem;
	
	/**
	 *	Codigo de Servico SFA.
	 */
	private CodigoServicoSFA codigoServicoSFA;
	
	/**
	 *	Plano de preco.
	 */
	private PlanoPreco planoPreco;
	
	/**
	 *	Distema de origem.
	 */
	private SistemaOrigem sistemaOrigem;

	/**
	 * @return Codigo de Servico SFA.
	 */
	public CodigoServicoSFA getCodigoServicoSFA() 
	{
		return codigoServicoSFA;
	}

	/**
	 * @param codigoServicoSFA Codigo de Servico SFA.
	 */
	public void setCodigoServicoSFA(CodigoServicoSFA codigoServicoSFA) 
	{
		this.codigoServicoSFA = codigoServicoSFA;
	}

	/**
	 * @return Sistema de origem.
	 */
	public SistemaOrigem getSistemaOrigem() 
	{
		return sistemaOrigem;
	}

	/**
	 * @param sistemaOrigem Sistema de origem.
	 */
	public void setSistemaOrigem(SistemaOrigem sistemaOrigem) 
	{
		this.sistemaOrigem = sistemaOrigem;
	}

	/**
	 * Esse campo equivale a codigoServicoSFA.getIdtTipoServico().
	 *  
	 * @return Identificador do tipo de servico SFA.
	 */
	public String getIdtTipoServico() 
	{
		if (codigoServicoSFA == null)
			return null;
		return codigoServicoSFA.getIdtTipoServico();
	}

	/**
	 * Esse campo codigoServicoSFA.setIdtTipoServico().
	 * Evite usar esse metodo! <br>
	 * 
	 * NOTA: remocao desse metodo causa erro no mapeamento hibernate.
	 * 
	 * @param idtTipoServico Identificador do tipo de servico SFA.
	 */
	public void setIdtTipoServico(String idtTipoServico) 
	{
		if (codigoServicoSFA == null)
		{
			codigoServicoSFA = new CodigoServicoSFA();
		}
		codigoServicoSFA.setIdtTipoServico(idtTipoServico);
	}

	/**
	 * @return Origem de recarga.
	 */
	public OrigemRecarga getOrigem() 
	{
		return origem;
	}

	/**
	 * @param origem Origem de recarga.
	 */
	public void setOrigem(OrigemRecarga origem) 
	{
		this.origem = origem;
	}

	/**
	 * @return Plano de preco.
	 */
	public PlanoPreco getPlanoPreco() 
	{
		return planoPreco;
	}

	/**
	 * @param planoPreco Plano de preco.
	 */
	public void setPlanoPreco(PlanoPreco planoPreco) 
	{
		this.planoPreco = planoPreco;
	}

	/**
	 *	@see		java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof RecargaServico))
			return false;
		
		if (obj == this)
			return true;
				
		boolean equal = true;	
		equal &= isEqual(this.origem, 			    ((RecargaServico)obj).getOrigem());
		equal &= isEqual(this.planoPreco, 		    ((RecargaServico)obj).getPlanoPreco());
		equal &= isEqual(this.sistemaOrigem,   		((RecargaServico)obj).getSistemaOrigem());
		equal &= isEqual(this.getIdtTipoServico(),  ((RecargaServico)obj).getIdtTipoServico());
		
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
	
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		StringBuffer buffer = new StringBuffer();
		if (origem != null)				buffer.append(origem.hashCode());
		if (planoPreco != null) 		buffer.append(planoPreco.hashCode());
		if (sistemaOrigem != null) 		buffer.append(sistemaOrigem.hashCode());
		if (getIdtTipoServico() != null)buffer.append(getIdtTipoServico().hashCode());
		
		return buffer.toString().hashCode();
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[RecargaServico]");
		if (this.origem != null)
			result.append("ORIGEM=" + origem.getIdCanal() + origem.getIdOrigem());
		if(this.codigoServicoSFA != null)
		{
			result.append(";CODIGO_SFA=" + codigoServicoSFA.getIdtCodigoServicoSFA());
			result.append(";TIPO_SERVICO=" + getIdtTipoServico());
		}
		if(this.sistemaOrigem != null)
			result.append(";SISTEMA_ORIGEM=" + this.sistemaOrigem.getIdSistemaOrigem());
		if(this.planoPreco != null)
			result.append(";PLANO_PRECO=" + planoPreco.getIdtPlanoPreco());

		return result.toString();
	}

}
