/*
 * Created on 24/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.ajusteCreditoBancario;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.CanalDAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.dao.ValorRecargaDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Canal;

/**
 * Mostra a tela de ajuste de crédito bancario
 * 
 * @author Anderson Jefferson Cerqueira
 * @since 12/02/2008
 */
public class ShowFiltroAjusteCreditoBancarioAction extends ShowActionHibernate 
{
	private String codOperacao = Constantes.MENU_AJUSTE_CREDITO_BANCARIO;
	Logger logger = Logger.getLogger(this.getClass());
    
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
    {
		return "filtroAjusteCreditoBancario.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
    {
		return codOperacao;
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, 
            SessionFactory sessionFactory) throws Exception
	{
		Session sessionHibernate = null;
		try 
		{
			// Inicia a sessão do hibernate
			sessionHibernate = sessionFactory.getCurrentSession();
			sessionHibernate.beginTransaction();
			
			Collection comboRestrito = new ArrayList();
			Canal combo = new Canal();
			// Carrega a combo canal de recarga com as opções 00-BANCO e 11-BANCO FIXA
			combo = CanalDAO.findByIdCanal(sessionHibernate, "00");
			comboRestrito.add(combo);
			combo = CanalDAO.findByIdCanal(sessionHibernate, "11");
			comboRestrito.add(combo);
			
			context.put("canais",comboRestrito);
						
			// Carrega a lista de valores possíveis para a combo valor de crédito
			context.put("obr_valor", ValorRecargaDAO.findVigentes(sessionHibernate));			
		
			if (request.getParameter("canalRecarga") != null 
                    && !request.getParameter("canalRecarga").equals(""))
			{
				context.put("origens",OrigemRecargaDAO.findByIdCanal(sessionHibernate, request.getParameter("canalRecarga")));
				context.put("origemRecarga",request.getParameter("origemRecarga"));
				context.put("canalRecarga",request.getParameter("canalRecarga"));
				context.put("obr_msisdn",request.getParameter("obr_msisdn"));
				context.put("msisdn",request.getParameter("msisdn"));
				context.put("obr_nsu",request.getParameter("obr_nsu"));
			}
			
			context.put("df", new DecimalFormat("R$ ##,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR"))));
			sessionHibernate.getTransaction().rollback();
		}
		catch (Exception e) 
		{
            context.put("erro", "Erro ao abrir filtro de ajuste de crédito bancário. " + e);
			logger.error("Erro ao abrir filtro de ajuste de credito bancario. " + e);
			if (sessionHibernate != null) 
				sessionHibernate.getTransaction().rollback();
		}	
	}
}
