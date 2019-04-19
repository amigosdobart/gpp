/*
 * Faz a busca das informações no banco de dados e as coloca no context
 * Criado em 16/06/2005
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaCaracteristicaAssinante;

import java.util.Collection;

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
import br.com.brasiltelecom.ppp.home.ResultadosTesteSMSHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaCaracteristicaAssinanteGPP;

/**
 * @author Marcelo Alves Araujo
 */
public class ConsultaCaracteristicaAssinanteAction extends ActionPortal {
	
    //private String codOperacao = Constantes.COD_CONSULTAR_CARAC_ASS;
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
		AparelhoAssinante aparelho = new AparelhoAssinante();
		Collection resultados = null;
		String msisdn = null;
		if(request.getParameter("key") == null){
			if(request.getParameter("telefone") == null){
				if(request.getParameter("msisdn") == null){
					request.setAttribute(Constantes.MENSAGEM, "Código do MSISDN é obrigatório");
					return actionMapping.findForward("filtro");
				}
				else{
					msisdn = "55" + request.getParameter("msisdn");
				}
			}
			else{
				msisdn = "55" + request.getParameter("telefone");
			}
		}
		else{
			msisdn = "55" + request.getParameter("key");
		}
			
		// Busca informações de porta e servidor do GPP
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		try 
		{
			// Retorna os dados do aparelho do assinante 
		    aparelho = ConsultaCaracteristicaAssinanteGPP.getAparelho(msisdn,servidor,porta);
		    
		    db.begin();
		    resultados = ResultadosTesteSMSHome.findByMsisdn(db, msisdn);
		}
		
		catch (Exception e)
		{
			request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar ao GPP");
		}
					
		request.setAttribute("msisdn",request.getParameter("msisdn"));
		request.setAttribute("retorno",ConsultaCaracteristicaAssinanteGPP.getRetorno(msisdn,servidor,porta));
		request.setAttribute("aparelho",aparelho);
		request.setAttribute("testes",resultados);
		
		HttpSession session = request.getSession(true);
		session.setAttribute("msisdn",request.getParameter("msisdn"));
		session.setAttribute("retorno",ConsultaCaracteristicaAssinanteGPP.getRetorno(msisdn,servidor,porta));
		session.setAttribute("aparelho",aparelho);
		session.setAttribute("testes",resultados);
		
		
		return actionMapping.findForward("success");
	}

	/* 
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}