
package br.com.brasiltelecom.ppp.action.consultaHistoricoSMS;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.TipoSMSDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Bernardo Vergne Dias
 * Data: 10-12-2007
 */
public class ShowFiltroConsultaHistoricoSMS extends ShowActionHibernate 
{
	private String codOperacao = Constantes.MENU_CON_HISTORICO_SMS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "filtroConsultaHistoricoSMS.vm";
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
           
            context.put("tipos", TipoSMSDAO.findAll(session));
            
            session.getTransaction().commit();      
        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao consultar tipos de SMS. " + e.getMessage());
            logger.error("Erro ao consultar tipos de SMS. " + e);  
            if (session != null && session.getTransaction() != null) 
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
