// Arquivos de Imports Java/CORBA
import org.omg.PortableServer.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;

import java.io.*;

// Classes de Objetos de Negocio Importadas
import com.brt.gpp.aplicacoes.enviarSMS.EnvioSMSProdutor;
import com.brt.gpp.aplicacoes.gerenciarGPP.GerenteGPP;
import com.brt.gpp.componentes.aprovisionamento.ComponenteNegocioAprovisionamento;
import com.brt.gpp.componentes.consulta.ComponenteNegocioConsulta;
import com.brt.gpp.componentes.recarga.ComponenteNegocioRecarga;
import com.brt.gpp.componentes.processosBatch.ComponenteNegocioProcessosBatch;
import com.brt.gpp.componentes.gerenteGPP.ComponenteNegocioGerenteGPP;

// Gerentes
import com.brt.gpp.gerentesPool.GerenteORB;
import com.brt.gpp.gerentesPool.GerentePoolSMPP;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerenteProdutorRecarga;
//import com.brt.gpp.gerentesPool.GerenteArquivosCDR;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.GerenciaPedidosVoucher;
import com.brt.gpp.aplicacoes.aprovisionar.GerenciadorDesbloqueioHotLine;

// Arquivos de Imports do GPP
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamentos;

// Constantes
import com.brt.gpp.comum.Definicoes;

//Outros
import com.brt.gpp.aplicacoes.gerenciarGPP.FinalizaGPP;
import com.brt.gpp.aplicacoes.importacaoCDR.GerenciadorCacheFF;
//import com.brt.gpp.aplicacoes.gerenciarGPP.GerenteGPP;

/**
  *
  * Este arquivo refere-se a classe StartComponents. Esta classe e responsavel pela ativacao
  * dos componentes, do ORB e de todos os gerenciadores de POOls (Bancos, Logs, Conexcao com Tecnomen), etc.
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Daniel Cintra Abib
  * Data: 				18/02/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class AtivaComponentes
{
	// Variáveis Membro
    static GerentePoolLog 			gerenteLog;
    static GerentePoolTecnomen 		gerenteTecnomen;
    static GerentePoolBancoDados 	gerenteBD;
    static GerentePoolSMPP			gerenteSMPP;
    static ArquivoConfiguracaoGPP 	arquivoConfiguracao;

	/**
	 * Construtor Padrao
	 *
	 */
	public AtivaComponentes ( )
	{
		// Nao realiza acao nenhuma
	}

	/**
	 * Cria a conexao ao ORB, ativa as classes servantes e gerenciadoras
	 *
	 * @param	args	Argumentos da linha de comanado que ativou o sistema
	 */
	public static void main(String[] args)
	{
		// Variaveis Locais
		ORB conexaoORB; 	// Referencia ao OBR CORBA
		POA objetoPOA;		// Referencia a POA (Portable Object Adapter)

		//System.getSecurityManager().checkExit(1);
		// Verifica argumentos passados
		if ( args.length == 0 )
		{
			System.out.println ("Uso Correto: AtivaComponentes <arquivo de configuracao>");
			System.exit( 1 ); // Retornar erro ao sistema
		}

		try
		{
			// Lendo o arquivo de configuracao do GPP
			arquivoConfiguracao = ArquivoConfiguracaoGPP.getInstance(args[0]);
			// Iniciando o componente de gerenciamento de LOG
			gerenteLog = GerentePoolLog.getInstancia(AtivaComponentes.class);
			//Iniciando o Gerente do Object Request Broker da aplicacao.
			conexaoORB = GerenteORB.newInstance(args).getOrb();
			// Iniciando o componente de gerenciamento de conexoes com a Tecnomen
			gerenteTecnomen = GerentePoolTecnomen.getInstancia(0);
			// Iniciando o componente de gerenciamento de conexoes ao banco de dados
	  		gerenteBD = GerentePoolBancoDados.getInstancia(0);
	  		// Iniciando o componente de gerenciamento de conexoes com a plataforma SMSC
	  		gerenteSMPP = GerentePoolSMPP.getInstancia(0);
	  		// Cria o pool de conexoes para o Banco de Dados
	  		criaPoolBD();

	  		// Se nao conseguiu criar conexoes de banco de dados entao todo o sistema e finalizado
	  		if (gerenteBD.getNumeroConexoesBancoDadosDisponiveis() == 0)
	  		{
	  			gerenteLog.log(0,Definicoes.FATAL,"AtivaComponentes","main","Nao ha conexoes de banco de dados disponiveis");
	  			GerenteGPP gerGPP = new GerenteGPP(0);
	  			gerGPP.paraGPP();
	  			System.exit(1);
	  		}

			// Criando os Mapeamentos de Dados em Memoria
			Mapeamentos.getInstance();
			// Iniciando o gerente de producao de SMS
			
			//Realiza o registro do GPP no Name Service e no OSAgent.
	        gerenteLog.logComponente (Definicoes.INFO, Definicoes.PR_ATIVA_COMPONENTES, "Inicializando registro do sistema GPP na implementacao CORBA");
			// Obtem uma referencia ao POA ROOT
			POA rootPOA = POAHelper.narrow (conexaoORB.resolve_initial_references( arquivoConfiguracao.getNomeOrbPOARoot() ) );
			// Cria as politicas para o POA Persistente
			Policy[] policies = { rootPOA.create_lifespan_policy(LifespanPolicyValue.PERSISTENT) };
			// Cria o POA com as poticicas criadas
			objetoPOA = rootPOA.create_POA(arquivoConfiguracao.getNomeAtivaComponentesPOA(), rootPOA.the_POAManager(), policies );
	        // Ativando o gerenciador POA
			objetoPOA.the_POAManager().activate();

			// Registra o GPP no NameService, criando o contexo especifico para o Sistema
			// e inicializa os componentes GPP no CORBA tanto no OSAgent quanto no NameService
			registraGPPCORBA(conexaoORB,objetoPOA);

			// ********* Busca o Singleton da classe de Processamento de CDRs
			//GerenteArquivosCDR gerCDR = GerenteArquivosCDR.getInstance(0);
			// ********* Inicia o pool Friends&Family de assinantes para a importacao de CDRs
			GerenciadorCacheFF.getInstance();
			// ********* Inicializa o Singleton para processamento da fila de recargas
			GerenteProdutorRecarga.getInstancia();
			// ********* Inicializa o Singleton para gerenciamento de pedido de voucher
			GerenciaPedidosVoucher.getInstancia();
			// ********* Cria o pool de conexoes com a plataforma SMSC.
			gerenteSMPP.criarPool();
			// ********* Inicializa o Singleton para gerenciamento de SMS
			EnvioSMSProdutor envioSMSProdutor = EnvioSMSProdutor.getInstancia(0);
			envioSMSProdutor.iniciarConsumoSMS();
			// ********* Inicializa o Singleton para gerenciamento de desbloqueio hot-line
			GerenciadorDesbloqueioHotLine.getInstance();

			// Cria thread para finalizacao de conexoes de banco de dados e tecnomen
			criaFinalizacaoGPP (gerenteLog);
	        gerenteLog.logComponente (Definicoes.INFO, Definicoes.PR_ATIVA_COMPONENTES, "Servidor do GPP (Gateway de Pre-pago) ativo..." );
	        // Espera as requisicoes de conexao
	        conexaoORB.run();
		}
		catch (Exception unknowException)
		{
			System.out.println ("Exception desconhecida.");
			unknowException.printStackTrace();
		}
	}

	/**
	 * Metodo....:registraNameService
	 * Descricao.:Realiza o registro do GPP no NameService e logo apos realiza o registro
	 *            dos componentes de Negocio
	 * @param conexaoORB - Conexao CORBA (ORB) a ser utilizado
	 * @param objetoPOA  - Objeto POA para registro dos objetos no OSAgent
	 * @throws Exception
	 */
	private static void registraGPPCORBA(ORB conexaoORB,POA objetoPOA) throws Exception
	{
		// Cria os objetos Servant (Componentes que serao utilizados)
		ComponenteNegocioGerenteGPP 		gerenteGPP 			= new ComponenteNegocioGerenteGPP();
		ComponenteNegocioAprovisionamento 	aprovisionamento 	= new ComponenteNegocioAprovisionamento();
		ComponenteNegocioConsulta 			consulta 			= new ComponenteNegocioConsulta();
		ComponenteNegocioRecarga 			recarga 			= new ComponenteNegocioRecarga();
		ComponenteNegocioProcessosBatch 	processosBatch 		= new ComponenteNegocioProcessosBatch();

		boolean regObjetosNS = true;
		// Define os objetos necessarios para o registro do GPP no NameService
		// A realizacao dos registros de componentes eh realizado apos sendo
		// separado o registro entre os objetos no NameService do OSAgent devido
		// o sistema utilizar as duas formas para busca do objeto CORBA
		org.omg.CORBA.Object obj;
		NamingContextExt rootContext = null;
		NamingContext 	 gppContext	 = null;
		NameComponent[] gppC = {new NameComponent("gpp","")};
		try
		{
			obj 		= conexaoORB.resolve_initial_references("NameService");
			rootContext = NamingContextExtHelper.narrow( obj );
			gppContext  = rootContext.new_context();
			rootContext.bind_context(gppC,gppContext);
		}
		catch(OBJECT_NOT_EXIST e)
		{
			// Em caso de Erro no NameService, imprime uma mensagem de ERRO,mas inicializa o GPP
			// pois os objetos poderao ser utilizados pelo OSAgent mas nao no Name Service
			gerenteLog.logComponente(Definicoes.WARN,Definicoes.PR_ATIVA_COMPONENTES, "Servidor de Nomes CORBA nao esta ativo. GPP Ira somente registrar objetos no OSAgent" );
			regObjetosNS = false;
		}
		catch(AlreadyBound ex)
		{
			org.omg.CORBA.Object objRef = rootContext.resolve(gppC);
			gppContext = NamingContextHelper.narrow(objRef);
		}
		gerenteLog.logComponente (Definicoes.DEBUG, Definicoes.PR_ATIVA_COMPONENTES, "Inicializando registro de objetos CORBA no OSAgent");
		// Realiza o registro de objetos (componentes) no OSAgent. Caso o GPP tenha conseguido
		// realizar o registro no NameServices, entao registra os servants lah tambem
		registraServantOSAgent(conexaoORB,objetoPOA,gerenteGPP		,arquivoConfiguracao.getNomeGerenteGPPPOA());
		registraServantOSAgent(conexaoORB,objetoPOA,aprovisionamento,arquivoConfiguracao.getNomeAprovisionamentoPOA());
		registraServantOSAgent(conexaoORB,objetoPOA,consulta		,arquivoConfiguracao.getNomeConsultaPOA());
		registraServantOSAgent(conexaoORB,objetoPOA,recarga			,arquivoConfiguracao.getNomeRecargaPOA());
		registraServantOSAgent(conexaoORB,objetoPOA,processosBatch	,arquivoConfiguracao.getNomeProcessosBatchPOA());
		// Portanto se conseguiu inicializar o GPP no NameService entao registra os componentes
		if (regObjetosNS)
		{
			gerenteLog.logComponente (Definicoes.DEBUG, Definicoes.PR_ATIVA_COMPONENTES, "Inicializando registro de objetos CORBA utilizando o NameService");
			registraServantNS(objetoPOA,rootContext,gppContext,gerenteGPP		,arquivoConfiguracao.getNomeGerenteGPPPOA());
			registraServantNS(objetoPOA,rootContext,gppContext,aprovisionamento	,arquivoConfiguracao.getNomeAprovisionamentoPOA());
			registraServantNS(objetoPOA,rootContext,gppContext,consulta			,arquivoConfiguracao.getNomeConsultaPOA());
			registraServantNS(objetoPOA,rootContext,gppContext,recarga			,arquivoConfiguracao.getNomeRecargaPOA());
			registraServantNS(objetoPOA,rootContext,gppContext,processosBatch	,arquivoConfiguracao.getNomeProcessosBatchPOA());
		}
	}

	/**
	 * Metodo....:registraServantOSAgent
	 * Descricao.:Realiza o registro dos Componentes no OSAgent
	 * @param conexaoORB - Conexao CORBA (ORB) a ser utilizado
	 * @param objetoPOA  - Objeto POA onde serah registrado o componente
	 * @param servant    - Componente a ser registrado
	 * @param nomeObjeto - Nome do componente
	 * @throws Exception
	 */
	private static void registraServantOSAgent(ORB conexaoORB, POA objetoPOA, Servant servant, String nomeObjeto) throws Exception
	{
		// Registra o Servant (Componente) sendo o ID baseado no nome deste componente
		// logo apos cria o arquivo de IOR do mesmo
		byte[] idServant = nomeObjeto.getBytes();
		objetoPOA.activate_object_with_id(idServant,servant);
		geraArquivoIOR(conexaoORB,objetoPOA.servant_to_reference(servant),"./IOR/"+nomeObjeto+".ior");
	}

	/**
	 * Metodo....:registraServantNS
	 * Descricao.:Realiza o registro dos Componentes no NameService
	 * @param objetoPOA   - Objeto POA a ser utilizado para criar a referencia do servant
	 * @param rootContext - Contexto root para gerar referencia do servant
	 * @param gppContext  - Contexto do GPP onde serah registrado o objeto
	 * @param servant     - Componente a ser registrado
	 * @param nomeObjeto  - Nome do componente a ser associado no name service
	 * @throws Exception
	 */
	private static void registraServantNS(POA objetoPOA
			                             ,NamingContextExt rootContext
			                             ,NamingContext gppContext
			                             ,Servant servant
			                             ,String nomeObjeto) throws Exception
	{
		// Realiza o registro do Servant(Componente) no servidor de nomes CORBA - NameService
		NameComponent[] nc = rootContext.to_name(nomeObjeto);
		gppContext.rebind(nc, objetoPOA.servant_to_reference(servant));
	}

	/**
	 * Metodo: criaFinalizacaoGPP
	 * Descricao: Cria uma thread que sera chamada ao finalizar o sistema, para fechar as conexoes com
	 * 			  a plataforma Tecnomen e o Banco de dados
	 */
	protected static void criaFinalizacaoGPP (GerentePoolLog gerenteLog)
	{
		java.lang.Runtime runTime = Runtime.getRuntime();
		gerenteLog.logComponente (Definicoes.INFO, Definicoes.PR_ATIVA_COMPONENTES, "Ativando thread de finalizacao de conexoes" );
		// Adicionando a thread de finalizacao no GPP
		runTime.addShutdownHook(new Thread (new FinalizaGPP()));
		gerenteLog.logComponente (Definicoes.INFO, Definicoes.PR_ATIVA_COMPONENTES, "Thread de finalizacao de conexoes ativada" );
	}

	/**
	 * Metodo....:geraArquivoIOR
	 * Descricao.:Escreve o IOR dos componentes de Negocio CORBA no diretorio especificado
	 * @param conexaoORB - Conexao CORBA (ORB) a ser utilizado
	 * @param initRef    - Referencia do objeto a ser gerado o IOR
	 * @param nomeArquivoIOR - Diretorio e nome do arquivo onde serah gravado o IOR
	 */
	protected static void geraArquivoIOR(ORB conexaoORB, org.omg.CORBA.Object initRef, String nomeArquivoIOR)
	{
		try
		{
			FileWriter output = new FileWriter(nomeArquivoIOR);
	        output.write(conexaoORB.object_to_string(initRef));
	        output.close();
		}
		catch(IOException ioE)
		{
			System.out.println("Erro ao Gerar Arquivo de IOR: "+nomeArquivoIOR);
		}
	}

	/**
	 * Metodo...: criaPoolTecnomen()
	 * Descricao: Cria um pool de conexoes da Tecnomen
	 * @deprecated		Nao mais utilizado. O proprio Gerente cria o pool.
	 */
	/*
	private static void criaPoolTecnomen()
	{
	    try
	    {
	        gerenteTecnomen.criaPool();
	        gerenteLog.log (0, Definicoes.DEBUG, Definicoes.CL_GERENTE_TECNOMEN, "criaPoolTecnomen", "Gerente de Conexao com Tecnomem ATIVADO " );
	    }
	    catch (GPPInternalErrorException e)
		{
	        gerenteLog.log (0, Definicoes.FATAL, Definicoes.CL_GERENTE_TECNOMEN, "criaPoolTecnomen", "ERRO interno do GPP: "  + e);
		}
		catch (GPPTecnomenException e)
		{
		    gerenteLog.log (0, Definicoes.FATAL, Definicoes.CL_GERENTE_TECNOMEN, "criaPoolTecnomen", "ERRO TECNOMEN "  + e);
		}
		catch (GPPCorbaException e)
		{
		    gerenteLog.log (0, Definicoes.FATAL, Definicoes.CL_GERENTE_TECNOMEN, "criaPoolTecnomen", "ERRO CORBA: "  + e);
		}
		catch ( Exception e )
		{
			System.out.println (e);
			gerenteTecnomen = null;
			gerenteLog.log(0, Definicoes.FATAL, Definicoes.CL_GERENTE_TECNOMEN, "criaPoolTecnomen", "ERRO:"+e.getMessage() );
		}
	}
	*/

	/**
	 * Metodo...: criaPoolBD()
	 * Descricao: Cria um pool de conexoes de banco de dados
	 */
	private static void criaPoolBD()
	{
	    try
	    {
	        gerenteBD.criaPool();
			if (gerenteBD.getNumeroConexoesBancoDados() > 0)
			    gerenteLog.logComponente (Definicoes.DEBUG, Definicoes.GP_BANCO_DADOS, ": Gerente de Pool ATIVADO." );
			else
			    gerenteLog.logComponente (Definicoes.WARN, Definicoes.GP_BANCO_DADOS, ": Gerente de Pool inicializado SEM CONEXOES ATIVAS." );
		}
	    catch ( GPPInternalErrorException e )
		{
	        gerenteBD = null;
			gerenteTecnomen.destroiPool(0);

			// Erro fatal na ativacao do gerente do banco de dados
			gerenteLog.log(0, Definicoes.FATAL, Definicoes.GP_BANCO_DADOS, "criaPoolBD", "ERRO FATAL na ativacao do gerente do banco de dados:" + e +"\n Desativando GPP!" );
			System.exit(2);
		}
	}
}