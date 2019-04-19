package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.valueObject.UsuarioVO;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * @author Victor Paulo A. de Almeida / Sandro Augusto
 *
 * Classe com métodos estáticos para localização de usuários do Portal de Notas <br>baseado em um critério de pesquisa,
 * pré-definido pelo usuário.
 */
public class UsuarioHome {

	/** Método responsável pela recuperação de um objeto Usuário através seu id. 
	 *  @param db Conexão com o banco de dados, necessária para efetuar a busca no banco de dados.
	 *  @param id Matricula do usuario a ser localizado.
	 *  @return result Objeto do tipo Usuário. 
	 *   
	 */
	
	public static Usuario findByID(Database db, String id) throws PersistenceException {

		OQLQuery query = null;
		Usuario result = null;
		QueryResults rs = null;
		
		try{

			query = db.getOQLQuery("select a from "+
				"br.com.brasiltelecom.ppp.portal.entity.Usuario a "+
				"where  lower(a.matricula) = \""+id.toLowerCase()+"\"");

			rs = query.execute();

			if(rs.hasMore()){
				result = (Usuario) rs.next();
			}

		} finally {
			if(rs != null) rs.close();
			if(query!=null){
				query.close();
			}
		}
		return result;
	}

	/** Método responsável pela recuperação de vários objetos do tipo Usuário através de parâmetros passados via formulário de consulta. 
	 *  @param db Conexão com o banco de dados, necessária para efetuar a busca no banco de dados.
	 *  @param param Conjunto (Map) chave-valor dos parâmetros do formulário de consulta.
	 *  @return result Objeto do tipo Collection com um ou mais usuários.
	 */

	public static Collection findByFilter(Database db, Map param) throws PersistenceException {

		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;

		try{

			StringBuffer filtro = new StringBuffer();

			if(param.get("matricula") != null && !"".equals(param.get("matricula"))){
				filtro.append("a.matricula = \"").append(param.get("matricula").toString().trim()).append("\"");
			}

			if(param.get("nome") != null && !"".equals(param.get("nome"))){
				if(filtro.length() > 0){
					filtro.append(" and ");	
				}
				filtro.append("a.nome like \"%").append(param.get("nome").toString().trim()).append("%\"");
			}

			StringBuffer consulta = new StringBuffer("select a from ");
			consulta.append("br.com.brasiltelecom.ppp.portal.entity.Usuario a ");

			if(filtro.length() > 0){
				consulta.append(" where ").append( filtro.toString() );	
			}

			query = db.getOQLQuery(consulta.toString());
			rs = query.execute();
			
			while(rs.hasMore()){
				result.add((Usuario) rs.next());
			}

		} finally {
			if(rs != null) rs.close();
			if(query!=null){
				query.close();
			}
		}
		return result;
	}
	
	/** Método responsável por receber uma requisição e definir os valores para o usuário interno e externo.<br> 
	 *	@param usuario Instância de um usuário
	 *  @param request Requisição web com os valores passados via formulário, a serem definidos para o usuário. 
	 *  @throws Exception Lança a exceção em caso de algum dos objetos recebidos estiverem nulos.
	 */

	public static void setByRequest(Usuario usuario, HttpServletRequest request, 
			Database db)throws Exception{

		usuario.setMatricula(request.getParameter("obr_matricula"));
		usuario.setNome(request.getParameter("obr_nome"));
 		usuario.setCargo(request.getParameter("cargo"));
 		usuario.setEmail(request.getParameter("email"));
 		usuario.setDepartamento(request.getParameter("departamento"));
		usuario.setUltimoAcessoStr(request.getParameter("ultimoAcesso"));
		Collection gruposUsuario = GrupoHome.getGrupos(db, Util.parameterToMap(request));
		usuario.setGrupos(gruposUsuario);
	}

	/*** Método responsável pela criação de um usuário e definir sua senha inicial 
	 *   igual aos primeiros 4 digitos do cpf 
	 *   @param usuario Objeto com as propriedade de um usuário.
	 *   @param db Conexão com o Banco de Dados.
	 *   @throws Exception caso a criação do usuário resulte em falha.
	 */
/*
	public static void criarUsuario(Usuario usuario, Database db) throws Exception{
		Senha senhaInicial = new Senha();
		String senha = null;
		senhaInicial.setId(0);
		senhaInicial.setDataAtualizacao(new Date(0));
		senhaInicial.setUsuario(usuario);
		if(usuario.getCpf() != null) {
			senha = usuario.getCpf().length()>4 ? usuario.getCpf().substring(0,4) : usuario.getCpf();
		} else {
			senha = "senha";	
		}
		senhaInicial.setSenha(GeradorHash.CriptografarSenha(senha));
		usuario.getSenhas().add(senhaInicial);
		db.create(usuario);
	}
*/	
	/** Método responsável por recuperar um ou mais usuários, bem como seu Grupo, 
	 *  retornando um ValueObject desse relacionamento. 
	 *	@param db Conexão com o banco de dados.
	 *  @param param Requisição web, tranformada em Map(conjunto chave-valor) dos parâmetros do formulário. 
	 *  @throws PersistenceException Lança a exceção em caso de não conseguir recuperar o objeto através do castor.
	 *  @see br.com.brasiltelecom.valueObject.UsuarioVO ValueObject para relacionamento <b>usuário - grupo</b>.
	 */

	public static Collection findByFilterUsuarioVO(Database db, Map param) throws PersistenceException {
		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try{

			StringBuffer filtro = new StringBuffer();
			
			//verifica se já existe uma string de filtro [Paginacao]
   			if ((param.get("filtro_sql") == null) || ("".equals(param.get("filtro_sql"))))
   			{			
			
				if(param.get("matricula") != null && !"".equals(param.get("matricula").toString().trim())){
					filtro.append(" and UPPER(A.ID_USUARIO) = '").append(param.get("matricula").toString().trim().toUpperCase()).append("'");
				}


				if(param.get("nome") != null && !"".equals(param.get("nome").toString().trim())){
					filtro.append(" and UPPER(NOM_USUARIO) like '%").append((param.get("nome")).toString().trim().toUpperCase()).append("%'");
				}

				if(param.get("grupo") != null && !"0".equals(param.get("grupo").toString().trim())){
					filtro.append("and ID_GRUPO =").append((String)param.get("grupo"));
				}
				if(param.get("departamento") != null && !param.get("departamento").toString().trim().equals("")){
					filtro.append("and UPPER(NOM_DEPARTAMENTO) LIKE '%").append((String)param.get("departamento").toString().trim().toUpperCase()).append("%'");
				}
				if(param.get("cargo") != null && !param.get("cargo").toString().trim().equals("")){
					filtro.append("and UPPER(DES_CARGO) LIKE '%").append((String)param.get("cargo").toString().trim().toUpperCase()).append("%'");
				}

		

			}
   			else
   			{
    			filtro.append(param.get("filtro_sql").toString()).append(" ");
   			}

			StringBuffer consulta = new StringBuffer("CALL SQL SELECT DISTINCT A.ID_USUARIO AS ID_USUARIO, NOM_USUARIO, ");
		    consulta.append(" NOM_DEPARTAMENTO,  ");
			consulta.append(" DES_CARGO,  ");
			consulta.append(" IDT_EMAIL  ");
		    consulta.append(" FROM TBL_PPP_USUARIO A, TBL_PPP_GRUPO_USUARIO B");
		    consulta.append(" WHERE ");
		    consulta.append("      A.ID_USUARIO  =  B.ID_USUARIO (+) ");

			if(filtro.length() > 0) {
				consulta.append(filtro.toString());	
			}
			consulta.append(" ORDER BY A.ID_USUARIO ");			consulta.append("AS br.com.brasiltelecom.ppp.portal.valueObject.UsuarioVO ");

			query = db.getOQLQuery(consulta.toString());
			rs = query.execute();
			
			while(rs.hasMore()){

				result.add((UsuarioVO) rs.next());
			}
			//Adiciona o filtro na Collection
			result.add(filtro.toString());
			
		} finally {
			if(rs != null) rs.close();
			if(query!=null){
				query.close();
			}
		} 

		return result;
	}
}