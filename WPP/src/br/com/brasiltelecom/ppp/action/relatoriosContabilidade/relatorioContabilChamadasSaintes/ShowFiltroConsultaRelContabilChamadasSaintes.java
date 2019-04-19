package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilChamadasSaintes;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

import org.apache.log4j.Logger;


/**
 * Mostra a tela de consulta de relatorio Chamadas Saintes
 * 
 * @author Diego Luitgards
 * @since 16/10/2006
 */
public class ShowFiltroConsultaRelContabilChamadasSaintes extends ShowAction {

	private String codOperacao = Constantes.MENU_CONT_CHAMADAS_SAINTES;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelContabilChamadasSaintes.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception 
		{
			try 
			{
				TreeMenu menu = new TreeMenu("MenuParametros");
				
				GrupoItem grupoCategorias 	 = new GrupoItem("Categorias");
				
				// Monta Categorias
				Object[] categorias = CategoriaHome.findAll(db).toArray();
				Categoria categoria = null;
				
				db.begin();
				
				for (int i = 0; i < categorias.length; i++)
				{
					categoria = ((Categoria)categorias [i]);
					grupoCategorias.addItem(new OpcaoItem(categoria.getDesCategoria(), "CATEGORIA", categoria.getIdCategoria(), false));
				
				}
				
				menu.addItem(grupoCategorias);
				
				context.put("menuParametros", menu.escapeToJS(menu.toXML()));
			} 
			catch (PersistenceException pe) 
			{
				logger.error("Não foi possível realizar a consulta do relatório, problemas na conexão com o banco de dados (" +
							pe.getMessage() + ")");
				throw pe;
			}
			finally
			{
				db.commit();
			}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
