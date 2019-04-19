package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilErrosRecargaControle;

import java.text.ParseException;

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
 * Classe responsavel por realizar a chamada da fábrica de
 * relatórios para gerar o relatório
 * 
 * @author Bernardo Pina
 * @since  06/06/2007
 */
public class ConsultaContabilErrosRecControleAction extends ActionPortal
{
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
	
	/**
	 * @throws ParseException 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		ActionForward result = null;
		
		logger.info("Consulta relatorio contábil de erros do plano controle");
		
		// Seta a operacao
		this.codOperacao = Constantes.COD_CONSULTA_ERR_REC_CONTROLE;
		
		String endereco 		= "";
		
		String dtInicio 		= request.getParameter("dtInicio");
		String dtFinal 			= request.getParameter("dtFinal");
		String categoria		= request.getParameter("controle") + request.getParameter("controleTotal");
		
		endereco = "./relatoriosWPP?NOME_RELATORIO=ContabilErrosPlanoControle+ARQUIVO_PROPRIEDADES=/relatorio/Contabilidade.xml+DTINICIO="+dtInicio+"+DTFIM="+dtFinal+"+CATEGORIA="+categoria;
		
		request.setAttribute(Constantes.MENSAGEM, "Relatório Contábil de erros do plano Controle gerado com sucesso!");
		
		request.setAttribute("endereco",endereco);
		
		result = actionMapping.findForward("redirect");
		
		return result;
	}
}