package br.com.brasiltelecom.ppp.action.ratingServico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.RatingDao;
import br.com.brasiltelecom.ppp.dao.RatingServicoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Rating;
import com.brt.gpp.comum.mapeamentos.entidade.RatingServico;

/**
 * Mostra a página de consulta de RecargaServico.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class ShowConsultaRatingServico extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_VINCULACAO_SFA_CDR;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "consultaRatingServico.vm";
	}
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        Rating rating = null;
	        String filtroOrigem = null;
	        HashMap ratingServicos = new HashMap();
	        
	        context.put("recuperarEstado", request.getAttribute("recuperarEstado"));
	        context.put("entidade", request.getAttribute("entidade"));
	        
	        filtroOrigem = request.getParameter("filtroOrigem");
	        
	        if (filtroOrigem == null)
	        	filtroOrigem = (String)request.getAttribute("filtroOrigem");

	        if (filtroOrigem != null)
	        {
	        	rating = RatingDao.findByRateName(session, filtroOrigem);
	        	if (rating != null)
	        	{
		        	List list = RatingServicoDAO.findByRate(session, rating);
		        	// Agrupa a lista de RecargaServico pela seguinte chave:
		        	// <sistema de origem, codigo SFA, categoria de plano preco>
		        	// Obs: Permanece no hashmap apenas uma instancia de RecargaServico para cada chave
		        	for (Iterator it = list.iterator(); it.hasNext();)
		        	{
		        		RatingServico ratingServico = (RatingServico)it.next();
		        		ratingServicos.put(
		        				ratingServico.getCodigoServicoSFA().getIdtCategoria() + 
		        				ratingServico.getCsp() +
		        				ratingServico.getIdtConsumoDados() +
		        				ratingServico.getIdtTipoServico() + 
		        				ratingServico.getModulacao().getIdModulacao() +
		        				ratingServico.getPlanoPreco().getCategoria().getIdCategoria() +
		        				ratingServico.getTipChamada().getRateName(), ratingServico);
		        	}
	        	}
	        	else
	        		request.setAttribute("mensagem", "[erro]O tipo de chamada informado não existe.");
	        }
            
	        context.put("filtroOrigem", filtroOrigem);
	        context.put("rating", rating);
	        context.put("ratingServicos", ratingServicos.values());
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar vinculacoes de SFA. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar vinculacoes de SFA (RecargaServico). " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
