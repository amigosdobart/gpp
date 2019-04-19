package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;

/**
 * Esta classe armazena as propriedades de um Job da plataforma
 * Tecnomen. Esse job e consultado pela Api de voucher management 
 * para identificar o status atual de uma requisicao de pedido de
 * voucher.
 * 
 * @author Joao Carlos
 * Data..: 24-Janeiro-2005
 *
 */
public class JobTecnomen
{
	private int		numeroJob;
	private long	workTotal;
	private long	workDone;
	private long	opState;
	
	public JobTecnomen(int numeroJob)
	{
		this.numeroJob = numeroJob;
		this.opState   = -1;
	}

	/**
	 * Metodo....:getStatusJob
	 * Descricao.:Retorna a descricao do status do Job
	 * @return String - Descricao do status
	 */
	public String getStatusJob()
	{
		MapTVMCodRetorno map = MapTVMCodRetorno.getInstancia();
		return map.getDescricao((int)getOpState());
	}

	/**
	 * Metodo....:percentualPronto
	 * Descricao.:Retorna o percentual do trabalho pronto em relacao ao total
	 *            a ser realizado
	 * @return int - Percentual de processo pronto
	 */
	public int percentualPronto()
	{
		return (int)(workTotal/workDone)*100;
	}

	/**
	 * Metodo....:jobTerminadoSucesso
	 * Descricao.:Retorna se o job esta terminado ou seja.. 100 da tarefa foi concluida
	 *            e se seu status e igual a sucesso
	 * @return boolean - Indica se o job terminou ou nao com sucesso
	 */
	public boolean jobTerminadoSucesso()
	{
		if (getOpState() == Definicoes.RET_OPERACAO_OK)
			return (getWorkTotal() == getWorkDone());
		return false;
	}

	/**
	 * Metodo....:jobTerminadoErro
	 * Descricao.:Retorna se o job esta terminado ou seja.. 100 da tarefa foi concluida
	 *            e se seu status e diferente de sucesso ou em processamento
	 * @return boolean - Indica se o job terminou ou nao com erro
	 */
	public boolean jobTerminadoErro()
	{
		if (getOpState() > Definicoes.MAX_COD_RETORNO_JOB_OK)
			return true;
		return false;
	}

	/**
	 * @return Returns the opState.
	 */
	public long getOpState()
	{
		return opState;
	}

	/**
	 * @param opState The opState to set.
	 */
	public void setOpState(long opState)
	{
		this.opState = opState;
	}
	
	/**
	 * @return Returns the workDone.
	 */
	public long getWorkDone()
	{
		return workDone;
	}
	
	/**
	 * @param workDone The workDone to set.
	 */
	public void setWorkDone(long workDone)
	{
		this.workDone = workDone;
	}
	
	/**
	 * @return Returns the workTotal.
	 */
	public long getWorkTotal()
	{
		return workTotal;
	}
	
	/**
	 * @param workTotal The workTotal to set.
	 */
	public void setWorkTotal(long workTotal)
	{
		this.workTotal = workTotal;
	}
	
	/**
	 * @return Returns the numeroJob.
	 */
	public int getNumeroJob()
	{
		return numeroJob;
	}
}
