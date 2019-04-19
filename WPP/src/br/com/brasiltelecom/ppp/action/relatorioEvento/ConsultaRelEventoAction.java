package br.com.brasiltelecom.ppp.action.relatorioEvento;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
 * Gera o relatório Contabil/Ajuste
 * 
 * @author Luciano Vilela
 * @since 29/09/2004
 */
public class ConsultaRelEventoAction extends ActionPortal {
	

	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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
				
				logger.info("Consulta por relatório Eventos");
				Date dataInicial = null;
				Date dataFinal = new Date();
				String tipoPeriodo=request.getParameter("tipoPeriodo");

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
	
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Eventos realizada com sucesso!");
				//this.codOperacao = "MENU_REL_EVENTO";

				
				String endereco = null;
				if("sintetico".equalsIgnoreCase(request.getParameter("opcao"))){
						endereco = "../reports/rwservlet?ppp+EstatisticaEventos.rdf+"+request.getParameter("DESFORMAT")+"+datai="+sdf.format(dataInicial)+"+dataf="+sdf.format(dataFinal)+"+TIPO="+request.getParameter("TIPO");
						if (request.getParameter("DESFORMAT").equals("DELIMITED"))
							endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Evento_Sintetico.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
				} else if ("analitico".equalsIgnoreCase(request.getParameter("opcao"))){
						endereco = "../reports/rwservlet?ppp+EstatisticaEventosRegiao.rdf+"+request.getParameter("DESFORMAT")+"+datai="+sdf.format(dataInicial)+"+dataf="+sdf.format(dataFinal)+"+TIPO="+request.getParameter("TIPO");
						if (request.getParameter("DESFORMAT").equals("DELIMITED"))
							endereco = "./rep2excel?relatorioXSL=./dtd/Relatorio_Evento_Analitico.xsl&relatorioXML="+endereco.replaceFirst("DELIMITED","XML");
				}
				

								
				request.setAttribute("endereco",endereco);
				result = actionMapping.findForward("redirect");

				return result;
			}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return "MENU_REL_EVENTO";
	}
}
