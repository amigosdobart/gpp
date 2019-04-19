package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Map;
import java.text.DecimalFormat;

public class FiltroERB implements FiltroConteudo
{
	private 		long 			lac;
	private			long			cellId;
	private static	DecimalFormat 	df = new DecimalFormat("00");
	private static 	String 			sqlFiltro =   "select 1 " +
													"from wigc_filtro_erb " +
												   "where co_resposta = ? " +
												     "and nu_lac = ? " +
												     "and nu_cell_id = ?";

	/**
	 *  (non-Javadoc)
	 * @see br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo#validarParametros(java.util.Map)
	 */
	public boolean validarParametros(Map parameters)
	{
		String loc = (String)parameters.get("loc");
		// Somente os 4 ultimos bytes serao utilizados para a identificacao
		// do LAC e do CellID
		if (loc == null || loc.length() != 7)
			return false;
		
		try
		{
			lac  	= getLac(loc);
			cellId  = getCellId(loc);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public static long getLac (String loc)
	{
		return Long.parseLong(getHexaString(loc.charAt(3))+getHexaString(loc.charAt(4)),16);
	}
	
	public static long getCellId (String loc)
	{
		return Long.parseLong(getHexaString(loc.charAt(5))+getHexaString(loc.charAt(6)),16);
	}
	
	/**
	 * Metodo....:getHexaString
	 * Descricao.:Retorna a representacao hexa do caracter
	 * @param character - Caracter desejado
	 * @return String	- Representacao em hexa
	 */
	private static String getHexaString(char character)
	{
		String hexa = Integer.toHexString((int)character);
		try{
			hexa = df.format(Integer.parseInt(hexa));
		}catch (NumberFormatException n){};
		return hexa;
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
			pstmt.setInt	(1,resposta.getCodigoResposta());
			pstmt.setLong	(2,lac);
			pstmt.setLong	(3,cellId);
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
