package br.com.brasiltelecom.ppp.action.recargaMassa;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;
import br.com.brasiltelecom.ppp.util.MultipartParser;

/**
 * 
 * Classe para upload de arquivo de lotes do recarga em massa
 * 
 * @author Bernardo Vergne Dias
 * Data: 09/08/2007
 */
public class UploadArquivoRecargaMassa extends ActionPortalHibernate 
{
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param response			Resposta HTTP a ser encaminhada ao usu�rio.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, SessionFactory sessionFactory) throws Exception 
	{
		ActionForward result = null;
		
		try 
		{	
			HashMap params = MultipartParser.parseStream(request);
			
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_RECARGA_MASSA));
			String pasta = ((FileItem)(params.get("pasta"))).getString();
			String destino = raiz + java.io.File.separator + pasta;
			
			GerenciadorArquivo.upload(request, (FileItem)params.get("arquivo"), destino, null);

			result = actionMapping.findForward("success"); 
		}
		catch (Exception e)
		{
			logger.error("N�o foi poss�vel realizar o upload.");	
			result = actionMapping.findForward("error");
		} 
		
		return result;
		
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return null;//codOperacao;
	}
}
