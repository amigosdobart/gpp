package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.conteudo;

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
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;

/**
 * @author Bernardo Dias
 * @since 20/04/2007
 * 
 */
public class ShowPesquisaConteudoWigAction extends ShowActionHibernate
{
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela()
	{
		return "conteudoWig.vm";
	}
	
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request,	HttpServletResponse response, Database db) throws Exception
	{
		response.setContentType("text/xml");
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
		ConteudoWig conteudoWig = null;
		Session session = null;
	
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        conteudoWig = ConteudoWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codigo")));
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			logger.error("Erro ao consultar ConteudoWig '" + request.getParameter("codigo") + "' " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
	
		context.put("conteudoWig",conteudoWig);
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
