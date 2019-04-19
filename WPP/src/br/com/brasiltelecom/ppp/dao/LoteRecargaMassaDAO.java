package br.com.brasiltelecom.ppp.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import br.com.brasiltelecom.ppp.portal.entity.LoteRecargaMassa;


/**
 * Interface de acesso ao cadastro de <code>LoteRecargaMassa</code>.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 09/08/2007
 */
public class LoteRecargaMassaDAO
{
	/**  
	 * Consulta todos os dados de forma agrupada por lote.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>LoteRecargaMassa</code>.
	 */
	public static List findAllGroupByLote(Session session)
	{
		SQLQuery query = session.createSQLQuery(
				"SELECT count(*), sum(a.vlr_credito_bonus), sum(a.vlr_credito_sms), sum(a.vlr_credito_gprs)," +
				"       a.idt_status_processamento, a.id_usuario, a.num_lote, a.dat_processamento " +
				"FROM TBL_INT_SOLICITACAO_RECARGA a " +
				"GROUP BY a.num_lote, a.idt_status_processamento, a.id_usuario, a.dat_processamento " +
				"ORDER BY a.idt_status_processamento ASC, a.dat_processamento DESC, a.num_lote ASC ");
		
		query.addScalar("count(*)", Hibernate.INTEGER);
		query.addScalar("sum(a.vlr_credito_bonus)");
		query.addScalar("sum(a.vlr_credito_sms)");
		query.addScalar("sum(a.vlr_credito_gprs)");
		query.addScalar("idt_status_processamento", Hibernate.INTEGER);
		query.addScalar("id_usuario");
		query.addScalar("num_lote");
		query.addScalar("dat_processamento", Hibernate.TIMESTAMP);
		
		
		ArrayList list = new ArrayList();
		
		for (Iterator it = query.list().iterator(); it.hasNext();) 
		{
	        Object[] row = (Object[]) it.next();
	        if (row == null) return list;
	        
	        LoteRecargaMassa lote = new LoteRecargaMassa();
	        
	        lote.setTotalRegistros(((Integer)row[0]).intValue());
	        lote.setTotalVlrBonus(((BigDecimal)row[1]).doubleValue());
	        lote.setTotalVlrDados(((BigDecimal)row[2]).doubleValue());
	        lote.setTotalVlrSm(((BigDecimal)row[3]).doubleValue());
	        lote.setStatusProcessamento(((Integer)row[4]).intValue());
	        lote.setUsuario((String)row[5]);
	        lote.setNumLote((String)row[6]);
	        lote.setDataProcessamento((Date)row[7]);
	        
	        list.add(lote);	        
	    }
		
		return list;
	}
	
}