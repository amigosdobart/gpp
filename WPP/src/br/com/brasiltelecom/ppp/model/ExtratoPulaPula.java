package br.com.brasiltelecom.ppp.model;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;

/**
 * Classe que mapeia os dados de extrato
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ExtratoPulaPula
{
	private DescontoPulaPula	desconto;
    private boolean				indEvento;
	private String				originador;
	private Date				timestamp;
	private String				descricao;
	private int					duracao;
	private double				bonus;
	//private PhoneNumberFormat	conversorPhone;

	//Construtores.

	/**
	 *	Construtor da classe.
	 */
	public ExtratoPulaPula()
	{
		this.desconto			= null;
	    this.indEvento			= false;
	    this.originador			= null;
	    this.timestamp			= null;
	    this.descricao			= null;
	    this.duracao			= 0;
	    this.bonus				= 0;
	}

	/**
	 * Retorna o valor de bonus concedido na chamada/evento.
	 *
	 * @return double bonus
	 */
	public double getBonus()
	{
		return bonus;
	}

	/**
	 * Atribui o valor de bonus concedido na chamada/evento.
	 *
	 * @param bonus
	 */
	public void setBonus(double bonus)
	{
		this.bonus = bonus;
	}

	/**
	 * Retorna o tipo de desconto da chamada.
	 * @return DescontoPulaPula desconto
	 */
	public DescontoPulaPula getDesconto()
	{
		return desconto;
	}

	/**
	 * Atribui o tipo de desconto da chamada.
	 * @param desconto
	 */
	public void setDesconto(DescontoPulaPula desconto)
	{
		this.desconto = desconto;
	}

	/**
	 * Retorna o a descrição da chamada/evento.
	 * @return String descricao
	 */
	public String getDescricao()
	{
		return descricao;
	}

	/**
	 * Atribui a descrição da chamada/evento.
	 * @param descricao
	 */
	public void setDescricao(String descricao)
	{
		this.descricao = descricao;
	}

	/**
	 * Retorna a duração da chamada/evento.
	 * @return int duracao
	 */
	public int getDuracao()
	{
		return duracao;
	}

	/**
	 * Atribui a duração da chamada/evento.
	 * @param duracao
	 */
	public void setDuracao(int duracao)
	{
		this.duracao = duracao;
	}

	/**
	 * Retorna indicativo de evento.
	 * @return boolean indEvento
	 */
	public boolean isIndEvento()
	{
		return indEvento;
	}

	/**
	 * Atribui indicativo de evento.
	 * @param indEvento
	 */
	public void setIndEvento(boolean indEvento)
	{
		this.indEvento = indEvento;
	}

	/**
	 * Retorna o número do originador da chamada
	 * @return String originador
	 */
	public String getOriginador()
	{
		return originador;
	}

	/**
	 * Atribui o número do originador da chamada
	 * @param originador
	 */
	public void setOriginador(String originador)
	{
		this.originador = originador;
	}

	/**
	 * Retorna a data/hora da chamada/evento.
	 * @return Date timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Atribui a data/hora da chamada/evento.
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 *	Indica se o registro se trata de um evento.
	 *
	 *	@return		boolean					indEvento					Indicador de recarga.
	 */
	public boolean ehEvento()
	{
		return this.indEvento;
	}

	/**
	 * Retorna a data da chamada/evento.
	 * @return
	 */
	public String getData()
	{
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(timestamp);
	}

	/**
	 * Retorna a hora da chamada/evento.
	 * @return
	 */
	public String getHora()
	{
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(timestamp);
	}
}
