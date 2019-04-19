/*
 * Created on 08/04/2004
 *
 */
package br.com.brasiltelecom.ppp.model;

/**
 * Modela as informações de evento
 * @author Alex Pitacci Simões
 * @since 21/05/2004
 */
public class Evento {

	private String nome;
	private String data;
	private String hora;
	
	
	/**
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * @return
	 */
	public String getHora() {
		return hora;
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
	public void setData(String string) {
		data = string;
	}

	/**
	 * @param string
	 */
	public void setHora(String string) {
		hora = string;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}

}
