package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Map;
import java.util.Calendar;

public class FiltroRestricaoTempo implements FiltroConteudo
{
	private static 	String sqlFiltro =  "select nu_mes,nu_dia,nu_dia_semana,nu_hora from wigc_filtro_restricao_tempo where co_resposta=?";

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
			while (rs.next())
			{
				// Busca a instancia atual da data para servir de base da pesquisa
				Calendar cal = Calendar.getInstance();
				// Busca no resultset as configuracoes de restricao do tempo a serem aplicadas
				String meses		[] = rs.getString(1).split(",");
				String dias 		[] = rs.getString(2).split(",");
				String diasSemana 	[] = rs.getString(3).split(",");
				String horas		[] = rs.getString(4).split(",");
				
				// Faz a verificacao se o valor existente na data estah presenta nas configuracoes
				// de restricao de tempo para Meses,Dias,Dias da Semana e Horas. Retorna verdadeiro
				// somente se todas as condicoes forem verdadeiras
				if ( valorEstaContido(cal.get(Calendar.MONTH)+1, meses) )
					if ( valorEstaContido(cal.get(Calendar.DAY_OF_MONTH), dias) )
						if ( valorEstaContido(cal.get(Calendar.DAY_OF_WEEK), diasSemana) )
							if ( valorEstaContido(cal.get(Calendar.HOUR_OF_DAY), horas) )
							{
								aplicouFiltro=true;
								break;
							}
			}
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
	
	/**
	 * Metodo....:valorEstaContido
	 * Descricao.:Verifica se o valor desejado estah contido na lista de valores
	 *            OBS: O valor * indica que qualquer valor eh valido 
	 * @param valor		 - Valor desejado
	 * @param parametros - Lista de valores
	 * @return boolean	 - Indica se o valor estah presente na lista de valores
	 */
	private boolean valorEstaContido(int valor, String[] parametros)
	{
		// Faz uma varredura nos valores e verifica se o valor
		// desejado faz parte da lista. Se o valor * estiver presente
		// entao jah retorna true
		for (int i=0; i < parametros.length; i++)
		{
			if ( "*".equals(parametros[i]))
				return true;
			if ( Integer.parseInt(parametros[i]) == valor )
				return true;
		}
		return false;
	}
}
