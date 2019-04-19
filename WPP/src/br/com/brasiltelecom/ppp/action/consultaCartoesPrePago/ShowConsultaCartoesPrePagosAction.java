/*
 * Created on 19/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaCartoesPrePago;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.action.grupo.SegurancaGrupo;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta do cartão pré-pago
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowConsultaCartoesPrePagosAction extends ShowAction {
	
	private String codOperacao = Constantes.MENU_CARTOES_PP;
	
	/** 
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaCartoesPrePagos.vm";
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,
		Database db) 
	{
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);
		
		Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));	
		boolean podeVerFormularioCodSeguranca = (usuario.getOperacoesPermitidas(Constantes.ACAO).get(Constantes.COD_VER_FORMULARIO_PIN) != null);
		context.put("podeVerFormularioCodSeguranca", new Boolean(podeVerFormularioCodSeguranca)); 
		
		context.put("usuarios", request.getAttribute(Constantes.RESULT));

	}		

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
