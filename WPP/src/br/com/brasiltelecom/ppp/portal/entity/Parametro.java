package br.com.brasiltelecom.ppp.portal.entity;


/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade respons�vel pela obten��o e defini��o dos Par�metros de Seguran�a da Aplica��o.
 * 
 * <ul> 
 * 		<li>Define o tamanho m�ximo da senha do usu�rio.</li>
 * 		<li>Define a tempo de validade de uma senha.</li>
 *      <li>Define o hist�rico de senhas dos usu�rios.</li>
 *      <li>N�mero de Tentativas de login do usu�rio, ap�s exceder esse valor  o usu�rio ser� bloqueado.</li>
 * </ul> 
 */

public class Parametro  {

	/** R�tulo Identificador dos par�metros de seguran�a.*/
	private String id;
	
	/** Descri��o do par�metro.*/
	private String descricao;
	
	/**Valor para o par�metro. */
	private String valor;
	
	
	/**
    * M�todo respons�vel por obter a Descri��o do Par�metro.
	* @return descricao String com a descri��o do Par�metro.
	*/
	public String getDescricao() {
		return descricao;
	}

	/**
    * M�todo respons�vel por obter o r�tulo identificador do Par�metro.
	* @return id String com o r�tulo identificador do Par�metro.
	*/
	public String getId() {
		return id;
	}

	/**
    * M�todo respons�vel por obter o Valor para o Par�metro.
	* @return valor String com o Valor para o Par�metro.
	*/
	public String getValor() {
		return valor;
	}

  /**
   * M�todo respons�vel por definir a Descri��o do Par�metro.
   * @param descricao String com a Descri��o do Par�metro.
   */
  	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

  /**
   * M�todo respons�vel por definir o r�tulo identificador do Par�metro.
   * @param id String com o r�tulo identificador do Par�metro.
   */
	public void setId(String id) {
		this.id = id;
	}

  /**
   * M�todo respons�vel por definir o Valor para o par�metro.
   * @param valor String com o Valor para o par�metro.
   */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	/**
    * M�todo respons�vel por obter o Valor para o Par�metro como inteiro.
	* @return valor Inteiro com o Valor para o Par�metro.
	*/

	public int getValorInt(){
		return Integer.parseInt(valor);	
	}

}
