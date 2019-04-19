package br.com.brasiltelecom.ppp.action.acesso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

/**
 * Classe que mostra a primeira p�gina depois do logon do usu�rio.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowWelcomeAction extends ShowAction {

	/**
	 * M�todo para pegar a Tela de Apresenta��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresenta��o.
	 */
	public String getTela() {
		return "welcome.vm";
	}

	/**
	 * M�todo principal da Classe, � o corpo da Classe.
	 * 
	 * @param context par�metro do tipo VelocityContext.
	 * @param request par�metro do tipo HttpServletRequest.
	 * @param db	   par�metro do tipo Database do Castor.
	 * 
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request,
		Database db) {
			
		/************Declaracao das variaveis do metodo**************/
 		 StringBuffer mensagem = new StringBuffer();
 		 HttpSession session = null;
		/************************************************************/	

		session = request.getSession();		
																
		Usuario sessaoUser = (Usuario) session.getAttribute(Constantes.USUARIO);

		String nome = sessaoUser.getNome();
		
		mensagem.append("<b>Prezado(a) "+ nome +", Seja Bem Vindo(a)!</b><br><br>");
		mensagem.append("<b>Aten��o:</b><br><br>");
		mensagem.append("- Os limites de gastos com di�rias e alimenta��o reembols�veis est�o definidos no Anexo II do Procedimento de Viagem em vigor.<br><br>");
		mensagem.append("- Somente ser�o liberadas no mesmo dia para emiss�o do Bilhete/E-Ticket, as Requisi��es Aprovadas no SAP at� as 17:15 horas (Hor�rio de Bras�lia).<br><br>");
		mensagem.append("- Lembramos que o funcionamento da Ag�ncia de Viagem � de 8 �s 18 horas, de segunda-feira a sexta-feira.<br><br>");
		mensagem.append("- <b>Novidade:</b> Consulte a Rela��o de Hot�is com Tarifa Acordo.<br><br>");
		mensagem.append("- Lembramos que cabe ao colaborador selecionar a melhor op��o de hotel, atendendo sempre o limite estipulado para hospedagem, conforme Tabela de Limites (Anexo II do Procedimento de Viagem em vigor). <br><b>O pagamento da hospedagem � efetuado diretamente pelo colaborador junto ao hotel.</b>");

		context.put("mensagemEsquerda", mensagem);
			
	}

	/**
	 * M�todo para pegar a Opera��o.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Opera��o realizada.
	 */
	public String getOperacao() {
		return null;
	}

}