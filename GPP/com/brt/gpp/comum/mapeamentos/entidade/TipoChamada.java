package com.brt.gpp.comum.mapeamentos.entidade;

import java.math.BigDecimal;

/**
 * Entidade <code>TipoChamada</code>. Referência: TBL_GER_RATING
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007 
 */
public class TipoChamada {

	private String idTipoChamada;
	private String desOperacao;
	
	/*
	 * Tarifaval e TipoRate são do tipo 'character 'no banco de dados.
	 * Aqui esses atributos foram mapeados como String, pois:
	 *    a) atributos primitivos não permitem valor null 
	 *    b) Character (objeto), apesar de representar um char, não
	 *       é tratado adequadamente pelo velocity
	 */
	private String tarifavel;
	private String tipoRate;
	
	private BigDecimal fatorConversao;
	
	/**
	 * @return Descrição da operação
	 */
	public String getDesOperacao() 
	{
		return desOperacao;
	}
	
	/**
	 * @param desOperacao Descrição da operação
	 */
	public void setDesOperacao(String desOperacao) 
	{
		this.desOperacao = desOperacao;
	}
	
	/**
	 * @return Fator de conversao
	 */
	public BigDecimal getFatorConversao() 
	{
		return fatorConversao;
	}
	
	/**
	 * @param fatorConversao  Fator de conversao
	 */
	public void setFatorConversao(BigDecimal fatorConversao) 
	{
		this.fatorConversao = fatorConversao;
	}
	
	/**
	 * @return Indicador de tarifável
	 */
	public String getTarifavel() 
	{
		return tarifavel;
	}
	
	/**
	 * @param tarifavel Indicador de tarifável
	 */
	public void setTarifavel(String tarifavel) 
	{
		this.tarifavel = tarifavel;
	}
	
	/**
	 * @return Indicador de tarifável
	 */
	public boolean isTarifavel()
	{
		if (tarifavel == null) return false;
		return (tarifavel.toUpperCase().charAt(0) == 'S') ? true : false;
	}
		
	/**
	 * @return Tipo de chamada
	 */
	public String getIdTipoChamada() 
	{
		return idTipoChamada;
	}
	
	/**
	 * @param tipoChamada Tipo de chamada
	 */
	public void setIdTipoChamada(String tipoChamada) 
	{
		this.idTipoChamada = tipoChamada;
	}
	
	/**
	 * @return Tipo de rate
	 */
	public String getTipoRate() 
	{
		return tipoRate;
	}
	
	/**
	 * @param tipoRate Tipo de rate
	 */
	public void setTipoRate(String tipoRate) 
	{
		this.tipoRate = tipoRate;
	}
	

	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof TipoChamada))
			return false;
		
		if (obj == this)
			return true;
				
		boolean equal = true;		
		equal &= isEqual(this.idTipoChamada, 	((TipoChamada)obj).getIdTipoChamada());
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
		result.append(this.idTipoChamada);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();

		result.append("[TipoChamada]");
		if (idTipoChamada != null)	result.append("TIPO_CHAMADA=" + this.idTipoChamada);
		if (desOperacao != null)	result.append(";DES_OPERACAO=" + this.desOperacao);
		if (tarifavel != null)		result.append(";TARIFAVEL=" + this.tarifavel);
		if (tipoRate != null)		result.append(";TIPO_RATE=" + this.tipoRate);
		result.append(";FATOR_CONVERSAO=" + this.fatorConversao);

		return result.toString();
	}
	
}
