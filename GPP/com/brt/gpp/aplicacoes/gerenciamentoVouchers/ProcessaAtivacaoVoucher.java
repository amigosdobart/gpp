package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import com.brt.gpp.aplicacoes.consultar.Voucher;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao.VoucherDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;

import java.sql.SQLException;

/**
 * Esta classe realiza o processamento de uma requisicao de ativacao
 * de voucher. Os dados da requisicao sao passados como parametro na
 * criacao desta thread, sendo que o ciclo de vida deste programa 
 * consiste em atualizar um status na tabela de interface para indicar
 * que esta requisicao esta em processamento. Apos essa atualizacao
 * envia a requisicao para a plataforma tecnomen para a ativacao gravando
 * o numero do Job responsavel por esse processo
 *  
 * @author Joao Carlos 
 * Data..: 30-Jan-2005
 *
 */
public class ProcessaAtivacaoVoucher implements ProcessaSolicitacao
{
	private SolicitacaoAtivacao solicitacao;
	private long				idProcesso;
	private GerentePoolLog		poolLog;

	/**
	 * Metodo....:ProcessaAtivacaoVoucher
	 * Descricao.:Construtor da classe
	 * @param solicitacao - Dados da solicitacao de ativacao
	 */
	public ProcessaAtivacaoVoucher(SolicitacaoAtivacao solicitacao, long idProcesso)
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
			poolLog.log(idProcesso, Definicoes.INFO, "ProcessaAtivacaoVoucher", "executarSolicitacao"
					, "Inicio da ativacao de vouchers da solicitacao " + solicitacao.getIdRequisicao());
			OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
			MapTVMCodRetorno mapRet = MapTVMCodRetorno.getInstancia();
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			conexaoPrep.setAutoCommit(false);

			

			// Realiza a pesquisa das informacoes pelo numero do voucher inicial e final
			// dessa requisicao, pegando as informacoes de numeracao de batch para ser
			// enviada na requisicao de ativacao por faixa de valores de batch.
			// Nos dados da solicitacao o numero do voucher a ser utilizado e o numero da
			// caixa concatenado com os valores iniciais e finais. Esta faixa sera ativada
			String numVoucherIni = solicitacao.getNumero_Caixa() + solicitacao.getNum_Inicial_Lote();
			String numVoucherFim = solicitacao.getNumero_Caixa() + solicitacao.getNum_Final_Lote();

			// Pesquisa o primeiro voucher nao ativo a partir do voucher inicial somando o numero de vouchers
			// por batch de forma que o primeiro voucher e menor que o ultimo
			Voucher primVoucher  = getPrimeiroVoucherNaoAtivo(numVoucherIni,numVoucherFim,Definicoes.NUMERO_VOUCHERS_POR_BATCH,opVoucher);
			numVoucherIni = primVoucher.getNumeroVoucher();
			
			if(numVoucherIni != null)
			{
				// Atualiza dados da interface indicando o registro em processamento
				VoucherDAO.atualizaStatusInterface(conexaoPrep, idProcesso, Definicoes.IDT_PROCESSANDO
						               ,solicitacao.getIdRequisicao()
									   ,solicitacao.getEventoNegocio());

				// Se a numeracao do voucher inicial for maior que a numeracao do voucher final
				// isso indica que toda a faixa ja esta ativa portanto insere um job fake na 
				// tabela indicando que ja esta ok o processamento
				if(Long.parseLong(numVoucherIni) > Long.parseLong(numVoucherFim))
				{
					VoucherDAO.insereInformacoesJob(conexaoPrep, idProcesso, solicitacao
										,0
										,Definicoes.COD_STATUS_JOB_PROCESSADO_OK
										,Definicoes.COD_RET_JOB_PROCESSANDO);
				}
				else // Situacao normal
				{
					// Pesquisa o primeiro voucher nao ativo a partir do voucher final diminuindo o numero de vouchers
					// por batch de forma que o primeiro voucher e maior que o ultimo
					Voucher ultmVoucher  = getPrimeiroVoucherNaoAtivo(numVoucherFim,numVoucherIni,Definicoes.NUMERO_VOUCHERS_POR_BATCH*-1,opVoucher);
					numVoucherFim 		 = ultmVoucher.getNumeroVoucher();

					String numBatchInicial 	= primVoucher.getNumeroBatch();
					String numBatchFinal 	= ultmVoucher.getNumeroBatch();
		
					// Realiza a ativacao da faixa de cartoes
					int codRetorno = opVoucher.ativaFaixaBatch(numBatchInicial,numBatchFinal);
					if (codRetorno > Definicoes.MIN_COD_RETORNO_JOB_TECNOMEN)
					{
						// Insere as informacoes do job em uma tabela para controle
						VoucherDAO.insereInformacoesJob(conexaoPrep, idProcesso, solicitacao
											,codRetorno
											,Definicoes.COD_STATUS_JOB_PROCESSANDO
											,Definicoes.COD_RET_JOB_PROCESSANDO);
					}
					else // Em caso de erro ao chamar a API entao desfaz a transacao e efetua um log do erro
					{
						poolLog.log(idProcesso
							    ,Definicoes.WARN
								,"ProcessaAtivacaoVoucher"
								,"processarSolicitacao"
								,"Erro ao ativar batch de vouchers. Batch inicial:"+numBatchInicial+" Batch final:"+numBatchFinal+". Retorno:"+codRetorno+" - "+mapRet.getDescricao(codRetorno)
								);
						conexaoPrep.rollback();
					}
				}
				conexaoPrep.commit();
			}
		}
		catch(Exception e)
		{
			try
			{
				conexaoPrep.rollback();
			}
			catch(SQLException se)
			{
				poolLog.log(idProcesso,Definicoes.WARN,"ProcessaAtivacaoVoucher","processarSolicitacao","Erro ao desfazer transacoes de ativacao de voucher. Erro:"+se);
			}
			poolLog.log(idProcesso,Definicoes.WARN,"ProcessaAtivacaoVoucher","processarSolicitacao","Erro ao processar solicitacao de ativacao de vouchers fisicos. Erro:"+e);
			throw(e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
			poolLog.log(idProcesso, Definicoes.INFO, "ProcessaAtivacaoVoucher", "executarSolicitacao"
					, "Fim da ativacao de vouchers da solicitacao " + solicitacao.getIdRequisicao());
		}
	}

	

	/**
	 * Metodo....:primeiroVoucherNaoAtivo
	 * Descricao.:Retorna o numero do primeiro voucher da faixa dessa requisicao
	 *            que nao esta ativo
	 * @param solicitacao 	- Dados da solicitacao de ativacao
	 * @param opVoucher 	- Classe para as operacoes de voucher
	 * @return Voucher		- Primeiro voucher nao ativo da faixa solicitada 
	 * @throws Exception
	 */
	private Voucher getPrimeiroVoucherNaoAtivo(String numVoucherIni, String numVoucherFim, int fator, OperacoesVoucher opVoucher) throws Exception
	{
		// Divide o fator pelo modulo dele mesmo para identificar qual sinal esta sendo utilizado
		// em caso de positivo o numero inicial e menor que o numero final, em caso negativo entao
		// e o contrario. Isso voi ser decisivo para que a condicao no loop seja satisfeita para
		// identificar em qual voucher esta valendo a possibilidade de ativacao
		int div = fator/Math.abs(fator);
		
		// Consulta o voucher da faixa inicial e final, caso o status deste seja ativo, entao
		// incrementa o valor da quantidade de vouchers em um batch e tenta novamente ate encontrar
		// um voucher que atenda a situacao de ISSUED pronto pra ativacao
		// Devido a numeracao de batch ser o numero de vouchers que serao ativados entao ao verificar
		// se o voucher esta ativo ou nao a numeracao nao pode ser maior que a numeracao do ultimo batch
		Voucher voucher = opVoucher.getInformacoesVoucher(numVoucherIni);
		while ( (voucher.getCodStatusVoucher() == Definicoes.VOUCHER_ATIVO  || 
				 voucher.getCodStatusVoucher() == Definicoes.VOUCHER_USADO) &&
				Long.parseLong(numVoucherIni)*div <= Long.parseLong(numVoucherFim)*div 
			   )
		{
			// Realiza o incremento pelo numero de cartoes em um batch e transforma o numero 
			// para uma string devido a api da tecnomen ser deste tipo de dados
			numVoucherIni = String.valueOf(Long.parseLong(numVoucherIni) + fator);
			voucher = opVoucher.getInformacoesVoucher(numVoucherIni);
		}
		return voucher;
	}
}
