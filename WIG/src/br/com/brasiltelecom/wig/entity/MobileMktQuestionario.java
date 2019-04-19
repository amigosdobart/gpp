package br.com.brasiltelecom.wig.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MobileMktQuestionario implements Comparable
{
	private int id;
	private String mensagem;
	private String textoExplicativo;
	private MobileMktPesquisa mobileMktPesquisa;
	private Collection mobileMktPerguntas;

	public MobileMktQuestionario()
	{
		mobileMktPerguntas = new LinkedHashSet();
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
	 * Metodo....:getmobileMktPergunta
	 * Descricao.:Retorna o valor de mobileMktPergunta
	 * @return mobileMktPergunta.
	 */
	public Collection getMobileMktPerguntas()
	{
		return mobileMktPerguntas;
	}

	/**
	 * Metodo....:setmobileMktPergunta
	 * Descricao.:Define o valor de mobileMktPergunta
	 * @param mobileMktPergunta o valor a ser definido para mobileMktPergunta
	 */
	public void setMobileMktPerguntas(Collection mobileMktPerguntas)
	{
		this.mobileMktPerguntas = mobileMktPerguntas;
	}

	/**
	 * Metodo....:getmobileMktPesquisa
	 * Descricao.:Retorna o valor de mobileMktPesquisa
	 * @return mobileMktPesquisa.
	 */
	public MobileMktPesquisa getMobileMktPesquisa()
	{
		return mobileMktPesquisa;
	}

	/**
	 * Metodo....:setmobileMktPesquisa
	 * Descricao.:Define o valor de mobileMktPesquisa
	 * @param mobileMktPesquisa o valor a ser definido para mobileMktPesquisa
	 */
	public void setMobileMktPesquisa(MobileMktPesquisa mobileMktPesquisa)
	{
		this.mobileMktPesquisa = mobileMktPesquisa;
	}
	
	public boolean addPergunta(MobileMktPergunta pergunta)
	{
		return this.mobileMktPerguntas.add(pergunta);
	}
	
	/**
	 * Metodo....:getmensagem
	 * Descricao.:Retorna o valor de mensagem
	 * @return mensagem.
	 */
	public String getMensagem()
	{
		return mensagem;
	}

	/**
	 * Metodo....:setmensagem
	 * Descricao.:Define o valor de mensagem
	 * @param mensagem o valor a ser definido para mensagem
	 */
	public void setMensagem(String mensagem)
	{
		this.mensagem = mensagem;
	}
	
	/**
	 * Metodo....:gettextoExplicativo
	 * Descricao.:Retorna o valor de textoExplicativo
	 * @return textoExplicativo.
	 */
	public String getTextoExplicativo()
	{
		return textoExplicativo;
	}

	/**
	 * Metodo....:settextoExplicativo
	 * Descricao.:Define o valor de textoExplicativo
	 * @param textoExplicativo o valor a ser definido para textoExplicativo
	 */
	public void setTextoExplicativo(String textoExplicativo)
	{
		this.textoExplicativo = textoExplicativo;
	}
	
	/**
	 * Metodo....:getPerguntaById
	 * Descricao.:Retorna a pergunta desejada existente no objeto Questionario
	 * @param idPergunta - id da pergunta
	 * @return MobileMktPergunta
	 */
	public MobileMktPergunta getPerguntaById(int idPergunta)
	{
		// Realiza uma iteracao em todos as perguntas configurados
		// para este Questionario e se a pergunta for igual ao id requerido
		// entao ele eh retornado.
		for (Iterator i = getMobileMktPerguntas().iterator(); i.hasNext();)
		{
			MobileMktPergunta pergunta = (MobileMktPergunta)i.next();
			if (pergunta.getId() == idPergunta)
				return pergunta;
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
		return this.mobileMktPesquisa.getDescricaoPesquisa() + " Quest:" + this.id;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof MobileMktQuestionario))
			return false;
		
		MobileMktQuestionario quest = (MobileMktQuestionario)obj;
		if (quest.getMobileMktPesquisa().equals(this.getMobileMktPesquisa()) && quest.getId() == this.getId())
			return true;
		
		return false;
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof MobileMktQuestionario) )
			throw new ClassCastException("Classe invalida para comparacao de Questionario. Classe:"+obj.getClass().getName());
		
		MobileMktQuestionario quest = (MobileMktQuestionario)obj;
		return new Integer(this.getId()).compareTo(new Integer(quest.getId()));
	}
}
