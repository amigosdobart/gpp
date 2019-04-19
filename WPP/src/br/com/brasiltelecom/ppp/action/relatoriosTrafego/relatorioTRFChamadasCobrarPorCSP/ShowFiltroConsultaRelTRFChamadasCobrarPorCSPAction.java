package br.com.brasiltelecom.ppp.action.relatoriosTrafego.relatorioTRFChamadasCobrarPorCSP;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CategoriaHome;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.OperadorasLDHome;
import br.com.brasiltelecom.ppp.portal.entity.Categoria;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.entity.OperadoraLD;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/**
 * Mostra a tela de consulta de relatório de Trafego de Chamadas a Cobrar por CSP
 * 
 * @author Geraldo Palmeira
 * @since 07/06/2006
 */
public class ShowFiltroConsultaRelTRFChamadasCobrarPorCSPAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_REL_TRF_CHAMADAS_COBRAR;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelTRFChamadasCobrarPorCSP.vm";
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
				GrupoItem grupoOperadoras 	 = new GrupoItem("Operadoras LDN");

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

				// Monta CSP
				OperadoraLD operadoraLd = null;
				Object[] operadoras = OperadorasLDHome.findAll(db).toArray();

				for (int i = 0; i < operadoras.length; i++)
				{
					operadoraLd = ((OperadoraLD)operadoras[i]);
					if (operadoraLd.getNumCSP() != 0)
						grupoOperadoras.addItem(new OpcaoItem(operadoraLd.getNumCSP() + " - " + operadoraLd.getNomOperadora(), 
								"CSP", "" + operadoraLd.getNumCSP(), false));
				}

				menu.addItem(grupoCod);
				menu.addItem(grupoOperadoras);
				menu.addItem(grupoCategorias);

				context.put("menuParametros", menu.escapeToJS(menu.toXML()));

			} 
			catch (PersistenceException pe) 
			{
				logger.error("Não foi possível realizar a consulta de relatório de Trafego de Chamadas à Cobrar por CSP, problemas na conexão com o banco de dados (" +
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
