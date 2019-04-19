/*
 * Created on 13/09/2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */


package br.com.brasiltelecom.ppp.portal.entity;
import java.util.Date;

/**
 * @author Henrique Canto
 *
 */
public class AssinanteServicoBloqueado {
private String idMsisdn;
private String descServico;
private String descMotivo;
private String DescStatus;
private Date datAtualizacao;




/**
 * @return Returns the datAtualizacao.
 */
public Date getDatAtualizacao() {
	return datAtualizacao;
}
/**
 * @param datAtualizacao The datAtualizacao to set.
 */
public void setDatAtualizacao(Date datAtualizacao) {
	this.datAtualizacao = datAtualizacao;
}
/**
 * @return Returns the descMotivo.
 */
public String getDescMotivo() {
	return descMotivo;
}
/**
 * @param descMotivo The descMotivo to set.
 */
public void setDescMotivo(String descMotivo) {
	this.descMotivo = descMotivo;
}
/**
 * @return Returns the descServico.
 */
public String getDescServico() {
	return descServico;
}
/**
 * @param descServico The descServico to set.
 */
public void setDescServico(String descServico) {
	this.descServico = descServico;
}
/**
 * @return Returns the descStatus.
 */
public String getDescStatus() {
	return DescStatus;
}
/**
 * @param descStatus The descStatus to set.
 */
public void setDescStatus(String descStatus) {
	DescStatus = descStatus;
}
/**
 * @return Returns the idMsisdn.
 */
public String getIdMsisdn() {
	return idMsisdn;
}
/**
 * @param idMsisdn The idMsisdn to set.
 */
public void setIdMsisdn(String idMsisdn) {
	this.idMsisdn = idMsisdn;
}
}
