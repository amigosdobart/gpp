package br.com.brasiltelecom.wig.filtrosResposta;

import br.com.brasiltelecom.wig.entity.Resposta;

import java.util.Map;
import java.sql.Connection;

public interface FiltroConteudo
{
	/**
	 * Metodo....:validarParametros
	 * Descricao.:Validar os parametros a serem utilizados para identificar se 
	 *            todos existem e estao com valores validos
	 * @param parameters - Parametros a serem verificados
	 * @return boolean   - Indica se os parametros estao validos ou nao
	 */
	public boolean validarParametros(Map parameters);
	
	/**
	 * Metodo....:aplicarFiltro
	 * Descricao.:Aplica o filtro para os parametros enviados
	 * @param resposta   - Resposta a ser confrontada com os parametros
	 * @param con 		 - Conexao de banco de dados utilizada
	 * @return boolean 	 - Indica se pelos valores dos parametros se o filtro eh valido ou nao
	 */
	public boolean aplicarFiltro(Resposta resposta, Connection con);
}
