/*
 * Created on 17/05/2005
 */
package br.com.brasiltelecom.ppp.action.ajustaStatusServico;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteDB;
import br.com.brasiltelecom.ppp.home.AssinanteDBHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.home.ConfigAssinanteHome;

/**
 * @author Lawrence Josuá
 */
public class AjustaStatusServicoAction extends ActionPortal {
	
	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		logger.info("Consulta às informações do assinante solicitada");
		//codOperacao = "AJUSTAR_STATUS_SERVICO";
		
		Assinante assinante = new Assinante();
		AssinanteDB assinanteDB = null;
		Collection statusBloqueio = null;
		
		String msisdn = "55" + request.getParameter("msisdn");
		
		// Busca informações de porta e servidor do GPP
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		try 
		{
			assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn,servidor,porta);
			db.begin();
			assinanteDB = AssinanteDBHome.findByID(db, msisdn);
			statusBloqueio = ConfigAssinanteHome.findAllBloqueio(db);
		}
		
		catch (Exception e)
		{
			request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar ao GPP");
		}
		
			assinante.setMsisdn(msisdn);
			Integer status = new Integer(assinante.getStatusServico()); 
		
			request.setAttribute("msisdn",msisdn);
			request.setAttribute("bloqueios", statusBloqueio);
			request.setAttribute("assinante",assinante);
			request.setAttribute("assinanteDB", assinanteDB);
			request.setAttribute("status", status);
			return actionMapping.findForward("success");
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
