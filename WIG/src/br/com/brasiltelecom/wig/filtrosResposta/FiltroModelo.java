package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Map;

public class FiltroModelo implements FiltroConteudo
{
	private 		String msisdn;
	private static 	String sqlFiltro = 	"select 1 " +
										  "from hsid_cliente c " +
										      ",wigc_filtro_modelo f " +
										 "where c.nu_msisdn   = ? " +
										   "and f.co_resposta = ? " +
										   "and f.co_modelo   = c.co_modelo ";

	/**
	 *  (non-Javadoc)
	 * @see br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo#validarParametros(java.util.Map)
	 */
	public boolean validarParametros(Map parameters)
	{
		msisdn = (String)parameters.get("MSISDN");
		if (msisdn == null || msisdn.length() != 12)
			return false;

		return true;
	}

	/**
	 * @see br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo#aplicarFiltro(java.util.Map, java.sql.Connection)
	 */
	public boolean aplicarFiltro(Resposta resposta, Connection con)
	{
		boolean aplicouFiltro=false;
		PreparedStatement 	pstmt 	= null;
		ResultSet 			rs 		= null;
		try
		{
			pstmt = con.prepareStatement(sqlFiltro);
			pstmt.setString(1,msisdn);
			pstmt.setInt   (2,resposta.getCodigoResposta());
			// Executa o comando de pesquisa, sendo que se algo retornar no resultSet
			// entao o filtro eh valido para esses parametros
			rs = pstmt.executeQuery();
			aplicouFiltro = rs.next();
		}
		catch(Exception e){
			aplicouFiltro=false;
		}
		finally{
			try{
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			}catch(Exception e){};
		}
		return aplicouFiltro;
	}
}
