package br.com.brasiltelecom.ppp.action.grupo;


import java.util.Map;

import org.apache.velocity.VelocityContext;

import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe respons�vel por definir as permiss�es de gravar, excluir, alterar e visualizar o menu do Grupo.
 * 
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 */
public class SegurancaGrupo {

	/**
	 * Construtor padr�o para a classe SegurancaEmpresa.
	 */
	public SegurancaGrupo() {
		super();
	}

	/**
	 * M�todo st�tico que define as permiss�es do usu�rio do sistema.
	 * 
	 * @param user par�metro do tipo Usuario.
	 * @param context par�metro do tipo VelocityContext.
	 * 
	 */	
	public static void setPermissao(Usuario user, VelocityContext context){

		Map operacao = user.getOperacoesPermitidas("MENU_CONTROLE");

		if(operacao.get(Constantes.COD_MENU_GRUPO) != null){
			context.put("menuGrupo", "S");				
		}

		operacao = user.getOperacoesPermitidas("ACAO");

		if(operacao.get(Constantes.COD_GRAVAR_GRUPO) != null){
			context.put("gravarGrupo", "S");
		}

		if(operacao.get(Constantes.COD_ALTERAR_GRUPO) != null){
			context.put("alterarGrupo", "S");
		}

		if(operacao.get(Constantes.COD_EXCLUIR_GRUPO) != null){
			context.put("excluirGrupo", "S");
		}

	}

}