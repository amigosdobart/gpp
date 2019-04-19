package br.com.brasiltelecom.ppp.action.relatorioMKTPacoteDados;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.OfertaPacoteDadosDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de Filtro do Relatorio das 
 * Ofertas de Pacote de Dados
 * 
 * @author Joao Paulo Galvagni
 * @since  17/09/2007
 */
public class ShowFiltroConsultaRelMKTPacoteDadosAction extends ShowActionHibernate 
{
	private String codOperacao = Constantes.MENU_MKT_PCT_DADOS;
	private Logger logger = Logger.getLogger(this.getClass());
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelMKTPacoteDados.vm";
	}
	
	/**
	 * Implementa a logica de negocio dessa acao.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisicao HTTP que originou a execucao dessa acao.
	 * @param sessionFactory	Factory de sessoes para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
									  HttpServletRequest request,
									  SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        Date dataInicial = sdf.parse(request.getParameter("dataInicial"));
			Date dataFinal 	 = sdf.parse(request.getParameter("dataFinal"));
			
			if (dataInicial != null && dataFinal != null)
			{
				Collection ofertas = OfertaPacoteDadosDAO.findByPeriodoCadastro(session, dataInicial, dataFinal);
				
				if (ofertas != null && ofertas.size() > 0)
					context.put("ofertas", ofertas);
			}
			
			session.getTransaction().commit();	
		}
		catch (Exception e)
		{
			context.put("erro", "Não foi possível realizar a consulta pelos dados de filtros, problemas na conexão com o banco de dados. " + e.getMessage());
			logger.error("Não foi possível realizar a consulta pelos dados de filtros, problemas na conexão com o banco de dados. " + e);	
			
			if (session != null && session.isOpen()) 
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