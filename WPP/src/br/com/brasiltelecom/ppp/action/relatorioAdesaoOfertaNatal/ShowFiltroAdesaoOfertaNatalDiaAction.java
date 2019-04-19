package br.com.brasiltelecom.ppp.action.relatorioAdesaoOfertaNatal;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ServicoAssinanteDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta do relatorio de Adesao a Oferta de Natal por Dia.
 * 
 * @author Jorge Abreu
 * @since 15/12/2007
 */
public class ShowFiltroAdesaoOfertaNatalDiaAction extends ShowActionHibernate 
{	    

	private String codOperacao = Constantes.MENU_ADESAO_OFERTA_NATAL;	
	Logger logger = Logger.getLogger(this.getClass());
	    
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaAdesaoOfertaNatalDia.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception 
	{
		Session session = null;
		
		try
		{
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			Collection colecao = new ArrayList();
			colecao = ServicoAssinanteDAO.findAll(session);
	        
			context.put("resultado", colecao);
		
			session.close();
		}
		catch(Exception e)
		{
			if(session != null)
				session.close();
			logger.info("Erro ao inicializar filtro de consulta para o relatorio de Adesao da Oferta de Natal.");
		}
	}		

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}