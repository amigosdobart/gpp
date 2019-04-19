package com.brt.gppAlarmes.aplicacao;

import org.apache.log4j.Logger;

import com.brt.gppAlarmes.conexoes.GerentePoolConexoes;
import com.brt.gppAlarmes.dao.AlarmeDAO;
import com.brt.gppAlarmes.dao.EventoDAO;

/**
 * Essa classe e a responsavel para finalizar qualquer recurso
 * que porventura for inicializado pelo programa de alarmes como
 * por exemplo conexoes com o banco de dados
 * 
 * @author Joao Carlos
 * Data..: 01-Abr-2005
 *
 */
public class ShutdownAlarmes extends Thread
{
	public void run()
	{
		Logger logger = Logger.getLogger("Alarmes");
		logger.info("Executando procedimentos para finalizacao do programa");

		try
		{
			System.out.println("Adios compadre");
			// Fecha conexao com banco de dados
			AlarmeDAO.getInstance().close();
			EventoDAO.getInstance().close();
			GerentePoolConexoes.getInstance().destroyPool();
			// Termina agendamentos e a analise de alarmes
			StartupAlarmes startup = StartupAlarmes.getInstance();
			startup.finalizaAgendamentos();
			startup.finalizaAnalise();
		}
		catch(Exception e)
		{
			logger.error("Nao conseguiu executar finalizacao de conexao com o banco de dados. Erro "+e);
			System.exit(1);
		}
		logger.info("Fim do analisador de status dos alarmes");
		System.exit(0);
	}
}
