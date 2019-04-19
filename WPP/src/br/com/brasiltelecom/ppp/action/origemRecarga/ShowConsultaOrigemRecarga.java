package br.com.brasiltelecom.ppp.action.origemRecarga;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CanalDAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Canal;

/**
 * Mostra a p�gina de consulta de OrigemRecarga.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class ShowConsultaOrigemRecarga extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_ORIGEM_RECARGA;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "consultaOrigemRecarga.vm";
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
	        
	        context.put("recuperarEstadoOrigem", request.getAttribute("recuperarEstadoOrigem"));
	        context.put("modoOrigem", request.getAttribute("modoOrigem"));
	        context.put("entidadeOrigem", request.getAttribute("entidadeOrigem"));

	        context.put("recuperarEstadoCanal", request.getAttribute("recuperarEstadoCanal"));
	        context.put("modoCanal", request.getAttribute("modoCanal"));
	        context.put("entidadeCanal", request.getAttribute("entidadeCanal"));
	        
	        List canais = null;
	        List origens = null;
	        String filtroCanal = null;
	        
	        canais = CanalDAO.findAll(session);
	        
	        filtroCanal = request.getParameter("filtroCanal");
	        
	        if (filtroCanal == null)
	        	filtroCanal = (String)request.getAttribute("filtroCanal");
	        
	        if (filtroCanal == null)
	        	filtroCanal = (canais.size() > 0) ? ((Canal)canais.get(0)).getIdCanal() : null;
	        
	        if (filtroCanal != null)
	        	origens = OrigemRecargaDAO.findByIdCanal(session, filtroCanal);
	        
	        context.put("filtroCanal", filtroCanal);
	        context.put("canais", canais);
	        context.put("origens", origens);
	       
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar origens de recarga. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar origens de recarga. " + e);	
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

 
