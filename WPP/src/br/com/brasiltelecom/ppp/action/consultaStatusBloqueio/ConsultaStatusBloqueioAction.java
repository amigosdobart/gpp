/*
 * Created on 16/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaStatusBloqueio;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaAssinantesBloqueados;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteBloqueado;
import br.com.brasiltelecom.ppp.portal.entity.BloqueioServico;
import br.com.brasiltelecom.ppp.portal.entity.StatusBloqueioAssinante;
;

/**
 * @author André Gonçalves
 * @since 16/07/2004
 */
public class ConsultaStatusBloqueioAction extends ActionPortal {

	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result = null;
		
		db.begin();
		
		logger.info("Consulta por status de bloqueio solicitada");
		
		String msisdn = request.getParameter("obr_msisdn");
		if (msisdn != null && msisdn.length() == 13)
			msisdn = "55" + msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		
		AssinanteBloqueado assinante = null;
		StatusBloqueioAssinante status = null;
		ArrayList bloqueios = null;
		BloqueioServico bloqueioServico = null;
		String statusServico = "";
		boolean desbloqueadoFCL = false;
		boolean desbloqueadoIDC = false;
		
		try
		{
			assinante = ConsultaAssinantesBloqueados.findByMsisdn(db, msisdn);
			status = ConsultaAssinantesBloqueados.findStatusByMsisdn(db, msisdn);
			bloqueios = (ArrayList) ConsultaAssinantesBloqueados.findBloqueiosAutomaticosByMsisdn(db, msisdn);
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar a consulta dos status de bloqueio, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		
		if ( assinante == null ) //caso o assinante não esteja na tabela de msisdns bloqueados
		{
			// caso não esteja presente na tabela de interfaces
			if ( status == null )
			{
				statusServico = "Ativo";
			}
			// caso o seu status seja 'desbloqueado'
			else if ( Character.toLowerCase(status.getEvento()) == 'd' )
			{
				statusServico = "Desbloqueio em Andamento";
			}
		}
		else //caso o assinante esteja na tabela de msisdns bloqueados
		{
			// caso não esteja presente na tabela de interfaces
			if ( status == null )
			{
				statusServico = "Bloqueado";
			}
			// caso o seu status seja 'bloqueado'
			else if ( Character.toLowerCase(status.getEvento()) == 'b' )
			{
				statusServico = "Bloqueio em Andamento";
			}
		}
		
		for (int i = 0; i < bloqueios.size(); i++)
		{
			bloqueioServico = (BloqueioServico) bloqueios.get(i);
			if ( bloqueioServico.getServico().toLowerCase().equals("fcl") )
				desbloqueadoFCL = true;
			else if ( bloqueioServico.getServico().toLowerCase().equals("idc") )
				desbloqueadoIDC = true;
		}
		
		if ( desbloqueadoFCL && desbloqueadoIDC )
			request.setAttribute("desbloqueado", "checked");
		
		request.setAttribute("statusServico", statusServico);
		request.setAttribute("msisdn", request.getParameter("obr_msisdn"));
		
		result = actionMapping.findForward("success");		    
		
		return result;
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}

}
