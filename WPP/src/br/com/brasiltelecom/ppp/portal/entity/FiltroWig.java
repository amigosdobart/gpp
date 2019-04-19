package br.com.brasiltelecom.ppp.portal.entity;

public class FiltroWig
{
	
	private String nomeClasse;
	private String descricao;
	
	public String getDescricao()
	{
		return descricao;
	}
	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}
	public String getNomeClasse()
	{
		return nomeClasse;
	}
	public void setNomeClasse(String nomeClasse)
	{
		this.nomeClasse = nomeClasse;
	}
	
	public int hashCode()
	{
		return getNomeClasse().hashCode();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof FiltroWig) )
			return false;
		
		if ( ((FiltroWig)obj).getNomeClasse().equals(this.getNomeClasse()) )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getDescricao();
	}
}
