/**
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaComparativoPlano;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;

/**
 * Action para as consultas aos comparativos de planos
 * 
 * @author Osvaldeir
 * @since 7.0 Data: 19/02/2008 Hora: 15:27:42
 */
public class ComparativoPlanosAction extends DispatchAction
{
	private static Logger logger = Logger.getLogger(ComparativoPlanosAction.class);
	
	private String[] meses = { "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO",
			"JULHO", "AGOSTO", "SETEMBRO", "OUTRUBRO", "NOVEMBRO", "DEZEMBRO" };
	
    protected static DecimalFormat formatoDecimal           = new DecimalFormat("#,##0.00",
            new DecimalFormatSymbols(new Locale("pt","BR")));

	
	public ActionForward showFiltraClientePorMSISDN(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{

		ShowActionHibernate action = new ShowActionHibernate()
		{

			public void updateVelocityContext(VelocityContext context,
					HttpServletRequest request, SessionFactory sessionFactory)
					throws Exception
			{

			}

			public String getTela()
			{
				return "filtroConsultaComparativoPlanosAssinante.vm";
			}

			public String getOperacao()
			{
				return "MENU_COMPARATIVO_PLANOS";
			}

		};
		action.setServlet(this.getServlet());
		return action.execute(mapping, form, request, response);
	}

	public ActionForward showResultadoComparativoPlanos(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		ShowActionHibernate action = new ShowActionHibernate()
		{

			public void updateVelocityContext(VelocityContext context,
					HttpServletRequest request, SessionFactory sessionFactory)
					throws Exception
			{
				Session session = null;
				
				try 
				{
					session = sessionFactory.getCurrentSession();
			        session.beginTransaction();
			        
                    String msisdn = "55" + request.getParameter("msisdn");
					GrupoSumarioServicoDAO dao = new GrupoSumarioServicoDAO(msisdn, session);
					context.put("servicos", dao.findSumarioServicoBrt());
                    context.put("chamadas", dao.findSumarioChamadasRoaming());
                    context.put("dados", dao.findSumarioDadosEvento());
                    context.put("meses", dao.getMeses());
                    context.put("df", formatoDecimal);
					
					session.getTransaction().commit();		
				}
				catch (Exception e)
				{
					context.put("erro", "Erro ao realizar a consulta. " + e);
					logger.error("Erro ao realizar a consulta. " + e);	
					if (session != null && session.isOpen()) 
						session.getTransaction().rollback();
				}		
			}

			public String getTela()
			{
				return "mostrarComparativoPlanos.vm";
			}

			public String getOperacao()
			{
				return "COMPARAR_PLANOS";
			}

		};
		action.setServlet(this.getServlet());
		return action.execute(mapping, form, request, response);
	}

}
