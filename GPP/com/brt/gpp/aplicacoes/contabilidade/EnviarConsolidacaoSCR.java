package com.brt.gpp.aplicacoes.contabilidade;

//Arquivo de Imports de Gerentes do GPP 
import java.sql.ResultSet;

import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

//Arquivos de Import Internos
import com.brt.gpp.comum.*;

/**
* Este arquivo refere-se à classe EnviarConsolidacaoSCR, responsavel pela implementacao 
* do processo de envio de dados consolidados de receita para o SCR
*
* <P> Versao:			1.0
*
* @Autor: 			Denys Oliveira
* Data: 				14/07/2004 
*
*/
public class EnviarConsolidacaoSCR extends Contabilidade
{
	/**
	 * Metodo...: EnviarConsolidacaoSCR
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public EnviarConsolidacaoSCR (long logId, String aDataReferencia) throws GPPInternalErrorException
	 {
		super(logId, Definicoes.CL_CONSOLIDACAO_SCR, aDataReferencia);
	 }
	 
	 /***
	  * Metodo...: gerarConsolidacaoSCR
	  * Descricao: Preencher a tabela TBL_INT_CONTABIL a partir dos dados disponíveis na
	  * TBL_REL_CONTABIL
	  * @param aData	Data de Referencia, a ser usada no histórico do processo
	  * @return	short	0, se ok; !0 se houver erro
	  * @throws GPPInternalErrorException
	  */
	 public short gerarConsolidacaoSCR() throws GPPInternalErrorException
	 {
		String status = null;
		PREPConexao conexaoPrep = null; 
		int nLinhasInseridas = 0;
		String dataInicial = null;
		short retorno = 0;
		
	 	try
	 	{
	 		super.log(Definicoes.INFO, "gerarConsolidacaoSCR", "Inicio DATA "+super.dataReferencia);
			
	 		// Pega conexão com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			if(verificarRegistros(conexaoPrep) == Definicoes.RET_SEM_REGISTRO_ULTIMA_EXECUCAO)
	            return Definicoes.RET_SEM_REGISTRO_ULTIMA_EXECUCAO;

	 		/*******************************************************************************************************************/
			// Limpa tabela 
			String query = "DELETE FROM TBL_INT_CONTABIL WHERE DAT_SERVICO = TO_NUMBER(substr(?,7,4)||substr(?,4,2))";
			// Deleta TBL_REL_CONTABIL
			super.log(Definicoes.INFO,"consolidaContabil","Delecao dados tabela TBL_INT_CONTABIL");
			conexaoPrep.executaPreparedQuery(query,new Object[]{super.dataReferencia,super.dataReferencia},super.getIdLog());
			/*******************************************************************************************************************/
			// Registra Data/Hora do início do processo
			dataInicial = GPPData.dataCompletaForamtada();
			
			
			//	Query para inserir na tabela TBL_INT_CONTABIL
		 	String sqlInsert = "INSERT INTO TBL_INT_CONTABIL "+
		 	" (num_registro, dat_processamento, num_ciclo_faturamento, " +
		 	"  tip_registro, idt_localidade, idt_central, id_categoria, " +
		 	"  idt_unidade_negocio, idt_codigo_servico, num_sequencial_telefone, " +
		 	"  qtd_fisica, vlr_liquido, vlr_icms, vlr_iss, nom_orgao, vlr_pasep, " +
		 	"  vlr_cofins, tip_processamento, dat_servico, idt_uf, " +
		 	"  num_segmento_carteira, idt_roaming_origin, idt_empresa, " +
		 	"  idt_status_processamento, idt_cod_cnl, des_filler, tip_pessoa, " +
		 	"  num_cpf_cnpj, idt_unidade_medicao, idt_plano_brt, idt_plano_anatel, " +
		 	"  idt_eot_origem, idt_eot_destino, idt_setor_anatel, idt_trailer, " +
		 	"  idt_info_anatel) "+
			"SELECT "+  
			"  seq_registro_contabil.NEXTVAL, dat_processamento, ciclo_faturamento, " +
			"  tipo_registro, localidade, central, categoria, " +
			"  unidade_negocio, codigo_servico, sequencial_telefone,  " +
			"  qtd_fisica, valor_liquido, valor_icms, valor_iss, orgao, valor_pasep,  " +
			"  valor_cofins, tipo_processamento, dat_servico, unidade_federativa, " +
			"  segmento_carteira, cod_eot_origem, empresa,  " +
			"  idt_status_processamento, idt_cod_cnl, des_filler, tip_pessoa, " +
			"  num_cpf_cnpj, unidade_medicao, idt_plano_brt, idt_plano_anatel, " +
			"  idt_eot_origem, idt_eot_destino, idt_setor_anatel, idt_trailer, " +
			"  idt_info_anatel "+
			"FROM "+  
			"(SELECT "+  
			"SYSDATE AS DAT_PROCESSAMENTO,"+
			"'' AS CICLO_FATURAMENTO, "+
			"decode( "+ 
                      "Greatest(1,cont.vlr_indice_bonificacao),1,SFA.TIP_REGISTRO, "+ //Há Saturação de Bonus
					   "decode(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ // Se não for um consumo Interno/BTSA
								"SFA.TIP_REGISTRO,"+		// Mantem o tipo de registro normal
					   			"decode(plano.idt_categoria,1,"+  //Se for Hibrido
								"'"+Definicoes.COD_REG_SCR_HIBRIDO_SATURADO+"',"+
								"'"+Definicoes.COD_REG_SCR_PREPAGO_SATURADO+"') "+  //Caso não seja hibrido
								") "+
                 ") AS TIPO_REGISTRO, "+
			"'' AS LOCALIDADE, "+
			"'' AS CENTRAL, "+
			"decode( "+ 
            "Greatest(1,cont.vlr_indice_bonificacao),1,SFA.IDT_CATEGORIA, "+ //Há Saturação de Bonus
			   "decode(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ // Se não for um consumo Interno/BTSA
						"SFA.IDT_CATEGORIA,"+		// Mantem o tipo de registro normal
			   			"decode(plano.idt_categoria,1,"+  //Se for Hibrido
						"'"+Definicoes.COD_CAT_SCR_HIBRIDO_SATURADO+"',"+
						"'"+Definicoes.COD_CAT_SCR_PREPAGO_SATURADO+"') "+  //Caso não seja hibrido
						") "+
				") AS CATEGORIA, "+ 
			"'' AS UNIDADE_NEGOCIO, "+
			"decode( "+ 
            "Greatest(1,cont.vlr_indice_bonificacao),1,CONT.IDT_CODIGO_SERVICO_SFA, "+ //Há Saturação de Bonus
			   "decode(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ // Se não for um consumo Interno/BTSA
						"CONT.IDT_CODIGO_SERVICO_SFA,"+		// Mantem o tipo de registro normal
			   			"decode(plano.idt_categoria,1,"+  //Se for Hibrido
						"'"+Definicoes.COD_SFA_SCR_HIBRIDO_SATURADO+"',"+
						"'"+Definicoes.COD_SFA_SCR_PREPAGO_SATURADO+"') "+  //Caso não seja hibrido
						") "+
				") AS CODIGO_SERVICO, "+ 
			"'' AS SEQUENCIAL_TELEFONE, "+
			 "sum(DECODE(CDR.IDT_CODIGO_SERVICO_SFA,NULL,CONT.QTD_REGISTRO, "+ //Caso nao seja consumo de chamada pega qtuantidade
					" DECODE(CDR.IDT_CONSUMO_DADOS,'S',abs(CONT.QTD_REGISTRO),abs(CONT.VLR_DURACAO)))) AS QTD_FISICA, "+ 
					 // Caso seja um consumo de chamada de VOZ ai pega-se a duração se for dados pega a quantidade. 
               "sum(decode(greatest(1,cont.vlr_indice_bonificacao),1, CONT.VLR_TOTAL_SI, "+ 
      				"DECODE(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ //Se for consumo interno pega-se o valor absoluto 
					  "CONT.VLR_TOTAL_SI, abs(CONT.VLR_TOTAL_SI)) "+ 
						")) AS VALOR_LIQUIDO, "+ 
               "sum(decode(greatest(1,cont.vlr_indice_bonificacao),1,CONT.VLR_TOTAL - CONT.VLR_TOTAL_SI, "+ 
      				"DECODE(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0,"+ //Se for consumo pega-se o valor absoluto. 
					  "CONT.VLR_TOTAL - CONT.VLR_TOTAL_SI, abs(CONT.VLR_TOTAL - CONT.VLR_TOTAL_SI)) "+ 
						")) AS VALOR_ICMS, "+ 
			"'' AS VALOR_ISS, "+
			"'' AS ORGAO, "+
			"'' AS VALOR_PASEP, "+
			"'' AS VALOR_COFINS, "+
			"'' AS TIPO_PROCESSAMENTO, "+
			"TO_NUMBER(substr(cont.idt_periodo_contabil,4,4)||substr(cont.idt_periodo_contabil,1,2)) as dat_servico, "+
			"CONT.IDT_CODIGO_NACIONAL AS UNIDADE_FEDERATIVA, "+
			"'' AS SEGMENTO_CARTEIRA, "+
			"CONT.IDT_ROAMING_ORIGIN AS COD_EOT_ORIGEM, "+
			"decode(plano.idt_categoria,2,'BTS','SMP') AS EMPRESA, "+		// Paremtro -1(2:categoria ligmix),0('BTSA'), 0.5('SMP')
			"? AS IDT_STATUS_PROCESSAMENTO, "+		// Parametro 1 
			"'' AS IDT_COD_CNL, "+ 
			"'' AS DES_FILLER, "+ 
			"'' AS TIP_PESSOA, "+ 
			"'' AS NUM_CPF_CNPJ, " +
			"DECODE(cdr.idt_codigo_servico_sfa, " +
			"       NULL, 'UN', " +
			"       DECODE(cdr.idt_consumo_dados, " +
			"              'S','UN',  " +
			"			   'N','MIN' " +
			"              ) " +
			"       ) AS unidade_medicao, " +
			"cont.idt_plano_preco AS idt_plano_brt, " +
			"plano.idt_plano_anatel AS idt_plano_anatel, " +
			"cont.idt_eot_origem AS idt_eot_origem, " +
			"cont.idt_eot_destino AS idt_eot_destino, " +
			"'' AS idt_setor_anatel, " +
			"1 AS idt_trailer, " +
			"'C' AS idt_info_anatel "+  
			 "FROM "+  
			 "(SELECT * FROM TBL_CON_CONTABIL  WHERE IDT_PERIODO_CONTABIL = ?) cont, "+  // Parametro 2 
			 "(SELECT IDT_PLANO_PRECO, idt_categoria, idt_plano_anatel from tbl_geR_plano_preco WHERE IDT_CATEGORIA in (0,1,2)) plano, "+ 
			 "(select distinct idt_codigo_servico_sfa, idt_consumo_dados from TBL_CON_CDR_SERVICO) CDR, "+  
			"TBL_CON_CODIGO_SERVICO_SFA SFA, "+  
			"(SELECT IDT_CODIGO_NACIONAL FROM TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1) CN "+ 
			 "WHERE "+  
			 "cont.idt_plano_preco = plano.idt_plano_preco AND "+  
			"SFA.TIP_REGISTRO IS NOT NULL AND SFA.IDT_CATEGORIA IS NOT NULL AND "+  
			"CONT.IDT_TIPO_SERVICO <> 'CBG' AND "+  
			"CONT.IDT_CODIGO_NACIONAL = CN.IDT_CODIGO_NACIONAL AND "+  
			"CONT.IDT_CODIGO_SERVICO_SFA = SFA.IDT_CODIGO_SERVICO_SFA AND "+  
			 "CONT.IDT_PLANO_PRECO = PLANO.IDT_PLANO_PRECO AND "+ 
			"CONT.IDT_CODIGO_SERVICO_SFA = CDR.IDT_CODIGO_SERVICO_SFA (+) "+ 
			"group by "+
				"decode( "+ 
                "Greatest(1,cont.vlr_indice_bonificacao),1,SFA.TIP_REGISTRO, "+ //Há Saturação de Bonus
				   "decode(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ // Se não for um consumo Interno/BTSA
							"SFA.TIP_REGISTRO,"+		// Mantem o tipo de registro normal
				   			"decode(plano.idt_categoria,1,"+  //Se for Hibrido
							"'"+Definicoes.COD_REG_SCR_HIBRIDO_SATURADO+"',"+
							"'"+Definicoes.COD_REG_SCR_PREPAGO_SATURADO+"') "+  //Caso não seja hibrido
							") "+
				"), "+
				"decode( "+ 
	            "Greatest(1,cont.vlr_indice_bonificacao),1,SFA.IDT_CATEGORIA, "+ //Há Saturação de Bonus
				   "decode(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ // Se não for um consumo Interno/BTSA
							"SFA.IDT_CATEGORIA,"+		// Mantem o tipo de registro normal
				   			"decode(plano.idt_categoria,1,"+  //Se for Hibrido
							"'"+Definicoes.COD_CAT_SCR_HIBRIDO_SATURADO+"',"+
							"'"+Definicoes.COD_CAT_SCR_PREPAGO_SATURADO+"') "+  //Caso não seja hibrido
							") "+
					") , "+ 
					"decode( "+ 
		            "Greatest(1,cont.vlr_indice_bonificacao),1,CONT.IDT_CODIGO_SERVICO_SFA, "+ //Há Saturação de Bonus
					   "decode(instr('CRG:CRB',CONT.IDT_TIPO_SERVICO),0, "+ // Se não for um consumo Interno/BTSA
								"CONT.IDT_CODIGO_SERVICO_SFA,"+		// Mantem o tipo de registro normal
					   			"decode(plano.idt_categoria,1,"+  //Se for Hibrido
								"'"+Definicoes.COD_SFA_SCR_HIBRIDO_SATURADO+"',"+
								"'"+Definicoes.COD_SFA_SCR_PREPAGO_SATURADO+"') "+  //Caso não seja hibrido
								") "+
						"), "+ 
			"TO_NUMBER(substr(cont.idt_periodo_contabil,4,4)||substr(cont.idt_periodo_contabil,1,2)), "+
            "CONT.IDT_CODIGO_NACIONAL, "+
			"CONT.IDT_ROAMING_ORIGIN," +
			"decode(plano.idt_categoria,2,'BTS','SMP'),"+
		 	"DECODE(cdr.idt_codigo_servico_sfa, " +
		 	"           NULL, 'UN', " +
		 	"           DECODE(cdr.idt_consumo_dados, " +
		 	"                  'S','UN', " +
		 	" 				   'N','MIN' " +
		 	"                 ) " +
		 	"       ),      " +
		 	"cont.idt_plano_preco, " +
		 	"plano.idt_plano_anatel, " +
		 	"cont.idt_eot_origem, " +
		 	"cont.idt_eot_destino)";
	
			Object sqlParams[] = {	Definicoes.IND_LINHA_NAO_TRANSFERIDA, //P1
									super.periodoContabil};	//P2
					
			// Executa Query 
			nLinhasInseridas = conexaoPrep.executaPreparedUpdate(sqlInsert,sqlParams,super.getIdLog());
			super.log(Definicoes.INFO,"gerarConsolidacaoSCR","Foram inseridas "+nLinhasInseridas+" na TBL_INT_CONTABIL");

			// String no histórico apresentará sucesso
			status = Definicoes.TIPO_OPER_SUCESSO;
				
			// Gera o trailer a partir dos dados da tbl_int_contabil
			this.gerarTrailer(conexaoPrep);
			
			// Loga Fechamento de Período Contábil
			this.fechaPeriodoContabil(conexaoPrep);	
			
			// Cria o novo período contábil
			criarNovoPeriodoContabil(conexaoPrep);
	 	}
	 	catch(Exception e)
	 	{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.ERRO, "gerarConsolidacaoSCR", "Excecao Interna GPP: "+ e);
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 		
	 	}
		finally
		{
			limparHistorico(conexaoPrep);
			
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Chama a funcao para gravar no historico o Processo em questao
			String descricao = "Foram Inseridas " + nLinhasInseridas + " entradas na TBL_INT_CONTABIL";
			String dataFinal = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_ENVIO_CONSOLIDACAO_SCR, dataInicial, dataFinal, status, descricao, super.dataReferencia);
		
			super.log(Definicoes.INFO, "gerarConsolidacaoSCR", "Fim");			
		}
	 	return retorno;
	 }
	 
	 /**
	  * Metodo...: fechaPeriodoContabil
	  * Descricao: Registra o fechamento desse periodo contabil inserindo/atualizando registro
	  * 			na tbl_con_periodo_contabil
	  */
	 private void fechaPeriodoContabil(PREPConexao conexaoPrep) throws GPPInternalErrorException
	 {
	 	try
		{
		 	// Desativa o autoCommit
		 	conexaoPrep.setAutoCommit(false);
		 	
		 	// Atualiza flag de Período Fechado na tbl_con_periodo_contabil
		 	String sqlPeriodo = "UPDATE TBL_CON_PERIODO_CONTABIL SET IND_FECHADO = 1 WHERE IDT_PERIODO_CONTABIL = ? ";
		 	Object[] parPeriodo = { super.periodoContabil };
		 	
		 	// Atualiza o flag de periodo fechado
		 	int nroLinhasAtualizadas = conexaoPrep.executaPreparedUpdate(sqlPeriodo, parPeriodo, super.getIdLog()); 
		 	if( nroLinhasAtualizadas != 1)	// Caso haja mais de um registro do mesmo período, gera erro
		 	{
		 		super.log(Definicoes.ERRO,"fechaPeriodoContabil", "Erro ao Fechar Período Contabil: "+nroLinhasAtualizadas+" registros do Periodo estao cadastrados");
		 		conexaoPrep.rollback();
		 	}
		 	else
		 	{
		 		conexaoPrep.commit();
		 	}
		 	
		 	// Ativa o autoCommit
		 	conexaoPrep.setAutoCommit(true);	 		
		}
	 	catch(Exception e)
		{
	 		super.log(Definicoes.ERRO, "fechaPeriodoContabil", "Erro ao Fechar Periodo Contabil: "+ e);
	 		throw new GPPInternalErrorException("Erro ao Fechar Periodo Contabil: "+e);
		}
	 }
	 
	 /**
	  * Gera uma sumarização da tabela de interface
	  * @param PREPConexao - Conexão com o banco de dados
	  */
	 private void gerarTrailer(PREPConexao conexao) throws GPPInternalErrorException
	 {
	     try
	     {
	    	 // Gera o trailer
	         String sql = 	" INSERT INTO tbl_int_contabil  " +
	         				" (num_registro, dat_processamento,   " +
	         				" tip_registro, id_categoria,    " +
	         				" qtd_fisica, vlr_liquido,   " +
	         				" vlr_icms, dat_servico, idt_uf,   " +
	         				" idt_empresa, idt_status_processamento,   " +
	         				" idt_trailer)   " +
	         				" SELECT " +
	         				"  SEQ_REGISTRO_CONTABIL.NEXTVAL as num_registro, " +
	         				"  contabil.* " +
	         				" FROM " +
	         				" (SELECT     " +
	         				"   sysdate as dat_processamento, " +
	         				"   tip_registro as tip_registro, " +
	         				"   id_categoria as id_categoria, " +
	         				"   sum(qtd_fisica) as qtd_fisica,  " +
	         				"   sum(vlr_liquido) as vlr_liquido, " +
	         				"   sum(vlr_icms) as vlr_icms,  " +
	         				"   dat_servico as dat_servico,  " +
	         				"   idt_uf as idt_uf, " +
	         				"   'SMP' as idt_empresa,  " +
	         				"   'N' as idt_status_processamento,  " +
	         				"   '9' as idt_trailer " +
	         				"  FROM  " +
	         				"   tbl_int_contabil " +
	         				"  WHERE  " +
	         				"   dat_servico = TO_CHAR(TO_DATE(?,'mm/yyyy'),'yyyymm') " +
	         				"  GROUP BY  " +
	         				"   idt_uf, " +
	         				"   dat_servico, " +
	         				"   id_categoria, " +
	         				"   tip_registro)contabil";
	         
	         Object sqlParam[] = {super.periodoContabil};
	         int linhasAtualizadas = conexao.executaPreparedUpdate(sql, sqlParam, super.getIdLog());
	         super.log(Definicoes.DEBUG, "geraTrailer", linhasAtualizadas + " Linhas atualizadas com sucesso");
	     }
	     catch(GPPInternalErrorException e)
	     {
	         super.log(Definicoes.ERRO, "geraTrailer", "Erro ao Gerar o Rodape: " + e);
	         throw new GPPInternalErrorException("Erro ao Gerar o Rodape: " + e);
	     }
	 }
	 
	 /**
	  * Verifica se os registros anteriores já foram processados
	  * @param PREPConexao - Conexão com o banco de dados
	  */
	 private short verificarRegistros(PREPConexao conexao) throws Exception
	 {
	     short teste = 0;
	     String sql = 	" SELECT " +
	     				"   1 " +
	     				" FROM " +
	     				"   tbl_int_contabil " +
	     				" WHERE " +
	     				"   idt_status_processamento = ?";
	     
	     Object parametro[] = {Definicoes.IND_LINHA_NAO_PROCESSADA};
	     
	     ResultSet rs = null;
	     rs = conexao.executaPreparedQuery(sql, parametro, super.getIdLog());
	     // Testa se existe algum registro não processado
	     if(rs.next())
	         teste = Definicoes.RET_SEM_REGISTRO_ULTIMA_EXECUCAO;
	     rs.close();
	     
	     super.log(Definicoes.DEBUG, "testaRegistros", "Teste de registros. Retorno: " + teste);
	     
	     return teste;
	 }
	 
	 /**
	  * Cria o novo período contábil para a contabilidade
	  * @param conexao	Conexão com o banco de dados
	  * @return sucesso	Indica se a inserção foi concluída com sucesso
	  */
	 private boolean criarNovoPeriodoContabil(PREPConexao conexao)
	 {
		 boolean sucesso = false;
		 
		 String inserePeriodo	= "insert into tbl_con_periodo_contabil																		  " 
			 					+ " (IDT_PERIODO_CONTABIL,DAT_INICIO_PERIODO,DAT_FINAL_PERIODO,IND_FECHADO)"
								+ " select to_char(add_months(TO_DATE(IDT_PERIODO_CONTABIL,'mm/yyyy'),1),'mm/yyyy') IDT_PERIODO_CONTABIL,     "
								+ "        DAT_FINAL_PERIODO + 1 DAT_INICIO_PERIODO,                                                          "
								+ "        '28/'||to_char(add_months(TO_DATE(IDT_PERIODO_CONTABIL,'mm/yyyy'),1),'mm/yyyy') DAT_FINAL_PERIODO, "
								+ "        0 IND_FECHADO                                                                                      "
								+ " FROM tbl_con_periodo_contabil                                                                             "
								+ " WHERE idt_periodo_contabil = ?                                                                            ";
		 
		 Object [] paramInsere = {super.periodoContabil};
		 
		 try
		 {
			 conexao.executaPreparedUpdate(inserePeriodo, paramInsere, super.getIdLog());
			 sucesso = true;
		 }
		 catch(GPPInternalErrorException e)
		 {
			 super.log(Definicoes.WARN, "criarNovoPeriodoContabil", "Nao foi possivel criar um novo periodo contabil. Erro: " + e);
		 }		 
		 
		 return sucesso;
	 }
	 
	 /**
	  * Limpa o histórico de dados
	  * @param conexao	Conexão com o banco de dados
	  * @return sucesso	Indica se a remoção foi concluída com sucesso
	  */
	 private boolean limparHistorico(PREPConexao conexao)
	 {	
		 boolean sucesso = false;
		 
		 try
		 {
			 // Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			 MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
	
			 // Limpa Registros já processados da Tabela
			 String sqlLimpa 	= "DELETE TBL_INT_CONTABIL " 
								+ "WHERE IDT_STATUS_PROCESSAMENTO = ? " 
								+ "  and dat_processamento < (sysdate - ?)";
			
			 Object limpaParams[] = {Definicoes.IND_LINHA_TRANSFERIDA,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
				
			 if(conexao != null)
			 {
				 conexao.executaPreparedUpdate(sqlLimpa,limpaParams,super.getIdLog());
				 sucesso = true;
			 }
		 }
		 catch(GPPInternalErrorException e)
		 {
			 super.log(Definicoes.WARN, "limparHistorico", "Nao foi possivel remover registros antigos. Erro: " + e);
		 }		 
		 
		 return sucesso;
	 }
}