package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTDemonstativoBonus;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.PromocaoHome;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.entity.Promocao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/**
 * Classe responsavel por selecionar os dados
 * do Banco e redirecionar a requisicao para
 * mostrar a tela para o usuario
 * 
 * @author Geraldo Palmeira
 * @since  23/01/2007
 */
public class ShowFiltroConsultaRelMKTDemonstativoBonusAction extends ShowAction 
{
	private String codOperacao = Constantes.MENU_REL_MKT_DEMONSTRATIVO_BONUS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "filtroConsultaRelMKTDemonstrativoBonus.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		try
		{
			TreeMenu menu = new TreeMenu("MenuParametros");
			
			GrupoItem grupoCod 		 = new GrupoItem("Codigos Nacionais");
			GrupoItem grupoCategorias 	 = new GrupoItem("Categorias");
			GrupoItem grupoPromocoes = new GrupoItem("Promoções");
			
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
			
			// Monta Promoções
			Object[] promocoes =  PromocaoHome.findAll(db).toArray();
			Promocao promocao = null;
			
			for (int i = 0; i < promocoes.length; i++)
			{
				promocao = ((Promocao)promocoes [i]);
				grupoPromocoes.addItem(new OpcaoItem(promocao.getNomPromocao(), "PROMOCAO", promocao.getIdtPromocao().toString(), false));
			}
			
			// Monta Códigos Nacionais
			CodigoNacional cod = null;
			Object[] codigos = CodigoNacionalHome.findAllBrt(db).toArray();
			
			for (int i = 0; i < codigos.length; i++)
			{
				cod = ((CodigoNacional)codigos[i]);
				grupoCod.addItem(new OpcaoItem(cod.getIdtCodigoNacional() + " - " + cod.getIdtUF(), 
											   "COD_NACIONAL", "" + cod.getIdtCodigoNacional(), false));
			}
			
			menu.addItem(grupoCategorias);
			menu.addItem(grupoPromocoes);
			menu.addItem(grupoCod);
			
			context.put("menuParametros", menu.escapeToJS(menu.toXML()));
			
		}
		catch(PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta do Demonstrativo de Bônus: (" +
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


