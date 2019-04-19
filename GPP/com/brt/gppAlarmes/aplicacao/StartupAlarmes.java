package com.brt.gppAlarmes.aplicacao;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.brt.gppAlarmes.conexoes.Configuracao;
import com.brt.gppAlarmes.dao.AlarmeDAO;
import com.brt.gppAlarmes.dao.EventoDAO;

/**
 * Esta classe realiza o inicio do modulo de analise e agendamento de execucao
 * de alarmes.
 * 
 * @author Joao Carlos
 * Data..: 11-Abr-2005
 *
 */
public class StartupAlarmes
{
	private static 	StartupAlarmes 	instance;
	private 		Thread 			analisadorAlarmes;
	private 		ThreadGroup		threadGroup;
	private			Logger 			logger;

	private StartupAlarmes()
	{
		// Define a thread para finalizacao do programa 
		Runtime.getRuntime().addShutdownHook(new ShutdownAlarmes());

		//Inicia a instancia do Logger do LOG4J
		Configuracao conf = Configuracao.getInstance();
		PropertyConfigurator.configure(conf.getPropriedadesLog());
		logger = Logger.getLogger("Alarmes");
		
		// Inicializa o threadGroup que sera o controlador das threads ativas.
		threadGroup = new ThreadGroup("AnalisadorAlarmes");
	}
	
	public static StartupAlarmes getInstance()
	{
		if (instance == null)
			instance = new StartupAlarmes();
		
		return instance;
	}
	
	/**
	 * Metodo....:inicializaAgendamentos
	 * Descricao.:Este metodo inicializa a classe de gerenciamento dos agendamentos
	 *            de execucao do comando SQL de busca dos valores de contagem. O agendamento
	 *            e inicializado para processamento de todos os alarmes
	 * @throws Exception
	 */
	public void inicializaAgendamentos() throws Exception
	{
		// Inicializa o Agendamento dos alarmes que irao executar o comando SQL
		// para buscar o valor de contador a ser inserido como evento
		AnalisadorAgendamento agendamento = AnalisadorAgendamento.getInstance();
		agendamento.inicializaAlarmes(AlarmeDAO.getInstance().findAll());
	}

	/**
	 * Metodo....:inicializaAnalise
	 * Descricao.:Este metodo inicializa a thread de analise que sera um (Daemon) que ficara em execucao
	 *            durante todo o tempo em um loop infinito realizando em cada iteracao a analise de todos
	 *            os alarmes cadastrados. Para finalizacao deve-se finalizar a thread de execucao que 
	 *            esta armazenada nessa classe singleton.
	 * @throws Exception
	 */
	public void inicializaAnalise() throws Exception
	{
		// Pega a instancia da classe DAO para realizar a conexao com o banco
		// de dados. Apos processamento, a classe de shutdown realiza o fechamento 
		// destas conexoes.
		AlarmeDAO.getInstance();
		EventoDAO.getInstance();
		
		// Cria a thread que sera o analisador de alarmes passando como parametro um threadgroup para
		// controle de threads ativos
		analisadorAlarmes = new Thread(threadGroup,new AnalisadorAlarmes());
		analisadorAlarmes.start();
	}

	/**
	 * Metodo....:isAtivo
	 * Descricao.:Indica se ha alguma trhead ativa no grupo indicando entao que ainda ha
	 *            processamento pelo analisador de alarmes
	 * @return boolean - Indica se ha processamento do analisador de alarmes
	 */
	public boolean isAtivo()
	{
		return (threadGroup.activeCount() > 0);
	}

	/**
	 * Metodo....:finalizaAnalise
	 * Descricao.:Finaliza a thread de execucao da analise de alarmes
	 *
	 */
	public void finalizaAnalise()
	{
		try
		{
			// Termina a thread de analisa de alarmes
			analisadorAlarmes.interrupt();
			logger.info("Thread de Analise de Alarmes foi finalizada.");
		}
		catch(Exception e)
		{
			logger.error("Erro ao interromper processamento de analise de alarmes. Erro:"+e);
		}
	}

	/**
	 * Metodo....:finalizaAgendamentos
	 * Descricao.:Este metodo e responsavel pela finalizacao das threads de agendamento
	 *
	 */
	public void finalizaAgendamentos()
	{
		// Realiza a finalizacao das threads de execucao de agendamento
		AnalisadorAgendamento.getInstance().finalizaAlarmes();
	}

	public static void main(String args[])
	{
		Logger logger = Logger.getLogger("Alarmes");
		try
		{
			// Utiliza o tempo de espera para identificar se o programa ainda esta ativo
			Configuracao conf = Configuracao.getInstance();
			int tempoEspera	= Integer.parseInt(conf.getPropriedade("alarmes.app.tempoEspera"));

			// Inicializa o processamento de agendamentos e logo em seguida inicia
			// a thread de processamento da analise de alarmes, sendo que esta thread
			// sera iniciada em um threadGroup especifico para controle e este programa
			// ficara ativo enquanto houve alguma thread neste grupo
			StartupAlarmes startup = StartupAlarmes.getInstance();
			logger.info("Iniciando gerenciamento de agendamento dos alarmes");
			startup.inicializaAgendamentos();
			logger.info("Iniciando analisador de status dos alarmes");
			startup.inicializaAnalise();
			while (startup.isAtivo())
				Thread.sleep(tempoEspera*1000);
			
			logger.info("Termino da execucao da analise de alarmes e agendamento de SQL contador");
			System.exit(0);
		}
		catch(Exception e)
		{
			logger.error("Erro no processamento de analise de alarmes. Erro:"+e);
			// Em caso de excecao entao finaliza com o procedimento de shutdown
			// Executa o metodo run sem iniciar nova thread
			ShutdownAlarmes shutdown = new ShutdownAlarmes();
			shutdown.run();
			System.exit(1);
		}
	}
}
