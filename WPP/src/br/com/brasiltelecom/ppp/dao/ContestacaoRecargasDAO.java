package br.com.brasiltelecom.ppp.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * DAO para consulta da view ContestacaoRecargas.
 * 
 * @author Bernardo Dias
 * Data: 31/01/2008
 */
public class ContestacaoRecargasDAO
{
	
	/**  
	 * Consulta uma lista de ContestacaoRecargas de acordo com os criterios.
	 *   
	 * @param session 				Sessão do Hibernate.
     * @param msisdn                Msisdn [opcional].
     * @param numeroBS              Numero do BS [opcional]
     * @param dataInicio            Data inical (inclusivo) [opcional].
     * @param dataFim               Data final (exclusivo) [opcional].
     * @param contestadas           Somente chamadas contestadas.
	 * @return 						Coleção de <code>ContestacaoRecargas</code>.
	 */
	public static List findByCaracteristicas(Session session, String msisdn, 
            String numeroBS, Date dataInicio, Date dataFim, boolean contestadas)
	{
        Criteria criteria = session.createCriteria(com.brt.gpp.comum.mapeamentos.entidade.ContestacaoRecargas.class);
        
        if (msisdn != null)     criteria.add(Restrictions.eq("subId", msisdn));
        if (numeroBS != null)   criteria.add(Restrictions.eq("contestacao.numeroBS", numeroBS));
        if (dataInicio != null) criteria.add(Restrictions.ge("timestamp", dataInicio));
        if (dataFim != null)    criteria.add(Restrictions.lt("timestamp", dataFim));
        if (contestadas)        criteria.add(Restrictions.isNotNull("idItemContestacao"));
        
        criteria.addOrder(Order.desc("timestamp"));
        
        return criteria.list();
    }
}