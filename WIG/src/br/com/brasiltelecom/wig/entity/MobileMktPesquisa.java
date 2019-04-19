package br.com.brasiltelecom.wig.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MobileMktPesquisa
{
	private int id;
	private String descricaoPesquisa;
	private Collection mobileMktQuestionarios;
	
	public MobileMktPesquisa()
	{
		mobileMktQuestionarios = new LinkedHashSet();
	}
	
	/**
	 * Metodo....:getmobileMktQuestionario
	 * Descricao.:Retorna o valor de mobileMktQuestionario
	 * @return mobileMktQuestionario.
	 */
	public Collection getMobileMktQuestionarios()
	{
		return mobileMktQuestionarios;
	}

	/**
	 * Metodo....:setmobileMktQuestionario
	 * Descricao.:Define o valor de mobileMktQuestionario
	 * @param mobileMktQuestionario o valor a ser definido para mobileMktQuestionario
	 */
	public void setMobileMktQuestionarios(Collection mobileMktQuestionarios)
	{
		this.mobileMktQuestionarios = mobileMktQuestionarios;
	}

	/**
	 * Metodo....:getdescricaoPesquisa
	 * Descricao.:Retorna o valor de descricaoPesquisa
	 * @return descricaoPesquisa.
	 */
	public String getDescricaoPesquisa()
	{
		return descricaoPesquisa;
	}
	
	/**
	 * Metodo....:setdescricaoPesquisa
	 * Descricao.:Define o valor de descricaoPesquisa
	 * @param descricaoPesquisa o valor a ser definido para descricaoPesquisa
	 */
	public void setDescricaoPesquisa(String descricaoPesquisa)
	{
		this.descricaoPesquisa = descricaoPesquisa;
	}
	
	/**
	 * Metodo....:getid
	 * Descricao.:Retorna o valor de id
	 * @return id.
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Metodo....:setid
	 * Descricao.:Define o valor de id
	 * @param id o valor a ser definido para id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Metodo....:addQuestionario
	 * Descricao.:Adiciona um Questionario a Pesquisa de marketing
	 * @param questionario
	 * @return
	 */
	public boolean addQuestionario(MobileMktQuestionario questionario)
	{
		return this.mobileMktQuestionarios.add(questionario);
	}
	
	/**
	 * Metodo....:getQuestionarioById
	 * Descricao.:Retorna o questionario desejado existente no objeto Pesquisa
	 * @param idQuestionario - id do questionario
	 * @return MobileMktQuestionario
	 */
	public MobileMktQuestionario getQuestionarioById(int idQuestionario)
	{
		// Realiza uma iteracao em todos os questionarios configurados
		// para esta Pesquisa e se o questionario for igual ao id requerido
		// entao ele eh retornado.
		for (Iterator i = getMobileMktQuestionarios().iterator(); i.hasNext();)
		{
			MobileMktQuestionario questionario = (MobileMktQuestionario)i.next();
			if (questionario.getId() == idQuestionario)
				return questionario;
		}
		return null;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return getId();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getDescricaoPesquisa();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof MobileMktPesquisa) )
			return false;
		
		if ( ((MobileMktPesquisa)obj).getId() == this.getId() )
			return true;
		
		return false;
	}
}
 
