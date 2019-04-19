/*
 * Created on 20/07/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaStatusBloqueio;

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
public class EfetuaDesbloqueioStatusAction extends ActionPortal {

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
		
		logger.info("Desbloqueio de msisdn solicitado");
		
		String msisdn = request.getParameter("obr_msisdn");
		if (msisdn != null && msisdn.length() == 13)
			msisdn = msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		
		AssinanteBloqueado assinante = null;
		StatusBloqueioAssinante status = null;
		
		try
		{
			assinante = ConsultaAssinantesBloqueados.findByMsisdn(db, msisdn);
			status = ConsultaAssinantesBloqueados.findStatusByMsisdn(db, msisdn);
		}
		catch (Exception e)
		{
			logger.error("Não foi possível realizar o desbloqueio do assinante, problemas na conexão com o banco de dados (" +
						e.getMessage() + ")");
			throw e;
		}
		
		if ( assinante == null ) //caso o assinante não esteja na tabela de msisdns bloqueados
		{
			if (status == null)
			{
				StatusBloqueioAssinante statusBloqueio = new StatusBloqueioAssinante();
				statusBloqueio.setMsisdn(msisdn);
				statusBloqueio.setStatus("Desbloqueado");
				ConsultaAssinantesBloqueados.setStatusBloqueioAssinante(db, statusBloqueio);
			}
		}
		else //caso o assinante esteja na tabela de msisdns bloqueados
		{
			if ( status != null && status.getStatus().toLowerCase().equals("bloqueado") )
			{
				/*
				 * - Deletar registro de bloqueio pendente da tabela de interfaces
				 * - Remover MSISDN da tabela de MSISDN's bloqueados
				 */
				ConsultaAssinantesBloqueados.deleteStatusBloqueioAssinante(db,status);
				ConsultaAssinantesBloqueados.deleteAssinanteBloqueado(db, assinante);
			}
			if (status == null)
			{
				/*
				 * - Remover MSISDN da tabela de MSISDN's bloqueados
				 * - Inserir registro de desbloqueio na tabela de interfaces
				 */
				ConsultaAssinantesBloqueados.deleteAssinanteBloqueado(db, assinante);
				
				StatusBloqueioAssinante statusBloqueio = new StatusBloqueioAssinante();
				statusBloqueio.setMsisdn(msisdn);
				statusBloqueio.setStatus("Desbloqueado");
				ConsultaAssinantesBloqueados.setStatusBloqueioAssinante(db, statusBloqueio);
			}
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
