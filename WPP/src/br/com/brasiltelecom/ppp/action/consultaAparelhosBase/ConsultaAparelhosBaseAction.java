package br.com.brasiltelecom.ppp.action.consultaAparelhosBase;

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
 * @since 12/08/2005
 */
public class ConsultaAparelhosBaseAction extends ActionPortal 
{
	private String codOperacao = Constantes.COD_CONSULTAR_BASE_APARELHOS;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{					
		logger.info("Consulta de Relatorio de Base de Aparelhos");

		// Monta o endereço e passa os parâmetros do relatório delimitado de base de aparelhos
		String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTBaseAparelhosDelimited+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml+DUMMY="+Constantes.GPP_RET_OPERACAO_OK; // Constante DUMMY, ocorre erro se nao passar nenhum parametro 
		
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
