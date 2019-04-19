package br.com.brasiltelecom.ppp.action.acesso;

import java.io.IOException;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;

import br.com.brasiltelecom.ppp.home.UsuarioHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.*;

/**
 * Classe que realiza a altera��o da senha dos usu�rios.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class AlteraSenhaAction extends Action {


	/**
	 * M�todo principal do Struts, � o corpo da Classe.
	 * 
	 * @param actionMapping par�metro do tipo ActionMapping.
	 * @param actionForm par�metro do tipo ActionForm.
	 * @param request  par�metro do tipo HttpServletRequest.
	 * @param response par�metro do tipo HttpServletResponse.
	 *
	 * @throws IOException, ServletException.
	 */
	public ActionForward execute(
		   ActionMapping actionMapping,
		   ActionForm actionForm,
		   HttpServletRequest request,
		   HttpServletResponse response) throws IOException, ServletException {

		ServletContext servletContext = servlet.getServletContext();
		HttpSession session = request.getSession(true);

		//String acao = (String) request.getParameter("ACAO");
		ActionForward result = null;
		String mensagemRetorno = null;
		String usuario = null;
		JDO jdo = (JDO) servlet.getServletContext().getAttribute(Constantes.JDO);

		try{

			Database db = jdo.getDatabase();
			db.begin();

			usuario = request.getParameter("matricula");
			String senha = request.getParameter("senha");
			String senhaAtual = request.getParameter("senhaatual");

			int retorno = alteraSenha(usuario, senhaAtual, senha,  db, session);

		
			switch(retorno){
				case Constantes.USUARIO_LOGADO_COM_SUSCESSO :
						result = actionMapping.findForward("success");
						mensagemRetorno = "Senha alterada com sucesso";

						/*session.invalidate();
						Usuario user = (Usuario) session.getAttribute(Constantes.USUARIO);
						Map ativos = (Map) session.getServletContext().getAttribute(Constantes.USUARIOS_ATIVOS);
						ativos.put(user.getId(), "");
						System.out.println("usuario logado " + user.getId());*/
						break;

				case Constantes.USUARIO_SENHA_INVALIDA :
						mensagemRetorno = "Senha incorreta";
						session.invalidate();
						break;

				case Constantes.USUARIO_NAO_CADASTRADO:
						mensagemRetorno = "C�digo de usu�rio n�o cadastrado";
						session.invalidate();
						break;

				case Constantes.USUARIO_BLOQUEADO :
						mensagemRetorno = "Usu�rio bloqueado";
						session.invalidate();
						break;

				case Constantes.USUARIO_SENHA_JA_CADASTRADA :
						mensagemRetorno = "Esta senha j� foi utilizada";
						session.invalidate();
						break;

				case Constantes.USUARIO_DATA_VALIDADE_EXPIRADA:
						mensagemRetorno = "Usu�rio bloqueado";
						session.invalidate();
						break;

				case Constantes.USUARIO_TAMANHO_SENHA_INVALIDO:
						Map parametros = (Map) servletContext.getAttribute(Constantes.PARAMETRO);
						int tamanhoSenha = ((Parametro)parametros.get("TAMANHO_SENHA")).getValorInt(); 
						mensagemRetorno = "A senha deve ter no m�nimo <b>" + tamanhoSenha + "</b> caracteres";
						session.invalidate();
						break;

				case Constantes.USUARIO_SENHA_EXPIRADA:
						mensagemRetorno = "Senha expirada";
						session.invalidate();
						break;

				case Constantes.PROBLEMA_BANCO_DADOS:
						mensagemRetorno = "Banco de dados com problema";
						session.invalidate();
						break;
			}	

			if(mensagemRetorno != null){
				request.setAttribute(Constantes.MENSAGEM, mensagemRetorno);	
			}

			if(result == null){
				result = actionMapping.findForward("showAlteraSenha");
			}

			//LogFacade.log(db, usuario, "ALTERA_SENHA", mensagemRetorno, (String) request.getRemoteAddr());

			db.commit();
			db.close();
		}
		catch(Exception e){
			request.setAttribute(Constantes.MENSAGEM, e.toString());
			result = actionMapping.findForward("error");
			session.invalidate();
		}
		
		return result;
	}
	private int alteraSenha(String id, String senhaAtual, String novaSenha, Database db, HttpSession session){
		Usuario usuario = null;
		try{
			usuario= UsuarioHome.findByID(db, id);
		}
		catch(Exception e){
			return Constantes.PROBLEMA_BANCO_DADOS;
		}
		if(usuario == null){
			return Constantes.USUARIO_NAO_CADASTRADO;
		}
		if(!usuario.getSenha().equals(senhaAtual)){
			return Constantes.USUARIO_SENHA_INVALIDA;
		}
		usuario.setSenha(novaSenha);
		session.setAttribute(Constantes.USUARIO, usuario);
		return Constantes.USUARIO_LOGADO_COM_SUSCESSO;
	}
}