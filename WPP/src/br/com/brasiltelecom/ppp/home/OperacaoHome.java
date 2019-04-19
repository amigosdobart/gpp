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
 * Classe com métodos estáticos para recuperação das Operações que um usuário pode realizar no sistema.<br>
 * As operações estão relacionadas a um Grupo/Tarefa específica atribuida a um Usuário.<br>
 * Define o perfil/ações de um usuário, tais como: Logar no Sistema, Cadastrar Documentos, etc...
 */
public class OperacaoHome {
	
	/**  
	 *   Método estático responsável pela recuperação de uma Operação através de seu identificador.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @param id Número sequencial que identifica uma Operação.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto com os dados da Operação.
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
	 *   Método estático responsável pela recuperação de todas as Operações do Sistema.
	 * 	 @param db Conexão com o Banco de Dados.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Map com todas as Operações.
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
	 *   Método estático responsável pela recuperação de uma Operação do Sistema, a partir de seu nome.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @param nome Nome da Operação.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto com os dados da Operação.
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
	 *   Método estático responsável pela recuperação de uma Operação do Sistema, a partir de seu tipo.
	 *   <ul>
	 * 		<li>tipo <b>ACAO<b> - Define operações como: Salvar, Excluir, Alterar</li>
	 * 		<li>tipo <b>MENU<b> - Defie operações de visualizão de um determinado Menu da aplicação</li>
	 *   </ul>
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @param tipo tipo da Operação.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection com as operações de um determinado tipo..
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
	 *   Método estático responsável pela recuperação de uma coleção de Operações do Sistema, a partir de uma requisição Web.
	 *   @param db Conexão com o Banco de Dados.
	 * 	 @param request Requisão Web com as operações no formato par-valor.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection com as operações.
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
