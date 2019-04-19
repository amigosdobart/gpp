package br.com.brasiltelecom.ppp.action.grupo;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * 
 * Classe para mostrar o resultado da consulta de Grupos.
 * 
 * @author Luciano Vilela
 * @since 22/09/2003
 * 
 */

public class ShowConsultaGrupoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_CADASTRO_PERFIL;
	/**
	 * Método para pegar a Tela do Filtro de Consulta de Grupo.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaGrupo.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,
		Database db) {	
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaGrupo.setPermissao(login, context);
		
		context.put("grupos", request.getAttribute(Constantes.RESULT));

		Collection result = (Collection) request.getAttribute(Constantes.RESULT);
		context.put("tamanho",String.valueOf(result.size()));	
	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
