package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.TipoTransacaoCDR;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento;
import com.brt.gpp.comum.mapeamentos.chave.ChaveHashMap;

/**
 * Esta classe realiza o mapeamento dos dados da tabela
 * de mapeamento de transacao no processo de importacao
 * de CDR. Este mapeamento em memoria visa facilitar o
 * acesso diminuindo I/O no banco de dados melhorando 
 * entao a performance da busca desses valores que sao
 * constantes durante a importacao de arquivos de CDR
 * para Aprovisionamento e/ou Recarga
 *  
 * <P> Versao:			1.0
 *
 * @Autor: 			Joao Carlos
 * Data: 				10/08/2004
 *
 * Modificado por:
 * Data:
 * Razao:
 *
 */

public class MapTipoTransacaoCDR extends Mapeamento
{
	private static MapTipoTransacaoCDR	instance;

	/**
	 * Metodo....:MapTipoTransacaoCDR
	 * Descricao.:Construtor da classe (Singleton)
	 * @param idProcesso	- Id do Processo
	 */
	private MapTipoTransacaoCDR() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna uma instancia dessa classe (Singleton)
	 *            Esta classe implementa um singleton devido a ter todo
	 *            o seu acesso aos dados em memoria portanto uma unica
	 *            instancia e necessaria para manter a boa performance
	 * @param idProcesso			- ID do processo
	 * @return MapTipoTransacaoCDR	- Referencia para o singleton
	 */
	public static MapTipoTransacaoCDR getInstance() 
	{
		try
		{
			if (instance == null)
				instance = new MapTipoTransacaoCDR();
		}
		catch(Exception e)
		{
			MapTipoTransacaoCDR.instance = null;
		}
			
		return instance;
	}

	/**
	 * Metodo....:getTipoTransacao
	 * Descricao.:Busca na colecao qual objeto correspondente ao par de valores passados como
	 *            parametro contendo informacoes de mapeamento de CDRs
	 * @param transType		- Transaction type
	 * @param extTransType	- External Transaction type
	 * @return TipoTransacaoCDR - Tipo de transacao de CDR
	 */	
	public synchronized TipoTransacaoCDR getTipoTransacao(long transType, long extTransType)
	{
		return (TipoTransacaoCDR)super.values.get(this.getChave(transType, extTransType));
	}

	/**
	 *	Retorna a chave para o acesso ao container de objetos.
	 *	
	 *	@param		transType				Transaction type.
	 *	@param		extTransType			External Transaction type.
	 *	@return		Tipo de transacao de CDR.
	 */	
	private ChaveHashMap getChave(long transType, long extTransType)
	{
		return new ChaveHashMap(new Object[]{new Long(transType), new Long(extTransType)});
	}

	/**
	 * Metodo....:atualizaMapeamento
	 * Descricao.:Esta classe atualiza o objeto collection desta para armazenar
	 *            os dados da tabela em memoria
	 *
	 */
	public synchronized void load() 
	{
		PREPConexao conexaoPrep=null;
		try
		{
			// Busca uma conexao com o banco de dados no pool de conexoes
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			
			// Monta o comando para consulta dos dados na tabela e atualiza
			// as informacoes no objeto para esta finalidade armazenando
			// os dados na colecao na qual esta classe gerencia
			String sql =    "SELECT  M.TRANSACTION_TYPE " +
							       ",M.EXTERNAL_TRANSACTION_TYPE " +
							       ",M.IND_RECARGA " +
							       ",M.IND_AJUSTE " +
							       ",M.IND_APROVISIONAMENTO " +
							       ",E.IDT_ANTIGO_CAMPO " +
							       ",E.IDT_NOVO_CAMPO " +
							       ",E.TIP_OPERACAO " +
							       ",R.ID_CANAL  || R.ID_ORIGEM AS TIP_TRANSACAO_RECARGA " +
							       ",A.ID_CANAL  || A.ID_ORIGEM AS TIP_TRANSACAO_AJUSTE " +
							       ",I.IND_INVERTE_PRINCIPAL " +
							       ",I.IND_INVERTE_PERIODICO " +
							       ",I.IND_INVERTE_BONUS " +
							       ",I.IND_INVERTE_SM " +
							       ",I.IND_INVERTE_DADOS " +
							  "FROM TBL_GER_MAPEAMENTO_TRANSACAO M " +
							      ",TBL_GER_MAP_CDR_RECARGA R " +
							      ",TBL_GER_MAP_CDR_AJUSTE  A " +
							      ",TBL_GER_MAP_CDR_EVENTOS E " +
							      ",TBL_GER_MAP_CDR_INV_LANCAMENTO I " +
							 "WHERE R.TRANSACTION_TYPE          (+) = M.TRANSACTION_TYPE " +
							   "AND R.EXTERNAL_TRANSACTION_TYPE (+) = M.EXTERNAL_TRANSACTION_TYPE " +
							   "AND A.TRANSACTION_TYPE          (+) = M.TRANSACTION_TYPE " +
							   "AND A.EXTERNAL_TRANSACTION_TYPE (+) = M.EXTERNAL_TRANSACTION_TYPE " +
							   "AND E.TRANSACTION_TYPE          (+) = M.TRANSACTION_TYPE " +
							   "AND E.EXTERNAL_TRANSACTION_TYPE (+) = M.EXTERNAL_TRANSACTION_TYPE " +
							   "AND I.TRANSACTION_TYPE          (+) = M.TRANSACTION_TYPE " +
							   "AND I.EXTERNAL_TRANSACTION_TYPE (+) = M.EXTERNAL_TRANSACTION_TYPE";

			ResultSet rs = conexaoPrep.executaQuery(sql,0);
			while (rs.next())
			{
				// Inicia uma nova instancia da classe TipoTransacaoCDR e armazena o objeto
				// na colecao para futura pesquisa
				TipoTransacaoCDR tipTransacao = new TipoTransacaoCDR(rs.getLong("TRANSACTION_TYPE"),
				 												     rs.getLong("EXTERNAL_TRANSACTION_TYPE"));
				tipTransacao.setAjuste               	(rs.getInt   ("IND_AJUSTE")           			);
				tipTransacao.setRecarga              	(rs.getInt   ("IND_RECARGA")          			);
				tipTransacao.setEvento               	(rs.getInt   ("IND_APROVISIONAMENTO") 			);
				tipTransacao.setInverterSaldoPrincipal	(rs.getInt   ("IND_INVERTE_PRINCIPAL")			);
				tipTransacao.setInverterSaldoPeriodico	(rs.getInt   ("IND_INVERTE_PERIODICO")			);
				tipTransacao.setInverterSaldoBonus		(rs.getInt   ("IND_INVERTE_BONUS")				);
				tipTransacao.setInverterSaldoSm			(rs.getInt   ("IND_INVERTE_SM")					);
				tipTransacao.setInverterSaldoDados		(rs.getInt   ("IND_INVERTE_DADOS")				);
				tipTransacao.setIdtAntigoCampo		 	(rs.getString("IDT_ANTIGO_CAMPO")				);
				tipTransacao.setIdtNovoCampo		 	(rs.getString("IDT_NOVO_CAMPO")					);
				tipTransacao.setTipoOperacao         	(rs.getString("TIP_OPERACAO")         			);
				tipTransacao.setTipoTransacaoAjuste  	(rs.getString("TIP_TRANSACAO_AJUSTE") 			);
				tipTransacao.setTipoTransacaoRecarga 	(rs.getString("TIP_TRANSACAO_RECARGA")			);
				
				ChaveHashMap chave = this.getChave(tipTransacao.getTransactionType(), tipTransacao.getExternalTransactionType());
				super.values.put(chave, tipTransacao);
			}
		}
		catch(Exception e)
		{
			// Erro na atualização do Mapeamento
		}
		finally
		{
			// Libera a conexao do banco dados de volta para o pool
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep,0);
		}
	}
}
