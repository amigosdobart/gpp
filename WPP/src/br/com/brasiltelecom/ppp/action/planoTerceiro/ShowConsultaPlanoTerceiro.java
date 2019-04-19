package br.com.brasiltelecom.ppp.action.planoTerceiro;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteTerceiroDAO;

/**
 * Mostra a página de consulta de OrigemCDR.
 * 
 * @author Lucas Andrade
 * Criado em: 01/10/2007
 */
public class ShowConsultaPlanoTerceiro extends ShowActionHibernate
{	
	private String codOperacao = "MENU_PLANO_TERCEIRO";
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "consultaPlanoTerceiro.vm";
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
	        
	        String busca = request.getParameter("busca");
	        
	        context.put("recuperarEstadoOrigem", request.getAttribute("recuperarEstadoOrigem"));
	        context.put("modoOrigem", request.getAttribute("modoOrigem"));
	        context.put("entidadeOrigem", request.getAttribute("entidadeOrigem"));
	        context.put("busca", busca);
        
	        List origens = null;
	        if(busca == null)
	        	origens = null;
	        else if(busca == "")
	        	origens = AssinanteTerceiroDAO.findAll(session);
	        else 
	        	origens = AssinanteTerceiroDAO.findByMsidnPart(session, busca);

	        context.put("origens", origens);
	       
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar os chips funcionais.");
			logger.error("Erro ao consultar os chips funcionais. " + e);	
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

 
