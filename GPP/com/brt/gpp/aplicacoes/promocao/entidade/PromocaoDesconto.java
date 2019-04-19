package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

public class PromocaoDesconto implements Entidade
{
	private int idDesconto;
	private String descricao;
	
	public PromocaoDesconto()
	{
	}

	public String getDescricao()
	{
		return descricao;
	}

	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	public int getIdDesconto()
	{
		return idDesconto;
	}

	public void setIdDesconto(int idDesconto)
	{
		this.idDesconto = idDesconto;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		PromocaoDesconto prom = new PromocaoDesconto();
		prom.setIdDesconto	(getIdDesconto());
		prom.setDescricao	(getDescricao());
		
		return prom;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return getIdDesconto();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof PromocaoDesconto) )
			return false;
		
		if ( ((PromocaoDesconto)obj).getIdDesconto() == this.getIdDesconto() )
			return true;
		
		return false;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getDescricao();
	}
}
