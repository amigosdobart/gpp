package br.com.brasiltelecom.ppp.action.consultasLogistica;

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
 * Chama a execucao do relatorio de consulta de cartoes de recarga utilizando a fabrica do portal.
 * 
 * @author Jorge Abreu
 * @since 04/12/2007
 */
public class ShowConsultaCartaoRecargaAction extends ActionPortalHibernate 
{

	private String codOperacao = Constantes.CONSULTA_CARTAO_RECARGA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
	
				ActionForward result = null;
				
				logger.info("Executando a Consulta ao relatório de Cartoes de Recarga.");
	
				String pinInicial = request.getParameter("pinInicial");
				String pinFinal   = request.getParameter("pinFinal");
				
				if(pinFinal == null || pinFinal.equals(""))
					pinFinal = pinInicial;
				
				String endereco = "./relatoriosWPP?NOME_RELATORIO=RelatorioCartaoRecarga+ARQUIVO_PROPRIEDADES=/relatorio/Logistica.xml+PININICIAL="+pinInicial+"+PINFINAL="+pinFinal;
			
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Cartoes de Recarga realizada com sucesso!");
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
