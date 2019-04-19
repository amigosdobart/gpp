package br.com.brasiltelecom.ppp.action.relatorioContabilSaldo;

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
 * Gera o relatório de ajuste Carry Over
 * 
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ConsultaRelContabilSaldoAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório contabil/saldos");
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/saldos realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_SALDO;

				
				String endereco = "../reports/rwservlet?ppp+ContabilSaldos.rdf+"+request.getParameter("DESFORMAT")+"+PERIODO="+request.getParameter("MES")+"/"+request.getParameter("ANO")+"+CNS="+request.getParameter("codigos_nacionais")+"+GRUPOS="+request.getParameter("grupos_de_servicos")+"+CLASSE="+Constantes.CLASSE_SFA_SALDOS;
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Contabil_Saldo.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
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
