package br.com.brasiltelecom.ppp.dao;

import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao;

/**
 * DAO para consulta de Item de Contestacao.
 * 
 * @author Bernardo Dias
 * Data: 14/02/2008
 */
public class ItemContestacaoDAO
{
    /**
     * Consulta uma instância de ItemContestacao a partir do ID
     * 
     * @param session               Sessão do Hibernate.
     * @param id                 ID do item de contestacao
     * @return                      Instância de <code>ItemContestacao</code>.
     */
    public static ItemContestacao findById(Session session, int id)
    {
        return (ItemContestacao)session.get(com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao.class, new Integer(id));
    }
    
    /**
     * Retorna o proximo valor da sequence SEQ_ITEM_CONTESTACAO
     * 
     * @param session           Sessão do Hibernate.
     * @return                  ID do item.
     */
    public static long getIdItemContestacao(Session session) 
    {   
        
        SQLQuery query = session.createSQLQuery(
                "select SEQ_ITEM_CONTESTACAO.nextval as idproc from dual");
        
        query.addScalar("idproc", Hibernate.LONG);          
        Iterator it = query.list().iterator();
        return ((Long)it.next()).longValue();        
    }
    
    /**
     * Inclui um ItemContestacao.
     * 
     * @param session           Sessão do Hibernate.
     * @param item              Entidade <code>ItemContestacao</code>.
     */
    public static void incluirItem(Session session, ItemContestacao item) 
    {   
        // Necessario informar o nome da entidade, pois o hibernate pode fazer confusao com instancias
        // filhas (como ContestacaoChamadas, ContestacaoRecargas)
        session.save("com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao", item);
    }
    
    
}