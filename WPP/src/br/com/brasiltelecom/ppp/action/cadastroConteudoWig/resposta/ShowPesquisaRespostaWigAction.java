package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.resposta;

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
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;

/**
 * @author Bernardo Dias
 * @since 19/04/2007
 * 
 */
public class ShowPesquisaRespostaWigAction extends ShowActionHibernate
{
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "respostaWig.vm";
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
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		RespostaWig respostaWig = null;
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        respostaWig = RespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codigo")));
	        session.getTransaction().commit();
	 	}
		catch (Exception e)
		{
			logger.error("Erro ao consultar RespostaWig '" + request.getParameter("codigo") + "' " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
			
		context.put("respostaWig",respostaWig);
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
