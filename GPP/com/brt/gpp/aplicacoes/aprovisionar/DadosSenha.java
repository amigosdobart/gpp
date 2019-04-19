// Definicao do Pacote
package com.brt.gpp.aplicacoes.aprovisionar;

import com.brt.gpp.comum.GPPData;

/**
  *
  * Este arquivo contem a definicao da classe 
  * de Dados de Senha do Assinante 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Camile Cardoso Couto
  * Data:               06/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class DadosSenha
{
	// Atributos de Classe
	private short 	retorno;
	private String	descRetorno;
	private String 	MSISDN;
	private String 	senha;
	
	// Metodos setters

	/**
	 * Metodo...: setRetorno
	 * Descricao: Seta o Código de Retorno
	 * @param	short	aRetorno	Código de Retorno
	 */
	public void setRetorno (short aRetorno)
	{
		this.retorno = aRetorno;
	}
	
	/**
	 * Metodo...: setDescRetorno
	 * Descricao: Seta Descrição do Código de Retorno
	 * @param 	String	aDescRetorno	Descrição do Código de Retorno
	 */
	public void setDescRetorno(String aDescRetorno) 
	{
		this.descRetorno = aDescRetorno;
	}
	
	/**
	 * Metodo...: setMSISDN
	 * Descricao: Seta o MSISDN
	 * @param 	String	aMSISDN		MSISDN do assinante
	 */
	public void setMSISDN (String aMSISDN)
	{
		this.MSISDN = aMSISDN;	
	}

	/**
	 * Metodo...: setSenha
	 * Descricao: Seta a senha
	 * @param 	String	aSenha	Senha
	 */
	public void setSenha (String aSenha)
	{
		this.senha = aSenha;	
	}
	
	// Metodos getters

	/**
	 * Metodo...: getRetorno
	 * Descricao: Retorna o código de erro do processo
	 * @return	short
	 */
	public short getRetorno ( )
	{
		return this.retorno;
	}
	
	/**
	 * Metodo...: getDescRetorno
	 * Descricao: Retorna a Descrição do Código de Retorno
	 * @return	String
	 */
	public String getDescRetorno() 
	{
		return descRetorno;
	}
	
	/**
	 * Metodo...: getMSISDN
	 * Descricao: Retorna o MSISDN
	 * @return	String
	 */
	public String getMSISDN ( )
	{
		return this.MSISDN;	
	}

	/**
	 * Metodo...: getSenha
	 * Descricao: Retorna a senha
	 * @return	String	
	 */
	public String getSenha ( )
	{
		return this.senha;	
	}
	
	/**
	 * Metodo...: getXMLSaida
	 * Descricao: Retorna o XML de saída
	 * @return	String
	 */
	public String getXMLSaida ()
	{
		String retorno = "<?xml version=\"1.0\"?>";
		retorno = retorno + "<GPPRetornoTrocaSenha>"; 
			retorno = retorno + "<retorno>" 	+ GPPData.formataNumero(this.getRetorno(),4) 	+ "</retorno>";
			retorno = retorno + "<descRetorno>" + this.getDescRetorno() + "</descRetorno>";
		retorno = retorno + "</GPPRetornoTrocaSenha>";

		return retorno;  
	}
}