/* * Created on 27/09/2005
 */
package br.com.brasiltelecom.ppp.action.consultaCreditoPulaPula;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.PromocaoHome;

/**
 * @author Marcos C. Magalhães
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowFiltroConsultaCreditoPulaPulaAction extends ShowAction 
{
	
	public String getTela() 
	{
		return "filtroConsultaCreditoPulaPula.vm";

	}
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception 
	{
		
		//db.begin();
		//Collection listaPromocoes		= null;
		//listaPromocoes = PromocaoHome.findAllPulaPulaAndPendenteRecarga(db);
		//listaPromocoes = PromocaoHome.findAllPulaPulaPrePago(db);
		//context.put("listaPromocoes", listaPromocoes);
	}

	public String getOperacao() 
	{
		return null;
	}
}
