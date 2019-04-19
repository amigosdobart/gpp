package br.com.brasiltelecom.ppp.action.origemRecarga;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.CanalDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Canal;

/**
 * Cadastra um Canal.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class InclusaoCanal extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CADASTRAR_CANAL;
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
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        Canal canal = new Canal();
	        ShowEdicaoCanal.processarRequest(canal, request, session);
	  
	        request.setAttribute("entidadeCanal",  canal);
	        CanalDAO.incluirCanal(session, canal);
	        
	        session.getTransaction().commit();
	        
	        request.setAttribute("filtroCanal", canal.getIdCanal());
			request.setAttribute("mensagem", "Canal cadastrado com sucesso!");
		}
		catch (NonUniqueObjectException ex)
		{
			registraErro("Possivelmente já existe um registro para o Id de Canal informado.", session, request, ex);
		}
		catch (ConstraintViolationException ex)
		{
			registraErro("Possivelmente já existe um registro para o Id de Canal informado.", session, request, ex);
		}
		catch (Exception ex)
		{
			registraErro(ex.getMessage(), session, request, ex);
		}
		
		return actionMapping.findForward("redirect");
	}
	
	private void registraErro(String msg, Session session, HttpServletRequest request, Exception e)
	{
		request.setAttribute("mensagem", "[erro]Erro ao cadastrar Canal. <br><br>" + msg);
		request.setAttribute("recuperarEstadoCanal", "true");
		request.setAttribute("modoCanal", "inclusao");
		logger.error("Erro ao cadastrar Canal. " + e);	
		if (session != null) 
			session.getTransaction().rollback();
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
 
