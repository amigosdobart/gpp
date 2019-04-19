package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 *	Entidade <code>RatingServico</code>. Referência: TBL_CON_CDR_SERVICO.
 * 
 *	@author		Lucas Mindêllo de Andrade
 *  Data: 15/04/2008
 */
public class RatingServico implements Serializable
{	
	private String csp = null;
	private Modulacao modulacao = null;
	private Rating tipChamada = null;
	private PlanoPreco planoPreco = null;
	private CodigoServicoSFA codigoServicoSFA = null;
	private String idtConsumoDados = null;

	public RatingServico()
	{
		
	}

	/**
	 * @return String. Referência: NUM_CSP
	 */
	public String getCsp() 
	{
		return csp;
	}

	public void setCsp(String csp)
	{
		this.csp = csp;
	}

	/**
	 * @return Modulacao. Referência: IDT_MODULACAO
	 */
	public Modulacao getModulacao()
	{
		return modulacao;
	}

	public void setModulacao(Modulacao modulacao)
	{
		this.modulacao = modulacao;
	}

	/**
	 * @return Rating. Referência: TIP_CHAMADA
	 */
	public Rating getTipChamada()
	{
		return tipChamada;
	}

	public void setTipChamada(Rating tipChamada)
	{
		this.tipChamada = tipChamada;
	}

	/**
	 * @return String. Referência: IDT_TIPO_SERVICO
	 */
	public String getIdtTipoServico()
	{
		if(codigoServicoSFA == null)
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
	 * @return PlanoPreco. Referência: IDT_PLANO_PRECO
	 */
	public PlanoPreco getPlanoPreco()
	{
		return planoPreco;
	}

	public void setPlanoPreco(PlanoPreco planoPreco)
	{
		this.planoPreco = planoPreco;
	}

	/**
	 * @return CodigoServicoSFA. Referência: IDT_CODIGO_SERVICO_SFA
	 */
	public CodigoServicoSFA getCodigoServicoSFA()
	{
		return codigoServicoSFA;
	}

	public void setCodigoServicoSFA(CodigoServicoSFA codigoServicoSFA)
	{
		this.codigoServicoSFA = codigoServicoSFA;
	}

	/**
	 * @return String. Referência: IDT_CONSUMO_DADOS
	 */
	public String getIdtConsumoDados()
	{
		return idtConsumoDados;
	}

	public void setIdtConsumoDados(String idtConsumoDados)
	{
		this.idtConsumoDados = idtConsumoDados;
	}
	
	public int hashCode()
	{
		String hash = "";
		hash += this.getCsp();
		hash += this.getModulacao().getIdModulacao();
		hash += this.getTipChamada().getTipRate();
		hash += this.getIdtTipoServico();
		hash += this.getPlanoPreco().getIdtPlanoPreco();
		return hash.hashCode();
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof RatingServico))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equals = true;
		equals = equals && ((RatingServico)obj).getCsp().equals(this.getCsp());
		equals = equals && ((RatingServico)obj).getModulacao().equals(this.getModulacao());
		equals = equals && ((RatingServico)obj).getTipChamada().equals(this.getTipChamada());
		equals = equals && ((RatingServico)obj).getIdtTipoServico().equals(this.getIdtTipoServico());
		equals = equals && ((RatingServico)obj).getPlanoPreco().equals(this.getPlanoPreco());
		return equals;
	}
}
