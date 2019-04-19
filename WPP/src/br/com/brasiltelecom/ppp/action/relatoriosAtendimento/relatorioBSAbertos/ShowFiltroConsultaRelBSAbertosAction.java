package br.com.brasiltelecom.ppp.action.relatoriosAtendimento.relatorioBSAbertos;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class ShowFiltroConsultaRelBSAbertosAction extends ShowAction
{

	public String getTela()
	{
		return "filtroConsultaRelBSAbertoS.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{

	}

	public String getOperacao()
	{
		return Constantes.MENU_CONSULTA_BS_ABERTOS;
	}
}
