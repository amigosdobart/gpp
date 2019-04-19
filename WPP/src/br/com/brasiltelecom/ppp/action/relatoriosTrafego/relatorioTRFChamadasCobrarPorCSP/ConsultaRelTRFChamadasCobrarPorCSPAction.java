package br.com.brasiltelecom.ppp.action.relatoriosTrafego.relatorioTRFChamadasCobrarPorCSP;

import java.text.SimpleDateFormat;
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
import br.com.brasiltelecom.ppp.util.Uteis;

/**
 * Gera o relatório de Trafego de Chamadas a Cobrar por CSP
 * 
 * @author Geraldo Palmeira	
 * @since 07/06/2006
 */
public class ConsultaRelTRFChamadasCobrarPorCSPAction extends ActionPortal {

	SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATA_FORMATO);
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
				throws Exception 
	       {

				ActionForward result = null;
				
				logger.info("Consulta relatório de marketing bonus por promocao e faixa de valores");
	
				this.codOperacao = Constantes.COD_CONSULTAR_REL_CHAMADAS_COBRAR;
				
				String desFormat = request.getParameter("DESFORMAT");
				Date dataInicial = sdf.parse(request.getParameter("dataInicial"));
				Date dataFinal   = sdf.parse(request.getParameter("dataFinal"));
				String cn        = Uteis.arrayParaString(request.getParameterValues("CN"),",");
				String csp       = Uteis.arrayParaString(request.getParameterValues("CSP"),",");
				String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
				String endereco  = null;
							
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=ChamadasCobrarPorCSPDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Trafego.xml+DATAINICIAL="+sdf.format(dataInicial)+"+DATAFINAL="+sdf.format(dataFinal)+"+CN="+cn+"+CSP="+csp+"+CATEGORIA="+categoria;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+TRFChamadasCobrarPorCSP.rdf+"+request.getParameter("DESFORMAT")+"+DATAINICIAL="+sdf.format(dataInicial)+"+DATAFINAL="+sdf.format(dataFinal)+"+CN="+cn+"+CSP="+csp+"+CATEGORIA="+categoria;
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de Trafego de Chamadas à Cobrar por CSP!");
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
