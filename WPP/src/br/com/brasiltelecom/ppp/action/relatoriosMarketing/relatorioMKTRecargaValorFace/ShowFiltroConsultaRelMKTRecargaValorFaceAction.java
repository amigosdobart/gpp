/*
 * Created on 12/05/2005
 *
 */
package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTRecargaValorFace;
 
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
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

/**
 * Mostra a tela de Filtro de Relatorio de consumo de saldo do clientes do Amigos Toda Hora 
 * 
 * @author Marcelo Alves Araujo
 * @since 08/06/2005
 */
public class ShowFiltroConsultaRelMKTRecargaValorFaceAction extends ShowAction 
{
    private String codOperacao = Constantes.MENU_MKT_RECARGAS_FACE;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
	    return "filtroConsultaRelMKTRecargaValorFace.vm";
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
			
			// Monta Categorias
			for (int i = 0; i < categorias.length; i++)
			{
				categoria = ((Categoria)categorias [i]);
				grupoCategorias.addItem(new OpcaoItem(categoria.getDesCategoria(), "CATEGORIA", categoria.getIdCategoria(), false));			
			}
					
			menu.addItem(grupoCategorias);
			
			context.put("menuParametros", menu.escapeToJS(menu.toXML()));
						
			//Inserindo os meses disponiveis para a pesquisa
			Calendar calMeses = Calendar.getInstance();
			SimpleDateFormat conversorData = new SimpleDateFormat("MMM/yyyy");
			
			for(int i = 0; i <= 5; i++)
			{
				context.put("mes" + String.valueOf(i), conversorData.format(calMeses.getTime()));
				calMeses.add(Calendar.MONTH, -1);
			}
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