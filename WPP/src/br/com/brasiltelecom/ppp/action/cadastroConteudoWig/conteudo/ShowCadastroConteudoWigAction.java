package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.conteudo;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de cadastro das informacoes de conteudo WIG.
 * 
 * @author Bernardo Vergne Dias
 * Data: 17/04/2007
 */
public class ShowCadastroConteudoWigAction extends ShowActionHibernate
{
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela()
	{
		return "cadastroConteudoWig.vm";
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
	        
	        // Realiza a pesquisa de todos os conteudos existentes na tabela
	        context.put("conteudos", ConteudoWigDAO.findAll(session));
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			context.put("erro", "Erro ao consultar o cadastro de ConteudoWig. <br><br>" + e);
			logger.error("Erro ao consultar o cadastro de ConteudoWig. " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
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
