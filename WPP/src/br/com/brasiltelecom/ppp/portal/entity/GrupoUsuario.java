package br.com.brasiltelecom.ppp.portal.entity;

/**
 *  @author Victor Paulo A. de Almeida / Sandro Augusto
 * 
 *  Entidade responsável por obter e definir os dados pertinentes a associação entre um Usuário e Grupo.
 * 
 */

public class GrupoUsuario { 

	/** Identificador do Grupo de usuários. */
	private int id;
	
	/** Matricula do usuário. */
    private String matricula;

	/**
	 * Construtor padrão da classe.
	 */
	public GrupoUsuario() {

	}

	/**
	 * Retorna o Identificador do Grupo do usuário.
	 * @return id Número Identificador do Grupo do usuário.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retorna a matricula do usuário.
	 * @return matricula Matricula do usuário.
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * Retorna o identificador único do Grupo.
	 * @return id Identificador único do Grupo..
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retorna a Matricula do usuário.
	 * @return matricula Matricula do usuário.
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}