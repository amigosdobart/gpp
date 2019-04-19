package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilCobrancaServico;

import java.util.Collection;
import java.util.Iterator;

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
import br.com.brasiltelecom.ppp.portal.entity.PlanoPreco;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/**
 * Mostra a tela de consulta do Relatório Cobrança Serviço 102
 * 
 * @author Geraldo Palmeira
 * @since 14/05/2007
 */
public class ShowFiltroConsultaRelContabilCobrancaServico extends ShowAction 
{

	private String codOperacao = Constantes.COD_CONSULTA_COBRANCA_SERVICO_102;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelContabilCobrancaServico.vm";
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
				// Inicializa a criação do menu
				TreeMenu menu 					= new TreeMenu("MenuParametros");
				
				GrupoItem grupoCategorias 	 	= new GrupoItem("Categorias");
				GrupoItem grupoCod 				= new GrupoItem("Codigos Nacionais");
				GrupoItem grupoTemp = null;

				
				// Monta Categorias
				Collection categorias = CategoriaHome.findAll(db);
				db.begin();
				
				for (Iterator i=categorias.iterator(); i.hasNext();)
				{
					Categoria categoria = ((Categoria)i.next());
					grupoTemp = new GrupoItem(categoria.getDesCategoria());
					
					for (Iterator j=categoria.getPlanos().iterator(); j.hasNext();)
					{ 
						PlanoPreco plano = ((PlanoPreco)j.next());
						grupoTemp.addItem(new OpcaoItem(plano.getDescricao(), "PLANO", plano.getIdPlano(), false));
					}
					grupoCategorias.addItem(grupoTemp);
				}
				
				// Monta os Cod. Nacionais
				
				CodigoNacional cod = null;
				Object[] codigos = CodigoNacionalHome.findAllBrt(db).toArray();
				
				for (int i = 0; i < codigos.length; i++)
				{
					cod = ((CodigoNacional)codigos[i]);
					grupoCod.addItem(new OpcaoItem(cod.getIdtCodigoNacional() + " - " + cod.getIdtUF(),"CN", "" + cod.getIdtCodigoNacional(), false));
				}
				
				// Conclui o menu
				
				menu.addItem(grupoCategorias);
				menu.addItem(grupoCod);
	
				context.put("menuParametros", menu.escapeToJS(menu.toXML()));

				db.commit();
			}
			catch (PersistenceException pe)
			{
				logger.error("Não foi possível gerar relatório Contábil de Cobrança de Serviço 102. Problemas com o banco de dados (" +
						pe.getMessage() + ")");
				db.rollback();
			}
			finally
			{
				db.close();
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