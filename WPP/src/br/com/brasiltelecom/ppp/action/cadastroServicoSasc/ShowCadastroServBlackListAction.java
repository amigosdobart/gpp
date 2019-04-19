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

public class ShowCadastroServBlackListAction extends ShowAction
{
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "cadastroServBlackList.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_ATUALIZACAO_BLACK_LIST;
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
			
			
			/*// Organiza os servicos por grupo
			Map grupos = new TreeMap();
			Iterator i = todosServicos.iterator();
			
			// Adiciona os servicos nos grupos
			while (i.hasNext())
			{
				SascServico servico = (SascServico) i.next();
				SascGrupo grupo = servico.getGrupo();
				
				Map servicos = (Map)grupos.get(grupo.getNomeGrupo());
				
				// Verifica se o Mapeamento esta nulo e, caso
				// positivo, cria um novo mapeamento para o servico
				if (servicos==null)
				{
					servicos = new HashMap();
					grupos.put(grupo.getNomeGrupo(), servicos);
				}
				
				servicos.put(new Integer(servico.getCodServico()), servico);
			}*/
			
			// Pesquisa os servicos relacionados ao perfil informado
			// para utilizacao no gerenciamento dos servicos da BlackList
			/*context.put("grupos", grupos.values());*/
			context.put("perfil"  , perfil);
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