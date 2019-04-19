/*
 * Created on 19/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de recargas/ajustes
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class FiltroRecargaAction extends ShowActionHibernate 
{	    
	private String codOperacao = "MENU_REC_CARTAO";	
	Logger logger = Logger.getLogger(this.getClass());
	    
	public String getTela() 
	{
		return "filtroRecarga.vm";
	}
	
	public String getOperacao() 
	{
		return codOperacao;
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session sessionHibernate = null;
		Set perfis = new HashSet();
		
		// Verifica se perfil do usuário permite acesso ao tipo de terminal requisitado (LigMix ou GSM)
    	HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);
		Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));			

		try 
		{
			// Inicia a sessão do hibernate
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
			
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
		
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar Informações de Assinante. <br><br>" + e);
			logger.error("Erro ao consultar Informações de Assinante. " + e.getMessage());	
			if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
		}
		
		// Atualiza o context com as coleções preenchidas
		context.put("perfis",perfis);
	}
}