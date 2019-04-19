package br.com.brasiltelecom.sasc.action;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.comum.produtorConsumidor.ProdutorConsumidorDelegate;


public class GerenciadorSasc implements Runnable
{
	private boolean deveExecutar;
	private static GerenciadorSasc instance;
	private String servidorWSM;
	private String portaServidorWSM;
	private String usuarioWSM;
	private String senhaWSM;
	private DataSource dataSource;
	private int numConsumidores;
	private int qtdeMaxTentativas;
	private Logger  logger 		= Logger.getLogger(this.getClass());
	
	private GerenciadorSasc()
	{
	}
	
	public static GerenciadorSasc getInstance()
	{
		if (instance == null)
			instance = new GerenciadorSasc();
		
		return instance;
	}
	
	public void start(String servidor, String porta, String usuario, String senha, DataSource ds, int numConsumidores, int qtdeMaxTentativas)
	{
		this.deveExecutar 	   = true;
		this.servidorWSM 	   = servidor;
		this.portaServidorWSM  = porta;
		this.usuarioWSM 	   = usuario;
		this.senhaWSM 		   = senha;
		this.numConsumidores   = numConsumidores;
		this.qtdeMaxTentativas = qtdeMaxTentativas;
		this.dataSource		   = ds;
		
		Thread t = new Thread(instance);
		t.start();
	}
	
	public boolean estaExecutando()
	{
		return this.deveExecutar;
	}
	
	public void stop()
	{
		this.deveExecutar = false;
	}
	
	public void run()
	{
		while (deveExecutar)
		{
			try
			{
				ProdutorConsumidorDelegate delegate = new ProdutorConsumidorDelegate();
				Produtor produtor = new AtualizadorSimcardProdutor(dataSource, servidorWSM, portaServidorWSM, usuarioWSM, senhaWSM, qtdeMaxTentativas);
				delegate.exec(numConsumidores, produtor, null, AtualizadorSimcardConsumidor.class);
				
				// Pausa a execucao da thread durante 5 segundos
				// System.out.println("Inseto dormindo...\n");
				logger.debug("GerenciadorSasc domindo...");
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				logger.error("Erro: ", e);
				e.printStackTrace();
			}
			catch (Exception ex)
			{
				logger.error("Erro: ", ex);
				ex.printStackTrace();
			}
		}
	}

}
