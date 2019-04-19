package br.com.brasiltelecom.ppp.portal.entity;


/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade responsável pela obtenção e definição dos Parâmetros de Segurança da Aplicação.
 * 
 * <ul> 
 * 		<li>Define o tamanho máximo da senha do usuário.</li>
 * 		<li>Define a tempo de validade de uma senha.</li>
 *      <li>Define o histórico de senhas dos usuários.</li>
 *      <li>Número de Tentativas de login do usuário, após exceder esse valor  o usuário será bloqueado.</li>
 * </ul> 
 */

public class Parametro  {

	/** Rótulo Identificador dos parâmetros de segurança.*/
	private String id;
	
	/** Descrição do parâmetro.*/
	private String descricao;
	
	/**Valor para o parâmetro. */
	private String valor;
	
	
	/**
    * Método responsável por obter a Descrição do Parâmetro.
	* @return descricao String com a descrição do Parâmetro.
	*/
	public String getDescricao() {
		return descricao;
	}

	/**
    * Método responsável por obter o rótulo identificador do Parâmetro.
	* @return id String com o rótulo identificador do Parâmetro.
	*/
	public String getId() {
		return id;
	}

	/**
    * Método responsável por obter o Valor para o Parâmetro.
	* @return valor String com o Valor para o Parâmetro.
	*/
	public String getValor() {
		return valor;
	}

  /**
   * Método responsável por definir a Descrição do Parâmetro.
   * @param descricao String com a Descrição do Parâmetro.
   */
  	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

  /**
   * Método responsável por definir o rótulo identificador do Parâmetro.
   * @param id String com o rótulo identificador do Parâmetro.
   */
	public void setId(String id) {
		this.id = id;
	}

  /**
   * Método responsável por definir o Valor para o parâmetro.
   * @param valor String com o Valor para o parâmetro.
   */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	/**
    * Método responsável por obter o Valor para o Parâmetro como inteiro.
	* @return valor Inteiro com o Valor para o Parâmetro.
	*/

	public int getValorInt(){
		return Integer.parseInt(valor);	
	}

}
