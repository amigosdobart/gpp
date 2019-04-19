package br.com.brasiltelecom.ppp.home;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Operacao;

/**
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 *
 * Classe com m�todos est�ticos para recupera��o das Opera��es que um usu�rio pode realizar no sistema.<br>
 * As opera��es est�o relacionadas a um Grupo/Tarefa espec�fica atribuida a um Usu�rio.<br>
 * Define o perfil/a��es de um usu�rio, tais como: Logar no Sistema, Cadastrar Documentos, etc...
 */
public class OperacaoHome {
	
	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de uma Opera��o atrav�s de seu identificador.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @param id N�mero sequencial que identifica uma Opera��o.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Objeto com os dados da Opera��o.
	 */
	public static Operacao findByID(Database db, int id) throws PersistenceException {
	
			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Operacao a "+
				" where a.id = $1");
	
			query.bind(id);
	
			QueryResults rs = query.execute();
	
			Operacao result = null;
	
			if(rs.hasMore()){
	
				result = (Operacao) rs.next();
	
			}
			
			if(rs != null) rs.close();
			
			if(query!=null){

				query.close();
			}
			return result;
	}

	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de todas as Opera��es do Sistema.
	 * 	 @param db Conex�o com o Banco de Dados.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Map com todas as Opera��es.
	 */
	
	public static Map findAll(Database db) throws PersistenceException {
	
			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Operacao a" +
				" order by a.nivelalfa ");
	
			QueryResults rs = query.execute();
	
			Map result = new TreeMap();
	
			while(rs.hasMore()){
	
				Operacao op = (Operacao) rs.next();
				result.put(op.getNivelalfa() + " " + op.getTipo() + " " + op.getDescricao() + " " + op.getNome(), op);		
	
			}
	
			if(rs != null) rs.close();
			query.close();
			return result; 
	}
	
	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de uma Opera��o do Sistema, a partir de seu nome.
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @param nome Nome da Opera��o.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Objeto com os dados da Opera��o.
	 */

	public static Operacao findByNome(Database db, String nome) throws PersistenceException {
	
			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Operacao a "+
				" where a.nome = $1");
	
			query.bind(nome);
	
			QueryResults rs = query.execute();
	
			Operacao result = null;
	
			if(rs.hasMore()){
	
				result = (Operacao) rs.next();
	
			}
			if(rs != null) rs.close();
			if(query!=null){

				query.close();
			}
			return result;
	}
	
	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de uma Opera��o do Sistema, a partir de seu tipo.
	 *   <ul>
	 * 		<li>tipo <b>ACAO<b> - Define opera��es como: Salvar, Excluir, Alterar</li>
	 * 		<li>tipo <b>MENU<b> - Defie opera��es de visualiz�o de um determinado Menu da aplica��o</li>
	 *   </ul>
	 * 	 @param db Conex�o com o Banco de Dados.
	 * 	 @param tipo tipo da Opera��o.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Collection com as opera��es de um determinado tipo..
	 */
	
	public static Collection findByTipo(Database db, String tipo) throws PersistenceException {

			Collection result = new ArrayList();

			OQLQuery query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Operacao a "+
				" where a.tipo = $1" + 
				" order by a.nivelalfa ");

			query.bind(tipo);

			QueryResults rs = query.execute();

			while(rs.hasMore()){

				result.add((Operacao) rs.next());
			}
			if(rs != null) rs.close();
			if(query!=null){

				query.close();
			}
			return result;
	}

	/**  
	 *   M�todo est�tico respons�vel pela recupera��o de uma cole��o de Opera��es do Sistema, a partir de uma requisi��o Web.
	 *   @param db Conex�o com o Banco de Dados.
	 * 	 @param request Requis�o Web com as opera��es no formato par-valor.
	 *   @throws PersistenceException Exce��o lan�ada para poss�veis erros de consulta dos dados.
	 * 	 @return result Collection com as opera��es.
	 */
	
	public static Collection getOperacoes(Database db, Map request) throws Exception {

		Collection result = new ArrayList();

		for(Iterator it= request.keySet().iterator(); it.hasNext();){

			String key = (String) it.next();

			if(key.startsWith("operacao_")){

				int id = Integer.parseInt((String) request.get(key));
				Operacao op = OperacaoHome.findByID(db, id);
				result.add(op);	

			}					
		}

		return result;
	}


}
