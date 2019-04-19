package br.com.brasiltelecom.ppp.portal.valueObject;

/**
 * Modela a tabela de grupo tbl_ppp_grupo
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public class GrupoVO {
	private int id;
	private String nome;
	private int ativo;
	/**
	 * Returns the id.
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the nome.
	 * @return String
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the nome.
	 * @param nome The nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return
	 */
	public int getAtivo() {
		return ativo;
	}

	/**
	 * @param i
	 */
	public void setAtivo(int i) {
		ativo = i;
	}

}
