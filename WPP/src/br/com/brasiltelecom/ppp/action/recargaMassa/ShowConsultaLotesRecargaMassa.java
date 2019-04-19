package br.com.brasiltelecom.ppp.action.recargaMassa;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.Definicoes;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.LoteRecargaMassaDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * Mostra a p�gina de aprova��o de lotes de recarga em massa.
 * 
 * @author Bernardo Vergne Dias
 * Data: 09/08/2007
 */
public class ShowConsultaLotesRecargaMassa extends ShowActionHibernate 
{	
	private String codOperacao = Constantes.MENU_APROVACAO_LT_RECARGA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "filtroAprovacaoLoteRecarga.vm";
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
	        
	        // Realiza a pesquisa de todos os lotes existentes na tabela
	        context.put("lotes", LoteRecargaMassaDAO.findAllGroupByLote(session));
	        context.put("STATUS_PENDENTE", new Integer(Definicoes.STATUS_RECARGA_MASSA_PENDENTE));
	        context.put("STATUS_APROVADO", new Integer(Definicoes.STATUS_RECARGA_MASSA_APROVADO));
	        context.put("STATUS_REJEITADO", new Integer(Definicoes.STATUS_RECARGA_MASSA_REJEITADO));
	        context.put("sdf", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{			
			context.put("mensagem", "[erro]Erro ao consultar os lotes na base de dados.");
			logger.error("Erro ao consultar os lotes.");
			if ((session != null) && session.getTransaction().isActive())
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
