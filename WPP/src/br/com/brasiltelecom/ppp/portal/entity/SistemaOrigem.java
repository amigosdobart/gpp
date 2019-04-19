/*
 * Criado em 05/07/2005
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Modela a tabela de sistema de origem
 * @author Marcelo Alves Araujo
 * @since 05/07/2005
 */
public class SistemaOrigem {
	
private String	idSistemaOrigem;
private String 	desSistemaOrigem;
private int 	validaSistemaOrigem;
private int 	validaSaldo;

/**
 * @return
 */
public String getDesSistemaOrigem() {
	return desSistemaOrigem;
}

/**
 * @return
 */
public String getIdSistemaOrigem() {
	return idSistemaOrigem;
}

/**
 * @return
 */
public int getValidaSistemaOrigem() {
	return validaSistemaOrigem;
}

/**
 * @return
 */
public int getValidaSaldo() {
	return validaSaldo;
}

/**
 * @param string
 */
public void setDesSistemaOrigem(String string) {
	desSistemaOrigem = string;
}

/**
 * @param char
 */
public void setIdSistemaOrigem(String idSistem) {
	idSistemaOrigem = idSistem;
}

/**
 * @param int
 */
public void setValidaSistemaOrigem(int valida) {
	validaSistemaOrigem = valida;
}

/**
 * @param int
 */
public void setValidaSaldo(int valida) {
	validaSaldo = valida;
}
}
