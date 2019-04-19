package br.com.brasiltelecom.ppp.action.planoTerceiro;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteTerceiroDAO;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteTerceiro;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Altera uma OrigemRecarga.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class AlteracaoPlanoTerceiro extends ActionPortalHibernate 
{	
	private String codOperacao = "ALTERAR_PLANO_TERCEIRO";
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
	        
	        request.setAttribute("busca", request.getParameter("busca"));
			String msisdn = request.getParameter("msisdn");
			String remover = request.getParameter("remover");
			
			if(msisdn!=null)
			{
				AssinanteTerceiro assinanteTerceiro = AssinanteTerceiroDAO.findByAssinante(session, msisdn);
				if(remover != null)
				{
					session.delete(assinanteTerceiro);
					request.setAttribute("entidadeOrigem", assinanteTerceiro);
			        
					request.setAttribute("mensagem", "Chip funcional removido com sucesso!");
				}
				else
				{
			        ShowEdicaoPlanoTerceiro.processarRequest(assinanteTerceiro, request, session);
			        Usuario usuario = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);
			        assinanteTerceiro.setOperador(usuario.getMatricula());
			        assinanteTerceiro.setAtualizacao(new Date());
		            
			        request.setAttribute("entidadeOrigem", assinanteTerceiro);
			        
					request.setAttribute("mensagem", "Chip funcional alterado com sucesso!");
				}
			}
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao alterar chip funcional. <br><br>" + e);
			request.setAttribute("recuperarEstadoOrigem", "true");
			request.setAttribute("modoOrigem", "alteracao");
			logger.error("Erro ao alterar chip funcional. " + e.getMessage());
			if (session != null) 
				session.getTransaction().rollback();
		}
		
		return actionMapping.findForward("redirect");
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
 
