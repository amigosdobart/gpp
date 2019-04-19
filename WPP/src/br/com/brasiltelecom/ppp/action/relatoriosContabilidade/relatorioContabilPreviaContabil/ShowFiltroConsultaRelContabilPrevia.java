package br.com.brasiltelecom.ppp.action.relatoriosContabilidade.relatorioContabilPreviaContabil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.PeriodoContabilHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;

/**
 * Mostra a tela de consulta de relatorio de Prévia Contabil
 * 
 * @author Geraldo Palmeira
 * @since 12/12/2006 
 */
public class ShowFiltroConsultaRelContabilPrevia extends ShowAction 
{

	private String codOperacao = Constantes.MENU_CONSULTA_PREVIA_CONTABIL;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelContabilPrevia.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext
	(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception 
		{
			try 
			{
				db.begin();
				Collection periodosContabeis = PeriodoContabilHome.findPeriodoContabilEspecifico(db, 4, 3);
				context.put("periodosContabeis",periodosContabeis);
			} 
			catch (PersistenceException pe) 
			{	
				logger.error("Não foi possível realizar a consulta de relatório prévia contábil, problemas na conexão com o banco de dados (" +
							pe.getMessage() + ")");
				throw pe;
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
