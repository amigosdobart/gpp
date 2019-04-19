package br.com.brasiltelecom.ppp.action.relatorioMKTMudancaPromocao;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.PromocaoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe de filtro do relatorio de mudanca de promocao.
 * 
 * @author Lucas Mind�llo de Andrade
 * @since 07/05/2008
 */
public class ShowFiltroConsultaRelMKTMudancaPromocao extends ShowActionHibernate
{
	private String operacao = "MENU_REL_MKT_MUD_PROMO"; 
	
	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "filtroRelatorioMudancaPromocao.vm";
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, SessionFactory sessionFactory)
			throws Exception 
	{
		Logger logger = Logger.getLogger(ShowFiltroConsultaRelMKTMudancaPromocao.class);
		Session session = null;
	
		try
		{
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			context.put("categorias",CategoriaDAO.findAllOrderedByDesc(session));
			PromocaoCategoria categoria = new PromocaoCategoria();
			categoria.setIdtCategoria(Constantes.ID_CATEGORIA_PULAPULA);
			context.put("promocoes", PromocaoDAO.findAllByCategoria(session, categoria));
			
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			if(session!=null)
			{
				session.getTransaction().rollback();
			}
			logger.error("Erro na consulta das categorias e planos.",e);
			context.put("erro", "Erro na consulta das categorias e planos.");
		}		
	}

	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return operacao;
	}

}
