//Definicao do Pacote
package com.brt.gpp.aplicacoes.estornarRetirarCredito;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
/*
// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

// Arquivos de imports do java
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
*/
/**
  *
  * Este arquivo refere-se a classe EstornoBonusSobreBonus, responsavel pela implementacao da
  * do estorno de credito sobre a utilizacao de bonus sobre bonus nas chamadas com saldo
  * de bonus
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Daniel Abib
  * Data: 				30/01/2005
  *
  * Modificado por:     Daniel Ferreira
  * Data:				31/01/2005
  * Razao:				Termino da Implementacao
  *
  */

public final class EstornoBonusSobreBonus extends Aplicacoes
{
	/*
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	protected GerentePoolTecnomen gerenteTecnomen = null; 		// Gerente de conexoes Tecnomen
	protected long idLog; 										// Armazena o ID do log
	*/     
	/**
	 * Metodo...: EstornoBonusSobreBonus
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public EstornoBonusSobreBonus (long logId)
	 {
		super(logId, Definicoes.CL_ESTORNO_BONUS_SOBRE_BONUS);
		/*
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);

		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
		
		this.idLog = logId;
		*/
	 }

	/**
	 * Metodo...: EstornaBonusSobreBonus
	 * Descricao: Verifica os CRDs de ligacaoes sujetias a estorno de bonus sobre bonus
	 * 
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 */
	public short estornaBonusSobreBonus ( ) throws Exception 
	{
		/*
		long nroEstornosSucesso = 0;
		long nroEstornosFalha = 0;
		short retorno = -1 ;
		String status = Definicoes.PROCESSO_ERRO;
		Timestamp timestampInicial = new Timestamp(Calendar.getInstance().getTimeInMillis());
		
		super.log(Definicoes.DEBUG, "EstornaBonusSobreBonus", "Inicio do processo estorno de bonus sobre bonus");
		PREPConexao conexaoPrep = null;

		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(this.idLog);
		
			// Busca a data da ultima execucao
			Date dataInicioUltimaExecucao = this.retornaInicioUltimaExecutaoBonusSobreBonus(conexaoPrep);
			
			// Busca a data da ultima importacao de assinantes
			Date dataUltimaImportacaoAssinantes = this.retornaUltimaImportacaoAssinantes(conexaoPrep);

			// Faz a pesquisa no banco para saber quais chamadas elegiveis a serem re-tarifadas
			// na janela entre a data de inicio da ultima execucao e a data de inicio da execucao atual
			String sql = "SELECT A.SUB_ID, A.CALL_ID, A.TIMESTAMP, " + 
			             "(A.FF_DISCOUNT * A.BONUS_BALANCE_DELTA)/(100 - A.FF_DISCOUNT) AS VALOR_DESCONTO, " +
	                     "A.BONUS_BALANCE AS SALDO_BONUS " +		
						 "  FROM TBL_GER_CDR A, TBL_APR_ASSINANTE_TECNOMEN B" + 
						 "  WHERE A.TIMESTAMP BETWEEN ? AND ? " +
						 "    AND A.FF_DISCOUNT > ? " +
						 "    AND A.BONUS_BALANCE_DELTA <> ?" + 
						 "    AND A.BONUS_BALANCE + ABS(A.BONUS_BALANCE_DELTA) > ?" +
						 "    AND B.SUB_ID = ('55' || SUBSTR(A.CALL_ID, 2, 10))" +
						 "    AND B.FAMILY_AND_FRIENDS LIKE ('%0' || SUBSTR(A.SUB_ID,3,10) || '%')" +
						 "    AND B.DAT_IMPORTACAO = ?";
						
			Object parametros[] = {new Timestamp(dataInicioUltimaExecucao.getTime()),
								   timestampInicial,
								   new Integer(0),
								   new Integer(0),
					               new Long(Definicoes.TECNOMEN_MULTIPLICADOR * 1000), // R$1000,00 de credito
								   new Timestamp(dataUltimaImportacaoAssinantes.getTime())
								  };

			ResultSet rs = conexaoPrep.executaPreparedQuery(sql, parametros, this.idLog);
			
			//Vetor de informacoes de desconto dos assinantes
			//O ajuste nao sera feito de CDR em CDR, mas de assinante em assinante
			//Agrupar todos os CDR's do assinante em um unico estorno
			Vector vecInfoDesconto = new Vector();
			
			// Para cada registro retornado, atualizar o vetor de informacoes de desconto
			while (rs.next())
			{
				InfoDescontoAssinante infoAssinante = new InfoDescontoAssinante();
				infoAssinante.setMsisdn(rs.getString("SUB_ID"));
				infoAssinante.setSaldoBonus(rs.getDouble("SALDO_BONUS")/Definicoes.TECNOMEN_MULTIPLICADOR);
				infoAssinante.setValorDesconto(Math.abs(rs.getDouble("VALOR_DESCONTO")/Definicoes.TECNOMEN_MULTIPLICADOR));
				infoAssinante.setTimestamp(rs.getTimestamp("TIMESTAMP"));
				insertInfoDescontoAssinante(vecInfoDesconto, infoAssinante);
				
			}
				
			for(int i = 0; i < vecInfoDesconto.size(); i++)
			{	
				// Instancia objeto de ajuste
				Ajustar ajustar = new Ajustar(this.idLog);
				
				//Se o Saldo de Bônus for suficiente, deve ser debitado o valor integral do desconto da chamada.
				//Caso contrário, deve ser debitado todo o Saldo de Bônus do assinante.
				
				double valorDesconto = ((InfoDescontoAssinante)vecInfoDesconto.get(i)).getValorDesconto();
				double saldoBonus = ((InfoDescontoAssinante)vecInfoDesconto.get(i)).getSaldoBonus();
				double valorAjuste = (valorDesconto < saldoBonus) ? valorDesconto: saldoBonus;
				
				// Executa o ajuste
				short retAjuste = ajustar.executaAjuste(
						            ((InfoDescontoAssinante)vecInfoDesconto.get(i)).getMsisdn(), 
									Definicoes.AJUSTE_BONUS_SOBRE_BONUS, 
									Definicoes.TIPO_CREDITO_MINUTOS, 
									valorAjuste, 
									Definicoes.TIPO_AJUSTE_DEBITO, 
									GPPData.formataDataRecarga(GPPData.dataCompletaForamtada()), 
									Definicoes.SO_GPP, 
									Definicoes.GPP_OPERADOR, 
									GPPData.mudaFormato(GPPData.dataFormatada()), 
									null, "",
									Definicoes.AJUSTE_NORMAL);
				
				nroEstornosSucesso += (retAjuste == 0)? 1: 0;
				nroEstornosFalha   += (retAjuste != 0)? 1: 0;

			}
			
			status = Definicoes.PROCESSO_SUCESSO;
			retorno = 0;
		}
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "EstornaBonusSobreBonus", "Excecao Interna GPP ocorrida: "+ e1);
		}
		catch (GPPTecnomenException e2)
		{
			super.log(Definicoes.ERRO, "EstornaBonusSobreBonus", "Excecao Interna GPP ocorrida: "+ e2);
		}
		catch (SQLException e3)
		{
			super.log(Definicoes.ERRO, "EstornaBonusSobreBonus", "Erro durante execucao de operacao no Banco de Dados: "+ e3);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "EstornaBonusSobreBonus. Numero de estornos com sucesso: " + nroEstornosSucesso + 
			                                          " Numero de estornos com erro: "    + nroEstornosFalha;
			
			SimpleDateFormat conversorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			//chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_BONUS_SOBRE_BONUS, 
					                      conversorData.format(timestampInicial), 
										  dataFinal, 
										  status, 
										  descricao, 
										  conversorData.format(timestampInicial));
			super.log(Definicoes.INFO, "EstornaBonusSobreBonus", "Fim do processo batch de estorno de bonus sobre bonus");
		}
		return retorno;
		*/
		return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 * Metodo....: retornaInicioUltimaExecutaoBonusSobreBonus
	 * Descricao.: Retorna a data e hora do início da ultima execucao do processo batch que calcula o estorno do bonus sobre bonus
	 * 
	 * @param prep 			- Conexao com o banco de dados
	 * @return String 		- Data da ultima execucao do processo
	 */
	/*
	protected Date retornaInicioUltimaExecutaoBonusSobreBonus (PREPConexao prep)
	{
		Date retorno = null;
		// Busca a ultima data de execucao encontrada para este processo batch
		// caso o registro seja nulo (primeira execucao) entao utiliza-se a data
		// do dia sem as informacoes de hora
		String sql = "SELECT NVL(MAX(DAT_INICIAL_EXECUCAO),TRUNC(SYSDATE)) AS DAT_INICIAL_EXECUCAO" + 
		             "  FROM TBL_GER_HISTORICO_PROC_BATCH WHERE ID_PROCESSO_BATCH = ?" + 
					 " AND IDT_STATUS_EXECUCAO = ?";
		
        Object parametros[] = {new Integer(Definicoes.IND_BONUS_SOBRE_BONUS), Definicoes.PROCESSO_SUCESSO};
        try
		{
	        ResultSet rs = prep.executaPreparedQuery(sql, parametros, this.idLog);
			if (rs.next())
			{
				// Retorna a data da ultima execucao cadastrada
				retorno = (Date)rs.getTimestamp("DAT_INICIAL_EXECUCAO");
			}
		}
		catch (Exception e )
		{
			super.log(Definicoes.DEBUG, "retornaUltimaExecutaoBonusSobreBonus", "Erro na busca da ultima data de execucao do processo de estorno de bonus sobre bonus");
		}

		return retorno;
	}
	
	/**
	 * Metodo....: retornaUltimaImportacaoAssinantes
	 * Descricao.: Retorna a ultima data de importacao de assinantes da Tecnomen
	 * 
	 * @param prep 			- Conexao com o banco de dados
	 * @return Date 		- Data da ultima execucao do processo
	 */
	/*
	protected Date retornaUltimaImportacaoAssinantes (PREPConexao prep)
	{
		Date retorno = null;
		
		String sql = "SELECT NVL(MAX(DAT_IMPORTACAO),TRUNC(SYSDATE)) AS DAT_IMPORTACAO FROM TBL_APR_ASSINANTE_TECNOMEN";
		
		try
		{
	        ResultSet rs = prep.executaPreparedQuery(sql, null, this.idLog);
	        
			if (rs.next())
			{
				// Retorna a data da ultima importacao de assinantes
				retorno = (Date)rs.getTimestamp("DAT_IMPORTACAO");
			}
		}
		catch (Exception e )
		{
			super.log(Definicoes.DEBUG, "retornaUltimaImportacaoAssinantes", "Erro na busca da ultima data da importacao de assinantes");
		}
		return retorno;
	}
	
	/**
	 * Metodo....: insertInfoDescontoAssinante
	 * Descricao.: Insere informacoes para desconto (estorno) no saldo de bonus do assinante
	 * 
	 * @param vecInfo 			- Vetor de informacoes de desconto
	 * @param novaInfo 			- Informacoes de desconto
	 */
	/*
	private void insertInfoDescontoAssinante(Vector vecInfo, InfoDescontoAssinante novaInfo)
	{
		//Insere as informações para desconto do assinante no vetor
		InfoDescontoAssinante infoAssinante = null;
		double saldoAtual = 0;
		double saldo = 0;
		Timestamp timestampAtual = null;
		Timestamp timestamp = null;
		
		for(int i = 0; (i < vecInfo.size()); i++ )
		{
			//Se já houver informações do assinante no vetor, atualizar as informacoes
			if(((InfoDescontoAssinante)vecInfo.get(i)).getMsisdn().equals(novaInfo.getMsisdn()))
			{
				infoAssinante = (InfoDescontoAssinante)vecInfo.get(i);
				//O valor total de desconto sera acrescido do valor do CDR atual
				infoAssinante.setValorDesconto(infoAssinante.getValorDesconto() + novaInfo.getValorDesconto());
				//Se o CDR for mais recente que os anteriores, atualizar o timestamp e o saldo de bonus do assinante
				saldoAtual = infoAssinante.getSaldoBonus();
				timestampAtual = infoAssinante.getTimestamp();
				saldo = novaInfo.getSaldoBonus();
				timestamp = novaInfo.getTimestamp();
				infoAssinante.setSaldoBonus((timestampAtual.compareTo(timestamp) >= 0) ? saldoAtual: saldo);
				infoAssinante.setTimestamp((timestampAtual.compareTo(timestamp) >= 0) ? timestampAtual: timestamp);
			}
		}
		//Se as informacoes nao forem encontradas no vetor, atualizar com as novas informacoes
	    if(infoAssinante == null) vecInfo.add(novaInfo);
	}
	*/
}