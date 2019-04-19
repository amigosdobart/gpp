package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.app.Velocity;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.mapping.Mapping;

import br.com.brasiltelecom.ppp.dao.HibernateHelper;
import br.com.brasiltelecom.ppp.home.GrupoHome;
import br.com.brasiltelecom.ppp.home.OperacaoHome;

/**
 * É inicializado juntamente com o tomcat e inicializa as variáveis do portal
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class Startup extends HttpServlet
{
	private static final long serialVersionUID = 2274388971616232731L;

	// Thread para consulta status GPP;
	private static PingGPP pingGPP = null;
	/**
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig scfg) throws ServletException
	{
		ServletContext servletContext = scfg.getServletContext();

		PropertyConfigurator.configureAndWatch(servletContext.getRealPath("/WEB-INF/log4j.properties"));

		Logger logger = Logger.getLogger(this.getClass());

		logger.debug("Logger iniciado");

		logger.info("Inicio Portal Pre-pago");


		/**
		*  Iniciando o framework Velocity
		*/
		try{
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(servletContext.getRealPath("/WEB-INF/velocity.properties"));
			prop.load(in);
			in.close();
			prop.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, servletContext.getRealPath("/WEB-INF/templates"));
			Velocity.init(prop);
		}
		catch(Exception e){
			logger.error("Erro ao iniciar Velocity", e);

		}

		/**
		 * Iniciando o framework Hibernate
		 */
		/*String pathToHibernate = servletContext.getRealPath( "/WEB-INF/hibernate.cfg.xml");
		HibernateHelper.load(pathToHibernate);*/

		/**
		*  Iniciando o framework Castor
		*/

		try
		{
	        //java.io.PrintWriter log = new Logger( System.out ).setPrefix( "test" );
   			Mapping map = new Mapping();
	        //map.setLogWriter(log);
	        map.loadMapping("file://localhost/"+servletContext.getRealPath( "/WEB-INF/mapping.xml"));
	        JDO jdo = new JDO();
	        jdo.setConfiguration("file://localhost/"+servletContext.getRealPath( "/WEB-INF/database.xml" ));
	        jdo.setDatabaseName( "test" );

	        //jdo.setLogWriter(log);
			servletContext.setAttribute(Constantes.JDO, jdo);
			servletContext.setAttribute(Constantes.MAPPING, map);


			Database db = jdo.getDatabase();
			db.begin();

		/**
		 * Iniciando parametros do sistema
		 */
//			Map parametros = ParametroHome.findAll(db);
//			servletContext.setAttribute(Constantes.PARAMETRO,parametros);

			servletContext.setAttribute(Constantes.PATH_FABRICA_REL, scfg.getInitParameter("pathFabricaRelatorios"));
			servletContext.setAttribute(Constantes.PATH_ESTORNO_EXPURGO_PP, scfg.getInitParameter("pathEstornoExpurgoPP"));
			servletContext.setAttribute(Constantes.PATH_RECARGA_MASSA, scfg.getInitParameter("pathRecargaMassa"));

			servletContext.setAttribute(Constantes.GPP_NOME_SERVIDOR , scfg.getInitParameter("nomeServidorGPP"));
			servletContext.setAttribute(Constantes.GPP_PORTA_SERVIDOR, scfg.getInitParameter("portaServidorGPP"));
			servletContext.setAttribute(Constantes.GPP_PERIODO_PING  , scfg.getInitParameter("periodoPingGPP"));

			servletContext.setAttribute(Constantes.REPORTS_NOME_SERVIDOR , scfg.getInitParameter("nomeServidorReports"));
			servletContext.setAttribute(Constantes.REPORTS_PORTA_SERVIDOR, scfg.getInitParameter("portaServidorReports"));

			servletContext.setAttribute(Constantes.INTEGRACAO_ICHAIN, scfg.getInitParameter("integracaoiChain"));

			servletContext.setAttribute(Constantes.VITRIA_NOME_SERVIDOR , scfg.getInitParameter("nomeServidorVitria"));
			servletContext.setAttribute(Constantes.VITRIA_PORTA_SERVIDOR, scfg.getInitParameter("portaServidorVitria"));
			servletContext.setAttribute(Constantes.VITRIA_TIMEOUT_SERVIDOR, scfg.getInitParameter("timeoutServidorVitria"));

			servletContext.setAttribute(Constantes.SERVIDOR_SMTP, scfg.getInitParameter("servidorSMTP"));
			servletContext.setAttribute(Constantes.CONTA_EMAIL, scfg.getInitParameter("contaEmail"));

			servletContext.setAttribute(Constantes.LDAP_NOME_SERVIDOR  			, scfg.getInitParameter("nomeServidorLDAP"));
			servletContext.setAttribute(Constantes.LDAP_PORTA_SERVIDOR 			, scfg.getInitParameter("portaServidorLDAP"));
			servletContext.setAttribute(Constantes.LDAP_NOME_DOMAIN    			, scfg.getInitParameter("nomeDomainLDAP"));
			servletContext.setAttribute(Constantes.LDAP_DOMAIN_PASSWORD			, scfg.getInitParameter("passwordDomainLDAP"));
			servletContext.setAttribute(Constantes.LDAP_BASE_SEARCH    			, scfg.getInitParameter("baseSearchLDAP"));
			servletContext.setAttribute(Constantes.LDAP_NOME_ARQUIVO_CERTIFICADO, "/WEB-INF/"+scfg.getInitParameter("nomeArquivoCertificadoLDAP"));

			servletContext.setAttribute(Constantes.LISTA_COD_SRV_SFA_PULAPULA 	, scfg.getInitParameter(Constantes.LISTA_COD_SRV_SFA_PULAPULA));
			servletContext.setAttribute(Constantes.ID_MOTIVO_CONTESTACAO_RECARGA_PULAPULA 	, scfg.getInitParameter(Constantes.ID_MOTIVO_CONTESTACAO_RECARGA_PULAPULA));
			servletContext.setAttribute(Constantes.DATASOURCE_NAME,  scfg.getInitParameter(Constantes.DATASOURCE_NAME));

			servletContext.setAttribute(Constantes.DIRETORIO_RELATORIOS,  scfg.getInitParameter(Constantes.DIRETORIO_RELATORIOS));
			servletContext.setAttribute(Constantes.ENDERECO_PORTAL,  scfg.getInitParameter(Constantes.ENDERECO_PORTAL));

			servletContext.setAttribute(Constantes.WIG_NOME_SERVIDOR,  scfg.getInitParameter("nomeServidorWIG"));
			servletContext.setAttribute(Constantes.WIG_PORTA_SERVIDOR,  scfg.getInitParameter("portaServidorWIG"));

			servletContext.setAttribute(Constantes.IDT_MOSTRA_LOGIN,  scfg.getInitParameter("mostraLogin"));

			//Obtendo a lista de diretorios para geracao de arquivos temporarios de comprovantes
			ArrayList diretorios = new ArrayList();
			String diretorio = "";
			int i = 0;
			while((diretorio = scfg.getInitParameter("diretorioComprovantes" + String.valueOf(i))) != null)
			{
			    diretorios.add(diretorio);
			    i++;
			}
			servletContext.setAttribute(Constantes.DIRETORIO_COMPROVANTES, diretorios);


		/**
		 * Iniciando cache de Operacao
		 */
			logger.debug("Carregando operacoes na memoria");
			Map operacoes = OperacaoHome.findAll(db);
			servletContext.setAttribute(Constantes.OPERACOES,operacoes);

			Collection grupos = GrupoHome.findByFilter(db, new HashMap());
			servletContext.setAttribute(Constantes.GRUPOS, grupos);

			db.commit();
			db.close();



		}
		catch(Exception e)
		{
			logger.error("Erro ao iniciar Castor",e);
		}

		try
		{
		/**
		 * Iniciando autenticador
		 */
//			String autenticadorNome =  scfg.getInitParameter("autenticador");
//			Class autenImpl = Class.forName(autenticadorNome);
//			Autenticador autenticador = new Autenticador();
//			autenticador.setTheImplAutenticador((ImplAutenticador)autenImpl.newInstance() );
//			servletContext.setAttribute(Constantes.AUTENTICADOR,autenticador);
		}
		catch(Exception e)
		{
			logger.error("Erro ao iniciar Autenticador", e);
		}


		/*

		 try{
		java.util.Properties prop = new java.util.Properties();
		java.io.InputStream in = new java.io.FileInputStream(servletContext.getRealPath("/WEB-INF/log4j.properties"));
		prop.load(in);
		in.close();
		PropertyConfigurator.configure(prop);

		//PropertyConfigurator.configure(servletContext.getRealPath("/WEB-INF/log4j.properties"));
		}
		catch(java.io.IOException ioe){
			ioe.printStackTrace();
		}
		*/

		// Iniciação do Thread para Ping no GPP


		try {
			pingGPP = new PingGPP(scfg.getInitParameter("portaServidorGPP"),scfg.getInitParameter("nomeServidorGPP"));
			pingGPP.setDaemon(true);
			pingGPP.setPeriodo(Long.parseLong(scfg.getInitParameter("periodoPingGPP")));
			pingGPP.start();

		} catch (RuntimeException e) {
			logger.error("Erro ao iniciar Ping GPP", e);
		}

		// Pegando tipo e build do Deploy

		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream(new File(servletContext.getRealPath("/META-INF/deploy.dat"))));
			servletContext.setAttribute(Constantes.TIPO_DEPLOY	, props.getProperty("deploy.type"));
			servletContext.setAttribute(Constantes.VERSAO_DEPLOY, props.getProperty("deploy.version"));
			servletContext.setAttribute(Constantes.BUILD_DEPLOY	, props.getProperty("build.number"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			logger.error("Erro ao ler arquivo de propriedades de deploy.", e);
		}

		logger.info("Fim da inicializacao Portal Pré-pago");

	}

	protected void finalize() throws Throwable
	{
		try
		{
			pingGPP.interrupt();        // desliga Thread de Ping
		} finally
		{
			super.finalize();
		}
	}


}
