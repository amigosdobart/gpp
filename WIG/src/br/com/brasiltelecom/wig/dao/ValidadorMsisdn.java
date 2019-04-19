package br.com.brasiltelecom.wig.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import br.com.brasiltelecom.wig.entity.Conteudo;
import br.com.brasiltelecom.wig.entity.Resposta;
import br.com.brasiltelecom.wig.entity.Servico;
import br.com.brasiltelecom.wig.exception.NaoSubiuTSDException;

/**
 * Esta classe realiza a validacao por msisdn retornando entao
 * um objeto resposta alternativo ao valor default para o conteudo
 * 
 * @author Joao Carlos
 * Data..: 16/06/2005
 */
public class ValidadorMsisdn implements ValidadorConteudo
{
	private static String SQL_PESQUISA =   "SELECT A.CO_RESPOSTA,A.CO_TIPO_RESPOSTA " +
										     "FROM WIGC_VALIDA_MSISDN A " +
										    "WHERE A.NU_MSISDN = ? " +
										      "AND A.CO_CONTEUDO = ?";
		
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
			// Executa a consulta sendo que o primeiro registro encontrado sera utilizado
			// para criar o objeto resposta alternativo para este conteudo
			pstmt = con.prepareStatement(SQL_PESQUISA);
			pstmt.setString	(1,msisdn);
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
		catch(Exception e)
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
}
