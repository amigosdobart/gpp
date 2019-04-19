package br.com.brasiltelecom.ppp.action.cadastroServicoSasc;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.SascServicoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class ShowPerfisSascAction extends ShowAction
{
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "filtroPerfisSasc.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CONSULTAR_PERFIS_SERVICO;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		try
		{
			db.begin();
			
			// Pesquisa os perfis disponiveis no Banco de Dados
			// para utilizacao na manipulacao dos servicos relacionados
			context.put("perfis", SascServicoHome.findAllPerfis(db));
			db.commit();
		}
		catch(Exception e)
		{
			db.rollback();
		}
		finally
		{
			if (!db.isClosed())
				db.close();
		}
	}
}