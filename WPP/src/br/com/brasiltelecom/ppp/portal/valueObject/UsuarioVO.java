package br.com.brasiltelecom.ppp.portal.valueObject;

/**
 * Modela a tabela de usuario tbl_ppp_usuario
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class UsuarioVO {
	private String matricula;
	private String nome;
	private String departamento;
	private String email;
	private String cargo;



	/**
	 * Returns the cargo.
	 * @return String
	 */


	/**
	 * @return
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @return
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @return
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param string
	 */
	public void setDepartamento(String string) {
		departamento = string;
	}

	/**
	 * @param string
	 */
	public void setMatricula(String string) {
		matricula = string;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}

	/**
	 * @return
	 */
	public String getCargo() {
		return cargo;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param string
	 */
	public void setCargo(String string) {
		cargo = string;
	}

	/**
	 * @param string
	 */
	public void setEmail(String string) {
		email = string;
	}

}
