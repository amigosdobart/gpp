package com.brt.gpp.aplicacoes.importacaoEstoqueSap;

import java.sql.Timestamp;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *
 * Este arquivo contem a definicao da classe de ImportacaoEstoqueSapConsumidor.
 * Esta classe importa os dados inseridos pelo ETI na TBL_INT_ETI_IN, separa as informações
 * do campo DES_DADOS e os insere na TBL_SAP_ESTOQUE acrescentando a data atual.
 * 
 * <P> Versao:	1.0
 *
 * @Autor:		Diego Luitgards
 * Data:		04/10/2006
 *
 * Atualizacao:	Joao Paulo Galvagni Junior
 * Data:		23/03/3007
 * Descricao:	Antes de realizar o insert na tabela hsid_estoque_sap eh feito uma tentativa
 * 				de atualizacao nessa mesma tabela. Isso impede que seja gravado falha na tabela
 * 				de origem quando o insert falha por duplicacao de chave
 */
public class ImportacaoEstoqueSapConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private PREPConexao		conexaoConsumidorPrep;
	
	/**
	 * Metodo....:ImportacaoEstoqueSapConsumidor
	 * Descricao.:Construtor da classe do processo batch
	 */
	public ImportacaoEstoqueSapConsumidor()
	{
		super(GerentePoolLog.getInstancia(ImportacaoEstoqueSapConsumidor.class).getIdProcesso(Definicoes.CL_IMPORTACAO_ENTRADA_ETI),Definicoes.CL_IMPORTACAO_ENTRADA_ETI);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		conexaoConsumidorPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor)produtor);
	}	
	
    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        startup();
    }
    
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{
		// Para cada linha encontrada na TBL_INT_ETI_IN com status N
		long idProcessamento = 0;
		String statusProc = null;
		
		this.conexaoConsumidorPrep.setAutoCommit(false);
		
		// Parse dos dados do campo DES_DADOS para uma instancia do objeto DadosEstoqueSap		
		DadosEstoqueSap dadosEstoqueSap = (DadosEstoqueSap)obj;
		idProcessamento = (dadosEstoqueSap.getIdProcessamento()) ;
		try
		{
			// Realiza a tentativa de atualizacao dos dados na hsid_estoque_sap
			if (this.atualizaDados(dadosEstoqueSap) == 0)
			{
				// Caso a atualizacao falhe, sera realizado um insert
				// na HSID_ESTOQUE_SAP com as informacoes de DadosEstoqueSap
				statusProc = this.gravarDados(dadosEstoqueSap);
			}
			else
				statusProc = Definicoes.IDT_PROCESSAMENTO_OK;
			
			// Atualizacao do campo IDT_STATUS_PROCESSAMENTO de N para Y (OK)ou E (ERRO)
			this.atualizarTabelaOrigem(idProcessamento, statusProc);
			
			// Se tudo foi bem, faz o commit
			conexaoConsumidorPrep.commit();
		}
		catch ( Exception pe2 )
		{
			// Se houve algum problema, faz rollback
			conexaoConsumidorPrep.rollback();
			
			super.log(Definicoes.ERRO,"ImportarEstoqueSapConsumidor","Erro interno Identificador: " + idProcessamento + " " +pe2);
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoConsumidorPrep,super.getIdLog());
		super.log(Definicoes.DEBUG,"Consumidor.finish","Alteracao do status do usuario" );
		
	}
	
	/**
	 * Metodo....: atualizaDados
	 * Descricao.: 
	 * 
	 * @param  dados				 - Objeto contendo os dados para atualizacao
	 * @param  conexaoConsumidorPrep - Conexao com o 
	 * @return retorno				 - Resultado da atualizacao
	 */
	private int atualizaDados (DadosEstoqueSap dados)
	{
		int retorno = 0;
		
		try
		{
			if (dados.getImei() != null)
			{
				String sql = "UPDATE HSID_ESTOQUE_SAP 	 " +
							 "	 SET DAT_IMPORTACAO = ?, " +
							 "	 	 STATUS = ?, 		 " +
							 "	 	 COD_LOJA = ? 		 " +
							 " WHERE IMEI = ? 			 " ;
				
				Object paramAtualizacao[] = {new Timestamp(dados.getDataImportacao().getTime()), 
											 dados.getStatus(),
											 dados.getCodigoLoja(),
											 dados.getImei()};
				
				retorno = this.conexaoConsumidorPrep.executaPreparedUpdate(sql, paramAtualizacao, super.logId);
			}
		}
		catch(GPPInternalErrorException e)
		{
		}
		
		return retorno;
	}
	
	/**
	 * .Método que grava os dados do objeto DadosEntradaEti na TBL_SAP_ESTOQUE.
	 * 
	 * @param  dados
	 * @throws GPPInternalErrorException
	 */
	private String gravarDados (DadosEstoqueSap dados) 
	{
		String valorRetorno = Definicoes.IDT_PROCESSAMENTO_ERRO;
		try
		{
			if (dados.getImei() != null)
			{
				String sql_grava= "INSERT INTO HSID_ESTOQUE_SAP " +
								  "		  (DAT_IMPORTACAO, 		" +
								  "		   IMEI, 				" +
								  "		   STATUS, 				" +
								  "		   COD_LOJA)			" +
								  "VALUES (?, ?, ?, ?)			" ;
				
				Object paramInsert[] = {new Timestamp(dados.getDataImportacao().getTime()), dados.getImei(), dados.getStatus(), dados.getCodigoLoja()};
				this.conexaoConsumidorPrep.executaPreparedUpdate(sql_grava, paramInsert, super.logId);
				
				valorRetorno = Definicoes.IDT_PROCESSAMENTO_OK;
			}
		}
		catch(GPPInternalErrorException pe1)
		{
		}
		
		return valorRetorno;
	}
	
	/**
	 * .Método que atualiza o campo IDT_STATUS_PROCESSAMENTO da tabela TBL_INT_ETI_IN com o valor Y ou E
	 * após o processamento das informações na TBL_SAP_ESTOQUE.
	 * 
	 * @param idProcessamento
	 * @param statusProcessamento
	 * @throws GPPInternalErrorException
	 */
	private void atualizarTabelaOrigem (long idProcessamento, String statusProcessamento) throws GPPInternalErrorException
	{
		String sql_alteracao = "UPDATE TBL_INT_ETI_IN SET IDT_STATUS_PROCESSAMENTO=?, DAT_LEITURA = SYSDATE "+
		"WHERE ID_SEQUENCIAL=? "+
		"AND IDT_EVENTO_NEGOCIO=?";
		
		Object paramaAlteracao[] = {statusProcessamento, new Long(idProcessamento), Definicoes.IDT_EVT_NEGOCIO_ESTOQUE_SAP};
		this.conexaoConsumidorPrep.executaPreparedUpdate(sql_alteracao, paramaAlteracao ,super.logId);
	}
}
