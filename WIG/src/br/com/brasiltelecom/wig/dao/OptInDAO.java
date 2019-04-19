package br.com.brasiltelecom.wig.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptInIncentivado;
import br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptInVoluntario;
import br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptOutIncentivado;
import br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptOutVoluntario;
import br.com.brasiltelecom.wig.util.Definicoes;

public class OptInDAO
{
	private final String SELECIONA_CO_MODELO = "SELECT CO_MODELO" 	  +
											   "  FROM HSID_CLIENTE"  +
											   " WHERE NU_MSISDN = ?" ;
	
	private final String SELECIONA_OPTIN = "SELECT NU_MSISDN,"  	+
										   "	 DT_OPTIN," 		+
										   "	 DT_FIDELIZACAO," 	+
										   "	 DT_OPTOUT," 		+
										   "	 NU_LAC," 			+
										   "	 NU_CELL_ID," 		+
										   "	 CO_MODELO,"		+
										   "	 IN_MANDATORIO,"	+
										   "	 CO_CONTEUDO"		+
										   "  FROM WIGC_OPTIN " 	+
										   " WHERE NU_MSISDN = ?"	;
	
	private final String INSERE_OPTIN = "INSERT INTO WIGC_OPTIN (NU_MSISDN,"+
										"					DT_OPTIN," 		+
										"					DT_FIDELIZACAO,"+
										"					DT_OPTOUT," 	+
										"					NU_LAC," 		+
										"					NU_CELL_ID," 	+
										"					CO_MODELO,"		+
										"					IN_MANDATORIO,"	+
										"	 				CO_CONTEUDO"	+
										"					)" 				+
										"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final String INSERE_OPTIN_HISTORICO = "INSERT " 								+
												  "  INTO WIGC_OPTIN_HISTORICO (NU_MSISDN,"	+
												  "					  	   DT_OPTIN," 		+
												  "						   DT_FIDELIZACAO,"	+
												  "						   DT_OPTOUT," 		+
												  "						   NU_LAC," 		+
												  "						   NU_CELL_ID," 	+
												  "						   CO_MODELO,"		+
												  "						   IN_MANDATORIO,"	+
												  "	 					   CO_CONTEUDO,"	+
												  "						   NO_OPERADOR"		+
												  "						   )" 				+
												  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"		;
	
	private final String ATUALIZA_OPTIN = "UPDATE WIGC_OPTIN " 			+
										  "   SET DT_OPTIN = ? ," 		+
										  "		  DT_FIDELIZACAO = ?, " +
										   "	  DT_OPTOUT = ?," 		+
										  "		  NU_LAC = ?,"			+
										  "		  NU_CELL_ID = ?, "		+
										  "		  CO_MODELO = ?, "		+
										  "		  IN_MANDATORIO = ?,"	+
										  "	 	  CO_CONTEUDO = ?"		+
										  "	WHERE NU_MSISDN = ?"		;
	
	private final String ATUALIZA_OPTOUT = "UPDATE WIGC_OPTIN " 		+
										   "   SET DT_FIDELIZACAO = ?, "+
										   "	   DT_OPTOUT = ?," 		+
										   "	   NU_LAC = ?,"			+
										   "	   NU_CELL_ID = ?, "	+
										   "	   CO_MODELO = ?, "		+
										   "	   IN_MANDATORIO = ?,"	+
										   "	   CO_CONTEUDO = ?,"    +
										   "	   NO_OPERADOR = ?"		+
										   "	WHERE NU_MSISDN = ?"	;
	
	// Insercao do assinante em uma determinada categoria
	private final String INSERE_CATEGORIAS_ASSINANTE = "INSERT INTO wigc_optin_categoria" 	  +
											 		   "	   	   (nu_msisdn, id_categoria)" +
											 		   "VALUES (?,?)"						  ;
	
	// Delecao de todas as categorias vinculadas ao assinante
	private final String DELETA_CATEGORIAS_ASSINANTE = "DELETE wigc_optin_categoria woc" +
											 		   " WHERE woc.nu_msisdn = ?"		 ;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Metodo....: deletaTodasCategoriasAssinante
	 * Descricao.: Deleta todas as preferencias associadas ao assinante
	 * 
	 * @param msisdn		- Numero de acesso do assinante
	 * @param con			- Conexao com o Banco de Dados
	 * @throws SQLException
	 */
	public void deletaTodasCategoriasAssinante(String msisdn, Connection con) throws SQLException
	{
		PreparedStatement pstmt = null;
		con.setAutoCommit(false);
		
		try
		{
			// Seta o msisdn cujas preferencias serao deletadas
			pstmt = con.prepareStatement(DELETA_CATEGORIAS_ASSINANTE);
			pstmt.setString(1, msisdn);

			// Executa a delecao das preferencias do assinante
			pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Erro na tentativa de deletar as preferencias do assinante " + msisdn + ". ERRO: ", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	/**
	 * Metodo....: insereTodasCategoriasAssinante
	 * Descricao.: Realiza a insercao de todas as categorias
	 * 			   para o assinante
	 * 
	 * @param msisdn		- Numero de acesso do assinante
	 * @param con			- Conexao com o Banco de Dados
	 * @throws SQLException
	 */
	public void insereTodasCategoriasAssinante(String msisdn, Connection con) throws SQLException
	{
		try
		{
			// Realiza a insercao das categorias para o assinante
			// considerando a quantidade de categorias pai da Definicao
			for (int i = Definicoes.WIG_QTDE_CATEGORIAS_PAI; i > 0; i--)
			{
				insereCategoriaAssinante(msisdn, con, i);
			}
		}
		catch (SQLException e)
		{
			logger.error("Erro na tentativa de inserir todas as categorias do assinante " + msisdn + ". ERRO: ", e);
			throw e;
		}
	}
	
	/**
	 * Metodo....: insereCategoriaAssinante
	 * Descricao.: Insere o assinante e uma categoria, respectivamente
	 * 
	 * @param msisdn		- Msisdn do assinante
	 * @param con			- Conexao com o Banco de Dados
	 * @param idCategoria	- ID da categoria a ser inserida
	 * @throws SQLException
	 */
	public void insereCategoriaAssinante(String msisdn, Connection con, int idCategoria) throws SQLException
	{
		PreparedStatement pstmt		= null;
		
		try
		{
			// Seta o msisdn e a categoria a ser inserida
			pstmt = con.prepareStatement(INSERE_CATEGORIAS_ASSINANTE);
			pstmt.setString(1,msisdn);
			pstmt.setInt(2, idCategoria);
			
			// Executa a insercao do assinante e a respectiva categoria
			pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.debug("Erro na tentativa de inserir as categorias do assinante" + msisdn + ". ERRO: ", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
			con.setAutoCommit(true);
		}
	}
	
	/**
	 * Metodo....: getCoCategorias
	 * Descricao.: Retorna os co_categorias, utilizando o ResultSet
	 * 			   proveniente do select no BD
	 * 
	 * @param rs			- Resultado do select no Banco de Dados
	 * @return int			- Codigo da categoria
	 * @throws SQLException
	 *//*
	public int getCoCategorias (ResultSet rs) throws SQLException
	{
		try
		{
			return rs.getInt("co_categoria");
		}
		catch (SQLException e)
		{
			logger.error("Erro getCoCategorias.", e);
			throw e;
		}
	}*/
	
	/**
	 * Metodo....: getCoModelo
	 * Descricao.: Seleciona o co_modelo do assinante conforme o MSISDN do assinante
	 * 
	 * @param MSISDN		- Numero de acesso do assinante
	 * @param con			- Conexao com o Banco de Dados
	 * @return int			- Codigo do modelo do assinante
	 * @throws SQLException
	 */
	public int getCoModelo(String msisdn, Connection con) throws SQLException
	{
		PreparedStatement pstmt		= null;
		ResultSet		  rs		= null;
		int 			  resultado	= 0;
		
		try
		{
			pstmt = con.prepareStatement(SELECIONA_CO_MODELO);
			pstmt.setString(1,msisdn);
			
			rs = pstmt.executeQuery();
			if (rs.next())
				resultado = rs.getInt("CO_MODELO");
		}
		catch (SQLException e)
		{
			logger.error("Erro na consulta do CO_MODELO.", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
		}
		
		return resultado;
	}
	
	/**
	 * Metodo....: getOptIn
	 * Descricao.: Realiza uma consulta para verificar se o assinante ja esta ativo no OptIn
	 * 
	 * @param optIn		- Objeto contendo os dados do assinante para a consulta
	 * @param con		- Conexao com a base de dados do Clarify
	 * @return OptIn	- Objeto a ser retornado a classe solicitante
	 */
	public OptIn getOptIn(String msisdn, Connection con) throws SQLException
	{
		PreparedStatement pstmt		= null;
		ResultSet		  rs		= null;
		OptIn 			  resultado = null;
		
		try
		{
			pstmt = con.prepareStatement(SELECIONA_OPTIN);
			pstmt.setString(1,msisdn);
			
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				// Cria uma nova instancia do objeto OptIn
				resultado = new OptIn();
				
				// Setando os atributos do objeto OptIn
				resultado.setDataOptIn(rs.getDate("dt_optin"));
				resultado.setDataOptOut(rs.getDate("dt_optout"));
				resultado.setDataFidelizacao(rs.getDate("dt_fidelizacao"));
				resultado.setLac(rs.getLong("nu_lac"));
				resultado.setCellId(rs.getLong("nu_cell_id"));
				resultado.setCoModelo(rs.getInt("co_modelo"));
				resultado.setMandatorio((rs.getInt("in_mandatorio") == 0 ? false : true));
				resultado.setCoConteudo(rs.getInt("co_conteudo"));
				
				// Se nao houver data de fidelizacao, o ValidadorOptIn sera o Voluntario
				if (resultado.getDataFidelizacao() == null)
					resultado.setValidadorOptIn( new ValidadorOptInVoluntario());
				else
					resultado.setValidadorOptIn(new ValidadorOptInIncentivado());
			}
		}
		catch (SQLException e)
		{
			logger.error("Erro na consulta de OptIn.", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
		}
		return resultado;
	}
	
	/**
	 * Metodo....: getOptOut
	 * Descricao.: Realiza uma consulta para verificar se o 
	 * 			   assinante possui OptIn desativado - OptOut
	 * 
	 * @param msisdn		- Numero de acesso do assinante
	 * @param con			- Conexao com o Banco de Dados
	 * @return				- Objeto contendo os atributos do OptIn
	 * @throws SQLException
	 */
	public OptIn getOptOut(String msisdn, Connection con) throws SQLException
	{
		PreparedStatement pstmt		= null;
		ResultSet		  rs		= null;
		OptIn 			  resultado = null;
		
		try
		{
			pstmt = con.prepareStatement(SELECIONA_OPTIN);
			pstmt.setString(1,msisdn);
			
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				// Cria uma nova instancia do objeto OptIn
				resultado = new OptIn();
				
				// Setando os atributos do objeto OptIn
				resultado.setDataOptIn(rs.getDate("dt_optin"));
				resultado.setDataOptOut(rs.getDate("dt_optout"));
				resultado.setDataFidelizacao(rs.getDate("dt_fidelizacao"));
				resultado.setLac(rs.getLong("nu_lac"));
				resultado.setCellId(rs.getLong("nu_cell_id"));
				resultado.setCoModelo(rs.getInt("co_modelo"));
				resultado.setMandatorio((rs.getInt("in_mandatorio") == 0 ? false : true));
				resultado.setCoConteudo(rs.getInt("co_conteudo"));
				
				// Se nao houver data de fidelizacao, o ValidadorOptIn sera o Voluntario
				if (resultado.getDataFidelizacao() == null)
					resultado.setValidadorOptOut( new ValidadorOptOutVoluntario());
				else
					resultado.setValidadorOptOut(new ValidadorOptOutIncentivado());
			}
		}
		catch (SQLException e)
		{
			logger.error("Erro na consulta de OptIn.", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
		}
		return resultado;
	}
	
	/**
	 * Metodo....: inserirOptIn
	 * Descricao.: Insere os dados do assinante no OptIn
	 * 
	 * @param optIn			- Objeto contendo os dados para a insercao
	 * @param con			- Conexao com o Banco de Dados
	 * @throws SQLException
	 */
	public void inserirOptIn(OptIn optIn, Connection con) throws SQLException
	{
		PreparedStatement pstmt = null;
		con.setAutoCommit(false);
		
		try
		{
			pstmt = con.prepareStatement(INSERE_OPTIN);
			// Seta os valores necessarios para insercao no OptIn/OptInHistorico
			pstmt.setString(1, optIn.getMsisdn());
			pstmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pstmt.setDate(3, optIn.getDataFidelizacao() != null ? new java.sql.Date(optIn.getDataFidelizacao().getTime()) : null);
			pstmt.setDate(4, null);
			pstmt.setLong(5, optIn.getLac());
			pstmt.setLong(6, optIn.getCellId());
			pstmt.setInt(7, optIn.getCoModelo());
			pstmt.setInt(8, (optIn.isMandatorio() ? 1 : 0));
			pstmt.setInt(9, optIn.getCoConteudo());
			// Insere o assinante no OptIn
			pstmt.executeUpdate();
			// Insere o assinante no OptInHistorico
			inserirOptInHistorico(optIn, con);
			
			// Commit das acoes realizadas no Banco de Dados
			con.commit();
			logger.debug("OptIn inserido com sucesso para o assinante " + optIn.getMsisdn());
		}
		catch (SQLException e)
		{
			logger.error("Erro na tentativa de cadastro do OptIn.", e);
			con.rollback();
			throw e;
		}
		finally
		{
			con.setAutoCommit(true);
			if (pstmt != null)
				pstmt.close();
		}
	}
	
	/**
	 * Metodo....: inserirOptInHistorico
	 * Descricao.: Insere os dados do assinante na tabela 
	 * 			   de historico do OptIn
	 * 
	 * @param optIn			- Objeto contendo os valores para insercao no Banco
	 * @param con			- Conexao com o Banco de Dados
	 * @throws SQLException
	 */
	public void inserirOptInHistorico(OptIn optIn, Connection con) throws SQLException
	{
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = con.prepareStatement(INSERE_OPTIN_HISTORICO);
			
			pstmt.setString(1, optIn.getMsisdn());
			pstmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pstmt.setDate(3, optIn.getDataFidelizacao() != null ? new java.sql.Date(optIn.getDataFidelizacao().getTime()) : null);
			pstmt.setDate(4, optIn.getDataOptOut() != null ? new java.sql.Date(optIn.getDataOptOut().getTime()) : null);
			pstmt.setLong(5, optIn.getLac());
			pstmt.setLong(6, optIn.getCellId());
			pstmt.setInt(7, optIn.getCoModelo());
			pstmt.setInt(8, (optIn.isMandatorio() ? 1 : 0));
			pstmt.setInt(9, optIn.getCoConteudo());
			pstmt.setString(10, optIn.getOperador());
			
			pstmt.executeUpdate();
			
			logger.debug("Atualizacao do assinante " + optIn.getMsisdn() + " no OptIn historico efetuada com sucesso.");
		}
		catch (SQLException e)
		{
			logger.error("Erro na gravacao do OptIn historico.", e);
			con.rollback();
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
		}
	}
	 
	/**
	 * Metodo....: atualizarOptIn
	 * Descricao.: Atualiza os dados do assinante que ja possui o OptIn
	 * 
	 * @param optIn			- Objeto contendo os novos valores para atualizacao
	 * @param con			- Conexao com o Banco de Dados
	 * @throws SQLException
	 */
	public void atualizarOptIn(OptIn optIn, Connection con) throws SQLException
	{
		PreparedStatement pstmt = null;
		con.setAutoCommit(false);
		try
		{
			pstmt = con.prepareStatement(ATUALIZA_OPTIN);
			
			pstmt.setTimestamp(1, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pstmt.setDate(2, optIn.getDataFidelizacao() != null ? new java.sql.Date(optIn.getDataFidelizacao().getTime()) : null);
			pstmt.setDate(3, null);
			pstmt.setLong(4, optIn.getLac());
			pstmt.setLong(5, optIn.getCellId());
			pstmt.setInt (6, optIn.getCoModelo());
			pstmt.setInt (7, (optIn.isMandatorio() ? 1 : 0));
			pstmt.setInt(8, optIn.getCoConteudo());
			pstmt.setString(9, optIn.getMsisdn());
			
			pstmt.executeUpdate();
			
			inserirOptInHistorico(optIn, con);
			con.commit();
			
			logger.debug("Atualizacao do OptIn do assinante " + optIn.getMsisdn() + " efetuada com sucesso.");
		}
		catch (SQLException e)
		{
			logger.error("Erro na atualizacao do OptIn.", e);
			con.rollback();
			throw e;
		}
		finally
		{
			con.setAutoCommit(true);
			if (pstmt != null)
				pstmt.close();
		}
	}

	/**
	 * Metodo....: atualizarOptOut
	 * Descricao.: Atualiza os dados na tabela de OptIn referente ao OptOut, ou seja,
	 * 			   desativacao do OptIn
	 * 
	 * @param optIn			- Objeto contendo os valores para insercao no OptIn
	 * @param con			- Conexao com o Banco de Dados
	 * @throws SQLException
	 */
	public void atualizarOptOut(OptIn optIn, Connection con)throws SQLException
	{
		PreparedStatement pstmt = null;
		con.setAutoCommit(false);
		
		try
		{
			pstmt = con.prepareStatement(ATUALIZA_OPTOUT);
			
			pstmt.setDate		(1, null);
			pstmt.setTimestamp	(2, optIn.getDataOptOut() != null ? new Timestamp(optIn.getDataOptOut().getTime()) : null);
			pstmt.setLong		(3, optIn.getLac());
			pstmt.setLong		(4, optIn.getCellId());
			pstmt.setInt 		(5, optIn.getCoModelo());
			pstmt.setInt 		(6, (optIn.isMandatorio() ? 1 : 0));
			pstmt.setInt		(7, optIn.getCoConteudo());
			pstmt.setString		(8, optIn.getOperador());
			pstmt.setString		(9, optIn.getMsisdn());
			
			pstmt.executeUpdate();
			
			inserirOptInHistorico(optIn, con);
			con.commit();
			
			logger.debug("Atualziacao do OptOut para o assinante " + optIn.getMsisdn() + " efetuada com sucesso.");
		}
		catch (SQLException e)
		{
			logger.error("Erro na atualizacao do OptOut.", e);
			throw e;
		}
		finally
		{
			con.setAutoCommit(true);
			if (pstmt != null)
				pstmt.close();
		}
	}
}
 
