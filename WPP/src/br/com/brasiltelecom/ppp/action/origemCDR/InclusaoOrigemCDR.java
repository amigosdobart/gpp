package br.com.brasiltelecom.ppp.action.origemCDR;

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
import br.com.brasiltelecom.ppp.dao.RatingDao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Rating;

/**
 * Cadastra uma OrigemRecarga.<br>
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class InclusaoOrigemCDR extends ActionPortalHibernate 
{	
	private String codOperacao = Constantes.COD_CADASTRAR_ORIGEM_CDR;
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
	        
	        Rating origemCDR = new Rating();
	        ShowEdicaoOrigemCDR.processarRequest(origemCDR, request, session);
	        
	        request.setAttribute("busca", request.getParameter("busca"));
	        request.setAttribute("entidadeOrigem",  origemCDR);
	        RatingDao.incluirRating(session, origemCDR);
		
	        session.getTransaction().commit();
			request.setAttribute("mensagem", "Origem de CDR cadastrada com sucesso!");
		}
		catch (NonUniqueObjectException ex)
		{
			registraErro("Possivelmente j� existe um registro para o nome de rate informado.", session, request, ex);
		}
		catch (ConstraintViolationException ex)
		{
			registraErro("Possivelmente j� existe um registro para o nome de rate informado.", session, request, ex);
		}
		catch (Exception ex)
		{
			registraErro(ex.getMessage(), session, request, ex);
		}
		
		return actionMapping.findForward("redirect");
	}
	
	private void registraErro(String msg, Session session, HttpServletRequest request, Exception e)
	{
		request.setAttribute("mensagem", "[erro]Erro ao cadastrar Origem de CDR. <br><br>" + msg);
		request.setAttribute("recuperarEstadoOrigem", "true");
		request.setAttribute("modoOrigem", "inclusao");
		logger.error("Erro ao cadastrar Origem de CDR. " + e);	
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
 
