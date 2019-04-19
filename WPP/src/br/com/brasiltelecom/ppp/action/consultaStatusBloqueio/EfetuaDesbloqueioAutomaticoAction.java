
package br.com.brasiltelecom.ppp.action.consultaStatusBloqueio;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ConsultaAssinantesBloqueados;
import br.com.brasiltelecom.ppp.portal.entity.BloqueioServico;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * TODO Descrição
 * @author Alex Pitacci Simões
 * @since 28/07/2004
 */
public class EfetuaDesbloqueioAutomaticoAction extends ActionPortal {

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		ActionForward result = null;
		
		db.begin();
		
		String operacao = request.getParameter("operacao");
		
		String msisdn = request.getParameter("obr_msisdn");
		if (msisdn != null && msisdn.length() == 13)
			msisdn = "55" + msisdn.substring(1,3) + msisdn.substring(4,8) + msisdn.substring(9,13);
		
		
		if ( operacao.equals("desativacao") ) // quando o bloqueio automático for desabilitado
		{
			ArrayList bloqueios = (ArrayList) ConsultaAssinantesBloqueados.findBloqueiosAutomaticosByMsisdn(db, msisdn);
			BloqueioServico bloqueioServico;
			
			for (int i = 0; i < bloqueios.size(); i++)
			{
				bloqueioServico = (BloqueioServico) bloqueios.get(i);
				if ( bloqueioServico.getServico().toLowerCase().equals("fcl")
					|| bloqueioServico.getServico().toLowerCase().equals("idc") )
					ConsultaAssinantesBloqueados.deleteAssinanteBloqueioAutomatico(db, bloqueioServico);
			}
		}
		else if ( operacao.equals("ativacao") ) // quando o bloqueio automático for habilitado
		{
			BloqueioServico bloqueio = new BloqueioServico();
			bloqueio.setMsisdn(msisdn);
			bloqueio.setServico("FCL");
			bloqueio.setDataBloqueio(new Date());			
			// Obtém o usuário logado no portal
			Usuario usuario = (Usuario)request.getSession().getAttribute(Constantes.USUARIO);			
			bloqueio.setUsuario(usuario.getNome());
			ConsultaAssinantesBloqueados.setBloqueioAutomatico(db, bloqueio);
			
			bloqueio = new BloqueioServico();
			bloqueio.setMsisdn(msisdn);
			bloqueio.setServico("IDC");
			bloqueio.setDataBloqueio(new Date());
			bloqueio.setUsuario(usuario.getNome());
			ConsultaAssinantesBloqueados.setBloqueioAutomatico(db, bloqueio);
		}
		
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
