package br.com.brasiltelecom.ppp.action.recargaServico;

import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.CodigoServicoSFADAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.SistemaOrigemDAO;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.RecargaServico;
import com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem;

/**
 * Mostra a página de edição de RecargaServico.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class ShowEdicaoRecargaServico extends ShowActionHibernate
{	
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "edicaoRecargaServico.vm";
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
			
			RecargaServico recargaServico = new RecargaServico();
			
			if (recuperarEstado) 
				processarRequest(recargaServico, request, session);
			else
			{
                OrigemRecarga origemRecarga = OrigemRecargaDAO.findById(
                        session, origem.substring(0,2), origem.substring(2,5));
                recargaServico.setOrigem(origemRecarga);
			}
            
            // Busca as collections lazy
            Iterator it = recargaServico.getOrigem().getCategorias().iterator();
            if (it.hasNext())
                it.next();
            
            it = recargaServico.getOrigem().getSistemasOrigem().iterator();
            if (it.hasNext())
                it.next();
				
			context.put("origem", origem);  
			context.put("entidade", recargaServico);  
			context.put("categorias", recargaServico.getOrigem().getCategorias());
			context.put("sistemas", recargaServico.getOrigem().getSistemasOrigem());
			
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
	public static void processarRequest(RecargaServico recargaServico,
			HttpServletRequest request, Session session) throws Exception
	{
		String origem = request.getParameter("origem");
		String sistema = request.getParameter("sistema");
		String codSFA = request.getParameter("codSFA");
		String categoria = request.getParameter("categoria");
		
		if (origem != null && origem.length() == 5)
		{
			OrigemRecarga origemRecarga = OrigemRecargaDAO.findById(
					session, origem.substring(0,2), origem.substring(2,5));
			recargaServico.setOrigem(origemRecarga);
		}
				
		if (sistema != null)
		{
			SistemaOrigem sis = SistemaOrigemDAO.findByIdSistemaOrigem(session, sistema);
			recargaServico.setSistemaOrigem(sis);
		}
					
		if (codSFA != null && !codSFA.equals(""))
		{
			CodigoServicoSFA servicoSFA = CodigoServicoSFADAO.findById(session, Integer.parseInt(codSFA));
			recargaServico.setCodigoServicoSFA(servicoSFA);
		}
		
		if (categoria != null && !categoria.equals(""))
		{
			Categoria cat =  CategoriaDAO.findByCodigo(session, Integer.parseInt(categoria));
			PlanoPreco plano = new PlanoPreco();
			plano.setCategoria(cat);
			recargaServico.setPlanoPreco(plano);
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

 
