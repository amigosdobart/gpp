
package com.brt.gpp.aplicacoes.consultar;

/**
* Este arquivo contem a definicao da classe de SolicitacaoRecarga,
* responsável por encapsular as diversas solicitações de recarga de uma 
* consulta pré-recarga 
* <P> Versao:        	1.0
*
* @Autor:            Denys Oliveira
* Data:              23/11/2004
*
* Modificado por:
* Data:
* Razao:
*
*/
public class SolicitacaoRecarga 
{
	private double	idValor;
	private String	msisdn;
	
	/**
	 * Metodo...: SolicitacaoRecarga
	 * Descricao: Construtor
	 * @param 	long	idValor		Id do valor da recarga
	 * @param 	String	msisdn		Msisdn do Assinante
	 */
	public SolicitacaoRecarga(double idValor, String msisdn)
	{
		this.idValor = idValor;
		this.msisdn = msisdn;
	}
	
	/**
	 * @return Returns the idValor.
	 */
	public double getIdValor() {
		return idValor;
	}
	/**
	 * @param idValor The idValor to set.
	 */
	public void setIdValor(double idValor) {
		this.idValor = idValor;
	}
	/**
	 * @return Returns the msisdn.
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @param msisdn The msisdn to set.
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
}
