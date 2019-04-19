package br.com.brasiltelecom.ppp.action.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.dao.HibernateHelper;

/**
 * Classe abstrata que deve ser extendida por todas as classes responsáveis pela escolha da tela de apresentação.
 * 
 * @author Bernardo Vergne Dias
 * 
 * Criado em: 28/02/2007
 */
public abstract class ShowActionHibernate extends ShowAction
{	
	/**
     * Método abstrato que representa o corpo de todas as Classes que extendem a Classe ShowAction.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	  parâmetro do tipo Database do Castor.
	 */
	public final void updateVelocityContext(VelocityContext context,
				HttpServletRequest request, Database db) throws Exception
	{
		this.updateVelocityContext(context, request, HibernateHelper.getSessionFactory());
        
        /*
        try
        {
            SessionFactory fac = HibernateHelper.getSessionFactory();
            if (fac != null)
            {
                Session session = fac.getCurrentSession();
                if (session != null && session.isOpen())
                    session.close();
            }
        }
        catch (Exception ignored)
        { }
        */
	}
	
	/**
     * Método abstrato que representa o corpo de todas as Classes que extendem a Classe ShowAction.
     * Inclui acesso ao Hibernate.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param session parâmetro do tipo Sessão do Hibernate.
	 */
	public abstract void updateVelocityContext(VelocityContext context,
				HttpServletRequest request, SessionFactory sessionFactory) throws Exception;
}