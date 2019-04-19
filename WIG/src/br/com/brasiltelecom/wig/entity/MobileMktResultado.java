package br.com.brasiltelecom.wig.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;

public class MobileMktResultado
{
	private int idPesquisa;
	private int idQuestionario;
	private Map perguntasRespostas;
	private String msisdn;
	private Date dataRespostaPesquisa;

	public MobileMktResultado (int idPesquisa, int idQuestionario)
	{
		this.idPesquisa = idPesquisa;
		this.idQuestionario = idQuestionario;
		perguntasRespostas = new HashMap();
	}
	
	public void addResposta(int idPergunta, int idResposta)
	{
		perguntasRespostas.put(new Integer(idPergunta), new Integer(idResposta));
	}
	
	/**
	 * @return Returns the dataRespostaPesquisa.
	 */
	public Date getDataRespostaPesquisa()
	{
		return dataRespostaPesquisa;
	}

	/**
	 * @param dataRespostaPesquisa The dataRespostaPesquisa to set.
	 */
	public void setDataRespostaPesquisa(Date dataRespostaPesquisa)
	{
		this.dataRespostaPesquisa = dataRespostaPesquisa;
	}

	/**
	 * @return Returns the idPesquisa.
	 */
	public int getIdPesquisa()
	{
		return idPesquisa;
	}

	/**
	 * @param idPesquisa The idPesquisa to set.
	 */
	public void setIdPesquisa(int idPesquisa)
	{
		this.idPesquisa = idPesquisa;
	}

	/**
	 * @return Returns the idQuestionario.
	 */
	public int getIdQuestionario()
	{
		return idQuestionario;
	}

	/**
	 * @param idQuestionario The idQuestionario to set.
	 */
	public void setIdQuestionario(int idQuestionario)
	{
		this.idQuestionario = idQuestionario;
	}

	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn()
	{
		return msisdn;
	}

	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	/**
	 * @return Returns the perguntasRespostas.
	 */
	public Map getPerguntasRespostas()
	{
		return perguntasRespostas;
	}

	/**
	 * @param perguntasRespostas The perguntasRespostas to set.
	 */
	public void setPerguntasRespostas(Map perguntasRespostas)
	{
		this.perguntasRespostas = perguntasRespostas;
	}
	
	/**
	 * Metodo....:getIdUltimaPergunta
	 * Descricao.:Retorna o id da ultima pergunta registrada neste resultado
	 * @return int - Id da ultima pergunta
	 */
	public int getIdUltimaPergunta()
	{
		int idComparacao = 0;
		for (Iterator i = perguntasRespostas.keySet().iterator(); i.hasNext();)
		{
			Integer idPergunta = (Integer)i.next();
			if (idPergunta.intValue() > idComparacao)
				idComparacao = idPergunta.intValue();
		}
		return idComparacao;
	}
	
	/**
	 * Metodo....:getIdResposta
	 * Descricao.:Retorna o id da resposta feita pela pergunta especificada
	 * @param idPergunta - Id da pergunta a saber a resposta
	 * @return int - Id da resposta
	 */
	public int getIdResposta(int idPergunta)
	{
		return ((Integer)perguntasRespostas.get(new Integer(idPergunta))).intValue();
	}
}

