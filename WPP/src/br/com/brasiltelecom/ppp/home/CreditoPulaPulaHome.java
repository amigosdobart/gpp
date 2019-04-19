/*
 * Created on 21/02/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * 
 */
package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.CastorResultObject;


/**
 * 
 * @author 	Daniel Ferreira
 * @since 	21/02/2005
 */
public class CreditoPulaPulaHome 
{
	/**  
	 *   Método estático responsável pela localização de uma Promocao, atraves de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Promocao com todas suas propriedades e funcionalidades.
	 */

	public static CastorResultObject findByParam(Database db, String datMes, String msisdn, int promo) throws PersistenceException 
	{

			OQLQuery query = null;
			CastorResultObject result = null;
			QueryResults rs = null;
			
			try
			{

				query = db.getOQLQuery(	"CALL SQL SELECT " +
										" cont as id, " +
										" nome_promocao as field00,  " +
										" count(*) as field01,  " +
										" to_char(sum(regra_ff), '999999.99') as field02, " + 
										" null as field03, " +
										" null as field04, " +
										" null as field05, " +
										" null as field06, " +
										" null as field07, " +
										" null as field08, " +
										" null as field09, " +
										" null as field10, " +
										" null as field11, " +
										" null as field12, " +
										" null as field13, " +
										" null as field14, " +
										" null as field15, " +
										" null as field16, " +
										" null as field17, " +
										" null as field18, " +
										" null as field19 " +
										" FROM " +
										" ( SELECT " +
										" c.rowid AS cont, " +
										" b.nom_promocao AS nome_promocao, " +
										" ((((c.num_segundos_total - c.num_segundos_ff)/60) * d.vlr_bonus_minuto) +" +
										" ((c.num_segundos_ff/60) * d.vlr_bonus_minuto_ff)) AS regra_ff " +
										" FROM " +
										" TBL_PRO_PROMOCAO B," +
										" TBL_PRO_TOTALIZACAO_PULA_PULA C, " +
										" (SELECT DISTINCT idt_codigo_nacional,vlr_bonus_minuto_ff,vlr_bonus_minuto " +
										" FROM tbl_pro_bonus_pula_pula pbpp, tbl_ger_plano_preco gpp " +
										" WHERE pbpp.idt_plano_preco = gpp.idt_plano_preco " +
										" AND gpp.idt_categoria = 0" +
										" AND pbpp.dat_ini_periodo < SYSDATE" +
										" AND NVL (pbpp.dat_fim_periodo, SYSDATE) >= SYSDATE) d " +
										" WHERE " +
										" C.dat_mes = $1 " +
										" AND d.idt_codigo_nacional = substr(c.idt_msisdn, 3, 2) " +
										" AND b.idt_promocao = $2 " +
										" AND c.idt_msisdn = $3 " +
										"  ) " +
										" GROUP BY " +
										" nome_promocao, " +
										" cont AS br.com.brasiltelecom.ppp.portal.entity.CastorResultObject " );

				query.bind(datMes);
        		query.bind(promo);
        		query.bind(msisdn);
        		rs = query.execute();
				
				if(rs.hasMore())
				{
					result = (CastorResultObject) rs.next();
				}
			
			}
			finally
			{
				if(rs != null) 
				{
					rs.close();
				}
				if(query != null)
				{
					query.close();	
				}	
			}
			
			
			return result;
	}
}