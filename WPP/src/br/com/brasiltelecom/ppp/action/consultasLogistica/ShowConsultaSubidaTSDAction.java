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
 * Chama a execucao do relatorio de consulta de subida de tsd utilizando a fabrica do portal.
 * 
 * @author Jorge Abreu
 * @since 04/12/2007
 */
public class ShowConsultaSubidaTSDAction extends ActionPortalHibernate 
{

	private String codOperacao = Constantes.CONSULTA_SUBIDA_TSD;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
	
				ActionForward result = null;
				
				logger.info("Executando a Consulta ao relatório de Subida de TSD.");
	
				String msisdn = request.getParameter("msisdn");
				String imei   = request.getParameter("imei");
				String iccid  = request.getParameter("iccid");
				
				if(msisdn == null || msisdn.equals(""))
					msisdn = "%";
				else
				{
					if(msisdn.length() == 10)
						msisdn = "55" + msisdn;
				}
				
				if(imei == null || imei.equals(""))
					imei = "%";
				
				if(iccid == null || iccid.equals(""))
					iccid = "%";
				
				String endereco = "./relatoriosWPP?NOME_RELATORIO=RelatorioSubidaTSD+ARQUIVO_PROPRIEDADES=/relatorio/Logistica.xml+MSISDN="+msisdn+"+IMEI="+imei+"+ICCID="+iccid;
			
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Subida de TSD realizada com sucesso!");
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