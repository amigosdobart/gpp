/*
 * Created on 20/04/2004
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioBSCliente;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.home.CanalOrigemBSHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * Mostra a tela de consulta de BS por cliente
 * 
 * @author Andr� Gon�alves
 * @since 21/05/2004
 */
public class ShowFiltroConsultaRelBSClienteAction extends ShowAction {
	
	private String codOperacao = Constantes.MENU_BS_CLIENTE;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroConsultaRelBSCliente.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			//HttpSession session = request.getSession();
			try {
				db.begin();
				Collection c;
				try
				{
					c = CanalOrigemBSHome.findAll(db);
				}
				catch (PersistenceException pe)
				{
					logger.error("N�o foi poss�vel realizar a consulta de relat�rio de BS/cliente, problemas na conex�o com o banco de dados (" +
								pe.getMessage() + ")");
					throw pe;
				}
				context.put("origens",c);
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}