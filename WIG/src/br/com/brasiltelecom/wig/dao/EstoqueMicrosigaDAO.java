package br.com.brasiltelecom.wig.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import br.com.brasiltelecom.wig.entity.EstoqueMicrosiga;

/**
 * Esta classe realiza a consulta da quantidade de aparelhos ou
 * simcards no estoque do Microsiga
 * 
 * @author Joao Paulo Galvagni
 * Data..: 14/12/2005
 */
public class EstoqueMicrosigaDAO
{
	private static String SQL_MODELO  = "SELECT a.cod_aparelho," +
										"	   	a.idt_cd, " 								+
										"		UPPER(a.idt_mnemonico) as idt_mnemonico,"	+
									    "	   	a.des_aparelho, " 							+
									    "	   	b.des_canal, " 								+
										"	   	a.qtd_estoque, " 							+
									    "	   	a.qtd_livre, "		 						+
									    "	   	a.qtd_reservada" 							+
									    "  FROM tbl_ext_estoque_aparelho a," 				+
									    "       tbl_ext_canal_estoque b "	 				+
									    " WHERE a.id_canal				= ?"				+
									    "   AND UPPER(a.idt_mnemonico)	= UPPER(?)"			+
									    "   AND a.id_canal 				= b.id_canal "		;
	
	private static String SQL_SIMCARD = "SELECT a.cod_aparelho," 							+
										"	   	a.idt_cd, " 								+
										"       UPPER(a.idt_mnemonico) as idt_mnemonico, "	+
									    "	   	a.des_aparelho, " 							+
									    "	   	b.des_canal, " 								+
								        "		a.qtd_estoque, "							+
								        " 		a.qtd_livre, " 								+
								        "		a.qtd_reservada " 							+
									    "  FROM tbl_ext_estoque_aparelho a, "				+
									    "       tbl_ext_canal_estoque b "	 				+
									    " WHERE a.id_canal 		= ? "						+
									    "   AND a.cod_aparelho	= ? "						+
									    "   AND a.id_canal 		= b.id_canal "				;

	/**
	 * Metodo....: getEstoque
	 * Descricao.: Define o objeto estoque com os valores da consulta
	 * 
	 * @param rs   Resultado da consulta, feita por codigo ou mnemonico do aparelho 
	 * @throws SQLException
	 */
	public EstoqueMicrosiga getEstoque(ResultSet rs) throws SQLException
	{
		EstoqueMicrosiga estoque = new EstoqueMicrosiga();

		estoque.setCodAparelho  (rs.getInt	 ("cod_aparelho"));
		estoque.setCd			(rs.getString("idt_cd"));
		estoque.setIdtMnemonico	(rs.getString("idt_mnemonico"));
		estoque.setDesAparelho	(rs.getString("des_aparelho"));
		estoque.setDesCanal		(rs.getString("des_canal"));
		estoque.setQtdEstoque	(rs.getInt	 ("qtd_estoque"));
		estoque.setQtdLivre		(rs.getInt	 ("qtd_livre"));
		estoque.setQtdReservada	(rs.getInt	 ("qtd_reservada"));

		return estoque;
	}

	/**
	 * Metodo....: findByCode
	 * Descricao.: Realiza a consulta ao estoque Microsiga,
	 * 			   utilizando o codigo do simcard e canal como parametros
	 * 
	 * @param canal		- Canal que esta realizando a consulta ao estoque
	 * @param codigo	- codigo do simcard a ser consultado na base
	 * @param con		- conexao com o banco de dados para realizacao da consulta
	 * @throws SQLException
	 */
	public Collection findByCode(int canal, int codigo, Connection con) throws SQLException
	{
		PreparedStatement	pstmt 			= null;
		ResultSet			rs				= null;
		Collection 			listaEstoque 	= new ArrayList();
		
		try
		{
			pstmt = con.prepareStatement(SQL_SIMCARD);
			
			pstmt.setInt(1,canal);
			pstmt.setInt(2,codigo);
			
			rs = pstmt.executeQuery();
			while (rs.next())
				listaEstoque.add(getEstoque(rs));
		}
		catch(SQLException e)
		{
			throw e;
		}
		return listaEstoque;
	}

	/**
	 * Metodo....: findByModel
	 * Descricao.: Realiza a consulta da quantidade de aparelhos em
	 * 			   estoque, utilizando o mnemonico e canal como parametros
	 * 
	 * @param canal		- Canal que esta realizando a consulta
	 * @param modelo	- Mnemonico do aparelho que esta sendo consultado
	 * @param con		- Conexao com o banco de dados para a realizacao da consulta
	 * @throws SQLException
	 */
	public Collection findByModel(int canal, String modelo, Connection con) throws SQLException
	{
		PreparedStatement	pstmt 			= null;
		ResultSet			rs				= null;
		Collection 			listaEstoque 	= new ArrayList();
		
		try
		{
			pstmt = con.prepareStatement(SQL_MODELO);
			
			pstmt.setInt(1,canal);
			pstmt.setString(2,modelo);
			
			rs = pstmt.executeQuery();
			while (rs.next())
				listaEstoque.add(getEstoque(rs));
		}
		catch(SQLException e)
		{
			throw e;
		}
		return listaEstoque;
	}
	
}






