package br.com.brasiltelecom.ppp.action.promocaoLancamento;

import java.util.Collection;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
//import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.model.RetornoPulaPula;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe responsável por mostrar o filtro de consulta de Usuários do sistema.
 *
 * @author Luciano Vilela
 *
 */
public class ShowConsultaPromocaoAssinanteAction extends ShowAction
{


	private String codOperacao = Constantes.MENU_CONSULTA_PULA_PULA;
	/*** Guarda o content type da página.*/
     String  strContentType = "text/html";



	/**
	 * Método para pegar a Tela do Filtro de Consulta de Usuários.
	 *
	 * @return Um objeto do tipo String contendo o nome da Tela de Apresentação.
	 * @see br.com.brasiltelecom.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "consultaPromocaoAssinante.vm";
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
	   Database db)
	{
		HttpSession session = request.getSession();
		Usuario login = (Usuario) session.getAttribute(Constantes.LOGIN);
		SegurancaUsuario.setPermissao(login, context);

		RetornoPulaPula assinante = null;

		try
		{
			assinante= (RetornoPulaPula)request.getAttribute("assinante");
			DecimalFormat conversorValor = new DecimalFormat(Constantes.DOUBLE_FORMATO, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
			
			//Inserindo os valores no contexto			
			context.put("msisdn", 					assinante.getMsisdn());
			context.put("nomePromocao", 			assinante.getNome());
			context.put("idPromocao", 				Integer.valueOf(assinante.getIdentificador()));
			context.put("dataCreditoBonus", 		assinante.getDataCredito());
			context.put("dataEntradaPromocao", 		assinante.getDataEntrada());
			context.put("dataInicioAnalise", 		assinante.getDataInicioAnalise());
			context.put("dataFimAnalise", 			assinante.getDataFimAnalise());
			context.put("descricaoStatus", 			request.getAttribute("descricaoStatus"));
			context.put("totalMinutos", 			assinante.getRecebimentoTotal());
			context.put("totalRecargasEfetuadas", 	assinante.getRecargasEfetuadas());
			context.put("bonificacoes", 			assinante.getBonificacoes());
			context.put("conversorDouble", 			conversorValor);
			context.put("valorTotalPulaPula", 		assinante.getValorTotal());
			context.put("valorParcialPulaPula", 	assinante.getValorParcial());
			context.put("valorAReceberPulaPula", 	assinante.getValorAReceber());
			context.put("podeTrocarPromocao", 		(Boolean)request.getAttribute("podeTrocarPromocao"));
			context.put("podeTrocarIsencaoLimite", 	(Boolean)request.getAttribute("podeTrocarIsencaoLimite"));
			context.put("listaPromocoes", 			(Collection)request.getAttribute("listaPromocoes"));
			context.put("listaTipoDocumento", 		(Collection)request.getAttribute("listaTipoDocumento"));
			context.put("listaMotivoTrocaProm", 	(Collection)request.getAttribute("listaMotivoTrocaProm"));
			context.put("matricula", 				login.getMatricula());			
			context.put("conversorValor", 			conversorValor);
			context.put("existeBonusAgendado", 	(assinante.getBonusAgendados().size() > 0) ? new Boolean(true) : new Boolean(false));
			context.put("existeBonusFuturo", 	(assinante.getBonusFuturos().size() > 0) ? new Boolean(true) : new Boolean(false));
			context.put("bonusAgendados", 			assinante.getBonusAgendados());
			context.put("bonusFuturos", 			assinante.getBonusFuturos());
			context.put("totalBonusFuturos", 		conversorValor.format(Double.parseDouble(assinante.getValorTotalBonusAgendado())));
			
			context.put(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));

		}
		catch (NullPointerException ne)
		{
			return;
		}
	}

	/**
	 * Método para pegar a Operação.
	 *
	 * @return Um objeto do tipo String contendo o nome da Operação realizada.
	 * @see br.com.brasiltelecom.portalNF.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}

}
