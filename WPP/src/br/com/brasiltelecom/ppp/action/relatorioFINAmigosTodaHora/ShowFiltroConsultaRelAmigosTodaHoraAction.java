/*
 * Created on 12/05/2005
 *
 */
package br.com.brasiltelecom.ppp.action.relatorioFINAmigosTodaHora;

import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;

/**
 * Mostra a tela de Filtro de Relatorio de consumo de saldo do clientes do Amigos Toda Hora 
 * 
 * @author Marcelo Alves Araujo
 * @since 08/06/2005
 */
public class ShowFiltroConsultaRelAmigosTodaHoraAction extends ShowAction 
{

    private String codOperacao = Constantes.MENU_FIN_AMIGOS_TH;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelFINAmigosTodaHora.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, Database db) throws Exception 
	{
		try
		{
			db.begin();
			
			//Pesquisa pelos Codigos Nacionais validos para geracao do filtro na pagina de apresentacao
			Collection codigosNacionais = CodigoNacionalHome.findAllBrt(db);
			context.put("codigosNacionais", codigosNacionais);
			
			//Inserindo os meses disponiveis para a pesquisa
			Calendar calMeses = Calendar.getInstance();
			SimpleDateFormat conversorData = new SimpleDateFormat("MMM/yyyy");
			for(int i = 0; i <= 5; i++)
			{
				context.put("mes" + String.valueOf(i), conversorData.format(calMeses.getTime()));
				calMeses.add(Calendar.MONTH, -1);
			}
		}
		catch (PersistenceException e1) 
		{
			logger.error("Não foi possível realizar a consulta pelos dados de filtros, problemas na conexão com o banco de dados (" +
						e1.getMessage() + ")");
		}
		
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}

}