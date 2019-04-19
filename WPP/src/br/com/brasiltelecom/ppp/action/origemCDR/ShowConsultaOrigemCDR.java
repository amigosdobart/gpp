package br.com.brasiltelecom.ppp.action.origemCDR;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.RatingDao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a p�gina de consulta de OrigemCDR.
 * 
 * @author Lucas Andrade
 * Criado em: 01/10/2007
 */
public class ShowConsultaOrigemCDR extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_ORIGEM_CDR;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "consultaOrigemCDR.vm";
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        String busca = request.getParameter("busca");
	        
	        context.put("recuperarEstadoOrigem", request.getAttribute("recuperarEstadoOrigem"));
	        context.put("modoOrigem", request.getAttribute("modoOrigem"));
	        context.put("entidadeOrigem", request.getAttribute("entidadeOrigem"));
	        context.put("busca", busca);
        
	        List origens = null;
	        if(busca == null)
	        	origens = null;
	        else if(busca == "")
	        	origens = RatingDao.findAll(session);
	        else 
	        	origens = RatingDao.findByRateNamePart(session, busca);

	        context.put("origens", origens);
	       
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar os tipos de chamada. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar os tipos de chamada. " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
