package br.com.brasiltelecom.ppp.action.base.fabricaRelatorios;

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
 * Classe para download de arquivo gerado pela Fábrica de Relatórios.
 * 
 * @author Bernardo Vergne Dias
 * @since 04/12/2006
 */
public class DownloadFabricaRelatoriosAction extends ActionPortal 
{
	protected ParametrosFabricaRelatorios parametros;
	
	private String url;
	
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

		// recupera os dados da sessao (parametros do relatorio)
		ActionForward result = null;
		parametros = (ParametrosFabricaRelatorios)(request.getSession().getAttribute("parametrosFabricaRelatorios"));
		url = (String)(request.getSession().getAttribute("urlOperacao"));
		
		try 
		{
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_FABRICA_REL));
			String pasta = request.getParameter("pasta");
			String arquivo = request.getParameter("arquivo");
	
			// realiza o download
			GerenciadorArquivo.download(response, raiz + java.io.File.separator + url + 
		             java.io.File.separator + pasta + java.io.File.separator + arquivo);
		}
		catch (Exception e)
		{
			logger.error("nao foi possível ler o arquivo especificado");	
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

