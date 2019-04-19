/*
 * Criado em 06/07/2005
 *
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

import br.com.brasiltelecom.ppp.portal.entity.RecargasTFPP;
/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a recargas TFPP
 * 
 * @author Marcelo Alves Araujo
 * @since 06/07/2005
 */
public class RecargaTFPPHome {

	/**  
	 *   Método estático responsável pela localização de todos os Chamadas que satisfaçam ao critério de pesquisa informado pelo usuário.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection de Chamadas com todas suas propriedades e funcionalidades.
	 */	
	public static Collection findByFilter(Database db, Map param) throws PersistenceException 
	{
			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			
			try
			{
				StringBuffer filtro = new StringBuffer();
				
				if(param.get("msisdn") != null && !"".equals(param.get("msisdn")))
				{
					
					if(filtro.length() > 0)
					{
					   filtro.append(" and ");	
					}
					if (param.get("msisdn").toString().length() == 13) 
					{
						String ddd = (String)param.get("msisdn").toString().substring(1,3);
						String telefone = (String)param.get("msisdn").toString().substring(4,8) + (String)param.get("msisdn").toString().substring(9, 13);
						filtro.append("a.msisdn = \"55").append(ddd + telefone + "\"");
					} 
					else if(param.get("msisdn").toString().length() == 12)
					{
						String ddd = (String)param.get("msisdn").toString().substring(1,3);
						String telefone = (String)param.get("msisdn").toString().substring(4,7) + (String)param.get("msisdn").toString().substring(8, 12);
						filtro.append("a.msisdn = \"55").append(ddd + telefone + "\"");
					}
					else 
					{
						filtro.append("a.msisdn = \"55").append((String)param.get("msisdn") + "\"");	
					}					
				}
				
				if(param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo")))
				{
				  if(param.get("tipoPeriodo").equals("P")) 
				  {
				
					if(filtro.length() > 0)
					{
					   filtro.append(" and ");	
					}					
					
					Calendar c = Calendar.getInstance();
				    c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt( (String) param.get("periodo")));
				    Date dataInicial = c.getTime();
				    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				    
					filtro.append(" a.datOrigem between to_date(\"").append(sdf.format(dataInicial)).append(" 00:00:00\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(sdf.format(new Date())).append(" 23:59:59\",\"DD/MM/YYYY hh24:mi:ss\")");
				  }														
				}
				
				if(param.get("tipoPeriodo") != null && !"".equals(param.get("tipoPeriodo")))
				{
					if(param.get("tipoPeriodo").equals("D")) 
					{
						
						if(filtro.length() > 0)
						{
						   filtro.append(" and ");	
						}
						
						filtro.append(" a.datOrigem between to_date(\"").append(param.get("dataInicial")).append(" 00:00:00\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(param.get("dataFinal")).append(" 23:59:59\",\"DD/MM/YYYY hh24:mi:ss\")");
					}
				}
				
				if(param.get("canalRecarga") != null && !"".equals(param.get("canalRecarga")))
				{

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
				
				if(param.get("origemRecarga") != null && !"".equals(param.get("origemRecarga")))
				{
					if(filtro.length() > 0)
					{
					   filtro.append(" and ");	
					}
					
					filtro.append("a.idOrigem = \"").append((String)param.get("origemRecarga") + "\"");
				}	
				if(param.get("nsu") != null && !"".equals(param.get("nsu")))
				{
					if(filtro.length() > 0)
					{
					   filtro.append(" and ");	
					}
					filtro.append("a.nsuInstituicao = \"").append((String)param.get("nsu")+ "\"");
				}
				
				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.entity.RecargasTFPP a ");
								
				
				if(filtro.length() > 0)
				{
					consulta.append("where ").append( filtro.toString() );	
				}
				
				consulta.append(" order by a.datOrigem");	
				
				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				while(rs.hasMore())
				{
					RecargasTFPP r =(RecargasTFPP) rs.next();
				    if (r.getIdCanal() != null  && r.getIdOrigem() != null)
				    {
						r.setOrigem(OrigemHome.findByID(db, r.getIdCanal(), r.getIdOrigem()));
					}
					result.add(r);	
                }
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(rs != null) rs.close();
				if(query!=null) query.close();
			}
			return result;	
	}	
}