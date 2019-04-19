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
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;

/**
 * 
 * Classe para download de arquivo qualquer do Estorno/Expurgo
 * 
 * Usada atualmente para download de:
 * 
 *  -> Arquivos disponibilizados (entrada)
 *  -> Arquivos processados (saida)
 *  -> Erros (erros na carga dos lotes)
 *  -> Previas (geradas pelo GPP)
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class DownloadArquivoAction extends ActionPortal 
{
	
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método principal da classe, reponsável pelo download de arquivo.
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
			ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,
			Database db)throws Exception 
	{
		 ActionForward result = null;
		 
		try 
		{
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_ESTORNO_EXPURGO_PP));
			String pasta = request.getParameter("pasta");
			String arquivo = request.getParameter("arquivo");
	
			// realiza o download
			GerenciadorArquivo.download(response, raiz + java.io.File.separator + pasta
					+ java.io.File.separator + arquivo);
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "Erro: não foi possível realizar o download.");
			logger.error("Não foi possível realizar o download.");	
			result = actionMapping.findForward("error");
		}
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return null;
	}
}

