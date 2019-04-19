package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTBonificacaoCampanhas;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.OrigemHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de relatorio de marketing Bonificacao de Campanhas
 * 
 * @author Geraldo Palmeira
 * @since  13/11/2006 
 */
public class ShowFiltroConsultaRelMKTBonificacaoCampanhas extends ShowAction {

	private String codOperacao = Constantes.MENU_MKT_BONIFICACAO_CAMPANHA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelMKTBonificacaoCampanhas.vm";
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
				context.put("codigosNacionais",CodigoNacionalHome.findAllBrt(db));
				context.put("origem",OrigemHome.findByCanal(db,"08"));
			} 
			catch (PersistenceException pe) 
			{
				logger.error("Não foi possível realizar a consulta de relatório de markting Bonificacao de Campanhas, problemas na conexão com o banco de dados (" +
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
