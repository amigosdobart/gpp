package br.com.brasiltelecom.ppp.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;

/**
 * Interface de acesso ao cadastro de Origem de Recarga.
 * 
 * @author Geraldo Palmeira
 * Criado em: 15/06/2007
 * 
 * Alterado por Bernardo Dias
 * Data: 29/09/2007
 */
public class OrigemRecargaDAO
{
	
	/**  
	 * Consulta todos os dados cadastrados.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>OrigemRecarga</code>.
	 */
	public static List findAll(Session session)
	{
		Query query = session.createQuery(
				"FROM com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga a " +
				"ORDER BY a.idCanal ASC, a.idOrigem  ASC");	
		
		return query.list();
	}
	
	/**
	 * Consulta uma coleção de OrigemRecarga a partir do tipLancamento
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param tipLancamento 		tipLancamento
	 * @return 						Coleção de <code>OrigemRecarga</code>.
	 */
	public static List findByTipLancamento(Session session, String tipLancamento)
	{
		Query query = session.createQuery(
		" FROM com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga a" +
		" WHERE a.tipLancamento = ? " );
		query.setString(0, tipLancamento);
		return query.list();
	}
	
    /**
     * Consulta uma coleção das Origens de Recarga disponiveis para novo item de contestacao
     * 
     * @param session               Sessão do Hibernate.
     * @return                      Coleção de <code>OrigemRecarga</code>.
     */
    public static List findAllDispNovoItemConestacao(Session session)
    {
        Query query = session.createQuery(
        " FROM com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga a" +
        " WHERE a.indDispNovoItemContestacao = ? " +
        " ORDER BY a.idCanal, a.idOrigem " );
        query.setBoolean(0, true);
        
        return query.list();
    }
    
	/**
	 * Consulta uma coleção de OrigemRecarga a partir do idCanal
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param idCanal 				idCanal
	 * @return 						Coleção de <code>OrigemRecarga</code>.
	 */
	public static List findByIdCanal(Session session, String idCanal)
	{
		Query query = session.createQuery(
				" FROM com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga a" +
				" WHERE a.idCanal = ? " +
				" ORDER BY a.idCanal ASC, a.idOrigem  ASC " );
				query.setString(0, idCanal);
		
		return query.list();
	}
	
	/**
	 * Consulta uma OrigemRecarga a partir do idCanal e idOrigem
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param idCanal 				idCanal
	 * @param idOrigem 				idOrigem
	 * @return 						Instancia de <code>OrigemRecarga</code>.
	 */
	public static OrigemRecarga findById(Session session, String idCanal, String idOrigem)
	{
		Query query = session.createQuery(
				" FROM com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga a" +
				" WHERE a.idCanal = ? AND a.idOrigem = ? " );
				query.setString(0, idCanal);
				query.setString(1, idOrigem);
				
		return (OrigemRecarga)query.uniqueResult();
	}
	
	/**
	 * Inclui uma origem.
	 * 
	 * @param session 			Sessão do Hibernate.
	 * @param origem 			Entidade <code>OrigemRecarga</code>.
	 */
	public static void incluirOrigem(Session session, OrigemRecarga origem) 
	{	
		session.save(origem);
	}
	
	/**
	 * Remove uma origem.
	 * 
	 * @param session 			Session session
	 * @param origem 			Entidade <code>OrigemRecarga</code>.
	 */
	public static void removerOrigem(Session session, OrigemRecarga origem) 
	{	
		session.delete(origem);
	}
	
	/**
	 * Consulta a lista de classificacoes existentes no banco
	 * (mesmo que distinc idtClassificacaoRecarga)
	 * 
	 * @param session			Session session
	 * @return	ArrayList contendo strings (idtClassificacaoRecarga)
	 */
	public static ArrayList findListaClassificacao(Session session)
	{
		SQLQuery query = session.createSQLQuery(
				"SELECT distinct a.idt_Classificacao_Recarga as classificacao " +
				"FROM TBL_REC_ORIGEM a " +
				"ORDER BY a.idt_Classificacao_Recarga ASC ");
		
		query.addScalar("classificacao", Hibernate.STRING);
		ArrayList list = new ArrayList();
		
		for (Iterator it = query.list().iterator(); it.hasNext();) 
		{ 
			String item = (String) it.next();
			if (item != null)
				list.add(item);	        
	    }
		
		return list;
	}
}