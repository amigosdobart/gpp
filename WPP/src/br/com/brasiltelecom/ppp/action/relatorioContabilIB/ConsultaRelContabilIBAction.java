package br.com.brasiltelecom.ppp.action.relatorioContabilIB;

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
public class ConsultaRelContabilIBAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório contabil Indice Bonificacao");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/Indice Bonificacao realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_IB;

				
				String endereco = "../reports/rwservlet?ppp+ContabilIB.rdf+"+request.getParameter("DESFORMAT")+"+PERIODO="+request.getParameter("MES")+"/"+request.getParameter("ANO")+"+CNS="+request.getParameter("codigos_nacionais")+"+PREHIBRIDO="+request.getParameter("prehibrido");
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Contabil_IB.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
				request.setAttribute("endereco",endereco);
				result = actionMapping.findForward("redirect");
				
				
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}
