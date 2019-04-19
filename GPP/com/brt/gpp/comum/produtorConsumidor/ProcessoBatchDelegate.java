package com.brt.gpp.comum.produtorConsumidor;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapProcessosBatch;

/**
 * @author Joao Carlos
 * Data..: 19/08/2005
 * Modificado por: Danilo Alves Araujo	
 * Data:26/05/2006
 * Razao: Evitar execução múltipla de um processo batch
 */
public class ProcessoBatchDelegate
{
	private long idProcesso;
	ProcessoBatchDAO processoDAO=new ProcessoBatchDAO();
	public ProcessoBatchDelegate(long idProcesso)
	{
		this.idProcesso = idProcesso;
	}
	
	/**
	 * Metodo....:executaProcessoBatch
	 * Descricao.:Este metodo eh responsavel pela delegacao de execucao do processo batch
	 *            Este identifica qual o processo batch deve ser executado e atraves das
	 *            informacoes cadastradas chama a classe que devera executar o produtor
	 *            desse processo. Previne a múltipla chamada de um processo batch.
	 * @param idProcessoBatch	- Id do processo batch
	 * @param params			- parametros a serem repassados
	 * @throws Exception
	 */
	public void executaProcessoBatch(int idProcessoBatch, String params[]) throws Exception
	{
		// Busca a instancia de gerenciamento do mapeamento de 
		// processos batch em memoria e busca o objeto correspondente
		// ao id desejado
		MapProcessosBatch map = MapProcessosBatch.getInstancia();
		ProcessoBatch procBatch = map.getProcessoBatch(idProcessoBatch);
		// Cria uma instancia do delegate produtor consumidor GPP responsavel
		// pela execucao dos processos batch e executa o metodo passando como
		// parametro as informacoes retornadas do objeto processos batch configurado
		// em memoria no mapeamento.
		// Verifica se o processo pode ser executado em paralelo ou se não existe 
		// nenhum processo em execução.
		if (procBatch.getExecucaoParalelo() || processoDAO.atualizarServidor(procBatch,idProcesso))
		{
			try
			{
				ProdutorConsumidorDelegateGPP delegateGPP = new ProdutorConsumidorDelegateGPP(idProcesso);
				delegateGPP.exec(procBatch.getNumeroThreads(),procBatch.getProdutor(idProcesso),params,procBatch.getClasseConsumidor());
			}
			finally
			{		
				processoDAO.liberarBase(procBatch,idProcesso);
			}
		}
		else
			throw new GPPInternalErrorException("Processo batch ja esta em execucao no momento.");
			
	}
	
	
}
