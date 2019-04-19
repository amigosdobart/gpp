package br.com.brasiltelecom.ppp.action.logOperacao;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;


/**
 * 
 * Classe para mostrar a p�gina inicial do Log das Opera��es, possui os seguintes bot�es:
 * 	- de Consulta;
 *  - e de Limpar os Dados Preenchidos.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowFiltroLogOperacaoAction extends ShowAction {

	private String codOperacao = Constantes.MENU_LOG_OPERACOES;
	/**
	 * M�todo para pegar a Tela Principal para o Log de Opera��es.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroLogOperacao.vm";
	}

	/**
	 * M�todo principal da Classe, � o corpo da Classe.<BR>
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
	 * @see br.com.brasiltelecom.portalNF.action.base.ShowAction#updateVelocityContext(VelocityContext)
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, Database db) throws Exception{
	
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		Map operacoesUsuario = login.getOperacoesPermitidas("MENU_CONTROLE");

		if(operacoesUsuario.get("MENU_LOG_OPERACOES") != null){
			context.put("consultar", "S");				
		}
	
		context.put("operacoes",Util.getOperacoesLog());
	}

	/**
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
