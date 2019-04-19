/*
 * Criado em  05/07/2005
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioContabilRecargaDia;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CanalHome;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.SistemaOrigemHome;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/**
 * Mostra a Tela de Filtro de Relatorio de Recargas por Dia 
 * 
 * @author Marcelo Alves Araujo
 * @since 05/07/2005
 */
public class ShowFiltroConsultaRelContabilRecargaDiaAction extends ShowAction 
{
    private String codOperacao = Constantes.MENU_CONT_RECARGA_DIA;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
	    return "filtroConsultaRelContabilRecargaDia.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception 
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
			
			Collection canal = CanalHome.findAll(db);
			context.put("canais",canal);
			
			Collection sistema = SistemaOrigemHome.findAll(db);
			context.put("sistemas",sistema);
		}
		catch (PersistenceException e1) 
		{
			logger.error("Não foi possível realizar a consulta pelos dados de filtros, problemas na conexão com o banco de dados (" +
						e1.getMessage() + ")");
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