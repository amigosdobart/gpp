package br.com.brasiltelecom.ppp.action.cadastroConteudoWig.conteudo;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ConteudoWigDAO;
import br.com.brasiltelecom.ppp.dao.RespostaWigDAO;
import br.com.brasiltelecom.ppp.dao.ServicoWigDAO;
import br.com.brasiltelecom.ppp.dao.TipoRespostaWigDAO;
import br.com.brasiltelecom.ppp.portal.entity.ConteudoWig;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra o formulario para inclusao/alteracao de conteúdo Wig.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 20/04/2007
 */
public class ShowFiltroEdicaoConteudoWigAction extends ShowActionHibernate
{	
	private String codOperacao = Constantes.COD_CONSULTAR_ANEXO_RESPOSTA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "filtroEdicaoConteudoWig.vm";
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
	        context.put("tiposResposta", TipoRespostaWigDAO.findAll(session));
	        context.put("servicos", ServicoWigDAO.findAll(session));
	        context.put("respostas", RespostaWigDAO.findAll(session));
	        	        
	        if (request.getParameter("cache") != null)
    		{	
	        	// Recupera os dados preenchidos pelo usuario. O objeto é fazer persistencia
	        	// dos campos da VM caso ocorrer erro no submit
	        	
	        	if (!request.getParameter("codigo").equals(""))
	        		context.put("codigo",new Integer(request.getParameter("codigo")));
	        	context.put("conteudo_descricao", escape(request.getParameter("descricao")));
	        	if (!request.getParameter("tipo_resposta").equals(""))
	        		context.put("tipo_resposta", new Integer(request.getParameter("tipo_resposta")));
	        	if (!request.getParameter("servico").equals(""))
	        		context.put("servico", new Integer(request.getParameter("servico")));
	        	if (!request.getParameter("billing").equals(""))
	        		context.put("billing", new Long(request.getParameter("billing")));
	        	if (request.getParameter("menu_opcoes") != null && !request.getParameter("menu_opcoes").equals(""))
	        		context.put("menu_opcoes", new Boolean(request.getParameter("menu_opcoes")));
	        	if (request.getParameter("registra_acesso") != null && !request.getParameter("registra_acesso").equals(""))
	        		context.put("registra_acesso",new Boolean(request.getParameter("registra_acesso")));
	        	if (!request.getParameter("resposta_cod").equals(""))
	        		context.put("resposta_cod", new Integer(request.getParameter("resposta_cod")));
	        	context.put("resposta_descricao", escape(request.getParameter("resposta_des")));
    		}
	        else
	        {
	        	if (request.getParameter("acao").equals("alteracao"))
		        {
			        ConteudoWig conteudoWig = ConteudoWigDAO.findByCodigo(session, Integer.parseInt(request.getParameter("codigo")));
			        
			        context.put("codigo",new Integer(conteudoWig.getCodConteudo()));
			        context.put("conteudo_descricao", escape(conteudoWig.getDescricaoConteudo()));
			        context.put("tipo_resposta", new Integer(conteudoWig.getTipoResposta().getCodTipoResposta()));
		        	context.put("servico", new Integer(conteudoWig.getServico().getCodServicoWig()));
		        	context.put("billing", new Long(conteudoWig.getBillingCode()));
		        	context.put("menu_opcoes", new Boolean(conteudoWig.isMenuOpcoes()));
		        	context.put("registra_acesso", new Boolean(conteudoWig.isRegistraAcesso()));
		        	context.put("resposta_cod", new Integer(conteudoWig.getResposta().getCodResposta()));
			        context.put("resposta_descricao", escape(conteudoWig.getResposta().getDescricaoResposta()));
		    	}
	        }
	        	    	    	
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			context.put("erro", "Erro ao consultar os dados de Conteudo WIG. <br><br>" + e);
			logger.error("Erro ao consultar os dados de Conteudo WIG. " + e.getMessage());	
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

 
