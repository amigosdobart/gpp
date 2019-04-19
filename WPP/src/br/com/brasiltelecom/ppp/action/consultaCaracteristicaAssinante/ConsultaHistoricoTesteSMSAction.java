/*
 * Faz a busca das informações no banco de dados e as coloca no context
 * Criado em 16/06/2005
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaCaracteristicaAssinante;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import com.brt.gpp.aplicacoes.consultar.consultaAparelho.AparelhoAssinante;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.interfacegpp.EnviaSMSTeste;

/**
 * @author Marcelo Alves Araujo
 */
public class ConsultaHistoricoTesteSMSAction extends ActionPortal {
	
    //private String codOperacao = "CONSULTAR_CARAC_ASS";
	Logger logger = Logger.getLogger(this.getClass());

	/* 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception 
	{		
		logger.info("Consulta às características do aparelho do assinante");
		
		// Classe que modela as informações das características do assinante
		new AparelhoAssinante();
		
		HttpSession session = request.getSession(true);
		
		
		String msisdn = "55" + session.getAttribute("msisdn");
		
		// Busca informações de porta e servidor do GPP
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		try 
		{
		    // Retorna os dados do aparelho do assinante 
			EnviaSMSTeste.enviaSMS(msisdn,servidor,porta);
		}
		
		catch (Exception e)
		{
			request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar ao GPP");
		}
		
		session.getAttribute("testes");
		
		return actionMapping.findForward("success");
	}

	/* 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}