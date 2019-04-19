package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTSaldosAssinantes;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * @author Gustavo Gusmão
 * @since 09/03/2006
 *
 */
public class ShowFiltroConsultaRelMKTSaldosAssinantesAction extends ShowAction
{
	private String codOperacao = Constantes.MENU_MKT_SALDO_ASSINANTES;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public String getTela()
	{
		return "filtroConsultaRelMKTSaldosAssinantes.vm";
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
					"saldos de assinantes, problemas na conexão com o banco de dados (" +
						pe.getMessage() + ")");
			throw pe;
		}
	}

	public String getOperacao()
	{
		return codOperacao;
	}
}