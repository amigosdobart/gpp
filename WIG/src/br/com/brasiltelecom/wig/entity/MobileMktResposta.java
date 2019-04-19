package br.com.brasiltelecom.wig.entity;

public class MobileMktResposta
{
	private int id;
	private String descricaoResposta;
	private String cardReference;
	private int idQuestionarioReferencia;
	private MobileMktPergunta mobileMktPergunta;
	
	/**
	 * Metodo....:getcardReference
	 * Descricao.:Retorna o valor de cardReference
	 * @return cardReference.
	 */
	public String getCardReference()
	{
		return cardReference;
	}

	/**
	 * Metodo....:setcardReference
	 * Descricao.:Define o valor de cardReference
	 * @param cardReference o valor a ser definido para cardReference
	 */
	public void setCardReference(String cardReference)
	{
		this.cardReference = cardReference;
	}

	/**
	 * Metodo....:getdescricaoResposta
	 * Descricao.:Retorna o valor de descricaoResposta
	 * @return descricaoResposta.
	 */
	public String getDescricaoResposta()
	{
		return descricaoResposta;
	}

	/**
	 * Metodo....:setdescricaoResposta
	 * Descricao.:Define o valor de descricaoResposta
	 * @param descricaoResposta o valor a ser definido para descricaoResposta
	 */
	public void setDescricaoResposta(String descricaoResposta)
	{
		this.descricaoResposta = descricaoResposta;
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
	public MobileMktPergunta getMobileMktPergunta()
	{
		return mobileMktPergunta;
	}

	/**
	 * Metodo....:setmobileMktPergunta
	 * Descricao.:Define o valor de mobileMktPergunta
	 * @param mobileMktPergunta o valor a ser definido para mobileMktPergunta
	 */
	public void setMobileMktPergunta(MobileMktPergunta mobileMktPergunta)
	{
		this.mobileMktPergunta = mobileMktPergunta;
	}

	/**
	 * Metodo....:getquestionarioResposta
	 * Descricao.:Retorna o valor de questionarioResposta
	 * @return questionarioResposta.
	 */
	public MobileMktQuestionario getQuestionarioResposta()
	{
		// Este metodo retorna o questionario referencia somente
		// neste devido a instanciacao adiada devido a nem
		// todos os questionarios estarem carregados na memoria
		return getMobileMktPergunta().getMobileMktQuestionario().getMobileMktPesquisa().getQuestionarioById(getIdQuestionarioReferencia());
	}

	/**
	 * Metodo....:getidQuestionarioReferencia
	 * Descricao.:Retorna o valor de idQuestionarioReferencia
	 * @return idQuestionarioReferencia.
	 */
	public int getIdQuestionarioReferencia()
	{
		return idQuestionarioReferencia;
	}

	/**
	 * Metodo....:setidQuestionarioReferencia
	 * Descricao.:Define o valor de idQuestionarioReferencia
	 * @param idQuestionarioReferencia o valor a ser definido para idQuestionarioReferencia
	 */
	public void setIdQuestionarioReferencia(int idQuestionarioReferencia)
	{
		this.idQuestionarioReferencia = idQuestionarioReferencia;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return this.getId();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.getDescricaoResposta();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof MobileMktResposta) )
			return false;
		
		MobileMktResposta resposta = (MobileMktResposta)obj;
		if (resposta.getMobileMktPergunta().equals(this.getMobileMktPergunta()) && resposta.getId() == this.getId())
			return true;
		
		return false;
	}
	
	public int compareTo(Object obj)
	{
		if ( !(obj instanceof MobileMktResposta) )
			throw new ClassCastException("Classe invalida para comparacao de Resposta. Classe:"+obj.getClass().getName());
		
		MobileMktResposta resp = (MobileMktResposta)obj;
		return new Integer(this.getId()).compareTo(new Integer(resp.getId()));
	}
}
