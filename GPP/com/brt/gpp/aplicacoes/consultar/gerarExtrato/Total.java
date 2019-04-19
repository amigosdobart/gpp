//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.gerarExtrato;

public class Total {

	// Dados referentes aos Totais
	private double saldoInicialPeriodo;
	private double totalDebitos;
	private double totalCreditos;
	private double saldoFinalPeriodo;
	
	/**
	 * Metodo...: Detalhe
	 * Descricao: Construtor 
	 * @param  
	 * @return	
	 */
	public Total()
	{
		// Somente para indicar um objeto vazio
	}
	
	/**
	 * Metodo...: Detalhe
	 * Descricao: Construtor 
	 * @param saldoInicialPeriodo	- Saldo inicial do assinante
	 * @param totalDebitos			- Total de debitos do assinante
	 * @param totalCreditos			- Total de creditos do assinante
	 * @param saldoFinalPeriodo		- Saldo final do assinante
	 * @return	
	 */
	public Total(	double saldoInicialPeriodo,double totalDebitos,double totalCreditos,
					double saldoFinalPeriodo)
	{
		this.saldoInicialPeriodo = saldoInicialPeriodo;
		this.totalDebitos = totalDebitos;
		this.totalCreditos = totalCreditos;
		this.saldoFinalPeriodo = saldoFinalPeriodo;
	}

	// Metodos Get
	/**
	 * Metodo...: getSaldoFinalPeriodo
	 * Descricao: Retorna o saldo final do assinante
	 * @param
	 * @return double	- Saldo final do assinante
	 */
	public double getSaldoFinalPeriodo() {
		return saldoFinalPeriodo;
	}

	/**
	 * Metodo...: getSaldoInicialPeriodo
	 * Descricao: Retorna o saldo inicial do assinante
	 * @param
	 * @return double	- Saldo inicial do assinante
	 */
	public double getSaldoInicialPeriodo() {
		return saldoInicialPeriodo;
	}

	/**
	 * Metodo...: getTotalCreditos
	 * Descricao: Retorna o total de creditos do assinante
	 * @param
	 * @return double	- Total de creditos do assinante
	 */
	public double getTotalCreditos() {
		return totalCreditos;
	}

	/**
	 * Metodo...: getTotalDebitos
	 * Descricao: Retorna o total de debitos do assinante
	 * @param
	 * @return double	- Total de debitos do assinante
	 */
	public double getTotalDebitos() {
		return totalDebitos;
	}

	// Metodos Set
	/**
	 * Metodo...: setSaldoFinalPeriodo
	 * Descricao: Atribui o saldo final ao assinante
	 * @param  d  - Saldo final do assinante
	 * @return
	 */
	public void setSaldoFinalPeriodo(double d) {
		saldoFinalPeriodo = d;
	}

	/**
	 * Metodo...: setSaldoInicialPeriodo
	 * Descricao: Atribui o saldo inicial ao assinante
	 * @param  d  - Saldo inicial do assinante
	 * @return
	 */
	public void setSaldoInicialPeriodo(double d) {
		saldoInicialPeriodo = d;
	}

	/**
	 * Metodo...: setTotalCreditos
	 * Descricao: Atribui o total de creditos ao assinante
	 * @param  d  - Total de creditos do assinante
	 * @return
	 */
	public void setTotalCreditos(double d) {
		totalCreditos = d;
	}

	/**
	 * Metodo...: setTotalDebitos
	 * Descricao: Atribui o total de debitos ao assinante
	 * @param  d  - Total de debitos do assinante
	 * @return
	 */
	public void setTotalDebitos(double d) {
		totalDebitos = d;
	}

}
