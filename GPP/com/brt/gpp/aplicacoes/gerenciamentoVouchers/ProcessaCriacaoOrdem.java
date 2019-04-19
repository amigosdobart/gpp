package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao.PedidoDAO;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.sql.SQLException;
import java.util.Calendar;

/**
 * Esta classe e a responsavel por criar a ordem na plataforma 
 * tecnomen do pedido passado como parametro controlando a situacao 
 * do pedido tanto na tabela de interface quanto na copia deste para 
 * a tabela interna do sistema.
 *
 */
public class ProcessaCriacaoOrdem implements ProcessaSolicitacao
{
	private VoucherPedido 			pedidoVoucher;
	private long					idProcesso;
	private GerentePoolLog			poolLog;

	/**
	 * Metodo....:ProcessaCriacaoOrdem
	 * Descricao.:Construtor da classe
	 * @param pedido - Pedido de voucher a ser processado
	 */
	public ProcessaCriacaoOrdem(VoucherPedido pedido, long idProcesso)
	{
		this.pedidoVoucher 	= pedido;
		this.idProcesso		= idProcesso;
		this.poolLog		= GerentePoolLog.getInstancia(this.getClass());
	}

	
	
	/**
	 * @see com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaSolicitacao#executarSolicitacao()
	 */
	public void executarSolicitacao() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			conexaoPrep.setAutoCommit(false);
			// Realiza a troca de status na tabela de interface
			PedidoDAO.trocaStatusPedido(conexaoPrep, idProcesso, pedidoVoucher.getNumPedido());

			// Acerta as informacoes de numero da ordem e do arquivo a serem criados
			MapConfiguracaoGPP confGpp = MapConfiguracaoGPP.getInstancia();
			// Busca o numero da ordem utilizando a API da tecnomen
			pedidoVoucher.setNumOrdem(OperacoesVoucher.getProximoNumeroOrdem(idProcesso));
			pedidoVoucher.setDataCriacao(Calendar.getInstance().getTime());
			pedidoVoucher.setNomeArquivo(confGpp.getMapValorConfiguracaoGPP("NOM_ARQUIVOS_ORDENS_VOUCHER")	+
			                    		 pedidoVoucher.getNumOrdem()   										+
			                    		 confGpp.getMapValorConfiguracaoGPP("EXT_ORDENS_VOUCHER_COMPACT")
					 					);

			// Insere os dados na tabela definitiva de pedidos de voucher
			PedidoDAO.insereDadosPedido(conexaoPrep, idProcesso, pedidoVoucher);

			// Cria a ordem na plataforma tecnomen
			OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
			int codRetorno = opVoucher.criaOrdemVoucher(pedidoVoucher);
			// Caso tenha obtido sucesso entao realiza o commit dos dados atualizados 
			// no banco de dados. O codigo de retorno maior que 10000 indica um numero
			// do job na tecnomen que esta processando a requisicao. Este numero e armazenado
			// no pedido
			if (codRetorno > Definicoes.MIN_COD_RETORNO_JOB_TECNOMEN)
			{
				pedidoVoucher.setNumJobTecnomen(codRetorno);
				PedidoDAO.atualizaNumeroJobPedido(conexaoPrep, idProcesso, pedidoVoucher.getNumJobTecnomen(), pedidoVoucher.getNumPedido());
				conexaoPrep.commit();
			}
			else
			{
				conexaoPrep.rollback();
				MapTVMCodRetorno mapRet = MapTVMCodRetorno.getInstancia();
				poolLog.log(idProcesso,Definicoes.WARN,"ProcessaCriacaoOrdem","processaPedido",
						    "Erro ao criar a ordem de voucher na Tecnomen.Erro:"+codRetorno+" - "+mapRet.getDescricao(codRetorno));
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
				throw new GPPInternalErrorException("Erro ao desfazer processamento do pedido de voucher. Pedido:"+pedidoVoucher+" Erro:"+se);
			}
			throw new GPPInternalErrorException("Erro processar pedido de voucher. Pedido:"+pedidoVoucher+" Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
}
