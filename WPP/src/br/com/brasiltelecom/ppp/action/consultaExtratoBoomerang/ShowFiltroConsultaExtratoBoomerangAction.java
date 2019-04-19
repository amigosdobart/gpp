package br.com.brasiltelecom.ppp.action.consultaExtratoBoomerang;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;

/**
 * 
 * @author Henrique Canto
 * 
 */
public class ShowFiltroConsultaExtratoBoomerangAction extends ShowAction {


	public String getTela() {

		return "filtroConsultaExtratoBoomerang.vm";
	}


	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception {
		
		SimpleDateFormat df = new SimpleDateFormat("MMM/yyyy");
		Calendar cal = Calendar.getInstance();
		context.put("mes0", (String)df.format(cal.getTime()));
		for (int i=1 ; i<=5 ;i++)
		{
			cal.add(Calendar.MONTH,-1);
			context.put("mes"+i, (String)df.format(cal.getTime()));
		}

	}


	public String getOperacao() {
		return null;
	}

}
