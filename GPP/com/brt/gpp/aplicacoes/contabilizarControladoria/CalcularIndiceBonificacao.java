package com.brt.gpp.aplicacoes.contabilizarControladoria;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import com.brt.gpp.comum.*;

// Arquivos Java
import java.sql.*;

/**
  * Este arquivo refere-se à classe CalcularIndiceBonificacao, responsavel pela implementacao 
  * do processo de Cálculo dos Índices de Bonificação a fim de se determinar o percentual de Consumo Próprio
  * e/ou de Terceiros
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				20/07/2004
  *
  * Modificado por: Alberto Magno
  * Data:  19/10/2004
  * Razao: Correção para evitar replicação e divisão por zero.
  *
  */
public class CalcularIndiceBonificacao extends Aplicacoes 
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
		     
	/**
	 * Metodo...: CalcularIndiceBonificacao
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public CalcularIndiceBonificacao (long logId)
	 {
		super(logId, Definicoes.CL_INDICE_BONIFICACAO);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }
	 
	 
	 /**
	  * Metodo...: determinarIndiceBonificacao
	  * Descricao: Determina o Indice de Bonificacao de cada CN e as parcelas referentes
	  * 			a bonus/recarga dos consumos próprios além do consumo de terceiros
	  * @param 	String	data		Data de Referência para histórico (DD/MM/YYYY)
	  * @return	short	0, se ok; !0 se nok
	  * @throws GPPInternalErrorException
	  */
	 public short determinarIndiceBonificacao(String data) throws GPPInternalErrorException
	 {
		String status = null;
		PREPConexao conexaoPrep = null; 
		int nLinhasInseridas = 0;
		int nLinhasComplementares = 0;
		String dataInicial = null;
		short retorno = 0;
		ResultSet rsUltimaExecucao = null;
		int nroLinhasUltimaExecucao = 0;
		Timestamp dataUltimaExecucao = null;
		
		try
		{
			super.log(Definicoes.INFO, "determinarIndiceBonificacao", "Inicio DATA "+data);
			
			// Pega conexão com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());

/*******************************************************************************************************************/
			// Limpa tabela com índices calculados para aquele período
			String query = "DELETE FROM TBL_GER_INDICE_BONIFICACAO WHERE to_char(DAT_CALCULO,'/MM/YYYY') = ?";
			// Deleta TBL_REL_CDR_HORA
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Delecao dados tabela Índices de Bonificação");
			conexaoPrep.executaPreparedQuery(query,new Object[]{data.substring(2)},super.getIdLog());
/*******************************************************************************************************************/
			
			// Registra Data/Hora do início do processo
			dataInicial = GPPData.dataCompletaForamtada();
			
			//Determina a quantidade de registros inseridos na última execução e a data da última execução
			String sqlUltimaExecucao = "SELECT COUNT(IDT_CODIGO_NACIONAL) AS QTD, MAX(DAT_CALCULO) AS DATA FROM TBL_GER_INDICE_BONIFICACAO "+ 
					"WHERE DAT_CALCULO = (SELECT MAX(DAT_CALCULO) FROM TBL_GER_INDICE_BONIFICACAO)";
					
			rsUltimaExecucao = conexaoPrep.executaQuery(sqlUltimaExecucao,super.getIdLog());
			if(rsUltimaExecucao.next())
			{
				nroLinhasUltimaExecucao = rsUltimaExecucao.getInt("QTD");
				dataUltimaExecucao = rsUltimaExecucao.getTimestamp("DATA");
			}
		 	
			// Se não tem nada ainda na tabela, é necessário preenche-la zerada
			if(dataUltimaExecucao == null)
			{
				conexaoPrep.executaQuery("insert into tbl_ger_indice_bonificacao " +
						"select IDT_CODIGO_NACIONAL,to_date('"+Definicoes.INICIO_DOS_TEMPOS+"','YYYY-MM-DD'),0,0,0,0,0,0,0,0,0,0,0,0,0,0,PLANO " +
						"from (select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1), " +
						"((SELECT 'P' AS PLANO FROM DUAL)UNION(SELECT 'H' AS PLANO FROM DUAL))",super.getIdLog());
				rsUltimaExecucao = conexaoPrep.executaQuery(sqlUltimaExecucao,super.getIdLog());
				rsUltimaExecucao.next();
				nroLinhasUltimaExecucao = rsUltimaExecucao.getInt("QTD");
				dataUltimaExecucao = rsUltimaExecucao.getTimestamp("DATA");
			}
			// Insere Registros na tabela TBL_GER_INDICE_BONIFICACAO
			String sqlInsert = "INSERT INTO TBL_GER_INDICE_BONIFICACAO "+
				"(DAT_CALCULO, IDT_CODIGO_NACIONAL,VLR_SALDO_RECARGAS_INICIAL,VLR_SALDO_BONUS_INICIAL,"+
				"VLR_RECARGAS_PERIODO, VLR_BONUS_PERIODO,VLR_RECARGAS_TOTAL,VLR_BONUS_TOTAL,"+
				"VLR_SERVICOS_PROPRIOS, VLR_SERVICOS_TERCEIROS,VLR_INDICE_BONIFICACAO,"+
				"VLR_CONSUMO_BONUS,VLR_CONSUMO_RECARGAS,VLR_INDICE_BONIFICACAO_SP,VLR_SALDO_RECARGAS_FINAL,"+
				"VLR_SALDO_BONUS_FINAL, IDT_PRE_HIBRIDO) "+
				"SELECT "+
				"	TO_DATE(?,'DD/MM/YYYY'), "+		// Parâmetro 0
				"	TQQC.IDT_CODIGO_NACIONAL,"+
				"	TQQC.VLR_SALDO_RECARGAS_INICIAL,"+
				"	TQQC.VLR_SALDO_BONUS_INICIAL,"+
				"	TQQC.VLR_RECARGAS_PERIODO,"+
				"	TQQC.VLR_BONUS_PERIODO,"+
				"	TQQC.VLR_RECARGAS_TOTAL,"+
				"	TQQC.VLR_BONUS_TOTAL,"+
				"	TQQC.VLR_SERVICOS_PROPRIOS,"+
				"	TQQC.VLR_SERVICOS_TERCEIROS,"+
				"	TQQC.VLR_INDICE_BONIFICACAO,"+
				"	TQQC.VLR_CONSUMO_BONUS,"+
				"	TQQC.VLR_CONSUMO_RECARGAS,"+
				"	DECODE(TQQC.VLR_SERVICOS_PROPRIOS,0,0,TQQC.VLR_CONSUMO_BONUS/VLR_SERVICOS_PROPRIOS) AS VLR_INDICE_BONIFICACAO_SP,"+
				"	TQQC.VLR_RECARGAS_TOTAL - VLR_CONSUMO_RECARGAS AS VLR_SALDO_RECARGAS_FINAL,"+
				"	TQQC.VLR_BONUS_TOTAL - VLR_CONSUMO_BONUS AS VLR_SALDO_BONUS_FINAL," +
				"	TQQC.IDT_PRE_HIBRIDO "+
				"FROM "+
				"		(" +
				"		SELECT "+
				"			TQC.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+
				"			TQC.VLR_SALDO_RECARGAS_FINAL AS VLR_SALDO_RECARGAS_INICIAL,"+
				"			TQC.VLR_SALDO_BONUS_FINAL AS VLR_SALDO_BONUS_INICIAL,"+
				"			TQC.VLR_RECARGAS_PERIODO AS VLR_RECARGAS_PERIODO,"+
				"			TQC.VLR_BONUS_PERIODO AS VLR_BONUS_PERIODO,"+
				"			TQC.VLR_RECARGAS_TOTAL AS VLR_RECARGAS_TOTAL,"+
				"			TQC.VLR_BONUS_TOTAL AS VLR_BONUS_TOTAL,"+
				"			TQC.VLR_SERVICOS_PROPRIOS AS VLR_SERVICOS_PROPRIOS,"+
				"			TQC.VLR_SERVICOS_TERCEIROS AS VLR_SERVICOS_TERCEIROS,"+
				"			TQC.VLR_INDICE_BONIFICACAO AS VLR_INDICE_BONIFICACAO,"+
				"			(TQC.VLR_SERVICOS_PROPRIOS + TQC.VLR_SERVICOS_TERCEIROS)*TQC.VLR_INDICE_BONIFICACAO AS VLR_CONSUMO_BONUS,"+
				"			(TQC.VLR_SERVICOS_PROPRIOS + TQC.VLR_SERVICOS_TERCEIROS)*(1-TQC.VLR_INDICE_BONIFICACAO) AS VLR_CONSUMO_RECARGAS, " +
				"			TQC.IDT_PRE_HIBRIDO "+
				"		FROM "+
				"			(" +
				"			SELECT "+
				"				TBL_BONUS_RECARGA.VLR_SALDO_RECARGAS_FINAL AS VLR_SALDO_RECARGAS_FINAL,"+ 
				"				TBL_BONUS_RECARGA.VLR_SALDO_BONUS_FINAL AS VLR_SALDO_BONUS_FINAL,"+
				"				TBL_BONUS_RECARGA.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+
				"				TBL_BONUS_RECARGA.VLR_RECARGAS_PERIODO AS VLR_RECARGAS_PERIODO,"+
				"				TBL_BONUS_RECARGA.VLR_BONUS_PERIODO AS VLR_BONUS_PERIODO,"+
				"				TBL_BONUS_RECARGA.VLR_RECARGAS_TOTAL AS VLR_RECARGAS_TOTAL,"+
				"				TBL_BONUS_RECARGA.VLR_BONUS_TOTAL AS VLR_BONUS_TOTAL,"+
				"				TBL_BONUS_RECARGA.VLR_INDICE_BONIFICACAO AS VLR_INDICE_BONIFICACAO,"+
				"				DECODE(TBL_CONSUMO.VALOR_SERVICOS_PROPRIOS,NULL,0,TBL_CONSUMO.VALOR_SERVICOS_PROPRIOS)+ TBL_BONUS_RECARGA.VLR_CONS_PROPRIO AS VLR_SERVICOS_PROPRIOS,"+
				"				DECODE(TBL_CONSUMO.VALOR_SERVICOS_TERCEIROS,NULL,0,TBL_CONSUMO.VALOR_SERVICOS_TERCEIROS) + TBL_BONUS_RECARGA.VLR_CONS_TERCEIROS AS VLR_SERVICOS_TERCEIROS, " +
				"				TBL_BONUS_RECARGA.IDT_PRE_HIBRIDO "+
				"			FROM "+
				"				(" +
				"				SELECT " +
				"					VLR_SALDO_RECARGAS_FINAL AS VLR_SALDO_RECARGAS_FINAL,"+
				"					VLR_SALDO_BONUS_FINAL AS VLR_SALDO_BONUS_FINAL,"+
				"					IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+
				"					VLR_RECARGAS_PERIODO AS VLR_RECARGAS_PERIODO,"+
				"					VLR_BONUS_PERIODO AS VLR_BONUS_PERIODO,"+
				"					VLR_CONS_TERCEIROS AS VLR_CONS_TERCEIROS,"+
				"					VLR_CONS_PROPRIO AS VLR_CONS_PROPRIO,"+
				"					VLR_RECARGAS_TOTAL AS VLR_RECARGAS_TOTAL,"+
				"					VLR_BONUS_TOTAL AS VLR_BONUS_TOTAL,"+
				"					DECODE(VLR_RECARGAS_TOTAL+VLR_BONUS_TOTAL,0,0,VLR_BONUS_TOTAL/(VLR_RECARGAS_TOTAL+VLR_BONUS_TOTAL)) AS VLR_INDICE_BONIFICACAO, " +
				"					IDT_PRE_HIBRIDO "+
				"				FROM "+
				"					(" +
				"					SELECT "+ 
				"						TGIB.VLR_SALDO_RECARGAS_FINAL AS VLR_SALDO_RECARGAS_FINAL,"+
				"						TGIB.VLR_SALDO_BONUS_FINAL AS VLR_SALDO_BONUS_FINAL,"+
				"						TBL_VALORES_PERIODO.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+
				"						TBL_VALORES_PERIODO.VALOR_RECARGAS_PERIODO AS VLR_RECARGAS_PERIODO,"+
				"						TBL_VALORES_PERIODO.VALOR_BONUS_PERIODO AS VLR_BONUS_PERIODO,"+
				"						TBL_VALORES_PERIODO.VALOR_CONSUMO_TERCEIROS AS VLR_CONS_TERCEIROS,"+
				"						TBL_VALORES_PERIODO.VALOR_CONSUMO_PROPRIO AS VLR_CONS_PROPRIO,"+
				"						VALOR_RECARGAS_PERIODO+VLR_SALDO_RECARGAS_FINAL AS VLR_RECARGAS_TOTAL,"+
				"						VALOR_BONUS_PERIODO+VLR_SALDO_BONUS_FINAL AS VLR_BONUS_TOTAL," +
				"						TBL_VALORES_PERIODO.IDT_PRE_HIBRIDO "+
				"					FROM "+
				"						(" +
				"						SELECT "+ 
				"							TBL1.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+
				"							SUM(DECODE(IDT_TIPO_SERVICO,?,TOTAL_RECARGAS,?)) AS VALOR_RECARGAS_PERIODO,"+	// Parametro 1,2
				"							SUM(DECODE(IDT_TIPO_SERVICO,?,TOTAL_RECARGAS,?)) AS VALOR_BONUS_PERIODO,"+		// Parametro 3,4
				"							SUM(DECODE(IDT_TIPO_SERVICO,?,TOTAL_RECARGAS,?)) AS VALOR_CONSUMO_TERCEIROS,"+	// Parametro 5,6
				"							SUM(DECODE(IDT_TIPO_SERVICO,?,TOTAL_RECARGAS,?)) AS VALOR_CONSUMO_PROPRIO, "+	// Parametro 7,8
				"							TBL1.IDT_PRE_HIBRIDO "+	
				"						FROM "+
				"							(" +
				"							SELECT "+ 
				"								TRR.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+ 
				"								TGRS.IDT_TIPO_SERVICO AS IDT_TIPO_SERVICO,"+
				"								SUM(TRR.VLR_TOTAL_SI) AS TOTAL_RECARGAS, "+
				"								plano.prehibrido as IDT_PRE_HIBRIDO "+
				"							FROM " +
				"								TBL_REL_RECARGAS TRR, " +
				"								(select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1) TGAN, " +
				"								TBL_GER_RECARGA_SERVICO TGRS, "+
				"								(select idt_plano_preco as plano, decode(idt_categoria,0,'"+Definicoes.COD_ASSINANTE_PREPAGO+"','"+Definicoes.COD_ASSINANTE_HIBRIDO+"') as prehibrido from tbl_ger_plano_preco where idt_categoria < 2) plano "+
				"								WHERE " +
				"									TRR.DAT_RECARGA BETWEEN add_months(to_date('27/'||substr(?,4,8),'DD/MM/YYYY'),-1)+2	" +
				"											and to_date('28/'||substr(?,4,8),'DD/MM/YYYY')	"+	// Parametro 9,10
				"									AND TRR.IDT_CODIGO_NACIONAL = TGAN.IDT_CODIGO_NACIONAL "+
				"									AND TGRS.ID_CANAL = TRR.ID_CANAL "+
				"									AND TGRS.ID_ORIGEM = TRR.ID_ORIGEM "+
				"									AND TGRS.ID_SISTEMA_ORIGEM = TRR.ID_SISTEMA_ORIGEM "+
				"									AND trR.idt_plano = plano.plano "+
				"									AND tgRs.idt_pre_hibrido = plano.prehibrido "+	
				"							GROUP BY " +
				"									TRR.IDT_CODIGO_NACIONAL," +
				"									TGRS.IDT_TIPO_SERVICO," +
				"									plano.prehibrido " +
				"							) TBL1 "+
				"						GROUP BY " +
				"							TBL1.IDT_CODIGO_NACIONAL," +
				"							TBL1.IDT_PRE_HIBRIDO " +
				"						) TBL_VALORES_PERIODO,"+
				"						TBL_GER_INDICE_BONIFICACAO TGIB "+
				"					WHERE "+
				"						TGIB.IDT_CODIGO_NACIONAL (+)= TBL_VALORES_PERIODO.IDT_CODIGO_NACIONAL AND " +
				"						TGIB.IDT_PRE_HIBRIDO = TBL_VALORES_PERIODO.IDT_PRE_HIBRIDO and "+
				"						TGIB.DAT_CALCULO = ? "+	// Parametro 11
				"					)" +
				"				) TBL_BONUS_RECARGA, "+
				"				(" +
				"				SELECT "+
				"					TBL1.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+
				"					SUM(DECODE(IDT_TIPO_SERVICO,?,TOTAL_CHAMADAS,?)) AS VALOR_SERVICOS_PROPRIOS,"+	// Parametro 12,13
				"					SUM(DECODE(IDT_TIPO_SERVICO,?,TOTAL_CHAMADAS,?)) AS VALOR_SERVICOS_TERCEIROS, " + // Parametro 14,15
				"					TBL1.IDT_PRE_HIBRIDO "+	
				"				FROM "+
				"					(" +
				"					SELECT "+ 
				"						TRCD.IDT_CODIGO_NACIONAL AS IDT_CODIGO_NACIONAL,"+ 
				"						TGCS.IDT_TIPO_SERVICO AS IDT_TIPO_SERVICO,"+
				"						SUM(TRCD.VLR_TOTAL_SI) AS TOTAL_CHAMADAS, " +
				"						plano.prehibrido as IDT_PRE_HIBRIDO "+
				"					FROM " +
				"						TBL_REL_CDR_DIA TRCD, " +
				"						(select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1) TGAN, " +
				"						TBL_GER_CDR_SERVICO TGCS," +
				"						(select idt_plano_preco as plano, decode(idt_categoria,0,'"+Definicoes.COD_ASSINANTE_PREPAGO+"','"+Definicoes.COD_ASSINANTE_HIBRIDO+"') as prehibrido from tbl_ger_plano_preco where idt_categoria < 2) plano "+
				"					WHERE " +
				"						TRCD.DAT_CDR BETWEEN  add_months(to_date('27/'||substr(?,4,8),'DD/MM/YYYY'),-1)+2	" +
				"							and to_date('28/'||substr(?,4,8),'DD/MM/YYYY')	" +	// Parametros 16,17
				"						AND TRCD.IDT_CODIGO_NACIONAL = TGAN.IDT_CODIGO_NACIONAL "+
				"						AND TRCD.NUM_CSP = TGCS.NUM_CSP "+
				"						AND TRCD.IDT_MODULACAO = TGCS.IDT_MODULACAO "+
				"						AND TRCD.TIP_CHAMADA = TGCS.TIP_CHAMADA "+
				"						AND TGCS.IDT_TIPO_SERVICO IN (?,?) " + // Parametros 18, 19
				"						AND trim(trcd.idt_plano) = plano.plano "+
				"						AND tgcs.idt_pre_hibrido = plano.prehibrido "+	
				"					GROUP BY " +
				"						TRCD.IDT_CODIGO_NACIONAL," +
				"						TGCS.IDT_TIPO_SERVICO," +
				"						plano.prehibrido " +
				"					) TBL1 "+
				"			GROUP BY " +
				"				TBL1.IDT_CODIGO_NACIONAL," +
				"				TBL1.IDT_PRE_HIBRIDO " +
				"			) TBL_CONSUMO "+
				"		WHERE " +
				"			TBL_CONSUMO.IDT_CODIGO_NACIONAL (+)= TBL_BONUS_RECARGA.IDT_CODIGO_NACIONAL " +
				"			and TBL_CONSUMO.idt_pre_hibrido = TBL_BONUS_RECARGA.idt_pre_hibrido " +
				"		) " +
				"	TQC" +
				"	) TQQC";
				
			Object sqlParams[] = {	data,										// Parametro 0
									Definicoes.TIPO_SERVICO_RECARGA,			// Parametro 1
									new Integer(Definicoes.CONSTANTE_ZERO),		// Parametro 2
									Definicoes.TIPO_SERVICO_BONUS,				// Parametro 3
									new Integer(Definicoes.CONSTANTE_ZERO),		// Parametro 4
									Definicoes.TIPO_SERVICO_CONSUMO_TERCEIROS,	// Parametro 5 
									new Integer(Definicoes.CONSTANTE_ZERO),		// Parametro 6
									Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO,	// Parametro 7
									new Integer(Definicoes.CONSTANTE_ZERO),		// Parametro 8
									data, data,		// Parametro 9, 10
									dataUltimaExecucao,							// Parametro 11
									Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO,	// Parametro 12
									new Integer(Definicoes.CONSTANTE_ZERO),		// Parametro 13
									Definicoes.TIPO_SERVICO_CONSUMO_TERCEIROS,	// Parametro 14
									new Integer(Definicoes.CONSTANTE_ZERO),		// Parametro 15
									data, data,		// Parametro 16, 17
									Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO,	// Parametro 18
									Definicoes.TIPO_SERVICO_CONSUMO_TERCEIROS	// Parametro 19
								};
					
			// Executa Query
			nLinhasInseridas = conexaoPrep.executaPreparedUpdate(sqlInsert,sqlParams,super.getIdLog());
			super.log(Definicoes.INFO,"determinarIndiceBonificacao","Foram inseridas "+nLinhasInseridas+" na TBL_GER_INDICE_BONIFICACAO");

			// Completa TBL_GER_INDICE_BONIFICACAO caso haja algum CN sem chamada registrada no período
			if(nLinhasInseridas != nroLinhasUltimaExecucao)
			{
				String sqlCompletaCN = "INSERT INTO TBL_GER_INDICE_BONIFICACAO "+
					"(DAT_CALCULO, IDT_CODIGO_NACIONAL, " +
					" VLR_SALDO_RECARGAS_INICIAL,VLR_SALDO_BONUS_INICIAL," +
					" VLR_RECARGAS_PERIODO, VLR_BONUS_PERIODO," +
					" VLR_RECARGAS_TOTAL, VLR_BONUS_TOTAL," +
					" VLR_SERVICOS_PROPRIOS, VLR_SERVICOS_TERCEIROS," +
					" VLR_INDICE_BONIFICACAO, VLR_CONSUMO_BONUS," +
					" VLR_CONSUMO_RECARGAS, VLR_INDICE_BONIFICACAO_SP, " +
					" VLR_SALDO_RECARGAS_FINAL, VLR_SALDO_BONUS_FINAL) "+
					"SELECT TO_DATE(?, 'DD/MM/YYYY'), " +		// Parametro 0					"CN_ULT_EXEC," +
					"0,0,0,0,0,0,0,0,0,0,0,0," +					"SALDO_FINAL_RECARGAS," +					"SALDO_FINAL_BONUS FROM "+
					"(SELECT TBL_TOTAL.IDT_CODIGO_NACIONAL AS CN_NOVA_EXEC,"+
					"TBL_ULT_EXEC.CN AS CN_ULT_EXEC, " +					"TBL_ULT_EXEC.VRF AS SALDO_FINAL_RECARGAS, " +					"TBL_ULT_EXEC.VBF AS SALDO_FINAL_BONUS FROM "+ 
					"(SELECT IDT_CODIGO_NACIONAL AS CN, " +					"VLR_SALDO_RECARGAS_FINAL AS VRF, " +					"VLR_SALDO_BONUS_FINAL AS VBF FROM TBL_GER_INDICE_BONIFICACAO "+
					"WHERE DAT_CALCULO = ?) TBL_ULT_EXEC,"+					// Parametro 1
					"(SELECT TGIB.IDT_CODIGO_NACIONAL FROM TBL_GER_INDICE_BONIFICACAO TGIB "+
					"WHERE TGIB.DAT_CALCULO = (SELECT MAX(DAT_CALCULO) FROM TBL_GER_INDICE_BONIFICACAO)) TBL_TOTAL "+	
					"WHERE TBL_ULT_EXEC.CN = TBL_TOTAL.IDT_CODIGO_NACIONAL (+)) "+
					"WHERE CN_NOVA_EXEC IS NULL";
					
				Object sqlCompletaCNParams[] = 	{
													data,					// Parametro 0
													dataUltimaExecucao,		// Parametro 1
												};
				
				nLinhasComplementares = conexaoPrep.executaPreparedUpdate(sqlCompletaCN,sqlCompletaCNParams,super.getIdLog());			

				super.log(Definicoes.DEBUG,"determinarIndiceBonificacao"," Número de regiões que não apresentaram recargas/consumo: "+nLinhasComplementares);	
			}

			// String no histórico apresentará sucesso
			status = Definicoes.TIPO_OPER_SUCESSO;					
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
			super.gravaHistoricoProcessos(Definicoes.IND_INDICE_BONIFICACAO, dataInicial, dataFinal, status, descricao, data);
		
			super.log(Definicoes.INFO, "determinarIndiceBonificacao", "Fim");			
		}
		return retorno;	 
	}
}
