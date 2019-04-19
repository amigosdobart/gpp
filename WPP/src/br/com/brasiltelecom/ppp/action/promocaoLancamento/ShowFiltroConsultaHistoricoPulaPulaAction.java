package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 *	Consulta de Historico de execucao da Promocao Pula-Pula
 *  
 *	@author	Daniel Ferreira
 *	@since	28/03/2005
 * 
 */
public class ShowFiltroConsultaHistoricoPulaPulaAction extends ShowAction 
{

	private String codOperacao = Constantes.MENU_HIS_PULA_PULA;
	
	/**
	 * Metodo para pegar a Tela Principal do Usuario.
	 * 
	 *	@return	Um objeto do tipo String contendo o nome da Tela de Apresentacao.
	 *	@see	br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaHistoricoPulaPula.vm";
	}

	/**
	 *	Metodo principal da Classe, e o corpo da Classe.
	 * 
	 *	@param	context	parâmetro do tipo VelocityContext.
	 *	@param	request	parâmetro do tipo HttpServletRequest.
	 *	@param	db		parâmetro do tipo Database do Castor.
	 *	@see	br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) 
		throws Exception
	{

		HttpSession session = request.getSession();

		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);
		 
		context.put("usuarios", request.getAttribute(Constantes.RESULT));		 
	}

	/**
	 * Metodo para pegar a Operacao.
	 * 
	 *	@return	Um objeto do tipo String contendo o nome da Operacao realizada.
	 *	@see	br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}
