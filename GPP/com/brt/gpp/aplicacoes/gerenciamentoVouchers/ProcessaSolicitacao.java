package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

public interface ProcessaSolicitacao
{
	/**
	 * Metodo....: executarSolicitacao
	 * Descricao.: metodo que inicia o tratamento de uma solicitacao de voucher,
	 * seja de ativacao ou cancelamento. 
	 * @throws Exception
	 */
	public void executarSolicitacao() throws Exception;
}