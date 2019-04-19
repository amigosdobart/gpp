package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas aos registros de operações
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class LogOperacaoHome {

	/**  
	 *   Método estático responsável pela localização de todos os Logs através dos dados informados por um filtro de consulta.
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @param param Map com os dados da requisição Web.
	 * 	 @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection com os Logs.
	 */
	public static Collection findByFilter(Database db, Map param) throws PersistenceException {

			OQLQuery query =null;
			Collection result = new ArrayList();
			StringBuffer filtro = null;
			QueryResults rs = null;
			try{

				filtro = new StringBuffer();
				
				//verifica se já existe uma string de filtro [Paginacao]
	   			if ((param.get("filtro_sql") == null) || ("".equals(param.get("filtro_sql")))) {	

					if(param.get("matricula") != null && !"".equals(param.get("matricula").toString().trim())){
						filtro.append(" ID_USUARIO = '").append((String)param.get("matricula")).append("'");
					}
				
					if(param.get("operacao") != null && !"0".equals(param.get("operacao").toString().trim())){
				
						if(filtro.length() > 0) {
							filtro.append(" and ");	
						}
						filtro.append(" ID_OPERACAO =").append((String)param.get("operacao"));
					}
				
					if(param.get("acessoInicial") != null && !"".equals(param.get("acessoInicial").toString().trim())){
				
						if(filtro.length() > 0){
							filtro.append(" and ");	
						}
						filtro.append(" DAT_LOG_OPERACAO >= TO_DATE('").append((String)param.get("acessoInicial")).append(" 00:00:00', 'DD/MM/YYYY hh24:mi:ss') ");
					}
				
					if(param.get("acessoFinal") != null && !"".equals(param.get("acessoFinal").toString().trim())){
				
						if(filtro.length() > 0){
							filtro.append(" and ");	
						}
						filtro.append(" DAT_LOG_OPERACAO <= TO_DATE('").append((String)param.get("acessoFinal")).append(" 23:59:59', 'DD/MM/YYYY hh24:mi:ss') ");
					}
				
					if(param.get("mensagem") != null && !"".equals(param.get("mensagem").toString().trim())){
				
						if(filtro.length() > 0){
							filtro.append(" and ");	
						}
						filtro.append(" DES_MENSAGEM like '%").append((String)param.get("mensagem")).append("%'");
					}
				
	   			} else {
	   				
	   				filtro.append(param.get("filtro_sql").toString()).append(" ");
	   			}

				StringBuffer consulta = new StringBuffer("CALL SQL SELECT ID_LOG_OPERACAO, ID_USUARIO, ID_OPERACAO, DAT_LOG_OPERACAO, ");
				consulta.append("  DES_MENSAGEM, IDT_IP FROM TBL_PPP_LOG_OPERACAO ");
				
				if(filtro.length() > 0){
					consulta.append(" where ");
					consulta.append(filtro.toString());	
				}
				consulta.append(" ORDER BY ID_LOG_OPERACAO DESC ");
				consulta.append("AS br.com.brasiltelecom.ppp.portal.entity.LogOperacao ");

				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				
				while(rs.hasMore()){

					result.add( rs.next());
				}
				//Adiciona o filtro na Collection
				result.add(filtro.toString());
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
	 *   Método estático responsável pela recuperação de uma coleção de Logs de Operação do Sistema, a partir de seu tipo.
	 *   <ul>
	 * 		<li>tipo <b>ACAO<b> - Define operações como: Salvar, Excluir, Alterar</li>
	 * 		<li>tipo <b>MENU<b> - Defie operações de visualizão de um determinado Menu da aplicação</li>
	 *   </ul>
	 * 	 @param db Conexão com o Banco de Dados.
	 * 	 @param tipo tipo da Operação.
	 * 	 @param usuario Usuário que efetuou a operação.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Collection com os Logs de operações de um determinado tipo.
	 */
	public static Collection findByTipo(Database db, String tipo, String usuario) throws PersistenceException {

			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;

			try{

				StringBuffer filtro = new StringBuffer();

				filtro.append(" USUARIOID = '").append(usuario).append("'");
				filtro.append(" AND OPERACAOID IN (").append(tipo).append(")");
				
				StringBuffer consulta = new StringBuffer();
				consulta.append("CALL SQL SELECT ");
				consulta.append("ID_OPERACAO, ID_USUARIO, ID_OPERACAO, ");
				consulta.append("DATA, NULL AS FORNECEDOR, DOCUMENTOID, ");
				consulta.append("CNPJ, MENSAGEM, IP ");
				consulta.append("FROM LOG_OPERACAO");
				
				if(filtro.length() > 0){

					consulta.append(" WHERE ");
					consulta.append(filtro.toString());	

				}

				consulta.append(" ORDER BY LOGOPERACAOID ");
				consulta.append("AS br.com.brasiltelecom.ppp.portal.entity.LogOperacao ");

				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
				
				while(rs.hasMore()){

					result.add( rs.next());
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