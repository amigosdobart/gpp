package br.com.brasiltelecom.ppp.action.consultaStatusRecargaRecorrente;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de passagem de parâmetros
 * @author	Marcelo Alves Araujo
 * @since	13/04/2006
 *
 */
public class ShowFiltroCnsultaStatusRecargaRecorrente extends ShowAction
{

	public String getTela()
	{
		return "filtroConsultaStatusRecargaRecorrente.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception
	{
		// Somente chama a tela de apresentação sem passar parâmetros
	}

	public String getOperacao()
	{
		return Constantes.MENU_RECARGA_RECORRENTE;
	}

}
