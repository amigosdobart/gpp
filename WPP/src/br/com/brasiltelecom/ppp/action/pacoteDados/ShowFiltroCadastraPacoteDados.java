package br.com.brasiltelecom.ppp.action.pacoteDados;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.OfertaPacoteDadosDAO;
import br.com.brasiltelecom.ppp.dao.TipoSaldoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsavel por mostrar a tela para Cadastro de
 * Oferta de Pacote de Dados
 * 
 * @author João Paulo Galvagni
 * @since  29/08/2007
 */
public class ShowFiltroCadastraPacoteDados extends ShowActionHibernate
{
	private String codOperacao = Constantes.MENU_CADASTRA_PACOTE_DADOS;
	Logger  logger = Logger.getLogger(this.getClass());
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
			   						  HttpServletRequest request,
			   						  SessionFactory sessionFactory) throws Exception
	{
		Session 	sessionHibernate  	= null;
		
		try
		{
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
			Collection 	listaPacoteDados 	= new ArrayList();
			
			// Realiza a consulta das Ofertas de Pacote de Dados
			listaPacoteDados = OfertaPacoteDadosDAO.findAllPacoteDados(sessionHibernate);
			
			Collection tiposSaldo = new ArrayList();
			
			tiposSaldo.add(TipoSaldoDAO.findByIdTipoSaldo(sessionHibernate, TipoSaldo.DADOS));
			tiposSaldo.add(TipoSaldoDAO.findByIdTipoSaldo(sessionHibernate, TipoSaldo.TORPEDOS));
			
			// Inclui a lista das ofertas no contexto
			context.put("listaPacoteDados", listaPacoteDados);
			context.put("tiposSaldo", tiposSaldo);
			
			sessionHibernate.getTransaction().commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			context.put("erro", "Não foi possível realizar a consulta dos Pacotes de Dados, problemas de conexão com o Banco de Dados. " + e.getMessage());
			logger.error("Não foi possível realizar a consulta dos Pacotes de Dados, problemas de conexão com o Banco de Dados.", e);
			if (sessionHibernate != null && sessionHibernate.isOpen())
				sessionHibernate.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "filtroCadastraPacoteDados.vm";
	}
	
	/**
	 * @return Codigo da Operacao.
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}