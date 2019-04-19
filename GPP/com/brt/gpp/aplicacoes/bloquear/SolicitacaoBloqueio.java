package com.brt.gpp.aplicacoes.bloquear;

/**
  * Este arquivo refere-se a classe SolicitacaoBloqueio, responsavel por 
  * armazenar os dados referentes a uma solicitação de bloqueio/desbloqueio
  * do serviço de um usuário 
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
	String acao;			// indica se é um bloqueio("B")/desbloqueio("D")
	String servico;
	
	/**
	 * Metodo...: SolicitacaoBloqueio
	 * Descricao: Construtor com valores de inicialização
	 * @param 	String	msisdn	Msisdn da solicitação de bloqueio/desbloqueio
	 * @param 	Strng	acao	"B" para bloqueio, "D" para desbloqueio
	 * @param 	String	servico	Serviço a ser bloqueado/desbloqueado
	 * @param	String	retorno	Ação bem sucedida (Y) ou com erro (N)	
	 */
	public SolicitacaoBloqueio(String msisdn, String acao, String servico)
	{
		// Inicialização de todas as variáveis membro dessa classe
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
	 * Descricao: Construtor Padrão
	 */
	public SolicitacaoBloqueio()
	{
		// Não faz nada
	}
	
	// Métodos Getters and Setters
	
	/**
	 * Metodo...: getAcao
	 * Descricao: Retorna o tipo de ação (bloqueio/desbloqueio)
	 * @return	String	"B" para bloqueio/ "D" para desbloqueio
	 */
	public String getAcao() {
		return acao;
	}

	/**
	 * Metodo...: getMsisnd
	 * Descricao: Retorna o Msisdn da solicitação
	 * @return	String	Msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Metodo...: getServico
	 * Descricao: Retorna o serviço
	 * @return	String	Serviço
	 */
	public String getServico() {
		return servico;
	}

	/**
	 * Metodo...: setAcao
	 * Descricao: Indica se é bloqueio/desbloqueio
	 * @param 	String	string	"B" para bloqueio; "D" para desbloqueio
	 */
	public void setAcao(String string) {
		acao = string;
	}

	/**
	 * Metodo...: setMsisdn
	 * Descricao: Indica o Msisdn
	 * @param 	String	string	Msisdn da solicitação
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

	/**
	 * Metodo...: setServico
	 * Descricao: Indica o Serviço a ser bloqueado
	 * @param 	String	string	Identificador do Serviço
	 */
	public void setServico(String string) {
		servico = string;
	}
}
