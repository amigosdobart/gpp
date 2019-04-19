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
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisição HTTP que originou a execução dessa ação.
	 * @param response			Resposta HTTP a ser encaminhada ao usuário.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
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
			logger.error("Não foi possível realizar o upload.");	
			result = actionMapping.findForward("error");
		} 
		
		return result;
		
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return null;//codOperacao;
	}
}
