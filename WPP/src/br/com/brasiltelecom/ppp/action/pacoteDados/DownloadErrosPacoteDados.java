package br.com.brasiltelecom.ppp.action.pacoteDados;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.SessionFactory;
import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;

/**
 * 
 * Classe para download de arquivo de erros da configuracao de pacote
 * 
 * @author 
 * Data: 
 */
public class DownloadErrosPacoteDados extends ActionPortalHibernate 
{
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
									   ActionForm actionForm,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionFactory sessionFactory) throws Exception 
	{
		 String erros = (String)request.getSession().getAttribute("errosPacoteDados");
		 
		 response.setHeader("Content-type", "text/plain");
		 response.setHeader("Content-length", "" + erros.getBytes().length);
		 response.setHeader("Content-disposition", "attachment; filename=\"ErrosOferta.txt\"");
		 
		 response.getOutputStream().write(erros.getBytes());
		 response.flushBuffer();
		 return null;
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return null;
	}
}