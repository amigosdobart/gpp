package br.com.brasiltelecom.ppp.action.enviaSMSviaTangram;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;

/**
 * Mostra a tela de consulta de recargas/ajustes
 * 
 * @author Jorge Abreu
 * @since 10/10/2007
 */
public class ShowFiltroConsultaSMSviaTangram extends ShowActionHibernate 
{	    
	private String codOperacao = Constantes.MENU_ENVIA_SMS_TANGRAM;	
	Logger logger = Logger.getLogger(this.getClass());
	    
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaSMSviaTangram.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception 
	{
		
		context.put("dataInicial",request.getParameter("dataInicial"));
		context.put("dataFinal",request.getParameter("dataFinal"));
		context.put("msisdn",request.getParameter("msisdn"));
		context.put("origem",request.getParameter("origem"));
		
		TreeMenu menu         = new TreeMenu("MenuParametros");
		GrupoItem grupoStatus = new GrupoItem("Todos");
		grupoStatus.addItem(new OpcaoItem("Enviada","enviada","",false));
		grupoStatus.addItem(new OpcaoItem("Entregue","entregue","",false));
		grupoStatus.addItem(new OpcaoItem("Nao Entregue","naoentregue","",false));
		grupoStatus.addItem(new OpcaoItem("Expirada","expirada","",false));
		menu.addItem(grupoStatus);
		context.put("menuParametros", menu.escapeToJS(menu.toXML()));
		
	}		

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}