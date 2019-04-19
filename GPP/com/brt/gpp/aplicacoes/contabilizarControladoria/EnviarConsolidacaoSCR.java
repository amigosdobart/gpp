package com.brt.gpp.aplicacoes.contabilizarControladoria;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
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
* Modificado por:Alberto Magno (Acc).
* Data:22/11/2004
* Razao: Correções em período contábil e novas formas de preenchimento dos campos:Categoria e Registro.
*
* Modificado por:Alberto Magno (Acc).
* Data:30/11/2004
* Razao: Correções para deleção antes de inserção e inserção apenas por período
*/
public class EnviarConsolidacaoSCR extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
		     
	/**
	 * Metodo...: EnviarConsolidacaoSCR
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public EnviarConsolidacaoSCR (long logId)
	 {
		super(logId, Definicoes.CL_CONSOLIDACAO_SCR);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }
	 
	 /***
	  * Metodo...: gerarConsolidacaoSCR
	  * Descricao: Preencher a tabela TBL_INT_CONTABIL a partir dos dados disponíveis na
	  * TBL_REL_CONTABIL
	  * @param aData	Data de Referencia, a ser usada no histórico do processo
	  * @return	short	0, se ok; !0 se houver erro
	  * @throws GPPInternalErrorException
	  */
	 public short gerarConsolidacaoSCR(String aData) throws GPPInternalErrorException
	 {
		String status = null;
		PREPConexao conexaoPrep = null; 
		int nLinhasInseridas = 0;
		String dataInicial = null;
		short retorno = 0;
		
	 	try
	 	{

	 		super.log(Definicoes.INFO, "gerarConsolidacaoSCR", "Inicio DATA "+aData);
			
			// Pega conexão com Banco de Dados
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
	 		/*******************************************************************************************************************/
			// Limpa tabela de 
			String query = "DELETE FROM TBL_INT_CONTABIL WHERE DAT_SERVICO = TO_NUMBER(substr(?,7,4)||substr(?,4,2))";
			// Deleta TBL_REL_CONTABIL
			super.log(Definicoes.INFO,"consolidaContabil","Delecao dados tabela TBL_INT_CONTABIL");
			conexaoPrep.executaPreparedQuery(query,new Object[]{aData,aData},super.getIdLog());
			/*******************************************************************************************************************/

			// Registra Data/Hora do início do processo
			dataInicial = GPPData.dataCompletaForamtada();
		 	
		 	// Query para inserir na tabela TBL_INT_CONTABIL
		 	String sqlInsert = "INSERT INTO TBL_INT_CONTABIL "+
				"SELECT " +
				"SEQ_REGISTRO_CONTABIL.NEXTVAL," +//1
				"DAT_PROCESSAMENTO," +//2
				"CICLO_FATURAMENTO," +//3
				"TIPO_REGISTRO,"+//4
				"LOCALIDADE," +//5
				"CENTRAL," +//6
				"CATEGORIA," +//7
				"UNIDADE_NEGOCIO," +//8
				"CODIGO_SERVICO," +//9
				"SEQUENCIAL_TELEFONE," +//10
				"QTD_FISICA,"+//11
				"VALOR_LIQUIDO," +//12
				"VALOR_ICMS," +//13
				"VALOR_ISS," +//14
				"ORGAO," +//15
				"VALOR_PASEP," +//16
				"VALOR_COFINS," +//17
				"TIPO_PROCESSAMENTO,"+//18
				"DATA_SERVICO," +//19
				"UNIDADE_FEDERATIVA," +//20
				"SEGMENTO_CARTEIRA,"+//21
				"COD_EOT_ORIGEM," +//22
				"EMPRESA," +//23
				"IDT_STATUS_PROCESSAMENTO," +//24
				"IDT_COD_CNL, " +//25
				"DES_FILLER, " +//26
				"TIP_PESSOA, " +//27
				"NUM_CPF_CNPJ " +//28
				//",IDT_PLANO "+//29
				"FROM "+ 
				"(SELECT " +
				" SYSDATE AS DAT_PROCESSAMENTO,"+
				"'' AS CICLO_FATURAMENTO,"+
				
				" decode("+ 
              "        Greatest(1,cont.vlr_indice_bonificacao),1,SFA.TIP_REGISTRO, "+
				"		decode(CONT.IDT_TIPO_SERVICO,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"'," +
						"decode(cont.idt_pre_hibrido,'"+Definicoes.COD_ASSINANTE_HIBRIDO+"','"+Definicoes.COD_REG_SCR_HIBRIDO_SATURADO+"','"+Definicoes.COD_REG_SCR_PREPAGO_SATURADO+"')" +
								",SFA.TIP_REGISTRO)"+
              "    ) AS TIPO_REGISTRO,"+
				
				
				"'' AS LOCALIDADE,"+
				"'' AS CENTRAL,"+

				" decode("+ 
              "        Greatest(1,cont.vlr_indice_bonificacao),1,SFA.IDT_CATEGORIA, "+
				"		decode(CONT.IDT_TIPO_SERVICO,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"'," +
						"decode(cont.idt_pre_hibrido,'"+Definicoes.COD_ASSINANTE_HIBRIDO+"','"+Definicoes.COD_CAT_SCR_HIBRIDO_SATURADO+"','"+Definicoes.COD_CAT_SCR_PREPAGO_SATURADO+"')" +
								",SFA.IDT_CATEGORIA)"+
              "    ) AS CATEGORIA,"+


				"'' AS UNIDADE_NEGOCIO,"+
				
				" decode("+ 
              "        Greatest(1,cont.vlr_indice_bonificacao),1,CONT.IDT_CODIGO_SERVICO_SFA, "+
				"		decode(CONT.IDT_TIPO_SERVICO,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"'," +
						"decode(cont.idt_pre_hibrido,'"+Definicoes.COD_ASSINANTE_HIBRIDO+"',"+Definicoes.COD_SFA_SCR_HIBRIDO_SATURADO+","+Definicoes.COD_SFA_SCR_PREPAGO_SATURADO+")" +
								",CONT.IDT_CODIGO_SERVICO_SFA)"+
              "    ) AS CODIGO_SERVICO,"+

				"'' AS SEQUENCIAL_TELEFONE,"+
				
				"DECODE(CDR.IDT_CODIGO_SERVICO_SFA,NULL,CONT.QTD_REGISTRO," +
				
				" DECODE(CDR.IDT_CONSUMO_DADOS,'S',CONT.QTD_REGISTRO,CONT.VLR_DURACAO)) AS QTD_FISICA,"+

              " decode(greatest(1,cont.vlr_indice_bonificacao),1, CONT.VLR_TOTAL_SI,"+
      		"		DECODE(CONT.IDT_TIPO_SERVICO,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"',abs(CONT.VLR_TOTAL_SI),CONT.VLR_TOTAL_SI)"+
				"		) AS VALOR_LIQUIDO,"+

              " decode(greatest(1,cont.vlr_indice_bonificacao),1,CONT.VLR_TOTAL - CONT.VLR_TOTAL_SI,"+
      		"		DECODE(CONT.IDT_TIPO_SERVICO,'"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO+"',abs(CONT.VLR_TOTAL - CONT.VLR_TOTAL_SI),CONT.VLR_TOTAL - CONT.VLR_TOTAL_SI)"+
				"		) AS VALOR_ICMS,"+
						
				"'' AS VALOR_ISS,"+
				"'' AS ORGAO,"+
				"'' AS VALOR_PASEP,"+
				"'' AS VALOR_COFINS,"+
				"'' AS TIPO_PROCESSAMENTO,"+
				"TO_NUMBER(substr(cont.idt_periodo_contabil,4,4)||substr(cont.idt_periodo_contabil,1,2)) AS DATA_SERVICO,"+
				"CONT.IDT_CODIGO_NACIONAL AS UNIDADE_FEDERATIVA,"+
				"'' AS SEGMENTO_CARTEIRA,"+
				"CONT.IDT_ROAMING_ORIGIN AS COD_EOT_ORIGEM,"+
				"? AS EMPRESA,"+
				"? AS IDT_STATUS_PROCESSAMENTO," +
				"'' AS IDT_COD_CNL," +
				"'' AS DES_FILLER," +
				"'' AS TIP_PESSOA," +
				"'' AS NUM_CPF_CNPJ, " +
				" CONT.IDT_PLANO_PRECO AS IDT_PLANO " +
				"FROM " +
				" (SELECT * FROM TBL_REL_CONTABIL  WHERE IDT_PERIODO_CONTABIL = ?) cont," +
				" (SELECT IDT_PLANO_PRECO, decode(idt_categoria,0,'"+Definicoes.COD_ASSINANTE_PREPAGO+"','"+Definicoes.COD_ASSINANTE_HIBRIDO+"') as idt_pre_hibrido from tbl_geR_plano_preco WHERE IDT_CATEGORIA = 1 or idt_categoria = 0) plano," +
				//Nao ha necessidade ->"(select IDT_CODIGO_NACIONAL from TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1)  AREA, " +
				"(select distinct idt_codigo_servico_sfa, idt_consumo_dados from TBL_GER_CDR_SERVICO) CDR, " +
				"TBL_GER_CODIGO_SERVICO_SFA SFA, " +
				"(SELECT IDT_CODIGO_NACIONAL FROM TBL_GER_CODIGO_NACIONAL WHERE IND_REGIAO_BRT = 1) CN "+
				"WHERE " +
				" cont.idt_plano_preco = plano.idt_plano_preco AND " +
				"SFA.TIP_REGISTRO IS NOT NULL AND SFA.IDT_CATEGORIA IS NOT NULL AND " +
				"CONT.IDT_TIPO_SERVICO <> '"+Definicoes.TIPO_SERVICO_CONSUMO_PROPRIO_BONUS+"' AND " +
				"CONT.IDT_CODIGO_NACIONAL = CN.IDT_CODIGO_NACIONAL AND " +
				"CONT.IDT_CODIGO_SERVICO_SFA = SFA.IDT_CODIGO_SERVICO_SFA and " +
				//" CONT.IDT_PERIODO_CONTABIL = ? AND " +
				//Não há necessidade->"AREA.IDT_CODIGO_NACIONAL = CONT.IDT_CODIGO_NACIONAL AND"+
				"CONT.IDT_CODIGO_SERVICO_SFA = CDR.IDT_CODIGO_SERVICO_SFA (+))";
				
				Object sqlParams[] = {Definicoes.SFA_EMPRESA_GSM, Definicoes.IND_LINHA_NAO_TRANSFERIDA,aData.substring(3) };
					
				// Executa Query
				nLinhasInseridas = conexaoPrep.executaPreparedUpdate(sqlInsert,sqlParams,super.getIdLog());
				super.log(Definicoes.INFO,"gerarConsolidacaoSCR","Foram inseridas "+nLinhasInseridas+" na TBL_INT_CONTABIL");

				// String no histórico apresentará sucesso
				status = Definicoes.TIPO_OPER_SUCESSO;					
	 	}
	 	catch(GPPInternalErrorException e)
	 	{
			status = Definicoes.TIPO_OPER_ERRO;			 
			super.log(Definicoes.ERRO, "gerarConsolidacaoSCR", "Excecao Interna GPP: "+ e);
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 		
	 	}
		finally
		{
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Limpa Registros já processados da Tabela
			String sqlLimpa = "DELETE TBL_INT_CONTABIL WHERE IDT_STATUS_PROCESSAMENTO = ? " +
					" and dat_processamento < (sysdate - ?)";
			Object limpaParams[] = {Definicoes.IND_LINHA_TRANSFERIDA,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
				
			int nLinhasApagadas = conexaoPrep.executaPreparedUpdate(sqlLimpa,limpaParams,super.getIdLog());
			super.log(Definicoes.DEBUG,"gerarConsolidacaoSCR","Foram apagadas "+nLinhasApagadas+" entradas da TBL_INT_CONTABIL");

			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			// Chama a funcao para gravar no historico o Processo em questao
			String descricao = "Foram Inseridas " + nLinhasInseridas + " entradas na TBL_INT_CONTABIL";
			String dataFinal = GPPData.dataCompletaForamtada();
			super.gravaHistoricoProcessos(Definicoes.IND_ENVIO_CONSOLIDACAO_SCR, dataInicial, dataFinal, status, descricao, aData);
		
			super.log(Definicoes.INFO, "gerarConsolidacaoSCR", "Fim");			
		}
	 	return retorno;
	 }
}
