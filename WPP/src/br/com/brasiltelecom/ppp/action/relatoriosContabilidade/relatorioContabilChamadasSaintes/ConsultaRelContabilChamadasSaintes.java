package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilChamadasSaintes;
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
 * Gera o relatório de Chamadas Saintes
 * 
 * @author Diego Luitgards
 * @since 16/10/2006
 */
public class ConsultaRelContabilChamadasSaintes extends ActionPortal {

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
				
				logger.info("Consulta por relatório de Chamadas Saintes");
	
				this.codOperacao = Constantes.COD_CONSULTA_CONT_CHAMADAS_SAINTES;
				
				String periodoContabil = request.getParameter("periodoContabil");
				String categoria = Uteis.arrayParaString(request.getParameterValues("CATEGORIA"),",");
				
     			String endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilChamadasSaintesDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+PERIODO_CONTABIL="+periodoContabil+"+CATEGORIA="+categoria;
				
				request.setAttribute("endereco", endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/Chamadas Saintes realizada com sucesso!");
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
