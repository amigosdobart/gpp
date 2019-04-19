/*
 * Created on 29/10/2004
 */
package br.com.brasiltelecom.ppp.action.consultaInformacoesAssinante;

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
 * @author Henrique Canto
 * Window - Preferences - Java - Code Style - Code Templates
 * Alterado por Marcos Magalh�es em 09/03/2006 para restringir acesso por perfil
 */
public class ShowFiltroConsultaInformacoesAssinante extends ShowActionHibernate 
{
	Logger logger = Logger.getLogger(this.getClass());
	public String getTela() 
	{
		return "filtroConsultaInformacoesAssinante.vm";
	}
	
	public String getOperacao() 
	{
		return null;
	}
	
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session sessionHibernate = null;
		Set perfis = new HashSet();
		
		// Verifica se perfil do usu�rio permite acesso ao tipo de terminal requisitado (LigMix ou GSM)
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);
		Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));			

		try 
		{
			// Inicia a sess�o do hibernate
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
			
			for (Iterator i = usuario.getCategoriasPermitidas().iterator();i.hasNext();)
			{				
				Categoria categoria = (Categoria) i.next();
				
				// Atualiza o objeto categoria pelo Hibernate
				sessionHibernate.refresh(categoria);
				perfis.add(categoria.getDesMascaraMsisdn());
			}
			// Fecha a sess�o do Hibernate
			sessionHibernate.close();
		}
		
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar Informa��es de Assinante. <br><br>" + e);
			logger.error("Erro ao consultar Informa��es de Assinante. " + e.getMessage());	
			if (sessionHibernate != null) sessionHibernate.getTransaction().rollback();
		}
		
		// Atualiza o context com as cole��es preenchidas
		context.put("perfis",perfis);
	}
}
