package com.brt.gpp.comum.produtorConsumidor;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 * @author Joao Carlos
 *
 */
public interface ProcessoBatchProdutor extends Produtor
{
	/**
	 * Metodo....:getIdProcessoBatch
	 * Descricao.:Retorna a identificacao do processo batch
	 * @return int - Id do processo batch
	 */
	public int getIdProcessoBatch();
	
	/**
	 * Metodo....:getDescricaoProcesso
	 * Descricao.:Retorna a mensagem que sera utilizada para o historico
	 *            de execucao do processo batch
	 * @return String - Mensagem de retorno do processo
	 */
	public String getDescricaoProcesso();
	
	/**
	 * Metodo....:getStatusProcesso
	 * Descricao.:Retorna o status de execucao do processamento batch
	 * @return String - Status do processo (Erro ou Falha)
	 */
	public String getStatusProcesso();
	
	/**
	 * Metodo....:setStatusProcesso
	 * Descricao.:Define o status do produtor (Somente na excecao de consumidores)
	 *
	 */
	public void setStatusProcesso(String status);
	
	/**
	 * Metodo....:getDataProcessamento
	 * Descricao.:Retorna a data de Processamento a ser logada na 
	 * 			tbl_ger_historico_proc_batch (dd/mm/yyyy)
	 */
	public String getDataProcessamento();
		
	/**
	 * Metodo....:getConexao
	 * Descricao.:Este metodo eh responsavel por compartilhar a conexao 
	 * de DB obtida pelo Produtor com os respectivos Consumidores.
	 * @return
	 */
	public PREPConexao getConexao();
}
