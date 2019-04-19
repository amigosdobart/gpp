package com.brt.gpp.aplicacoes.gerenciamentoVouchers.produtorConsumidor;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaAtivacaoVoucher;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaCancelamentoVoucher;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.ProcessaSolicitacao;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.SolicitacaoAtivacao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class TratamentoVoucherConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	
	public TratamentoVoucherConsumidor()
	{
		super(GerentePoolLog.getInstancia(TratamentoVoucherConsumidor.class).getIdProcesso(Definicoes.CL_ATIVACAO_VOUCHER_FISICO), 
				Definicoes.CL_ATIVACAO_VOUCHER_FISICO);
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
		SolicitacaoAtivacao solicitacao = (SolicitacaoAtivacao) obj;
		super.log(Definicoes.DEBUG, "Consumidor.execute", "Processando solicitacao " + solicitacao.getIdRequisicao());
		ProcessaSolicitacao processador = null;
		switch(solicitacao.getCod_Ativacao())
		{
			case(Definicoes.IND_ATIVAR_VOUCHER):
			{
				processador = new ProcessaAtivacaoVoucher(solicitacao,super.getIdLog());
				break;
			}
			case(Definicoes.IND_DESATIVAR_VOUCHER):
			{
				processador = new ProcessaCancelamentoVoucher(solicitacao,super.getIdLog());
				break;
			}
		}
		processador.executarSolicitacao();
	}

	public void finish()
	{
		super.log(Definicoes.INFO, "Consumidor.finish", "Fim da execucao do consumidor" + this);
	}
}
