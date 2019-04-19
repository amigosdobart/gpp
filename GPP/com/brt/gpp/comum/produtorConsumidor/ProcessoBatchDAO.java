package com.brt.gpp.comum.produtorConsumidor;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
/**
*
* Este arquivo refere-se a classe ProcessoBatchDAO responsável por evitar
* execuçoes multiplas de um processo batch. 
*
* <P> Versao:			1.0
*
* @Autor: 			Danilo Alves Araujo	
* Data: 				25/05/2006
*
* Modificado por:
* Data:
* Razao:
*
*/
public class ProcessoBatchDAO
{
	private ArquivoConfiguracaoGPP configuracao;
	protected GerentePoolLog log; // Gerente de LOG
	
	/**
	 * Metodo...: ProcessoBatchDAO
	 * Descricao: Construtor 
	 * @param	
	 * @return	
	 */
	public ProcessoBatchDAO()
	{
		configuracao=ArquivoConfiguracaoGPP.getInstance();
		log=null;
	}
	
	/**
	 * Metodo...: gerarID
	 * Descricao: Liga o nome do servidor ao da porta
	 * @param	servidor - Nome do servidor atual
	 * @param	porta	 - Numero da porta atual 
	 * @return	retorna o nome do servidor e o da porta unidos por ":" 
	 */
	public String gerarID(String servidor, String porta)
	{			
		return servidor+":"+porta;
	}
	
	/**
	 * Metodo...: atualizarServidor
	 * Descricao: Atualiza a base caso nenhum processo esteja rodando 
	 * @param	procBatch	- Identifica o Processo Batch
	 * @param	idProcesso	- Identificador do processo 
	 * @return	true caso a tabela seja atualizada
	 */
	public boolean atualizarServidor(ProcessoBatch procBatch,long idProcesso) throws Exception
	{
		String idServidorPorta=gerarID(configuracao.getEnderecoOrbGPP(),configuracao.getPortaOrbGPP());
		int retornoUpdate;
		PREPConexao conexaoPrep = null;
		try
		{								
			conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
			String sql ="UPDATE tbl_ger_processos_batch "
						+"SET idt_servidor_gpp=? "
						+"WHERE id_processo_batch=? AND idt_servidor_gpp is null"; 
						
			Object[] params1= {idServidorPorta, new Integer(procBatch.getIdProcessoBatch())};
			retornoUpdate = conexaoPrep.executaPreparedUpdate(sql,params1,idProcesso);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep,0);
		}
		return (retornoUpdate>0?true:false);
	}
	
	/**
	 * Metodo...: liberarBase
	 * Descricao: Libera a base apos o processo batch rodar 
	 * @param	procBatch	- Identifica o Processo Batch
	 * @param	idProcesso	- Identificador do processo 
	 * @return	
	 */
	public void liberarBase(ProcessoBatch procBatch,long idProcesso)
	{
		PREPConexao conexaoPrep = null;
		try
		{								
			conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
			String sql ="UPDATE tbl_ger_processos_batch "
						+"SET idt_servidor_gpp=? "
						+"WHERE id_processo_batch=? AND idt_servidor_gpp is not null"; 
						
			Object[] params1= {null ,new Integer(procBatch.getIdProcessoBatch())};
			conexaoPrep.executaPreparedUpdate(sql,params1,idProcesso);
			
		}
		catch(Exception e)
		{			
			log.log(0,Definicoes.ERRO,"ProcessoBatchDAO","liberarBase","Erro ao liberar Base.");
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep,0);
		}
	}
	
	/**
	 * Metodo...: limparBase
	 * Descricao: Limpa a base ao iniciar o GPP
	 * @param	idProcesso		- Identificador do processo
	 * @return	
	 */
	public void limparBase(long idProcesso)
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
			String sql ="UPDATE tbl_ger_processos_batch "
							+"SET idt_servidor_gpp=? "
							+"WHERE idt_servidor_gpp=? "; 
							
			Object[] params1= {null ,gerarID(configuracao.getEnderecoOrbGPP(),configuracao.getPortaOrbGPP())};
			conexaoPrep.executaPreparedUpdate(sql,params1,idProcesso);
						
		}
		catch(Exception e)
		{
			log.log(0,Definicoes.ERRO,"ProcessoBatchDAO","limparBase","Erro ao limpar Base.");
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep,0);
		}	
	}
}
