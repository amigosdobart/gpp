package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;
import br.com.brasiltelecom.wig.util.Definicoes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Map;

public class FiltroCategoria implements FiltroConteudo
{
	private 		String msisdn;
	private static 	String sqlFiltro =  "select 1 " +
										  "from wigc_filtro_categoria c " +
										  "where co_resposta  = ? " +
										    "and co_categoria = nvl((select p.idt_categoria " +
										                              "from tbl_apr_assinante a " +
										                                  ",tbl_ger_plano_preco p " +
										                             "where a.idt_msisdn = ? " +
										                               "and p.idt_plano_preco = a.idt_plano_preco), ?)";

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
			pstmt.setInt   (1,resposta.getCodigoResposta());
			pstmt.setString(2,msisdn);
			pstmt.setInt   (3,Definicoes.WIG_CATEGORIA_POSPAGO);
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
