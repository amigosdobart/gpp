
/*
 * Created on 20/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTRecargasPlanoControle;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CanalHome;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.OrigemHome;
import br.com.brasiltelecom.ppp.home.SistemaOrigemHome;

/*import br.com.brasiltelecom.ppp.home.CanalOrigemBSHome;*/
import br.com.brasiltelecom.ppp.portal.entity.Canal;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.entity.Origem;
import br.com.brasiltelecom.ppp.portal.entity.SistemaOrigem;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/** 
 * Mostra a tela de consulta de Relatório Ativações Manuais
 * 
 * @author Marcos C. Magalhães
 * @since 11/06/2006
 * 
 * Atualizado por: Bernardo Vergne Dias
 * Data: 29/01/2007
 * Código completamente reescrito
 */
public class ShowFiltroConsultaRelMKTRecargasControleAction extends ShowAction {

	private String codOperacao = Constantes.MENU_MKT_REC_PLAN_CONTROL;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelMKTRecargasControle.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db) throws Exception 
	{
		
		try
		{
	
			// Inicializa a criação do menu
			
			TreeMenu menu 					= new TreeMenu("MenuParametros");
			
			GrupoItem grupoCategorias 	 	= new GrupoItem("Categorias");
			GrupoItem grupoCanais 			= new GrupoItem("Canais de Origem");
			GrupoItem grupoCod 				= new GrupoItem("Codigos Nacionais");
			GrupoItem grupoSistemas 		= new GrupoItem("Sistemas de Recargas");
			
			// Monta Categorias
			Object[] categorias = CategoriaHome.findAll(db).toArray();
			Categoria categoria = null;
			
			db.begin();
			
			for (int i = 0; i < categorias.length; i++)
			{
				categoria = ((Categoria)categorias [i]);
				grupoCategorias.addItem(new OpcaoItem(categoria.getDesCategoria(), "CATEGORIA", categoria.getIdCategoria(), false));
			
			}
			
			// Monta os Canais
			
			Canal canal = null;
			Origem origem = null;
			Object[] origens = null;
			Object[] canais = CanalHome.findAll(db).toArray();
			GrupoItem grupoTemp = null;
			
			for (int i = 0; i < canais.length; i++)
			{
				canal = ((Canal)canais[i]);
				grupoTemp = new GrupoItem(canal.getDescCanal());
				origens = OrigemHome.findByCanal(db, canal.getIdCanal()).toArray();
			
				for (int j = 0; j < origens.length; j++)
				{
					origem = ((Origem)origens[j]);
					grupoTemp.addItem(new OpcaoItem(origem.getDescOrigem(), "ORIGEM", canal.getIdCanal() + origem.getIdOrigem(), false));
				}
				if (origens.length > 0) grupoCanais.addItem(grupoTemp);
			}
			
			// Monta os Cod. Nacionais
			
			CodigoNacional cod = null;
			Object[] codigos = CodigoNacionalHome.findAllBrt(db).toArray();
			
			for (int i = 0; i < codigos.length; i++)
			{
				cod = ((CodigoNacional)codigos[i]);
				grupoCod.addItem(new OpcaoItem(cod.getIdtCodigoNacional() + " - " + cod.getIdtUF(), 
						"COD_NACIONAL", "" + cod.getIdtCodigoNacional(), false));
			}
		
			// Monta os Sistemas
			
			String idSistema = "";
			Object[] sistemas = SistemaOrigemHome.findAll(db).toArray();
			
			for (int i = 0; i < sistemas.length; i++)
			{
				idSistema = ((SistemaOrigem)sistemas[i]).getIdSistemaOrigem();
				grupoSistemas.addItem(new OpcaoItem(idSistema, "SISTEMA", idSistema, false));
			}
			
			// Conclui o menu
			
			menu.addItem(grupoCategorias);
			menu.addItem(grupoCanais);
			menu.addItem(grupoCod);
			menu.addItem(grupoSistemas);

			context.put("menuParametros", menu.escapeToJS(menu.toXML()));
		}
		catch (PersistenceException pe)
		{
			logger.error("Não foi possível gerar relatório MKTRecargasControle. Problemas com o banco de dados (" +
					pe.getMessage() + ")");
			throw pe;
		}
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}

