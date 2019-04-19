/*
 * Created on 01/04/2004
 *
 * Classe que mapeia as informa��es de extrato de contesta��o
 */
package br.com.brasiltelecom.ppp.model;

import java.util.Date;

/**
 * Dados de extrato de contesta��o
 * @author Andr� Gon�alves
 * @since 21/05/2004
 */
public class ExtratoContestacao extends Extrato {
	
	/**
	 * N�mero do boletim de sindic�ncia associado ao extrato, caso exista
	 * sindic�ncia aberta para o mesmo
	 */	
	private String boletimSindicancia;
	
	/**
	 * Status do boletim de indic�ncia (Procedente, Improcedente ou Aberto)
	 */
	private String statusBoletimSindicancia;
	
	/**
	 * Resultado do BS
	 */
	private String resultadoBs;
	
	/**
	 * Parecer da �rea de Sindic�ncia sobre o registro do Boletim de Sindic�ncia
	 */
	private String parecerSindicancia;
		
	/**
	 * Descri��o do Item de Chamada ou de Cobran�a/Recarga
	 */
	private String descricao;
	
	/**
	 * N�mero da Matr�cula do Atendente que efetuou a abertura da contesta��o
	 */
	private String matricula;
	
	/**
	 * Data de Abertura da Contesta��o
	 */
	private Date dataAbertura;
	
	/**
	 * Data de Fechamento da Contesta��o
	 */
	private Date dataFechamento;
	
	/**
	 * Valor Total do BS
	 */
	private double valorBs;
	
	/**
	 * Valor Ajustado Resultante da Contesta��o
	 */
	private double valorAjustado;
	
	/**
	 * Status da BS (Aberto ou Fechado)
	 */
	private String statusBs; 
	
	/**
	 * @return
	 */
	public String getBoletimSindicancia() {
		return boletimSindicancia;
	}

	/**
	 * @return
	 */
	public Date getDataAbertura() {
		return dataAbertura;
	}

	/**
	 * @return
	 */
	public Date getDataFechamento() {
		return dataFechamento;
	}

	/**
	 * @return
	 */
	public String getDescricao() {
		return descricao;
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
	public String getParecerSindicancia() {
		return parecerSindicancia;
	}

	/**
	 * @return
	 */
	public String getStatusBoletimSindicancia() {
		return statusBoletimSindicancia;
	}

	/**
	 * @return
	 */
	public double getValorAjustado() {
		return valorAjustado;
	}

	/**
	 * @return
	 */
	public double getValorBs() {
		return valorBs;
	}

	/**
	 * @param string
	 */
	public void setBoletimSindicancia(String string) {
		boletimSindicancia = string;
	}

	/**
	 * @param date
	 */
	public void setDataAbertura(Date date) {
		dataAbertura = date;
	}

	/**
	 * @param date
	 */
	public void setDataFechamento(Date date) {
		dataFechamento = date;
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
	public void setMatricula(String string) {
		matricula = string;
	}

	/**
	 * @param string
	 */
	public void setParecerSindicancia(String string) {
		parecerSindicancia = string;
	}

	/**
	 * @param string
	 */
	public void setStatusBoletimSindicancia(String string) {
		statusBoletimSindicancia = string;
	}

	/**
	 * @param d
	 */
	public void setValorAjustado(double d) {
		valorAjustado = d;
	}

	/**
	 * @param d
	 */
	public void setValorBs(double d) {
		valorBs = d;
	}

	/**
	 * @return
	 */
	public String getResultadoBs() {
		return resultadoBs;
	}

	/**
	 * @param string
	 */
	public void setResultadoBs(String string) {
		resultadoBs = string;
	}

	/**
	 * @return
	 */
	public String getStatusBs() {
		return statusBs;
	}

	/**
	 * @param string
	 */
	public void setStatusBs(String string) {
		statusBs = string;
	}

}