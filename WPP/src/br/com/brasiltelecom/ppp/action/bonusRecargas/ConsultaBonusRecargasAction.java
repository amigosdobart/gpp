/*
 * Created on 04/08/2004
 *
 */
package br.com.brasiltelecom.ppp.action.bonusRecargas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaCodigosRetornoHome;
import br.com.brasiltelecom.ppp.interfacegpp.BonusRecargasGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga;
import br.com.brasiltelecom.ppp.portal.entity.DadosBonusRecarga;


/**
 * @author André Gonçalves
 * @since 04/08/2004
 */
public class ConsultaBonusRecargasAction extends ActionPortal {
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		retornoConsultaBonusPorRecarga dados; //Objeto com o array de resultados da consulta e o código de retorno
		
		
		
		/* Dados para conexão com o GPP */
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		
		/* Solicitação de Bônus por Recarga ao GPP */
		dados = BonusRecargasGPP.consultaBonusRecargasExistentes(servidor, porta);

		
		/* Resposta recebe o código de retorno da operação */
		short resposta = dados.codRetorno;
		
		
		/* Se a resposta for diferente de 0 (erro), disponibilizar mensagem de erro.*/
		if (resposta!=0)
		{
			request.setAttribute(Constantes.MENSAGEM, ConsultaCodigosRetornoHome.findByValor(db, new Integer(resposta).toString()));
		}
		/* Caso contrário, tratar os dados recebidos */
		else
		{
			DadosBonusRecarga[] dadosBonus = new DadosBonusRecarga[dados.listaBonus.length]; // Array de elementos java para tratamento dos dados retornados
			
			// Para cada elemento do array retornado pela chamada ao GPP, criar objeto DadosBonusRecarga
			for (int i=0; i < dados.listaBonus.length; i++)
			{
				dadosBonus[i] = new DadosBonusRecarga(dados.listaBonus[i].numeroRecargas, dados.listaBonus[i].percentualBonus);
			}
			request.setAttribute("dados", dadosBonus);
			//request.setAttribute("resposta", new Integer(resposta).toString());
			request.setAttribute("tamanho", new Integer(dadosBonus.length));
			request.setAttribute(Constantes.MENSAGEM, request.getAttribute(Constantes.MENSAGEM));
		}
		
		return actionMapping.findForward("success");
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
