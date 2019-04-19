package com.brt.gpp.comum;

import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe Timer responsavel por limitar um numero de acoes(ticks) por um intervalo de tempo.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 04/12/2007
 */
public final class Timer extends Thread
{
	private int		interval;
	private int		maxTicks;
	private int		tick;
	private boolean stop;

	// Atributos necessarios para o funcionamento do LOG
	private GerentePoolLog log;
	private long 	idProcesso;
	private String	classe;

	public Timer(int maxTicks, int interval)
	{
		super();
		this.maxTicks	= maxTicks;
		this.interval	= interval;

		// Captura o nome da classe
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		classe 			= sb.substring(sb.lastIndexOf(".")+1);
		// Inicia Sistema de log
		log				= GerentePoolLog.getInstancia(this.getClass());
		idProcesso 		= log.getIdProcesso(this.classe);

		// Inicia a Thread.
		setDaemon(true);
		start();
		log.log(idProcesso, Definicoes.INFO, classe, "start", "Timer iniciado." +
				" Executando com "+this.maxTicks+" ticks por "+this.interval+" ms");
	}

	/**
	 * Executa o <code>Timer</code> resetando-o periodicamente.
	 */
	public void run()
	{
		while(!stop)
		{
			try
			{
				Thread.sleep(interval);
				reset();
			}
			catch (InterruptedException e)
			{
				log.log(idProcesso, Definicoes.ERRO, classe , "run()", e.getMessage());
			}
		}
	}

	/**
	 * Incrementa o numero de acoes. Caso o numero de acoes supere o numero maximo
	 * a <code>thread</code> que chamou o metodo entra em <code>wait</code> ate que o <code>Timer</code>
	 * seja resetado.
	 */
	public void tick()
	{
		synchronized(this)
		{
			if(tick >= maxTicks)
			{
				try
				{
					this.wait();
					tick();
				}
				catch (InterruptedException e)
				{
					log.log(idProcesso, Definicoes.ERRO, classe , "tick()", e.getMessage());
				}
			}
			else
				tick++;
		}
	}

	/**
	 * Acorda as Threads e reinicia o <code>Timer</code>
	 */
	private void reset()
	{
		synchronized(this)
		{
			tick = 0;
			this.notifyAll();
		}
	}

	/**
	 * Finaliza o timer.
	 */
	public void shutdown()
	{
		stop = true;
	}

	/**
	 * Obtem o valor do atributo <code>tick</code><br>
	 * Geralmente utilizado para debug.
	 *
	 * @return Valor de <code>tick</code>
	 */
	public int getTick()
	{
		return tick;
	}
}
