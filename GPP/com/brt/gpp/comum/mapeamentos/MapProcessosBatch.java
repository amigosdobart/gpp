package com.brt.gpp.comum.mapeamentos;

import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatch;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchDAO;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import java.sql.ResultSet;

public class MapProcessosBatch extends Mapeamento
{
    
	private static MapProcessosBatch 	instancia;
	private ProcessoBatchDAO processoDAO=new ProcessoBatchDAO();
	
	//Caso o processo seja interrompido de forma inesperara 
	//ao subir o GPP novamente a base é limpa para não interromper 
	//a chamada de um processo batch.
	private MapProcessosBatch() throws GPPInternalErrorException
	{
	    super();
	    processoDAO.limparBase(0);
	}
	
	/**
	 * Metodo....:getInstancia
	 * Descricao.:Retorna a instancia do mapeamento
	 * @return MapProcessosBatch
	 */
	public static MapProcessosBatch getInstancia() throws GPPInternalErrorException
	{
		if (instancia == null)
			instancia = new MapProcessosBatch();
		
		return instancia;
	}

	/**
	 * Metodo....:getProcessoBatch
	 * Descricao.:Retorna o objeto processo batch considerando o id desejado
	 * @param idProcessoBatch - Id do processo a ser retornado
	 * @return ProcessoBatch - Objeto do processo batch correspondente
	 */
	public ProcessoBatch getProcessoBatch(int idProcessoBatch)
	{
		return (ProcessoBatch)super.get(new Object[]{new Integer(idProcessoBatch)});
	}

	/**
	 * Metodo....:load
	 * Descricao.:Este metodo popula o hashmap com os valores existentes em tabela para os processos batch
	 *
	 */
	public void load() throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
			String sql = "select id_processo_batch " +
			                   ",nom_processo_batch " +
							   ",idt_classe_produtor " +
							   ",idt_classe_consumidor " +
							   ",num_threads " +
							   ",ind_execucao_paralelo "+
							   ",idt_servidor_gpp "+
						   "from tbl_ger_processos_batch";
			ResultSet rs = conexaoPrep.executaQuery(sql,0);
			while (rs.next())
			{
				// Cria uma referencia do objeto Processobatch para armazenar
				// as informacoes existentes na tabela sendo o getXXX do ResultSet
				// sendo feito na ordem que retorna o comando acima
				ProcessoBatch procBatch = new ProcessoBatch(rs.getInt(1));
				procBatch.setNomeProcessoBatch	(rs.getString(2));
				procBatch.setClasseProdutor		(rs.getString(3));
				procBatch.setClasseConsumidor	(rs.getString(4));
				procBatch.setNumeroThreads		(rs.getInt	 (5));
				procBatch.setExecucaoParalelo(rs.getInt(6) == 1 ? true : false);
				procBatch.setServidorGPP(rs.getString(7));
				
				
				// Adciona o objeto de processo batch no pool 
				// gerenciado por este mapeamento utilizando
				// o id do processo como sendo chave do map
				super.values.put(new Integer(procBatch.getIdProcessoBatch()),procBatch);
			}
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Erro ao pesquisar processos batch. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep,0);
		}
	}
}
