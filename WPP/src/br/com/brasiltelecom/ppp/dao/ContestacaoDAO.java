package br.com.brasiltelecom.ppp.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.brt.gpp.comum.mapeamentos.entidade.Contestacao;

/**
 * DAO para consulta de Contestacao.
 * 
 * @author Bernardo Dias
 * Data: 14/02/2008
 */
public class ContestacaoDAO
{
    /**
     * Consulta uma instância de Contestacao a partir do BS
     * 
     * @param session               Sessão do Hibernate.
     * @param numBS                 Numero do BS
     * @return                      Instância de <code>Contestacao</code>.
     */
    public static Contestacao findByNumBS(Session session, String numBS)
    {
        return (Contestacao)session.get(com.brt.gpp.comum.mapeamentos.entidade.Contestacao.class, numBS);
    }

	/**  
	 * Consulta uma lista de Contestacoes de acordo com os criterios.
	 *   
	 * @param session 				Sessão do Hibernate.
     * @param msisdn                Msisdn [opcional].
     * @param dataAberturaInicio    Data inical (inclusivo) [opcional].
     * @param dataAberturaFim       Data final (exclusivo) [opcional].
	 * @return 						Coleção de <code>ContestacaoChamadas</code>.
	 */
	public static List findByMsisdnDataAbertura(Session session, String msisdn, 
            Date dataAberturaInicio, Date dataAberturaFim)
	{
        Criteria criteria = session.createCriteria(com.brt.gpp.comum.mapeamentos.entidade.Contestacao.class);
        
        if (msisdn != null)     criteria.add(Restrictions.eq("idtMsisdn", msisdn));
        if (dataAberturaInicio != null) criteria.add(Restrictions.ge("dataAbertura", dataAberturaInicio));
        if (dataAberturaFim != null)    criteria.add(Restrictions.lt("dataAbertura", dataAberturaFim));
        
        criteria.addOrder(Order.asc("dataAbertura"));
        
        return criteria.list();
    }
    
    /**  
     * Consulta uma lista de Contestacao de acordo com os criterios.
     *   
     * @param session               Sessão do Hibernate.
     * @param msisdn                Msisdn [opcional].
     * @param numeroBS              Numero do BS [opcional]
     * @param dataAberturaInicio    Data inical (inclusivo) [opcional].
     * @param dataAberturaFim       Data final (exclusivo) [opcional].
     * @return                      Coleção de <code>Contestacao</code>.
     */
    public static List findByCaracteristicas(Session session, String msisdn, 
            String numeroBS, Date dataAberturaInicio, Date dataAberturaFim)
    {
        Criteria criteria = session.createCriteria(com.brt.gpp.comum.mapeamentos.entidade.Contestacao.class);
        
        if (msisdn != null)     criteria.add(Restrictions.eq("idtMsisdn", msisdn));
        if (numeroBS != null)   criteria.add(Restrictions.eq("numeroBS", numeroBS));
        if (dataAberturaInicio != null) criteria.add(Restrictions.ge("dataAbertura", dataAberturaInicio));
        if (dataAberturaFim != null)    criteria.add(Restrictions.lt("dataAbertura", dataAberturaFim));
        
        criteria.addOrder(Order.desc("dataAbertura"));
        
        return criteria.list();
    }
    
    /**
     * Inclui uma Contestacao.
     * 
     * @param session           Sessão do Hibernate.
     * @param contestacao       Entidade <code>Contestacao</code>.
     */
    public static void incluirContestacao(Session session, Contestacao contestacao) 
    {   
        session.save(contestacao);
    }
    
    
}