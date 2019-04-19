/*
 * Created on 22/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioTrafegoDiario;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.OperadorasLDHome;
import br.com.brasiltelecom.ppp.home.PlanoPrecoHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de receita por plano de serviço e produto
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowFiltroConsultaRelTrafegoDiarioAction extends ShowAction
{
	private String codOperacao = Constantes.MENU_REL_TRAFEGO_DIARIO;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "filtroConsultaRelTrafegoDiario.vm";
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
			
			context.put("listaPlanoPreco", PlanoPrecoHome.findAll(db));
			context.put("listaCn", CodigoNacionalHome.findAllBrt(db));
			context.put("listaOperadoras", OperadorasLDHome.findAll(db));
			
			db.commit();
		}
		catch (Exception e)
		{
			if (db.isActive())
				db.rollback();
		}
		finally
		{
			if (!db.isClosed())
				db.close();
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
