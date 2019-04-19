/*
 * Created on 27/08/2004
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConfiguracaoHome;
import br.com.brasiltelecom.ppp.home.ConsultaUfHome;
import br.com.brasiltelecom.ppp.home.MotivoBloqueioCrmHome;
import br.com.brasiltelecom.ppp.portal.entity.Configuracao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * @author ex352341
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FiltroDesbloqueioAction extends ActionPortal {

	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {

		//this.codOperacao = XXXXXX;
		
		db.begin();
		logger.info("Consulta de tipos de bloqueio solicitada");
		
		/**
		 * @Descrição: Consulta os Motivos de Bloqueio disponíveis;
		 * @return: Motivos de bloqueios cadastrados no banco de dados;
		 */
		Collection resultset;
		
		try
		{
			resultset = MotivoBloqueioCrmHome.findAll(db);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de motivos de bloqueio/desbloqueio, problemas na conexão com o banco de dados (" +
								pe.getMessage() + ")");
			throw pe;
		}

		request.setAttribute(Constantes.RESULT,resultset);
		
		/**
		 * @Descrição: Consulta o Módulo de Operação Atual. 
		 * @return: Valor da configuração de módulo de operação no banco de dados.
		 */
		Configuracao contingencia;
		
		try
		{
			contingencia = ConfiguracaoHome.findByID(db,"CONTINGENCIA_CRM");
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de motivos de bloqueio/desbloqueio, problemas na conexão com o banco de dados (" +
					pe.getMessage() + ")");
			throw pe;
		}
		
		if (!"1".equals(contingencia.getVlrConfiguracao()))
		{
			request.setAttribute(Constantes.MENSAGEM,"O servidor não está em módulo de contingência");
		}
		request.setAttribute("valorContingenciaCrm", contingencia);
		
		
		
		/**
		 * 
		 */
		Collection Uf;
		try
		{
			Uf = ConsultaUfHome.findAll(db);
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta de Uf, problemas na conexão com o banco de dados (" +
					pe.getMessage() + ")");
			throw pe;
		}
		request.setAttribute("Uf", Uf);
		
		return actionMapping.findForward("success");
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}