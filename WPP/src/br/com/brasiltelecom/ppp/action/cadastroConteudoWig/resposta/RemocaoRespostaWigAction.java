package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.resposta;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Remove uma resposta Wig.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 19/04/2007
 */
public class RemocaoRespostaWigAction extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
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
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        RespostaWigDAO.removerResposta(session, 
	        		RespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codigo"))));
	     
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Resposta WIG removida com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao remover Resposta WIG . <br><br>" + e);
			logger.error("Erro ao remover Resposta WIG . " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
		
		return actionMapping.findForward("success");
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
 
