package com.brt.gpp.aplicacoes.promocao.entidade;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

public class PromocaoWhiteList implements Entidade
{
	private String msisdn;
	private boolean isMascara;
	private PromocaoDesconto desconto;
	private String possivesPromocoes[];

	public PromocaoWhiteList()
	{
	}

	public PromocaoDesconto getDesconto()
	{
		return desconto;
	}

	public void setDesconto(PromocaoDesconto desconto)
	{
		this.desconto = desconto;
	}

	public boolean isMascara()
	{
		return isMascara;
	}

	public void setMascara(boolean isMascara)
	{
		this.isMascara = isMascara;
	}

	public String[] getPossivesPromocoes()
	{
		return possivesPromocoes;
	}
	
	public void setPossivesPromocoes(String[] possivesPromocoes)
	{
		this.possivesPromocoes = possivesPromocoes;
	}
	
	public String getMsisdn()
	{
		return msisdn;
	}

	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		PromocaoWhiteList whiteList = new PromocaoWhiteList();
		whiteList.setMsisdn(getMsisdn());
		whiteList.setMascara(isMascara());
		whiteList.setDesconto(getDesconto());
		
		return whiteList;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		String hashStr = getMsisdn() + getDesconto().getIdDesconto();
		return hashStr.hashCode();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof PromocaoWhiteList) )
			return false;
		
		PromocaoWhiteList whiteList = (PromocaoWhiteList)obj;
		if (whiteList.getDesconto().equals(this.getDesconto()) && 
			whiteList.getMsisdn().equals(this.getMsisdn())
			)
			return true;
		
		return false;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Msisdn:"+getMsisdn() + " Desc:"+getDesconto().getDescricao();
	}
}
