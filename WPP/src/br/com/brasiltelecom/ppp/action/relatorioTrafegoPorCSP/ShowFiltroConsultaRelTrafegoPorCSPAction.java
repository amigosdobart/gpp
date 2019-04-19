package br.com.brasiltelecom.ppp.action.relatorioTrafegoPorCSP;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;

/**
 * Mostra a tela de Filtro de Relatório de Tráfego por Código de Servico de 
 * Prestadora (CSP)
 * 
 * @author Daniel Ferreira
 * @since 10/02/2005
 */
public class ShowFiltroConsultaRelTrafegoPorCSPAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_REL_TRAFEGO_CSP;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelTrafegoPorCSP.vm";
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
			
			GrupoItem grupoCod 		 = new GrupoItem("Codigos Nacionais");
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
			
			// Monta Códigos Nacionais
			CodigoNacional cod = null;
			Object[] codigos = CodigoNacionalHome.findAllBrt(db).toArray();
			
			for (int i = 0; i < codigos.length; i++)
			{
				cod = ((CodigoNacional)codigos[i]);
				grupoCod.addItem(new OpcaoItem(cod.getIdtCodigoNacional() + " - " + cod.getIdtUF(), 
											   "CN", "" + cod.getIdtCodigoNacional(), false));
			}
			
			menu.addItem(grupoCategorias);
			menu.addItem(grupoCod);
			
			context.put("menuParametros", menu.escapeToJS(menu.toXML()));
			
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