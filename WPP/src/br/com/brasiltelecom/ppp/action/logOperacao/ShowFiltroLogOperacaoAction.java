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
 * Classe para mostrar a página inicial do Log das Operações, possui os seguintes botões:
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
	 * Método para pegar a Tela Principal para o Log de Operações.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroLogOperacao.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.<BR>
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
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
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}

}
