package br.com.brasiltelecom.ppp.action.ajustaStatusAssinante;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;

public class ShowFiltroAjustaStatusAssinante extends ShowActionHibernate 
{
	public String getTela() 
	{
		return "filtroAjustaStatusAssinante.vm";
	}
	
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, SessionFactory sessionFactory)
			throws Exception 
	{
			
	}

	public String getOperacao() 
	{
			return null;
	}
}
