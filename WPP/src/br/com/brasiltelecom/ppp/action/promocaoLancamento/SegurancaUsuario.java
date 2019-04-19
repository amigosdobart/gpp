package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import java.util.Map;

import org.apache.velocity.VelocityContext;

import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe respons�vel por definir as permiss�es de gravar, excluir, alterar e visualizar o menu do Usu�rio.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 */
public class SegurancaUsuario {

	/**
	 * Metodo Construtor.
	 */
	public SegurancaUsuario() {
		super();
	}

	/**
	 *
	 * M�todo para setar as var�veis referente as permiss�es atribu�das ao usu�rio do sistema.
	 * 
	 * @param user Objeto do tipo Usuario.
	 *         context Objeto do tipo VelocityContext.
	 * 
	*/
	public static void setPermissao(Usuario user, VelocityContext context){

		Map operacao = user.getOperacoesPermitidas("MENU_CONTROLE");

		//if(operacao.get(Constantes.COD_MENU_USUARIO) != null){
			context.put("menuUsuario", "S");
		//}

		operacao = user.getOperacoesPermitidas("ACAO");

		if(operacao.get(Constantes.COD_ALTERAR_USUARIO) != null){
			context.put("alterarUsuario", "S");				
		}

		if(operacao.get(Constantes.COD_GRAVAR_USUARIO) != null){
			context.put("gravarUsuario", "S");				
		}
		
		if(operacao.get(Constantes.COD_EXCLUIR_USUARIO) != null){
			context.put("excluirUsuario", "S");				
		}

		if(operacao.get(Constantes.COD_INICIALIZAR_SENHA) != null){
			context.put("resetarSenha", "S");				
		}
		
		if(operacao.get(Constantes.COD_DESBLOQUEAR_USUARIO) != null){
			context.put("desbloquear", "S");				
		}

	}

}