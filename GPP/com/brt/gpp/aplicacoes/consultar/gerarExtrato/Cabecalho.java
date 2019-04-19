//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.gerarExtrato;

public class Cabecalho {

	// Dados referentes ao Cabeçalho
	private String nomeCliente;
	private String endereco;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private String msisdn;
	private String plano;
	private String dataAtivacao;
	private String inicioPeriodo;
	private String finalPeriodo;
	private String dataHora;
	
	/**
	 * Metodo...: Cabecalho
	 * Descricao: Construtor 
	 * @param	
	 * @return	
	 */
	public Cabecalho()
	{
		// Construtor padrão não faz nada
	}
	
	/**
	 * Metodo...: Cabecalho
	 * Descricao: Construtor 
	 * @param nomeCliente	- Nome do assinante
	 * @param endereco		- Endereco do assinante
	 * @param complemento	- Complemento do endereco do assinante
	 * @param bairro		- Bairro do assinante
	 * @param cidade		- Cidade do assinante
	 * @param uf			- Estado do assinante
	 * @param cep			- Cep do assinante
	 * @param msisdn		- Numero do assinante
	 * @param plano			- Plano de precos
	 * @param dataAtivacao 	- Data de ativacao do assinante (formato dd/mm/aaaa)
	 * @param inicioPeriodo	- Periodo inicial do comprovante de servico - extrato (formato dd/mm/aaaa)
	 * @param finalPeriodo 	- Periodo final do comprovante de servico - extrato (format dd/mm/aaaa)
	 * @param dataHora		- Data e hora do sistema (formato dd/mm/aaaa hh:mm:ss)
	 * @param taxa			- Taxa do assinante
	 * @return
	 */
	public Cabecalho(	String nomeCliente, String endereco, String complemento, 
						String bairro, String cidade, String uf, String cep, String msisdn,
						String plano, String dataAtivacao, String inicioPeriodo, 
						String finalPeriodo, String dataHora, String taxa)
	{
		this.nomeCliente=nomeCliente;
		this.endereco=endereco;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
		this.cep = cep;
		this.msisdn=msisdn;
		this.plano=plano;
		this.dataAtivacao=dataAtivacao;
		this.inicioPeriodo=inicioPeriodo;
		this.finalPeriodo=finalPeriodo;
		this.dataHora=dataHora;
	}
	
	// Metodos Get
	/**
	 * Metodo...: getDataAtivacao
	 * Descricao: Retorna a data de ativacao do assinante
	 * @param
	 * @return String	- Data de ativacao do assinante (formato dd/mm/aaaa)
	 */
	public String getDataAtivacao() {
		return dataAtivacao;
	}

	/**
	 * Metodo...: getDataHora
	 * Descricao: Retorna a data e hora do sistema
	 * @param
	 * @return String	- Data e hora do sistema (formato dd/mm/aaaa hh:mm:ss)
	 */
	public String getDataHora() {
		return dataHora;
	}

	/**
	 * Metodo...: getEndereco
	 * Descricao: Retorna o endereceo do assinante
	 * @param
	 * @return String	- Endereco do assinante
	 */
	public String getEndereco() {
		return endereco;
	}

	/**
	 * Metodo...: getFinalPeriodo
	 * Descricao: Retorna o periodo final do comprovante de servico - extrato
	 * @param
	 * @return String	- Periodo final do comprovante de servico - extrato (format dd/mm/aaaa)
	 */
	public String getFinalPeriodo() {
		return finalPeriodo;
	}

	/**
	 * Metodo...: getInicioPeriodo
	 * Descricao: Retorna o periodo inicial do comprovante de servico - extrato
	 * @param
	 * @return String	- Periodo inicial do comprovante de servico - extrato (format dd/mm/aaaa)
	 */
	public String getInicioPeriodo() {
		return inicioPeriodo;
	}

	/**
	 * Metodo...: getMsisdn
	 * Descricao: Retorna o msisdn do assinante
	 * @param
	 * @return String	- Msisdn do assinante
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Metodo...: getNomeCliente
	 * Descricao: Retorna o nome do assinante
	 * @param
	 * @return String	- Nome do assinante
	 */
	public String getNomeCliente() {
		return nomeCliente;
	}

	/**
	 * Metodo...: getPlano
	 * Descricao: Retorna o plano do assinante
	 * @param
	 * @return String	- Plano do assinante
	 */
	public String getPlano() {
		return plano;
	}

	/**
	 * Metodo...: getCidade
	 * Descricao: Retorna o cidade do assinante
	 * @param
	 * @return String	- Cidade do assinante
	 */
	public String getCidade() {
		return cidade;
	}

	/**
	 * Metodo...: getComplemento
	 * Descricao: Retorna o complemento do endereco do assinante
	 * @param
	 * @return String	- Complemento do endereco do assinante
	 */
	public String getComplemento() {
		return complemento;
	}

	/**
	 * Metodo...: getCep
	 * Descricao: Retorna o cep do assinante
	 * @param
	 * @return String	- Cep do assinante
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * Metodo...: getUf
	 * Descricao: Retorna o estado do assinante
	 * @param
	 * @return String	- Estado do assinante
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * Metodo...: getBairro
	 * Descricao: Retorna o bairro do assinante
	 * @param
	 * @return String	- Bairro do assinante
	 */
	public String getBairro() {
		return bairro;
	}

	// Metodos Set
	/**
	 * Metodo...: setDataAtivacao
	 * Descricao: Atribui a data de ativacao do assinante ao cabecalho
	 * @param  String	- Data de ativacao do assinante (formato dd/mm/aaaa)
	 * @return
	 */
	public void setDataAtivacao(String string) {
		dataAtivacao = string;
	}

	/**
	 * Metodo...: setDataHora
	 * Descricao: Atribui a data e hora do sistema ao cabecalho
	 * @param  String	- Data e hora do sistema (formato dd/mm/aaaa hh:mm:ss)
	 * @return
	 */
	public void setDataHora(String string) {
		dataHora = string;
	}

	/**
	 * Metodo...: setEndereco
	 * Descricao: Atribui o endereco do assianante ao cabecalho
	 * @param  String	- Endereco do assinante 
	 * @return
	 */
	public void setEndereco(String string) {
		endereco = string;
	}

	/**
	 * Metodo...: setFinalPeriodo
	 * Descricao: Atribui periodo final do comprovante de servico - extrato ao cabecalho
	 * @param  String	- Periodo final do comprovante de servico - extrato (format dd/mm/aaaa)
	 * @return
	 */
	public void setFinalPeriodo(String string) {
		finalPeriodo = string;
	}

	/**
	 * Metodo...: setInicioPeriodo
	 * Descricao: Atribui periodo inicial do comprovante de servico - extrato ao cabecalho
	 * @param  String	- Periodo inicial do comprovante de servico - extrato (format dd/mm/aaaa)
	 * @return
	 */
	public void setInicioPeriodo(String string) {
		inicioPeriodo = string;
	}

	/**
	 * Metodo...: setMsisdn
	 * Descricao: Atribui o msisdn do assianante ao cabecalho
	 * @param  String	- Msisdn do assinante 
	 * @return
	 */
	public void setMsisdn(String string) {
		msisdn = string;
	}

	/**
	 * Metodo...: setNomeCliente
	 * Descricao: Atribui o nome do assianate ao cabecalho
	 * @param  String	- Nome do assinante 
	 * @return
	 */
	public void setNomeCliente(String string) {
		nomeCliente = string;
	}

	/**
	 * Metodo...: setPlano
	 * Descricao: Atribui o plano do assianate ao cabecalho
	 * @param  String	- Plano do assinante 
	 * @return
	 */
	public void setPlano(String string) {
		plano = string;
	}

	/**
	 * Metodo...: setCep
	 * Descricao: Atribui o cep do assianate ao cabecalho
	 * @param  String	- Cep do assinante 
	 * @return
	 */
	public void setCep(String string) {
		cep = string;
	}

	/**
	 * Metodo...: setCidade
	 * Descricao: Atribui a cidade do assianate ao cabecalho
	 * @param  String	- Cidade do assinante 
	 * @return
	 */
	public void setCidade(String string) {
		cidade = string;
	}

	/**
	 * Metodo...: setComplemento
	 * Descricao: Atribui o complemento do endereco do assianate ao cabecalho
	 * @param  String	- Complemento do endereco do assinante 
	 * @return
	 */
	public void setComplemento(String string) {
		complemento = string;
	}

	/**
	 * Metodo...: setUf
	 * Descricao: Atribui o estado do assianate ao cabecalho
	 * @param  String	- Estado do assinante 
	 * @return
	 */
	public void setUf(String string) {
		uf = string;
	}

	/**
	 * Metodo...: setBairro
	 * Descricao: Atribui o bairro do assianate ao cabecalho
	 * @param  String	- Bairro do assinante 
	 * @return
	 */
	public void setBairro(String string) {
		bairro = string;
	}

}
