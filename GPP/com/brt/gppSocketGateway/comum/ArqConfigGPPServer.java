package com.brt.gppSocketGateway.comum;

// Classes Importadas
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Enumeration;

public class ArqConfigGPPServer 
{
	 // Variaveis membro
	 private static  ArqConfigGPPServer  instancia;		// Objeto do pattern Singleton
	 //private static  FileInputStream   arquivoConfiguracao;	// Arquivo de configuracao

	 private String  portaServidorGPP;          				// Caminho do arquivo de Log
	 private String	 portaServidorAprovisionamento;								// Flag para identificar se as mensagens de DEBUG devem ser escritas no LOG
	 private int	 timeOut;
	 private int	 tempoAcknowledge;
	 private String	 portaGPP;
	 private String	 hostGPP;
	 private String	 hostAprovisionamento;
	 private String	 portaAprovisionamento;
	 private boolean saidaDebug;
	 private int	 numTentativasEnvioXML;

	 private String  caminhoSaidaLog;
	 private Properties propriedadesLog4j;					// Propriedades da ferramenta Log4j anexao ao GPP
	 private Properties propriedadesCorba;					// Propriedades de conexao CORBA 
	
	 public ArqConfigGPPServer()
	 {
	 	// Construtor padrão
	 }

	 /**
	  * Metodo...: setCaminhoSaidaLog
	  * Descricao: Associa o caminho do arquivo de LOg
	  * @param 	aCaminhoSaidaLog	- Caminho do arquivo de LOG
	  * @return 	
	  * @throws
	  */
	 public void setCaminhoSaidaLog ( String aCaminhoSaidaLog )
	 {
		 this.caminhoSaidaLog = aCaminhoSaidaLog;
	 }

	/**
	 * Metodo...: getInstance
	 * Descricao: Metodo que retorna uma unica instancia da classe (pattern Singleton)
	 * @param 	nomeArquivo				- Nome do arquivo de configuracao
	 * @return	ArquivoConfiguracaoGPP 	- Referencia (instancia) unica para o gerente de objetos
	 * @throws
	 */
	public static ArqConfigGPPServer getInstance(String nomeArquivo)
	{
		// Verifica se existe alguma outra instancia sendo executada
		if ( instancia == null )
		{
			instancia = new ArqConfigGPPServer( );
			try
			{
				instancia.getPropriedades(nomeArquivo);
			}
			catch ( Exception anotherException )
			{
			   System.out.println ( "ERRO GPP: Arquivo de configuração nao pode ser lido:" + anotherException );
			   System.exit( 1 );
			}
		}
		return instancia;
	}

	 /**
	  * Metodo...: getInstance
	  * Descricao: Metodo que retorna uma unica instancia da classe (pattern Singleton)
	  * @param 	nomeArquivo				- Nome do arquivo de configuracao
	  * @return	ArquivoConfiguracaoGPP 	- Referencia (instancia) unica para o gerente de objetos
	  * @throws
	  */
	 public static ArqConfigGPPServer getInstance()
	 {
		return ArqConfigGPPServer.getInstance("ConfigGPPServer.cfg");
	 }

	 /**
	  * Metodo...: getPropriedades
	  * Descricao: Metodo que busca no arquivo de configuracao os valores das variaveis
	  * @param	pi_file	- Arquivo de configuracao do GPP
	  * @return	
	  * @throws
	  */
	 private void getPropriedades (String pi_file) throws Exception
	 {
		 System.out.println("ConfiguracaoGPPServer->getPropriedades() - Inicio");

		 Properties prop =  new Properties();
		 prop.load(new FileInputStream(pi_file) );

		 this.setCaminhoSaidaLog( prop.getProperty("GPP_CAMINHO_LOG") );
		 this.setPortaServidorAprovisionamento(prop.getProperty("PORTA_SERVIDOR_APROVISIONAMENTO"));
		 this.setPortaServidorGPP(prop.getProperty("PORTA_SERVIDOR_GPP"));
		 this.setTimeOut(new Integer(prop.getProperty("TIME_OUT_SERVIDOR")).intValue());
		 this.setHostGPP(prop.getProperty("HOST_GPP"));
		 this.setPortaGPP(prop.getProperty("PORTA_GPP"));	
		 this.setHostAprovisionamento(prop.getProperty("HOST_APROVISIONAMENTO"));	
		 this.setPortaAprovisionamento(prop.getProperty("PORTA_APROVISIONAMENTO")); 
		 this.setSaidaDebug(Boolean.valueOf (prop.getProperty("GPP_SAIDA_DEBUG")).booleanValue());
		 this.setTempoAcknowledge(Integer.parseInt(prop.getProperty("TEMPO_ESPERA_ACKNOWLEDGE")));
		 this.setNumTentativasEnvioXML(Integer.parseInt(prop.getProperty("NUM_TENTATIVAS_ENVIO_XML")));

		 //Acertando propriedades do Log4j
		 propriedadesLog4j = new Properties();
		 for (Enumeration eNum = prop.propertyNames() ; eNum.hasMoreElements() ;)
		 {
			 String nomePropriedade = (String)eNum.nextElement(); 
			 if ( nomePropriedade.startsWith("log4j") )
				 propriedadesLog4j.put(nomePropriedade,prop.getProperty(nomePropriedade));
		 }
		 //Acertando propriedades de conexao CORBA
		 propriedadesCorba = new Properties();
		 for (Enumeration eNum = prop.propertyNames() ; eNum.hasMoreElements() ;)
		 {
			 String nomePropriedade = (String)eNum.nextElement(); 
			 if ( nomePropriedade.indexOf("CORBA") > -1 )
				 propriedadesCorba.put(nomePropriedade,prop.getProperty(nomePropriedade));
		 }

		 System.out.println("ArquivoConfiguracaoGPP->getPropriedades() - Fim");
	 }

	/**
	 * @return
	 */
	public String getPortaServidorAprovisionamento() {
		return portaServidorAprovisionamento;
	}

	/**
	 * @return
	 */
	public String getPortaServidorGPP() {
		return portaServidorGPP;
	}

	/**
	 * Metodo...: getCaminhoSaidaLog
	 * Descricao: Retorna o caminho do arquivo de LOG 
	 * @param
	 * @return String	- Caminho do arquivo de LOG
	 * @throws
	 */
	public String getCaminhoSaidaLog ( )
	{
		return this.caminhoSaidaLog;
	}
    
	/**
	 * Metodo...: getConfiguracoesLog4j
	 * Descricao: Metodo que retorna as configuracoes do Log4j do GPP
	 * @return Properties - Properties contendo as configuracoes do Log4j
	 */
	public Properties getConfiguracaoesLog4j()
	{
		if (propriedadesLog4j != null)
			return propriedadesLog4j;
		else return new Properties();
	}

	/**
	 * Metodo...: getConfiguracoesCorba
	 * Descricao: Metodo que retorna as configuracoes do CORBA para acesso ao GPP
	 * @return Properties - Properties contendo as configuracoes do Corba
	 */
	public Properties getConfiguracaoesCorba()
	{
		if (propriedadesCorba != null)
			return propriedadesCorba;
		else return new Properties();
	}

	/**
	 * @return
	 */
	public Properties getPropriedadesLog4j() {
		return propriedadesLog4j;
	}

	/**
	 * @param string
	 */
	public void setPortaServidorAprovisionamento(String string) {
		portaServidorAprovisionamento = string;
	}

	/**
	 * @param string
	 */
	public void setPortaServidorGPP(String string) {
		portaServidorGPP = string;
	}

	/**
	 * @param properties
	 */
	public void setPropriedadesLog4j(Properties properties) {
		propriedadesLog4j = properties;
	}

	/**
	 * @param properties
	 */
	public void setPropriedadesCorba(Properties properties) {
		propriedadesCorba = properties;
	}

	/**
	 * @return
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * @param i
	 */
	public void setTimeOut(int i) {
		timeOut = i;
	}

	/**
	 * @return
	 */
	public String getHostGPP() {
		return hostGPP;
	}

	/**
	 * @return
	 */
	public String getPortaGPP() {
		return portaGPP;
	}

	/**
	 * @param string
	 */
	public void setHostGPP(String string) {
		hostGPP = string;
	}

	/**
	 * @param string
	 */
	public void setPortaGPP(String string) {
		portaGPP = string;
	}

	/**
	 * @return
	 */
	public String getHostAprovisionamento() {
		return hostAprovisionamento;
	}

	/**
	 * @param string
	 */
	public void setHostAprovisionamento(String string) {
		hostAprovisionamento = string;
	}

	/**
	 * @return
	 */
	public String getPortaAprovisionamento() {
		return portaAprovisionamento;
	}

	/**
	 * @param string
	 */
	public void setPortaAprovisionamento(String string) {
		portaAprovisionamento = string;
	}
	/**
	 * @return
	 */
	public boolean getSaidaDebug() {
		return saidaDebug;
	}

	/**
	 * @param string
	 */
	public void setSaidaDebug(boolean string) {
		saidaDebug = string;
	}

	/**
	 * @return
	 */
	public int getNumTentativasEnvioXML() {
		return numTentativasEnvioXML;
	}

	/**
	 * @return
	 */
	public int getTempoAcknowledge() {
		return tempoAcknowledge;
	}

	/**
	 * @param i
	 */
	public void setNumTentativasEnvioXML(int i) {
		numTentativasEnvioXML = i;
	}

	/**
	 * @param i
	 */
	public void setTempoAcknowledge(int i) {
		tempoAcknowledge = i;
	}

}
