package com.brt.gpp.comum.arquivoConfiguracaoGPP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe responsavel por armazenar o mapeamento do Arquivo de Configuracoes do GPP.
 *
 * <hr>
 * <b>Modificado por:</b>	Leone Parise<br>
 * <b>Data:</b> 26/09/2007<br>
 * <b>Razao:</b><br> Reorganizacao do arquivo.<br> Adicao de novos metodos.<br> Novo parse.<br>
 * <hr>

 * @author 			Daniel Cintra Abib
 * @since			1.0, 18/02/2004
 *
 */

final public class ArquivoConfiguracaoGPP
{
	private static ArquivoConfiguracaoGPP instancia;

	private File arquivo;

	private Map mapa;

	private ArquivoConfiguracaoGPP(String nome) throws IOException, ParseException
	{
		arquivo = new File(nome);
		parseArquivo();
	}

	/**
	 * Inicia o servico de Configuracao do GPP.
	 *
	 * @param nomeArquivo
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static ArquivoConfiguracaoGPP getInstance (String nomeArquivo) throws IOException, ParseException
	{
		// Verifica se existe alguma outra instancia sendo executada
		if ( instancia == null )
		{
			instancia = new ArquivoConfiguracaoGPP(nomeArquivo);
			instancia.getPropriedades();
		}
		return instancia;
	}

	/**
	 * Retorna uma unica instancia da classe ArquivoConfiguracaoGPP
	 *
	 * @return	Intancia ArquivoConfiguracaoGPP
	 * @throws  RuntimeException	Caso o a instancia seja nula
	 */
	public static ArquivoConfiguracaoGPP getInstance() throws RuntimeException
	{
		// Caso noa existe nenhuma instancia lancar excecao explicando o que fazer.
		if ( instancia == null )
			throw new RuntimeException("Instancia de ArquivoConfiguracaoGPP nao encontrada. " +
					"Favor executar o metodo getInstance(String nomeArquivo) para iniciar o servico.");

		return instancia;
	}
	/**
	 * Executa o parse do arquivo de Configuracoes.
	 *
	 * @throws IOException
	 * @throws ParseException
	 */
	private void parseArquivo() throws IOException, ParseException
	{
		InputStreamReader ir = new InputStreamReader(new FileInputStream(arquivo));
		BufferedReader reader = new BufferedReader(ir);

		String linha = reader.readLine();
		int n = 1;
		// Classes de tratamento de Regular Expressions
		Pattern p;
		Matcher m;
		// Mapa de Parametros e Valores
		mapa = new LinkedHashMap();

		while (linha != null)
		{
			linha = linha.trim();
			// Reconhecendo Comentarios
			p = Pattern.compile("\\s*#.*");
			m = p.matcher(linha);
			// Retirando Comentarios
			linha = m.replaceAll("");
			// Reconhecendo Parametros
			p = Pattern.compile("^\\S+");
			m = p.matcher(linha);
			// Fazendo parse de Parametros
			if (m.find())
			{
				String param = linha.substring(m.start(), m.end()).trim();
				String valor = linha.substring(m.end(), linha.length()).trim();
				// Caso o valor seja nulo lancar uma excecao
				if (valor.length() == 0)
					throw new ParseException("O valor nao deve ser vazio. Parametro: "+param+", Linha: "+n, n);

				mapa.put(param, valor);
			}
			linha = reader.readLine();
			n++;
		}
		reader.close();
	}

	/**
	 * Recupera o Valor da Propriedade num <code>Object</code>
	 *
	 * @param propriedade	Propriedade contido no arquivo de configuracoes
	 * @return				Valor <code>Object</code>
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public Object getPropriedade(String propriedade) throws NullPointerException
	{
		if(!mapa.containsKey(propriedade))
			throw new NullPointerException("Parametro nao encontrado : " + propriedade);

		Object obj = mapa.get(propriedade);
		return obj;
	}

	/**
	 * Altera o valor de uma Propriedade dada
	 *
	 * @param propriedade				Propriedade contido no arquivo de configuracoes
	 * @param valor						Valor a ser alterado
	 * @throws NullPointerException		Caso o propriedade nao exista no arquivo
	 */
	public synchronized void setPropriedade(String propriedade, Object valor) throws NullPointerException
	{
		if(!mapa.containsKey(propriedade))
			throw new NullPointerException("Parametro nao encontrado : " + propriedade);
		mapa.remove(propriedade);
		mapa.put(propriedade, valor);
	}

	/**
	 * Recupera o Valor numa <code>String</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>String</code>
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public String getString(String propriedade) throws NullPointerException
	{
		return (String)getPropriedade(propriedade);
	}

	/**
	 * Recupera o Valor num tipo primitivo <code>boolean</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>boolean</code>
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public boolean getBoolean(String propriedade) throws NullPointerException
	{
		return Boolean.valueOf(getString(propriedade)).booleanValue();
	}

	/**
	 * Recupera o primeiro caractere do Valor.
	 *
	 * @param propriedade	Propriedade contido no arquivo de configuracoes
	 * @return				Valor <code>char</code>
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public char getChar(String propriedade) throws NullPointerException
	{
		return getString(propriedade).charAt(0);
	}

	/**
	 * Recupera o Valor num tipo primitivo <code>short</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>short</code>
	 * @throws NumberFormatException Caso o numero retornado seja invalido
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public short getShort(String propriedade) throws NumberFormatException, NullPointerException
	{
		return Short.parseShort(getString(propriedade));
	}

	/**
	 * Recupera o Valor num tipo primitivo <code>int</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>int</code>
	 * @throws NumberFormatException Caso o numero retornado seja invalido
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public int getInt(String propriedade) throws NumberFormatException, NullPointerException
	{
		return Integer.parseInt(getString(propriedade));
	}

	/**
	 * Recupera o Valor num tipo primitivo <code>long</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>long</code>
	 * @throws NumberFormatException Caso o numero retornado seja invalido
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public long getLong(String propriedade) throws NumberFormatException, NullPointerException
	{
		return Long.parseLong(getString(propriedade));
	}

	/**
	 * Recupera o Valor num tipo primitivo <code>float</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>float</code>
	 * @throws NumberFormatException Caso o numero retornado seja invalido
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public float getFloat(String propriedade) throws NumberFormatException, NullPointerException
	{
		return Float.parseFloat(getString(propriedade));
	}

	/**
	 * Recupera o Valor num tipo primitivo <code>double</code>
	 *
	 * @param propriedade		Propriedade contido no arquivo de configuracoes
	 * @return					Valor <code>double</code>
	 * @throws NumberFormatException Caso o numero retornado seja invalido
	 * @throws NullPointerException	Caso o propriedade nao exista no arquivo
	 */
	public double getDouble(String propriedade) throws NumberFormatException, NullPointerException
	{
		return Double.parseDouble(getString(propriedade));
	}

	public Map getMapa()
	{
		return this.mapa;
	}

	public File getArquivo()
	{
		return this.arquivo;
	}
	/*#########################################################
	       NÃO UTILIZAR MAIS ESTE MODELO DE ARQUIVO!!!
	          REMOVER OS ATRIBUTOS O QUANTO ANTES!!!
	  #########################################################*/

	private String  caminhoSaidaLog;          				// Caminho do arquivo de Log
	private boolean ativaDebug;								// Flag para identificar se as mensagens de DEBUG devem ser escritas no LOG
	private boolean saidaDebug;								// Flag para identificar se deve ser apresentado o trace em tela

	private String  portaOrbGPP;							// Porta ORB da GPP
	private String  enderecoOrbGPP;							// Nome ou IP da maquina do GPP
	private String  portaNameServiceGPP;					// Porta do NameService do GPP
	private String  enderecoNameServiceGPP;					// Nome ou IP da maquina do NameService para o GPP

	private String  nomeOrbPOARoot;							// Nome do POA Root
	private String  nomeAtivaComponentesPOA;				// Nome do AtivaComponetes POA
	private String  nomeAprovisionamentoPOA;				// Nome do Provisioning POA
	private String  nomeConsultaPOA;						// Nome do Consulta POA
	private String  nomeRecargaPOA;							// Nome do Recharging POA
	private String  nomeProcessosBatchPOA;					// Nome do BatchJobs POA
	private String  nomeGerenteGPPPOA;						// Nome do GPPManager POA

	private int		numeroTentativasConexaoTecnomen;		// Numero de tentativas para obter uma conexao tecnomen
	private int     tempoMaximoEspera;						// Tempo maximo para obter uma conexao
	private int     tempoEspera;							// Tempo de espera "sleep" para obter uma conexao
	private int 	tempoEsperaSMS;							// Tempo de espera para buscar dados de SMS no banco
	private int 	tempoMaxEsperaSMS;						// Tempo maximo de espera para buscar dados de SMS no banco
	private int     numeroThreadsEnvioSMS;                  // Numero de threads que serao utilizadas para envio de SMS
	private int		tempoEsperaAtivarVoucher;				// Tempo de espera para ativar um voucher
	private int		maximoNumeroInteracoes;					// Numero Máximo de interações para se ativar um voucher

	private int     numeroConexoesAprovisionamento; 		// Numero inicial de conexoes ao Aprovisionamento da Tecnomen
	private int     numeroConexoesRecarga; 					// Numero inicial de conexoes a Recarda da Tecnomen
	private int     numeroConexoesVoucher; 					// Numero inicial de conexoes a Voucher da Tecnomen
	private int     numeroConexoesAdmin; 					// Numero inicial de conexoes Admin da Tecnomen
	private int     numeroConexoesBanco; 					// Numero inicial de conexoes ao bando de dados

	private int		numeroTentativasConexao;				// Numero de tentativas para obter uma conexao de banco de dados
	private String  nomeUsuarioTecnomen;             		// User ID para conexao a plataforma tecnomen
	private String  senhaUsuarioTecnomen;             		// Senha do usuario para conexao a plataforma Tecnomen
	private String  nomeUsuarioTecnomenVoucher;            	// User ID para conexao a plataforma tecnomen (PP_Admin_VAdmin)
	private String  senhaUsuarioTecnomenVoucher;           	// Senha do usuario para conexao a plataforma Tecnomen (PP_Admin_VAdmin)

	private String	authServerReferencia;					// String de referencia para o Servidor de Autenticacao da Tecnomen.

	private String  nomeUsuarioBancoDados;             		// User ID para conexcao ao banco de dados
	private String  senhaUsuarioBancoDados;       			// Senha do usuario para conexao ao bando de dados
	private String  instanciaOracleBancoDados;     			// Instancia do banco de daos
	private String  urlOracleBancoDados;                    // URL de conexão para o banco de dados

	private String  nomeUsuarioSMSC;             			// User ID para conexao com o middleware SMSC
	private String  senhaUsuarioSMSC;       				// Senha do usuario para conexao com o middleware SMSC
	private String  enderecoMaquinaSMSC;     				// IP da maquina do middleware SMSC
	private String  portaMaquinaSMSC;         				// Porta do middleware SMSC
	private String	servicoAplicacaoSMSC;					// Número do Serviço de Aplicação do SMSC
	private String	itemAplicacaoSMSC;						// Número do Item de Aplicação do SMSC
	private int		originadorSMSC;							// Número do originador do SMSC
	private int		codigoTarifacao;						// Código de Tarifação do Serviço
	private String	mensagemURL;							// URL para envio da mensagem

	private String  nomeUsuarioMASC;             			// User ID para conexao com o MASC
	private String  senhaUsuarioMASC;       				// Senha do usuario para conexao com MASC
	private String  enderecoMaquinaMASC;     				// IP da maquina do MASC
	private String  portaMaquinaMASC;         				// Porta do MASC
	private int		numTentativasReadln;					// Numero de tentativas de leitura no socket
	private int		tempoTimeoutReadln;						// Tempo de espera entre um readln e o próximo no socket

	private String	dirImportacaoCdr;						// Diretorio inicial de importacao de CDRs
	private String	dirTrabalhoCdr;							// Diretorio de trabalho onde cdrs estao sendo processados
	private String	dirHistoricoCdr;						// Diretorio historico dos cdrs processados
	private String  dirRejeicaoCdr;							// Diretorio de rejeicao de CDRs

	private String	dirGenevaOrigem;						// Diretorio inicial de importacao de CDRs
	private String	dirGenevaDestino;						// Diretorio de trabalho onde cdrs estao sendo processados

	private boolean deveConsumirSMS;						// Flag indicando se o produto SMS deve ser iniciado
	private boolean deveProcessarFilaRecarga;				// Flag indicando se deve ser processada a fila de recarga
	private boolean deveGerenciarPedidosVoucher;			// Flag indicando se deve ser gerenciado os pedidos de voucher
	private boolean deveGerenciarSMS;						// Flag indicando se deve ser executar o teste de SMS
	private boolean deveImportarCDR;						// Flag indicando se a instancia deve importar CDR
	private boolean deveGerenciarHotLine;					// Flag indicando se a instancia deve gerenciar os desbloqueios de hot-line

	private String	nomeServidorSocket;						// Nome do servidor socket a ser utilizado
	private int		portaServidorSocket;					// Porta TCP/IP a ser utilizado para conexao

	private Properties propriedadesLog4j;					// Propriedades da ferramenta Log4j anexao ao GPP


	private String	servidorWIG; 							//Endereço servidor WIG
	private int		portaWIG;								//Porta Socket WIG
	private String	urlRetornoPesquisaAparelhoWIG;			//URL de retorno Pesquisa Aparelho
	private int 	timeOutWIG;								//Timeout pesquisa WIG
	private String	diretorioRetornoPesquisaAparelhoWIG; 	//Diretorio de retorno da pesquisa aparelho

	private String	formaEnvioSMS;							//Define a forma de envio de SMS - Middleware ou SMPP

	private String	dirRelChurn;							// Diretorio onde o relatório de Churn é gerado

	private String	dirRootFabricaRelatorio;				// Diretorio root da fábrica de relatórios

	private String	dirBateVolta;							// Diretorio para arquivos de totalizações de bate-volta

	private int		toolbarPorta;								// Porta do servidor Toolbar
	private short	toolbarNumTentativas;						// Numero de tentativas de conexao
	private int		toolbarTempoEspera;						// Tempo de espera entre as tentativas

	private void getPropriedades ()
	{
		System.out.println("ArquivoConfiguracaoGPP->getPropriedades() - Inicio");

		this.caminhoSaidaLog = this.getString("GPP_CAMINHO_LOG");
		this.ativaDebug = this.getBoolean("GPP_DEBUG_ATIVO");
		this.saidaDebug = this.getBoolean("GPP_SAIDA_DEBUG");

		this.portaOrbGPP = this.getString("GPP_PORTA_ORB" );
		this.enderecoOrbGPP = this.getString("GPP_ENDERECO_ORB" );
		this.portaNameServiceGPP = this.getString("GPP_PORTA_NAMESERVICE" );
		this.enderecoNameServiceGPP = this.getString("GPP_ENDERECO_NAMESERVICE" );

		this.nomeOrbPOARoot = this.getString("GPP_ORB_POA_ROOT" );
		this.nomeAtivaComponentesPOA = this.getString("GPP_NOME_ATIVA_COMPONENTES_POA" );
		this.nomeAprovisionamentoPOA = this.getString("GPP_APROVISIONAMENTO_POA" );
		this.nomeConsultaPOA = this.getString("GPP_CONSULTA_POA" );
		this.nomeRecargaPOA = this.getString("GPP_RECARGA_POA" );
		this.nomeProcessosBatchPOA = this.getString("GPP_PROCESSOSBATCH_POA" );
		this.nomeGerenteGPPPOA = this.getString("GPP_GERENTEGPP_POA" );


		this.tempoMaximoEspera = this.getInt("TECNOMEN_MAX_TEMPO_ESPERA");
		this.tempoEspera = this.getInt("TECNOMEN_TEMPO_ESPERA");
		this.tempoEsperaAtivarVoucher = this.getInt("TEMPO_ESPERA_ATIVACAO_VOUCHER");
		this.maximoNumeroInteracoes = this.getInt("MAXIMO_INTERACOES_VOUCHER");

		this.tempoEsperaSMS = this.getInt("TEMPO_ESPERA_SMS");
		this.tempoMaxEsperaSMS = this.getInt("TEMPO_MAX_ESPERA_SMS");
		this.numeroThreadsEnvioSMS = this.getInt("GPP_NUMERO_THREADS_ENVIO_SMS");
		this.deveConsumirSMS = this.getBoolean("GPP_DEVE_CONSUMIR_SMS");

		this.deveProcessarFilaRecarga = this.getBoolean("GPP_DEVE_PROCESSAR_FILA_RECARGA");
		this.deveGerenciarPedidosVoucher = this.getBoolean("GPP_DEVE_GERENCIAR_PEDIDOS_VOUCHER");
		this.deveGerenciarSMS = this.getBoolean("GPP_DEVE_GERENCIAR_SMS");
		this.deveImportarCDR = this.getBoolean("GPP_DEVE_IMPORTAR_CDR");
		this.deveGerenciarHotLine = this.getBoolean("GPP_DEVE_GERENCIAR_HOT_LINE");

		this.numeroConexoesAprovisionamento = this.getInt("TECNOMEN_CONEXOES_APROVISIONAMENTO");
		this.numeroConexoesRecarga = this.getInt("TECNOMEN_CONEXOES_RECARGA");
		this.numeroConexoesVoucher = this.getInt("TECNOMEN_CONEXOES_VOUCHER");
		this.numeroConexoesAdmin = this.getInt("TECNOMEN_CONEXOES_ADMIN");
		this.numeroTentativasConexao = this.getInt("GPP_NUM_TENTATIVAS_CONEXAO");

		this.numeroConexoesBanco = this.getInt("GPP_CONEXOES_BANCO");
		this.nomeUsuarioBancoDados = this.getString("GPP_NOME_USUARIO_BANCO" );
		this.senhaUsuarioBancoDados = this.getString("GPP_SENHA_USUARIO_BANCO" );
		this.instanciaOracleBancoDados = this.getString("GPP_INSTANCIA_ORACLE_BANCO" );
		this.urlOracleBancoDados = this.getString("GPP_URL_ORACLE_BANCO");

		this.nomeUsuarioSMSC = this.getString("SMSC_NOME_USUARIO" );
		this.senhaUsuarioSMSC = this.getString("SMSC_SENHA_USUARIO" );
		this.enderecoMaquinaSMSC = this.getString("SMSC_NOME_MAQUINA" );
		this.portaMaquinaSMSC = this.getString("SMSC_PORTA_MAQUINA" );
		this.mensagemURL = this.getString("SMSC_MENSAGEM_URL" );
		this.servicoAplicacaoSMSC = this.getString("SMSC_SERVICO_APLICACAO" );
		this.itemAplicacaoSMSC = this.getString("SMSC_ITEM_APLICACAO" );
		this.originadorSMSC = this.getInt("SMSC_ORIGINADOR");
		this.codigoTarifacao = this.getInt("SMSC_CODIGO_TARIFACAO");

		this.nomeUsuarioMASC = this.getString("MASC_NOME_USUARIO" );
		this.senhaUsuarioMASC = this.getString("MASC_SENHA_USUARIO" );
		this.enderecoMaquinaMASC = this.getString("MASC_NOME_MAQUINA" );
		this.portaMaquinaMASC = this.getString("MASC_PORTA_MAQUINA" );

		this.numeroTentativasConexaoTecnomen = this.getInt("TECNOMEN_NUM_TENTATIVAS_CONEXAO");
		this.nomeUsuarioTecnomen = this.getString("TECNOMEN_NOME_USUARIO" );
		this.senhaUsuarioTecnomen = this.getString("TECNOMEN_SENHA_USUARIO" );
		this.nomeUsuarioTecnomenVoucher = this.getString("TECNOMEN_NOME_USUARIO_VOUCHER" );
		this.senhaUsuarioTecnomenVoucher = this.getString("TECNOMEN_SENHA_USUARIO_VOUCHER" );
		this.authServerReferencia = this.getString("TECNOMEN_AUTH_SERVER_REFERENCIA");

		this.dirImportacaoCdr = this.getString("DIR_IMPORTACAO_CDR");
		this.dirTrabalhoCdr = this.getString("DIR_TRABALHO_CDR");
		this.dirHistoricoCdr = this.getString("DIR_HISTORICO_CDR");
		this.dirRejeicaoCdr = this.getString("DIR_REJEICAO_CDR");
		this.dirGenevaOrigem = this.getString("DIR_ORIGEM_GENEVA");
		this.dirGenevaDestino = this.getString("DIR_DESTINO_GENEVA");
		this.nomeServidorSocket = this.getString("GPP_SOCKET_GATEWAY_HOST");
		this.portaServidorSocket = this.getInt("GPP_SOCKET_GATEWAY_PORT");

		this.servidorWIG = this.getString("WIG_NOME_MAQUINA");
		this.portaWIG = this.getInt("WIG_PORTA_MAQUINA");
		this.urlRetornoPesquisaAparelhoWIG = this.getString("WIG_URL_RETORNO_PESQUISA");
		this.timeOutWIG = this.getInt("WIG_TIMEOUT_CONSULTA");
		this.diretorioRetornoPesquisaAparelhoWIG = this.getString("WIG_DIRETORIO_RETORNO");
		this.formaEnvioSMS = this.getString("FORMA_ENVIO_SMS");
		this.dirRelChurn = this.getString("DIR_REL_CHURN");
		this.dirRootFabricaRelatorio = this.getString("DIR_ROOT_FABRICA_RELATORIO");
		this.dirBateVolta = this.getString("DIR_BATE_VOLTA");

		this.toolbarPorta = this.getInt("TOOLBAR_PORTA");
		this.toolbarNumTentativas = this.getShort("TOOLBAR_NUM_TENTATIVAS_CONEXAO");
		this.toolbarTempoEspera = this.getInt("TOOLBAR_TEMPO_ESPERA_CONEXAO");

		//Acertando propriedades do Log4j
		propriedadesLog4j = new Properties();
		Iterator i = this.getMapa().keySet().iterator();
		while(i.hasNext()){
			String prop = (String)i.next();
			if(prop.startsWith("log4j"))
				propriedadesLog4j.put(prop ,this.getString(prop));
		}

		System.out.println("ArquivoConfiguracaoGPP->getPropriedades() - Fim");
	}
	/**
	 * @return the toolbarNumTentativas
	 */
	public short getToolbarNumTentativas()
	{
		return toolbarNumTentativas;
	}

	/**
	 * @return the toolbarPorta
	 */
	public int getToolbarPorta()
	{
		return toolbarPorta;
	}

	/**
	 * @return the toolbarTempoEspera
	 */
	public int getToolbarTempoEspera()
	{
		return toolbarTempoEspera;
	}

	/**
	 * @return Returns the diretorioRetornoPesquisaAparelhoWIG.
	 */
	public String getDiretorioRetornoPesquisaAparelhoWIG() {
		return diretorioRetornoPesquisaAparelhoWIG;
	}

	/**
	 * @return Returns the portaWIG.
	 */
	public int getPortaWIG() {
		return portaWIG;
	}

	/**
	 * @return Returns the servidorWIG.
	 */
	public String getServidorWIG() {
		return servidorWIG;
	}

	/**
	 * @return Returns the timeOutWIG.
	 */
	public int getTimeOutWIG() {
		return timeOutWIG;
	}

	/**
	 * @return Returns the urlRetornoPesquisaAparelhoWIG.
	 */
	public String getUrlRetornoPesquisaAparelhoWIG() {
		return urlRetornoPesquisaAparelhoWIG;
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
	 * Metodo...: getAtivaDebug
	 * Descricao: Retorna o status do flag de ativacao/desativacao do Debug
	 * @param
	 * @return boolean	- Flag de Ativacao/Desativacao de Debug
	 * @throws
	 */
	public boolean getAtivaDebug ( )
	{
		return this.ativaDebug;
	}

	/**
	 * Metodo...: getSaidaDebug
	 * Descricao: Retorna o status do flad de Debug
	 * @param
	 * @return boolean	- Flag de Saida de Debug
	 * @throws
	 */
	public boolean getSaidaDebug ( )
	{
		return this.saidaDebug;
	}

	/**
	 * Metodo...: getPortaOrbGPP
	 * Descricao: Retorna a porta ORB do GPP
	 * @param
	 * @return String	- Porta ORB do GPP
	 * @throws
	 */
	public String getPortaOrbGPP ( )
	{
		return this.portaOrbGPP;
	}

	/**
	 * Metodo...: getEnderecoOrbGPP
	 * Descricao: Retorna o endereco IP do GPP
	 * @param
	 * @return 	String	- Enredero IP do GPP
	 * @throws
	 */
	public String getEnderecoOrbGPP ( )
	{
		return this.enderecoOrbGPP;
	}

	/**
	 * Metodo...: getPortaNameServiceGPP
	 * Descricao: Retorna a porta do NameService
	 * @param
	 * @return String	- Porta do NameService
	 * @throws
	 */
	public String getPortaNameServiceGPP ( )
	{
		return this.portaNameServiceGPP;
	}

	/**
	 * Metodo...: getEnderecoNameServiceGPP
	 * Descricao: Retorna o endereco IP do NameService
	 * @param
	 * @return 	String	- Enredero IP do NameService
	 * @throws
	 */
	public String getEnderecoNameServiceGPP ( )
	{
		return this.enderecoNameServiceGPP;
	}

	/**
	 * Metodo...: getNomeOrbPOARoot
	 * Descricao: Retorna o nome do POA ROOT
	 * @param
	 * @return 	String	- Nome do ROOT POA
	 * @throws
	 */
	public String getNomeOrbPOARoot ( )
	{
		return this.nomeOrbPOARoot;
	}

	/**
	 * Metodo...: getNomeAtivaComponentesPOA
	 * Descricao: Retorna o nome d POA do AtivaComponentes
	 * @param
	 * @return 	String	- Nome ROOT POA do AtivaComponentes
	 * @throws
	 */
	public String getNomeAtivaComponentesPOA ( )
	{
		return this.nomeAtivaComponentesPOA;
	}

	/**
	 * Metodo...: getNomeAprovisionamentoPOA
	 * Descricao: Retorna o nome do POA de AProvisionamento
	 * @param
	 * @return 	String	- Nome do POA de AProvisionamento
	 * @throws
	 */
	public String getNomeAprovisionamentoPOA ( )
	{
		return this.nomeAprovisionamentoPOA;
	}

	/**
	 * Metodo...: getNomeConsultaPOA
	 * Descricao: Retorna o nome do POA de Consulta
	 * @param
	 * @return 	String	- Nome do POA de Consulta
	 * @throws
	 */
	public String getNomeConsultaPOA ( )
	{
		return this.nomeConsultaPOA;
	}

	/**
	 * Metodo...: getNomeRecargaPOA
	 * Descricao: Retorna o nome do POA de Recarga
	 * @param
	 * @return 	String	- Nome do POA de Recarga
	 * @throws
	 */
	public String getNomeRecargaPOA ( )
	{
		return this.nomeRecargaPOA;
	}

   /**
	 * Metodo...: getNomeProcessosBatchPOA
	 * Descricao: Retorna o nome do POA dos ProcessosBatch
	 * @param
	 * @return 	String	- Nome do POA dos ProcessosBatch
	 * @throws
	 */
	public String getNomeProcessosBatchPOA ( )
	{
		return this.nomeProcessosBatchPOA;
	}

   /**
	 * Metodo...: getNomeGerenteGPPPOA
	 * Descricao: Retorna o nome do POA do Gerente GPP
	 * @param
	 * @return 	String	- Nome do POA do Gerente GPP
	 * @throws
	 */
	public String getNomeGerenteGPPPOA ( )
	{
		return this.nomeGerenteGPPPOA;
	}

	/**
	 * Metodo...: getTempoMaximoEspera
	 * Descricao: Retorna o tempo maximo de expera para conseguir uma conexao
	 * @param
	 * @return 	int	- Tempo maximo para conexao
	 * @throws
	 */
	public int getTempoMaximoEspera ( )
	{
		return this.tempoMaximoEspera;
	}

	/**
	 * Metodo...: getTempoEspera
	 * Descricao: Retorna o tempo de "dormindo" para pegar uma conexao
	 * @param
	 * @return 	int	- Tempo de espera para obter uma conexao
	 * @throws
	 */
	public int getTempoEspera ( )
	{
		return this.tempoEspera;
	}

	/**
	 * Metodo...: getTempoEsperaAtivarVoucher
	 * Descricao: Retorna o tempo de espera para se ativar um voucher
	 * @param
	 * @return 	int	- Tempo espera para ativar voucher
	 * @throws
	 */
	public int getTempoEsperaAtivarVoucher ( )
	{
		return this.tempoEsperaAtivarVoucher;
	}

	/**
	 * Metodo...: getMaximoNumeroInteracoes
	 * Descricao: Retorna o máximo número de interações para se ativar voucher
	 * @param
	 * @return 	int	- Máximo Número de Interações
	 * @throws
	 */
	public int getMaximoNumeroInteracoes ( )
	{
		return this.maximoNumeroInteracoes;
	}

	/**
	 * Metodo...: getTempoEsperaSMS
	 * Descricao: Retorna o tempo de "dormindo" para pegar dados no SMS
	 * @param
	 * @return 	int	- Tempo de espera para obter dados de SMS
	 * @throws
	 */
	public int getTempoEsperaSMS ( )
	{
		return this.tempoEsperaSMS;
	}

	/**
	 * Metodo...: getTempoMaxEsperaSMS
	 * Descricao: Retorna o tempo maximo de "dormindo" para pegar dados no SMS
	 * @param
	 * @return 	int	- Tempo maximo de espera para obter dados de SMS
	 * @throws
	 */
	public int getTempoMaxEsperaSMS ( )
	{
		return this.tempoMaxEsperaSMS;
	}

	/**
	 * Metodo...: getNumeroConexoesAprovisionamento
	 * Descricao: Retorna o numero de conexoes com o componente de Aprovisionamento da Tecnomen
	 * @param
	 * @return 	int	- Numero de conexoes com o Aprovisionmento da Tecnonem
	 * @throws
	 */
	public int getNumeroConexoesAprovisionamento ( )
	{
		return this.numeroConexoesAprovisionamento;
	}

	/**
	 * Metodo...: getNumeroConexoesRecarga
	 * Descricao: Retorna o numero de conexoes com o componente de Recarga da Tecnomen
	 * @param
	 * @return 	int	- Numero de conexoes com o componente de Recarga da Tecnonem
	 * @throws
	 */
	public int getNumeroConexoesRecarga ( )
	{
		return this.numeroConexoesRecarga;
	}

	/**
	 * Metodo...: getNumeroConexoesVoucher
	 * Descricao: Retorna o numero de conexoes com o componente de Voucher da Tecnomen
	 * @param
	 * @return 	int	- Numero de conexoes com o componente de Voucher da Tecnonem
	 * @throws
	 */
	public int getNumeroConexoesVoucher ( )
	{
		return this.numeroConexoesVoucher;
	}

	/**
	 * Metodo...: getNumeroConexoesAdmin
	 * Descricao: Retorna o numero de conexoes com o componente de Admin da Tecnomen
	 * @param
	 * @return 	int	- Numero de conexoes com o Admin da Tecnonem
	 * @throws
	 */
	public int getNumeroConexoesAdmin ( )
	{
		return this.numeroConexoesAdmin;
	}

	/**
	 * Metodo...: getNumeroConexoesBanco
	 * Descricao: Retorna o numero de conexoes com o Bando de Dados
	 * @param
	 * @return 	int	- Numero de conexoes com o Banco de Dados
	 * @throws
	 */
	public int getNumeroConexoesBanco ( )
	{
		return this.numeroConexoesBanco;
	}

	/**
	 * Metodo...: getNomeUsuarioBancoDados
	 * Descricao: Retorna o nome do usuario do Banco de Dados
	 * @param
	 * @return 	String 	- Nome do usuario do Banco de Dados
	 * @throws
	 */
	public String getNomeUsuarioBancoDados ( )
	{
		return this.nomeUsuarioBancoDados;
	}

	/**
	 * Metodo...: getSenhaUsuarioBancoDados
	 * Descricao: Retorna a senha do usuario do Banco de dados
	 * @param
	 * @return 	String 	- Senha do usuario do Banco de dados
	 * @throws
	 */
	public String getSenhaUsuarioBancoDados ( )
	{
		return this.senhaUsuarioBancoDados;
	}

   /**
	 * Metodo...: getInstanciaOracleBancoDados
	 * Descricao: Retorna a instancia oracle do Banco de Dados
	 * @param
	 * @return 	String	- Instancia oracle do Banco de Dados
	 * @throws
	 */
	public String getInstanciaOracleBancoDados ( )
	{
		return this.instanciaOracleBancoDados;
	}

	/**
	 * Metodo...: getURLBancoDados
	 * Descricao: Retorna a url de conexão ao banco de dados
	 * @param
	 * @return 	String	- Url de conexão ao Bando de Dados
	 * @throws
	 */
	public String getURLBancoDados ( )
	{
		return this.urlOracleBancoDados;
	}

	/**
	 * Metodo...: getNomeUsuarioTecnomen
	 * Descricao: Retorna o nome do usuario de acesso a plataforma Tecnomen
	 * @param
	 * @return 	String	- Nome do usuario de acesso a plataforma Tecnomen
	 * @throws
	 */
	public String getNomeUsuarioTecnomen ( )
	{
		return this.nomeUsuarioTecnomen;
	}

	/**
	 * Metodo...: getSenhaUsuarioTecnomen
	 * Descricao: Retorna a senha do usuario de acesso a plataforma Tecnomen
	 * @param
	 * @return 	String	- Senha do usuario de acesso a plataforma Tecnomen
	 * @throws
	 */
	public String getSenhaUsuarioTecnomen ( )
	{
		return this.senhaUsuarioTecnomen;
	}

	/**
	 * Metodo...: getNomeUsuarioTecnomenVoucher
	 * Descricao: Retorna o nome do usuario de acesso a plataforma Tecnomen Voucher
	 * @param
	 * @return 	String	- Nome do usuario de acesso a plataforma Tecnomen Voucher
	 * @throws
	 */
	public String getNomeUsuarioTecnomenVoucher ( )
	{
		return this.nomeUsuarioTecnomenVoucher;
	}

	/**
	 * Metodo...: getSenhaUsuarioTecnomenVoucher
	 * Descricao: Retorna a senha do usuario de acesso a plataforma Tecnomen Voucher
	 * @param
	 * @return 	String	- Senha do usuario de acesso a plataforma Tecnomen Voucher
	 * @throws
	 */
	public String getSenhaUsuarioTecnomenVoucher ( )
	{
		return this.senhaUsuarioTecnomenVoucher;
	}

	/**
	 *	Retorna o string de referencia ao Servidor de Autenticacao da Tecnomen.
	 *
	 *	@return		String de referencia ao Servidor de Autenticacao da Tecnomen.
	 */
	public String getAuthServerReferencia()
	{
		return this.authServerReferencia;
	}


	/**
	 * Metodo...: getMensagemURLSMSC
	 * Descricao: Retorna o endereço http da mensagem
	 * @param
	 * @return 	URL da mensagem
	 * @throws
	 */
	public String getMensagemURLSMSC ( )
	{
		return this.mensagemURL;
	}

	/**
	 * Metodo...: getServicoAplicacaoSMSC
	 * Descricao: Retorna o serviço da aplicação de SMSC
	 * @param
	 * @return 	Número do serviço
	 * @throws
	 */
	public String getServicoAplicacaoSMSC ( )
	{
		return this.servicoAplicacaoSMSC;
	}

	/**
	 * Metodo...: getItemAplicacaoSMSC
	 * Descricao: Retorna o item da aplicação de SMSC
	 * @param
	 * @return 	Número do item
	 * @throws
	 */
	public String getItemAplicacaoSMSC ( )
	{
		return this.itemAplicacaoSMSC;
	}

	/**
	 * Metodo...: getOriginadorSMSC
	 * Descricao: Retorna o Originador de SMSC
	 * @param
	 * @return 	Número do originador
	 * @throws
	 */
	public int getOriginadorSMSC ( )
	{
		return this.originadorSMSC;
	}

	/**
	 * Metodo...: getCodigoTarifacao
	 * Descricao: Retorna o código de tarifação de SMSC
	 * @param
	 * @return 	Código de tarifação
	 * @throws
	 */
	public int getCodigoTarifacaoSMSC ( )
	{
		return this.codigoTarifacao;
	}

	/**
	 * Metodo...: getNomeUsuarioMASC
	 * Descricao: Retorna o nome do usuario do MASC
	 * @param
	 * @return 	String	- Nome do usuario do MASC
	 * @throws
	 */
	public String getNomeUsuarioMASC ( )
	{
		return this.nomeUsuarioMASC ;
	}

	/**
	 * Metodo...: getSenhaUsuarioMASC
	 * Descricao: Retorna a senha do usuario do MASC
	 * @param
	 * @return 	String	- Senha do usuario do MASC
	 * @throws
	 */
	public String getSenhaUsuarioMASC ( )
	{
		return this.senhaUsuarioMASC;
	}

	/**
	 * Metodo...: getEnderecoMaquinaMASC
	 * Descricao: Retorna o endereco da maquina do MASC
	 * @param
	 * @return 	String	- Endereco da maquina do MASC
	 * @throws
	 */
	public String getEnderecoMaquinaMASC ( )
	{
		return this.enderecoMaquinaMASC;
	}

	/**
	 * Metodo...: getPortaMaquinaMASC
	 * Descricao: Retorna a porta oracle do MASC
	 * @param
	 * @return 	String	- Porta oracle do MASC
	 * @throws
	 */
	public String getPortaMaquinaMASC ( )
	{
		return this.portaMaquinaMASC;
	}

	/**
	 * Metodo...: getNumTentativasReadln
	 * Descricao: Retorna o Numero de Tentativas de Readln no socket
	 * @param
	 * @return 	Número de Tentativas de Readln no socket
	 * @throws
	 */
	public int getNumTentativasReadln ( )
	{
		return this.numTentativasReadln;
	}

	/**
	 * Metodo...: getTempoTimeoutReadln
	 * Descricao: Retorna o Tempo entre Tentativas de Readln no socket em milisegundos
	 * @param
	 * @return 	Tempo entre Tentativas de Readln no socket (ms)
	 * @throws
	 */
	public int getTempoTimeoutReadln ( )
	{
		return this.tempoTimeoutReadln;
	}

	/**
	 * Metodo...: getNumeroTentativasConexao
	 * Descricao: Retorna o numero de conexoes
	 * @param
	 * @return 	int	- Numero de conexoes
	 * @throws
	 */
	public int getNumeroTentativasConexao() {
		return numeroTentativasConexao;
	}

	/**
	 * Metodo...: getNumeroTentativasConexaoTecnomen
	 * Descricao: Retorna o numero de conexoes na Tecnomen
	 * @param
	 * @return 	int	- Numero de conexoes na Tecnomen
	 * @throws
	 */
	public int getNumeroTentativasConexaoTecnomen() {
		return numeroTentativasConexaoTecnomen;
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
		return new Properties();
	}

	/**
	 * Metodo...: getNumeroThreadsEnvioSMS
	 * Descricao: Retorna o numero de threads de envio que serao inicializadas
	 *            no pool de envio pelo ConsumidorSMS
	 * @return int - Numero de threads para envio de SMS
	 */
	public int getNumeroThreadsEnvioSMS()
	{
		return numeroThreadsEnvioSMS;
	}

	/**
	 * Metodo....:deveConsumirSMS
	 * Descricao.:Indica se o produtor SMS deve ser iniciado
	 * @return boolean
	 */
	public boolean deveConsumirSMS()
	{
		return deveConsumirSMS;
	}

	/**
	 * Metodo....:deveProcessarFilaRecarga
	 * Descricao.:Indica se deve processar a fila de recarga
	 * @return boolean
	 */
	public boolean deveProcessarFilaRecarga()
	{
		return deveProcessarFilaRecarga;
	}

	/**
	 * Metodo....:deveGerenciarPedidosVoucher
	 * Descricao.:Indica se deve gerenciar os pedidos de voucher
	 * @return boolean
	 */
	public boolean deveGerenciarPedidosVoucher()
	{
		return deveGerenciarPedidosVoucher;
	}

	/**
	 * Metodo....:deveGerenciarSMS
	 * Descricao.:Indica se deve gerenciar os testes de SMS
	 * @return boolean
	 */
	public boolean deveGerenciarSMS()
	{
		return deveGerenciarSMS;
	}

	/**
	 * Metodo....:deveImportarCDR
	 * Descricao.:Indica se deve importar CDRs
	 * @return boolean
	 */
	public boolean deveImportarCDR()
	{
		return deveImportarCDR;
	}

	/**
	 * Metodo....:deveGerenciarHotLine
	 * Descricao.:Indica se deve gerenciar desbloqueio de hot-line
	 * @return boolean
	 */
	public boolean deveGerenciarHotLine()
	{
		return deveGerenciarHotLine;
	}
	/**
	 * Metodo...: setAtivaDebug
	 * Descricao: Associa o flag de ativacao/desativacao do Debug (modo de escrita no log)
	 * @param  aAtivaDebug	- Associa o flag de ativacao/desativacao do Debug (modo de escrita no log)
	 * @return
	 * @throws
	 */
	public void setAtivaDebug ( boolean aAtivaDebug )
	{
		this.ativaDebug = aAtivaDebug;
	}

	/**
	 * Metodo...: setSaidaDebug
	 * Descricao: Associa o flag de Debug (modo de exibicao)
	 * @param  aSaidaDebug	- Associa o flag de Debug (modo de exibicao)
	 * @return
	 * @throws
	 */
	public void setSaidaDebug ( boolean aSaidaDebug )
	{
		this.saidaDebug = aSaidaDebug;
	}

	/**
	 * Metodo....:getDirHistoricoCdr
	 * Descricao.:Retorna o diretorio de historico de importacao dos CDRs
	 * @return String	- Diretorio onde os arquivos serao armazenados apos processamento
	 */
	public String getDirHistoricoCdr()
	{
		return dirHistoricoCdr;
	}

	/**
	 * Metodo....:getDirImportacaoCdr
	 * Descricao.:Retorna o diretorio de importacao dos CDRs
	 * @return String	- Diretorio onde os arquivos serao armazenados porem ainda nao processados
	 */
	public String getDirImportacaoCdr()
	{
		return dirImportacaoCdr;
	}

	/**
	 * Metodo....:getDirTrabalhoCdr
	 * Descricao.:Retorna o diretorio de trabalho de importacao dos CDRs
	 * @return String	- Diretorio onde os arquivos serao armazenados temporariamente
	 *                    para processamento
	 */
	public String getDirTrabalhoCdr()
	{
		return dirTrabalhoCdr;
	}

	/**
	 * Metodo....:getDirRejeicaoCdr
	 * Descricao.:Retorna o diretorio de rejeicao da importacao dos CDRs
	 * @return String	- Diretorio onde os arquivos rejeitados serao armazenados
	 */
	public String getDirRejeicaoCdr()
	{
		return dirRejeicaoCdr;
	}

	/**
	 * Metodo....:getDirGenevaOrigem
	 * Descricao.:Retorna o diretorio de importacao dos arquivos Geneva
	 * @return String	- Diretorio onde os arquivos são processados
	 */
	public String getDirGenevaOrigem()
	{
		return dirGenevaOrigem;
	}

	/**
	 * Metodo....:getDirGenevaDestino
	 * Descricao.:Retorna o diretorio de destino dos arquivos Geneva processados
	 * @return String	- Diretorio dos arquivos já processados
	 */
	public String getDirGenevaDestino()
	{
		return dirGenevaDestino;
	}
	/**
	 * Metodo....:getNomeServidorSocket
	 * Descricao.:Retorna o nome do servidor socket a ser utilizado
	 * @return String	- Nome ou endereco IP do servidor socket
	 */
	public String getNomeServidorSocket()
	{
		return nomeServidorSocket;
	}
	/**
	 * Metodo....:getPortaServidorSocket
	 * Descricao.:Retorna a porta do servidor socket a ser utilizado
	 * @return int - Porta IP a ser utilizada para o servidor socket
	 */
	public int getPortaServidorSocket()
	{
		return portaServidorSocket;
	}
	public String getFormaEnvioSMS() {
		return formaEnvioSMS;
	}
	/**
	 * @return Returns the diretorioRelChurn.
	 */
	public String getDirRelChurn() {
		return dirRelChurn;
	}
	/**
	 * @return Retorna o dirRootFabricaRelatorio.
	 */
	public String getDirRootFabricaRelatorio() {
		return dirRootFabricaRelatorio;
	}
	/**
	 * @return Retorna o dirBateVolta.
	 */
	public String getDirBateVolta()
	{
		return dirBateVolta;
	}
}

