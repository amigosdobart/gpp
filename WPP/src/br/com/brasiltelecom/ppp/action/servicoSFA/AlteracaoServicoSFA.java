package br.com.brasiltelecom.ppp.action.servicoSFA;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.CodigoServicoSFADAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;

/**
 * Altera um ServicoSFA.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class AlteracaoServicoSFA extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_ALTERAR_SERVICO_SFA;
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
	        
	        String codSFA = request.getParameter("codSFA");
						
	        CodigoServicoSFA servicoSFA = CodigoServicoSFADAO.findById(session, Integer.parseInt(codSFA));
	        ShowEdicaoServicoSFA.processarRequest(servicoSFA, request, session);
				
	        request.setAttribute("entidade", servicoSFA);
	        request.setAttribute("filtroCodSFA", codSFA);
	        
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Origem de Recarga alterada com sucesso!");
		}
		catch (Exception e)
		{
			request.setAttribute("mensagem", "[erro]Erro ao alterar Serviço SFA. <br><br>" + e);
			request.setAttribute("recuperarEstado", "true");
			request.setAttribute("modo", "alteracao");
			logger.error("Erro ao alterar Serviço SFA. " + e.getMessage());
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
 
