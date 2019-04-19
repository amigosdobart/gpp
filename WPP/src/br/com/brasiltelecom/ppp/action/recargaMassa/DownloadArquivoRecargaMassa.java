package br.com.brasiltelecom.ppp.action.recargaMassa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.GerenciadorArquivo;

/**
 * 
 * Classe para download de arquivo qualquer da pasta de recarga em massa
 * 
 * Usada atualmente para download de:
 * 
 *  -> Arquivos disponibilizados (entrada)
 *  -> Arquivos processados (saida)
 *  -> Erros (erros na carga dos lotes)
 * 
 * @author Bernardo Vergne Dias
 * Data: 09/08/2007
 */
public class DownloadArquivoRecargaMassa extends ActionPortalHibernate 
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
			String raiz = (String)(servlet.getServletContext().getAttribute(Constantes.PATH_RECARGA_MASSA));
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
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return null;
	}
}

