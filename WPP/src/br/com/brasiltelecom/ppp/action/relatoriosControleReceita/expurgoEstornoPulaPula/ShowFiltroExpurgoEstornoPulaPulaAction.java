package br.com.brasiltelecom.ppp.action.relatoriosControleReceita.expurgoEstornoPulaPula;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.PromocaoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import org.apache.log4j.Logger;

/**
 *	Classe responsavel por mostrar a tela de filtro de consulta de relatorio gerencial de Expurgo/Estorno de
 *	Bonus Pula-Pula sumarizado.
 * 
 *	@author	Daniel Ferreira
 *	@since	27/05/2004
 */
public class ShowFiltroExpurgoEstornoPulaPulaAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_CTR_EXPURGO_ESTORNO;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroCTRExpurgoEstornoPulaPula.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception 
	{
		try 
		{
			db.begin();
			Collection cns = CodigoNacionalHome.findAllBrt(db);
			context.put("cns", cns);
			Collection promocoes = PromocaoHome.findAllPulaPula(db);
			context.put("promocoes", promocoes);
		} 
		catch (PersistenceException e) 
		{
			logger.error("Excecao: " + e);
			throw e;
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
