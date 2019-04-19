package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

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
 * Dispara o processo batch GPP para carga dos arquivos 
 * de lotes e processamento de previa de estorno/expurgo pula pula
 * 
 * @author Bernardo Vergne Dias
 * @since 03/01/2007
 */
public class SolicitaProcessamentoBatchAction extends ActionPortal 
{
	private String codOperacao = null;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private static Thread threadCargaLotes 		= null;
	private static BatchCargaLotes batchCargaLotes = null;
	
	private static Thread threadPrevia 	= null;
	private static BatchPrevia batchPrevia = null;
	
	private String servGPP 				= null;
	private String portGPP 				= null;
	private String raizEstornoExpurgo 	= null;
	
	/**
	 * Método principal da classe
	 * 
	 * @param actionMapping parâmetro do tipo org.apache.struts.action.ActionMapping.
	 * @param actionForm parâmetro do tipo org.apache.struts.action.ActionForm.
	 * @param request  parâmetro do tipo javax.servlet.http.HttpServletRequest.
	 * @param response parâmetro do tipo javax.servlet.http.HttpServletResponse.
	 * @param db parâmetro do tipo org.exolab.castor.jdo.Database.
	 * @throws java.lang.Exception, 
	 * @see br.com.brasiltelecom.action.base.ActionPortal#performPortal(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse, Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception 
	{
		
		ActionForward result = new ActionForward("/filtroAprovacaoPrevia.do");
		boolean procCompleto = (request.getParameter("completo") != null && request.getParameter("completo").equals("true"));
	
		if (procCompleto) result = new ActionForward("/showArquivosEstornoExpurgoPP.do"); 

		// Inicializa variaveis comuns
		
		servGPP = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR));
		portGPP = (String)(servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR));
		raizEstornoExpurgo = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_ESTORNO_EXPURGO_PP));
		
		// Inicializa o objeto da thread CargaLotes e Previa
		
		batchCargaLotes = new BatchCargaLotes(servGPP, portGPP, raizEstornoExpurgo, logger);
		batchPrevia = new BatchPrevia(servGPP, portGPP, logger);
		
		// Se alguma thread estiver em execução o action apenas retorna
		// um aviso para o usuario. 
		
		boolean cargaEmAndamento = (threadCargaLotes == null) ? false : threadCargaLotes.isAlive();
		boolean previaEmAndamento = (threadPrevia == null) ? false : threadPrevia.isAlive();
		
		if (cargaEmAndamento || previaEmAndamento)
		{
			request.setAttribute("mensagem", "O processamento está em endamento. Aguarde o resultado.");	
			return result;
		}
		
		// Verifica se o usuario solicitou o processamento completo (carga de lotes + previa)
		// ou somente a previa de estorno
		
		if (procCompleto)
		{
			// A thread de carga de lotes executa a previa automaticamente
			threadCargaLotes = new Thread(batchCargaLotes);
			threadCargaLotes.start();
		}
		else
		{
			// Executa apenas a thread de previa
			threadPrevia = new Thread(batchPrevia);
			threadPrevia.start();
		}
		
		request.setAttribute("mensagem", "O processamento foi iniciado com sucesso. " +
						"O resultado será disponibilizado em breve. Aguarde.");

		return result;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
