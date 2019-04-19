package br.com.brasiltelecom.ppp.action.origemRecarga;

import java.util.Iterator;

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
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 * Mostra a página de consulta de OrigemRecarga (resumo por Ajax).
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class ShowDetalhesOrigemRecarga extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_ORIGEM_RECARGA;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "detalhesOrigemRecarga.vm";
	}
	
	/**
	 * Cabeçalhos para requisição AJAX
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
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	  
	        String canal = request.getParameter("canal");
	        String origem = request.getParameter("origem");
	        
	        OrigemRecarga origemRecarga = OrigemRecargaDAO.findById(session, canal, origem);
	        context.put("origemRecarga", origemRecarga);
            
            // Busca as collections lazy
            Iterator it = origemRecarga.getCategorias().iterator();
            if (it.hasNext())
                it.next();
            
            it = origemRecarga.getSistemasOrigem().iterator();
            if (it.hasNext())
                it.next();
	       
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar origem de recarga. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar origem de recarga. " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
