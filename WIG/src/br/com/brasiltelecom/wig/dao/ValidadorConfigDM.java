package br.com.brasiltelecom.wig.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import br.com.brasiltelecom.wig.entity.Conteudo;
import br.com.brasiltelecom.wig.entity.Resposta;
import br.com.brasiltelecom.wig.entity.Servico;
import br.com.brasiltelecom.wig.exception.NaoSubiuTSDException;

/**
 * Esta classe realiza a validacao por imei retornando entao
 * um objeto resposta alternativo ao valor default para o conteudo
 * 
 * @author Joao Carlos
 * Data..: 16/06/2005
 */
public class ValidadorConfigDM implements ValidadorConteudo
{
	private static String SQL_PESQUISA =   "SELECT A.CO_RESPOSTA,A.CO_TIPO_RESPOSTA " +
										     "FROM WIGC_VALIDA_CONFIG_DM A, HSID_MODELO_TAC B " +
										    "WHERE A.IN_CONFIGURACAO_DM = B.IN_CONFIGURACAO_DM " +
								              "AND B.CO_TAC = substr(?,1,6) " +
										      "AND A.CO_CONTEUDO = ?";

	private static String SQL_PESQUISA_IMEI =     "SELECT NU_IMEI " +
												    "FROM HSID_CLIENTE " +
												   "WHERE NU_MSISDN = ?";
		
	/**
	 * @see br.com.brasiltelecom.wib.dao.ValidadorConteudo#getResposta(br.com.brasiltelecom.wib.entity.Servico, br.com.brasiltelecom.wib.entity.Conteudo, java.lang.String, java.lang.String, java.sql.Connection)
	 */
	public Resposta getResposta(Servico servico,
								Conteudo conteudo,
								String msisdn,
								String iccid,
								Connection con,
								Map parameters) throws NaoSubiuTSDException
	{
		Resposta resposta = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			// Busca o Imei do assinante
			String imei = getImei(msisdn,con);
			// Executa a consulta sendo que o primeiro registro encontrado sera utilizado
			// para criar o objeto resposta alternativo para este conteudo
			pstmt = con.prepareStatement(SQL_PESQUISA);
			pstmt.setString	(1,imei);
			pstmt.setInt	(2,conteudo.getCodigoConteudo());
			rs = pstmt.executeQuery();
			int codResposta 	= 0;
			int codTipoResposta = 0;
			if (rs.next())
			{
				codResposta 	= rs.getInt("CO_RESPOSTA");
				codTipoResposta = rs.getInt("CO_TIPO_RESPOSTA");
			}
			// Fecha os objetos de conexao com o banco de dados antes de realizar a pesquisa
			// do objeto resposta devido a este metodo utilizar a mesma conexao de banco de
			// dados
			if (codResposta != 0)
				resposta = RespostaDAO.getInstance().findByCodigo(codResposta,codTipoResposta,con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			resposta = null;
		}
		finally
		{
			try
			{
				if (rs != null)
					rs.close();
				
				if (pstmt != null)
					pstmt.close();
			}
			catch(Exception e){e.printStackTrace();};
		}
		return resposta;
	}

	/**
	 * Metodo....:getImei
	 * Descricao.:Pesquisa o imei do assinante 
	 * @param msisdn		- Msisdn a ser pesquisado
	 * @param con			- Conexao de banco de dados
	 * @return String		- Imei do assinante
	 * @throws SQLException
	 */
	private String getImei(String msisdn, Connection con) throws SQLException,NaoSubiuTSDException
	{
		String imei = null;
		PreparedStatement pstmt = con.prepareStatement(SQL_PESQUISA_IMEI);
		pstmt.setString(1,msisdn);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
			imei=rs.getString("NU_IMEI");
		rs.close();
		pstmt.close();
		// Se nao foi encontrado a informacao de Imei entao o TSD do usuario nao foi 
		// inicializado retornando entao uma excecao
		if (imei == null)
			throw new NaoSubiuTSDException(msisdn);

		return imei;
	}
}
