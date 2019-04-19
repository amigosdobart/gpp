package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.OrigensEstornoHome;
import br.com.brasiltelecom.ppp.portal.entity.OrigemEstorno;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Remove origem de Estorno/Expurgo de Bonus Pula-Pula.
 * 
 * @author Bernardo Vergne Dias
 * @since 20/12/2006
 */
public class RemoveOrigemEstornoAction extends ActionPortal 
{
	private String codOperacao = Constantes.COD_EXCLUIR_ORIGEM;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * M�todo principal da classe, repons�vel pela remo��o de origem de estorno pula-pula
	 * 
	 * @param actionMapping par�metro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm par�metro do tipo org.apache.struts.action.ActionForm.
	 * @param request  par�metro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response par�metro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db par�metro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception 
	{
		ActionForward result = null;
		
		try 
		{	
			db.begin();
			OrigemEstorno origem = OrigensEstornoHome.findByOrigem(db, request.getParameter("origem"));
			OrigensEstornoHome.excluirOrigem(db, origem);
			db.commit();
			
			request.setAttribute("mensagem", "Origem '"+request.getParameter("origem")+"' removida com sucesso!");
			result = actionMapping.findForward("success");
		}
		catch (Exception e)
		{
			logger.error("Erro ao excluir origem de Estorno/Expurgo PulaPula");	
			request.setAttribute("mensagem", "Erro ao excluir origem de Estorno/Expurgo PulaPula. " +
					"A origem especificada pode estar em uso.");
			result = actionMapping.findForward("error");
			
			db.rollback();
		} 
		finally
		{
			db.close();
		}
		
		return result;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}



