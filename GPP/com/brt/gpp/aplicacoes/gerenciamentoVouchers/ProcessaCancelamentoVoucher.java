package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;

import com.brt.gpp.aplicacoes.consultar.Voucher;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao.VoucherDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe realiza o processamento de uma requisicao de cancelamento
 * de voucher. Os dados da requisicao sao passados como parametro na
 * criacao desta thread, sendo que o ciclo de vida deste programa 
 * consiste em atualizar um status na tabela de interface para indicar
 * que esta requisicao esta em processamento. Apos essa atualizacao
 * envia a requisicao para a plataforma tecnomen para o cancelamento gravando
 * o numero do Job responsavel por esse processo

 *  
 * @author Joao Carlos
 *
 */

public class ProcessaCancelamentoVoucher implements ProcessaSolicitacao
{
	private SolicitacaoAtivacao solicitacao;
	private long				idProcesso;
	private GerentePoolLog		poolLog;
	
	/**
	 * Metodo....:ProcessaCancelamentoVoucher
	 * Descricao.:Construtor da classe
	 * @param solicitacao - Dados da solicitacao de ativacao
	 */
	public ProcessaCancelamentoVoucher(SolicitacaoAtivacao solicitacao, long idProcesso)
	{
		this.solicitacao = solicitacao;
		this.idProcesso  = idProcesso;
		this.poolLog	 = GerentePoolLog.getInstancia(this.getClass());
	}
	

	/**
	 * @see com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaSolicitacao#executarSolicitacao()
	 */
	public void executarSolicitacao() throws Exception
	{
		PREPConexao conexaoPrep = null;
		try
		{
			poolLog.log(idProcesso, Definicoes.INFO, "ProcessaCancelamentoVoucher", "executarSolicitacao"
					, "Inicio do cancelamento de vouchers da solicitacao " + solicitacao.getIdRequisicao());
			OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			conexaoPrep.setAutoCommit(false);
			
			// Atualiza dados da interface indicando o registro em processamento
			VoucherDAO.atualizaStatusInterface(conexaoPrep, idProcesso, Definicoes.IDT_PROCESSANDO
					,solicitacao.getIdRequisicao()
					,solicitacao.getEventoNegocio());

			// A numeracao dos vouchers e a concatenacao do numero da caixa com o numero inicio e fim
			String numVoucherIni = solicitacao.getNumero_Caixa() + solicitacao.getNum_Inicial_Lote();
			String numVoucherFim = solicitacao.getNumero_Caixa() + solicitacao.getNum_Final_Lote();
			
			// Define um hash para conter os status dos batches de voucher
			Map statusBatch = new HashMap();
			// Faz uma iteracao entre todos os batches de vouchers atraves dos numeros enviados
			// na solicitacao e para cada batch verifica seu status para definir se o mesmo sera
			// processado ou nao e entao montar a lista de resposta
			long longVoucherFim = Long.parseLong(numVoucherFim);
			while (Long.parseLong(numVoucherIni) <= longVoucherFim)
			{
				// Busca a informacao do voucher
				Voucher voucher = opVoucher.getInformacoesVoucher(numVoucherIni);
				// Caso o voucher esteja invalidado entao ja esta ok o cancelamento
				// o valor armazenado no hash indica o sucesso assim como se quando
				// a execucao da api retornar ok. No caso de voucher nao ativo
				// entao armazena o codigo de erro para identifcar as faixas problematicas
				if (voucher.getCodStatusVoucher() == Definicoes.VOUCHER_INVALIDADO)
					statusBatch.put(numVoucherIni, new Integer(Definicoes.IND_ACERTO_VOUCHER));
				else if (voucher.getCodStatusVoucher() != Definicoes.VOUCHER_ATIVO)
						statusBatch.put(numVoucherIni, new Integer(Definicoes.IND_ERRO_VOUCHER));
				else
				{
					// Nessa condicao entao o voucher esta ativo indicando que pode ser cancelado
					// portanto executa-se a api de cancelamento e o codigo de retorno indica
					// se a chamada foi executada com sucesso ou nao
					//int codRetorno = opVoucher.atualizaStatusVoucher(voucher.getNumeroVoucher(),Definicoes.VOUCHER_INVALIDADO,"Cancelamento");
					int codRetorno = opVoucher.atualizaStatusBatch(voucher.getNumeroBatch(),Definicoes.VOUCHER_INVALIDADO,"Cancelamento");
					statusBatch.put(numVoucherIni, codRetorno == Definicoes.IND_ACERTO_VOUCHER ? new Integer(codRetorno) : new Integer(Definicoes.IND_ERRO_VOUCHER));
				}
				// Realiza o incremento pelo numero de cartoes em um batch e transforma o numero 
				// para uma string devido a api da tecnomen ser deste tipo de dados
				numVoucherIni = String.valueOf(Long.parseLong(numVoucherIni) + Definicoes.NUMERO_VOUCHERS_POR_BATCH);
			}
			// Insere as informacoes na tabela de output contendo o XML de retorno
			VoucherDAO.atualizaInterfaceSaida(conexaoPrep, idProcesso, solicitacao,getDetalhesXML(statusBatch));

			// Atualiza dados da interface indicando o registro processado
			VoucherDAO.atualizaStatusInterface(conexaoPrep, idProcesso, Definicoes.IDT_PROCESSAMENTO_OK
											               ,solicitacao.getIdRequisicao()
														   ,solicitacao.getEventoNegocio());
			conexaoPrep.commit();
		}
		catch(Exception e)
		{
			try
			{
				conexaoPrep.rollback();
			}
			catch(SQLException se)
			{
				poolLog.log(idProcesso,Definicoes.WARN,"ProcessaCancelamentoVoucher","run","Erro ao desfazer transacoes de cancelamento de voucher. Erro:"+se);
			}
			poolLog.log(idProcesso,Definicoes.WARN,"ProcessaCancelamentoVoucher","run","Erro ao processar solicitacao de cancelamento de vouchers fisicos. Erro:"+e);
			throw(e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
			poolLog.log(idProcesso, Definicoes.INFO, "ProcessaCancelamentoVoucher", "executarSolicitacao"
					, "Fim do cancelamento de vouchers da solicitacao " + solicitacao.getIdRequisicao());
		}
	}

	/**
	 * Metodo....:getDetalhesXML
	 * Descricao.:Retorna uma colecao contendo informacoes dos detalhes do XML de resposta
	 * @param statusBatch - Hash contendo informacoes de batch
	 * @return
	 */
	private Collection getDetalhesXML(Map statusBatch)
	{
		DecimalFormat df = new DecimalFormat("000");
		Collection listaDetalhes = new LinkedList();
		// Por motivo de ordenacao, a busca do status de cada batch e feito 
		// atraves de uma iteracao pela numeracao dos vouchers ao inves de
		// ser feito atraves da iteracao dos elementos do Hash
		String numVoucherIni = solicitacao.getNumero_Caixa() + solicitacao.getNum_Inicial_Lote();
		String numVoucherFim = solicitacao.getNumero_Caixa() + solicitacao.getNum_Final_Lote();

		// Define algumas variaveis de referencia para serem utilizadas na comparacao com o restante
		// dos vouchers a serem verificados
		long numVouRef	= Long.parseLong(numVoucherIni);
		int  codAtvRef	= ((Integer)statusBatch.get(numVoucherIni)).intValue();
		// Realiza a iteracao na numeracao dos vouchers afim de identificar 
		// entao quais batches estao em quais estados de execucao.
		// OBS: A iteracao e realizada pela numeracao dos lotes ao inves
		// de ser pela iteracao do hash devido a ordem ser um fator importante
		// e nao confiavel na iteracao pelo hash
		long longVoucherFim = Long.parseLong(numVoucherFim);
		while (Long.parseLong(numVoucherIni) <= longVoucherFim)
		{
			// Busca a informacao do voucher sendo processado
			int codAtivacao = ((Integer)statusBatch.get(numVoucherIni)).intValue();
			// Atraves do codigo de ativacao identifica-se a faixa de batches estao com erro ou nao para entao
			// agrupar esta em um unico lancamento no XML de resposta da execucao
			if (codAtivacao != codAtvRef || Long.parseLong(numVoucherIni)+Definicoes.NUMERO_VOUCHERS_POR_BATCH > longVoucherFim)
			{
				DetalheVoucherXML detVoucher = new DetalheVoucherXML();
				detVoucher.setNumero_Caixa		(solicitacao.getNumero_Caixa());
				detVoucher.setNum_Inicial_Lote	(df.format(numVouRef % 1000));
				detVoucher.setNum_Final_Lote	(df.format( (codAtivacao!=codAtvRef ? (Long.parseLong(numVoucherIni)-1) : longVoucherFim ) % 1000));
				detVoucher.setCod_Ativacao		(codAtvRef);
				detVoucher.setMsgRetorno		(null);
				// Adiciona o elemento detalhe na colecao resultante
				listaDetalhes.add(detVoucher);
				// Define novamente as variaveis de referencia para serem novamente comparadas
				// afim de agrupar os proximos batches com mesmo status de execucao
				numVouRef = Long.parseLong(numVoucherIni);
				codAtvRef = codAtivacao;
			}
			// Incrementa a numeracao de voucher, pois o status do primeiro foi tratado antes da entrada na iteracao
			numVoucherIni = String.valueOf(Long.parseLong(numVoucherIni) + Definicoes.NUMERO_VOUCHERS_POR_BATCH);
		}
		return listaDetalhes;
	}
}
