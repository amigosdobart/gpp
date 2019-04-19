package com.brt.gpp.aplicacoes.bloquear;

/**
  * Este arquivo refere-se a classe SolicitacaoBloqueio, responsavel por 
  * armazenar os dados referentes a uma solicita��o de bloqueio/desbloqueio
  * do servi�o de um usu�rio 
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				02/08/2004
  *
  * Modificado por:		
  * Data:				
  * Razao:				
  */
public class SolicitacaoBloqueio 
{
	// Atributos da Classe
	String msisdn;
	String acao;			// indica se � um bloqueio("B")/desbloqueio("D")
	String servico;
	
	/**
	 * Metodo...: SolicitacaoBloqueio
	 * Descricao: Construtor com valores de inicializa��o
	 * @param 	String	msisdn	Msisdn da solicita��o de bloqueio/desbloqueio
	 * @param 	Strng	acao	"B" para bloqueio, "D" para desbloqueio
	 * @param 	String	servico	Servi�o a ser bloqueado/desbloqueado
	 * @param	String	retorno	A��o bem sucedida (Y) ou com erro (N)	
	 */
	public SolicitacaoBloqueio(String msisdn, String acao, String servico)
	{
		// Inicializa��o de todas as vari�veis membro dessa classe
		this.msisdn = msisdn;
		this.acao = acao;
		this.servico = servico;
	}	
	
	/***
	 * Metodo...: setAtributos
	 * Descricao: Seta todos os atributos da classe
	 */
	public void setAtributos(String msisdn, String acao, String servico)
	{
		this.msisdn = msisdn;
		this.acao = acao;
		this.servico = servico;

	}
	
	/**
	 * Metodo...: SolicitacaoBloqueio
	 * Descricao: Construtor Padr�o
	 */
	public SolicitacaoBloqueio()
	{
		// N�o faz nada
	}
	
	// M�todos Getters and Setters
	
	/**
	 * Metodo...: getAcao
	 * Descricao: Retorna o tipo de a��o (bloqueio/desbloqueio)
	 * @return	String	"B" para bloqueio/ "D" para desbloqueio
	 */
	public String getAcao() {
		return acao;
	}

	/**
	 * Metodo...: getMsisnd
	 * Descricao: Retorna o Msisdn da solicita��o
	 * @return	String	Msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Metodo...: getServico
	 * Descricao: Retorna o servi�o
	 * @return	String	Servi�o
	 */
	public String getServico() {
		return servico;
	}

	/**
	 * Metodo...: setAcao
	 * Descricao: Indica se � bloqueio/desbloqueio
	 * @param 	String	string	"B" para bloqueio; "D" para desbloqueio
	 */
	public void setAcao(String string) {
		acao = string;
	}

	/**
	 * Metodo...: setMsisdn
	 * Descricao: Indica o Msisdn
	 * @param 	String	string	Msisdn da solicita��o
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

	/**
	 * Metodo...: setServico
	 * Descricao: Indica o Servi�o a ser bloqueado
	 * @param 	String	string	Identificador do Servi�o
	 */
	public void setServico(String string) {
		servico = string;
	}
}
