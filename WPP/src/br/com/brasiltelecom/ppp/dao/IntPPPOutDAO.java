package br.com.brasiltelecom.ppp.dao;

import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.IntPPPOut;

/**
 * DAO para consulta de IntPPPOut.
 * 
 * @author Bernardo Dias
 * Data: 14/02/2008
 */
public class IntPPPOutDAO
{
    /**
     * Retorna o proximo valor da sequence SEQ_ID_PROCESSAMENTO
     * 
     * @param session           Sessão do Hibernate.
     * @return                  ID de processamento.
     */
    public static long getIdProcessamento(Session session) 
    {   
        
        SQLQuery query = session.createSQLQuery(
                "select seq_id_processamento.nextval as idproc from dual");
        
        query.addScalar("idproc", Hibernate.LONG);  
        
        Iterator it = query.list().iterator();
        Long idProcessamento = (Long)it.next();  
        
        return idProcessamento.longValue();        
    }
    
    /**
     * Inclui um IntPPPOut.
     * 
     * @param session           Sessão do Hibernate.
     * @param obj              Entidade <code>IntPPPOut</code>.
     */
    public static void incluir(Session session, IntPPPOut obj) 
    {   
        session.save(obj);
    }
    
    
}