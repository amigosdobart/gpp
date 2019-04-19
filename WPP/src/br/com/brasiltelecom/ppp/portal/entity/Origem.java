/*
 * Created on 24/03/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de origem
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Origem {
	private String 	idOrigem;
	private String 	descOrigem;
	private int 	modificarDataExp;
	private String 	idCanal;
	private Canal 	canal;
	private int 	numDiasExpiracao;
	private int		ativo;
	private char	tipoLancamento;
	private int		disponivel;
	
	// Métodos get
	
	/**
	 * @return
	 */
	public String getDescOrigem() {
		return descOrigem;
	}

	/**
	 * @return
	 */
	public String getIdOrigem() {
		return idOrigem;
	}

	/**
	 * @return
	 */
	public String getIdCanal() {
		return idCanal;
	}

	/**
	 * @return
	 */
	public Canal getCanal() {
		return canal;
	}

	/**
	 * @return
	 */
	public int getModificarDataExp() {
		return modificarDataExp;
	}

	/**
	 * @return
	 */
	public int getNumDiasExpiracao() {
		return numDiasExpiracao;
	}

	/**
	 * @return
	 */
	public int getAtivo() {
		return ativo;
	}

	/**
	 * @return
	 */
	public char getTipoLancamento() {
		return this.tipoLancamento;
	}
	
	/**
	 * @return
	 */
	public int getDisponivel() {
		return this.disponivel;
	}

	// Métodos set
	
	/**
	 * @param string
	 */
	public void setDescOrigem(String string) {
		descOrigem = string;
	}

	/**
	 * @param string
	 */
	public void setIdOrigem(String string) {
		idOrigem = string;
	}
	
	/**
	 * @param string
	 */
	public void setIdCanal(String string) {
		idCanal = string;
	}

	/**
	 * @param canal
	 */
	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	/**
	 * @param i
	 */
	public void setModificarDataExp(int i) {
		modificarDataExp = i;
	}

	/**
	 * @param i
	 */
	public void setNumDiasExpiracao(int i) {
		numDiasExpiracao = i;
	}

	/**
	 * @param i
	 */
	public void setAtivo(int i) {
		ativo = i;
	}
	
	/**
	 * @param i
	 */
	public void setTipoLancamento(char tipo) 
	{
		this.tipoLancamento = tipo;
	}
	
	/**
	 * @param i
	 */
	public void setDisponivel(int disponivel) 
	{
		this.disponivel = disponivel;
	}	

}
