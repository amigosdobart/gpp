package com.brt.gpp.aplicacoes.gerenciamentoVouchers.produtorConsumidor;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaCriacaoOrdem;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaSolicitacao;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ImportacaoPedidosConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{

	public ImportacaoPedidosConsumidor()
	{
		super(GerentePoolLog.getInstancia(ImportacaoPedidosConsumidor.class).getIdProcesso(Definicoes.CL_IMPORTACAO_PEDIDOS_VOUCHER), 
				Definicoes.CL_IMPORTACAO_PEDIDOS_VOUCHER);
	}

	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		startup();
	}

	public void startup() throws Exception
	{
		super.log(Definicoes.INFO, "Consumidor.startup", "Inicio da execucao do consumidor" + this);
	}

	public void startup(Produtor produtor) throws Exception
	{
		startup();
	}

	public void execute(Object obj) throws Exception
	{
		//super.log(Definicoes.DEBUG, "Consumidor.execute", "Processando ");
		ProcessaSolicitacao processador = new ProcessaCriacaoOrdem((VoucherPedido) obj, super.getIdLog());
		processador.executarSolicitacao();
	}

	public void finish()
	{
		super.log(Definicoes.INFO, "Consumidor.finish", "Fim da execucao do consumidor" + this);
	}
}