package br.com.brasiltelecom.ppp.action.consultaBlackList;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de passagem de parâmetros
 * @author	Geraldo Palmeira
 * @since	03/01/2007
 *
 */
public class ShowFiltroConsultaBlackListAction extends ShowAction
{

	public String getTela()
	{
		return "filtroConsultaBlackList.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		
	}

	public String getOperacao()
	{
		return Constantes.COD_ATUALIZACAO_BLACK_LIST;
	}
}
