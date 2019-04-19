package br.com.brasiltelecom.ppp.action.relatorioContabilRecarga;

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
 * Gera o relatório contabil/recargas
 * 
 * @author Alberto Magno
 * @since 01/07/2004
 */
public class ConsultaRelContabilRecargaAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório contabil para recargas");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/recargas realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_RECARG;

				
				String endereco = "../reports/rwservlet?ppp+ContabilRecargas.rdf+"+request.getParameter("DESFORMAT")+"+PERIODO="+request.getParameter("MES")+"/"+request.getParameter("ANO")+"+CNS="+request.getParameter("codigos_nacionais")+"+GRUPOS="+request.getParameter("grupos_de_servicos")+"+CLASSE="+Constantes.CLASSE_SFA_RECARGAS;
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Contabil_Recarga.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
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
