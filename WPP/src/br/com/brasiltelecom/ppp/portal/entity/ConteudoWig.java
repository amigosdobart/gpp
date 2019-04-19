package br.com.brasiltelecom.ppp.portal.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConteudoWig
{
	
	private int codConteudo;
	private String descricaoConteudo;
	private boolean registraAcesso;
	private boolean menuOpcoes;
	private long billingCode;
	
	private TipoRespostaWig tipoResposta;
	private ServicoWig servico;
	private RespostaWig resposta;
	private Collection filtrosResposta;
	
	public TipoRespostaWig getTipoResposta() 
	{
		return tipoResposta;
	}

	public void setTipoResposta(TipoRespostaWig tipoResposta) 
	{
		this.tipoResposta = tipoResposta;
	}

	public ServicoWig getServico() 
	{
		return servico;
	}

	public void setServico(ServicoWig servico) 
	{
		this.servico = servico;
	}

	public RespostaWig getResposta()
	{
		return resposta;
	}

	public void setResposta(RespostaWig resposta)
	{
		this.resposta = resposta;
	}

	public long getBillingCode()
	{
		return billingCode;
	}
	
	public void setBillingCode(long billingCode)
	{
		this.billingCode = billingCode;
	}
	
	public int getCodConteudo()
	{
		return codConteudo;
	}
	
	public void setCodConteudo(int codConteudo)
	{
		this.codConteudo = codConteudo;
	}
	
	public String getDescricaoConteudo()
	{
		return descricaoConteudo;
	}
	public void setDescricaoConteudo(String descricaoConteudo)
	{
		this.descricaoConteudo = descricaoConteudo;
	}
	
	public boolean isRegistraAcesso()
	{
		return registraAcesso;
	}
	
	public void setRegistraAcesso(boolean registraAcesso)
	{
		this.registraAcesso = registraAcesso;
	}
	
	public boolean isMenuOpcoes()
	{
		return menuOpcoes;
	}

	public void setMenuOpcoes(boolean menuOpcoes)
	{
		this.menuOpcoes = menuOpcoes;
	}

	public Collection getFiltrosResposta()
	{
		return filtrosResposta;
	}

	public void setFiltrosResposta(Collection filtrosResposta)
	{
		this.filtrosResposta = filtrosResposta;
	}
	
	public int hashCode()
	{
		return this.getCodConteudo();
	}
	
	/**
	 * Metodo....:getRespostasFiltros
	 * Descricao.:Retorna uma lista de respostas distintas, então se uma resposta
	 *            possui mais de um filtro então somente uma unica instancia serah
	 *            retornada
	 * @return Collection
	 */
	public Collection getSomenteRespostas()
	{
		TreeMap listaRespostas = new TreeMap();
		
		for (Iterator i = getFiltrosResposta().iterator(); i.hasNext();)
		{
			FiltroConteudoWig filtro = (FiltroConteudoWig)i.next();
			RespostaWig resposta = filtro.getResposta();
			listaRespostas.put(resposta.getNomeResposta(), resposta);
		}
		return listaRespostas.values();
	}
	
	/*
	public int getNumeroFiltros()
	{
		return getSomenteRespostas().size();
	}
	*/
	
	/**
	 * Metodo....:getFiltrosPorResposta
	 * Descricao.:Retorna a lista de filtros existentes para esta resposta
	 * @param resposta - Resposta a ser listada
	 * @return Collection
	 */
	public Collection getFiltrosPorResposta(RespostaWig resposta)
	{
		TreeMap listaFiltros = new TreeMap();
		
		for (Iterator i = getFiltrosResposta().iterator(); i.hasNext();)
		{
			FiltroConteudoWig filtroConteudo = (FiltroConteudoWig)i.next();
			if (filtroConteudo.getResposta().equals(resposta))
			{
				listaFiltros.put(filtroConteudo.getFiltro().getDescricao(), filtroConteudo);
			}
		}
		return listaFiltros.values();
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof ConteudoWig))
			return false;
		
		if ( ((ConteudoWig)obj).getCodConteudo() == this.getCodConteudo() )
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return this.getDescricaoConteudo();
	}
}
