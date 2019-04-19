package br.com.brasiltelecom.ppp.action.ratingServico;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.CodigoServicoSFADAO;
import br.com.brasiltelecom.ppp.dao.ModulacaoDAO;
import br.com.brasiltelecom.ppp.dao.OperadoraLDDAO;
import br.com.brasiltelecom.ppp.dao.RatingDao;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;
import com.brt.gpp.comum.mapeamentos.entidade.Modulacao;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.Rating;
import com.brt.gpp.comum.mapeamentos.entidade.RatingServico;

/**
 * Mostra a página de edição de RecargaServico.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class ShowEdicaoRatingServico extends ShowActionHibernate
{	
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "edicaoRatingServico.vm";
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
	        
	        String origem = request.getParameter("origem");
			boolean recuperarEstado = Boolean.valueOf(
					request.getParameter("recuperarEstado")).booleanValue();
			
			RatingServico ratingServico = new RatingServico();
			
			if (recuperarEstado) 
				processarRequest(ratingServico, request, session);
			else
			{
				
                Rating rating = RatingDao.findByRateName(
                        session, origem);
                ratingServico.setTipChamada(rating);
			}
				
			context.put("origem", origem);  
			context.put("entidade", ratingServico);  
			context.put("modulacoes", ModulacaoDAO.findAll(session));
			context.put("categorias", CategoriaDAO.findAllOrderedByDesc(session));
			context.put("operadoras", OperadoraLDDAO.findAllOrderedById(session));
			
	        session.getTransaction().commit();
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
	public static void processarRequest(RatingServico ratingServico,
			HttpServletRequest request, Session session) throws Exception
	{
		String origem = request.getParameter("origem");
		String codSFA = request.getParameter("codSFA");
		String mod = request.getParameter("modulacao");
		String categoria = request.getParameter("categoria");
		String csp = request.getParameter("csp");
		String consumoDados = request.getParameter("consumoDados"); 
		
		ratingServico.setIdtConsumoDados(consumoDados);
		ratingServico.setCsp(csp);

		if (origem != null)
		{
			Rating rating = RatingDao.findByRateName(session, origem);
			ratingServico.setTipChamada(rating);
		}
				
		if (mod != null && mod.length() == 1)
		{
			Modulacao modulacao = ModulacaoDAO.findById(session, mod);
			ratingServico.setModulacao(modulacao);
		}
					
		if (codSFA != null && !codSFA.equals(""))
		{
			CodigoServicoSFA servicoSFA = CodigoServicoSFADAO.findById(session, Integer.parseInt(codSFA));
			ratingServico.setCodigoServicoSFA(servicoSFA);
		}
		
		if (categoria != null && !categoria.equals(""))
		{
			Categoria cat =  CategoriaDAO.findByCodigo(session, Integer.parseInt(categoria));
			PlanoPreco plano = new PlanoPreco();
			plano.setCategoria(cat);
			ratingServico.setPlanoPreco(plano);
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

 
