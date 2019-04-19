package br.com.brasiltelecom.ppp.action.ratingServico;

import java.util.Iterator;
import java.util.List;

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
import br.com.brasiltelecom.ppp.dao.PlanoPrecoDAO;
import br.com.brasiltelecom.ppp.dao.RatingServicoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.RatingServico;

/**
 * Cadastra uma RecargaServico.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class InclusaoRatingServico extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CADASTRAR_VINCULACAO_SFA_CDR;
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
	        
	        request.setAttribute("filtroOrigem",  request.getParameter("origem"));
	        
	        RatingServico ratingServico = new RatingServico();
	        ShowEdicaoRatingServico.processarRequest(ratingServico, request, session);
	        List planosPreco = PlanoPrecoDAO.findByCategoria(session, ratingServico.getPlanoPreco().getCategoria());
	        
	        request.setAttribute("entidade",  ratingServico);
	        
	        if (ratingServico.getCodigoServicoSFA() == null)
	        	throw new Exception("O C�digo de Servi�o SFA informado n�o existe.");

	        if (planosPreco.size() == 0)
	        	throw new Exception("N�o existe planos de pre�o para a categoria informada.");
		       
	        // Cria RecargaServico para todos os planos de preco da categoria em questao
	        for (Iterator it = planosPreco.iterator(); it.hasNext();)
	        {
	        	PlanoPreco plano = (PlanoPreco)it.next();
	        	RatingServico ratingServicoFinal = new RatingServico();
	        	ratingServicoFinal.setCodigoServicoSFA(ratingServico.getCodigoServicoSFA());
	        	ratingServicoFinal.setCsp(ratingServico.getCsp());
	        	ratingServicoFinal.setIdtConsumoDados(ratingServico.getIdtConsumoDados());
	        	ratingServicoFinal.setModulacao(ratingServico.getModulacao());
	        	ratingServicoFinal.setTipChamada(ratingServico.getTipChamada());
	        	ratingServicoFinal.setPlanoPreco(plano);
	        	RatingServicoDAO.incluirRecargaServico(session, ratingServicoFinal);
	        }
	        
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Vincula��o de SFA cadastrada com sucesso!");
		}
		catch (NonUniqueObjectException ex)
		{
			registraErro("Possivelmente j� existe uma Vincula��o de SFA para os dados informados.", session, request, ex);
		}
		catch (ConstraintViolationException ex)
		{
			registraErro("Possivelmente j� existe uma Vincula��o de SFA para os dados informados.", session, request, ex);
		}
		catch (Exception ex)
		{
			registraErro(ex.getMessage(), session, request, ex);
		}
		
		return actionMapping.findForward("redirect");
	}
	
	private void registraErro(String msg, Session session, HttpServletRequest request, Exception e)
	{
		request.setAttribute("mensagem", "[erro]Erro ao cadastrar Vincula��o de SFA. <br><br>" + msg);
		request.setAttribute("recuperarEstado", "true");
		logger.error("Erro ao cadastrar Vincula��o de SFA (RatingServico). " + e);	
		if (session != null) 
			session.getTransaction().rollback();
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
 
