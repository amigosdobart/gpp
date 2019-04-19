package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.ValoresRecargasHome;
import br.com.brasiltelecom.ppp.home.CampanhaHome;
import br.com.brasiltelecom.ppp.portal.entity.Campanha;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de cadastro das campanhas promocionais
 * 
 * @author Joao Carlos
 * @since 14/02/2006
 */
public class ShowCadCampanhaIncentRecargaAction extends ShowAction
{
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela(){
		return "cadastroIncentivoRecargas.vm";
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
			// Verifica se o parametro do ID da campanha foi passado como parametro
			// em caso verdadeiro entao tenta realizar a pesquisa da campanha 
			// cadastrada no banco de dados e insere no contexto para ser visualizada
			// pelo usuario
			String idCampanha = request.getParameter("idCampanha");
			if (idCampanha != null)
			{
				Campanha campanha = CampanhaHome.findById(Long.parseLong(idCampanha),db);
				context.put("campanha",campanha);
				context.put("paramsIncentivo",CampanhaHome.findParamsIncentivoRecargas(campanha,db));
				context.put("condsIncentivo" ,CampanhaHome.findCondsIncentivoRecargas(campanha,db));
			}
			// Realiza a pesquisa de valores de recargas
			// para popular combo box com os valores para que 
			// o usuario possa escolher somente os valores
			// que podem ser utilizados em recargas (valores de face)
			context.put("valoresRecargas",ValoresRecargasHome.findValoresFace(db));
			
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
		return Constantes.COD_CADASTRAR_CAMPANHA;
	}

}
