package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Map;

public class FiltroQtdeAcessos implements FiltroConteudo
{
	private static 	String sqlFiltro =	"select 1 " +
										  "from (select q.qt_acessos as qtdeAcessosPermitido " +
											           ",(select nvl(count(1),0) " +
											               "from wigc_acessos_conteudo a " +
											              "where a.co_conteudo = q.co_conteudo " +
											             ") as qtdeAcessosExecutados " +
											      "from wigc_filtro_qtde_acessos q " +
											     "where q.co_resposta = ? " +
											    ") " +
									     "where qtdeAcessosExecutados < qtdeAcessosPermitido";

	/**
	 *  (non-Javadoc)
	 * @see br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo#validarParametros(java.util.Map)
	 */
	public boolean validarParametros(Map parameters)
	{
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
			pstmt.setInt(1,resposta.getCodigoResposta());
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
