package br.com.brasiltelecom.ppp.action.recargaServico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.RecargaServicoDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.RecargaServico;

/**
 * Mostra a página de consulta de RecargaServico.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 02/10/2007
 */
public class ShowConsultaRecargaServico extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_VINCULACAO_SFA;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "consultaRecargaServico.vm";
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
	        
	        OrigemRecarga origemRecarga = null;
	        String filtroOrigem = null;
	        HashMap recargasServico = new HashMap();
	        
	        context.put("recuperarEstado", request.getAttribute("recuperarEstado"));
	        context.put("entidade", request.getAttribute("entidade"));
	        
	        filtroOrigem = request.getParameter("filtroOrigem");
	        
	        if (filtroOrigem == null)
	        	filtroOrigem = (String)request.getAttribute("filtroOrigem");

	        if (filtroOrigem != null)
	        {
	        	origemRecarga = OrigemRecargaDAO.findById(session, 
	        			filtroOrigem.substring(0, 2), filtroOrigem.substring(2, 5));
	        	if (origemRecarga != null)
	        	{
		        	List list = RecargaServicoDAO.findByOrigem(session, origemRecarga);
		        	
		        	// Agrupa a lista de RecargaServico pela seguinte chave:
		        	// <sistema de origem, codigo SFA, categoria de plano preco>
		        	// Obs: Permanece no hashmap apenas uma instancia de RecargaServico para cada chave
		        	for (Iterator it = list.iterator(); it.hasNext();)
		        	{
		        		RecargaServico recargaServico = (RecargaServico)it.next();
		        		String hash = recargaServico.getSistemaOrigem().getIdSistemaOrigem() + "&" +
		        					  recargaServico.getCodigoServicoSFA().getIdtCodigoServicoSFA() + "&" +
		        					  recargaServico.getPlanoPreco().getCategoria().getIdCategoria();
		        		recargasServico.put(hash, recargaServico);
		        	}
	        	}
	        	else
	        		request.setAttribute("mensagem", "[erro]A Origem de Recarga informada não existe.");
	        }
            
            // Verifica se a origem de recarga possui associacoes com Sistemas de Origem e Categorias.
            // Caso nao existir, deve-se bloquear o botao de cadastro de vinculacao SFA.
            // Essas associacoes devem ser previamente feitas na propria tela de Origem de Recarga.
            if (origemRecarga != null)
            {
                if (  (origemRecarga.getCategorias() != null && origemRecarga.getCategorias().size() == 0)
                    ||(origemRecarga.getSistemasOrigem() != null && origemRecarga.getSistemasOrigem().size() == 0))
                {
                    context.put("bloqueiaCadastrar", "true");
                }
            }
	        
	        context.put("filtroOrigem", filtroOrigem);
	        context.put("origemRecarga", origemRecarga);
	        context.put("recargasServico", recargasServico.values());
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar vinculacoes de SFA. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar vinculacoes de SFA (RecargaServico). " + e);	
			if (session != null) 
				session.getTransaction().rollback();
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

 
