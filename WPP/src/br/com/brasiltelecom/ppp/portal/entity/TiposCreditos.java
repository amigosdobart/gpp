/*
 * Created on 26/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de tipos de creditos
 * @author Ricardo Albuquerque
 * @since 21/05/2004
 */
public class TiposCreditos {

private String tipoCredito;
private String tipoCreditoDesc;

/**
 * @return
 */
public String getTipoCredito() {
	return tipoCredito;
}

/**
 * @return
 */
public String getTipoCreditoDesc() {
	return tipoCreditoDesc;
}

/**
 * @param string
 */
public void setTipoCredito(String string) {
	tipoCredito = string;
}

/**
 * @param string
 */
public void setTipoCreditoDesc(String string) {
	tipoCreditoDesc = string;
}

}
