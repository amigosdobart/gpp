package com.brt.gpp.comum.mapeamentos.entidade;

import java.io.Serializable;

/**
 * Entidade <code>CategoriaChamada</code>. Referência: TBL_TAR_CATEGORIA_CHAMADA
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 26/04/2007
 */
public class CategoriaChamada implements Serializable
{
	private int idCategoriaChamada;
	private TipoChamada tipoChamada;
	private ValorDeslocamento valorDeslocamento;
	private TipoCategoria tipoCategoria;
	private TipoChamada tipoChamadaSecundario;
	
	/**
	 * @return ID da categoria de chamada
	 */
	public int getIdCategoriaChamada() 
	{
		return idCategoriaChamada;
	}

	/**
	 * @param idCategoriaChamada ID da categoria de chamada
	 */
	public void setIdCategoriaChamada(int idCategoriaChamada) 
	{
		this.idCategoriaChamada = idCategoriaChamada;
	}

	/**
	 * @return Instancia de <code>TipoCategoria</code>
	 */
	public TipoCategoria getTipoCategoria() 
	{
		return tipoCategoria;
	}

	/**
	 * @param tipoCategoria Instancia de <code>TipoCategoria</code>
	 */
	public void setTipoCategoria(TipoCategoria tipoCategoria) 
	{
		this.tipoCategoria = tipoCategoria;
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
	
	/**
	 * @return Instancia de <code>TipoChamada</code>
	 */
	public TipoChamada getTipoChamadaSecundario() 
	{
		return tipoChamadaSecundario;
	}

	/**
	 * @param tipoChamadaSecundario Instancia de <code>TipoChamada</code>
	 */
	public void setTipoChamadaSecundario(TipoChamada tipoChamadaSecundario) 
	{
		this.tipoChamadaSecundario = tipoChamadaSecundario;
	}

	/**
	 * @return Instancia de <code>ValorDeslocamento</code>
	 */
	public ValorDeslocamento getValorDeslocamento() 
	{
		return valorDeslocamento;
	}

	/**
	 * @param valorDeslocamento Instancia de <code>ValorDeslocamento</code>
	 */
	public void setValorDeslocamento(ValorDeslocamento valorDeslocamento) 
	{
		this.valorDeslocamento = valorDeslocamento;
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof CategoriaChamada))
			return false;
		
		if (obj == this)
			return true;

		return idCategoriaChamada == ((CategoriaChamada)obj).getIdCategoriaChamada();
	}
	
	public int hashCode() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append(this.getClass().getName());
		result.append("||");
		result.append(this.idCategoriaChamada);
		
		return result.toString().hashCode();
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[CategoriaChamada]");
		result.append("ID=" + this.idCategoriaChamada);
		
		if (tipoChamada != null && tipoChamada.getIdTipoChamada() != null) 
			result.append(";TIPO_CHAMADA=" + this.tipoChamada.getIdTipoChamada());
		
		if (tipoChamadaSecundario != null && tipoChamadaSecundario.getIdTipoChamada() != null) 
			result.append(";TIPO_CHAMADA_SECUNDARIO=" + this.tipoChamadaSecundario.getIdTipoChamada());

		if ( (valorDeslocamento != null) && (valorDeslocamento.getDesDeslocamento() != null))
			result.append(";VALOR_DESLOCAMENTO=" + this.valorDeslocamento.getDesDeslocamento());
		
		if (tipoCategoria != null && tipoCategoria.getDesTipoCategoria() != null) 
			result.append(";TIPO_CATEGORIA=" + this.tipoCategoria.getDesTipoCategoria());
		
		return result.toString();
	}

}
