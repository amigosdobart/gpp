/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioMKTPeriodoRecargas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Gera o relatório de receita por período de recargas
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaRelMKTPeriodoRecargasAction extends ActionPortal {
	
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
				
				logger.info("Consulta por relatório de receita por período de recargas solicitada");
	
				String tipoPeriodo=request.getParameter("tipoPeriodo");
				
				String diaInicial = null;
				String diaFinal = null;
				if (tipoPeriodo != null){
					if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo"))){
						diaFinal = request.getParameter("periodo").substring(3);
						diaInicial = request.getParameter("periodo").substring(0,3);
					} else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("diaInicial")) && !"".equals(request.getParameter("diaFinal"))){
						diaInicial = request.getParameter("diaInicial");
						diaFinal = request.getParameter("diaFinal");
					}
				}
				
					request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de período de recargas realizada com sucesso!");
					this.codOperacao = Constantes.COD_CONSULTA_PERIODO_RECARGAS;

				
					String endereco = "../reports/rwservlet?ppp+MKTPeriodoRecargas.rdf+"+request.getParameter("DESFORMAT")+"+diai="+diaInicial+"+diaf="+diaFinal;
					if (request.getParameter("DESFORMAT").equals("DELIMITED"))
						endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Marketing_Periodo_Recargas.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
								
					request.setAttribute("endereco",endereco);

					result = actionMapping.findForward("redirect");

					//response.sendRedirect("../reports/rwservlet?ppp+MKTPeriodoRecargas.rdf+"+request.getParameter("DESFORMAT")+"+diai="+diaInicial+"+diaf="+diaFinal);
				

				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}
