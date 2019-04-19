package br.com.brasiltelecom.ppp.action.relatorioContingenciaCRM;

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
 * Gera o relatório CONTINGENCIA CRM
 * 
 * @author Alberto Magno
 * @since 20/09/2004
 */
public class ConsultaRelContingenciaCRMAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório Contingencia CRM");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório Contingencia CRM realizada com sucesso!");
				
				this.codOperacao = Constantes.COD_CONSULTAR_CONTINGENCIA_CRM;

				String msisdn = request.getParameter("msisdn").equals("")?"":"55"+request.getParameter("msisdn");
				
				String endereco = "../reports/rwservlet?ppp+ContingenciaCRM.rdf+"+request.getParameter("DESFORMAT")+"" +
						"+DATAINICIAL="+request.getParameter("dataInicial")+"+DATAFINAL="+request.getParameter("dataFinal")+
								"+MOTIVOBLOQUEIO="+request.getParameter("motivoBloqueio")+
								"+MSISDN="+msisdn+
								"+PLANO="+request.getParameter("plano")+
								"+ATENDENTE="+request.getParameter("atendente");
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_ContingenciaCRM.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
				request.setAttribute("endereco",endereco);
				result = actionMapping.findForward("redirect");
				
					//response.sendRedirect("../reports/rwservlet?ppp+FINAjstCarryOver.rdf+"+request.getParameter("DESFORMAT")+"+MES="+request.getParameter("MES")+"+ANO="+request.getParameter("ANO"));
				
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}
