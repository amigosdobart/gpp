package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTOperacionalOptIn;

import java.util.Collection;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.ModeloHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta relatório Operacional de Opt-in
 * 
 * @author Geraldo Palmeira
 * @since 14/07/2006
 */
public class ShowFiltroConsultaRelMKTOperacionalOptIn extends ShowAction {

	private String codOperacao = Constantes.MENU_MKT_OPERACIONAL_OPT_IN;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelMKTOperacionalOptIn.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
									 HttpServletRequest request,
									 Database db) throws Exception 
	{
		try 
		{
			db.begin();
			context.put("codigosNacionais",CodigoNacionalHome.findAllBrt(db));
			
			String nomeModelo = request.getParameter("modelo");
			Collection modelos = new TreeSet();
			if (nomeModelo != null && !"".equals(nomeModelo))
			{
				String mods[] = nomeModelo.split(",");
				for (int i = 0; i < mods.length; i++)
					modelos.addAll(ModeloHome.findByNome(db,mods[i]));
			}
			db.commit();
			context.put("modelos",modelos);
		} 
		catch (PersistenceException pe) 
		{
			logger.error("Não foi possível realizar a consulta de relatório Operacional de Opt-In, problemas na conexão com o banco de dados (" +
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
