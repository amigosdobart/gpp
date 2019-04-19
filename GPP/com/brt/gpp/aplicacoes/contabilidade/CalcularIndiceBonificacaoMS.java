package com.brt.gpp.aplicacoes.contabilidade;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;

// Arquivos Java
import java.sql.*;
import java.util.*;

/**
  * Este arquivo refere-se à classe CalcularIndiceBonificacao, responsavel pela implementacao 
  * do processo de Cálculo dos Índices de Bonificação a fim de se determinar o percentual de Consumo Próprio
  * e/ou de Terceiros.
  * Para a contabilidade oficial, a data que deve ser passada como parâmetro é o dia 28 do mês atual
  * a fim de que seja considerado o período [29/(current month-1) ; 28/(current month)]
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				20/07/2004
  *
  */
public class CalcularIndiceBonificacaoMS extends Contabilidade 
{
	/**
	 * Metodo...: CalcularIndiceBonificacao
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public CalcularIndiceBonificacaoMS (long logId, String aData) throws GPPInternalErrorException
	 {
		super(logId, Definicoes.CL_INDICE_BONIFICACAO, aData);
	 }
	 
	 /**
	  * Metodo...: determinarIndiceBonificacao
	  * Descricao: Determina o Indice de Bonificacao de cada CN e as parcelas referentes
	  * 			a bonus/recarga dos consumos próprios além do consumo de terceiros
	  * @param 	String	data		Data de Referência para histórico (dd/MM/YYYY)
	  * @return	short	0, se ok; !0 se nok
	  * @throws GPPInternalErrorException
	  */
	 public short determinarIndiceBonificacao() throws GPPInternalErrorException
	 {
		String status = null;
		PREPConexao conexaoPrep = null; 
		int nLinhasInseridas = 0;
		int nLinhasComplementares = 0;
		String dataInicial = null;
		short retorno = 0;
		ResultSet rsUltimaExecucao = null;
		Timestamp dataUltimaExecucao = null;
		
		try
		{
			super.log(Definicoes.INFO, "determinarIndiceBonificacao", "Inicio");
			
			// Pega conexão com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			//Pega o limite máximo, em reais, de CDRs e Recargas sem cadastros no SFA
			MapConfiguracaoGPP mapGPP = MapConfiguracaoGPP.getInstancia();
			Double vlrMax = new Double(mapGPP.getMapValorConfiguracaoGPP("VLR_MAX_SEM_CODIGO_SFA"));

			// Verifica se existem códigos SFA sem cadastrar
			verificarCodigoCdrSFA(conexaoPrep,vlrMax);
			verificarCodigoRecargaSFA(conexaoPrep,vlrMax);
			
/*******************************************************************************************************************/
			// Limpa tabela com índices calculados para aquele período
			String query = "DELETE FROM TBL_CON_INDICE_BONIFICACAO WHERE IDT_PERIODO_CONTABIL = ?";
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Delecao dados tabela Indices de Bonificacao");
			conexaoPrep.executaPreparedQuery(query,new Object[]{periodoContabil},super.getIdLog());
/*******************************************************************************************************************/
			
			// Registra Data/Hora do início do processo
			dataInicial = GPPData.dataCompletaForamtada();
			
			//Determina a quantidade de registros inseridos na última execução e a data da última execução
			String sqlUltimaExecucao = "SELECT COUNT(IDT_CODIGO_NACIONAL) AS QTD, MAX(DAT_PROCESSAMENTO) AS DATA FROM TBL_CON_INDICE_BONIFICACAO "+ 
					"WHERE DAT_PROCESSAMENTO = (SELECT MAX(DAT_PROCESSAMENTO) FROM TBL_CON_INDICE_BONIFICACAO)";
					
			rsUltimaExecucao = conexaoPrep.executaQuery(sqlUltimaExecucao,super.getIdLog());
			if(rsUltimaExecucao.next())
				dataUltimaExecucao = rsUltimaExecucao.getTimestamp("DATA");
			
			// Se não tem nada ainda na tabela, é necessário preenche-la zerada
			if(dataUltimaExecucao == null)
			{
				conexaoPrep.executaQuery("insert into tbl_con_indice_bonificacao " +
						"(idt_codigo_nacional, idt_pre_hibrido, idt_periodo_contabil, dat_processamento, idt_tipo_saldo," +
						"vlr_saldo_recargas_inicial, vlr_saldo_bonus_inicial, vlr_recargas_periodo, vlr_bonus_periodo, " +
						"vlr_recargas_total, vlr_bonus_total, vlr_perdas_cdr, " +
						"vlr_servicos_proprios_gsm, vlr_servicos_proprios_btsa, vlr_servicos_outras," +
						"vlr_indice_bonificacao, vlr_consumo_bonus, vlr_consumo_recargas, " +
						"vlr_indice_geral_gsm, vlr_indice_geral_btsa," +
						"vlr_saldo_recargas_final, vlr_saldo_bonus_final, " +
						"vlr_consumo_bonus_gsm, vlr_consumo_bonus_btsa, " +
						"vlr_consumo_recargas_gsm, vlr_consumo_recargas_btsa," +
						"vlr_consumo_recargas_outras, " +
						"vlr_indice_bonificacao_gsm, vlr_indice_bonificacao_btsa)" +
						"select IDT_CODIGO_NACIONAL, PLANO, '01/2004',to_date('"+Definicoes.INICIO_DOS_TEMPOS+"','YYYY-MM-DD'), SALDO, " +
						"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 " +
						"from (select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1), " +
						"((SELECT 'P' AS PLANO FROM DUAL)UNION(SELECT 'H' AS PLANO FROM DUAL))," +
						"((SELECT 'SP' AS SALDO FROM DUAL)UNION(SELECT 'SB' AS SALDO FROM DUAL)UNION(SELECT 'SS' AS SALDO FROM DUAL)UNION(SELECT 'SD' AS SALDO FROM DUAL)" +
						"UNION(SELECT 'SF' AS SALDO FROM DUAL))",super.getIdLog());
				rsUltimaExecucao = conexaoPrep.executaQuery(sqlUltimaExecucao,super.getIdLog());
				rsUltimaExecucao.next();
				dataUltimaExecucao = rsUltimaExecucao.getTimestamp("DATA");
			}
	
			// Faz a contabilidade para cada um dos saldos da Tecnomen
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Processando Saldo Principal");
			this.contabilizarPorSaldo("SP", super.dataReferencia, conexaoPrep, dataUltimaExecucao, periodoContabil);
			
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Processando Saldo de Bonus");
			this.contabilizarPorSaldo("SB", super.dataReferencia, conexaoPrep, dataUltimaExecucao, periodoContabil);
			
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Processando Saldo SMS");
			this.contabilizarPorSaldo("SS", super.dataReferencia, conexaoPrep, dataUltimaExecucao, periodoContabil);
			
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Processando Saldo Dados");
			this.contabilizarPorSaldo("SD", super.dataReferencia, conexaoPrep, dataUltimaExecucao, periodoContabil);
			
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Processando Saldo Periodico");
			this.contabilizarPorSaldo("SF", super.dataReferencia, conexaoPrep, dataUltimaExecucao, periodoContabil);
			// String no histórico apresentará sucesso
			status = Definicoes.TIPO_OPER_SUCESSO;	

			return retorno;
		}
		catch(GPPInternalErrorException e)
		{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.ERRO, "determinarIndiceBonificacao", "Excecao Interna GPP: "+ e);
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 		
		}
		catch(SQLException eSQL)
		{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.WARN, "determinarIndiceBonificacao", "Excecao SQL: "+ eSQL);
			throw new GPPInternalErrorException ("Excecao GPP: " + eSQL);					
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Chama a funcao para gravar no historico o Processo em questao
			String descricao = "Foram Inseridas " + (nLinhasInseridas+nLinhasComplementares) + " entradas na TBL_GER_INDICE_BONIFICACAO";
			String dataFinal = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_INDICE_BONIFICACAO, dataInicial, dataFinal, status, descricao, super.dataReferencia);
		
			super.log(Definicoes.INFO, "determinarIndiceBonificacao", "Fim");			
		}			
	}
	 
	private void contabilizarPorSaldo(	String tipoSaldo, String data, PREPConexao conexaoPrep, 
										Timestamp dataUltimaExecucao, String periodoContabil) throws GPPInternalErrorException
	{
		// Montagem do Hash de Bonus/Recarga
		super.log(Definicoes.INFO,"contabilizarPorSaldo",tipoSaldo +":Montando Hash de Recargas");
		HashMap hashBonusRecarga = this.constroiHashRecargas(conexaoPrep, data, tipoSaldo, dataUltimaExecucao);
		
		// Montagem do Hash de Consumo de CDRs
		super.log(Definicoes.INFO,"contabilizarPorSaldo",tipoSaldo +":Montando Hash de Consumo");
		HashMap hashConsumo = this.constroiHashConsumo(conexaoPrep, data, tipoSaldo);
		
		// Montagem do Hash de Saldos
		super.log(Definicoes.INFO,"contabilizarPorSaldo",tipoSaldo +":Montando Hash de Saldos");
		HashMap hashSaldos = this.controiHashSaldos(conexaoPrep, data, tipoSaldo);
		
		// Determina os índices de bonificação para cada cn+plano
		Consumo aConsumo = null;
		BonusRecarga aBonusRecarga = null;
		String chaveHash = null;
		int nroLinhasInseridas = 0;
		for(Iterator i=hashConsumo.values().iterator(); i.hasNext();)
		{
			// Para cada CN+Plano do hash de consumo, procurar o equivalente no hash de bonus/recargas
			aConsumo = (Consumo) i.next();
			chaveHash = aConsumo.getIdtCodigoNacional() + aConsumo.getIdtPreHibrido();
			aBonusRecarga = (BonusRecarga) hashBonusRecarga.get(chaveHash);
			
			super.log(Definicoes.INFO,"contabilizarPorSaldo",tipoSaldo +":"+chaveHash+":Calculando Indice");
			
			// Migrar os consumos registrados na tabela de recargas (consumo via ajuste)
			// Para o Hash de consumos
			aConsumo.setVlrServicosGSM(aConsumo.getVlrServicosGSM() + aBonusRecarga.getVlrConsumoGSM());
			aConsumo.setVlrServicosBTSA(aConsumo.getVlrServicosBTSA() + aBonusRecarga.getVlrConsumoBTSA());
			aConsumo.setVlrServicosOutros(aConsumo.getVlrServicosOutros() + aBonusRecarga.getVlrConsumoTerceiros());
			
			// Cálculo dos Totais de Recarga e Bônus
			double totalCreditos = aBonusRecarga.getVlrRecargasTotal() + aBonusRecarga.getVlrBonusTotal(); 
			
			// Cálculo dos Consumos Total sem índices
			double consumoTotalSemIndices = aConsumo.getVlrServicosGSM() + aConsumo.getVlrServicosBTSA() + aConsumo.getVlrServicosOutros();
			
			// Cálculo da Perda de CDRs
			// saldo calculado = saldo total anterior + total de recargas/bônus - consumo total	
			double saldoCalculado = aBonusRecarga.getVlrSaldoRecargasFinal() + aBonusRecarga.getVlrSaldoBonusFinal() +
							aBonusRecarga.getVlrRecargasPeriodo() + aBonusRecarga.getVlrBonusPeriodo() +	//Soma-se, pois a variável consumoTotalSemIndices é negativa
							consumoTotalSemIndices;
			
			// perda cdrs = saldo oficial - saldo calculado
			double perdaCDR = ((Double)hashSaldos.get(chaveHash)).doubleValue() - saldoCalculado;
			
			// saldo oficial < saldo calculado <=> Houve perda de CDRs (consumo)
			if(perdaCDR < 0)
			{
				// Incorpora a perda de CDRs ao vlr_servico_GSM no hash de consumos
				aConsumo.setVlrServicosGSM(aConsumo.getVlrServicosGSM() + perdaCDR);
				
				// Atualiza variáveis em função da absorção da perda de cdrs pelo consumo gsm
				// Somente o consumo total de CDRs depende dessa variável até o momento
				consumoTotalSemIndices = aConsumo.getVlrServicosGSM() + aConsumo.getVlrServicosBTSA() + aConsumo.getVlrServicosOutros();
			}
			// perda CDR >0 <=> saldo oficial > saldo calculado <=> Houve perda de Créditos (Recarga/Bonus)
			else
			{
				// Incorporar essa perda aos bônus (em módulo)
				aBonusRecarga.setVlrBonusPeriodo(aBonusRecarga.getVlrBonusPeriodo() + perdaCDR);
				
				// Atualiza variáveis em função da absorção da perda de créditos
				// O bonus total e o total de créditos devem ser atualizados
				aBonusRecarga.setVlrBonusTotal(aBonusRecarga.getVlrBonusTotal() + perdaCDR); 
				totalCreditos = totalCreditos + perdaCDR;
			}
	
			// Cálculo dos índices gerais de bonificação (gsm/btsa)
			double indiceGeralBonificacao = 0;
			if(totalCreditos !=0)
			{
				indiceGeralBonificacao = aBonusRecarga.getVlrBonusTotal() / totalCreditos;	
			}
			
			double consumoBonus = consumoTotalSemIndices * indiceGeralBonificacao;
			double consumoRecarga = consumoTotalSemIndices - consumoBonus;
			
			double indiceGeralBonificacaoGSM = 0;
			double indiceGeralBonificacaoBTSA = 0;
			if(aConsumo.getVlrServicosGSM() + aConsumo.getVlrServicosBTSA() != 0)
			{
				indiceGeralBonificacaoGSM = aConsumo.getVlrServicosGSM() / (aConsumo.getVlrServicosGSM() + aConsumo.getVlrServicosBTSA());
				indiceGeralBonificacaoBTSA = aConsumo.getVlrServicosBTSA() / (aConsumo.getVlrServicosGSM() + aConsumo.getVlrServicosBTSA());
			}
			
			// Cálculo da Receita
			double consumoBonusGSM = consumoBonus * indiceGeralBonificacaoGSM;
			double consumoBonusBTSA = consumoBonus * indiceGeralBonificacaoBTSA;
			double consumoRecargaGSM = aConsumo.getVlrServicosGSM() - consumoBonusGSM;
			double consumoRecargaBTSA = aConsumo.getVlrServicosBTSA() - consumoBonusBTSA;
			double consumoRecargaOutros = aConsumo.getVlrServicosOutros();
//			double consumoTotal = consumoBonusGSM + consumoBonusBTSA + consumoRecargaGSM + consumoRecargaBTSA + consumoRecargaOutros;
			
			// Cálculo dos Índices de Bonificação
			double indiceBonificacaoGSM = 0;
			if(aConsumo.getVlrServicosGSM() !=0 )
			{
				indiceBonificacaoGSM = consumoBonusGSM / aConsumo.getVlrServicosGSM();
			}
			
			double indiceBonificacaoBTSA = 0;			
			if(aConsumo.getVlrServicosBTSA() != 0 )
			{
				indiceBonificacaoBTSA = consumoBonusBTSA / aConsumo.getVlrServicosBTSA();
			}
			
			// Cálculo dos Saldos Finais
			double saldoRecargas = aBonusRecarga.getVlrRecargasTotal() - (-consumoRecarga);	// consumoRecarga tem valor negativo
			double saldoBonus = aBonusRecarga.getVlrBonusTotal() - (-consumoBonus);		// consumoBonus tem valor negativo	
			
			// Inserir valores calculados na tabela de índices de bonificação
			String sqlInsert = "INSERT INTO TBL_CON_INDICE_BONIFICACAO " +
					"(DAT_PROCESSAMENTO, IDT_PERIODO_CONTABIL, IDT_CODIGO_NACIONAL, IDT_TIPO_SALDO, IDT_PRE_HIBRIDO, " +
					"VLR_SALDO_RECARGAS_INICIAL, VLR_SALDO_BONUS_INICIAL," +
					"VLR_RECARGAS_PERIODO, VLR_BONUS_PERIODO, VLR_RECARGAS_TOTAL," +
					"VLR_BONUS_TOTAL, VLR_PERDAS_CDR, VLR_SERVICOS_PROPRIOS_GSM," +
					"VLR_SERVICOS_PROPRIOS_BTSA, VLR_SERVICOS_OUTRAS, VLR_INDICE_BONIFICACAO, " +
					"VLR_CONSUMO_BONUS, VLR_CONSUMO_RECARGAS, VLR_INDICE_GERAL_GSM," +
					"VLR_INDICE_GERAL_BTSA, VLR_SALDO_RECARGAS_FINAL," +
					"VLR_SALDO_BONUS_FINAL, VLR_CONSUMO_BONUS_GSM, VLR_CONSUMO_BONUS_BTSA," +
					"VLR_CONSUMO_RECARGAS_GSM, VLR_CONSUMO_RECARGAS_BTSA, VLR_CONSUMO_RECARGAS_OUTRAS," +
					"VLR_INDICE_BONIFICACAO_GSM, VLR_INDICE_BONIFICACAO_BTSA) VALUES " +
					"(to_date(?,'dd/mm/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			
			Object[] sqlIndiceParams = {	data,
											periodoContabil,
											new Integer(aConsumo.idtCodigoNacional),
											tipoSaldo,
											aConsumo.getIdtPreHibrido(),
											new Double(aBonusRecarga.vlrSaldoRecargasFinal),
											new Double(aBonusRecarga.vlrSaldoBonusFinal),
											new Double(aBonusRecarga.vlrRecargasPeriodo),
											new Double(aBonusRecarga.vlrBonusPeriodo),
											new Double(aBonusRecarga.getVlrRecargasTotal()),
											new Double(aBonusRecarga.getVlrBonusTotal()),
											new Double(perdaCDR),
											//new Double(0),
											new Double(aConsumo.getVlrServicosGSM()),
											new Double(aConsumo.getVlrServicosBTSA()),
											new Double(aConsumo.getVlrServicosOutros()),
											new Double(indiceGeralBonificacao),
											new Double(consumoBonus),
											new Double(consumoRecarga),
											new Double(indiceGeralBonificacaoGSM),
											new Double(indiceGeralBonificacaoBTSA),
											new Double(saldoRecargas),
											new Double(saldoBonus),
											new Double(consumoBonusGSM),
											new Double(consumoBonusBTSA),
											new Double(consumoRecargaGSM),
											new Double(consumoRecargaBTSA),
											new Double(consumoRecargaOutros),
											new Double(indiceBonificacaoGSM),
											new Double(indiceBonificacaoBTSA)
										};
			int retornoInsert = conexaoPrep.executaPreparedUpdate(sqlInsert, sqlIndiceParams, super.getIdLog());
			
			if(retornoInsert > 0)
			{
				nroLinhasInseridas += retornoInsert;
			}
		}
			
		//this.completaTblIndiceBonificacao(conexaoPrep, nroLinhasInseridas, nroLinhasUltimaExecucao, data, dataUltimaExecucao, periodoContabil);
	}
		
		
	/**
	 * Método...: controiHashRecargas
	 * Descricao: Monta um hash com as recargas do período [29/month(data)-1 ; data]
	 * 			Vale lembrar que, para a contabilização oficial, esse periodo deve ser [29/month(data)-1 ; 28/month(data)]
	 * @param PREPConexao	conexaoPrep			Conexão com Banco de Dados
	 * @param String 		data				Parametro de Execução (dd/mm/yyyy)
	 * @param String		tipoSaldo			Saldo Contabilizado (SP, SB, SS, SD)		
	 * @param Timestamp		dataUltimaExecucao	Data da última execução (Usada para determinar o saldo inicial do período)
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private HashMap constroiHashRecargas(PREPConexao conexaoPrep, String data, String tipoSaldo, Timestamp dataUltimaExecucao) throws GPPInternalErrorException
	 {
		HashMap hashBonusRecarga = new HashMap();
		String cn = null;
		String indPH = null;
		
		// Query que busca recargas/bonus, consumos via ajuste e
		// Valores da última execução do processo
		String sqlBonusRecarga ="SELECT "+ 
			"TGIB.VLR_SALDO_RECARGAS_FINAL AS VLR_SALDO_RECARGAS_FINAL,"+ 
		    "TGIB.VLR_SALDO_BONUS_FINAL AS VLR_SALDO_BONUS_FINAL,"+ 
			"TBL_VALORES_PERIODO.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL, "+ 
			"TBL_VALORES_PERIODO.VALOR_RECARGAS_PERIODO AS VLR_RECARGAS_PERIODO, "+ 
			"TBL_VALORES_PERIODO.VALOR_BONUS_PERIODO AS VLR_BONUS_PERIODO, "+ 
			"TBL_VALORES_PERIODO.VALOR_CONSUMO_TERCEIROS AS VLR_CONS_TERCEIROS, "+ 
			"TBL_VALORES_PERIODO.VALOR_CONSUMO_GSM AS VLR_CONS_GSM, "+
			"TBL_VALORES_PERIODO.VALOR_CONSUMO_BTSA AS VLR_CONS_BTSA, "+
			"TBL_VALORES_PERIODO.VALOR_RECARGAS_PERIODO+TGIB.VLR_SALDO_RECARGAS_FINAL AS VLR_RECARGAS_TOTAL, "+ 
			"TBL_VALORES_PERIODO.VALOR_BONUS_PERIODO+TGIB.VLR_SALDO_BONUS_FINAL AS VLR_BONUS_TOTAL, "+ 
			"TBL_VALORES_PERIODO.IDT_PRE_HIBRIDO AS IDT_PRE_HIBRIDO "+ 
			"FROM "+ 
			"( "+ 
			   //--- TBL_VALORES_PERIODO
			    "SELECT "+  
			    "TBL1.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL, "+
			    "TBL1.IDT_PRE_HIBRIDO AS IDT_PRE_HIBRIDO, "+
			    "SUM(DECODE(TBL1.IDT_TIPO_SERVICO,'RE',TBL1.TOTAL_RECARGAS,0)) AS VALOR_RECARGAS_PERIODO, "+ 	// Recargas
			    "SUM(DECODE(TBL1.IDT_TIPO_SERVICO,'BO',TBL1.TOTAL_RECARGAS,0)) AS VALOR_BONUS_PERIODO, "+ 	// Bonus
			    "SUM(DECODE(TBL1.IDT_TIPO_SERVICO,'CRO',TBL1.TOTAL_RECARGAS,0)) AS VALOR_CONSUMO_TERCEIROS, "+	// Consumo de Outras Operadoras
				
				// Não estão sendo sumarizados os 'CBB' e 'CRG' para evitar duplicação. Na verdade, tanto faz qual está sendo sumarizado. Qq um daria certo
				"SUM(DECODE(TBL1.IDT_TIPO_SERVICO,'CBG',TBL1.TOTAL_RECARGAS,0)) AS VALOR_CONSUMO_GSM, "+ 	// Consumo de Recarga GSM
			    "SUM(DECODE(TBL1.IDT_TIPO_SERVICO,'CRB',TBL1.TOTAL_RECARGAS,0)) AS VALOR_CONSUMO_BTSA "+ // Consumo de Recarga BTSA
			    "FROM "+ 
			    "(   SELECT  TRR.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL, "+ 
			                "trim(TGRS.IDT_TIPO_SERVICO) AS IDT_TIPO_SERVICO,"+ 
                            "SUM(TRR."+ this.getCampoReferenteAoSaldo(tipoSaldo,false) +") AS TOTAL_RECARGAS,"+	// Sumarização das Recargas no saldo em questão
			                "plano.prehibrido as IDT_PRE_HIBRIDO "+  
			        "FROM    TBL_REL_RECARGAS TRR, "+  
			                "(select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1) TGAN, "+  
			                "TBL_CON_RECARGA_SERVICO TGRS, "+
			                "(   select idt_plano_preco as plano, decode(idt_categoria,0,'P','H') as prehibrido "+ 
			                    "from tbl_ger_plano_preco "+  
			                    "where idt_categoria < 2 "+ 
			                ") plano "+ 
			        "WHERE "+
					// Parametros 1 e 2 - Inicio/Final do período contábil (O parametro é a data do final do período)
					//"TRR.DAT_RECARGA BETWEEN add_months(to_date('27/'||substr(?,4,8),'DD/MM/YYYY'),-1)+2	and to_date('28/'||substr(?,4,8),'DD/MM/YYYY') "+ 
			        "TRR.DAT_RECARGA BETWEEN add_months(to_date('27/'||substr(?,4,8),'DD/MM/YYYY'),-1)+2	and to_date(?,'DD/MM/YYYY') "+
			        "AND TRR.IDT_CODIGO_NACIONAL = TGAN.IDT_CODIGO_NACIONAL "+  
			        "AND TGRS.ID_CANAL = TRR.ID_CANAL "+ 
			        "AND TGRS.ID_ORIGEM = TRR.ID_ORIGEM "+  
			        "AND TGRS.ID_SISTEMA_ORIGEM = TRR.ID_SISTEMA_ORIGEM "+  
			        "AND trR.idt_plano = plano.plano "+ 
                    "AND TGRS.IDT_PLANO_PRECO = plano.plano " +
                    "AND TRR.COD_RETORNO = 0 "+  
			        "GROUP BY TRR.IDT_CODIGO_NACIONAL, trim(TGRS.IDT_TIPO_SERVICO), plano.prehibrido "+  
			    ") TBL1 "+  
			    "GROUP BY TBL1.IDT_CODIGO_NACIONAL,TBL1.IDT_PRE_HIBRIDO "+  
			") TBL_VALORES_PERIODO, "+ 
			"TBL_CON_INDICE_BONIFICACAO TGIB "+  
			"WHERE "+  
			"TGIB.IDT_CODIGO_NACIONAL (+)= TBL_VALORES_PERIODO.IDT_CODIGO_NACIONAL "+  
			"AND TGIB.IDT_PRE_HIBRIDO = TBL_VALORES_PERIODO.IDT_PRE_HIBRIDO "+
			"AND TGIB.DAT_PROCESSAMENTO = ? " +		// Parametro 3 - Data da última execução
			"and TGIB.idt_tipo_saldo = ?";		// Se não for colocada essa condição (que poderia ser contruída
												// com qualquer saldo), retornariam 4 linhas de cada CN para esse select
		
		try
		{
			// Execução da Query de Bonus/Recarga
			Object[] bonusRecargaPars = {data, data, dataUltimaExecucao, tipoSaldo};
			ResultSet rsBonusRecarga = conexaoPrep.executaPreparedQuery(sqlBonusRecarga, bonusRecargaPars, super.getIdLog());
			
			while (rsBonusRecarga.next())
			{
				// Constrói um objeto com os dados consolidados de bonus/recarga de cada CN
				cn = rsBonusRecarga.getString("IDT_CODIGO_NACIONAL");
				indPH = rsBonusRecarga.getString("IDT_PRE_HIBRIDO");
				BonusRecarga aBonusRecarga = new BonusRecarga(	cn, 
																indPH, 
																rsBonusRecarga.getDouble("VLR_RECARGAS_PERIODO"),
																rsBonusRecarga.getDouble("VLR_BONUS_PERIODO"), 
																rsBonusRecarga.getDouble("VLR_CONS_TERCEIROS"), 
																rsBonusRecarga.getDouble("VLR_CONS_BTSA"),
																rsBonusRecarga.getDouble("VLR_CONS_GSM"), 
																rsBonusRecarga.getDouble("VLR_RECARGAS_TOTAL"), 
																rsBonusRecarga.getDouble("VLR_BONUS_TOTAL"),
																rsBonusRecarga.getDouble("VLR_SALDO_RECARGAS_FINAL"), 
																rsBonusRecarga.getDouble("VLR_SALDO_BONUS_FINAL"));
				
				hashBonusRecarga.put(cn+indPH, aBonusRecarga);
			}			
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO, "constroiHashRecargas", "Erro Banco de Dados: "+sqlE);
			throw new GPPInternalErrorException("Erro Banco Dados: "+sqlE);			
		}
		
		return hashBonusRecarga; 
	 }
	 
	 /**
	  * Metodo...: constroiHashConsumo
	  * Descricao: Monta um hash com os consumos do período [29/month(data)-1 ; data]
	  * 			Vale lembrar que, para a contabilização oficial, esse periodo deve ser [29/month(data)-1 ; 28/month(data)]
	  * @param PREPConexao	conexaoPrep		Conexão com o Banco de Dados
	  * @param String		data			Data da contabilização
	  * @param String		tipoSaldo		Tipo de Saldo (SP, SB, SS, SD)
	  * @return	HashMap		Hash com os consumos. Chave do hash = CN + H/P
	  * @throws GPPInternalErrorException
	  */
	 private HashMap constroiHashConsumo(PREPConexao conexaoPrep, String data, String tipoSaldo) throws GPPInternalErrorException
	 {
		HashMap hashConsumo = new HashMap();
		String cn = null;
		String indPH = null;
		
		//	Query que traz os valores de consumo
		String sqlConsumo = "SELECT "+
		"TBL1.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL, "+
		"SUM(DECODE(IDT_TIPO_SERVICO,'CRG',TOTAL_CHAMADAS,0))*(-1) AS VALOR_SERVICOS_GSM, "+ //--- Linha Nova
		"SUM(DECODE(IDT_TIPO_SERVICO,'CRB',TOTAL_CHAMADAS,0))*(-1) AS VALOR_SERVICOS_BTSA, "+ //-- Linha Nova
		"SUM(DECODE(IDT_TIPO_SERVICO,'CRO',TOTAL_CHAMADAS,0))*(-1) AS VALOR_SERVICOS_TERCEIROS, "+ //-- Consumo de Outras Operadoras
		"TBL1.IDT_PRE_HIBRIDO AS IDT_PRE_HIBRIDO "+
		"FROM "+
		"( "+
		"    SELECT "+ 
		"    TRCD.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL, "+ 
		"    TGCS.IDT_TIPO_SERVICO AS IDT_TIPO_SERVICO, "+
		"    SUM(TRCD."+ this.getCampoReferenteAoSaldo(tipoSaldo,false) +") AS TOTAL_CHAMADAS, "+ 
		"    plano.prehibrido as IDT_PRE_HIBRIDO "+
		"    FROM "+
		"    TBL_REL_CDR_DIA TRCD, "+ 
		"    (select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1) TGAN, "+ 
		"    TBL_CON_CDR_SERVICO TGCS, "+
		"    (select idt_plano_preco as plano, decode(idt_categoria,0,'P','H') as prehibrido from tbl_ger_plano_preco where idt_categoria < 2) plano "+ 
		"    WHERE "+
		//"    TRCD.DAT_CDR BETWEEN  add_months(to_date('27/'||substr(?,4,8),'DD/MM/YYYY'),-1)+2 and to_date('28/'||substr(?,4,8),'DD/MM/YYYY') "+	
		"    TRCD.DAT_CDR BETWEEN  add_months(to_date('27/'||substr(?,4,8),'DD/MM/YYYY'),-1)+2 and to_date(?,'DD/MM/YYYY') "+
		"    AND TRCD.IDT_CODIGO_NACIONAL = TGAN.IDT_CODIGO_NACIONAL "+
		"    AND TRCD.NUM_CSP = TGCS.NUM_CSP " +
		"    AND TRCD.IDT_MODULACAO = TGCS.IDT_MODULACAO "+ 
		"    AND TRCD.TIP_CHAMADA = TGCS.TIP_CHAMADA "+ 
		"    AND TGCS.IDT_TIPO_SERVICO IN ('CRG','CRB','CRO') "+ 
		"	 AND trim(trcd.idt_plano) = plano.plano "+
		"    AND tgcs.idt_plano_preco = plano.plano "+
		"    GROUP BY TRCD.IDT_CODIGO_NACIONAL,TGCS.IDT_TIPO_SERVICO,plano.prehibrido "+ 
		") TBL1 "+
		"GROUP BY TBL1.IDT_CODIGO_NACIONAL,TBL1.IDT_PRE_HIBRIDO ";
		
		try
		{
			// Execução da Query de Consumo
			Object[] consumoPars = {data, data};
			ResultSet rsConsumo = conexaoPrep.executaPreparedQuery(sqlConsumo, consumoPars, super.getIdLog());
			
			while(rsConsumo.next())
			{
				// Constrói um objeto com os dados consolidados de consumo de cada CN
				cn = rsConsumo.getString("IDT_CODIGO_NACIONAL");
				indPH = rsConsumo.getString("IDT_PRE_HIBRIDO");
				Consumo aConsumo = new Consumo(	cn, 
												indPH,
												rsConsumo.getDouble("VALOR_SERVICOS_BTSA"),
												rsConsumo.getDouble("VALOR_SERVICOS_GSM"),
												rsConsumo.getDouble("VALOR_SERVICOS_TERCEIROS"));
				hashConsumo.put(cn+indPH, aConsumo);
			}			
		}
		catch (SQLException sqlE)
		{
			super.log(Definicoes.ERRO, "constroiHashConsumo", "Erro Banco de Dados: "+sqlE);
			throw new GPPInternalErrorException("Erro Banco Dados: "+sqlE);
		}

		return hashConsumo;
	 }
	 
	 /**
	  * Método...: controiHashSaldos
	  * Descricao: Monta hash com os saldos do dia [data+1]
	  * @param 	PREPConexao		conexaoPrep		Conexão com Banco de Dados
	  * @param 	String			data			Data do final do período (dd/mm/yyyy)
	  * @param 	String			tipoSaldo		Saldo Contabilizado (SP, SB, SS, SD)
	  * @return	HashMap			Hash com os saldos
	  * @throws GPPInternalErrorException
	  */
	 private HashMap controiHashSaldos(PREPConexao conexaoPrep, String data, String tipoSaldo) throws GPPInternalErrorException
	 {
		HashMap hashSaldos = new HashMap();
		String cn = null;
		String indPH = null;
		
		try
		{
			String sqlSaldos = "select rs.idt_codigo_nacional as cn, "+
				"decode(pp.idt_categoria,0,'P','H') as idt_pre_hibrido,"+
				"sum("+this.getCampoReferenteAoSaldo(tipoSaldo, false)+")/100000 as saldo " +     
				"from tbl_rel_saldos rs, tbl_ger_plano_preco pp, tbl_ger_codigo_nacional tgcn "+
				"where trim(rs.idt_plano) = trim(pp.idt_plano_preco) " +
				"and pp.idt_categoria < 2 "+
				"and tgcn.idt_codigo_nacional = rs.idt_codigo_nacional "+
				"and rs.dat_saldo_assinante = to_date(?,'dd/mm/yyyy') + 1"+	// Parametro 1: Data de Processamento
				"and idt_status_assinante in (2,3) "+
				"and tgcn.ind_regiao_brt = 1 "+
				"group by rs.idt_codigo_nacional, decode(pp.idt_categoria,0,'P','H') ";
			
			Object[] sqlSaldosPars = { data };
			
			ResultSet rsSaldos = conexaoPrep.executaPreparedQuery(sqlSaldos, sqlSaldosPars, super.getIdLog());
			
			while(rsSaldos.next())
			{
				// Constrói um objeto com os dados consolidados de consumo de cada CN
				cn = rsSaldos.getString("cn");
				indPH = rsSaldos.getString("IDT_PRE_HIBRIDO");

				hashSaldos.put(cn+indPH, new Double(rsSaldos.getDouble("saldo")));
			}			
		}
		catch (SQLException sqlE)
		{
			super.log(Definicoes.ERRO, "constroiHashSaldos", "Erro Banco de Dados: "+sqlE);
			throw new GPPInternalErrorException("Erro Banco Dados: "+sqlE);
		}

		return hashSaldos;
	 }
	 
	 /**
	  * Metodo...: completaTblIndiceBonificacao
	  * Descricao: Completa a tabela de índices de bonificação para CNs que não tiveram nada
	  * @param conexaoPrep
	  * @param nLinhasInseridas
	  * @param nroLinhasUltimaExecucao
	  * @param data
	  * @param dataUltimaExecucao
	  * @throws GPPInternalErrorException
	  */
//	 private void completaTblIndiceBonificacao(PREPConexao conexaoPrep, int nLinhasInseridas, 
//	 			int nroLinhasUltimaExecucao, String data, Timestamp dataUltimaExecucao, String periodoContabil) throws GPPInternalErrorException
//	 {
//		// Completa TBL_GER_INDICE_BONIFICACAO caso haja algum CN sem chamada registrada no período
//		if(nLinhasInseridas != nroLinhasUltimaExecucao)
//		{
//			String sqlCompletaCN = "INSERT INTO TBL_CON_INDICE_BONIFICACAO "+
//			// Arrumar uma maneira melhor de fazer isso... sem tanta query 
//			"(idt_codigo_nacional, idt_pre_hibrido, idt_periodo_contabil, dat_processamento, idt_tipo_saldo," +
//			"vlr_saldo_recargas_inicial, vlr_saldo_bonus_inicial, vlr_recargas_periodo, vlr_bonus_periodo, " +
//			"vlr_recargas_total, vlr_bonus_total, vlr_perdas_cdr, " +
//			"vlr_servicos_proprios_gsm, vlr_servicos_proprios_btsa, vlr_servicos_outras," +
//			"vlr_indice_bonificacao, vlr_consumo_bonus, vlr_consumo_recargas, " +
//			"vlr_indice_bonificacao_geral_gsm, vlr_indice_bonificacao_geral_btsa," +
//			"vlr_saldo_recargas_final, vlr_saldo_bonus_final, " +
//			"vlr_consumo_bonus_gsm, vlr_consumo_bonus_btsa, " +
//			"vlr_consumo_recargas_gsm, vlr_consumo_recargas_btsa," +
//			"vlr_consumo_recargas_outras, " +
//			"vlr_indice_bonificacao_gsm, vlr_indice_bonificacao_btsa)"+
//
//			"(DAT_PROCESSAMENTO, IDT_PERIODO_CONTABIL, IDT_CODIGO_NACIONAL, " +
//				" VLR_SALDO_RECARGAS_INICIAL, VLR_SALDO_BONUS_INICIAL," +
//				" VLR_RECARGAS_PERIODO, VLR_BONUS_PERIODO," +
//				" VLR_RECARGAS_TOTAL, VLR_BONUS_TOTAL," +
//				" VLR_SERVICOS_PROPRIOS, VLR_SERVICOS_TERCEIROS," +
//				" VLR_INDICE_BONIFICACAO, VLR_CONSUMO_BONUS," +
//				" VLR_CONSUMO_RECARGAS, VLR_INDICE_BONIFICACAO_SP, " +
//				" VLR_SALDO_RECARGAS_FINAL, VLR_SALDO_BONUS_FINAL) "+
//				
//				"SELECT CN_ULT_EXEC, PLANO, ?, TO_DATE(?, 'DD/MM/YYYY'), SALDO, " +		// Parametro 0,1
//				"0,0,0,0,0,0,0,0,0,0,0,0," +
//				"SALDO_FINAL_RECARGAS," +
//				"SALDO_FINAL_BONUS FROM "+
//				"(SELECT TBL_TOTAL.IDT_CODIGO_NACIONAL AS CN_NOVA_EXEC,"+
//				"TBL_ULT_EXEC.CN AS CN_ULT_EXEC, " +
//				"TBL_ULT_EXEC.VRF AS SALDO_FINAL_RECARGAS, " +
//				"TBL_ULT_EXEC.VBF AS SALDO_FINAL_BONUS, " +
//				"TBL_ULT_EXEC.PLANO AS PLANO," +
//				"TBL_ULT_EXEC.TIPO_SALDO AS SALDO FROM "+ 
//
//				"(SELECT IDT_CODIGO_NACIONAL AS CN, " +
//				"VLR_SALDO_RECARGAS_FINAL AS VRF, " +
//				"VLR_SALDO_BONUS_FINAL AS VBF, IDT_PRE_HIBRIDO AS PLANO, IDT_TIPO_SALDO AS TIPO_SALDO FROM TBL_GER_INDICE_BONIFICACAO "+
//				"WHERE DAT_CALCULO = ?) TBL_ULT_EXEC,"+					// Parametro 2
//				"(SELECT TGIB.IDT_CODIGO_NACIONAL FROM TBL_GER_INDICE_BONIFICACAO TGIB "+
//				"WHERE TGIB.DAT_PROCESSAMENTO = (SELECT MAX(DAT_PROCESSAMENTO) FROM TBL_GER_INDICE_BONIFICACAO)) TBL_TOTAL "+	
//				"WHERE TBL_ULT_EXEC.CN = TBL_TOTAL.IDT_CODIGO_NACIONAL (+)) "+
//				"WHERE CN_NOVA_EXEC IS NULL";
//				
//			Object sqlCompletaCNParams[] = 	{	periodoContabil,		// Parametro 0
//												data,					// Parametro 1
//												dataUltimaExecucao,		// Parametro 2
//											};
//			
//			int nLinhasComplementares = 0;
//			try
//			{
//				nLinhasComplementares = conexaoPrep.executaPreparedUpdate(sqlCompletaCN,sqlCompletaCNParams,super.getIdLog());				
//			}
//			catch(GPPInternalErrorException gppE)
//			{
//				super.log(Definicoes.ERRO, "completaTblIndiceBonificacao","Erro Banco de Dados: "+gppE);
//				throw new GPPInternalErrorException("Erro Banco Dados: "+gppE);
//			}
//			
//			super.log(Definicoes.DEBUG,"determinarIndiceBonificacao"," Número de regiões que não apresentaram recargas/consumo: "+nLinhasComplementares);
//		}
//	 }
		
	 /**
	  * Metodo...: getCampoReferenteAoSaldo
	  * Descricao: Retorna o campo referente ao saldo nas tabelas
	  * @param String		tipoSaldo		Tipo do Saldo (SP,SB,SS,SD)
	  * @param boolean		semImposto		true indica que é o campo sem imposto	
	  * @return
	  */
	 private String getCampoReferenteAoSaldo(String tipoSaldo, boolean semImposto)
	 {
		String retorno = null;
		
		if(tipoSaldo.equals("SP"))
			retorno = "vlr_total_principal";
		if(tipoSaldo.equals("SB"))
			retorno = "vlr_total_bonus";
		if(tipoSaldo.equals("SS"))
			retorno = "vlr_total_sms";
		if(tipoSaldo.equals("SD"))
			retorno = "vlr_total_dados";
		if(tipoSaldo.equals("SF"))
			retorno = "VLR_TOTAL_PERIODICO";
		
		// Acrescenta sufixo '_si'  no nome do campo se for sem imposto
		if(retorno != null && semImposto)
		{
			retorno = retorno + "_si";
		}
		
		return retorno;
	 }	
	 
		
	/**
	 * Retorna uma exceção caso exista um total de cdrs sem código sfa superior a R$ 100,00
	 * @param	PREPConexao	conexaoPrep		Conexão com o banco de dados
	 * @param	Double		valorMaximo		Limite máximo de cdrs sem código SFA
	 * @throws 	GPPInternalErrorException 
	 * @throws 	SQLException 
	 */
	private void verificarCodigoCdrSFA(PREPConexao conexaoPrep, Double valorMaximo) throws GPPInternalErrorException, SQLException
	{
		ResultSet rsConsulta = null;
		try
		{
			// Total de chamadas sem códigos SFA cadastrados
			String verificaCDR = 	"SELECT SUM (ABS (rr.vlr_total)) AS vlr_cdr " +
									"  FROM tbl_rel_cdr_dia rr, tbl_ger_plano_preco gpp " +
									" WHERE rr.dat_cdr > ADD_MONTHS(TO_DATE ('28/"+this.periodoContabil+"', 'dd/mm/yyyy'),-1) " +
									"   AND rr.dat_cdr <= TO_DATE ('28/"+this.periodoContabil+"', 'dd/mm/yyyy') " +
									"   AND trunc(rr.idt_plano) = gpp.idt_plano_preco " +
									"   AND gpp.idt_categoria IN (0, 1) " +
									"   AND NOT EXISTS ( " +
									"          SELECT 1 " +
									"            FROM tbl_con_cdr_servico rs " +
									"           WHERE rs.num_csp = rr.num_csp " +
									"             AND rs.idt_modulacao = rr.idt_modulacao " +
									"             AND rs.tip_chamada = rr.tip_chamada " +
									"             AND TRIM (rs.idt_plano_preco) = TRIM (rr.idt_plano))";
			
			// Se o valor de cdr sem código SFA exceder R$ 100,00
			rsConsulta = conexaoPrep.executaPreparedQuery(verificaCDR, null, super.getIdLog());
			if(rsConsulta.next())
				if(rsConsulta.getDouble("vlr_cdr") > valorMaximo.doubleValue())
					throw new GPPInternalErrorException("Existe billing code sem codigo SFA");
		}
		finally
		{
			rsConsulta.close();
		}
	 }	
	
	
	/**
	 * Retorna uma exceção caso exista um total de recargas sem código sfa superior a R$ 100,00
	 * @param	PREPConexao	conexaoPrep		Conexão com o banco de dados	
	 * @param	Double		valorMaximo		Limite máximo de recargas sem código SFA	
	 * @throws 	GPPInternalErrorException 
	 * @throws 	SQLException 
	 */
	private void verificarCodigoRecargaSFA(PREPConexao conexaoPrep, Double valorMaximo) throws GPPInternalErrorException, SQLException
	{
		ResultSet rsConsulta = null;
		try
		{
			// Total de recargas sem códigos SFA cadastrados
			String verificaRecarga = 	"SELECT SUM (ABS (rr.vlr_total)) AS vlr_recarga " +
										"  FROM tbl_rel_recargas rr, " +
										"       tbl_rec_canal c, " +
										"       tbl_rec_origem o, " +
										"       tbl_ger_plano_preco gpp " +
										" WHERE rr.dat_recarga > ADD_MONTHS(TO_DATE ('28/"+this.periodoContabil+"', 'dd/mm/yyyy'),-1) " +
										"   AND rr.dat_recarga <= TO_DATE ('28/"+this.periodoContabil+"', 'dd/mm/yyyy') " +
										"   AND c.id_canal = rr.id_canal " +
										"   AND o.id_canal = rr.id_canal " +
										"   AND o.id_origem = rr.id_origem " +
										"   AND rr.idt_plano = gpp.idt_plano_preco " +
										"   AND gpp.idt_categoria IN (0, 1) " +
										"   AND NOT EXISTS ( " +
										"          SELECT 1 " +
										"            FROM tbl_con_recarga_servico rs " +
										"           WHERE rs.id_origem = rr.id_origem " +
										"             AND rs.id_sistema_origem = rr.id_sistema_origem " +
										"             AND rs.id_canal = rr.id_canal " +
										"             AND TRIM (rs.idt_plano_preco) = TRIM (rr.idt_plano))";
			
			// Se o valor de recargas sem código SFA exceder R$ 100,00
			rsConsulta = conexaoPrep.executaPreparedQuery(verificaRecarga, null, super.getIdLog());
			if(rsConsulta.next())
				if(rsConsulta.getDouble("vlr_recarga") > valorMaximo.doubleValue())
					throw new GPPInternalErrorException("Existe recarga sem codigo SFA");
		}
		finally
		{
			rsConsulta.close();
		}
	 }
}