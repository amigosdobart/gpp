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
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Altera uma resposta Wig.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 19/04/2007
 */
public class AlteracaoRespostaWigAction extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
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
		ActionForward forward = actionMapping.findForward("success");
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	       
	        RespostaWig respostaWig = RespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codigo")));
	
	        respostaWig.setDescricaoResposta(request.getParameter("descricao"));
	        
	        if (request.getParameter("nome").equals(""))
	        	throw new Exception("xiii");
	        
	        respostaWig.setNomeResposta(request.getParameter("nome"));  
			
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Resposta WIG alterada com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao alterar Resposta WIG. <br><br>" + e);
			logger.error("Erro ao alterar Resposta WIG. " + e.getMessage());	
			forward = actionMapping.findForward("error");
			if (session != null) session.getTransaction().rollback();
		}
		
		return forward;
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
 
