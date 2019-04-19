package br.com.brasiltelecom.wig.dao;

import br.com.brasiltelecom.wig.entity.Conteudo;
import br.com.brasiltelecom.wig.entity.Servico;
import br.com.brasiltelecom.wig.entity.Resposta;
import br.com.brasiltelecom.wig.exception.NaoSubiuTSDException;

import java.sql.Connection;
import java.util.Map;

/**
 * @author Joao Carlos
 * Data..: 16/06/2005
 *
 */
public interface ValidadorConteudo
{
	/**
	 * Metodo....:getResposta
	 * Descricao.:Este metodo retorna a Resposta alternativa para os validadores
	 *            de conteudo
	 * @param servico	- Servico que sera validado
	 * @param conteudo	- Conteudo que sera validado
	 * @param msisdn	- Msisdn do Assinante
	 * @param iccid		- Iccid do Assinante
	 * @param con		- Conexao de banco de dados a ser utilizada
	 * @param Map		- Map contendo os valores dos parametros da querystring
	 * @return Resposta - Objeto resposta a ser utilizado como alternativa
	 * @throws NaoSubiuTSDException
	 */
	public Resposta getResposta(Servico servico,
								Conteudo conteudo,
								String msisdn,
								String iccid,
								Connection con,
								Map parameters) throws NaoSubiuTSDException;
}
