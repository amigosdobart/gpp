package com.brt.gppSocketGateway.logServer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Enumeration;

import org.apache.log4j.*;
import com.brt.gppSocketGateway.comum.Definicoes;
import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;

/**
  * Este arquivo refere-se a classe LogGPPServer, responsavel por realizar
  * os logs do Servidor de Sockets usando o log4j
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				25/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class LogGPPServer 
{
	static LogGPPServer instancia;
	Logger logger = null;
	ArqConfigGPPServer arqConfig;
	String hostName;
	int idProcesso = 0;
	
	/**
	 * Metodo...: LogGPPServer
	 * Descricao: Construtor
	 */
	public LogGPPServer()
	{
		arqConfig = ArqConfigGPPServer.getInstance();
		
		//Define o nome e a porta do servidor q serao utilizados no LOG
		hostName = arqConfig.getHostGPP() + ":" + arqConfig.getPortaGPP();

		try
		{
			// Liga o log4j
			this.ligaLog4j();
		}
		catch(Exception e)
		{
			System.out.println("Não foi possível instanciar logger");
		}
	}
	
	/***
	 * Metodo...: getInstancia
	 * Descricao: Pega uma instancia do singleton de logs
	 * @return	LogGPPServer	Instancia da classe gerenciadors de logs
	 */
	public static LogGPPServer getInstancia()
	{
		if(instancia == null)
		{
			instancia = new LogGPPServer(); 
		}

		return( instancia );
	}
	
	/*
	 * Método...: ligaLog4j
	 * Descricao: ativa o Log4j
	 */
	private void ligaLog4j() throws Exception
	{
		Properties prop =  new Properties();
		prop.load(new FileInputStream("ConfigGPPServer.cfg") );
		
		//Acertando propriedades do Log4j
		Properties propriedadesLog4j = new Properties();
		for (Enumeration eNum = prop.propertyNames() ; eNum.hasMoreElements() ;)
		{
			String nomePropriedade = (String)eNum.nextElement(); 
			if ( nomePropriedade.startsWith("log4j") )
				propriedadesLog4j.put(nomePropriedade,prop.getProperty(nomePropriedade));
		}
		
		// Configura o log4j
		PropertyConfigurator.configure(propriedadesLog4j);
		
		//Inicia a instancia do Logger do LOG4J
		logger = Logger.getLogger(Definicoes.NOME_SISTEMA);
	}
	
	/** 
	 * Metodo....: log
	 * Descricao.: Grava uma linha no arquivo de LOG para log de classes do sistema
	 * @param aidProcesso	- Identificacao do processo que escreve no log 
	 * @param aTipo			- Tipo da severidade do log
	 * @param aClasse		- Classe java que chamou o log
	 * @param aMetodo		- Metodo da classe java que chamou o log
	 * @param aMensagem 	- A mensagem a ser escrita no log
	 * @return 
	 * @throws
	 */
	public void log(long idProcesso, int aTipo, String aClasse, String aMetodo, String aMensagem)
	{
		String mensagemFinal = "<Servidor> " + this.hostName; 
		mensagemFinal = mensagemFinal + " <ID> " + idProcesso;
		mensagemFinal = mensagemFinal + " <CL> " + aClasse;
		mensagemFinal = mensagemFinal + " <ME> " + aMetodo;
		mensagemFinal = mensagemFinal + " <Mensagem> " + aMensagem;
		
		registraLog(aTipo,mensagemFinal);
	}
	
	/**
	 * Metodo....: registraLog
	 * Descricao.: Este metodo efetiva o registro do Log nos Appenders do Log4j
	 * @param aTipo			- Severidade utilizada
	 * @param aMensagem		- Mensagem a ser escrita
	 * @return 
	 * @throws
	 */
	private void registraLog(int aTipo, String aMensagem)
	{
		if (aTipo == Definicoes.FATAL)
			logger.fatal(aMensagem);
		else if (aTipo == Definicoes.ERRO)
				logger.error(aMensagem);
		else if (aTipo == Definicoes.WARN)
				logger.warn(aMensagem);
		else if (aTipo == Definicoes.INFO)
				logger.info(aMensagem);
		else if (aTipo == Definicoes.DEBUG)
		{
				if (arqConfig.getSaidaDebug())
					logger.debug(aMensagem);
		}
		else logger.warn("SEVERIDADE NAO DEFINIDA - " + aMensagem);
	}
	
	/**
	 * Metodo....: getIdProcesso
	 * Descricao.: Metodo para criar e retornar o id do processo que escreverá no log
	 * @param  compNegocio 	- Componente de negocio que enviou a mensagem
	 * @return long			- Referencia unica de identificacao do processo
	 * @throws
	 */
	public long getIdProcesso()
	{
		//seta o valor do idProcesso
		idProcesso = idProcesso +1;
		//registra o log do inicio do componente	
		//registraLog(Definicoes.INFO,compNegocio,"INICIO","SUCESSO");
		return idProcesso;
	}    

}
