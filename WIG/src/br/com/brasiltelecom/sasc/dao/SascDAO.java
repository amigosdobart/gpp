package br.com.brasiltelecom.sasc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.sasc.entity.WsmServico;

/**
 * Classe responsavel pelo acesso as informacoes
 * contidas no Banco de Dados do HSID
 * 
 * @author JOAO PAULO GALVAGNI 
 * @since  12/10/2006
 * 
 */
public class SascDAO
{
	private String msisdn;
	private String iccid;
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static String sqlConsultaSimcardsParaAtualizacao = "SELECT a.nu_iccid, a.nu_msisdn, 			" +
															   " 		a.qt_tentativas, co_atualizacao_sim " +
															   "  FROM sasc_atualizacao_sim a	 			" +
															   " WHERE a.co_atualizacao_sim in (1,2,4) 		" +
															   "   AND a.qt_tentativas <= ?					" ;
	
	private static String sqlConsultaServicosBlackList = "SELECT b.id_servico,		" +
														 "		 b.id_grupo			" +
														 "  FROM sasc_black_list b	" ;
	
	private static String sqlAtualizaStatusAtualizacao = "UPDATE sasc_atualizacao_sim s	    	 " +
														 "   SET s.co_atualizacao_sim = ?,  	 " +
														 "		 s.dt_atualizacao_sim = sysdate, " +
														 "       s.qt_tentativas = ?			 " +
														 " WHERE s.nu_iccid = ?					 " ;
	
	/**
	 *
	 * Metodo....: getSimcardsByStatus
	 * Descricao.: Realiza a selecao dos simcards com o status informado
	 * 
	 * @param  status		- Status dos simcards a serem selecionados
	 * @param  Connection	- Conexao com o Banco de Dados
	 * @return result		- ResultSet contendo as informacoes do Banco
	 * @throws SQLException - Possivel excecao na consulta SQL
	 */
	public ResultSet getSimcardsParaAtualizacao(Connection con, int qtdeMaxTentativas) throws SQLException
	{
		PreparedStatement pstmt		= null;
		ResultSet		  rs		= null;
		
		try
		{
			// Seta o status para realizacao da consulta
			pstmt = con.prepareStatement(sqlConsultaSimcardsParaAtualizacao);
			pstmt.setInt(1, qtdeMaxTentativas);
			
			// Realiza a consulta dos simcards com o perfil informado
			rs = pstmt.executeQuery();
		}
		catch (SQLException e)
		{
			logger.error("Erro na consulta dos simcard a serem atualizados: ", e);
			throw e;
		}
		
		// Retorna o resultado ao solicitante
		return rs;
	}
	
	/**
	 *
	 * Metodo....: getServicosBlackList
	 * Descricao.: Realiza a consulta dos servicos que nao devem
	 *             ser instalados no simcard e/ou devem ser excluidos
	 *
	 * @param Connection		- Conexao com o Banco de Dados
	 * @return result			- Lista contendo os IDs dos servicos da Black List
	 * @exception SQLException	- Possivel excecao no comando SQL
	 */
	public Collection getServicosBlackList(Connection con) throws SQLException
	{
		WsmServico servico 	= null;
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Collection resultado	= new ArrayList();
		
		try
		{
			pstmt = con.prepareStatement(sqlConsultaServicosBlackList);
			
			// Realizacao da consulta dos servicos da BlackList
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				// Seta os resultados obtidos
				servico = new WsmServico();
				servico.setIdServico(rs.getInt("id_servico"));
				servico.setIdGrupo(rs.getInt("id_grupo"));
				
				// Adiciona o objeto a Collection de resultados
				resultado.add(servico);
			}
		}
		catch (SQLException e)
		{
			logger.error("Erro na consulta dos servicos da Black List: ", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
		}
		
		// Retorno do resultado ao solicitante
		return resultado;
	}
	
	/**
	 *
	 * Metodo....: atualizaStatusSimcard
	 * Descricao.: Atualiza o status do simcard conforme o 
	 *             id informado 
	 *
	 * @param status			- Novo status para ser definido no Banco
	 * @param iccid				- ICCID do simcard a ser atualizado
	 * @param con				- Conexao com o Banco de Dados
	 * @exception SQLException	- Possivel excecao na intrucao do SQL
	 */
	public void atualizaStatusSimcard(int status, String iccid, int qtdeTentativas, Connection con) throws SQLException
	{
		PreparedStatement pstmt = null;
		con.setAutoCommit(true);
		
		try
		{
			// Seta o status e o simcard a ser atualizado
			pstmt = con.prepareStatement(sqlAtualizaStatusAtualizacao);
			pstmt.setInt(1, status);
			pstmt.setInt(2, qtdeTentativas);
			pstmt.setString(3, iccid);
			
			// Executa a atualizacao do status do simcard informado
			pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Erro na atualizacao do status do simcard: ", e);
			throw e;
		}
		finally
		{
			if (pstmt != null)
				pstmt.close();
		}
	}
}