package br.com.brasiltelecom.ppp.action.origemRecarga;

import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.SistemaOrigemDAO;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 * Mostra a página de edição de OrigemRecarga.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 01/10/2007
 */
public class ShowEdicaoOrigemRecarga extends ShowActionHibernate
{	
	private String codOperacao = null;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela() 
	{
		return "edicaoOrigemRecarga.vm";
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
	        String canal = request.getParameter("canal");
			String origem = request.getParameter("origem");
			boolean recuperarEstado = Boolean.valueOf(
					request.getParameter("recuperarEstadoOrigem")).booleanValue();
			
			OrigemRecarga origemRecarga = new OrigemRecarga();
			
			if (recuperarEstado) 
				processarRequest(origemRecarga, request, session);
			else
			{
				if (modo.equals("inclusao"))
		        {
		        	origemRecarga.setIdCanal(canal);
		        	origemRecarga.setIndAtivo(new Integer(1));
		        	origemRecarga.setIndDisponivelPortal(new Integer(1));
		        	origemRecarga.setIndModificarDataExp(new Integer(0));
                    origemRecarga.setCategorias(new HashSet());
                    origemRecarga.setSistemasOrigem(new HashSet());
		        }
				else
                {
					origemRecarga =  OrigemRecargaDAO.findById(session, canal, origem);
                    
                    // Busca as collections lazy
                    Iterator it = origemRecarga.getCategorias().iterator();
                    if (it.hasNext())
                        it.next();
                    
                    it = origemRecarga.getSistemasOrigem().iterator();
                    if (it.hasNext())
                        it.next();
                }
			}
            
			context.put("entidadeOrigem", origemRecarga);  
			context.put("classificacoes", OrigemRecargaDAO.findListaClassificacao(session));
	        context.put("modoOrigem", modo);
            context.put("categorias", CategoriaDAO.findAll(session));
            context.put("sistemasOrigem", SistemaOrigemDAO.findAll(session));
	      
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
	public static void processarRequest(OrigemRecarga origemRecarga,
			HttpServletRequest request, Session session) throws Exception
	{
		String canal = request.getParameter("canal");
		String origem = request.getParameter("origem");
		String descricao = request.getParameter("descricao");
		String classificacao = request.getParameter("classificacao");
		String lancamento = request.getParameter("lancamento");
		String ativo = request.getParameter("ativo");
		String modifData = request.getParameter("modifData");
		String dispPortal = request.getParameter("dispPortal");
        String[] sistemas = request.getParameterValues("sistemas");
        String[] categorias = request.getParameterValues("categorias");
        String idCanalItemProcedente = request.getParameter("idCanalItemProcedente");
        String idOrigemItemProcedente = request.getParameter("idOrigemItemProcedente");
        String idCanalItemEmDobro = request.getParameter("idCanalItemEmDobro");
        String idOrigemItemEmDobro = request.getParameter("idOrigemItemEmDobro");
        
		if (canal != null)
			origemRecarga.setIdCanal(canal);
				
		if (origem != null)
			origemRecarga.setIdOrigem(origem);
		
		if (descricao != null)
			origemRecarga.setDesOrigem(descricao);
		
		if (classificacao != null)
			origemRecarga.setIdtClassificacaoRecarga(classificacao);
		
		if (lancamento != null)
			origemRecarga.setTipLancamento(lancamento);
		
		if (ativo != null && !ativo.equals(""))
			origemRecarga.setIndAtivo(new Integer(ativo));
		else
			origemRecarga.setIndAtivo(new Integer(0));
		
		if (modifData != null && !modifData.equals(""))
			origemRecarga.setIndModificarDataExp(new Integer(modifData));
		else
			origemRecarga.setIndModificarDataExp(new Integer(0));
		
		if (dispPortal != null && !dispPortal.equals(""))
			origemRecarga.setIndDisponivelPortal(new Integer(dispPortal));
		else
			origemRecarga.setIndDisponivelPortal(new Integer(0));
        
        origemRecarga.setSistemasOrigem(new HashSet());
        if (sistemas != null && sistemas.length > 0)
            for (int i = 0; i < sistemas.length; i++)
            {
                String id = sistemas[i].trim();
                if (!id.equals(""))
                    origemRecarga.getSistemasOrigem().add(SistemaOrigemDAO.findByIdSistemaOrigem(session, id));
            }
  
        origemRecarga.setCategorias(new HashSet());
        if (categorias != null && categorias.length > 0)
            for (int i = 0; i < categorias.length; i++)
            {
                String id = categorias[i].trim();
                if (!id.equals(""))
                    origemRecarga.getCategorias().add(CategoriaDAO.findByCodigo(session, Integer.parseInt(categorias[i].trim())));
            }
        
        if(!"".equals(idCanalItemProcedente))
        	origemRecarga.setIdCanalItemProcedente(idCanalItemProcedente);
        else
        	origemRecarga.setIdCanalItemProcedente(null);
        
        if(!"".equals(idOrigemItemProcedente))
        	origemRecarga.setIdOrigemItemProcedente(idOrigemItemProcedente);
        else
        	origemRecarga.setIdOrigemItemProcedente(null);
        
        if(!"".equals(idCanalItemEmDobro))
        	origemRecarga.setIdCanalItemEmDobro(idCanalItemEmDobro);
        else
        	origemRecarga.setIdCanalItemEmDobro(null);
        
        if(!"".equals(idOrigemItemEmDobro))
        	origemRecarga.setIdOrigemItemEmDobro(idOrigemItemEmDobro);
        else
        	origemRecarga.setIdOrigemItemEmDobro(null);
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
