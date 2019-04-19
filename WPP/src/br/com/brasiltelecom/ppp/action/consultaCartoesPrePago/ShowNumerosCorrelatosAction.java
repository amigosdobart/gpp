/*
 * Created on 24/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaCartoesPrePago;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de números correlatos
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowNumerosCorrelatosAction extends ShowAction {
	
	private String codOperacao = Constantes.MENU_CARTOES_PP;
	/** 
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "numerosCorrelatos.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,
		Database db) {
		//HttpSession session = request.getSession();
		//Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		//SegurancaGrupo.setPermissao(login, context);
		// Retornar linhas anteriores para controle de acesso.
		
		context.put("dadosChamadas", request.getAttribute(Constantes.RESULT));

		Collection result = (Collection) request.getAttribute(Constantes.RESULT);
		context.put("tamanho",String.valueOf(result.size()));

	
	}

	/**
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}
}