package br.com.brasiltelecom.wig.entity;

public class AlteracaoBrtVantagem
{
	private String valorAntigo;
	private String valorNovo;
	private String acao;
	
	public String getAcao()
	{
		return acao;
	}
	
	public String getValorAntigo()
	{
		return valorAntigo;
	}
	
	public String getValorNovo()
	{
		return valorNovo;
	}
	
	public void setAcao(String acao)
	{
		this.acao = acao;
	}
	
	public void setValorAntigo(String valorAntigo)
	{
		this.valorAntigo = valorAntigo;
	}
	
	public void setValorNovo(String valorNovo)
	{
		this.valorNovo = valorNovo;
	}
	
}
