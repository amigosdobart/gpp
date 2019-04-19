package br.com.brasiltelecom.ppp.action.consultaRecarga;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.mapeamentos.entidade.Categoria;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.action.grupo.SegurancaGrupo;
import br.com.brasiltelecom.ppp.dao.CanalDAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de recargas/ajustes
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class FiltroConsultaRecargaAction extends ShowActionHibernate 
{
	private String codOperacao = Constantes.MENU_RECARGAS;	
	Logger logger = Logger.getLogger(this.getClass());
	    
	public String getTela() 
	{
		return "filtroConsultaRecarga.vm";
	}

	public String getOperacao() 
	{
		return codOperacao;
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session sessionHibernate = null;
		Set perfis = new HashSet();
		context.put("periodo","0");
		try 
		{
			// Inicia a sessão do hibernate
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
			
			context.put("canais",CanalDAO.findAll(sessionHibernate));
		} 
		catch (Exception e) 
		{
			logger.error("Erro ao consultar canais de origem, problemas na conexão com o banco de dados (" +
					e.getMessage() + ")");
			if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
		}
		
		if (request.getParameter("canalRecarga") != null && !request.getParameter("canalRecarga").equals(""))
		{
			try 
			{
				context.put("origens",OrigemRecargaDAO.findByIdCanal(sessionHibernate, request.getParameter("canalRecarga")));
				context.put("canalRecarga",request.getParameter("canalRecarga"));
				context.put("msisdn",request.getParameter("msisdn"));
				context.put("nsu",request.getParameter("nsu"));
				context.put("tipoPeriodo",request.getParameter("tipoPeriodo"));
				context.put("dataInicial",request.getParameter("dataInicial"));
				context.put("dataFinal",request.getParameter("dataFinal"));
				context.put("periodo",request.getParameter("periodo"));
			}
			catch (Exception e1) 
			{
				logger.error("Não foi possível realizar a consulta de recarga/ajuste, problemas na conexão com o banco de dados (" +
							e1.getMessage() + ")");
				if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
			}
		}
		
		try 
		{
			// Verifica se perfil do usuário permite acesso ao tipo de terminal requisitado (LigMix ou GSM)
			HttpSession session = request.getSession();
			Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
			SegurancaGrupo.setPermissao(login, context);
			Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));			
			
			for (Iterator i = usuario.getCategoriasPermitidas().iterator();i.hasNext();)
			{
				Categoria categoria = (Categoria) i.next();
				
				// Atualiza o objeto categoria pelo Hibernate
				sessionHibernate.refresh(categoria);
				perfis.add(categoria.getDesMascaraMsisdn());
			}
			// Fecha a sessão do Hibernate
			sessionHibernate.close();
		}
		catch(Exception e3)
		{
			logger.error("Erro ao consultar canais de origem, problemas na conexão com o banco de dados (" +
					e3.getMessage() + ")");
			if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
		}
		
		// Atualiza o context com as coleções preenchidas
		context.put("perfis",perfis);
	}
}