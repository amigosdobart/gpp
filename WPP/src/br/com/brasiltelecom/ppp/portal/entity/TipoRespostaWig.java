package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;

public class TipoRespostaWig implements Serializable
{
	private int codTipoResposta;
	private String desTipoResposta;
	private char agrupaConteudo;

	public char getAgrupaConteudo() 
	{
		return agrupaConteudo;
	}

	public void setAgrupaConteudo(char agrupaConteudo) 
	{
		this.agrupaConteudo = agrupaConteudo;
	}

	public int getCodTipoResposta() 
	{
		return codTipoResposta;
	}

	public void setCodTipoResposta(int codTipoResposta) 
	{
		this.codTipoResposta = codTipoResposta;
	}

	public String getDesTipoResposta() 
	{
		return desTipoResposta;
	}

	public void setDesTipoResposta(String desTipoResposta) 
	{
		this.desTipoResposta = desTipoResposta;
	}
	
	public boolean isAgrupaConteudo()
	{
		return (agrupaConteudo == 'S' || agrupaConteudo == 's') ? true : false;
	}
	
	public int hashCode()
	{
		return getCodTipoResposta();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof TipoRespostaWig) )
			return false;
		
		if ( ((TipoRespostaWig)obj).getCodTipoResposta() == this.getCodTipoResposta() )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getDesTipoResposta();
	}
}
