/*
 * Created on 09/03/2004
 *
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Canal;
import br.com.brasiltelecom.ppp.portal.entity.Origem;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a cadastro de motivos de ajustes
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class AdministrativoAjusteHome {
	/**  
	 *   Método estático responsável pela localização de uma Empresa com base no identificador da mesma.
	 * 	 @param db Conexão com o Banco de Dados.
	 *   @param id Identificador da Empresa - Chave primária.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados.
	 * 	 @return result Objeto Empresa com todas suas propriedades e funcionalidades.
	 */

	public static Origem findByID(Database db, int idOrigem, int idCanal) throws PersistenceException {

			OQLQuery query = null;
			Origem result = null;
			QueryResults rs = null;
			try{

				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.Origem a "+
					"where a.ativo=1 " +
					" and a.idOrigem = " + idOrigem +
					" and a.idCanal = " + idCanal);

				rs = query.execute();
				
				if(rs.hasMore()){
					Origem origem = (Origem) rs.next();
					origem.setCanal(CanalHome.findByID(db,origem.getIdCanal())); 
					result = origem;
				}
				
			}
			finally{
				if(rs != null){
					rs.close();
				}
				if(query!=null){

					query.close();
				}
			}

			return result;
	}

	/**  
	 *   Método estático responsável pela localização de todas as Empresas.
	 * 	 @param db Conexão com o Banco de Dados.
	 *   @throws PersistenceException Exceção lançada para possíveis erros de consulta dos dados. 
	 * 	 @return result Collection com todas as Empresas.
	 */

	public static Collection findAll(Database db) throws PersistenceException {

			OQLQuery query = null;
			Collection result = new ArrayList();
			QueryResults rs = null;

			try{
				query = db.getOQLQuery("select a from "+
					"br.com.brasiltelecom.ppp.portal.entity.Origem a where canal=\"05\" or canal=\"06\" and a.ativo=1");
									
				
				rs = query.execute();
				while (rs.hasMore()){
				
					result.add((Origem) rs.next());				
				}
			}
			finally{
				if(rs != null){
					rs.close();
				}
				
				if(query != null){
					query.close();	
				}	
			}
			
			return result;
	}

	/**
	 *  Método responsável pela definição dos dados de uma Empresa provenientes da requisição do cadastro realizada pelo usuário.
	 *  @param empresa Nova instância de Empresa
	 *  @param request Requisição WEB com os dados para a Empresa
	 *  @param db Conexão com o Banco de Dados
	 *  @throws Exception Exceção lançada para possíveis erros de consulta dos dados.
	 */

	public static void setByRequest(Origem origem, HttpServletRequest request, 
			Database db)throws Exception{

			if (request.getParameter("idOrigem") != null && !request.getParameter("idOrigem").toString().equals("")){
				origem.setIdOrigem(request.getParameter("idOrigem"));
			}
			origem.setDescOrigem((String) request.getParameter("obr_descOrigem"));
		
			int mod = 0;
			if (request.getParameter("diasExpiracao") != null) {
			 mod = Integer.parseInt(request.getParameter("diasExpiracao"));
			}
			if (mod == 0){
				origem.setModificarDataExp(0);
			} else {
				origem.setModificarDataExp(1);
			}
			origem.setNumDiasExpiracao(mod);

			Canal canal = new Canal();
			canal.setIdCanal(request.getParameter("obr_canal"));

			origem.setCanal(canal);
			origem.setIdCanal(canal.getIdCanal());
			
			origem.setAtivo(1);
	}
	
	
	/**
	 *  Método responsável por persistir os dados para a tabela de Empresa.
	 *  @param empresa Nova instância de Empresa com os dados.
	 *  @param db Conexão com o Banco de Dados
	 *  @throws Exception Exceção lançada para possíveis erros de consulta dos dados.
	 */
	public static void criarOrigem(Origem origem, Database db)throws Exception{

		OQLQuery query = db.getOQLQuery("select max(a.idOrigem) from br.com.brasiltelecom.ppp.portal.entity.Origem a " +
			"where a.idCanal=\"05\" or a.idCanal=\"06\"");
		QueryResults rs = query.execute();
		int id = Integer.parseInt((String)rs.next());
			if (id<100)
				origem.setIdOrigem('0'+Integer.toString(++id));
			else
				origem.setIdOrigem(Integer.toString(++id));
		rs.close();
		query.close();
		db.create(origem);
	}

	public static Collection findByFilter(Database db, Map param) throws PersistenceException {

			OQLQuery query =null;
			Collection result = new ArrayList();
			QueryResults rs = null;
			try{

				StringBuffer filtro = new StringBuffer();

				filtro.append(" (a.idCanal = \"05\" or a.idCanal=\"06\") and a.ativo=1");
				if(param.get("descOrigem") != null && !"".equals(param.get("descOrigem"))){
					filtro.append(" and UPPER(a.descOrigem) like \"%").append(param.get("descOrigem").toString().toUpperCase().trim()).append("%\"");
				}

				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.entity.Origem a");

				if(filtro.length() > 0){
					consulta.append(" where ").append( filtro.toString() );	
				}
				
				consulta.append(" order by a.descOrigem");

				query = db.getOQLQuery(consulta.toString());
				rs = query.execute();
			
				while(rs.hasMore()){
					Origem origem = (Origem) rs.next();
					origem.setCanal(CanalHome.findByID(db,origem.getIdCanal())); 
					result.add(origem);
				}

			} finally {
				if(rs != null){
					rs.close();
				}

				if(query!=null){

					query.close();
				}
			}
			return result;
		}
}
