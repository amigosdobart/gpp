package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilAssinantesRecarregaram;
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
 * Gera o relatório de Códigos de Serviços Pré-Pago
 * 
 * @author Geraldo Palmeira
 * @since 18/10/2006
 */
public class ConsultaRelContabilAssinantesRecarregaram extends ActionPortal {

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
				
				logger.info("Consulta por relatório de Assinantes que Recarregaram");
	
				this.codOperacao = Constantes.COD_CONSULTA_CONT_ASS_RECARREGARAM;
				
				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");
				String cn = request.getParameter("codigosNacionais");
     			 		
     			String endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilAssinantesRecarregaramDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+CN="+cn+"+DATAINICIAL="+dataInicial+"+DATAFINAL="+dataFinal;
				
				request.setAttribute("endereco", endereco);
				request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório contabil/consumo realizada com sucesso!");
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
