package br.com.brasiltelecom.ppp.action.origemRecarga;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 * Altera uma OrigemRecarga.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class AlteracaoOrigemRecarga extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_ALTERAR_ORIGEM_RECARGA;
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
	        
	        String canal = request.getParameter("canal");
			String origem = request.getParameter("origem");
			
	        OrigemRecarga origemRecarga = OrigemRecargaDAO.findById(session, canal, origem);
	        ShowEdicaoOrigemRecarga.processarRequest(origemRecarga, request, session);
            
	        request.setAttribute("entidadeOrigem", origemRecarga);
	        request.setAttribute("filtroCanal", canal);
	        
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Origem de Recarga alterada com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao alterar Origem de Recarga. <br><br>" + e);
			request.setAttribute("recuperarEstadoOrigem", "true");
			request.setAttribute("modoOrigem", "alteracao");
			logger.error("Erro ao alterar Origem de Recarga. " + e.getMessage());
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
 
