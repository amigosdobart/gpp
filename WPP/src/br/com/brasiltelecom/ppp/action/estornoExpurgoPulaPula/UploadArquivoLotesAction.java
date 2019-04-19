package br.com.brasiltelecom.ppp.action.estornoExpurgoPulaPula;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;
import br.com.brasiltelecom.ppp.util.MultipartParser;

/**
 * 
 * Classe para upload de arquivo de lotes do Estorno/Expurgo
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class UploadArquivoLotesAction extends ActionPortal 
{
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Método principal da classe, reponsável pelo upload de arquivo.
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
		ActionForward result = null;
		
		try 
		{	
			HashMap params = MultipartParser.parseStream(request);
			
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_ESTORNO_EXPURGO_PP));
			String pasta = ((FileItem)(params.get("pasta"))).getString();
			String destino = raiz + java.io.File.separator + pasta;
			
			GerenciadorArquivo.upload(request, (FileItem)params.get("arquivo"), destino, null);

			result = actionMapping.findForward("success"); 
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar o upload.");	
			result = actionMapping.findForward("error");
		} 
		
		return result;
		
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return null;//codOperacao;
	}
}
