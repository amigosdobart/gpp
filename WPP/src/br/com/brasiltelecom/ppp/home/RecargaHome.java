/*
 * Created on 25/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Recargas;
import br.com.brasiltelecom.ppp.portal.entity.UsedVoucher;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a recargas
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class RecargaHome {

	/**  
	 *   Método estático responsável pela localização de todos os Chamadas que satisfaçam ao critério de pesquisa informado pelo usuário.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection de Chamadas com todas suas propriedades e funcionalidades.
	 */	
	public static Collection findByFilter(Database db, Map param) {
			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			
			try{
				StringBuffer filtro = new StringBuffer();
				
				if(param.get("msisdn") != null && !"".equals(param.get("msisdn"))){
					
					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}
					if (param.get("msisdn").toString().length() != 10) {
						String ddd = param.get("msisdn").toString().substring(1,3);
						String telefone = param.get("msisdn").toString().substring(4,8) + param.get("msisdn").toString().substring(9, 13);
						filtro.append("a.msisdn = \"55").append(ddd + telefone + "\"");
					} else {
						filtro.append("a.msisdn = \"55").append((String)param.get("msisdn") + "\"");	
					}
					
				}
				
				if(param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo"))){
				  if(param.get("tipoPeriodo").equals("P")) {
				
					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}					
					
					Calendar c = Calendar.getInstance();
				    c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt( (String) param.get("periodo")));
				    Date dataInicial = c.getTime();
				    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				    
					filtro.append(" a.datOrigem between to_date(\"").append(sdf.format(dataInicial)).append(" 00:00:00\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(sdf.format(new Date())).append(" 23:59:59\",\"DD/MM/YYYY hh24:mi:ss\")");
					
					/*				
	                filtro.append("to_date(to_char(a.datOrigem, \"DD/MM/YYYY\"), \"DD/MM/YYYY\") between " + 
					"to_date(to_char((" + dataAtual + " - ").append((String)param.get("periodo")).append("), \"DD/MM/YYYY\"), \"DD/MM/YYYY\") and " +
					"to_date(to_char(" + dataAtual +  ", \"DD/MM/YYYY\"), \"DD/MM/YYYY\")");
					*/
				  }														
				}
				
				if(param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo"))){
					if(param.get("tipoPeriodo").equals("D")) {
						
						if(filtro.length() > 0){
						   filtro.append(" and ");	
						}
						
						filtro.append(" a.datOrigem between to_date(\"").append(param.get("dataInicial")).append(" 00:00:00\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(param.get("dataFinal")).append(" 23:59:59\",\"DD/MM/YYYY hh24:mi:ss\")");
					}
				}
				
				if(param.get("canalRecarga") != null && !"".equals(param.get("canalRecarga"))){

					if(!"TODOS".equals(param.get("canalRecarga")))
					{
						if(filtro.length() > 0)
						{
						   filtro.append(" and ");	
						}
						
						filtro.append("a.idCanal = \"").append((String)param.get("canalRecarga") + "\"");
					}
				}
				
				if(param.get("incluiNaoOk") == null && !"".equals(param.get("incluiNaoOk")))
				{
					if(filtro.length() > 0)
					{
					   filtro.append(" and ");	
					}
					filtro.append("a.status = \"").append("ok" + "\"");
				}
				
				if(param.get("origemRecarga") != null && !"".equals(param.get("origemRecarga"))){

					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}
					
					filtro.append("a.idOrigem = \"").append((String)param.get("origemRecarga") + "\"");
				}	
				if(param.get("nsu") != null && !"".equals(param.get("nsu"))){
					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}
				filtro.append("a.nsuInstituicao = \"").append((String)param.get("nsu")+ "\"");
			}
				
				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.entity.Recargas a ");
								
				
				if(filtro.length() > 0){
					consulta.append("where ").append( filtro.toString() );	
				}
				
				consulta.append(" order by a.datOrigem");	
				
				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				while(rs.hasMore()){
					Recargas r =(Recargas) rs.next();
				    if (r.getIdCanal() != null  && r.getIdOrigem() != null)
				    {
						r.setOrigem(OrigemHome.findByID(db, r.getIdCanal(), r.getIdOrigem()));
					}
					result.add(r);	
                   // result.add((Recargas) rs.next()); (funcionando)					
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(rs != null) rs.close();
				if(query!=null){
					query.close();
				}
			}
			return result;	
	}
	
	/**
	 * Metodo temporario para consulta de cartao ligmix a ser utilizado
	 * @param voucherNo
	 * @param db
	 * @return
	 */
	public static UsedVoucher findVoucherByNo(String voucherNo, Database db)
	{
		Logger logger = Logger.getLogger(RecargaHome.class);
		UsedVoucher used = null;
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery(	"select a from br.com.brasiltelecom.ppp.portal.entity.UsedVoucher a" +
            					 	" where a.voucherNo=$1");
			query.bind(voucherNo);
			rs = query.execute();
			if (rs.hasMore())
				used = (UsedVoucher)rs.next();
		}
		catch(Exception e)
		{
			logger.error("Erro ao consultar cartao temporario ligmix. Erro:"+e);
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return used;
	}
	
	/**
	 * Metodo para consulta das recargas recorrente
	 * @param msisdn	- Número do cliente
	 * @param db		- Conexão com o banco
	 * @return
	 */
	public static Collection findRecargasControle(String msisdn, Date dataInicial, Date dataFinal, Database db)
	{
		Logger logger = Logger.getLogger(RecargaHome.class);
		Collection listaRecargas = new ArrayList();
		OQLQuery query = null;
		QueryResults rs = null;
		try
		{
			query = db.getOQLQuery(	"select a " +
									"  from br.com.brasiltelecom.ppp.portal.entity.Recargas a " +
									" where a.msisdn=$1 " +
					               	"   and (a.tipoTransacao = $2 " +
					               	"	 or  a.tipoTransacao = $3 " +
					               	"	 or  a.tipoTransacao = $4 " +
					               	"	 or  a.tipoTransacao = $5 " +
					               	"	 or  a.tipoTransacao = $6 " +
					               	"	 or  a.tipoTransacao = $7) " +
					               	"   and a.datOrigem >= $8 " +
					               	"   and a.datOrigem <= $9 " +
					               	"   and a.idtErro    = 0  ");

			query.bind(msisdn);
			
			query.bind(Constantes.FRANQUIA_CONTROLE);
			query.bind(Constantes.BONUS_CONTROLE);
			query.bind(Constantes.EXPIRACAO_BONUS);
			query.bind(Constantes.EXPIRACAO_BONUS_CONTROLE);
			query.bind(Constantes.EXPIRACAO_CREDITOS);
			query.bind(Constantes.EXPIRACAO_FRANQUIA_CONTROLE);
			
			query.bind(dataInicial);
			query.bind(dataFinal);
			
			rs = query.execute();
			while (rs.hasMore())
				listaRecargas.add(rs.next());
		}
		catch(Exception e)
		{
			logger.error("Erro ao consultar cartao temporario ligmix. Erro:"+e);
		}
		finally
		{
			if (query != null)
				query.close();
			if (rs != null)
				rs.close();
		}
		return listaRecargas;
	}
	
	public static void atualizaCartao(UsedVoucher used, String msisdn, Database db) throws Exception
	{
		Logger logger = Logger.getLogger(RecargaHome.class);
		try
		{
			used.setSubId(msisdn);
			db.update(used);
		}
		catch(Exception e)
		{
			logger.error("Erro ao atualizar informacoes de cartao utilizado. erro:"+e);
		}
	}
}