package br.com.brasiltelecom.ppp.portal.entity;


public class TipoEvento{
	private String tipoOperacao;
	private String descricao;	
	
	/**
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return
	 */
	public String getTipoOperacao() {
		return tipoOperacao;
	}

	/**
	 * @param string
	 */
	public void setDescricao(String string) {
		descricao = string;
	}

	/**
	 * @param string
	 */
	public void setTipoOperacao(String string) {
		tipoOperacao = string;
	}

}