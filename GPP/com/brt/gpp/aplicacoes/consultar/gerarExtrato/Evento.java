//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.gerarExtrato;

public class Evento {

	// Atributos da Classe
	private String descricao;
	private String data;
	private String hora;
	private String numeroLinha;
	
	/**
	 * Metodo...: Detalhe
	 * Descricao: Construtor 
	 * @param  descricao	- Descricao	do demonstrativo de eventos
	 * @param  data			- Data do demonstrativo de eventos (formatacao dd/mm/aaaa)
	 * @param  hora			- Hora do demonstrativo de eventos (hh:mm:ss)
	 * @return	
	 */
	public Evento(String numeroLinha, String descricao,String data,String hora)
	{
		this.numeroLinha=numeroLinha;
		this.descricao=descricao;
		this.data = data;
		this.hora = hora;
	}

	// Metodos Get
	/**
	 * Metodo...: getData
	 * Descricao: Retorna a data do demonstrativo de eventos
	 * @param
	 * @return String	- Data do demonstrativo de eventos (formatacao dd/mm/aaaa)
	 */
	public String getData() {
		return data;
	}

	/**
	 * Metodo...: getDescricao
	 * Descricao: Retorna a descricao do demonstrativo de eventos
	 * @param
	 * @return String	- Descricao do demonstrativo de eventos 
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Metodo...: getHora
	 * Descricao: Retorna a hora do demonstrativo de eventos
	 * @param
	 * @return String	- Hora do demonstrativo de eventos (formatacao hh:mm:ss)
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Metodo...: getNumeroLinha
	 * Descricao: Retorna o numero da linha do demonstrativo de eventos
	 * @param
	 * @return String	- Numero da linha do demonstrativo de eventos
	 */
	public String getNumeroLinha() {
		return numeroLinha;
	}

	// Metodos Set
	/**
	 * Metodo...: setData
	 * Descricao: Atribui a data do demonstrativo de eventos
	 * @param  string	- Data do demonstrativo de eventos (formato dd/mm/aaaa)
	 * @return
	 */
	public void setData(String string) {
		data = string;
	}

	/**
	 * Metodo...: setDescricao
	 * Descricao: Atribui a descricao do demonstrativo de eventos
	 * @param  string	- Descricao do demonstrativo de eventos 
	 * @return
	 */
	public void setDescricao(String string) {
		descricao = string;
	}

	/**
	 * Metodo...: setHora
	 * Descricao: Atribui a hora do demonstrativo de eventos
	 * @param  string	- Hora do demonstrativo de eventos (formato hh:mm:ss)
	 * @return
	 */
	public void setHora(String string) {
		hora = string;
	}

	/**
	 * Metodo...: setNumeroLinha
	 * Descricao: Atribui o numero da linha do demonstrativo de eventos
	 * @param  string	- Numero da linha do demonstrativo de eventos 
	 * @return
	 */
	public void setNumeroLinha(String string) {
		numeroLinha = string;
	}

}
