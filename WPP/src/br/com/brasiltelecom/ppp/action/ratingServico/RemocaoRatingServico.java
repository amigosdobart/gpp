package br.com.brasiltelecom.ppp.action.ratingServico;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import br.com.brasiltelecom.ppp.action.base.ActionPortalHibernate;
import br.com.brasiltelecom.ppp.dao.RatingServicoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.RatingServico;

/**
 * Remove uma RecargaServico.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class RemocaoRatingServico extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_REMOVER_VINCULACAO_SFA_CDR;
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

	        request.setAttribute("filtroOrigem", request.getParameter("origem"));
	        
	        RatingServico ratingServico = new RatingServico();
	        ShowEdicaoRatingServico.processarRequest(ratingServico, request, session);
	        
	        List list = RatingServicoDAO.findByCaracteristicas(session, ratingServico.getCsp(), ratingServico.getModulacao(),
	        		ratingServico.getTipChamada(), ratingServico.getPlanoPreco().getCategoria(), ratingServico.getCodigoServicoSFA());
		       
	        // Remove todas as instancias de RecargaServico para a categoria em questao
	        for (Iterator it = list.iterator(); it.hasNext();)
	        	RatingServicoDAO.removerRecargaServico(session, (RatingServico)it.next());
	        	       	        
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Vinculação de SFA removida com sucesso!");
		}
		catch (ConstraintViolationException ex)
		{
			registraErro("Possivelmente o registro está em uso.", session, request, ex);
		}
		catch (Exception ex2)
		{
			registraErro("", session, request, ex2);
		}
		
		return actionMapping.findForward("redirect");
	}
	
	private void registraErro(String msg, Session session, HttpServletRequest request, Exception e)
	{
		request.setAttribute("mensagem", "[erro]Erro ao remover Vinculação de SFA. <br>" + msg + "<br><br>" + e);
		logger.error("Erro ao remover Vinculacao de SFA (RecargasServico). " + e.getMessage());	
		if (session != null && session.isOpen()) 
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
 
