
package com.brt.clientes.form.aprovisionamento;

import org.apache.struts.action.ActionForm;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;

/**
 * Modelo de desativacao
 * @author Alex Pitacci Simões
 * @since 02/06/2004
 */
public class DesativacaoForm extends ActionForm {

	private String msisdn;
	private String motivo;
	private retornoDesativacaoAssinante retorno;
	
	public short getCodigoRetorno(){
		return retorno.codigoRetorno;
	}
	
	public String getSaldoFinal(){
		return retorno.saldoFinal;
	}
	/**
	 * @return Returns the retorno.
	 */
	public retornoDesativacaoAssinante getRetorno() {
		return retorno;
	}
	/**
	 * @param retorno The retorno to set.
	 */
	public void setRetorno(retornoDesativacaoAssinante retorno) {
		this.retorno = retorno;
	}
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
