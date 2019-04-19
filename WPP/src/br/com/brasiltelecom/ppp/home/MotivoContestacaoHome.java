/*
 * Created on 04/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.MotivoContestacao;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a motivos de contestação
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class MotivoContestacaoHome {

	/**
	 * Obtém todos os motivos de contestação existentes no sistema 
	 * 
	 * @param db Conexão com o banco de dados
	 * @return Coleção de objetos MotivoContestacao
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

		OQLQuery query = null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{
// SELECIONA APENAS OS REGISTROS QUE DEVEM SER EXIBIDOS NO MENU DROP-DOWN, OU SEJA,
//CUJO CAMPO IND_ATIVO SEJA IGUAL A 1
			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.MotivoContestacao a " +
				"where a.indAtivo = 1" +
				"order by a.idMotivoContestacao");

			rs = query.execute();
	
			while(rs.hasMore()){

				result.add(rs.next());
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
	 *   Método estático responsável pela localização de um Grupo, através de seu identificador único.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Grupo com todas suas propriedades e funcionalidades.
	 */

	public static MotivoContestacao findByID(Database db, int id) throws PersistenceException {

			OQLQuery query = null;
			MotivoContestacao result = null;
			QueryResults rs = null;

			try{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.MotivoContestacao a "+
					" where  a.idMotivoContestacao = $1");

				query.bind(id);
				rs = query.execute();
				
				if(rs.hasMore()){

					result = (MotivoContestacao) rs.next();
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
}
