//Definicao do Pacote
package com.brt.gpp.aplicacoes.cobilling;

/**
 *
 * Este arquivo refere-se a classe Cobilling, cont�m os
 * dados referentes ao campo detalhe no arquivo gerado
 * por GerarArquivoCobilling
 *
 * <P> Versao		:	1.0
 *
 * @Autor			: 	Marcelo Alves Araujo
 * Data				:	04/05/2005
 * 	
 * 
 * Modificado por 	:
 * Data 			:
 * Razao 			:
 * 
 * 
 */

public class CobillingDetalhe {

	// Dados referentes aos detalhes do Cobilling
    private String assinanteA;
	private String dataChamada;
	private String horaChamada;
	private String assinanteB;
	private String pontoInterconexao;
	private String duracaoRealChamada;
	private String duracaoTarifadaChamada;
	private String tipoServico;
	private String valorLiquidoChamada;
	private String valorBrutoChamada;
	private String reserva;
	
	/**
	 * Metodo...: Cabecalho
	 * Descricao: Construtor 
	 * @param	
	 * @return	
	 */
	public CobillingDetalhe()
	{
		// Construtor padr�o n�o faz nada
	}
	
	/**
	 * Metodo...: Cabecalho
	 * Descricao: Construtor 
	 * @param assinanteA			- C�digo de �rea + Prefixo da Central + MCDU do originador da chamada
	 * @param dataChamada			- Data da realiza��o da chamada (ddmmaaaa)
	 * @param horaChamada			- Hora da realiza��o da chamada (hhmmss)
	 * @param assinanteB			- N�mero do assinante de destino:
	 *									Chamadas Nacionais: CSP + c�digo de �rea + Prefixo da Central + MCDU. 
	 *									Chamadas Internaci: "0" + CSP + c�digo do pa�s + n� do assinante.
	 * @param pontoInterconexao		- C�digo POI/PPI (Inicialmente "")
	 * @param duracaoRealChamada	- Tempo medido da chamada (hhhmmss)
	 * @param duracaoTarifadaChamada- Tempo cobrado da chamada(hhhmmss)
	 * @param tipoServico			- "01": VC2 
	 * 								- "02": VC3 
	 * 								- "03": 0300 
	 * 								- "04": 0500 
	 * 								- "05": Internacionais
	 * @param valorLiquidoChamada 	- Valor cobrado pela chamada sem taxas (com duas casas decimais)
	 * @param valorBrutoChamada		- Valor cobrado pela chamada com taxas (com duas casas decimais)
	 * @param reserva 				- Espa�o em branco
	 * @return
	 */
	public CobillingDetalhe(	String assinanteA, String dataChamada, String horaChamada, 
	        					String assinanteB, String pontoInterconexao, String duracaoRealChamada,
	        					String duracaoTarifadaChamada, String tipoServico, String valorLiquidoChamada,
	        					String valorBrutoChamada, String reserva)
	{
	    this.assinanteA				= assinanteA;
		this.dataChamada			= dataChamada;
		this.horaChamada			= horaChamada;
		this.assinanteB				= assinanteB;
		this.pontoInterconexao		= pontoInterconexao;
		this.duracaoRealChamada		= duracaoRealChamada;
		this.duracaoTarifadaChamada	= duracaoTarifadaChamada;
		this.tipoServico			= tipoServico;
		this.valorLiquidoChamada	= valorLiquidoChamada;
		this.valorBrutoChamada		= valorBrutoChamada;
		this.reserva				= reserva;
	}
	
	// Metodos Get
	
	/**
	 * Metodo...: getAssinanteA
	 * Descricao: Retorna o n�mero do originador da chamada
	 * @param
	 * @return String	- C�digo de �rea + Prefixo da Central + MCDU
	 */
	public String getAssinanteA() {
		return this.assinanteA;
	}

	/**
	 * Metodo...: getDataChamada
	 * Descricao: Retorna a data em que foi realizada a chamada
	 * @param
	 * @return String	- Data da chamada (formato ddmmaaaa)
	 */
	public String getDataChamada() {
		return this.dataChamada;
	}

	/**
	 * Metodo...: getHoraChamada
	 * Descricao: Retorna a hora em que foi realizada a chamada
	 * @param
	 * @return String	- Hora da chamada (formato hhmmss)
	 */
	public String getHoraChamada() {
		return this.horaChamada;
	}

	/**
	 * Metodo...: getFinalPeriodo
	 * Descricao: Retorna o n�mero do receptor da chamada
	 * @param
	 * @return String	-	Chamadas Nacionais: CSP + c�digo de �rea + Prefixo da Central + MCDU. 
	 *						Chamadas Internaci: "0" + CSP + c�digo do pa�s + n� do assinante.
	 */
	public String getAssinanteB() {
		return this.assinanteB;
	}

	/**
	 * Metodo...: getPontoInterconexao
	 * Descricao: Retorna o c�digo POI/PPI
	 * @param
	 * @return String	- Inicialmente retorna somente espa�os em branco
	 */
	public String getPontoInterconexao() {
		return this.pontoInterconexao;
	}

	/**
	 * Metodo...: getDuracaoRealChamada
	 * Descricao: Retorna a dura��o real da liga��o 
	 * @param
	 * @return String	- Tempo de liga��o (formato hhhmmss)
	 */
	public String getDuracaoRealChamada() {
		return this.duracaoRealChamada;
	}

	/**
	 * Metodo...: getDuracaoTarifadaChamada
	 * Descricao: Retorna a dura��o tarifada da liga��o
	 * @param
	 * @return String	- Tempo tarifado (formato hhhmmss)
	 */
	public String getDuracaoTarifadaChamada() {
		return this.duracaoTarifadaChamada;
	}

	/**
	 * Metodo...: getTipoServico
	 * Descricao: Retorna tipo de servi�o utilizado
	 * @param
	 * @return String	- "01": VC2, "02": VC3, "03": 0300, "04": 0500, "05": Internacionais
	 */
	public String getTipoServico() {
		return this.tipoServico;
	}

	/**
	 * Metodo...: getValorLiquidoChamada
	 * Descricao: Retorna o valor da chamada sem a cobran�a de taxas
	 * @param
	 * @return String	- Valor com duas casas decimais
	 */
	public String getValorLiquidoChamada() {
		return this.valorLiquidoChamada;
	}

	/**
	 * Metodo...: getValorBrutoChamada
	 * Descricao: Retorna o valor da chamada com a cobran�a de taxas
	 * @param
	 * @return String	- Valor com duas casas decimais
	 */
	public String getValorBrutoChamada() {
		return this.valorBrutoChamada;
	}
	
	/**
	 * Metodo...: getComplemento
	 * Descricao: Retorna o campo reservado
	 * @param
	 * @return String	- Inicialmente � uma string com espa�os em branco
	 */
	public String getReserva() {
		return this.reserva;
	}

	/**
	 * Metodo...: getDetalhe
	 * Descricao: Retorna String do arquivo de batimento de cobilling
	 * @param
	 * @return String	- Rertorna uma String com todas as vari�veis globais
	 */
	public String getDetalhe() {
		return (assinanteA+dataChamada+horaChamada+assinanteB+
		        pontoInterconexao+duracaoRealChamada+duracaoTarifadaChamada+
		        tipoServico+valorLiquidoChamada+valorBrutoChamada+reserva);
	}
	
	// Metodos Set
	
	/**
	 * Metodo...: setAssinanteA
	 * Descricao: Atribui o n�mero do originador da chamada
	 * @param	String	- C�digo de �rea + Prefixo da Central + MCDU
	 * @return 
	 */
	public void setAssinanteA(String assinanteA) {
		this.assinanteA = assinanteA;
	}

	/**
	 * Metodo...: setDataChamada
	 * Descricao: Atribui a data em que foi realizada a chamada
	 * @param	String	- Data da chamada (formato ddmmaaaa)
	 * @return 
	 */
	public void setDataChamada(String dataChamada) {
		this.dataChamada = dataChamada;
	}

	/**
	 * Metodo...: setHoraChamada
	 * Descricao: Atribui a hora em que foi realizada a chamada
	 * @param	String	- Hora da chamada (formato hhmmss)
	 * @return 
	 */
	public void setHoraChamada(String horaChamada) {
		this.horaChamada = horaChamada;
	}

	/**
	 * Metodo...: setFinalPeriodo
	 * Descricao: Atribui o n�mero do receptor da chamada
	 * @param	String	-	Chamadas Nacionais: CSP + c�digo de �rea + Prefixo da Central + MCDU. 
	 *						Chamadas Internaci: "0" + CSP + c�digo do pa�s + n� do assinante.
	 * @return 
	 */
	public void setAssinanteB(String assinanteB) {
		this.assinanteB = assinanteB;
	}

	/**
	 * Metodo...: setPontoInterconexao
	 * Descricao: Atribui o c�digo POI/PPI
	 * @param	String	- Inicialmente retorna somente espa�os em branco
	 * @return 
	 */
	public void setPontoInterconexao(String pontoInterconexao) {
		this.pontoInterconexao = pontoInterconexao;
	}

	/**
	 * Metodo...: setDuracaoRealChamada
	 * Descricao: Atribui a dura��o real da liga��o 
	 * @param	String	- Tempo de liga��o (formato hhhmmss)
	 * @return 
	 */
	public void setDuracaoRealChamada(String duracaoRealChamada) {
		this.duracaoRealChamada = duracaoRealChamada;
	}

	/**
	 * Metodo...: setDuracaoTarifadaChamada
	 * Descricao: Atribui a dura��o tarifada da liga��o
	 * @param	String	- Tempo tarifado (formato hhhmmss)
	 * @return 
	 */
	public void setDuracaoTarifadaChamada(String duracaoTarifadaChamada) {
		this.duracaoTarifadaChamada = duracaoTarifadaChamada;
	}

	/**
	 * Metodo...: setTipoServico
	 * Descricao: Atribui tipo de servi�o utilizado
	 * @param	String	- "01": VC2, "02": VC3, "03": 0300, "04": 0500, "05": Internacionais
	 * @return 
	 */
	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	/**
	 * Metodo...: setValorLiquidoChamada
	 * Descricao: Atribui o valor da chamada sem a cobran�a de taxas
	 * @param	String	- Valor com duas casas decimais
	 * @return 
	 */
	public void setValorLiquidoChamada(String valorLiquidoChamada) {
		this.valorLiquidoChamada = valorLiquidoChamada;
	}

	/**
	 * Metodo...: setValorBrutoChamada
	 * Descricao: Atribui o valor da chamada com a cobran�a de taxas
	 * @param	String	- Valor com duas casas decimais
	 * @return 
	 */
	public void setValorBrutoChamada(String valorBrutoChamada) {
		this.valorBrutoChamada = valorBrutoChamada;
	}
	
	/**
	 * Metodo...: setComplemento
	 * Descricao: Atribui o campo reservado
	 * @param	String	- Inicialmente � uma string com espa�os em branco
	 * @return 
	 */
	public void setReserva(String reserva) {
		this.reserva = reserva;
	}
}
