package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.MotivoContestacaoDAO;

/**
 * Consulta uma dica de Motivo de Contestacao
 * 
 * @author Bernardo Vergne Dias
 * Data: 08/02/2008
 */
public class ShowDicasMotivoAction extends ActionPortalHibernate
{
    Logger logger = Logger.getLogger(this.getClass());

    public ActionForward performPortal(ActionMapping actionMapping, 
            ActionForm actionForm, HttpServletRequest request, 
            HttpServletResponse response, SessionFactory sessionFactory) throws Exception
    {
        response.setContentType("text/plain");
        response.setHeader( "Pragma", "no-cache" );
        response.addHeader( "Cache-Control", "must-revalidate" );
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );
        response.setDateHeader("Expires", 0);
        
        Session session = null;
        
        try 
        {        
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            long idMotivo = Long.parseLong(request.getParameter("idMotivo"));
            String dicas = MotivoContestacaoDAO.findById(session, idMotivo).getDicas();
            dicas = (dicas != null) ? dicas = dicas.trim() : "Não há dica cadastrada para esse motivo.";
            
            response.getWriter().print(dicas);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            response.getWriter().print("Erro ao consulta a dica. " + e.getMessage());
            logger.error("Erro ao realizar a consulta de Motivo de Contestacao. " + e);  
            if (session != null && session.isOpen()) 
                session.getTransaction().rollback();
        }
        
        return null;
    }

    /**
     * @return Nome da operação (permissão) associada a essa ação.
     */
    public String getOperacao() 
    {
        return null;
    }
}