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
	 * Implementa a l�gica de neg�cio dessa a��o.
	 *
	 * @param actionMapping 	Instancia de <code>org.apache.struts.action.ActionMapping</code>.
	 * @param actionForm 		Instancia de <code>org.apache.struts.action.ActionForm</code>.
	 * @param request  			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param response			Resposta HTTP a ser encaminhada ao usu�rio.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
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
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return null;
	}
}