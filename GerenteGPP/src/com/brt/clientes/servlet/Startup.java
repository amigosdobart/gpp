
package com.brt.clientes.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.app.Velocity;
import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.consultas.action.consultaStatus.ShowConsultaStatusConexoesAction;

/**
 * Inicializa as variáveis de sistema
 * @author Alex Pitacci Simões
 * @since 01/06/2004
 */
public class Startup extends HttpServlet {

	
	/**
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig scfg) throws ServletException {
		
		ServletContext servletContext = scfg.getServletContext();
		PropertyConfigurator.configureAndWatch(servletContext.getRealPath("/WEB-INF/log4j.properties"));
		Logger logger = Logger.getLogger(this.getClass());
		logger.debug("Logger iniciado");

		// Configuração e iniciação do Gerente de ORB's
		logger.info("Inicio Gerente ORB");
		GerenteORB.setServidores(servletContext.getInitParameter("servidoresGPP"));
		GerenteORB.setPeriodoPingGPP(Long.parseLong(servletContext.getInitParameter("periodoPingGPP")));
		GerenteORB.startThread();
		
		// Configuracoes do middleware de SMS
		ShowConsultaStatusConexoesAction.ipMiddlewareSMS = servletContext.getInitParameter("IPMiddlewareSMS"); 
		ShowConsultaStatusConexoesAction.portaMiddlewareSMS = servletContext.getInitParameter("PortaMiddlewareSMS"); 

		// Configuracoes do servidor de socket do GPP
		ShowConsultaStatusConexoesAction.ipServidorSocketGPP =servletContext.getInitParameter("IPSocketServerGPP"); 
		ShowConsultaStatusConexoesAction.portaServidorSocketGPP = servletContext.getInitParameter("PortaSocketServerGPP"); 

		// Configuracoes do servidor Oracle Reports
		ShowConsultaStatusConexoesAction.urlServidorOracleReports = servletContext.getInitParameter("URLServidorOracleReports"); 

		logger.info("Inicio Gerente");

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


		// Pegando tipo e build do Deploy
		// Carga do arquivo de deploy
		logger.info("Carga dos dados de Deploy");
		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream(new File(servletContext.getRealPath("/META-INF/deploy.dat"))));
			servletContext.setAttribute("TIPO_DEPLOY"	, props.getProperty("deploy.type"));
			servletContext.setAttribute("VERSAO_DEPLOY"	, props.getProperty("deploy.version"));
			servletContext.setAttribute("BUILD_DEPLOY"	, props.getProperty("build.number"));		
		}
		catch(IOException e)
		{
			e.printStackTrace();
			logger.error("Erro ao ler arquivo de propriedades de deploy.", e);
		}

	}
}
