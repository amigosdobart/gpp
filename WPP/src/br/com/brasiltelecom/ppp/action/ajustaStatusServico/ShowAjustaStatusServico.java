/*
 * Created on 17/05/2005
 */
package br.com.brasiltelecom.ppp.action.ajustaStatusServico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.action.promocaoLancamento.SegurancaUsuario;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * @author Lawrence Josua
 */
public class ShowAjustaStatusServico extends ShowAction {

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		
		return "ajustaStatusServico.vm";
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);
		
		String matricula = login.getMatricula();
		
		session.setAttribute("assinante", request.getAttribute("assinante"));
		session.setAttribute("msisdn", request.getParameter("msisdn"));
		session.setAttribute("matricula", matricula);
		
		context.put("assinante", request.getAttribute("assinante"));
		context.put("msisdn", request.getParameter("msisdn"));
		context.put("assinanteDB", request.getAttribute("assinanteDB"));
		context.put("bloqueios", request.getAttribute("bloqueios"));
		context.put("util", new Util());
		context.put("mensagem", request.getAttribute(Constantes.MENSAGEM));
		context.put("status", request.getAttribute("status"));

	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
