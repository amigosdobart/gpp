package br.com.brasiltelecom.ppp.action.acesso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;

/**
 * Classe que mostra a primeira página depois do logon do usuário.
 * 
 * @author Luciano Vilela
 * @since 01/02/2003
 * 
 */
public class ShowWelcomeAction extends ShowAction {

	/**
	 * Método para pegar a Tela de Apresentação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 */
	public String getTela() {
		return "welcome.vm";
	}

	/**
	 * Método principal da Classe, é o corpo da Classe.
	 * 
	 * @param context parâmetro do tipo VelocityContext.
	 * @param request parâmetro do tipo HttpServletRequest.
	 * @param db	   parâmetro do tipo Database do Castor.
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
		mensagem.append("<b>Atenção:</b><br><br>");
		mensagem.append("- Os limites de gastos com diárias e alimentação reembolsáveis estão definidos no Anexo II do Procedimento de Viagem em vigor.<br><br>");
		mensagem.append("- Somente serão liberadas no mesmo dia para emissão do Bilhete/E-Ticket, as Requisições Aprovadas no SAP até as 17:15 horas (Horário de Brasília).<br><br>");
		mensagem.append("- Lembramos que o funcionamento da Agência de Viagem é de 8 às 18 horas, de segunda-feira a sexta-feira.<br><br>");
		mensagem.append("- <b>Novidade:</b> Consulte a Relação de Hotéis com Tarifa Acordo.<br><br>");
		mensagem.append("- Lembramos que cabe ao colaborador selecionar a melhor opção de hotel, atendendo sempre o limite estipulado para hospedagem, conforme Tabela de Limites (Anexo II do Procedimento de Viagem em vigor). <br><b>O pagamento da hospedagem é efetuado diretamente pelo colaborador junto ao hotel.</b>");

		context.put("mensagemEsquerda", mensagem);
			
	}

	/**
	 * Método para pegar a Operação.
	 * 
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 */
	public String getOperacao() {
		return null;
	}

}