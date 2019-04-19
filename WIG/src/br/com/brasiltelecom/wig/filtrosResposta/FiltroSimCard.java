package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Map;

public class FiltroSimCard implements FiltroConteudo
{
	private 		String simCard;
	private static 	String sqlFiltro =	"select 1 " +
										  "from wigc_filtro_simcard " +
										 "where ? between nu_simcard_ini and nu_simcard_fim " +
										   "and co_resposta = ?";

	/**
	 *  (non-Javadoc)
	 * @see br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo#validarParametros(java.util.Map)
	 */
	public boolean validarParametros(Map parameters)
	{
		String iccid = (String)parameters.get("ICCID");
		if (iccid == null || iccid.length() != 19)
			return false;
		
		simCard = iccid.substring(10,18);
		if (simCard == null || simCard.length() != 8)
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
			pstmt.setString (1,simCard);
			pstmt.setInt	(2,resposta.getCodigoResposta());
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
