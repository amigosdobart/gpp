package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>BonificacaoChamada</code>. Referência: TBL_TAR_BONIFICACAO_CHAMADA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class BonificacaoChamada implements Serializable
{
	private TipoChamada tipoChamada;
	private OperadoraLD operadoraLD;
	private GrupoBonificacao grupoBonificacao;
		
	/**
	 * @return Instancia de <code>GrupoBonificacao</code>
	 */
	public GrupoBonificacao getGrupoBonificacao() 
	{
		return grupoBonificacao;
	}

	/**
	 * @param grupoBonificacao Instancia de <code>GrupoBonificacao</code>
	 */
	public void setGrupoBonificacao(GrupoBonificacao grupoBonificacao) 
	{
		this.grupoBonificacao = grupoBonificacao;
	}

	/**
	 * @return Instancia de <code>OperadoraLD</code>
	 */
	public OperadoraLD getOperadoraLD() 
	{
		return operadoraLD;
	}

	/**
	 * @param operadoraLD Instancia de <code>OperadoraLD</code>
	 */
	public void setOperadoraLD(OperadoraLD operadoraLD) 
	{
		this.operadoraLD = operadoraLD;
	}

	/**
	 * @return Instancia de <code>TipoChamada</code>
	 */
	public TipoChamada getTipoChamada() 
	{
		return tipoChamada;
	}

	/**
	 * @param tipoChamada Instancia de <code>TipoChamada</code>
	 */
	public void setTipoChamada(TipoChamada tipoChamada) 
	{
		this.tipoChamada = tipoChamada;
	}

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof BonificacaoChamada))
			return false;
		
		if (obj == this)
			return true;
		
		boolean equal = true;	
		equal &= isEqual(this.tipoChamada, 		((BonificacaoChamada)obj).getTipoChamada());
		equal &= isEqual(this.grupoBonificacao, ((BonificacaoChamada)obj).getGrupoBonificacao());
		equal &= isEqual(this.operadoraLD, 		((BonificacaoChamada)obj).getOperadoraLD());
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
		if (tipoChamada != null) result.append(this.tipoChamada.getIdTipoChamada());
		if (operadoraLD != null) result.append(this.operadoraLD.getCsp());
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[GrupoBonificacao]");
	
		if (tipoChamada != null && tipoChamada.getIdTipoChamada() != null) 
			result.append(";TIPO_CHAMADA=" + this.tipoChamada.getIdTipoChamada());

		result.append(";OPERADORA_LD_CSP=" + this.operadoraLD.getCsp());
		
		if (operadoraLD != null && operadoraLD.getNomeOperadora() != null) 
			result.append(";OPERADORA_LD_NOME=" + this.operadoraLD.getNomeOperadora());
		
		if (grupoBonificacao != null && grupoBonificacao.getDesGrupoBonificacao() != null) 
			result.append(";GRUPO_BONIFICACAO=" + this.grupoBonificacao.getDesGrupoBonificacao());
		
		return result.toString();
	}

}
