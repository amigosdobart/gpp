package br.com.brasiltelecom.ppp.action.cadastroServicoSasc;

import java.util.Collection;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.SascServicoHome;
import br.com.brasiltelecom.ppp.portal.entity.SascPerfil;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class ShowCadastroNovoServSascAction extends ShowAction
{
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "cadastroNovoServicoSasc.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CADASTRAR_NOVO_SERV_SASC;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		try
		{
			db.begin();
			SascPerfil perfil = SascServicoHome.findPerfilById(db, Integer.parseInt(request.getParameter("codPerfil")), true);
			Collection todosServicos = new TreeSet();
			todosServicos.addAll(perfil.getListaDeServicos());
			perfil.setListaDeServicos(todosServicos);
			
			Collection grupos = SascServicoHome.findAllGrupos(db);
			
			context.put("grupos", grupos);
			context.put("perfil", perfil);
			db.commit();
		}
		catch(Exception e)
		{
			if (db.isActive())
				db.rollback();
		}
		finally
		{
			if (!db.isClosed())
				db.close();
		}
	}
}