package br.com.brasiltelecom.ppp.home;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Grupo;
import br.com.brasiltelecom.ppp.portal.valueObject.GrupoVO;

/**
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 *
 * Classe com m�todos est�ticos para localiza��o dos Grupos do sistema.
 * Um grupo � composto por opera��es/a��es que s�o realizadas no sistema
 */
public class GrupoHome {
	
	
	/**  
	 *   M�todo est�tico respons�vel pela localiza��o de um Grupo, atrav�s de seu identificador �nico.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Objeto Grupo com todas suas propriedades e funcionalidades.
	 */

	public static Grupo findByID(Database db, int id) throws PersistenceException {

			OQLQuery query = null;
			Grupo result = null;
			QueryResults rs = null;

			try{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.Grupo a "+
					" where  a.id = $1 and a.ativo=1");

				query.bind(id);
				rs = query.execute();
				
				if(rs.hasMore()){

					result = (Grupo) rs.next();
				}
			}
			finally{
				if(rs != null) rs.close();
				if(query != null){
					query.close();	
				}	
			}
			return result;
	}

	/**  
	 *   M�todo est�tico respons�vel por recuperar um Grupo atrav�s de seu nome.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Objeto Grupo com todas suas propriedades e funcionalidades.
	 */
	public static Grupo findByNome(Database db, String nome) throws PersistenceException {

			OQLQuery query = null;
			Grupo result = null;
			QueryResults rs = null;

			try{
				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.Grupo a "+
					" where upper(a.nome) = \""+nome.toUpperCase()+"\" and a.ativo=1");
					
				
				rs = query.execute();
				
				if(rs.hasMore()){
					result = (Grupo) rs.next();
				}
			}
			finally {
				if(rs != null) rs.close();

				if(query != null){
					query.close();	
				}	
			}
			return result;
	}

	/**  
	 *   M�todo est�tico respons�vel pela localiza��o de todos os Grupos que satisfa�am ao crit�rio de pesquisa informado pelo usu�rio.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Collection de Grupos com todas suas propriedades e funcionalidades.
	 */	
	public static Collection findByFilter(Database db, Map param) throws PersistenceException {
			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			
			try{
				StringBuffer filtro = new StringBuffer();
				filtro.append(" a.ativo = 1");
				if(param.get("id") != null && !"".equals(param.get("id"))){
					filtro.append(" and a.id = ").append((String)param.get("id"));
				}
				if(param.get("nome") != null && !"".equals(param.get("nome"))){
					filtro.append(" and UPPER(a.nome) like \"%").append((String)param.get("nome").toString().toUpperCase()).append("%\"");
				}  
				
				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.entity.Grupo a ");
				if(filtro.length() > 0){
					consulta.append(" where ").append( filtro.toString() );	
				}
				consulta.append(" order by a.id");
				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				
				while(rs.hasMore()){
					result.add((Grupo) rs.next());
				}
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
	 *   M�todo est�tico respons�vel pela localiza��o de todos os Grupos que satisfa�am ao crit�rio de pesquisa informado pelo usu�rio.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Collection com Value Object de Grupos, com todas suas propriedades e funcionalidades.
	 */	

	public static Collection findByFilterGrupoVO(Database db, Map param) throws PersistenceException {

			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;
		
			try{

				StringBuffer filtro = new StringBuffer();
				filtro.append(" a.ativo=1");
				if(param.get("id") != null && !"".equals(param.get("id"))){
					filtro.append(" and a.id = ").append((String)param.get("id"));
				}

				if(param.get("nome") != null && !"".equals(param.get("nome"))){
					filtro.append(" and UPPER(a.nome) like \"%").append((String)param.get("nome").toString().toUpperCase()).append("%\"");
				}
		
				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.valueObject.GrupoVO a ");
		
				if(filtro.length() > 0){
					consulta.append(" where ").append(filtro);	
				}
		
				consulta.append(" order by a.id");
				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				
				while(rs.hasMore()){
					result.add((GrupoVO) rs.next());
				}
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
	 *   M�todo est�tico respons�vel pela extra��o e localiza��o de Grupos atrav�s dos dados passados via requisi��o WEB.
	 * 	 @param db Conex�o com o Banco de Dados.
	 *   @param request Map com a Requisi��o WEB com todos os dados.
	 * 	 @throws Exception Exce��o lan�ada para poss�veis erros com as opera��es dos dados.
	 * 	 @return result Collection de Grupos com todas suas propriedades e funcionalidades.
	 */
	public static Collection getGrupos(Database db, Map request) throws Exception {

		Collection result = new ArrayList();
		for(Iterator it= request.keySet().iterator(); it.hasNext();){

			String key = (String) it.next();

			if(key.startsWith("grupo_")){

				int id = Integer.parseInt((String) request.get(key));
				Grupo op = findByID(db, id);
				result.add(op);	

			}					
		}

		return result;
			
	}

}
