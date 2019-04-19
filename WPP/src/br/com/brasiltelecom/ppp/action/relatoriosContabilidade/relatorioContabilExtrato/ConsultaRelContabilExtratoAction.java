package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilExtrato;

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
 * @author Alberto Magno
 * @since 27/05/2004
 */
public class ConsultaRelContabilExtratoAction extends ActionPortal {
	

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
				
				logger.info("Consulta por relatório Contabil/Extrato");
	
				this.codOperacao = Constantes.COD_CONSULTAR_CONTABIL_EXTRATO;

				String desFormat = request.getParameter("DESFORMAT");
				String periodo = request.getParameter("MES") + "/" + request.getParameter("ANO");
				
				String endereco = null;
				
				if (desFormat.equals("DELIMITED")) 
				{
					endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilExtratoDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+PERIODO=" + periodo;
				}
				else
				{
					endereco = "../reports/rwservlet?ppp+ContabilExtrato.rdf+"+desFormat+"+PERIODO="+periodo;
				}
								
				request.setAttribute("endereco",endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório Contabil/Extrato realizada com sucesso!");
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
