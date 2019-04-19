package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTSumarizacaoCampanha;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * 
 * @author Geraldo Palmeira
 * @since 16/03/2006
 *
 */
public class ShowFiltroConsultaRelMKTSumarizacaoCampanhaAction extends ShowAction
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	public String getTela()
	{
		return "filtroConsultaRelMKTSumarizacaoCampanha.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		try 
		{
			db.begin();
		} 
		catch (PersistenceException pe) 
		{
			logger.error("Não foi possível realizar a consulta de relatório de marketing consulta de " +
					"sumarização de campanhas, problemas na conexão com o banco de dados (" +
						pe.getMessage() + ")");
			throw pe;
		}
	}

	public String getOperacao()
	{
		return null;
	}
}