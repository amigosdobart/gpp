package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMktMobileMkt;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.PesquisaMktHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsavel por selecionar os dados
 * do Banco e redirecionar a requisicao para
 * mostrar a tela para o usuario
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  12/01/2007
 */
public class ShowFiltroConsultaRelMktMobileMktAction extends ShowAction 
{
	private String codOperacao = Constantes.MENU_REL_MKT_MOBILE_MKT;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "filtroConsultaRelMKTMobileMkt.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		try
		{
			db.begin();
			
			context.put("pesquisas", PesquisaMktHome.findAll(db));
		}
		catch(PersistenceException pe)
		{
			logger.error("Não foi possível realizar a consulta das Pesquisas Mobile Marketing (" +
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


