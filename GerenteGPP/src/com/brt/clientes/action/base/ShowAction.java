
package com.brt.clientes.action.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.brt.clientes.interfacegpp.GerenteORB;

/**
 * Método principal para mostrar os dados na tela
 * @author Alex Pitacci Simões
 * @since 31/05/2004
 */
public abstract class ShowAction extends ActionPortal {

	/**
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Template template = Velocity.getTemplate(getTela());
		VelocityContext vctx = new VelocityContext();
		updateVelocityContext(vctx,request,form);

		vctx.put("servidor", GerenteORB.getOrbServer(GerenteORB.getORBinUse()));
		vctx.put("porta", GerenteORB.getOrbPort(GerenteORB.getORBinUse()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		vctx.put("dataCabecalho",sdf.format(new Date()));

		vctx.put("tipoDeploy",  (String)servlet.getServletContext().getAttribute("TIPO_DEPLOY"));
		vctx.put("versaoDeploy",(String)servlet.getServletContext().getAttribute("VERSAO_DEPLOY"));
		vctx.put("buildDeploy", (String)servlet.getServletContext().getAttribute("BUILD_DEPLOY"));

		template.merge(vctx, response.getWriter());
		
		
		return null;
	}
	
	public abstract void updateVelocityContext(VelocityContext vctx, HttpServletRequest request, ActionForm form);
	public abstract String getTela();
}
