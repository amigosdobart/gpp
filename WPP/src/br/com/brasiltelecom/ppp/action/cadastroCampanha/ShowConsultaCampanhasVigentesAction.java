package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CampanhaHome;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de campanhas promocionais vigentes
 * 
 * @author Joao Carlos
 * @since 09/03/2006
 */
public class ShowConsultaCampanhasVigentesAction extends ShowAction
{
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela(){
		return "consultaCampanhasVigentes.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception 
	{
		try
		{
			db.begin();
			// Realiza a pesquisa de todas as campanhas vigentes para 
			// a visualizacao do Usuario. Este pode escolher uma campanha
			// para que seja visualizada todos os parametros e condicoes
			// cadastrados
			// Define a data de pesquisa como sendo a data atual.
			// Veja que os valores de segundos,minutos e milisegundos devem
			// ser zerados para que a data de hoje seja incluida na campanha
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.MINUTE     ,0);
			cal.set(Calendar.SECOND     ,0);
			cal.set(Calendar.MILLISECOND,0);
			context.put("campanhas",CampanhaHome.findCampanhasVigentes(cal.getTime(),db));
			db.commit();
		}
		catch(Exception e)
		{
			db.rollback();
		}
		finally
		{
			if (!db.isClosed())
				db.close();
		}
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return Constantes.COD_CONSULTAR_CAMPANHA;
	}

}
