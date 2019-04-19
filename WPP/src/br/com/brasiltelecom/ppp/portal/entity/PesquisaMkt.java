package br.com.brasiltelecom.ppp.portal.entity;

public class PesquisaMkt
{
	private int idPesquisa;
	private String nomePesquisa;
	
	/**
	 * @return the idPesquisa
	 */
	public int getIdPesquisa()
	{
		return idPesquisa;
	}
	
	/**
	 * @param idPesquisa the idPesquisa to set
	 */
	public void setIdPesquisa(int idPesquisa)
	{
		this.idPesquisa = idPesquisa;
	}
	
	/**
	 * @return the nomePesquisa
	 */
	public String getNomePesquisa()
	{
		return nomePesquisa;
	}
	
	/**
	 * @param nomePesquisa the nomePesquisa to set
	 */
	public void setNomePesquisa(String nomePesquisa)
	{
		this.nomePesquisa = nomePesquisa;
	}
}