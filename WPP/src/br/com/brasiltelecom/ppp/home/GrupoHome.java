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
 * Classe com métodos estáticos para localização dos Grupos do sistema.
 * Um grupo é composto por operações/ações que são realizadas no sistema
 */
public class GrupoHome {
	
	
	/**  
	 *   Método estático responsável pela localização de um Grupo, através de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
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
	 *   Método estático responsável por recuperar um Grupo através de seu nome.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
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
	 *   Método estático responsável pela localização de todos os Grupos que satisfaçam ao critério de pesquisa informado pelo usuário.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
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
	 *   Método estático responsável pela localização de todos os Grupos que satisfaçam ao critério de pesquisa informado pelo usuário.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
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
	 *   Método estático responsável pela extração e localização de Grupos através dos dados passados via requisição WEB.
	 * 	 @param db Conexão com o Banco de Dados.
	 *   @param request Map com a Requisição WEB com todos os dados.
	 * 	 @throws Exception Exceção lançada para possíveis erros com as operações dos dados.
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
