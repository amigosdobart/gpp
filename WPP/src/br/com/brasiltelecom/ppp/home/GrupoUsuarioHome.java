package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.GrupoUsuario;

/**
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 *
 * Classe com m�todos est�ticos para localiza��o dos Usu�rios de um determinado Grupo.
 */
public class GrupoUsuarioHome {

	/**  
	 *   M�todo est�tico respons�vel pela recupera��o dos Usu�rios de um determinado Grupo, a partir do identificador do Grupo.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @param id Identificador do Grupo.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Objeto GrupoUsuario com a matricula do usu�rio e o id grupo.
	 *   @see br.com.brasiltelecom.portalNF.entity.GrupoUsuario
	 */	
	public static GrupoUsuario findByGrupo(Database db, int id) throws PersistenceException {

			OQLQuery query =null;
			GrupoUsuario result = null;
			QueryResults rs = null;

			try{
								
				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.GrupoUsuario a "+
					"where  a.id = $1");

				query.bind(id);
				rs = query.execute();
				
				if(rs.hasMore()){
					result = (GrupoUsuario) rs.next();
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

}