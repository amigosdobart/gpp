package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilPreviaContabil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório de Prévia Contabil
 * 
 * @author Geraldo Palmeira
 * @since 12/12/2006 
 */
public class ConsultaRelContabilPrevia extends ActionPortal {

	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
					
				db.begin();
	
				ActionForward result = null;
				
				logger.info("Consulta por relatório de Prévia Contábil");
	
				this.codOperacao = Constantes.COD_CONSULTA_CONT_PREVIA_CONTABIL;
				
				String periodoContabil = request.getParameter("periodoContabil");
								 
     			String endereco = "../reports/rwservlet?ppp+ContabilPrevia.rdf+PDF+PERIODOCONTABIL="+periodoContabil;
				
				request.setAttribute("endereco", endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório Prévia Contábil realizada com sucesso!");
				result = actionMapping.findForward("redirect");
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}
