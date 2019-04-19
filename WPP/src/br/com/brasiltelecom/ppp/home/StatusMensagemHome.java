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
 * Classe respons�vel pelas consultas/atualiza��es no banco de dados relativas aos status das mensagens
 * 
 * @author Andr� Gon�alves
 * @since 24/05/2004
 */
public class StatusMensagemHome {

	/**  
	 *   M�todo est�tico respons�vel pela localiza��o de todos os Chamadas que satisfa�am ao crit�rio de pesquisa informado pelo usu�rio.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
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
