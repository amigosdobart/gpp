package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.resposta;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.RespostaWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra o formulario para inclusao/alteracao de resposta Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 19/04/2007
 */
public class ShowFiltroEdicaoRespostaWigAction extends ShowActionHibernate
{	
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "filtroEdicaoRespostaWig.vm";
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
	        
	        context.put("acao", request.getParameter("acao"));
	        
	        if (request.getParameter("cache") != null)
    		{	
	        	// Recupera os dados preenchidos pelo usuario. O objeto é fazer persistencia
	        	// dos campos da VM caso ocorrer erro no submit
	        	
	        	if (!request.getParameter("codigo").equals(""))
	        		context.put("codigo",new Integer(request.getParameter("codigo")));
	        	
	        	context.put("resposta_nome", escape(request.getParameter("nome")));
	        	context.put("resposta_descricao", escape(request.getParameter("descricao")));
    		}
	        else
	        {
	        	if (request.getParameter("acao").equals("alteracao"))
		        {
	        		RespostaWig respostaWig = RespostaWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codigo")));
	 		    	
			        context.put("codigo",new Integer(respostaWig.getCodResposta()));
			        context.put("resposta_nome", escape(respostaWig.getNomeResposta()));
					context.put("resposta_descricao", escape(respostaWig.getDescricaoResposta()));
		    	}
	        }

	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			context.put("erro", "Erro ao consultar os dados de Resposta WIG. <br><br>" + e);
			logger.error("Erro ao consultar os dados de Resposta WIG. " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
	/**
	 * Adapta uma string para impressão em HTML
	 */
	public String escape(String str)
	{
		if (str == null) return null;
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\\\\", "&#092;");
		return str;
	}
}

 
