package com.brt.gpp.aplicacoes.controle.analiseInconsistencia;

/**
* Este arquivo refere-se a classe MovimentacaoCredito, responsavel por armazenar
* os dados referentes a um estado do saldo de um assinante
*
* <P> Versao:			1.0
*
* @Autor: 			Denys Oliveira
* Data: 				29/12/2004
*
*/
public class MovimentacaoCredito 
{
	private String dataHora;
	private boolean indRecarga;
	private double valor;
	private double saldoFinal;
	
	/**
	 * Metodo...: Construtor
	 * @param String		_dataHora		Data/Hora do status do saldo do cliente (dd/mm/yyyy HH:MI:SS)
	 * @param boolean		_indRecarga		true indica que se trata de um estado após uma recarga
	 * 										false indica que se trata de um estado após uma chamada
	 * @param double		_valor			valor da recarga/chamada que trouxe o saldo do cliente para esse status
	 * @param double		_saldoFinal		saldo final do assinante nesse estado
	 */
	public MovimentacaoCredito(String _dataHora, boolean _indRecarga, double _valor, double _saldoFinal)
	{
		this.dataHora = _dataHora;
		this.indRecarga = _indRecarga;
		this.valor = _valor;
		this.saldoFinal = _saldoFinal;
	}

	/**
	 * Metodo...:copiaDe
	 * Descricao: Traz para esse objeto os parâmetros de outro objeto
	 * @param 	MovimentacaoCredito _objetoASerCopiado
	 */
	public void copiaDe(MovimentacaoCredito _objetoASerCopiado)
	{
		this.dataHora = _objetoASerCopiado.getDataHora();
		this.indRecarga = _objetoASerCopiado.isIndRecarga();
		this.valor = _objetoASerCopiado.getValor();
		this.saldoFinal = _objetoASerCopiado.getSaldoFinal();
	}
	
	/**
	 * Metodo...: checarConsistencia
	 * Descricao: 	Verifica se esse objeto pode ser o estado posterior do saldo de um assinante
	 * 				que tinha "estadoAnterior" como estado anterior de saldo
	 * 				
	 * @param estadoAnterior
	 */
	public double checarConsistencia(MovimentacaoCredito estadoAnterior)
	{
		double gapSaldo;
	
		// Caso uma ligação tenha trazido o saldo do assinante para esse estado
		if(!indRecarga)
		{
			// retorna zero, caso sejam consistentes ou o valor do gap caso seja inconsistente
			gapSaldo =  estadoAnterior.getSaldoFinal() - this.valor - this.saldoFinal;
		}
		else
		{
			// retorna zero, caso sejam consistentes ou o valor do gap caso seja inconsistente
			gapSaldo = estadoAnterior.getSaldoFinal() + this.valor - this.saldoFinal; 
		}
		
		return gapSaldo;
	}
	
	
	/**
	 * @return Returns the dataHora.
	 */
	public String getDataHora() {
		return dataHora;
	}
	/**
	 * @return Returns the indRecarga.
	 */
	public boolean isIndRecarga() {
		return indRecarga;
	}
	/**
	 * @return Returns the saldoFinal.
	 */
	public double getSaldoFinal() {
		return saldoFinal;
	}
	/**
	 * @return Returns the valor.
	 */
	public double getValor() {
		return valor;
	}
}
