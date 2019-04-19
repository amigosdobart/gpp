package br.com.brasiltelecom.ppp.action.relatorioAdesaoOfertaNatal;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.PromocaoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta do relatorio de Adesao a Oferta de Natal por Msisdn.
 * 
 * @author Jorge Abreu
 * @since 15/12/2007
 */

public class ShowFiltroAdesaoOfertaNatalMsisdnAction extends ShowActionHibernate 
{	    
	
	private String codOperacao = Constantes.MENU_ADES_OFERTA_NATAL_M;	
	Logger logger = Logger.getLogger(this.getClass());
	    
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaAdesaoOfertaNatalMsisdn.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception 
	{
		
		Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        
        Collection promo = PromocaoDAO.findAll(session);
 
		context.put("resultado", promo);
		
		session.close();
		
	}		

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}