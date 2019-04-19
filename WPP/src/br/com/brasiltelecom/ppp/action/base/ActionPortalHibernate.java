package br.com.brasiltelecom.ppp.action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.dao.HibernateHelper;

/**
 * Classe abstrata que deve ser extendida por todas as classes de ação (que usam o Hibernate).
 * 
 * @author Bernardo Vergne Dias
 * 
 * Criado em: 28/02/2007
 */
public abstract class ActionPortalHibernate extends ActionPortal 
{	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param actionMapping Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 	Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  		Requisição HTTP que originou a execução dessa ação.
	 * @param response		Resposta HTTP a ser encaminhada ao usuário.
	 * @param db			Banco de dados Castor.
	 * @throws <code>java.lang.Exception</code>
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public final ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception 
	{
        return this.performPortal(actionMapping, actionForm, request, response, HibernateHelper.getSessionFactory());
        
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
        {  }
        */
	}
	
	/**
	 * Método abstrato que representa a finalidade do método perform do Struts.
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param session parâmetro do tipo org.hibernate.Session.
	 * @throws java.lang.Exception, 
	 */
	public abstract ActionForward performPortal(
		ActionMapping actionMapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response, SessionFactory sessionFactory) throws Exception;
}