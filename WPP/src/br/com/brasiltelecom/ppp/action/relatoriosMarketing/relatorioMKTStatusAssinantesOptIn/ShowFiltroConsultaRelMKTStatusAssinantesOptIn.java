package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTStatusAssinantesOptIn;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de relatorio de marketing Status dos assiantes do Opt-in
 * 
 * @author Geraldo Palmeira
 * @since  02/05/2006
 */
public class ShowFiltroConsultaRelMKTStatusAssinantesOptIn extends ShowAction {

	private String codOperacao = Constantes.MENU_MKT_STATUS_ASS_OPT_IN;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelMKTStatusAssinantesOptIn.vm";
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
				db.begin();
			} 
			catch (PersistenceException pe) 
			{
				logger.error("Não foi possível realizar a consulta de relatório de markting Status dos assiantes do Opt-in, problemas na conexão com o banco de dados (" +
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
