package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao.PedidoDAO;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdem;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemCaixa;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemLote;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedidoItem;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Esta classe e responsavel pelo controle dos jobs assincronos de 
 * operacoes com os pedidos de voucher.
 * A classe realiza a verificacao de jobs dos pedidos, sendo entao
 * que esta executa o procedimento seguinte das operacoes necessarias
 * ate que todos os estagios do pedido sejam atingidos.
 * 
 */
public class GerenciaPedidosVoucher extends Thread
{
	private static	long					idProcesso;
	private	static	GerentePoolLog			poolLog;
	private static 	GerenciaPedidosVoucher 	instancia;

	/**
	 * Metodo....:GerenciaJobsTecnomen
	 * Descricao.:Construtor da classe
	 *
	 */
	private GerenciaPedidosVoucher()
	{
		poolLog 	= GerentePoolLog.getInstancia(this.getClass());
		idProcesso 	= poolLog.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		poolLog.log(idProcesso,Definicoes.INFO,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"GerenciaPedidosVoucher","Inicializado o Gerenciador de Pedidos de Voucher.");
		start();
	}

	/**
	 * Metodo....:getInstancia
	 * Descricao.:Retorna a instancia (singleton) desta classe
	 * @return GerenciaJobsTecnomen - Instancia da classe
	 */
	public static GerenciaPedidosVoucher getInstancia()
	{
		GerentePoolLog gerenteLog = GerentePoolLog.getInstancia(GerenciaPedidosVoucher.class);
		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		if (arqConf.deveGerenciarPedidosVoucher())
		{
			if (instancia == null)
				instancia = new GerenciaPedidosVoucher();
		}
		else
		{
			gerenteLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"getInstancia","O Gerente de Pedidos de Voucher nao foi inicializado.");
		}
		return instancia;
	}

	/**
	 * Metodo....:verificaCriacaoOrdemPedidos
	 * Descricao.:Este metodo realiza a verificacao dos job's para os pedidos com status 
	 *            de solicitacao para verificar se as ordens dos mesmos ja foram criadas
	 *            na plataforma. Em caso de termino o metodo somente atualiza o status
	 *            do pedido ja que a proxima acao depende do arquivo de vouchers a ser
	 *            disponibilizado
	 */
	private void verificaCriacaoOrdemPedidos()
	{
		MapTVMCodRetorno mapRet  	= MapTVMCodRetorno.getInstancia();
		OperacoesVoucher opVoucher 	= new OperacoesVoucher(idProcesso);
		// Os pedidos que estiverem com o status de EM SOLICITACAO sao os pedidos
		// que foram criados ordens, caso os job's desses pedidos tenham terminado
		// entao atualiza o status do mesmo deixando o campo num_job_tecnomen zerado
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,Definicoes.STATUS_VOUCHER_GERACAO_SOLICITADA);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedido = (VoucherPedido)i.next();
			JobTecnomen job = opVoucher.getJobTecnomen(pedido.getNumJobTecnomen());
			if (job != null && job.jobTerminadoSucesso())
				PedidoDAO.atualizaSituacaoPedido(pedido.getNumPedido(),0,Definicoes.STATUS_VOUCHER_CRIADO);
			else if (job != null && job.jobTerminadoErro())
					// Atualiza a descricao do erro do job na tabela do pedido
					PedidoDAO.atualizaStatusJobPedido(pedido.getNumPedido(),Definicoes.STATUS_PEDIDO_VOUCHER_ERRO,mapRet.getDescricao((int)job.getOpState()));
		}
	}

	/**
	 * Metodo....:verificaImpressaoOrdemPedidos
	 * Descricao.:Este metodo realiza a verificacao dos pedidos de ordens de voucher que foram
	 *            criados pela plataforma para identificar quais destes ja foram impressos ou
	 *            seja, ja possuem numeracao de caixa e lote para os vouchers criados para serem
	 *            atualizados nos dados do pedido
	 *
	 */
	private void verificaImpressaoOrdemPedidos()
	{
		// Os pedidos que estiverem com o status de CRIADO sao os pedidos
		// que foram criados ordens e que estao aguardando a impressao dessas
		// aonde busca-se as informacoes de numeros de caixa e lote correspondentes
		// atualizando tais informacoes na tabela e marcando o pedido como impresso
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,Definicoes.STATUS_VOUCHER_CRIADO);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedidoListado = (VoucherPedido)i.next();
			// Realiza a pesquisa para trazer os dados completos do pedido e seus itens
			VoucherPedido pedido = PedidoDAO.buscaPedido(pedidoListado.getNumPedido());
			if (pedido != null)
			{
				for (Iterator j = pedido.getItensPedido().iterator(); j.hasNext();)
				{
					VoucherPedidoItem item = (VoucherPedidoItem)j.next();
					// Se no objeto ja estiver populado as informacoes de caixa e lote
					// entao significa que o pedido ja foi disponibilizado em arquivo e
					// o mesmo ja pode ser recebido e continuar o fluxo de ativacao
					if (item.getNumCaixaLoteInicial() != 0 && item.getNumCaixaLoteFinal() != 0)
						PedidoDAO.atualizaSituacaoPedido(pedido.getNumPedido(),0,Definicoes.STATUS_VOUCHER_DISPONIBILIZADO);
				}
			}
		}
	}

	/**
	 * Metodo....:processaPedidosDisponibilizados
	 * Descricao.:Este metodo busca todos os pedidos que foram disponibilizados o arquivo de
	 *            voucher e para cada um realiza o recebimento na plataforma. O recebimento
	 *            retorna um numero de job que e novamente associado ao pedido para posterior
	 *            verificacao do termino deste procedimento e atualizacao do proximo estagio
	 *
	 */
	private void processaPedidosDisponibilizados()
	{
		OperacoesVoucher opVoucher 	= new OperacoesVoucher(idProcesso);
		// Para os pedidos Disponibilizados, executa o recebimento dos mesmos atualizando
		// o status no banco de dados indicando o processamento
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,Definicoes.STATUS_VOUCHER_DISPONIBILIZADO);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedido = (VoucherPedido)i.next();
			// Executa o recebimento dos pedidos na plataforma somente se o numero do job
			// for vazio (igual a 0), senao indica que este ja esta em processamento
			if (pedido.getNumJobTecnomen() == 0)
				recebeOrdem(opVoucher, pedido);
		}
	}
	
	/**
	 * Metodo....:verificaRecebimentoOrdemPedidos
	 * Descricao.:Este metodo realiza a verificacao dos job's para os pedidos com status 
	 *            de disponibilizados que possuem o numero do job diferente de 0 (zero)
	 *            para entao identificar que os mesmos foram recebidos. Caso afirmativo
	 *            entao a ordem e finalizada (ISSUED) e o job que esta realizando este processo
	 *            e definido na tabela de pedidos no banco de dados sendo que seu status entao
	 *            ja e recebido e em fase de finalizacao
	 */
	private void verificaRecebimentoOrdemPedidos()
	{
		MapTVMCodRetorno mapRet 	= MapTVMCodRetorno.getInstancia();
		OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
		// Os pedidos que possuirem status igual a DISPONIBILIZADO neste momento sao os pedidos
		// que o job da tecnomen esta realizando o recebimento da ordem. Portanto caso o job
		// tenha sido concluido entao atualiza o status no banco de dados e ja realiza o proximo
		// passo do processo que e passar o pedido para ISSUED (Finalizado)
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,Definicoes.STATUS_VOUCHER_DISPONIBILIZADO);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedido = (VoucherPedido)i.next();
			// O numero do Job tecnomen indica que o pedido em estado DISPONIBILIZADO esta em
			// recebimento, entao verifica se o mesmo ja acabou para atualizar seu status
			if (pedido.getNumJobTecnomen() != 0)
			{
				JobTecnomen job = opVoucher.getJobTecnomen(pedido.getNumJobTecnomen());
				// Caso o job for nulo, entao a plataforma perdeu esta referencia. Neste caso o GPP irah
				// verificar se a ordem estah correta com relacao ao status esperado e realiza os procedimentos
				// necessarios
				boolean ordemOk = false;
				if (job == null)
					ordemOk = verificarPedidoNaPlataforma(pedido,Definicoes.STATUS_VOUCHER_RECEBIDO);
				
				poolLog.log(0
						   ,Definicoes.INFO
						   ,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
						   ,"verificaRecebimentoOrdemPedidos"
						   ,"Job:"+job+ " Ord:"+pedido.getNumOrdem()+" Retornou "+ordemOk+" na verificacao na plataforma. Sta:"+Definicoes.STATUS_VOUCHER_RECEBIDO
						   );
				
				if ( (job != null && job.jobTerminadoSucesso()) || (job == null && ordemOk) )
					// Passa o pedido para ISSUED e atualiza o numero do job que esta realizando
					// tal acao assim como a indicacao que o recebimento esta ok no banco de dados
					finalizaOrdem(opVoucher, pedido);
				else if (job != null && job.jobTerminadoErro())
						// Atualiza a descricao do erro do job na tabela do pedido
						PedidoDAO.atualizaStatusJobPedido(pedido.getNumPedido(),Definicoes.STATUS_PEDIDO_VOUCHER_ERRO,mapRet.getDescricao((int)job.getOpState()));
				
				// Caso a flag ordemOK estiver false, isto indica que o job
				// foi perdido e este nao terminou seu processamento como deveria
				// portanto realiza novamente o recebimento da ordem
				if (!ordemOk && job == null)
					recebeOrdem(opVoucher, pedido);
			}
		}
	}

	/**
	 * Metodo....:verificaFinalizacaoOrdemPedidos
	 * Descricao.:Este metodo realiza a verificacao dos job's para os pedidos com status
	 *            de RECEBIDO para identificar se o job atual (finalizacao) ja foi concluido
	 *            caso afirmativo entao o pedido e encaminhado para a ativacao e seu status
	 *            indica que ja foi finalizado (ISSUED) 
	 */
	private void verificaFinalizacaoOrdemPedidos()
	{
		MapTVMCodRetorno mapRet 	= MapTVMCodRetorno.getInstancia();
		OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
		// Busca os pedidos que estao com o status recebido para verificar se o job tecnomen
		// destes ja foi concluido indicando entao que os mesmos ja foram finalizados e estao
		// prontos para a fase de ativacao.
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,Definicoes.STATUS_VOUCHER_RECEBIDO);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedido = (VoucherPedido)i.next();
			JobTecnomen job = opVoucher.getJobTecnomen(pedido.getNumJobTecnomen());
			
			// Caso o job for nulo, entao a plataforma perdeu esta referencia. Neste caso o GPP irah
			// verificar se a ordem estah correta com relacao ao status esperado e realiza os procedimentos
			// necessarios
			boolean ordemOk = false;
			if (job == null)
				ordemOk = verificarPedidoNaPlataforma(pedido,Definicoes.STATUS_VOUCHER_EMITIDO);
			
			poolLog.log(0
					   ,Definicoes.INFO
					   ,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
					   ,"verificaFinalizacaoOrdemPedidos"
					   ,"Job:"+job+ " Ord:"+pedido.getNumOrdem()+" Retornou "+ordemOk+" na verificacao na plataforma. Sta:"+Definicoes.STATUS_VOUCHER_EMITIDO
					   );
			
			if ( (job != null && job.jobTerminadoSucesso()) || (job == null && ordemOk) )
			{
				// Neste ponto do processamento da ordem somente as ordens de cartoes virtuais
				// ou Ligmix sao ativadas, sendo que as de cartao fisico sao realizadas em outro momento.
				// Portanto os pedidos de cartao fisico sao nesso ponto finalizados
				if (!Definicoes.TIPO_CARTAO_FISICO.equals(pedido.getTipCartao()))
					// Ativa a ordem na tecnomen, pega o numero do job que esta realizando a tarefa
					// e atualiza no cabecalho do pedido esta informacao
					ativaOrdem(opVoucher, pedido);
				else
					PedidoDAO.finalizaProcessamentoPedido(pedido.getNumPedido());
			}
			else if (job != null && job.jobTerminadoErro())
					// Atualiza a descricao do erro do job na tabela do pedido
					PedidoDAO.atualizaStatusJobPedido(pedido.getNumPedido(),Definicoes.STATUS_PEDIDO_VOUCHER_ERRO,mapRet.getDescricao((int)job.getOpState()));
			
			// Caso a flag ordemOK estiver false, isto indica que o job
			// foi perdido e este nao terminou seu processamento como deveria
			// portanto realiza novamente a finalizacao da ordem
			if (!ordemOk && job == null)
				finalizaOrdem(opVoucher, pedido);
		}
	}


	/**
	 * Metodo....:verificaAtivacaoOrdemPedidos
	 * Descricao.:Este metodo realiza a verificacao dos job's para os pedidos com status 
	 *            EMITIDO (ISSUED) pesquisando se o job de ativacao foi terminado. Em caso
	 *            afirmativo entao o pedido e identificado com este status e o numero do 
	 *            job fica nulo indicando que nenhuma acao e necessaria. O pedido neste ponto
	 *            tambem ja e processado na tabela de interface.
	 */
	private void verificaAtivacaoOrdemPedidos()
	{
		MapTVMCodRetorno mapRet 	= MapTVMCodRetorno.getInstancia();
		OperacoesVoucher opVoucher 	= new OperacoesVoucher(idProcesso);
		// Busca a lista de pedidos que estao em ativacao, caso o job desta tarefa
		// tenha sido concluido entao atualiza o status indicando que o pedido esta
		// ativo e este ja pode ser concluido para a tabela de interface
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,Definicoes.STATUS_VOUCHER_EMITIDO);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedido = (VoucherPedido)i.next();
			// Se a numeracao do Job estiver igual a zero neste ponto do processo, indica
			// que por algum motivo o job de ativacao nao finalizou e portanto devera
			// ser re-submetido ao mesmo job de ativacao
			if ( pedido.getNumJobTecnomen() == 0 && Definicoes.TIPO_CARTAO_VIRTUAL.equals(pedido.getTipCartao()) )
				ativaOrdem(opVoucher,pedido);
			else // O numero do Job e diferente de zero, entao pesquisa informacoes sobre job pra continuacao do processo
			{
				JobTecnomen job = opVoucher.getJobTecnomen(pedido.getNumJobTecnomen());
				
				// Caso o job for nulo, entao a plataforma perdeu esta referencia. Neste caso o GPP irah
				// verificar se a ordem estah correta com relacao ao status esperado e realiza os procedimentos
				// necessarios
				boolean ordemOk = false;
				if (job == null)
					ordemOk = verificarPedidoNaPlataforma(pedido,Definicoes.STATUS_VOUCHER_ATIVADO);
				
				poolLog.log(0
						   ,Definicoes.INFO
						   ,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
						   ,"verificaAtivacaoOrdemPedidos"
						   ,"Job:"+job+ " Ord:"+pedido.getNumOrdem()+" Retornou "+ordemOk+" na verificacao na plataforma. Sta:"+Definicoes.STATUS_VOUCHER_ATIVADO
						   );
				
				if ( (job != null && job.jobTerminadoSucesso()) || (job == null && ordemOk) )
				{
					PedidoDAO.atualizaSituacaoPedido(pedido.getNumPedido(),0,Definicoes.STATUS_VOUCHER_ATIVADO);
					PedidoDAO.finalizaProcessamentoPedido(pedido.getNumPedido());
				}
				else if (job != null && job.jobTerminadoErro())
						// Atualiza a descricao do erro do job na tabela do pedido
						PedidoDAO.atualizaStatusJobPedido(pedido.getNumPedido(),Definicoes.STATUS_PEDIDO_VOUCHER_ERRO,mapRet.getDescricao((int)job.getOpState()));
				
				// Caso a flag ordemOK estiver false, isto indica que o job
				// foi perdido e este nao terminou seu processamento como deveria
				// portanto realiza novamente a ativacao da ordem
				if (!ordemOk && job == null)
					ativaOrdem(opVoucher, pedido);
			}
		}
	}
	
	/**
	 * Metodo....:ativaOrdem
	 * Descricao.:Metodo que realiza efetivamente a ativacao da ordem
	 *            O status do pedido fica como ISSUED indicando o status
	 *            atual. O numero do job responsavel pela ativacao e 
	 *            armazenado no cabecalho do pedido
	 * @param opVoucher - Classe que realizara as operacoes de voucher
	 * @param pedido	- Pedido de criacao de ordem de vouchers
	 */
	private void ativaOrdem(OperacoesVoucher opVoucher, VoucherPedido pedido)
	{
		MapTVMCodRetorno mapRet = MapTVMCodRetorno.getInstancia();
		// Realiza a ativacao da ordem e atualiza o status da mesma.
		// O status fica como issued para indicar o status atual.
		int codRetorno = opVoucher.ativaOrdemVoucher(pedido.getNumOrdem());
		if (codRetorno > Definicoes.MIN_COD_RETORNO_JOB_TECNOMEN)
		{
			PedidoDAO.atualizaSituacaoPedido(pedido.getNumPedido(),codRetorno,Definicoes.STATUS_VOUCHER_EMITIDO);
			poolLog.log(0
					   ,Definicoes.INFO
					   ,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
					   ,"ativaOrdem"
					   ,"Ped:"+pedido.getNumPedido()+" Ord:"+pedido.getNumOrdem()+" Sta:"+pedido.getStatusVoucher()+". Foi enviado para a ativacao. Job:"+codRetorno
					   );
		}
		else
			poolLog.log(0
					,Definicoes.WARN
					,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
					,"ativaOrdem"
					,"Erro ao ativar a ordem "+pedido.getNumOrdem() +" do "+pedido+". Retorno:"+codRetorno+" - "+mapRet.getDescricao(codRetorno)
			);
	}
	
	/**
	 * Metodo....:recebeOrdem
	 * Descricao.:Este metodo realiza efetivamente a chamada na plataforma
	 *            para receber a ordem. O status do pedido fica como PRINTED
	 *            para indicar o status atual. O numero do job responsavel
	 *            pelo recebimento eh armazenado no cabecalho do pedido
	 * @param opVoucher - Classe que realizara as operacoes de voucher
	 * @param pedido	- Pedido de criacao de ordem de vouchers
	 */
	private void recebeOrdem(OperacoesVoucher opVoucher, VoucherPedido pedido)
	{
		MapTVMCodRetorno mapRet = MapTVMCodRetorno.getInstancia();
		// Realiza o recebimento da ordem e atualiza o status da mesma.
		// O status fica como printed para indicar o status atual.
		int codRetorno = opVoucher.recebeOrdemVoucher(pedido.getNumOrdem());
		if (codRetorno > Definicoes.MIN_COD_RETORNO_JOB_TECNOMEN)
		{
			PedidoDAO.atualizaSituacaoPedido(pedido.getNumPedido(),codRetorno,Definicoes.STATUS_VOUCHER_DISPONIBILIZADO);
			poolLog.log(0
					   ,Definicoes.INFO
					   ,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
					   ,"recebeOrdem"
					   ,"Ped:"+pedido.getNumPedido()+" Ord:"+pedido.getNumOrdem()+" Sta:"+pedido.getStatusVoucher()+". Foi enviado para o recebimento. Job:"+codRetorno
					   );
		}
		else
			poolLog.log(0
					    ,Definicoes.WARN
						,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
						,"recebeOrdem"
						,"Erro ao receber a ordem "+pedido.getNumOrdem() +" do "+pedido+". Retorno:"+codRetorno+" - "+mapRet.getDescricao(codRetorno)
						);	
	}
	
	/**
	 * Metodo....:finalizaOrdem
	 * Descricao.:Este metodo realiza efetivamente a chamada na plataforma
	 *            para finalizar a ordem. O status do pedido fica como RECEIVED
	 *            para indicar o status atual. O numero do job responsavel
	 *            pelo recebimento eh armazenado no cabecalho do pedido
	 * @param opVoucher - Classe que realizara as operacoes de voucher
	 * @param pedido	- Pedido de criacao de ordem de vouchers
	 */
	private void finalizaOrdem(OperacoesVoucher opVoucher, VoucherPedido pedido)
	{
		MapTVMCodRetorno mapRet = MapTVMCodRetorno.getInstancia();
		// Realiza a finalizacao da ordem e atualiza o status da mesma.
		// O status fica como received para indicar o status atual.
		int codRetorno = opVoucher.finalizaOrdemVoucher(pedido.getNumOrdem());
		if (codRetorno > Definicoes.MIN_COD_RETORNO_JOB_TECNOMEN)
		{
			PedidoDAO.atualizaSituacaoPedido(pedido.getNumPedido(),codRetorno,Definicoes.STATUS_VOUCHER_RECEBIDO);
			poolLog.log(0
					   ,Definicoes.INFO
					   ,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
					   ,"finalizaOrdem"
					   ,"Ped:"+pedido.getNumPedido()+" Ord:"+pedido.getNumOrdem()+" Sta:"+pedido.getStatusVoucher()+". Foi enviado para a finalizacao. Job:"+codRetorno
					   );
		}
		else
			poolLog.log(0
				    ,Definicoes.WARN
					,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
					,"finalizaOrdem"
					,"Erro ao finalizar (ISSUED) a ordem "+pedido.getNumOrdem() +" do "+pedido+". Retorno:"+codRetorno+" - "+mapRet.getDescricao(codRetorno)
					);
	}
	
	/**
	 * Metodo....:verificaPendenciaEnvioOrdem
	 * Descricao.:Verifica os pedidos que ainda nao tiveram seus arquivos enviados
	 *            por e-mail para seus respectivos requisitantes. Apos enviar os
	 *            arquivos o pedido possui status indicando tal envio assim como
	 *            a data deste
	 *
	 */
	private void verificaPendenciaEnvioOrdem()
	{
		TransmitePedidoVoucher transPedido = new TransmitePedidoVoucher(idProcesso);
		// Busca a lista de pedidos que estao processados porem ainda nao foram enviados
		// os arquivos criptografados com as numeracoes de voucher por e-mail
		Collection listaPedidos = PedidoDAO.buscaListaPedidos(Definicoes.STATUS_PEDIDO_VOUCHER_PROCESSADO,Definicoes.STATUS_VOUCHER_CONCLUIDO);
		for (Iterator i = listaPedidos.iterator(); i.hasNext();)
		{
			VoucherPedido pedido = (VoucherPedido)i.next();
			// Se a data do envio for nula entao indica que este ainda nao foi processado portanto
			// envia os arquivos da ordem e atualiza o status
			if (pedido.getDataEnvioEmail() == null)
			{
				try
				{
					transPedido.enviaPedidoPorEMail(pedido.getNumOrdem());
					PedidoDAO.atualizaStatusJobPedido(pedido.getNumPedido(),Definicoes.STATUS_PEDIDO_VOUCHER_TRANSMITIDO,null);
				}
				catch(Exception ge)
				{
					poolLog.log(idProcesso,Definicoes.WARN
							    ,"GerenciaPedidosVoucher"
								,"verificaPendenciaEnvioOrdem"
								,"Erro ao enviar "+pedido+" por e-mail. Erro:"+ge); 
				}
			}
		}
	}

	/**
	 * Metodo....:verificarPedidoPlataforma
	 * Descricao.:Verifica se a ordem de cartoes na plataforma Tecnomen possui o status
	 *            correspondente ao status enviado como parametro. Para que a odem esteja
	 *            ok, o statu da ordem, das caixas (boxes) e dos lotes (batches) devem 
	 *            estar ok.
	 * @param pedido		- Pedido do GPP de criacao de vouchers
	 * @param statusVoucher	- Status do voucher no GPP para comparacao com a ordem
	 * @return boolean      - Retorna se a ordem estah ok ou nao na plataforma com o status
	 */
	private boolean verificarPedidoNaPlataforma(VoucherPedido pedido, int statusVoucher)
	{
		boolean ordemOk = true;
		OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
		
		// Realiza a consulta da ordem na plataforma Tecnomen
		// A ordem retornada possui informacoes de caixas (boxes) e
		// lotes (batches). Atraves dessas informacoes o metodo verifica
		// se a ordem possui o status correspondente com o status passado
		// como parametro
		VoucherOrdem ordem = opVoucher.getInformacoesOrdem(pedido.getNumOrdem());
		
		// Pega o valor do status do voucher correspondente na plataforma 
		// e este serah utilizado para a comparacao do status da ordem
		// retornada
		int statusTecnomen = getStatusVoucherTecnomen(statusVoucher);
		
		poolLog.log(0
				,Definicoes.INFO
				,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER
				,"verificarPedidoNaPlataforma"
				,"Ped:"+pedido.getNumPedido()+" Ord:"+pedido.getNumOrdem()+" Sta:"+pedido.getStatusVoucher()+
				" IdTec:"+statusTecnomen+" possui status de ordem na tecnomen no valor: "+ordem.getStatus()
		);
		
		// Realiza a verificacao se as caixas estao vazias. Se existir
		// a ordem porem a caixa estiver vazia entao tambem invalida a ordem.
		// Devido a problemas com a plataforma, o status 14 (fulfiled) na plataforma
		// tambem eh desconsiderado pois se trata de um status intermediario existente
		// na ordem de criacao de cartoes.
		if (ordem.getCaixasOrdem().size() == 0 || 
				(ordem.getStatus() != statusTecnomen && ordem.getStatus() != 14))
			return false;

		// Realiza a iteracao na ordem buscando todas as caixas associadas
		// Para cada caixa faz a iteracao buscando todos os lotes associados
		// e verifica entao para cada caixa e lote sendo processado se este
		// possui o status diferente do statusTecnomen. Se algum destes tiver
		// o status diferente entao a ordem nao estah ok
		for (Iterator i = ordem.getCaixasOrdem().iterator(); i.hasNext(); )
		{
			VoucherOrdemCaixa caixa = (VoucherOrdemCaixa)i.next();
			for (Iterator j = caixa.getLotesCaixa().iterator(); j.hasNext(); )
			{
				VoucherOrdemLote lote = (VoucherOrdemLote)j.next();
				
				// Verifica a situacao do status para o lote (batch) e a caixa (box)
				if (lote.getStatus() != statusTecnomen || caixa.getStatus() != statusTecnomen)
					return false;
			}
		}
		return ordemOk;
	}
	
	/**
	 * Metodo....:getStatusVoucherTecnomen
	 * Descricao.:Retorna o status do voucher na tecnomen correspondente ao do GPP
	 * @param statusVoucherGPP - Status do voucher no GPP
	 * @return status do voucher correspondente na Tecnomen
	 */
	private int getStatusVoucherTecnomen(int statusVoucherGPP)
	{
		Map statusVoucherTecnomen = new HashMap();
		statusVoucherTecnomen.put(new Integer(Definicoes.STATUS_VOUCHER_DISPONIBILIZADO), new Integer(5));
		statusVoucherTecnomen.put(new Integer(Definicoes.STATUS_VOUCHER_RECEBIDO)		, new Integer(6));
		statusVoucherTecnomen.put(new Integer(Definicoes.STATUS_VOUCHER_EMITIDO)		, new Integer(9));
		statusVoucherTecnomen.put(new Integer(Definicoes.STATUS_VOUCHER_ATIVADO)		, new Integer(1));
		
		return ((Integer)statusVoucherTecnomen.get(new Integer(statusVoucherGPP))).intValue();
	}
	
	/**
	 * Metodo....:resubmeterPedidoVoucher
	 * Descricao.:Re-envia o pedido de voucher para a sua continuacao na plataforma Tecnomen
	 *            Este processo irah analisar a situacao atual do pedido e atraves dela definir
	 *            qual acao executar. OBS:Somente pedidos com o status X de erro serao possiveis
	 *            de serem submetidos novamente, isso devido pedidos em andamento ainda estarem
	 *            sendo processados pela plataforma
	 * @param numPedido	- Numero do pedido de criacao de cartoes
	 * @throws GPPInternalErrorException
	 */
	public static void resubmeterPedidoVoucher(long numPedido) throws GPPInternalErrorException
	{
		// Primeiro realiza a pesquisa do Pedido de Cartoes e verifica
		// se este estah com o status X no Job que foi executado por ultimo
		VoucherPedido pedido = PedidoDAO.buscaPedido(numPedido);
		
		if (!pedido.getStatusPedido().equals(Definicoes.STATUS_PEDIDO_VOUCHER_ERRO))
			throw new GPPInternalErrorException("Somente pedidos com erro podem ser resubmetidos para a plataforma");
		
		PREPConexao conexaoPrep = null;
		try
		{
			// Como nem todos os servidores realizam o gerenciamento de vouchers
			// entao atualiza somente a informacao do status do pedido.
			// Como procedimento alternativo, o processo atualiza o numero do job
			// para um numero nao existente, portanto o processo de gerenciamento
			// irah processar de acordo com o status atual.
			PedidoDAO.atualizaStatusJobPedido(pedido.getNumPedido(),Definicoes.STATUS_PEDIDO_VOUCHER_EM_PROCESSO,null);
			
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			PedidoDAO.atualizaNumeroJobPedido(conexaoPrep, idProcesso, Definicoes.MIN_COD_RETORNO_JOB_TECNOMEN, numPedido);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep, idProcesso);
		}
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			MapConfiguracaoGPP confGpp = MapConfiguracaoGPP.getInstancia();
			int tempoEspera = Integer.parseInt(confGpp.getMapValorConfiguracaoGPP("GERENCIA_VOUCHER_TEMPO_ESPERA"));
			Integer.parseInt(confGpp.getMapValorConfiguracaoGPP("GERENCIA_VOUCHER_INTERVALO_PROCESSAMENTO"));
			
			VerificaJobAtivacao verJobAtivacao = new VerificaJobAtivacao(idProcesso);
			// Fica em espera por um perido x de tempo em segundos apos cada verificacao.
			// Cada verificacao busca todos os pedidos em determinadas situacoes e realiza 
			// as acoes devidas.
			while (true)
			{
				poolLog.log(0,Definicoes.INFO,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"run","Verificando situacao de vouchers...");
				// Apos cada verificacao aplica-se um tempo de espera (geralmente bem pequeno 1 ou 2 segundos) 
				// somente para que na proxima verificacao existir grande probabilidade do job previamente
				// iniciado estar pronto
				verificaCriacaoOrdemPedidos();
				verificaImpressaoOrdemPedidos();
				processaPedidosDisponibilizados();
				verificaRecebimentoOrdemPedidos();
				verificaFinalizacaoOrdemPedidos();
				verificaAtivacaoOrdemPedidos();
				verificaPendenciaEnvioOrdem();
				// Verifica a ativacao de vouchers fisicos
				verJobAtivacao.verificaJobs();
				// O proximo tempo de espera agora e para todo o ciclo de processamento
				// este tempo deve ter um valor bem estimado para evitar demoras ou
				// processamento excessivo
				Thread.sleep(tempoEspera*1000);
			}
		}
		catch(InterruptedException ie)
		{
			poolLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"run","Thread de verificacao de Jobs da Tecnomen foi interrompido.");	
		}
		catch(GPPInternalErrorException ge)
		{
			poolLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"run","Nao foi possivel ler mapeamento de configuracao do sistema");			
		}
		catch(Exception e)
		{
			poolLog.log(0,Definicoes.DEBUG,Definicoes.CL_GERENCIA_PEDIDOS_VOUCHER,"run","Excessao ocorrida no processamento. Erro:"+e);
		}
	}
}
