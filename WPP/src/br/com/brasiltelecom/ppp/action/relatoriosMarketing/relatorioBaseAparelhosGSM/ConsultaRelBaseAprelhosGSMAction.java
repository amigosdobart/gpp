/*
 * Created on 21/08/2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioBaseAparelhosGSM;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatorio de base de aparelhos GSM
 * 
 * @author Anderson Jefferson Cerqueira
 * @since 29/02/2008
 */
public class ConsultaRelBaseAprelhosGSMAction extends ActionPortalHibernate 
{
	private String codOperacao = Constantes.COD_CONSULTAR_BASE_APARELHOS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
	
				logger.info("Consulta ao relatório de base de aparelhos GSM");
	
				String endereco = null;
							
				endereco = "./relatoriosWPP?NOME_RELATORIO=BaseAparelhosGSMPrePago+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml";
			
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de base de aparelhos GSM realizada com sucesso!");
		   
				return actionMapping.findForward("redirect");
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
