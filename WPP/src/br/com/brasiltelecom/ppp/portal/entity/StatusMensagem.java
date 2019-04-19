/*
 * Created on 15/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de status mensagem
 * @author Ricardo Albuquerque
 * @since 21/05/2004
 */
public class StatusMensagem {

  private int idStatusVoucher;
  private String idMotivo;
  private String desMensagem;

/**
 * @return
 */
public String getDesMensagem() {
	return desMensagem;
}

/**
 * @return
 */
public String getIdMotivo() {
	return idMotivo;
}

/**
 * @return
 */
public int getIdStatusVoucher() {
	return idStatusVoucher;
}

/**
 * @param string
 */
public void setDesMensagem(String string) {
	desMensagem = string;
}

/**
 * @param string
 */
public void setIdMotivo(String string) {
	idMotivo = string;
}

/**
 * @param i
 */
public void setIdStatusVoucher(int i) {
	idStatusVoucher = i;
}

}
