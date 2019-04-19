package br.com.brasiltelecom.wig.dao;

import br.com.brasiltelecom.wig.entity.Servico;

import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author Joao Carlos
 * Data..:31/05/2005
 *
 */
public class ServicoDAO
{
	private static 	ServicoDAO 	instance;
	private 		HashMap 	poolServico;
	private	static 	String 		sqlPesquisa =  "SELECT CO_SERVICO, NO_SERVICO, DS_SERVICO, IN_VALIDA_MSISDN, " +
												      "IN_VALIDA_ICCID, IN_VALIDA_MODELO, IN_VALIDA_IMEI, " +
												      "IN_VALIDA_PROFILE_ICCID, IN_VALIDA_CONFIG_DM, " +
												      "IN_BLOQUEIO_CONCORRENTE " +
												 "FROM wigc_servico " +
												"WHERE co_servico = ?";

	private ServicoDAO()
	{
		poolServico = new HashMap();
	}
	
	public static ServicoDAO getInstance()
	{
		if (instance == null)
			instance = new ServicoDAO();
		
		return instance;
	}
	
	/**
	 * Metodo....:limpaCache
	 * Descricao.:Remove todos os elementos do cache
	 */
	public void limpaCache()
	{
		poolServico.clear();
	}

	/**
	 * Metodo....:getServico
	 * Descricao.:Retorna o objeto Servico baseado nos valores lidos no resultSet atual
	 * @param rs		- ResultSet de dados contendo as informacoes do servico
	 * @return Servico	- Objeto Servico com os dados preenchidos
	 * @throws SQLException
	 */
	private Servico getServico(ResultSet rs) throws SQLException
	{
		Servico servico = new Servico(rs.getInt("CO_SERVICO"));
		servico.setNomeServico			(rs.getString("NO_SERVICO"));
		servico.setDescricaoServico		(rs.getString("DS_SERVICO"));
		servico.setBloqueiaConcorrente	(rs.getString("IN_BLOQUEIO_CONCORRENTE").equals("N") ? false : true );
		servico.setValidaConfigDM		(rs.getString("IN_VALIDA_CONFIG_DM").equals("N")	 ? false : true );
		servico.setValidaIccid			(rs.getString("IN_VALIDA_ICCID").equals("N")	 	 ? false : true );
		servico.setValidaImei			(rs.getString("IN_VALIDA_IMEI").equals("N")	 		 ? false : true );
		servico.setValidaModelo			(rs.getString("IN_VALIDA_MODELO").equals("N")	 	 ? false : true );
		servico.setValidaMsisdn			(rs.getString("IN_VALIDA_MSISDN").equals("N")	 	 ? false : true );
		servico.setValidaProfileIccid	(rs.getString("IN_VALIDA_PROFILE_ICCID").equals("N") ? false : true );

		// Armazena a instancia do objeto que foi alterada ou criada no 
		// cache de dados da classe DAO
		poolServico.put(new Integer(servico.getCodigoServico()),servico);
		return servico;
	}

	/**
	 * Metodo....:findByCodigo
	 * Descricao.:Realiza a pesquisa do servico pelo codigo do mesmo
	 * @param codigoServico	- Codigo do servico
	 * @param con			- Conexao de banco de dados a ser utilizada para a pesquisa
	 * @return Servico		- Objeto Servico encontrado para o codigo passado como parametro
	 * @throws SQLException
	 */
	public synchronized Servico findByCodigo(int codigoServico, Connection con) throws SQLException
	{
		// Inicializa variavel que sera o objeto Servico retornado 
		Servico servico = (Servico)poolServico.get(new Integer(codigoServico));
		if (servico == null)
		{
			// Prepara e realiza a consulta. Caso o registro nao exista entao
			// a referencia nula sera retornada senao o valor sera preenchido
			// pelo metodo getServico
			PreparedStatement pstmt = null;
			ResultSet 		  rs    = null;
			try
			{
				pstmt = con.prepareStatement(sqlPesquisa);
				pstmt.setInt(1,codigoServico);
				rs = pstmt.executeQuery();
				if (rs.next())
					servico = getServico(rs);
			}
			catch(SQLException e){
				throw e;
			}
			finally{
				try{// Fecha os objetos de consulta
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
				}catch(Exception e){};
			}
		}
		return servico;
	}
}
