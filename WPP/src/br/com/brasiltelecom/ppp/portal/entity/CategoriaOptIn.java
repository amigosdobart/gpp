package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela as informações de Opt-in Categoria das Tabelas de Hsid
 * @author Geraldo Palmeira
 * @since 05/09/2006
 */
public class CategoriaOptIn 
{

	
	private int    idCategoria;
	private String nomeCategoria;
	private int    idCategoriaPai;
	
	
	/**
	 * @return Retorna o idCategoria.
	 */
	public int getIdCategoria() {
		return idCategoria;
	}
	/**
	 * @param idCategoria idCategoria
	 */
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	/**
	 * @return Retorna o idCategoriaPai.
	 */
	public int getIdCategoriaPai() {
		return idCategoriaPai;
	}
	/**
	 * @param idCategoriaPai idCategoriaPai
	 */
	public void setIdCategoriaPai(int idCategoriaPai) {
		this.idCategoriaPai = idCategoriaPai;
	}
	/**
	 * @return Retorna o nomeCategoria.
	 */
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	/**
	 * @param nomeCategoria nomeCategoria
	 */
	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
}
