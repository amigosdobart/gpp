package br.com.brasiltelecom.ppp.action.origemCDR;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.RatingDao;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Rating;

/**
 * Mostra a p�gina de consulta de Origem dos CDR (resumo por Ajax).
 * 
 * @author Lucas Mind�llo de Andrade
 * Criado em: 16/04/2008
 */
public class ShowDetalhesOrigemCDR extends ShowActionHibernate
{	
	private String codOperacao = Constantes.MENU_ORIGEM_CDR;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "detalhesOrigemCDR.vm";
	}
	
	/**
	 * Cabe�alhos para requisi��o AJAX
	 */
	public ActionForward performPortal(
	        ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response, Database db)throws Exception
	{
		//response.setContentType("text/xml");
		response.setHeader( "Pragma", "no-cache" );
		response.addHeader( "Cache-Control", "must-revalidate" );
		response.addHeader( "Cache-Control", "no-cache" );
		response.addHeader( "Cache-Control", "no-store" );
		response.setDateHeader("Expires", 0);
		return super.performPortal(actionMapping,actionForm,request,response,db);
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	  
	        String origem = request.getParameter("origem");
	        
	        Rating origemCDR = RatingDao.findByRateName(session, origem);
	        context.put("origemCDR", origemCDR);
	        context.put("formatter", new DecimalFormat(Constantes.DOUBLE_FORMATO, new DecimalFormatSymbols(new Locale("pt", "BR", ""))));
	        
	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao consultar tipo de chamada. <br><br>" + e.getMessage());
			logger.error("Erro ao consultar tipo de chamada. " + e);	
			if (session != null) 
				session.getTransaction().rollback();
		}
	}
	
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}

 
