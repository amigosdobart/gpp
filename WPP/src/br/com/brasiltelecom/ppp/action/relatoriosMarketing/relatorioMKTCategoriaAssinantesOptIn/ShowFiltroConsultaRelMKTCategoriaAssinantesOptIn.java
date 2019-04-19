package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTCategoriaAssinantesOptIn;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de relatorio de marketing Categoria dos assiantes do Opt-in
 * 
 * @author Geraldo Palmeira
 * @since  20/06/2006
 */
public class ShowFiltroConsultaRelMKTCategoriaAssinantesOptIn extends ShowAction {

	private String codOperacao = Constantes.MENU_MKT_CATEG_ASS_OPT_IN;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelMKTCategoriaAssinantesOptIn.vm";
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
				logger.error("N�o foi poss�vel realizar a consulta de relat�rio de markting Categoria dos assiantes do Opt-in, problemas na conex�o com o banco de dados (" +
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
