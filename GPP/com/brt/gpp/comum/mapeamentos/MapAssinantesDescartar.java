package com.brt.gpp.comum.mapeamentos;

import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.sql.ResultSet;

/**
  * Esta classe carrega em memoria a lista de assinantes que
  * serao considerados para importacao de CDRs tanto de
  * dados/voz quanto para eventos/recargas. O motivo para 
  * implementar esta foi de melhorar o acesso a lista devido
  * ser um processamento constante ja que cada linha do arquivo
  * de CDR deve ser verificado portanto estando em memoria a
  * lista entao ha um ganho consideravel de performance
  * 
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				13/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class MapAssinantesDescartar
{
	private GerentePoolBancoDados			gerBancoDados;
	private Collection						mapValoresAssinantes;
	private static MapAssinantesDescartar	instance;

	/**
	 * Metodo....:MapAssinantesDescartar
	 * Descricao.:Construtor da classe (Singleton)
	 */
	private MapAssinantesDescartar() throws GPPInternalErrorException
	{
		// Inicia propriedades da classe
		mapValoresAssinantes 	= new LinkedHashSet();
		gerBancoDados			= GerentePoolBancoDados.getInstancia(0);
		
		atualizaMapeamento();
	}
	
	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna uma instancia dessa classe (Singleton)
	 *            Esta classe implementa um singleton devido a ter todo
	 *            o seu acesso aos dados em memoria portanto uma unica
	 *            instancia e necessaria para manter a boa performance
	 * @return MapAssinantesDescartar	- Referencia para o singleton
	 */
	public static MapAssinantesDescartar getInstance() throws GPPInternalErrorException
	{
		if (instance == null)
			instance = new MapAssinantesDescartar();
			
		return instance;
	}

	/**
	 * Metodo....:atualizaMapeamento
	 * Descricao.:Esta classe atualiza o objeto collection desta para armazenar
	 *            os dados da tabela em memoria
	 *
	 */
	public synchronized void atualizaMapeamento() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep=null;
		try
		{
			// Busca uma conexao com o banco de dados no pool de conexoes
			conexaoPrep = gerBancoDados.getConexaoPREP(0);
			
			// Monta o comando para consulta dos dados na tabela e atualiza
			// as informacoes no objeto para esta finalidade armazenando
			// os dados na colecao na qual esta classe gerencia
			String sql =  "SELECT IDT_MSISDN FROM TBL_APR_ASSINANTE_DESCARTAR";
			
			// Para todos os assinantes e realizada a inclusao do MSISDN 
			// em uma colecao para futuras pesquisas
			ResultSet rs = conexaoPrep.executaQuery(sql,0);
			while (rs.next())
			{
				mapValoresAssinantes.add(rs.getString("IDT_MSISDN"));
			}
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Erro ao atualizar o mapeamento de Assinantes descartados:"+e);
		}
		finally
		{
			// Libera a conexao do banco dados de volta para o pool
			gerBancoDados.liberaConexaoPREP(conexaoPrep,0);
		}
	}
	
	/**
	 * Metodo....:existeAssinate
	 * Descricao.:Realiza a pesquisa para saber se o assinante existe na lista de valores
	 *            que devem ser desconsiderados
	 * @param idtMSISDN	- MSISDN do Assinante
	 * @return boolean	- Indica se o assinante existe na lista ou nao
	 */
	public boolean existeAssinante(String idtMSISDN)
	{
		return mapValoresAssinantes.contains(idtMSISDN);
	}
}