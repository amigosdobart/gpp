package br.com.brasiltelecom.wig.entity;

/**
 * Esta classe define a estrutura de armazenamento da resposta. No campo descricaoResposta
 * esta a URL que sera chamada para execucao da resposta ou possui o valor do WML resultado
 * no caso de URL a classe ira realizar a chamada e o resultado sera devolvido como um CARD.
 * Para casos onde o WML ja reside entao somente um parse eh realizado para identificar o CARD
 * 
 * @author Joao Carlos
 * Data..: 31/05/2005
 *
 */
public class Resposta implements Comparable
{
	private int 	codigoResposta;
	private int		tipoResposta;
	private String	descricaoResposta;
	private boolean	agrupaConteudo;
	private boolean executaResposta;
	
	public Resposta(int codigoResposta)
	{
		this.codigoResposta = codigoResposta;
	}
	
	public int getCodigoResposta()
	{
		return codigoResposta;
	}
	
	public String getDescricaoResposta()
	{
		return descricaoResposta;
	}
	
	public int getTipoResposta()
	{
		return tipoResposta;
	}

	public boolean agrupaConteudo()
	{
		return agrupaConteudo;
	}

	public boolean executaResposta()
	{
		return executaResposta;
	}

	public void setCodigoResposta(int codigoResposta)
	{
		this.codigoResposta = codigoResposta;
	}
	
	public void setTipoResposta(int tipoResposta)
	{
		this.tipoResposta = tipoResposta;
	}
	
	public void setDescricaoResposta(String descricaoResposta)
	{
		this.descricaoResposta = descricaoResposta;
	}
	
	public void setAgrupaConteudo(boolean agrupaConteudo)
	{
		this.agrupaConteudo = agrupaConteudo;
	}
	
	public void setExecutaResposta(boolean executaResposta)
	{
		this.executaResposta = executaResposta;
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Resposta))
			return false;
		
		if ( ((Resposta)obj).getCodigoResposta() == this.getCodigoResposta() )
			return true;
		return false;
	}
	
	public int hashCode()
	{
		return this.getCodigoResposta();
	}

	public String toString()
	{
		return "Resposta:"+this.getCodigoResposta();
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof Resposta) )
			return 0;
		
		if ( this.getCodigoResposta() > ((Resposta)obj).getCodigoResposta() )
			return 1;
		if ( this.getCodigoResposta() < ((Resposta)obj).getCodigoResposta() )
			return -1;
		
		return 0;
	}
}
