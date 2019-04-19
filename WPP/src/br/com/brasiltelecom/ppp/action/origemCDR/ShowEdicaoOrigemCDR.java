package br.com.brasiltelecom.ppp.action.origemCDR;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.RatingDao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Rating;

/**
 * Mostra a página de edição de Origem de CDR.
 * 
 * @author Lucas Andrade
 * Criado em: 16/04/2008
 */
public class ShowEdicaoOrigemCDR extends ShowActionHibernate
{	
	private String codOperacao = null;
	private static DecimalFormat format = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(new Locale("pt", "BR")));
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "edicaoOrigemCDR.vm";
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
	        
	        String modo = request.getParameter("modoOrigem");
	        String origem = request.getParameter("origem");
			String descricao = request.getParameter("descricao");
			String tarifavel = request.getParameter("tarifavel");
			String tipoRate = request.getParameter("tipoRate");
			String fatorConversao = request.getParameter("fatorConversao");
			boolean recuperarEstado = Boolean.valueOf(
					request.getParameter("recuperarEstadoOrigem")).booleanValue();
			/*
	       
	        */
			Rating origemCDR = new Rating();
			
			if (recuperarEstado) 
				processarRequest(origemCDR, request, session);
			else
			{
				if (modo.equals("inclusao"))
		        {
					origemCDR.setDesOperacao(descricao);
					origemCDR.setIdtTarifavel(tarifavel);
					origemCDR.setRateName(origem);
					origemCDR.setTipRate(tipoRate);
					if(fatorConversao!=null)
						origemCDR.setVlrFatorConversao(new BigDecimal(fatorConversao));
		        }
				else
                {
					origemCDR = RatingDao.findByRateName(session, origem);
                }
			}
			/*
	        
	        */
			context.put("formatter", new DecimalFormat(Constantes.DOUBLE_FORMATO, new DecimalFormatSymbols(new Locale("pt", "BR", ""))));
			context.put("entidadeOrigem", origemCDR);  
	        context.put("modoOrigem", modo);
	        context.put("busca", request.getParameter("busca"));
            context.put("categorias", CategoriaDAO.findAll(session));
	      
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
	public static void processarRequest(Rating origemCDR,
			HttpServletRequest request, Session session) throws Exception
	{
		String origem = request.getParameter("origem");
		String descricao = request.getParameter("descricao");
		String tarifavel = request.getParameter("tarifavel");
		String tipoRate = request.getParameter("tipoRate");
		String fatorConversao = request.getParameter("fatorConversao");
		String idCanalItemProcedente = request.getParameter("idCanalItemProcedente");
	    String idOrigemItemProcedente = request.getParameter("idOrigemItemProcedente");
	    String idCanalItemEmDobro = request.getParameter("idCanalItemEmDobro");
	    String idOrigemItemEmDobro = request.getParameter("idOrigemItemEmDobro");
	    
        if (origem!=null)
        	origemCDR.setRateName(origem);
        
        if (descricao!=null)
        	origemCDR.setDesOperacao(descricao);
        
        if (tarifavel != null)
        	origemCDR.setIdtTarifavel(tarifavel);
        
        if (tipoRate != null)
        	origemCDR.setTipRate(tipoRate);
        
        if (fatorConversao != null)
        	origemCDR.setVlrFatorConversao(new BigDecimal(format.parse(fatorConversao).doubleValue()));
        
        if(!"".equals(idCanalItemProcedente))
        	origemCDR.setIdCanalItemProcedente(idCanalItemProcedente);
        else
        	origemCDR.setIdCanalItemProcedente(null);
        
        if(!"".equals(idOrigemItemProcedente))
        	origemCDR.setIdOrigemItemProcedente(idOrigemItemProcedente);
        else
        	origemCDR.setIdOrigemItemProcedente(null);
        
        if(!"".equals(idCanalItemEmDobro))
        	origemCDR.setIdCanalItemEmDobro(idCanalItemEmDobro);
        else
        	origemCDR.setIdCanalItemEmDobro(null);
        
        if(!"".equals(idOrigemItemEmDobro))
        	origemCDR.setIdOrigemItemEmDobro(idOrigemItemEmDobro);
        else
        	origemCDR.setIdOrigemItemEmDobro(null);
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
