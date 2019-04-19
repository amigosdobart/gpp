package br.com.brasiltelecom.ppp.action.origemRecarga;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CanalDAO;

import com.brt.gpp.comum.mapeamentos.entidade.Canal;

/**
 * Mostra a página de edição de Canal.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class ShowEdicaoCanal extends ShowActionHibernate
{	
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "edicaoCanal.vm";
	}
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        String modo = request.getParameter("modoCanal");
	        String id = request.getParameter("id");
			boolean recuperarEstado = Boolean.valueOf(
					request.getParameter("recuperarEstadoCanal")).booleanValue();
			
			Canal canal = new Canal();
			
			if (recuperarEstado) 
				processarRequest(canal, request, session);
			else
			{
				if (modo.equals("alteracao"))
					canal =  CanalDAO.findByIdCanal(session, id);
			}
				
			context.put("entidadeCanal", canal);  
	        context.put("modoCanal", modo);
	      
	        session.getTransaction().rollback();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao abrir formulário de edição. <br><br>" + e.getMessage());
			logger.error("Erro ao abrir formulário de edição. " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * Transfere os parâmetros do request (caso existam) para uma entidade. 
	 */
	public static void processarRequest(Canal canal,
			HttpServletRequest request, Session session) throws Exception
	{
		String id = request.getParameter("id");
		String desc = request.getParameter("desc");

		if (id != null)
			canal.setIdCanal(id);

		if (desc != null)
			canal.setDescCanal(desc);
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
