package com.brt.gpp.comum;

import java.util.Calendar;
import java.util.Date;

/**
 * A classe Cronometro foi desenvolvida para atender praticamente todas as demandas
 * que nescessitam deste tipo de implementacao.
 * E possivel, com a classe Cronometro, obter o numero de segundos, minutos ou horas
 * decorrentes no dia ou em qualquer outra data passada.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 16/01/2008
 */
public class Cronometro
{
	private long inicio;
	/**
	 * Instancia um novo cronometro definindo o tempo inicial como
	 * o mili-segundo atual.
	 *
	 * @see Cronometro#setInicio()
	 */
	public Cronometro()
	{
		this.inicio = Calendar.getInstance().getTimeInMillis();
	}
	/**
	 * Retorna o tempo inicial em mili-segundos.
	 *
	 * @return Tempo inicial em mili-segundos.
	 */
	public long getInicio()
	{
		return inicio;
	}
	/**
	 * Define o tempo inicial de acordo com o Date dado.
	 *
	 * @param data Objeto Date.
	 */
	public void setInicio(Date data)
	{
		setInicio(data.getTime());
	}
	/**
	 * Define o tempo inicial de acordo com o mili-segundo dado
	 *
	 * @param inicio tempo em mili-segundos
	 */
	public void setInicio(long inicio)
	{
		if(inicio > Calendar.getInstance().getTimeInMillis())
			inicio = Calendar.getInstance().getTimeInMillis();

		this.inicio = inicio;
	}
	/**
	 * Define o mili-segundo atual como o tempo inicial.
	 */
	public void setInicio()
	{
		this.inicio = Calendar.getInstance().getTimeInMillis();
	}
	/**
	 * O metodo <code>getTempoDecorrido</code> retorna o tempo decorrido de acordo
	 * com o campo dado.
	 *
	 * @param campo Campo a ser pesquisado. Os possiveis valores sao:
	 * <ul>
	 *   <li>Calendar.DATE
	 *   <li>Calendar.HOUR
	 *   <li>Calendar.MINUTE
	 *   <li>Calendar.SECOND
	 * </ul>
	 * O campo DATE informa o tempo decorrido em dias.
	 * <p>
	 * @return Valor do tempo decorrido de acordo com o campo informado.
	 *
	 * @see Calendar#DATE
	 * @see Calendar#HOUR
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 */
	public long getTempoDecorrido(int campo)
	{
		int divisor = 1;

		if(campo == Calendar.HOUR)
			campo = Calendar.HOUR_OF_DAY;

		switch(campo)
		{
			case Calendar.DATE        : divisor *= 24;
			case Calendar.HOUR_OF_DAY : divisor *= 60;
			case Calendar.MINUTE      : divisor *= 60;
			case Calendar.SECOND      : divisor *= 1000;
		}

		return ((Calendar.getInstance().getTimeInMillis() - inicio)/divisor);
	}
	/**
	 * Retorna o tempo decorrido no formato DDD dia(s) DD hora(s) DD min(s) DD seg(s) DD mili(s).
	 *
	 * @return String contendo a informação do tempo decorrido.
	 */
	public String getTempoDecorrido()
	{
		long mili   = Calendar.getInstance().getTimeInMillis() - inicio;

		long sec	= mili/1000;
		mili	   %= 1000;
		int days	= (int)(sec/86400);
		sec		   %= 86400;
		int hours	= (int)(sec/3600);
		sec		   %= 3600;
		int minutes = (int)(sec/60);
		sec		   %= 60;

		StringBuffer sb = new StringBuffer();
		if(days != 0)
			sb.append(days).append(" dia(s) ");
		if(hours != 0)
			sb.append(hours).append(" hora(s) ");
		if(minutes != 0)
			sb.append(minutes).append(" min(s) ");
		if(sec != 0)
			sb.append(sec).append(" seg(s) ");
		if(mili != 0)
			sb.append(mili).append(" mili(s) ");

		return sb.toString();
	}
	/**
	 * Zera todos os campos de maior precisao a partir do campo dado.
	 * <pre>Cronometro cron = new Cronometro();</pre>
	 * Resultado: <code>12:51:34</code>
	 * <p>
	 * Caso queira zerar o campo hora:
	 * <pre>cron.zerarCampos(Calendar.HOUR);</pre>
	 * Resultado: <code>00:00:00</code>
	 * <p>
	 * @param campo Possiveis valores:
	 * <ul>
	 *   <li>Calendar.HOUR
	 *   <li>Calendar.HOUR_OF_DAY
	 *   <li>Calendar.MINUTE
	 *   <li>Calendar.SECOND
	 *   <li>Calendar.MILLISECOND
	 * </ul>
	 *
	 * @see Calendar#HOUR
	 * @see Calendar#MINUTE
	 * @see Calendar#SECOND
	 * @see Calendar#MILLISECOND
	 */
	public void zerarCampos(int campo)
	{
		if(campo == Calendar.HOUR)
			campo = Calendar.HOUR_OF_DAY;

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(inicio);

		switch(campo)
		{
			case Calendar.HOUR_OF_DAY  : cal.set(Calendar.HOUR_OF_DAY,  0);
			case Calendar.MINUTE       : cal.set(Calendar.MINUTE, 0);
			case Calendar.SECOND       : cal.set(Calendar.SECOND, 0);
			case Calendar.MILLISECOND  : cal.set(Calendar.MILLISECOND, 0);
		}

		this.inicio = cal.getTimeInMillis();
	}
}
