package com.brt.gppAlarmes.aplicacao;

import com.brt.gppAlarmes.entity.Alarme;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.Timer;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * 
 * @author Joao Carlos
 * Data..: 07-Abr-2005
 *
 */
public class AnalisadorAgendamento
{
	private static 	AnalisadorAgendamento 	instance;
	private 		Map						poolTarefas;
	private			Logger					logger;
	private			SimpleDateFormat		sdf;
	
	/**
	 * Metodo....:AnalisadorAgendamento
	 * Descricao.:Construtor padrao da classe
	 *
	 */
	private AnalisadorAgendamento()
	{
		poolTarefas = new HashMap();
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		logger = Logger.getLogger("Alarmes");
	}

	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia da classe (singleton)
	 * @return AnalisadorAgendamento - Instancia da classe
	 */
	public static AnalisadorAgendamento getInstance()
	{
		if (instance == null)
			instance = new AnalisadorAgendamento();
		
		return instance;
	}

	/**
	 * Metodo....:inicializaAlarme
	 * Descricao.:Inicializa a execucao do alarme baseado nas configuracoes do agendamento
	 * @param alarme - Alarme a ser inicializado
	 */
	public void inicializaAlarme(Alarme alarme)
	{
		// Caso o alarme nao possua
		if (alarme.getSQLBuscaContador() == null || alarme.getSQLBuscaContador().equals(""))
			throw new IllegalArgumentException("Alarme nao possui SQL para ser executado");

		// A partir da data de ultima execucao cadastrado para o alarme, busca a proxima data de execucao 
		// ate que esta data seja maior que a data atual, pois entao esta sera utilizada para agendamento 
		// da execucao
		Date dtProximaExecucao = alarme.getAgendamento().getProximoAgendamento(Calendar.getInstance().getTime());
		
		// Cria uma data que sera a proxima execucao apos a data programada para a primeira execucao, ou seja
		// caso o alarme seja executado de meia em meia hora por exemplo ficaria da seguinte forma:
		// dtProximaExecucao = 07/04/2005 10:00:00 entao a dtComparacao e igual a 07/04/2005 10:30:00
		// A partir disso entao a diferenca sera considerada como o intervalo de execucao
		Date dtComparacao = alarme.getAgendamento().getProximoAgendamento(dtProximaExecucao);
		long periodo = dtComparacao.getTime()-dtProximaExecucao.getTime();
		
		// Inicializa o objeto de Timer e inclui em um pool de tarefas para serem posteriormente gerenciadas
		Timer timer = new Timer();
		timer.schedule(new ProcessadorSQLContador(alarme),dtProximaExecucao,periodo);
		logger.info("O SQL definido para o alarme "+alarme+" sera executado em:"+sdf.format(dtProximaExecucao)+" com periodicidade de "+periodo/1000/60+" minutos");
		// Inclui no pool de controle a tarefa criada
		poolTarefas.put(alarme,timer);
	}

	/**
	 * Metodo....:inicializaAlarmes
	 * Descricao.:Inicializa a execucao dos alarmes passados como parametro
	 * @param alarmes - Lista contendo os alarmes a serem inicializados
	 */
	public void inicializaAlarmes(Collection alarmes)
	{
		// Realiza a iteracao para os alarmes passados como parametro para identificar
		// quais alarmes possui comando SQL a ser executado para a busca dos valores
		// de contagem. Para tais alarmes entao eh criado o agendamento para a execucao
		for (Iterator i=alarmes.iterator(); i.hasNext();)
		{
			Alarme alarme = (Alarme)i.next();
			if (alarme.getSQLBuscaContador() != null)
				inicializaAlarme(alarme);
		}
	}

	/**
	 * Metodo....:finalizaAlarmes
	 * Descricao.:Para cada elemento no pool a thread de execucao e terminada
	 *
	 */
	public void finalizaAlarmes()
	{
		// Realiza a iteracao em todos os valores do pool de tarefas para
		// executar o metodo para cancelamento do agendamento
		for (Iterator i = poolTarefas.values().iterator(); i.hasNext();)
		{
			Timer timer = (Timer)i.next();
			timer.cancel();
		}
		logger.info("Agendamentos finalizados.");
	}
}
