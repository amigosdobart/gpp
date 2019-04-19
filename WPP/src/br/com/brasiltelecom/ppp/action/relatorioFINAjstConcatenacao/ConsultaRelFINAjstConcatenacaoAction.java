/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioFINAjstConcatenacao;


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
 * Gera o relatório de ajuste Concatenação
 * 
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ConsultaRelFINAjstConcatenacaoAction extends ActionPortal {

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
				
				logger.info("Consulta por relatório de ajuste Concatenação");
	
				
				String endereco = "../reports/rwservlet?ppp+FINAjstConcatenacao.rdf+"+request.getParameter("DESFORMAT")+"+MES="+request.getParameter("MES")+"+ANO="+request.getParameter("ANO");
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Financeiro_Ajuste_Concatenacao.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");

								
				request.setAttribute("endereco",endereco);
				result = actionMapping.findForward("redirect");

				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de ajuste Concatenação realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_AJUS_CONCATEN;
							
				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return this.codOperacao;
	}
}
