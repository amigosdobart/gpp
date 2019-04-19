package com.brt.gpp.aplicacoes.enviarBonusCSP14;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
  *
  * Este arquivo refere-se à classe EmitirNFBonusTLDC, responsavel pela implementacao 
  * do processo de população da tabela de interfaces para emissão de NF referentes aos
  * bônus fornecidos no processo EnvioBonusCSP14
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				12/07/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class EmitirNFBonusTLDC extends Aplicacoes 
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
		     
	/**
	 * Metodo...: EmitirNFBonusTLDC
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public EmitirNFBonusTLDC (long logId)
	 {
		super(logId, Definicoes.CL_EMITIR_NF_BONUS_TLDC);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: selecionaBonusTLDC
	 * Descricao: Procura na TBL_REC_RECARGAS todos os bonus TLDC que foram repassados
	 * no dia da data passada como parâmetro
	 * @param String	aData	Usada somente para registro na tabela de histórico de processos	
	 * @return	short			0=processamento ok; !0: erro
	 * @throws GPPInternalErrorException
	 */
	public short selecionaBonusTLDC(String aData) throws GPPInternalErrorException
	{
		short retorno = 0;
		String status = null;
		PREPConexao conexaoPrep = null;
		int nRegistrosInseridos = 0;
		String dataInicial = null;
		ResultSet rsData = null;
		Timestamp dataUltimaExecucao = null;
		
		super.log(Definicoes.INFO, "selecionaBonusTLDC", "Inicio aData "+aData);

		try
		{
			// Registra Data/Hora do início do processo
			dataInicial = GPPData.dataCompletaForamtada();
			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			// Verifica qual é a data da última execução bem sucedida do processo batch
			String sqlData = 	"SELECT MAX(DAT_INICIAL_EXECUCAO) FROM TBL_GER_HISTORICO_PROC_BATCH "+
								"WHERE ID_PROCESSO_BATCH = ? " +
								"AND IDT_STATUS_EXECUCAO = ?";
								
			Object sqlDataParam[] = {new Integer (Definicoes.IND_EMITIR_NF_TLDC), Definicoes.PROCESSO_SUCESSO};
			
			// Determina a última data de execução do processo batch
			rsData = conexaoPrep.executaPreparedQuery(sqlData, sqlDataParam, super.logId);
			if (rsData.next())
			{
				dataUltimaExecucao = rsData.getTimestamp(1);
			}
			
			// Atribui o valor 01/01/2004 00:00:00 caso não haja registro de última execução p/ o processo
			if(dataUltimaExecucao == null)
			{
				dataUltimaExecucao = Timestamp.valueOf(Definicoes.INICIO_DOS_TEMPOS+" "+ Definicoes.HORA_INICIO_DIA);
			}
			rsData.close();			

			// Query para busca de bônus da TBL_REC_RECARGAS
		//	   String sql = "INSERT INTO TBL_INT_CONTABILIZACAO_RECARGA "+
//			    "(DAT_PROCESSAMENTO, TIP_RECARGA,IDT_CODIGO_NACIONAL, VLR_TOTAL_CREDITOS, IDT_STATUS_PROCESSAMENTO) " +
//			    "SELECT SYSDATE, ?, SUBSTR(REC.IDT_MSISDN,3,2), SUM(VLR_CREDITO), ? " +
//			    "FROM TBL_REC_RECARGAS REC " + 
//			    "WHERE TIP_TRANSACAO = ? " +
//			    "AND REC.DAT_RECARGA BETWEEN to_date(?,'DD/MM/YYYY HH24:MI:ss') AND SYSDATE " +
//			    "GROUP BY SUBSTR(REC.IDT_MSISDN,3,2)";
			   
			   String sql = "INSERT INTO TBL_INT_CONTABILIZACAO_RECARGA " +
		   		"(DAT_PROCESSAMENTO, TIP_RECARGA,IDT_CODIGO_NACIONAL, VLR_TOTAL_CREDITOS, IDT_STATUS_PROCESSAMENTO, IDT_CODIGO_SERVICO, NUM_CONTRATO) " +
		   		"select consolidada.data,?,consolidada.cn,consolidada.valor*(1-tblImpostos.vlr_aliquota),?,numcontrato.idt_codigo_servico,numcontrato.num_contrato from "+
		   		"( "+
				"select sysdate as data, substr(rec.idt_msisdn,3,2) as cn, sum(rec.vlr_credito) as valor "+
				"from tbl_rec_recargas rec "+
				"where tip_transacao = ? "+ 
				"and rec.dat_recarga between to_date(?,'DD/MM/YYYY HH24:MI:ss') and SYSDATE "+ 
				"group by substr(rec.idt_msisdn,3,2) "+
				") consolidada, tbl_ger_num_contrato numcontrato, tbl_ger_aliquota tblImpostos "+
				"where consolidada.cn = numcontrato.idt_codigo_nacional " +
				"and tblImpostos.idt_codigo_nacional = consolidada.cn "+
				"and tblImpostos.idt_imposto = ?";		
			   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			   Object sqlParam[] = {Definicoes.RECARGA_BONUS_CSP14, Definicoes.IND_LINHA_NAO_PROCESSADA,
			   						Definicoes.RECARGA_BONUS_CSP14, sdf.format((java.util.Date)dataUltimaExecucao),
									Definicoes.IMPOSTO_TLDC};

			// Insere linhas na TBL_INT_CONTABILIZACAO_RECARGAS
			nRegistrosInseridos = conexaoPrep.executaPreparedUpdate(sql, sqlParam, super.logId);

			// String no histórico apresentará sucesso
			status = Definicoes.TIPO_OPER_SUCESSO;
		}
		catch(GPPInternalErrorException e)
		{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.ERRO, "selecionaBonusTLDC", "Excecao Interna GPP:"+ e);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida:" + e);					
		}
		catch(SQLException eSQL)
		{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.ERRO, "selecionaBonusTLDC", "Excecao SQL:"+ eSQL);
			throw new GPPInternalErrorException ("Excecao SQL:" + eSQL);					
		}
		finally
		{
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Limpeza dos registros já processados pelo ETI
			String sqlDelete = "DELETE TBL_INT_CONTABILIZACAO_RECARGA "+
							"WHERE IDT_STATUS_PROCESSAMENTO = ? " +
							" and dat_processamento < (sysdate - ?)";
			
			Object sqlDeleteParam[] = {Definicoes.IND_LINHA_TRANSFERIDA,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
			
			int nRegistrosDeletados = conexaoPrep.executaPreparedUpdate(sqlDelete,sqlDeleteParam,super.logId);
			super.log(Definicoes.INFO,"selecionaBonusTLDC","Foram deletados "+nRegistrosDeletados+" que já foram processados pelo ETI");

			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Chama a funcao para gravar no historico o Processo em questao
			String descricao = "Foram Contabilizadas " + nRegistrosInseridos + " entradas na TBL_INT_CONTABILIZACAO_RECARGA";
			String dataFinal = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_EMITIR_NF_TLDC, dataInicial, dataFinal, status, descricao, aData);
		
			super.log(Definicoes.INFO, "selecionaBonusTLDC", "Fim");			
		}
		return retorno;
	}
}
