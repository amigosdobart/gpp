package com.brt.gppAlarmes.aplicacao;

import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.conexoes.Configuracao;
import com.brt.gppAlarmes.dao.AlarmeDAO;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Esta classe e responsavel pelo controle de processamento da analise
 * de alarmes. E atraves dessa que e feito o controle de threads para
 * a analise dos eventos de alarmes para identificacao de situacao
 *  
 * @author Joao Carlos
 * Data..: 28-Mar-2005
 *
 */
public class AnalisadorAlarmes implements Runnable
{
	private ThreadGroup threadGroupProc;

	/**
	 * Metodo....:analisaAlarmes
	 * Descricao.:Realiza a analise dos alarmes cadastrados na base de dados. Esse metodo controla o numero
	 *            de threads que irao processar os alarmes sendo que caso o numero maximo de threads seja
	 *            alcancado entao um tempo de espera e aplicado ate que alguma outra thread tenha sido terminada
	 *            liberando entao para o proximo processamento
	 * @param tempoEspera - Tempo de espera se o numero maximo de threads for alcancado
	 * @param numThreads  - Numero de threads que irao processar a analise dos alarmes
	 * @throws Exception
	 */
	public void analisaAlarmes(int tempoEspera, int numThreads) throws Exception
	{
		Logger logger = Logger.getLogger("Alarmes");
		
		// Realiza a busca de todos os alarmes a serem analisados
		// Essa pesquisa e feita nesse ponto para que nao seja instanciado
		// a cada iteracao de tempo todos os alarmes novamente.
		Collection listaAlarmes = AlarmeDAO.getInstance().findAll();

		// Para o processamento, cada alarme sera repassado para uma thread de processamento
		// sendo que o limite de threads definido sera respeitado e quando esse limite for 
		// atingido entao o sistema fica em espera ate que uma delas termine seu ciclo de vida
		// possibilitando entao processar o proximo alarme
		for (Iterator i=listaAlarmes.iterator(); i.hasNext();)
		{
			Alarme alarme = (Alarme)i.next();
			// Se o numero de threads em processamento for igual ao numero de threads
			// maximo definido entao a execucao espera um certo tempo ate que no grupo
			// haja disponibilidade para mais threads serem criadas
			while (threadGroupProc.activeCount() >= numThreads)
				Thread.sleep(tempoEspera*1000);

			// Realiza o processamento do alarme
			logger.info("Realizando analise para o alarme "+alarme);
			Thread t = new Thread(threadGroupProc,new ProcessadorAnalise(alarme));
			t.start();
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		Logger logger = Logger.getLogger("Alarmes");
		try
		{
			Configuracao conf = Configuracao.getInstance();
			// Identifica o numero de threads que serao utilizados para o processamento
			// e o tempo de espera caso o numero de threads maximo seja alcancado
			int numThreads 	= Integer.parseInt(conf.getPropriedade("alarmes.app.numThreads"));
			int tempoEspera	= Integer.parseInt(conf.getPropriedade("alarmes.app.tempoEspera"));
			int intervalo	= Integer.parseInt(conf.getPropriedade("alarmes.app.intervalo"));

			// Inicia o threadGroup para controle de threads de processamento
			threadGroupProc = new ThreadGroup("Processamento Alarmes");
			while(true)
			{
				logger.info("Realizando analise dos alarmes");
				analisaAlarmes(tempoEspera,numThreads);
				// Enquanto houver threads em processamento nao inicia o proximo ciclo de analise
				while (threadGroupProc.activeCount() > 0)
					synchronized(this){
						wait(tempoEspera*1000);}

				// Apos o processamento de todos os alarmes, o processamento e interrompido
				// pelo intervalo definido para entao comecar novo ciclo de processamento
				logger.info("Intervalo de processamento. "+intervalo+" segundos");
				synchronized(this){
					wait(intervalo*1000);}
			}
		}
		catch(InterruptedException ie)
		{
			logger.info("Thread de analise de alarmes interrompida.");
		}
		catch(Exception e)
		{
			logger.error("Erro na thread de analise de Alarmes. Erro:"+e);
		}
	}
}
