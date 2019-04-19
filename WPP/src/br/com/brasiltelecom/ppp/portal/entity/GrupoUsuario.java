package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade respons�vel por obter e definir os dados pertinentes a associa��o entre um Usu�rio e Grupo.
 * 
 */

public class GrupoUsuario { 

	/** Identificador do Grupo de usu�rios. */
	private int id;
	
	/** Matricula do usu�rio. */
    private String matricula;

	/**
	 * Construtor padr�o da classe.
	 */
	public GrupoUsuario() {

	}

	/**
	 * Retorna o Identificador do Grupo do usu�rio.
	 * @return id N�mero Identificador do Grupo do usu�rio.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retorna a matricula do usu�rio.
	 * @return matricula Matricula do usu�rio.
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * Retorna o identificador �nico do Grupo.
	 * @return id Identificador �nico do Grupo..
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retorna a Matricula do usu�rio.
	 * @return matricula Matricula do usu�rio.
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}