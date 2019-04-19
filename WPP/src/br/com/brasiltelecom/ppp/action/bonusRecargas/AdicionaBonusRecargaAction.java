/*
 * Created on 05/08/2004
 *
 */
package br.com.brasiltelecom.ppp.action.bonusRecargas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaCodigosRetornoHome;
import br.com.brasiltelecom.ppp.interfacegpp.BonusRecargasGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author André Gonçalves
 * @since 05/08/2004
 */
public class AdicionaBonusRecargaAction extends ActionPortal {

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {

		
		dadosBonusPorRecarga dados = new dadosBonusPorRecarga(); // Array de elementos retornados pela chamada CORBA ao GPP
		dados.numeroRecargas = Short.parseShort(request.getParameter("numeroRecargas"));
		dados.percentualBonus = Short.parseShort(request.getParameter("percentualBonus"));
		
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		short resposta = BonusRecargasGPP.adicionaBonusRecarga(servidor, porta, dados);
		if (resposta!=0)
		{
			request.setAttribute(Constantes.MENSAGEM, ConsultaCodigosRetornoHome.findByValor(db, new Integer(resposta).toString()));
		}
		else
			request.setAttribute(Constantes.MENSAGEM, "Inclusão realizada com sucesso.");
					
		return actionMapping.findForward("success");
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {

		return null;
	}

}
