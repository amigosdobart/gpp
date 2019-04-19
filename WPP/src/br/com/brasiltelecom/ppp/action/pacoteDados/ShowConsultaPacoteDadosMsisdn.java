package br.com.brasiltelecom.ppp.action.pacoteDados;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteOfertaPacoteDadosDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta de Pacote de Dados por Msisdn
 * 
 * @author Bernardo Vergne Dias
 * Data: 16/10/2007
 */
public class ShowConsultaPacoteDadosMsisdn extends ShowActionHibernate 
{
	private String codOperacao = Constantes.COD_CONS_ASS_PCT_DADOS;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaPacoteDadosMsisdn.vm";
	}
	
	/**
	 * Implementa a logica de negocio dessa acao.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisicao HTTP que originou a execucao dessa acao.
	 * @param sessionFactory	Factory de sessoes para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
									  HttpServletRequest request,
									  SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        String msisdn = request.getParameter("msisdn");
	        context.put("msisdn",msisdn);
	        
	        if (msisdn != null)
			{
	        	// Filtra os ultimos 6 meses
	        	Calendar cal = Calendar.getInstance();
	        	cal.add(Calendar.MONTH, -6);
	        	
				Collection resultado = AssinanteOfertaPacoteDadosDAO.findByMsisdnDataCadastro(session, "55" + msisdn, cal.getTime());
				
				if (resultado != null && resultado.size() > 0)
				{
					context.put("resultado", resultado);
					context.put("sdf", new SimpleDateFormat("dd/MM/yyyy"));
					context.put("sdf2", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
                    context.put("action", new ShowConsultaPacoteDadosMsisdn());
				}
				else
					context.put("mensagem","Nenhum resultado encontrado");
			}
			
			session.getTransaction().commit();	
		}
		catch (Exception e)
		{
			context.put("mensagem", "[erro]Não foi possível realizar a consulta de Pacotes de Dados. " + e.getMessage());
			logger.error("Não foi possível realizar a consulta de Pacotes de Dados. " + e);	
			
			if (session != null && session.isOpen()) 
				session.getTransaction().rollback();
		}
	}
    
    /**
     * Calcula a data prevista para retirada da promoção Pacote de Dados
     * para uma determinada oferta e uma determinada data de contratação
     */
    public static Date getDataPrevisaoRetirada(AssinanteOfertaPacoteDados assinanteOferta)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(assinanteOferta.getDataContratacao());
        cal.add(Calendar.DAY_OF_MONTH, assinanteOferta.getOfertaPacoteDados().getPacoteDados().getNumDias());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        
        return cal.getTime();
    }
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}