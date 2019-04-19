package com.brt.gpp.comum.conexoes;

import com.brt.gpp.comum.conexoes.DadosConexao;

/**
 *	Classe base para implementacao de conexoes do GPP com outros sistemas, tais como servicos da Tecnomen e Banco de Dados.
 * 
 *	@author		Daniel Ferreira
 *	@since		26/02/2007
 */
public abstract class Conexao 
{

	/**
	 *	Aloca a conexao para o processo requisitante. Caso seja chamado, atualiza os dados da conexao para indicar
	 *	que processo esta utilizando-a e a data de inicio do uso.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 */
	public abstract void alocarConexao(long idProcesso);
	
	/**
	 *	Estabelece a conexao com o servidor.
	 *
	 *	@throws		Exception
	 */
	public abstract void conectar() throws Exception;
	
	/**
	 *	Libera a conexao para o processo requisitante. Caso seja chamado, atualiza os dados da conexao para indicar
	 *	que nenhum processo esta utilizando-a.
	 */
	public abstract void desalocarConexao();
	
	/**
	 *	Fecha a conexao com o servidor.
	 */
	public abstract void fechar();
	
	/**
	 *	Retorna os dados da conexao.
	 *
	 *	@return		Dados da conexao.
	 */
	public abstract DadosConexao getDadosConexao();
	
	/**
	 *	Indica se a conexao encontra-se ativa.
	 *
	 *	@return		True se a conexao encontra-se ativa e false caso contrario.
	 */
	public abstract boolean isAtivo();
	
	/**
	 *	Reestabelece a conexao com o servidor.
	 *
	 *	@throws		Exception
	 */
	public abstract void reconectar() throws Exception;
	
	/**
	 *	Efetua o teste da conexao. Verifica se a conexao esta ativa. Se nao estiver, tenta reconectar ao servidor.
	 *	Lanca excecao caso o servidor nao esteja disponivel.
	 *
	 *	@throws		Exception
	 */
	public abstract void testarConexao() throws Exception;
	
}
