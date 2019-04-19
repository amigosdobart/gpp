package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTPromocaoXstatus;

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
 * Gera o relatório de Base de Aparelhos
 * 
 * @author Marcos Castelo Magalhães
 * @since 17/11/2005
 */
public class ConsultaRelMKTPromocaoXstatusAction extends ActionPortal
{
	private String codOperacao = Constantes.COD_CONSULTAR_PROM_X_STATUS;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{					
		logger.info("Consulta de relatorio de promocao versus status");

		
//		 Monta o endereço e passa os parâmetros do relatório de promocao versus status
		String endereco = "../reports/rwservlet?ppp+MKTPromocaoXstatus.rdf+"
						+ request.getParameter("DESFORMAT");
		
		// Se for delimitado
		if(request.getParameter("DESFORMAT").equals("DELIMITED"))
		endereco = "./relatoriosWPP?NOME_RELATORIO=MKTPromocaoXstatusDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DUMMY="+Constantes.GPP_RET_OPERACAO_OK; // Constante DUMMY, ocorre erro se nao passar nenhum parametro 
		
		request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório gerencial realizada com sucesso!");
		request.setAttribute("endereco",endereco);
		
		return actionMapping.findForward("redirect");
	}
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
}
