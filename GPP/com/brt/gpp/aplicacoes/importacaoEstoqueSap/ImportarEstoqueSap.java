/*
 * Created on 24/02/2005
 *
 */
package com.brt.gpp.aplicacoes.importacaoEstoqueSap;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Timestamp;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;


/**
 * .Esta classe importa os dados inseridos pelo ETI na TBL_INT_ETI_IN, separa as informações
 * do campo DES_DADOS e os insere na TBL_SAP_ESTOQUE acrescentando a data atual.
 * 
 * @metodos: 
 * getInstancia			 (long idProcesso); 
 * importar    			 (); 
 * parseDados  			 (String dados);
 * GravarDados 			 (DadosEntradaEti dados, PREPConexao conexaoPrep);
 * AtualizarTabelaOrigem (long idProcessamento, PREPConexao conexaoPrep)
 * 
 * @author Henrique Canto
 * @Data: 24/02/2005
 *
 */
public class ImportarEstoqueSap extends Aplicacoes {
	
	private static 	ImportarEstoqueSap 	instancia;
	/**
	 * Metodo...: ImportarDadosEtiIn
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 private ImportarEstoqueSap (long idProcesso)
	 {
	 	
		super(idProcesso, Definicoes.CL_IMPORTACAO_ENTRADA_ETI);
		
	 }
	 

	/**
	* Metodo....:getInstancia
	* Descricao.:Este metodo retorna a instancia singleton da classe para importacao do pedido
	* @param idProcesso	- Id do processo que esta chamando a instancia
	* @return ImportacaoPedidosVoucher - Instancia da classe de importacao de pedidos de voucher
	*/
	 
	 public static ImportarEstoqueSap getInstancia(long idProcesso)
	 {
	 	if (instancia == null);
	 		instancia = new ImportarEstoqueSap(idProcesso);
			
	 	instancia.logId = idProcesso;
		return instancia;
	 }
	 
	 
	 /**
	  * .Método...: importar();
	  * .Descricao: importa as informações de IMEI, STATUS e CODIGO DA LOJA provenientes do campo DES_DADOS 
	  * 			que o ETI insere na TBL_INT_ETI_IN. Após a importação os dados devem ser tratados e separados
	  * 			para inserção na hsid_estoque_sap com a DAT_ATUALIZACAO=SYSDATE. Após a inserção das informações
	  *             na hsid_estoque_sap, a TBL_INT_ETI_IN tem o campo IDT_STATUS_PROCESSAMENTO modificado para Y, 
	  *             indicando que o registro já foi processado.
	  * 
	  * @author Henrique Canto
	  * @return
	  * @throws GPPInternalErrorException
	  */
	 public synchronized short importar() throws GPPInternalErrorException
	 {
	 	//Inicializa variaveis do metodo
	 	short retorno = 0;
		long aIdProcesso = super.getIdLog();
		int qtd = 0;

		// Data para histórico de processos batch
		String dataInicial = GPPData.dataCompletaForamtada();
			
		PREPConexao conexaoPrep = null;

		super.log(Definicoes.INFO, "executaImportacaoEtiIn", "Inicio");
		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
			conexaoPrep.setAutoCommit(false);
					
			// Faz a pesquisa no banco para saber os dados da importacao de usuarios 
			String sql_pesquisa = "SELECT ID_SEQUENCIAL, DES_DADOS FROM TBL_INT_ETI_IN  " +
								  "WHERE IDT_STATUS_PROCESSAMENTO = ?" +
								  " AND IDT_EVENTO_NEGOCIO = ?";
				
			Object paramPesquisa[] = {Definicoes.IDT_PROCESSAMENTO_NOT_OK, Definicoes.IDT_EVT_NEGOCIO_ESTOQUE_SAP};
			ResultSet rs_pesquisa = conexaoPrep.executaPreparedQuery(sql_pesquisa,paramPesquisa, super.logId);
			
			// Para cada linha encontrada na TBL_INT_ETI_IN com status N
			long idProcessamento = 0;
			String statusProc = null;
			while(rs_pesquisa.next())
			{
				try
				{
					idProcessamento = rs_pesquisa.getLong("ID_SEQUENCIAL");
					
					// Parse dos dados do campo DES_DADOS para uma instancia do objeto DadosEstoqueSap
					DadosEstoqueSap dadosEstoqueSap = this.parseDados(rs_pesquisa.getString("DES_DADOS"));
					
					// Insert na HSID_ESTOQUE_SAP com as informacoes de DadosEstoqueSap
					statusProc = this.GravarDados(dadosEstoqueSap, conexaoPrep);
					
					// Atualizacao do campo IDT_STATUS_PROCESSAMENTO de N para Y 
					this.AtualizarTabelaOrigem(idProcessamento, conexaoPrep, statusProc);
					
					// Se tudo foi bem, faz o commit
					conexaoPrep.commit();
				}
				catch ( Exception pe2)
				{
					// Se houve algum problema, faz rollback
					conexaoPrep.rollback();
					
					// Atualizacao do campo IDT_STATUS_PROCESSAMENTO de N para Y 
					this.AtualizarTabelaOrigem(idProcessamento, conexaoPrep, Definicoes.IDT_PROCESSAMENTO_ERRO);

					super.log(Definicoes.ERRO,"ImportarEstoqueSap","Erro interno: "+pe2);
				}
				
				// Incrementa Contador de Importações
				qtd++;
			}
		}
		catch (SQLException e1)
		{
			super.log(Definicoes.WARN, "executaImportacaoEtiIn", "Excecao SQL: "+ e1);
			throw new GPPInternalErrorException ("Excecao GPP: " + e1);				
		}

		finally
		{
			try
			{
				conexaoPrep.setAutoCommit(true);
				
				// chama a funcao para gravar no historico o Processo em questao
				super.gravaHistoricoProcessos(	Definicoes.IND_ESTOQUE_SAP, 
												dataInicial, 
												GPPData.dataCompletaForamtada(), 
												"SUCESSO", 
												"Quantidade de Importacoes: "+qtd, 
												GPPData.dataFormatada());
			}
			catch(SQLException sqlE)
			{
				super.log(Definicoes.ERRO, "ImportarEstoqueSap","Problema com Banco: "+sqlE);
			}
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		}
		return retorno;
	 }
	 
	/**
	 * .Método que importa os dados do campo DES_DADOS da TBL_INT_ETI_IN que estão separados por ;
	 * e os grava em um objeto java (DadosEntradaEti) para serem inseridos na TBL_SAP_ESTOQUE.
	 * 
	 * @param dados
	 * @return DadosEntradaEti
	 */
	private DadosEstoqueSap parseDados(String dados) throws Exception
	{
		// Cria objeto para armazenar os dados
		DadosEstoqueSap dadosEstoqueSap= null;
		
		try
		{
			// Pega os campos 
			dadosEstoqueSap= new DadosEstoqueSap();
			dadosEstoqueSap.setImei(dados.substring(0,15));
			dadosEstoqueSap.setStatus(dados.substring(15,17));
			dadosEstoqueSap.setCodigoLoja(dados.substring(17));
			dadosEstoqueSap.setDataImportacao(Calendar.getInstance().getTime());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"parseDados","Erro de Formato dos Dados Mandados pelo SAP: "+e);
			throw new Exception(e);
		}
		
		return dadosEstoqueSap;
	}
	
	/**
	 * .Método que grava os dados do objeto DadosEntradaEti na TBL_SAP_ESTOQUE.
	 * 
	 * @param dados
	 * @param conexaoPrep
	 * @throws GPPInternalErrorException
	 */
	private String GravarDados (DadosEstoqueSap dados, PREPConexao conexaoPrep) 
	{
		String valorRetorno = Definicoes.IDT_PROCESSAMENTO_OK;
		try
		{
		String sql_grava= "INSERT INTO HSID_ESTOQUE_SAP "+
		"(DAT_IMPORTACAO, IMEI, STATUS, COD_LOJA)" +
		"VALUES(?,?,?,?)";
		
		
		Object paramInsert[] = {new Timestamp(dados.getDataImportacao().getTime()), dados.getImei(), dados.getStatus(), dados.getCodigoLoja()};
		conexaoPrep.executaPreparedUpdate(sql_grava, paramInsert, super.logId);
		
		}
		catch(GPPInternalErrorException pe1)
		{
			valorRetorno = Definicoes.IDT_PROCESSAMENTO_ERRO;
		}
		
		return valorRetorno;
		
	}
	
	/**
	 * .Método que atualiza o campo IDT_STATUS_PROCESSAMENTO da tabela TBL_INT_ETI_IN com o valor Y 
	 * após o processamento das informações na TBL_SAP_ESTOQUE.
	 * 
	 * @param idProcessamento
	 * @param conexaoPrep
	 * @throws GPPInternalErrorException
	 */
	private void AtualizarTabelaOrigem (long idProcessamento, PREPConexao conexaoPrep, String statusProcessamento) throws GPPInternalErrorException
	{
		String sql_alteracao = "UPDATE TBL_INT_ETI_IN SET IDT_STATUS_PROCESSAMENTO=?, DAT_LEITURA = SYSDATE "+
		"WHERE ID_SEQUENCIAL=? "+
		"AND IDT_EVENTO_NEGOCIO=?";
		
		Object paramaAlteracao[] = {statusProcessamento, new Long(idProcessamento), Definicoes.IDT_EVT_NEGOCIO_ESTOQUE_SAP};
		conexaoPrep.executaPreparedUpdate(sql_alteracao, paramaAlteracao ,super.logId);
	}
	
}
