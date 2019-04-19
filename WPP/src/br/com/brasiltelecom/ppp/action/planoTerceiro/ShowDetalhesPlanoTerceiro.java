package br.com.brasiltelecom.ppp.action.planoTerceiro;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteTerceiroDAO;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteTerceiro;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a p�gina de consulta de Origem dos CDR (resumo por Ajax).
 * 
 * @author Lucas Mind�llo de Andrade
 * Criado em: 16/04/2008
 */
public class ShowDetalhesPlanoTerceiro extends ShowActionHibernate
{	
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "detalhesPlanoTerceiro.vm";
	}
	
	/**
	 * Cabe�alhos para requisi��o AJAX
	 */
	public ActionForward performPortal(
	        ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response, Database db)throws Exception
	{
		//response.setContentType("text/xml");
		response.setHeader( "Pragma", "no-cache" );
		response.addHeader( "Cache-Control", "must-revalidate" );
		response.addHeader( "Cache-Control", "no-cache" );
		response.addHeader( "Cache-Control", "no-store" );
		response.setDateHeader("Expires", 0);
		return super.performPortal(actionMapping,actionForm,request,response,db);
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	  
	        String msisdn = request.getParameter("msisdn");
	        
	        AssinanteTerceiro assinanteTerceiro = AssinanteTerceiroDAO.findByAssinante(session, msisdn);
	        context.put("assinanteTerceiro", assinanteTerceiro);
	        context.put("datAtualizacao", assinanteTerceiro.getAtualizacao() != null ? new SimpleDateFormat(Constantes.DATA_HORA_FORMATO).format(assinanteTerceiro.getAtualizacao()) : "-");
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar o chip funcional.");
			logger.error("Erro ao consultar o chip funcional. " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
