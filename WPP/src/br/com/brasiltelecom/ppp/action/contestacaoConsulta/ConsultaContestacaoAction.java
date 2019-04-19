package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;

/**
 * DEPRECATED
 */
public class ConsultaContestacaoAction extends ActionPortal 
{
    public ActionForward performPortal(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, Database db) throws Exception
    {
        // Backward compatibility =D (Bernardo Dias)
        return actionMapping.findForward("success");
    }

    public String getOperacao()
    {
        return null;
    }
}