package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilChamadasAmigosTodaHora;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException; 

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/**
 * Mostra a tela de relatorio Contábil Amigos toda hora
 * Se encontra no novo modelo para testes da simplificação dos templates do portal
 * 
 * @author Magno Batista Corrêa
 * @since 2006/08/09 (yyyy/mm/dd)
 */
public class ShowFiltroConsultaRelContabilChamadasAmigosTodaHoraAction extends ShowAction
{
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "_filtroConsultaRelUnico.vm";
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
			this.codOperacao =  Constantes.MENU_CON_AMIGOS_TD_H;
			String imagemSrc = "img/tit_rel_cont_amigos_toda_hora_normal.gif";
			String imagemAlt = "Relatório Contábil Amigos Toda Hora";
			String frmAction = "consultaRelContabilChamadasAmigosTodaHora.do";
			String[] camposEntrada = {"CampoIntervaloDataPorMes"};
			String campoFormatosSaida = "ComboTodosFormatos";

			context.put("imagemSrc", imagemSrc);
			context.put("imagemAlt", imagemAlt);
			context.put("frmAction", frmAction);
			context.put("camposEntrada", camposEntrada);
			context.put("campoFormatosSaida", campoFormatosSaida);
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}
