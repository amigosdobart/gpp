package br.com.brasiltelecom.ppp.action.relatorioTrafegoCSP14;


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
 * Gera o relatório de tráfego do CSP 14
 * 
 * @author Marcos C. Magalhaes
 * @since 03/10/2005
 */
public class ConsultaRelTrafegoCSP14Action extends ActionPortal 
{
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
				
				logger.info("Consulta por relatório de trafego CSP 14");

				this.codOperacao = Constantes.COD_CONSULTAR_REL_TRAF_CSP_14;
				String mes = request.getParameter("MES");
				String ano = request.getParameter("ANO");
				String periodo = mes + "/" + ano;  
				String desFormat = request.getParameter("DESFORMAT");
				String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
				String cn 		 = Uteis.arrayParaString(request.getParameterValues("CN"),",");
								
				String endereco = null;
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=TrafegoCSP14Delimited+ARQUIVO_PROPRIEDADES=/relatorio/Trafego.xml+PERIODO="+periodo+"+CN="+cn+"+CATEGORIA="+categoria;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+Perfil_trafego_diario_CSP_14.rdf+"+desFormat+"+PERIODO="+periodo+"+CN="+cn+"+categoria="+categoria;
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de trafego CSP 14 realizada com sucesso!");
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
