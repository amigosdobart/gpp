package br.com.brasiltelecom.ppp.action.planoTerceiro;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.PlanoTerceiroDAO;
import br.com.brasiltelecom.ppp.dao.AssinanteTerceiroDAO;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteTerceiro;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a página de edição de Origem de CDR.
 * 
 * @author Lucas Andrade
 * Criado em: 16/04/2008
 */
public class ShowEdicaoPlanoTerceiro extends ShowActionHibernate
{	
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "edicaoPlanoTerceiro.vm";
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
	        
	        String modo = request.getParameter("modoPlanoTerceiro");
			String msisdn = request.getParameter("msisdn");
			String planoTerceiro = request.getParameter("planoTerceiro");
			boolean recuperarEstado = Boolean.valueOf(
					request.getParameter("recuperarEstadoOrigem")).booleanValue();
			
			AssinanteTerceiro assinanteTerceiro = new AssinanteTerceiro();
			
			if (recuperarEstado)
			{
				processarRequest(assinanteTerceiro, request, session);
				if (modo.equals("inclusao"))
		        {
					assinanteTerceiro.setAtualizacao(new Date());
					Usuario usuario = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);
					assinanteTerceiro.setOperador(usuario.getMatricula());
		        }
			}
			else
			{
				if (modo.equals("inclusao"))
		        {
					assinanteTerceiro.setMsisdn(msisdn);
					if(planoTerceiro!=null)
						assinanteTerceiro.setPlano(PlanoTerceiroDAO.findByAssinante(session, planoTerceiro));
					assinanteTerceiro.setAtualizacao(new Date());
					Usuario usuario = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);
					assinanteTerceiro.setOperador(usuario.getMatricula());
		        }
				else
                {
					assinanteTerceiro = AssinanteTerceiroDAO.findByAssinante(session, msisdn);
                }
			}
            
			context.put("assinanteTerceiro", assinanteTerceiro);  
			context.put("datAtualizacao", assinanteTerceiro.getAtualizacao() != null ? new SimpleDateFormat(Constantes.DATA_HORA_FORMATO).format(assinanteTerceiro.getAtualizacao()) : "-");
	        context.put("modoPlanoTerceiro", modo);
	        context.put("busca", request.getParameter("busca"));
            context.put("tipos", PlanoTerceiroDAO.findAll(session));
	      
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
	public static void processarRequest(AssinanteTerceiro assinanteTerceiro,
			HttpServletRequest request, Session session) throws Exception
	{
		String msisdn = request.getParameter("msisdn");
		String planoTerceiro = request.getParameter("planoTerceiro");
        
        if (msisdn!=null)
        	assinanteTerceiro.setMsisdn(msisdn);
        
        if (planoTerceiro != null)
        	assinanteTerceiro.setPlano(PlanoTerceiroDAO.findByAssinante(session, planoTerceiro));
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
