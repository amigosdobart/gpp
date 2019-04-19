/*
 * Created on 20/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaStatusBloqueio;

import java.util.Date;

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
import br.com.brasiltelecom.ppp.portal.entity.StatusBloqueioAssinante;

/**
 * @author André Gonçalves
 * @since 20/07/2004
 */
public class EfetuaBloqueioStatusAction extends ActionPortal {

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
		
		logger.info("Bloqueio de msisdn solicitado");
		
		String msisdn = request.getParameter("obr_msisdn");
		if (msisdn != null && msisdn.length() == 13)
			msisdn = msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		
		AssinanteBloqueado assinante = null;
		StatusBloqueioAssinante status = null;
		boolean nenhumaOperacao = false;
		
		try
		{
			assinante = ConsultaAssinantesBloqueados.findByMsisdn(db, msisdn);
			status = ConsultaAssinantesBloqueados.findStatusByMsisdn(db, msisdn);
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar o bloqueio do assinante, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		
		if ( assinante == null ) //caso o assinante não esteja na tabela de msisdns bloqueados
		{
			if ( status != null && status.getStatus().toLowerCase().equals("desbloqueado") )
			{
				ConsultaAssinantesBloqueados.deleteStatusBloqueioAssinante(db,status);
				db.commit();
				db.begin();
			}
			AssinanteBloqueado assinBloqueado = new AssinanteBloqueado();
			assinBloqueado.setDataBloqueio(new Date());
			assinBloqueado.setMsisdn(msisdn);
			ConsultaAssinantesBloqueados.setAssinanteBloqueado(db, assinBloqueado);
		}
		else //caso o assinante esteja na tabela de msisdns bloqueados
		{
			if ( status != null && status.getStatus().toLowerCase().equals("bloqueado") )
				nenhumaOperacao = true;
		}
		
		if (! nenhumaOperacao)
		{
			StatusBloqueioAssinante statusBloqueio = new StatusBloqueioAssinante();
			statusBloqueio.setMsisdn(msisdn);
			statusBloqueio.setStatus("Bloqueado");
			ConsultaAssinantesBloqueados.setStatusBloqueioAssinante(db, statusBloqueio);
		}		
		
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
