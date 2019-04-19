/*
 * Created on 19/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.TipoExtrato;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Classe respons�vel pelas consultas/atualiza��es no banco de dados relativas aos tipos de comprovantes de servi�os
 * 
 * @author Andr� Gon�alves
 * @since 24/05/2004
 */
public class AdministrativoTipoExtratoHome {
	
	/**
	 * Obt�m todos os extratos dos clientes ativos
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @return Cole��o de objetos TipoExtrato
	 * @throws PersistenceException
	 */
	public static Collection findAll(Database db) throws PersistenceException {

				OQLQuery query = null;
				Collection result = new ArrayList();
				QueryResults rs = null; 
				try{
					query = db.getOQLQuery("select a from "+
						"br.com.brasiltelecom.ppp.portal.entity.TipoExtrato a " +
						" where a.ativo=1 "
						);
									
					
					rs = query.execute();
					while (rs.hasMore()){
				
						result.add((TipoExtrato) rs.next());				
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
	 * Obt�m os extratos de clientes ativos de acordo com o par�metro passado
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @param param Descri��o do tipo de extrato
	 * @return Cole��o de objetos TipoExtrato
	 * @throws PersistenceException
	 */
	public static Collection findByFilter(Database db, Map param) throws PersistenceException {

		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{

			StringBuffer filtro = new StringBuffer();
			
			filtro.append(" a.ativo = 1");

			if(param.get("descTipoExtrato") != null && !"".equals(param.get("descTipoExtrato"))){
				filtro.append(" and UPPER(a.descTipoExtrato) like \"%").append(param.get("descTipoExtrato").toString().toUpperCase().trim()).append("%\"");
			}

			StringBuffer consulta = new StringBuffer("select a from ");
			consulta.append("br.com.brasiltelecom.ppp.portal.entity.TipoExtrato a ");

			if(filtro.length() > 0){
				consulta.append(" where ").append( filtro.toString() );	
			}

			query = db.getOQLQuery(consulta.toString());
			
			rs =  query.execute();
			while(rs.hasMore()){
				result.add((TipoExtrato) rs.next());
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
	
	/**
	 * Efetua altera��o no tipo de extrato
	 * 
	 * @param tipo Objeto TipoExtrato a ser alterado
	 * @param request Requisi��o WEB
	 * @param db Conex�o com o Banco de Dados
	 * @throws Exception
	 */
	public static void setByRequest(TipoExtrato tipo, HttpServletRequest request, 
			Database db)throws Exception{

		if (request.getParameter("idTipoExtrato") != null && !request.getParameter("idTipoExtrato").toString().equals("")){
			tipo.setIdTipoExtrato(new Integer(request.getParameter("idTipoExtrato")).intValue());
		}
		tipo.setDescTipoExtrato((String) request.getParameter("obr_descTipoExtrato"));
		tipo.setData(new Date());
		tipo.setUsuario(((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula());
		String valor = request.getParameter("obr_valor").toString().replace('.','#');
		valor = valor.replaceAll("#","");
		valor = valor.replaceAll(",","."); 
		tipo.setValor(Double.parseDouble(valor));
		tipo.setAtivo(1);
		
	}
	
	/**
	 * Obt�m os extratos ativos pelo identificador do tipo de extrato
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @param id id do tipo de extrato a se buscar
	 * @return objeto TipoExtrato
	 * @throws PersistenceException
	 */
	public static TipoExtrato findByID(Database db, int id) throws PersistenceException {

		OQLQuery query = null;
		TipoExtrato result = null;
		QueryResults rs = null;

		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.TipoExtrato a "+
				"where a.ativo=1 and a.idTipoExtrato = " + id);

			
			rs = query.execute();
			if(rs.hasMore()){

				result = (TipoExtrato) rs.next();
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
	 * Cria um tipo de extrato
	 * 
	 * @param tipo objeto TipoExtrato a ser criado
	 * @param db Conex�o com o Banco de Dados
	 * @throws Exception
	 */
	public static void criarTipoExtrato(TipoExtrato tipo, Database db)throws Exception{

		db.create(tipo);
	}

}

