/*
 * Created on 15/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.home;


import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import br.com.brasiltelecom.ppp.portal.entity.StatusMensagem;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas aos status das mensagens
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class StatusMensagemHome {

	/**  
	 *   Método estático responsável pela localização de todos os Chamadas que satisfaçam ao critério de pesquisa informado pelo usuário.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection de Chamadas com todas suas propriedades e funcionalidades.
	 */	
	public static StatusMensagem findByFilter(Database db, int idStatusVoucher, String idMotivo) throws PersistenceException {
		    StatusMensagem sm = new StatusMensagem();			
			OQLQuery query =null;
			QueryResults rs = null;
			
			try{
				StringBuffer filtro = new StringBuffer();
				
				filtro.append("a.idStatusVoucher = \"").append(idStatusVoucher + "\"");										
								
				if( idMotivo != null && !idMotivo.trim().equals("") ){
				 	if(filtro.length() > 0){
					   filtro.append(" and ");	
					}					
					filtro.append("a.idMotivo = \"").append(idMotivo + "\"");				 													
				}
				
				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.entity.StatusMensagem a ");
				
				if(filtro.length() > 0){ 
					consulta.append("where ").append( filtro.toString() );	
				}				
				
				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				
				if(rs != null && rs.hasMore()) {					
				   sm = (StatusMensagem) rs.next();					
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
			return sm;	
	}

}
