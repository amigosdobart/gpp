package br.com.brasiltelecom.ppp.action.acesso;

import java.io.IOException;

//import javax.servlet.ServletContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe que valida o logon dos usuários.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class LogonAction extends Action {


	/**
	 * Método principal do Struts, é o corpo da Classe.
	 * 
	 * @param actionMapping parâmetro do tipo ActionMapping.
	 * @param actionForm parâmetro do tipo ActionForm.
	 * @param request  parâmetro do tipo HttpServletRequest.
	 * @param response parâmetro do tipo HttpServletResponse.
	 *
	 * @throws IOException, ServletException.
	 */
	public ActionForward execute(
		   ActionMapping actionMapping,
		   ActionForm actionForm,
		   HttpServletRequest request,
		   HttpServletResponse response) throws IOException, ServletException {

		//ServletContext servletContext = servlet.getServletContext();
		HttpSession session = request.getSession(true);

		//String acao = (String) request.getParameter("ACAO");
		ActionForward result = null;
		String mensagemRetorno = null;
		String usuario = null;
		JDO jdo = (JDO) servlet.getServletContext().getAttribute(Constantes.JDO);

		try{

			Database db = jdo.getDatabase();
			db.begin();

			ServletContext context = getServlet().getServletContext();
			boolean mostraLogin  = new Boolean((String) context.getAttribute(Constantes.IDT_MOSTRA_LOGIN)).booleanValue();
			
			if(!mostraLogin)
			{
				result = actionMapping.findForward("showLogon");
			}
			else
			{
				usuario = request.getParameter("nome");
				String senha = request.getParameter("senha");
				int retorno = validaSenha(usuario, senha, db, session);
				switch(retorno){
	
					case Constantes.USUARIO_LOGADO_COM_SUSCESSO :
							result = actionMapping.findForward("success");
							mensagemRetorno = "ok";
							//Usuario user = (Usuario) session.getAttribute(Constantes.USUARIO);
							break;
	
					case Constantes.USUARIO_SENHA_INVALIDA :
							mensagemRetorno = "Senha Incorreta";
							break;
	
					case Constantes.USUARIO_NAO_CADASTRADO:
							mensagemRetorno = "Código de Usuário não cadastrado";
							break;
	
					case Constantes.USUARIO_BLOQUEADO :
							mensagemRetorno = "Usuário Bloqueado";
							break;
	
					case Constantes.USUARIO_DATA_VALIDADE_EXPIRADA:
							mensagemRetorno = "Usuário Bloqueado";
							break;
	
					case Constantes.USUARIO_SENHA_EXPIRADA:
							mensagemRetorno = "Senha Expirada";
							result = actionMapping.findForward("showAlteraSenha");
							session.invalidate();
							break;
	
	
					case Constantes.PROBLEMA_BANCO_DADOS:
							mensagemRetorno = "Banco de Dados com problema";
							break;
					}
		
					if(mensagemRetorno != null){
						request.setAttribute(Constantes.MENSAGEM, mensagemRetorno);	
					}
		
					if(result == null){
						result = actionMapping.findForward("showLogon");
					}
			}
			//LogFacade.log(db, usuario, "LOGON", mensagemRetorno , (String) request.getRemoteAddr());
			db.commit();
			db.close();
		}
		catch(Exception e){
			request.setAttribute(Constantes.MENSAGEM, e.toString());
			result = actionMapping.findForward("error");			
		}

		return result;
	}
	
	private int validaSenha(String id, String senha, Database db, HttpSession session){
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
		if(usuario.getSenha() == null || !usuario.getSenha().equals(senha)){
			return Constantes.USUARIO_SENHA_INVALIDA;
		}
		session.setAttribute(Constantes.USUARIO, usuario);
		return Constantes.USUARIO_LOGADO_COM_SUSCESSO;
	}
	
}