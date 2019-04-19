/*
 * Created on 20/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioBSCliente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * Gera o relatório de BS por cliente
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ConsultaRelBSClienteAction extends ActionPortal {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Logger logger = Logger.getLogger(this.getClass());
	private String codOperacao;
	
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
	
				ActionForward result = null;
				
				db.begin();
				
				logger.info("Consulta por relatório de BS/cliente solicitada");
				
				String tipoPeriodo=request.getParameter("tipoPeriodo");
				
				Date dataInicial = null;
				Date dataFinal = new Date();
				if (tipoPeriodo != null){
					if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo"))){
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
						dataInicial = c.getTime();
					} else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal"))){
						dataInicial = sdf.parse(request.getParameter("dataInicial"));
						dataFinal = sdf.parse(request.getParameter("dataFinal"));
					}
				}

				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de BS por cliente realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTA_BS_CLIENTE;

				//response.sendRedirect("http://"+(String)servlet.getServletContext().getAttribute(Constantes.REPORTS_NOME_SERVIDOR)+":"+(String)servlet.getServletContext().getAttribute(Constantes.REPORTS_PORTA_SERVIDOR)+"/reports/rwservlet?desformat="+request.getParameter("DESFORMAT")+"&report=BSClienteStatus.rdf&destype=cache&userid=pppdev/pppdev@brtdev2&datai="+sdf.format(dataInicial)+"&dataf="+sdf.format(dataFinal)+"&CANAL="+request.getParameter("CANALORIGEM"));
				//response.sendRedirect("../reports/rwservlet?ppp+BSClienteStatus.rdf+"+request.getParameter("DESFORMAT")+"+datai="+sdf.format(dataInicial)+"+dataf="+sdf.format(dataFinal)+"+CANAL="+request.getParameter("CANALORIGEM"));
				
				String endereco = "../reports/rwservlet?ppp+BSClienteStatus.rdf+"+request.getParameter("DESFORMAT")+"+datai="+sdf.format(dataInicial)+"+dataf="+sdf.format(dataFinal)+"+CANAL="+request.getParameter("CANALORIGEM");
				if (request.getParameter("DESFORMAT").equals("DELIMITED"))
					   endereco = "./relatoriosWPP?NOME_RELATORIO=BSClienteStatusDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Atendimento.xml+datai="+sdf.format(dataInicial)+"+dataf="+sdf.format(dataFinal)+"+CANAL="+request.getParameter("CANALORIGEM");

								
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
