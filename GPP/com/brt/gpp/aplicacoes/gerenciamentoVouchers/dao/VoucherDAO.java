package com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.DetalheVoucherXML;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.JobTecnomen;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.SolicitacaoAtivacao;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.util.ManipuladorXMLVoucher;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;

/**
 * Esta classe é responsável por fornecer métodos de acesso ao banco de dados
 * para o processo de tratamento de vouchers.
 * 
 * @author Gustavo Gusmao
 * @since 24/03/2006
 *
 */
public class VoucherDAO
{

	/**
	 * Metodo....:insereInformacoesJob
	 * Descricao.:Insere informacoes relacioandas ao job da tecnomen processando
	 *            a requisicao de ativacao de voucher
	 * @param conexaoPrep			- Conexao de banco de dados a ser utilizada
	 * @param idProcesso			- Id do processo que esta inserindo as informacoes
	 * @param SolicitacaoAtivacao	- Dados da solicitacao de ativacao
	 * @param numJob				- Numero do Job tecnomen
	 * @param statusJob				- Codigo do status atual do Job
	 * @param codRetorno			- Codigo de retorno a ser inserido para o Job
	 * @throws GPPInternalErrorException
	 */
	public static void insereInformacoesJob(PREPConexao conexaoPrep, long idProcesso, SolicitacaoAtivacao solicitacao
										   ,int numJob
										   ,int statusJob
										   ,int codRetorno) throws GPPInternalErrorException
	{
		MapTVMCodRetorno mapRetorno = MapTVMCodRetorno.getInstancia();
		String sqlInsert = "INSERT INTO TBL_GER_STATUS_JOB_TECNOMEN " +
		                   "(ID_PROCESSAMENTO,IDT_EVENTO_NEGOCIO,NUM_JOB_TECNOMEN,"+
						    "DES_STATUS_JOB,DAT_ATUALIZACAO,COD_RETORNO_EXECUCAO) "+
						   "VALUES (?,?,?,?,?,?)";
		Object params[] = {new Long(solicitacao.getIdRequisicao())
				          ,Definicoes.IDT_EVT_NEGOCIO_VOUCHER
						  ,new Integer(numJob)
						  ,mapRetorno.getDescricao(statusJob)
						  ,new Timestamp(Calendar.getInstance().getTimeInMillis())
						  ,new Integer(codRetorno)
						  };
		// O valor padrao para o campo COD_RETORNO_EXECUCAO e igual a -1 devido
		// ao valor 0 ser o retorno para o codigo de execucao com sucesso, Outra alternativa
		// seria que este campo ficasse nulo, porem para as consultas isso nao e interessante
		// Os valores acima de zero representam erro na plataforma
		conexaoPrep.executaPreparedUpdate(sqlInsert,params,idProcesso);
	}
	
	/**
	 * Metodo....:atualizaStatusJob
	 * Descricao.:Atualiza as informacoes de job de uma requisicao de voucher
	 * @param conexaoPrep		- Conexao de banco de dados a ser utilizada
	 * @param numJob			- Numero do job a atualizar no registro
	 * @param statusJob			- Descricao do status do job
	 * @param eventoNeg			- Evento de negocio do registro da requisicao
	 * @param idProcessamento	- id do processamento da requisicao
	 * @throws GPPInternalErrorException
	 */
	public static void atualizaStatusJob(PREPConexao conexaoPrep, long idProcesso, JobTecnomen job, long idProcessamento, String eventoNeg) 
	   throws GPPInternalErrorException 
	{
		MapTVMCodRetorno mapRet = MapTVMCodRetorno.getInstancia();
		String sqlUpdate = "UPDATE TBL_GER_STATUS_JOB_TECNOMEN " +
		                      "SET DES_STATUS_JOB = ? " +
							     ",COD_RETORNO_EXECUCAO = ? " +
								 ",DAT_ATUALIZACAO = SYSDATE " +
							"WHERE ID_PROCESSAMENTO = ? " +
							  "AND IDT_EVENTO_NEGOCIO = ? " +
							  "AND NUM_JOB_TECNOMEN = ?";
		Object param[] = {mapRet.getDescricao((int)job.getOpState())
						 ,new Long(job.getOpState())
						 ,new Long(idProcessamento)
						 ,eventoNeg
						 ,new Integer(job.getNumeroJob())
				         };
		conexaoPrep.executaPreparedUpdate(sqlUpdate,param,idProcesso);
	}
	
	/**
	 * Metodo....:atualizaStatusInterface
	 * Descricao.:Atualiza status do registro na interface para identificar
	 *            que este registro esta sendo processado
	 * @param conexaoPrep - Conexao de banco de dados a ser utilizada
	 * @throws GPPInternalErrorException
	 */
	public static void atualizaStatusInterface(PREPConexao conexaoPrep, long idProcesso, String status, long idProcessamento, String evtNegocio)
			throws GPPInternalErrorException
	{
		String sqlUpdate = "UPDATE TBL_INT_PPP_IN " +
		                      "SET IDT_STATUS_PROCESSAMENTO = ? " +
							"WHERE ID_PROCESSAMENTO = ? " +
							  "AND IDT_EVENTO_NEGOCIO = ?";
		Object params[] = {status, new Long(idProcessamento), evtNegocio};
		conexaoPrep.executaPreparedUpdate(sqlUpdate, params, idProcesso);
	}
	
	/**
	 * metodo....:atualizaInterfaceSaida
	 * Descricao.:Atualiza as informacoes da ativacao na tabela de interface para ser lida por
	 *            outros sistemas
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @param job			- Job tecnomen que executou a acao
	 * @param solicitacao	- Dados da solicitacao original
	 */
	public static void atualizaInterfaceSaida(PREPConexao conexaoPrep, long idProcesso, JobTecnomen job, SolicitacaoAtivacao solicitacao)
			throws GPPInternalErrorException,SQLException
	{
		// Busca informacoes do motivo da solicitacao para ser enviado no XML de resposta
		String descMotivo = null;
		String sqlMot = "SELECT DES_MOTIVO FROM TBL_REC_MOTIVO_BLOQ_VOUCHER WHERE ID_MOTIVO = ?";
		Object paramMot[] = {solicitacao.getCod_Motivo()};
		ResultSet rs = conexaoPrep.executaPreparedQuery1(sqlMot,paramMot,idProcesso);
		if (rs.next())
			descMotivo = rs.getString("DES_MOTIVO");

		// Realiza o insert na tabela de interface. Primeiro monta o XML de retorno que sera inserido no registro
		String sqlAtu = "INSERT INTO TBL_INT_PPP_OUT " +
		                "(ID_PROCESSAMENTO,DAT_CADASTRO,IDT_EVENTO_NEGOCIO,XML_DOCUMENT,IDT_STATUS_PROCESSAMENTO) "+
						"VALUES (?,?,?,?,?)";
		Object param[] = {new Long(solicitacao.getIdRequisicao())
				         ,new Timestamp(Calendar.getInstance().getTimeInMillis())
						 ,solicitacao.getEventoNegocio()
						 ,ManipuladorXMLVoucher.getXMLRetorno(solicitacao, montaDetalhesXML(job, solicitacao, descMotivo))
						 ,Definicoes.IDT_PROCESSAMENTO_NOT_OK
						 };
		
		conexaoPrep.executaPreparedUpdate(sqlAtu,param,idProcesso);
	}
	
	/**
	 * Metodo....:atualizaInterfaceSaida
	 * Descricao.:Atualiza a tabela de saida TBL_INT_PPP_OUT contendo as informacoes da solicitacao de 
	 *            ativacao/cancelamento de vouchers
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @param solicitacao	- Dados da solicitacao de ativacao/cancelamento de vouchers
	 * @param detalhesXML	- Detalhes da execucao de ativacao/cancelamento
	 * @throws GPPInternalErrorException
	 */
	public static void atualizaInterfaceSaida(PREPConexao conexaoPrep, long idProcesso, SolicitacaoAtivacao solicitacao, Collection detalhesXML) throws GPPInternalErrorException
	{
		// Realiza o insert na tabela de interface. Primeiro monta o XML de retorno que sera inserido no registro
		String sqlAtu = "INSERT INTO TBL_INT_PPP_OUT " +
		                "(ID_PROCESSAMENTO,DAT_CADASTRO,IDT_EVENTO_NEGOCIO,XML_DOCUMENT,IDT_STATUS_PROCESSAMENTO) "+
						"VALUES (?,?,?,?,?)";

		Object param[] = {new Long(solicitacao.getIdRequisicao())
				         ,new Timestamp(Calendar.getInstance().getTimeInMillis())
						 ,solicitacao.getEventoNegocio()
						 ,ManipuladorXMLVoucher.getXMLRetorno(solicitacao,detalhesXML)
						 ,Definicoes.IDT_PROCESSAMENTO_NOT_OK
						 };

		conexaoPrep.executaPreparedUpdate(sqlAtu,param,idProcesso);
	}
	
	/**
	 * Metodo....:removeStatusJob
	 * Descricao.:Remove a informacao do status do job na tabela
	 * @param conexaoPrep		- Conexao de banco de dados a ser utilizada
	 * @param idProcessamento	- Id do processamento da requisicao
	 * @param eventoNeg			- Evento de negocio do registro da requisicao
	 * @throws GPPInternalErrorException
	 */
	public static void removeStatusJob(PREPConexao conexaoPrep, long idProcesso, long idProcessamento, String eventoNeg) 
	   throws GPPInternalErrorException 
	{
		String sqlDelete = "DELETE FROM TBL_GER_STATUS_JOB_TECNOMEN " +
							"WHERE ID_PROCESSAMENTO = ? " +
							  "AND IDT_EVENTO_NEGOCIO = ? ";
		Object param[] = {new Long(idProcessamento) ,eventoNeg};
		conexaoPrep.executaPreparedUpdate(sqlDelete, param, idProcesso);
	}
	
	
	/**
	 * Metodo....:montaDetalhesXML
	 * Descricao.:Realiza a montagem dos detalhes do XML devido ao XML de retorno poder ter a numeracao
	 *            de voucher quebrada de forma em que um lote pode ter sucesso e outro nao na execucao
	 *            da operacao. No caso de ativacao de vouchers, todo a faixa Ini-Fim e realizada de uma
	 *            unica vez, sendo entao impossivel ter este dado quebrado, por motivo de reutilizacao
	 *            do metodo envia uma collection de somente um elemento contendo a faixa de ativacao
	 *            requisitada 
	 * @param job			- Job Tecnomen indicando o sucesso ou nao da ativacao
	 * @param solicitacao	- Dados da solicitacao
	 * @param motivo		- Descricao do motivo a ser adicionado nos detalhes
	 * @return Collection	- Lista de detalhes a serem incluidos no XML
	 */
	private static Collection montaDetalhesXML(JobTecnomen job, SolicitacaoAtivacao solicitacao, String motivo)
	{
		Collection listaDetalhes = new LinkedList();

		int codErro = job.jobTerminadoSucesso() ? Definicoes.IND_ACERTO_VOUCHER : Definicoes.IND_ERRO_VOUCHER;

		DetalheVoucherXML detVoucher = new DetalheVoucherXML();
		detVoucher.setCod_Ativacao		(codErro							);
		detVoucher.setNumero_Caixa		(solicitacao.getNumero_Caixa()		);
		detVoucher.setNum_Inicial_Lote	(solicitacao.getNum_Inicial_Lote()	);
		detVoucher.setNum_Final_Lote	(solicitacao.getNum_Final_Lote()	);
		detVoucher.setMsgRetorno		(motivo								);

		listaDetalhes.add(detVoucher);
		return listaDetalhes;
	}
}