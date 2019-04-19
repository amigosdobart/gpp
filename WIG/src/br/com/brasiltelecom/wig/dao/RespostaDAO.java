package br.com.brasiltelecom.wig.dao;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Joao Carlos
 * Data..: 16/06/2005
 *
 */
public class RespostaDAO
{
	private static 	RespostaDAO 	instance;
	private 		HashMap 		poolResposta;
	private	static	String			sqlPesquisa  = "select R.CO_RESPOSTA,T.CO_TIPO_RESPOSTA,"+
	                                                      "R.DS_RESPOSTA,T.IN_AGRUPA_CONTEUDO," +
														  "R.IN_EXECUTA_RESPOSTA " +
													 "from wigc_resposta r " +
												         ",wigc_tipo_resposta t " +
												    "where r.co_resposta = ? " +
												      "and t.co_tipo_resposta = ? ";
	
	public static final int RESPOSTA_TIPO_WML 			= 0;
	public static final int RESPOSTA_TIPO_REDIRECT 		= 2;
	public static final int RESPOSTA_TIPO_ERRO			= 9;
	public static final int RESPOSTA_TIPO_MENU_CONTEUDO	= 3;
	
	private RespostaDAO()
	{
		poolResposta = new HashMap();
	}
	
	public static RespostaDAO getInstance()
	{
		if (instance == null)
			instance = new RespostaDAO();
		
		return instance;
	}
	
	/**
	 * Metodo....:limpaCache
	 * Descricao.:Remove todos os elementos do cache
	 */
	public void limpaCache()
	{
		poolResposta.clear();
	}

	/**
	 * Metodo....:getConteudo
	 * Descricao.:Retorna o objeto Resposta baseado no resultSet passado como parametro
	 * @param rs		- ResultSet de dados de onde sera retirado as informacoes da resposta
	 * @return Resposta	- Objeto Resposta preenchido
	 * @throws SQLException
	 */
	public Resposta getResposta(ResultSet rs) throws SQLException
	{
		Resposta resposta = new Resposta(rs.getInt	 ("CO_RESPOSTA"));
		resposta.setDescricaoResposta	(rs.getString("DS_RESPOSTA"));
		resposta.setTipoResposta		(rs.getInt   ("CO_TIPO_RESPOSTA"));
		resposta.setAgrupaConteudo		(rs.getString("IN_AGRUPA_CONTEUDO" ).equals("N") ? false : true );
		resposta.setExecutaResposta		(rs.getString("IN_EXECUTA_RESPOSTA").equals("N") ? false : true );
		
		//Armazena a instancia do objeto que foi alterada ou criada no 
		// cache de dados da classe DAO
		poolResposta.put(new Integer(resposta.getCodigoResposta()),resposta);
		return resposta;
	}

	/**
	 * Metodo....:findByCodigo
	 * Descricao.:Realiza a pesquisa da Resposta pelo codigo do mesmo
	 * @param codResposta		- Codigo da resposta
	 * @param codTipoResposta	- Codigo do tipo de resposta
	 * @param con				- Conexao de banco de dados a ser utilizada para a pesquisa
	 * @return Resposta			- Objeto Resposta encontrado para o codigo passado como parametro
	 * @throws SQLException
	 */
	public synchronized Resposta findByCodigo(int codResposta, int codTipoResposta, Connection con) throws SQLException
	{
		// Busca no pool de objetos mantidos pela classe se pela chave do 
		// conteudo encontra o objeto no cache. Caso nao encontre entao uma
		// nova instancia eh criada
		Resposta resposta = (Resposta)poolResposta.get(new Integer(codResposta));
		if (resposta == null)
		{
			// Prepara e realiza a consulta. Caso o registro nao exista entao
			// a referencia nula sera retornada senao o valor sera preenchido
			// pelo metodo getResposta
			// OBS: O SQL dessa pesquisa nao eh realizado atraves de join devido ao modelo
			//      de dados estar incorreto. Devido a Resposta e TipoResposta nao terem ligacoes
			//      entre si porem deveria pois o tipo da resposta eh parte da resposta para o
			//      projeto WIGControl. Entao um cartesiano eh executado se alguns dos registros
			//      nao existir entao a resposta nao sera criada
			PreparedStatement pstmt = null;
			ResultSet 		  rs    = null;
			try
			{
				pstmt = con.prepareStatement(sqlPesquisa);
				pstmt.setInt(1,codResposta);
				pstmt.setInt(2,codTipoResposta);
				rs = pstmt.executeQuery();
				if (rs.next())
					resposta = getResposta(rs);
			}
			catch(SQLException e){
				throw e;
			}
			finally{
				try{
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
				}catch(Exception e){};
			}
		}
		return resposta;
	}
}
