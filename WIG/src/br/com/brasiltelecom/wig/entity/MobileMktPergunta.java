package br.com.brasiltelecom.wig.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MobileMktPergunta implements Comparable
{
	private int id;
	private String descricaoPergunta;
	private String titulo;
	private MobileMktQuestionario mobileMktQuestionario;
	private Collection mobileMktRespostas;
	
	public MobileMktPergunta()
	{
		mobileMktRespostas = new LinkedHashSet();
	}

	/**
	 * Metodo....:getdescricaoPergunta
	 * Descricao.:Retorna o valor de descricaoPergunta
	 * @return descricaoPergunta.
	 */
	public String getDescricaoPergunta()
	{
		return descricaoPergunta;
	}

	/**
	 * Metodo....:setdescricaoPergunta
	 * Descricao.:Define o valor de descricaoPergunta
	 * @param descricaoPergunta o valor a ser definido para descricaoPergunta
	 */
	public void setDescricaoPergunta(String descricaoPergunta)
	{
		this.descricaoPergunta = descricaoPergunta;
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
	 * Metodo....:getmobileMktQuestionario
	 * Descricao.:Retorna o valor de mobileMktQuestionario
	 * @return mobileMktQuestionario.
	 */
	public MobileMktQuestionario getMobileMktQuestionario()
	{
		return mobileMktQuestionario;
	}

	/**
	 * Metodo....:setmobileMktQuestionario
	 * Descricao.:Define o valor de mobileMktQuestionario
	 * @param mobileMktQuestionario o valor a ser definido para mobileMktQuestionario
	 */
	public void setMobileMktQuestionario(MobileMktQuestionario mobileMktQuestionario)
	{
		this.mobileMktQuestionario = mobileMktQuestionario;
	}

	/**
	 * Metodo....:getmobileMktResposta
	 * Descricao.:Retorna o valor de mobileMktResposta
	 * @return mobileMktResposta.
	 */
	public Collection getMobileMktRespostas()
	{
		return mobileMktRespostas;
	}

	/**
	 * Metodo....:setmobileMktResposta
	 * Descricao.:Define o valor de mobileMktResposta
	 * @param mobileMktResposta o valor a ser definido para mobileMktResposta
	 */
	public void setMobileMktRespostas(Collection mobileMktRespostas)
	{
		this.mobileMktRespostas = mobileMktRespostas;
	}

	/**
	 * Metodo....:gettitulo
	 * Descricao.:Retorna o valor de titulo
	 * @return titulo.
	 */
	public String getTitulo()
	{
		return titulo;
	}

	/**
	 * Metodo....:settitulo
	 * Descricao.:Define o valor de titulo
	 * @param titulo o valor a ser definido para titulo
	 */
	public void setTitulo(String titulo)
	{
		this.titulo = titulo;
	}
	
	/**
	 * Metodo....:addResposta
	 * Descricao.:Adiciona uma resposta a pergunta
	 * @param resposta - Resposta a ser adicionada
	 * @return
	 */
	public boolean addResposta(MobileMktResposta resposta)
	{
		return this.mobileMktRespostas.add(resposta);
	}
	
	/**
	 * Metodo....:getRespostasById
	 * Descricao.:Retorna a resposta desejada existente no objeto Pergunta
	 * @param idResposta - id da resposta
	 * @return MobileMktResposta
	 */
	public MobileMktResposta getRespostaById(int idResposta)
	{
		// Realiza uma iteracao em todos as respostas configurados
		// para esta Pergunta e se a resposta for igual ao id requerido
		// entao ele eh retornado.
		for (Iterator i = getMobileMktRespostas().iterator(); i.hasNext();)
		{
			MobileMktResposta resposta = (MobileMktResposta)i.next();
			if (resposta.getId() == idResposta)
				return resposta;
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
		return getDescricaoPergunta();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof MobileMktPergunta) )
			return false;
		
		MobileMktPergunta perg = (MobileMktPergunta)obj;
		if (perg.getMobileMktQuestionario().equals(this.getMobileMktQuestionario()) && perg.getId() == this.getId() )
			return true;
		
		return false;
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof MobileMktPergunta) )
			throw new ClassCastException("Classe invalida para comparacao de Pergunta. Classe:"+obj.getClass().getName());
		
		MobileMktPergunta perg = (MobileMktPergunta)obj;
		return new Integer(this.getId()).compareTo(new Integer(perg.getId()));
	}
}

