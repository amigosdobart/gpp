/*
 * Created on 19/03/2004
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

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.ContestacaoRecargas;
import br.com.brasiltelecom.ppp.portal.entity.ContestacaoChamadas;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a contestações
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class ContestacaoConsultaHome {
	
	/**
	 * Obtém as contestações de recargas de acordo com o parâmetro informado
	 * 
	 * @param db Conexão com o banco de dados
	 * @param param objeto Map contendo as informações para busca
	 * @return Coleção de objetos ContestacaoRecargas
	 * @throws PersistenceException
	 */
	public static Collection findContestacaoRecargas(Database db, Map param) throws PersistenceException {

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
					String ddd = (String)param.get("msisdn").toString().substring(1,3);
					String telefone = (String)param.get("msisdn").toString().substring(4,8) + (String)param.get("msisdn").toString().substring(9, 13);
					filtro.append("a.subId = \"55").append(ddd + telefone + "\"");
				} else {
					filtro.append("a.subId = \"55").append((String)param.get("msisdn") + "\"");	
				}
					
			}
			
			if(param.get("numBS") != null && !"".equals(param.get("numBS"))){
					
				if(filtro.length() > 0){
				   filtro.append(" and ");	
				}
				
				filtro.append("a.numeroBS = \"").append((String)param.get("numBS") + "\"");
						
			}
			
				
			if( (param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo"))) && (param.get("numBS") == null || "".equals(param.get("numBS")))){
				
			  if(param.get("tipoPeriodo").equals("P")) {
			  					
				if(filtro.length() > 0){
				   filtro.append(" and ");	
				}
				
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt( (String) param.get("periodo")));
				Date dataInicial = c.getTime();

				Calendar cf = Calendar.getInstance();
				cf.add(Calendar.DAY_OF_YEAR,+1);
				Date dataFinal = cf.getTime();
			
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
											    
				filtro.append(" a.data_hora between to_date(\"").append(sdf.format(dataInicial)).append("\",\"DD/MM/YYYY\") and to_date(\"").append(sdf.format(dataFinal)).append("\",\"DD/MM/YYYY\")");
														
			  }														
			}
				
			if( (param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo"))) && (param.get("numBS") == null || "".equals(param.get("numBS")))){
				
				if(param.get("tipoPeriodo").equals("D")) {						
				
					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}						
					
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					cal.setTime(sdf.parse( (String) param.get("dataFinal")));  
					cal.add(Calendar.DAY_OF_YEAR,1);					
				
					filtro.append(" a.data_hora between to_date(\"").append(param.get("dataInicial")).append("\",\"DD/MM/YYYY\") and to_date(\"").append(sdf.format(cal.getTime())).append("\",\"DD/MM/YYYY\")");
				}
			}
			
			if(param.get("flgContestadas") != null && !"".equals(param.get("flgContestadas"))){
		
				if(filtro.length() > 0){
				   filtro.append(" and ");	
				}
	
				filtro.append("is_defined(a.idItemContestacao)");

			}			
				
			StringBuffer consulta = new StringBuffer("select a from ");
			consulta.append("br.com.brasiltelecom.ppp.portal.entity.ContestacaoRecargas a ");
									
			if(filtro.length() > 0){ 
				consulta.append("where ").append( filtro.toString() );	
			}
			consulta.append(" order by a.data_hora desc");	
			query = db.getOQLQuery(consulta.toString());
			rs = query.execute();
			if(rs != null){
				
				while(rs.hasMore()){
				  result.add((ContestacaoRecargas) rs.next());				
				}		
				   
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(rs != null){
				rs.close();
			}

			if(query!=null){
				query.close();
			}
		}
		return result;
   }

   /**
    * Obtém as contestações de chamada de acordo com o parâmetro informado
    * 
    * @param db Conexão com o banco de dados
    * @param param objeto Map com as informações de consulta
    * @return Coleção de objetos ContestacaoChamadas
    * @throws PersistenceException
    */
   public static Collection findContestacaoChamadas(Database db, Map param) throws PersistenceException {			  

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
				
				String ddd = (String)param.get("msisdn").toString().substring(1,3);
				String telefone = (String)param.get("msisdn").toString().substring(4,8) + (String)param.get("msisdn").toString().substring(9, 13);
				filtro.append("a.subId = \"55").append(ddd + telefone + "\"");
				
			} else {
				filtro.append("a.subId = \"55").append((String)param.get("msisdn") + "\"");	
			}
					
		}
				
		if(param.get("numBS") != null && !"".equals(param.get("numBS"))){
					
			if(filtro.length() > 0){
			   filtro.append(" and ");	
			}
				
			filtro.append("a.numeroBS = \"").append((String)param.get("numBS") + "\"");
						
		}
				
		if( (param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo"))) && (param.get("numBS") == null || "".equals(param.get("numBS")))){
	
		  if(param.get("tipoPeriodo").equals("P")) {				
	
			if(filtro.length() > 0){
			   filtro.append(" and ");	
			}										
	
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt( (String) param.get("periodo")));
			Date dataInicial = c.getTime();
			
			Calendar cf = Calendar.getInstance();
			cf.add(Calendar.DAY_OF_YEAR,+1);
			Date dataFinal = cf.getTime();
						
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");				    
			filtro.append(" a.timestamp between to_date(\"").append(sdf.format(dataInicial)).append("\",\"DD/MM/YYYY\") and to_date(\"").append(sdf.format(dataFinal)).append("\",\"DD/MM/YYYY\")");										
	
		  }														
		}
				
		if( (param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo"))) && (param.get("numBS") == null || "".equals(param.get("numBS")))){
	
			if(param.get("tipoPeriodo").equals("D")) {						
	
				if(filtro.length() > 0){
				   filtro.append(" and ");	
				}						
	
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				cal.setTime(sdf.parse( (String) param.get("dataFinal")));  
				cal.add(Calendar.DAY_OF_YEAR,1);
				
				filtro.append(" a.timestamp between to_date(\"").append(param.get("dataInicial")).append("\",\"DD/MM/YYYY\") and to_date(\"").append(sdf.format(cal.getTime())).append("\",\"DD/MM/YYYY\")");
	
			}
		}

		if(param.get("flgContestadas") != null && !"".equals(param.get("flgContestadas"))){
					
			if(filtro.length() > 0){
			   filtro.append(" and ");	
			}
	
			filtro.append("is_defined(a.idItemContestacao)");
			
		}
				
		StringBuffer consulta = new StringBuffer("select a from ");
		consulta.append("br.com.brasiltelecom.ppp.portal.entity.ContestacaoChamadas a ");
									
		if(filtro.length() > 0){ 
			consulta.append("where ").append( filtro.toString() );	
		}
		consulta.append(" order by a.timestamp desc");				
		query = db.getOQLQuery(consulta.toString());
		rs = query.execute();
		if(rs != null){

			while(rs.hasMore()){
			  result.add((ContestacaoChamadas) rs.next());				
			}

		}

	}
	catch(Exception e){
		e.printStackTrace();
	}
	finally{
		if(rs != null){
			rs.close();
		}

		if(query!=null){
			query.close();
		}
	}
	return result;

   }
		
}