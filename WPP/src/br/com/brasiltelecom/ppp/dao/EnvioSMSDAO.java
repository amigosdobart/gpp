package br.com.brasiltelecom.ppp.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;

/**
 * Interface de acesso ao cadastro de EnvioSMS (entidade DadosSMS).
 * 
 * @author Bernardo Dias
 * Data: 10/12/2007
 */
public class EnvioSMSDAO
{
	
    /**  
     * Consulta todos os dados cadastrados.
     *   
     * @param session               Sessão do Hibernate.
     * @return                      Coleção de <code>DadosSMS</code>.
     */
    public static List findAll(Session session)
    {
        Query query = session.createQuery(
                "FROM com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS a " +
                "ORDER BY a.idRegistro");  
        return query.list();
    }
    
    /**
     * Consulta uma instância de DadosSMS a partir do idRegistro
     * 
     * @param session               Sessão do Hibernate.
     * @param idRegistro            ID do SMS
     * @return                      Instância de <code>DadosSMS</code>.
     */
    public static DadosSMS findById(Session session, long idRegistro)
    {
        return (DadosSMS)session.get(com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS.class, new Long(idRegistro));
    }
    
    
	/**  
	 * Consulta todos os casos a partir de um determinada data de envio, para um dado msisdn destino e tipo de SMS.
	 *   
	 * @param session 				Sessão do Hibernate.
     * @param msisdn                MSISDN de destino.
     * @param dataEnvio             Data agendada de envio.
     * @param tipoSMS               Instância de <code>TipoSMS</code>.
	 * @return 						Coleção de <code>DadosSMS</code>.
	 */
	public static List findByCriterios(Session session, String msisdn, Date dataEnvio, TipoSMS tipoSMS)
	{		
		Query query = session.createQuery(
				"FROM com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS a " +
				"WHERE a.idtMsisdn = ? AND a.datEnvioSMS >= ? AND a.tipoSMS = ? " +
				"ORDER BY a.datEnvioSMS DESC, a.datProcessamento DESC ");	
		
		query.setString(0, msisdn);
		query.setDate(1, dataEnvio);
        query.setEntity(2, tipoSMS);
		
		return query.list();
	}
}