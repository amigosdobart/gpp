package br.com.brasiltelecom.ppp.action.ajustaStatusAssinante;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ConfiguracaoAssinanteDAO;

public class ShowAjustaStatusAssinante extends ShowActionHibernate 
{
	public String getTela() 
	{
		return "ajustaStatusAssinante.vm";
	}
	
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, SessionFactory sessionFactory)
			throws Exception 
	{
		Logger logger = Logger.getLogger(ShowAjustaStatusAssinante.class);
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			session.beginTransaction();

			context.put("statusAssinante", ConfiguracaoAssinanteDAO.findByTipConfiguracao(session, "STATUS_ASSINANTE"));
			context.put("statusPeriodico", ConfiguracaoAssinanteDAO.findByTipConfiguracao(session, "STATUS_PERIODICO"));
			context.put("assinante", request.getAttribute("assinante"));
			context.put("msisdn", request.getParameter("msisdn"));
			context.put("mensagemAlerta", request.getAttribute("mensagemAlerta"));
			
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			if(session!=null)
				session.getTransaction().rollback();
			logger.error("Erro ao acessar os tipos de status disponíveis.", e);
		}
	}

	public String getOperacao() 
	{
			return null;
	}
	
}
