package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;

/**
 * Interface de acesso ao cadastro de TipoSMS.
 * 
 * @author Bernardo Dias
 * Data: 16/01/2008
 */
public class TipoSMSDAO
{	
    /**  
     * Consulta todos os dados cadastrados.
     *   
     * @param session               Sessão do Hibernate.
     * @return                      Coleção de <code>TipoSMS</code>.
     */
    public static List findAll(Session session)
    {
        Query query = session.createQuery(
                "FROM com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS a " +
                "ORDER BY a.idtTipoSMS");  
        return query.list();
    }
    
    /**
     * Consulta uma instância de TipoSMS a partir do idtTipoSMS
     * 
     * @param session               Sessão do Hibernate.
     * @param idtTipoSMS            ID do Tipo de SMS
     * @return                      Instância de <code>TipoSMS</code>.
     */
    public static TipoSMS findById(Session session, String idtTipoSMS)
    {
        return (TipoSMS)session.get(com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS.class, idtTipoSMS);
    }
}