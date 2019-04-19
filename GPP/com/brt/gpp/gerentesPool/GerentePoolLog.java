//Definicao do Pacote
package com.brt.gpp.gerentesPool;

// Arquivos de Import do GPP
import java.io.PrintWriter;
import java.io.StringWriter;

import com.brt.gpp.comum.arquivoConfiguracaoGPP.*;
import com.brt.gpp.comum.Definicoes;

// Aruivos de Import do Java/CORBA
//import java.io.*;

import org.apache.log4j.*;

/**
  * Este arquivo define o gerente de Pool de Log para o GPP 
  * Implementa a clase Singleton para o gerenciamento de LOG.
  * Esta classe e um Wrapper para o registro de LOG utilizando
  * as classes da ferramenta LOG4J. 
  *
  * Obs: O nivel de severidade definido no Log4J sera sempre o nivel
  * DEBUG, sendo que o GPP define se o Debug esta ativo ou nao podendo
  * ser alterado em tempo de execucao
  *
  * <P> Versao:        	1.0
  *
  * @Autor:            	Daniel Cintra Abib
  * Data:               27/02/2002
  *
  * Modificado por: Joao Carlos
  * Data: 15/06/2004
  * Razao: Modificacao utilizando o Log4J
  *
  */

public final class GerentePoolLog
{
    // Variaveis Membros
    //private static GerentePoolLog			instancia			= null; // Instancia do Gerente Log
	private static long 					idProcesso			= 0; 	// Identificador do processo que esta utilizando o Log
	private Logger							logger				= null; // Referencia para o Logger do LOG4J
	private String							hostName			= null; // Identificador do servidor GPP e porta

	/**
	 * Metodo...: GerentePoolLog
	 * Descricao: Construtor 
	 * @param	
	 * @return	
	 */
    protected GerentePoolLog(Class ownerClass)
    {
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		//Define o nome e a porta do servidor q serao utilizados no LOG
		hostName = arqConf.getEnderecoOrbGPP() + ":" + arqConf.getPortaOrbGPP();

		/* Configura o LOG4J com as propriedades definidas no arquivo
		 * de configuracao do GPP
		 */
		
		PropertyConfigurator.configure(arqConf.getConfiguracaoesLog4j());
		//Inicia a instancia do Logger do LOG4J
		logger = Logger.getLogger(ownerClass);
		log(0,Definicoes.DEBUG,"GerentePoolLog","Construtor","Iniciando escrita de Log do sistema GPP...");
    }

	/**
	 * Metodo....: getHostName
	 * Descricao.: Retorna o nome do servidor GPP e a porta ORB utilizada
	 * @param
	 * @return String  - Nome e porta do servidor GPP
	 * @throws
	 */
	private String getHostName()
	{
		return hostName;
	}

	/**
	 * Metodo....: getInstancia
	 * Descricao.: Metodo para criar e retornar uma instancia da classe
	 * @param ownerClass Classe que chamou o getInstancia
	 * @return GerentePoolLog  - Referencia unica do Servant
	 * @throws
	 */
    public static GerentePoolLog getInstancia(Class ownerClass)
    {
    	// Verifica se ja existe uma instancia do Pool em execucao se negativo entao inicializa
        /*if ( instancia == null )
        {
            System.out.println ("Gerente de LOG (Singleton) INICIADO ");
            instancia = 
        }*/
    	GerentePoolLog instancia = new GerentePoolLog(ownerClass);
             
        return instancia;
    }
	
    /**
     * Metodo....: destroiInstancia
     * Descricao.: Libera a instancia de LOG da memoria 
	 * 			   Atencao: Se uma instancia for liberada (existe apenas uma), nao sera possivel chamar
	 * 						qualquer metodo... Qualquer chamada causara um NULL POINT EXCEPTION e por isso este metodo
	 * 						so podera ser chamado pelo gerente do GPP
	 * @param
	 * @return 
	 * @throws
     */
    public void destroiInstancia()
    {
        // Libera a variavel instancia para ser limpa pelo garbage collection
        //instancia = null;
    }
    
	/**
	 * Metodo....: getIdProcesso
	 * Descricao.: Metodo para criar e retornar o id do processo que escreverá no log
	 * @param  compNegocio 	- Componente de negocio que enviou a mensagem
	 * @return long			- Referencia unica de identificacao do processo
	 * @throws
	 */
	public long getIdProcesso(String compNegocio)
	{
		//seta o valor do idProcesso
		idProcesso = idProcesso +1;
		//registra o log do inicio do componente	
		logComponente(idProcesso,Definicoes.DEBUG,compNegocio,"INICIO","SUCESSO");
		return idProcesso;
	}    

	/**
	 * Metodo....: liberaIdProcesso
	 * Descricao.: Metodo para liberar o id do processo que terminou a escrita no log
	 * @param  idProcesso 	- Identificacao do processo que escreve no log
	 * @param  compNegocio 	- Componente de negocio que enviou a mensagem
	 * @param  status		- Status de processamento (SUCESSO ou ERRO)
	 * @return 
	 * @throws
	 */
	public void liberaIdProcesso(long idProcesso, String compNegocio, String status)
	{
		logComponente(idProcesso,Definicoes.DEBUG,compNegocio,"FIM",status);
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
	 * 
	 */

    public void log(long idProcesso, int aTipo, String aClasse, String aMetodo, String aMensagem)
	{
		StringBuffer mensagemFinal = new StringBuffer("<Servidor> ").append(getHostName()); 
		mensagemFinal.append(" <ID> ").append(idProcesso);
		mensagemFinal.append(" <Metodo> ").append(aMetodo);
		mensagemFinal.append(" <Mensagem> ").append(aMensagem);
		
		registraLog(aTipo,mensagemFinal.toString());
		mensagemFinal = null;
	}

	/**
	 * Metodo....: logComponente
	 * Descricao.: Grava uma linha no arquivo de LOG para log de componentes de negocio
	 * @param idProcesso	- Identificacao do processo que escreve no log
	 * @param aTipo			- Tipo de severidade do Log
	 * @param aComponente	- Nome do componente de negocio que chamou o log
	 * @param aMensagem		- A mensagem a ser escrita no log
	 * @param aStatus		- Status a ser registrado desse componente
	 * @return 
	 * @throws
	 * 
	 */
	public void logComponente(long idProcesso, int aTipo, String aComponente, String aMensagem, String aStatus)
	{
		StringBuffer mensagemFinal = new StringBuffer("<Servidor> ").append( getHostName()); 
        mensagemFinal.append(" <ID> " ).append(idProcesso);
		mensagemFinal.append(" <Mensagem> " ).append(aMensagem);
		mensagemFinal.append(" <ST> " ).append(aStatus);
		
		registraLog(aTipo,mensagemFinal.toString());
		mensagemFinal = null;
	}

	/**
	 * Metodo....: logComponente
	 * Descricao.: Grava uma linha de informacoes de componentes de negocio.
	 * OBS: Este metodo foi mantido para compatibilidade com versoes anteriores.
	 * deprecated - Favor utilizar o metodo logComponente(idProcesso,tipo,nomeComponente,mensagem,status)
	 * @param aTipo
	 * @param aComponente
	 * @param aMensagem
	 * @return 
	 * @throws
	 */
	public void logComponente (int aTipo, String aComponente, String aMensagem )
	{
		logComponente(0,aTipo,aComponente,aMensagem," ");
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
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
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
				if (arqConf.getSaidaDebug())
					logger.debug(aMensagem);
		}
		else logger.warn("SEVERIDADE NAO DEFINIDA - " + aMensagem);
	}
	
	/**
	 * Metodo...: traceError
	 * Descricao: Retorna uma String com o trace do erro
	 * ESSE METODO DEVE SER LEVADO PARA OUTRA CLASSE MAIS GENERICA
	 * 
	 * Luciano Vilela
	 * 29/02/2008
	 * 
	 * 
	 * @param e
	 * @return
	 */
	public String traceError(Exception e)
	{
		try{
			StringWriter in = new StringWriter();
			e.printStackTrace(new PrintWriter(in));
			return in.getBuffer().toString();
		}
		catch(Exception ex){}
		return null;
	}
}