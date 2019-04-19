/*
 * Created on 14/02/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.relatorioTrafegoPorCSP;

import java.text.SimpleDateFormat;
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
import br.com.brasiltelecom.ppp.util.Uteis;

/**
 * Gera o relatório de tráfego por CSP
 * 
 * @author Daniel Ferreira
 * @since 14/02/2005
 */
public class ConsultaRelTrafegoPorCSPAction extends ActionPortal 
{

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal
	(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception {
					
				db.begin();
	
				ActionForward result = null;
				
				logger.info("Consulta por relatório de trafego por CSP");
	
				String desFormat = request.getParameter("DESFORMAT");
				Date dataInicial = sdf.parse(request.getParameter("dataInicial"));
				Date dataFinal 	 = sdf.parse(request.getParameter("dataFinal"));
				String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
				String cn 		 = Uteis.arrayParaString(request.getParameterValues("CN"),",");
				String endereco = null;
							
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=TrafegoPorCSPDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Trafego.xml+DATAINICIAL="+sdf.format(dataInicial)+"+DATAFINAL="+sdf.format(dataFinal)+"+CN="+cn+"+CATEGORIA="+categoria;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+Perfil_trafego_diario_CSP.rdf+"+request.getParameter("DESFORMAT")+"+DATAINICIAL="+sdf.format(dataInicial)+"+DATAFINAL="+sdf.format(dataFinal)+"+CN="+cn+"+categoria="+categoria;
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de trafego CSP 14 realizada com sucesso!");
				result = actionMapping.findForward("redirect"); 
		   
		 		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de trafego por CSP realizada com sucesso!");
				this.codOperacao = Constantes.COD_CONSULTAR_REL_TRAFEGO_CSP;
				
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
