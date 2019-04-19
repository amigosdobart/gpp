// Definicao do Pacote
package com.brt.gpp.aplicacoes.contestar;

/**
  *
  * Este arquivo contem a definicao da classe de Dados de Usuario em Shutdown
  * <P> Versao:        	1.0
  *
  * @Autor:            	Vanessa Andrade
  * Data:               14/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class DadosContestacao 
{
	private int id_evento;
	private String evento;
	private String numeroBS;
	private String status;
	private String datahoraFechamento;
	private String operador;
	private String msisdnOrigem;
	private String msisdnDestino;
	private String datahoraServico;
	private String descricaoAnalise;
	private double valorRetornadoPrincipal;
	private double valorRetornadoBonus;
	private double valorRetornadoSm;
//	private double valorRetornadoDados;
	private String codigoRetorno;
	
	
	/**
	 * Metodo...: DadosContestacao
	 * Descricao: Construtor Padrao
	 * @param	
	 * @return									
	 */
	public DadosContestacao ( )
	{
	}
    

	// Métodos Get                             
	/**
	 * Metodo...: getid_evento
	 * Descricao: Busca o idEvento para a contestacao de cobranca
	 * @param 
	 * @return	int 	- Retorna o idEvento para a contestacao de cobranca
	 */
	public int getid_evento() 
	{
		return this.id_evento;
	}

	/**
	 * Metodo...: getevento
	 * Descricao: Busca o evento para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o evento para a contestacao de cobranca
	 */
	public String getevento() 
	{
		return this.evento;
	}


	/**
	 * Metodo...: getnumeroBS
	 * Descricao: Busca o numeroBS para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o numeroBS para a contestacao de cobranca
	 */
	public String getnumeroBS() 
	{
		return this.numeroBS;
	}

	/**
	 * Metodo...: getstatus
	 * Descricao: Busca o status para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o status para a contestacao de cobranca
	 */
	public String getstatus() 
	{
		return this.status;
	}

	/**
	 * Metodo...: getdatahoraFechamento
	 * Descricao: Busca o datahoraFechamento para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o datahoraFechamento para a contestacao de cobranca
	 */
	public String getdatahoraFechamento() 
	{
		return this.datahoraFechamento;
	}

	/**
	 * Metodo...: getoperador
	 * Descricao: Busca o operador para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o operador para a contestacao de cobranca
	 */
	public String getoperador() 
	{
		return this.operador;
	}

	/**
	 * Metodo...: getmsisdnOrigem
	 * Descricao: Busca o msisdnOrigem para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o msisdnOrigem para a contestacao de cobranca
	 */
	public String getmsisdnOrigem() 
	{
		return this.msisdnOrigem;
	}

	/**
	 * Metodo...: getmsisdnDestino
	 * Descricao: Busca o msisdnDestino para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o msisdnDestino para a contestacao de cobranca
	 */
	public String getmsisdnDestino() 
	{
		return this.msisdnDestino;
	}

	/**
	 * Metodo...: getdatahoraServico
	 * Descricao: Busca o datahoraServico para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o datahoraServico para a contestacao de cobranca
	 */
	public String getdatahoraServico() 
	{
		return this.datahoraServico;
	}

	/**
	 * Metodo...: getdescricaoAnalise
	 * Descricao: Busca a descricaoAnalise para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna a descricaoAnalise para a contestacao de cobranca
	 */
	public String getdescricaoAnalise() 
	{
		return this.descricaoAnalise;
	}

	/**
	 * Metodo...: getvalorRetornadoPrincipal
	 * Descricao: Busca o valorRetornadoPrincipal para a contestacao de cobranca
	 * @param 
	 * @return String - Retorna o valorRetornadoPrincipal para a contestacao de cobranca
	 */
	public double getvalorRetornadoPrincipal() 
	{
		return this.valorRetornadoPrincipal;
	}

	/**
	 * Metodo....:getvalorRetornadoBonus
	 * Descricao.:Retorna o valor da contestacao do saldo de bonus
	 * @return - Valor a ser ajustado pela contestacao no saldo de bonus
	 */
	public double getvalorRetornadoBonus() 
	{
		return this.valorRetornadoBonus;
	}

	/**
	 * Metodo....:getvalorRetornadoSm
	 * Descricao.:Retorna o valor da contestacao do saldo de Sm
	 * @return - Valor a ser ajustado pela contestacao no saldo de Sm
	 */
	public double getvalorRetornadoSm() 
	{
		return this.valorRetornadoSm;
	}
	
	/**
	 * Metodo....:getvalorRetornadoDados
	 * Descricao.:Retorna o valor da contestacao do saldo de dados
	 * @return - Valor a ser ajustado pela contestacao no saldo de dados
	 */
	public double getvalorRetornadoDados() 
	{
		return this.valorRetornadoBonus;
	}

	/**
	 * Metodo....:getCodigoRetorno
	 * Descricao.:Retorna o codigo de retorno da operacao de obtencao do objeto DadosContestacao
	 * @return - Codigo de retorno da operacao de obtencao do objeto DadosContestacao
	 */
	public String getCodigoRetorno() 
	{
		return this.codigoRetorno;
	}

	// Métodos Set
	/**
	 * Metodo...: setid_evento
	 * Descricao: Armazena o id_evento a contestacao de cobranca
	 * @param aid_evento - O id_evento a contestacao de cobranca
	 * @return 
	 */
	public void setid_evento(int aid_evento) 
	{
		this.id_evento = aid_evento;
	}

	/**
	 * Metodo...: setevento
	 * Descricao: Armazena o evento a contestacao de cobranca
	 * @param aevento - O evento a contestacao de cobranca
	 * @return 
	 */
	public void setevento(String aevento) 
	{
		this.evento = aevento;
	}

	/**
	 * Metodo...: setnumeroBS
	 * Descricao: Armazena o numeroBS a contestacao de cobranca
	 * @param anumeroBS - O numeroBS a contestacao de cobranca
	 * @return 
	 */
	public void setnumeroBS(String anumeroBS) 
	{
		this.numeroBS = anumeroBS;
	}

	/**
	 * Metodo...: setstatus
	 * Descricao: Armazena o status a contestacao de cobranca
	 * @param astatus - O status a contestacao de cobranca
	 * @return 
	 */
	public void setstatus(String astatus) 
	{
		this.status = astatus;
	}

	/**
	 * Metodo...: setdatahoraFechamento
	 * Descricao: Armazena a datahoraFechamento a contestacao de cobranca
	 * @param adatahoraFechamento - A datahoraFechamento a contestacao de cobranca
	 * @return 
	 */
	public void setdatahoraFechamento(String adatahoraFechamento) 
	{
		this.datahoraFechamento = adatahoraFechamento;
	}

	/**
	 * Metodo...: setoperador
	 * Descricao: Armazena o operador a contestacao de cobranca
	 * @param aoperador - O operador a contestacao de cobranca
	 * @return 
	 */
	public void setoperador(String aoperador) 
	{
		this.operador = aoperador;
	}

	/**
	 * Metodo...: setmsisdnOrigem
	 * Descricao: Armazena o msisdnOrigem a contestacao de cobranca
	 * @param amsisdnOrigem - O msisdnOrigem a contestacao de cobranca
	 * @return 
	 */
	public void setmsisdnOrigem(String amsisdnOrigem) 
	{
		this.msisdnOrigem = amsisdnOrigem;
	}

	/**
	 * Metodo...: setmsisdnDestino
	 * Descricao: Armazena o msisdnDestino a contestacao de cobranca
	 * @param amsisdnDestino - O msisdnDestino a contestacao de cobranca
	 * @return 
	 */
	public void setmsisdnDestino(String amsisdnDestino) 
	{
		this.msisdnDestino = amsisdnDestino;
	}

	/**
	 * Metodo...: setdatahoraServico
	 * Descricao: Armazena a datahoraServico a contestacao de cobranca
	 * @param adatahoraServico - A datahoraServico a contestacao de cobranca
	 * @return 
	 */
	public void setdatahoraServico(String adatahoraServico) 
	{
		this.datahoraServico = adatahoraServico;
	}

	/**
	 * Metodo...: setdescricaoAnalise
	 * Descricao: Armazena a descricaoAnalise a contestacao de cobranca
	 * @param adescricaoAnalise - A descricaoAnalise a contestacao de cobranca
	 * @return 
	 */
	public void setdescricaoAnalise(String adescricaoAnalise) 
	{
		this.descricaoAnalise = adescricaoAnalise;
	}

	/**
	 * Metodo...: setvalorRetornadoPrincipal
	 * Descricao: Armazena a valorRetornadoPrincipal a contestacao de cobranca
	 * @param avalorRetornado - A valorRetornadoPrincipal a contestacao de cobranca
	 * @return 
	 */
	public void setvalorRetornadoPrincipal(double avalorRetornado) 
	{
		this.valorRetornadoPrincipal = avalorRetornado;
	}

	/**
	 * Metodo....:setvalorRetornadoBonus
	 * Descricao.:Define o valor ajustado para o saldo de Bonus pela contestacao
	 * @param avalorRetornado	- Valor a ser ajustado para o saldo de Bonus
	 */
	public void setvalorRetornadoBonus(double avalorRetornado) 
	{
		this.valorRetornadoBonus = avalorRetornado;
	}

	/**
	 * Metodo....:setvalorRetornadoSm
	 * Descricao.:Define o valor ajustado para o saldo de Sm pela contestacao
	 * @param avalorRetornado	- Valor a ser ajustado para o saldo de Sm
	 */
	public void setvalorRetornadoSm(double avalorRetornado) 
	{
		this.valorRetornadoSm = avalorRetornado;
	}
	
	/**
	 * Metodo....:setvalorRetornadoDados
	 * Descricao.:Define o valor ajustado para o saldo de Dados pela contestacao
	 * @param avalorRetornado	- Valor a ser ajustado para o saldo de Dados
	 */
	public void setvalorRetornadoDados(double avalorRetornado) 
	{
//		this.valorRetornadoDados = avalorRetornado;
	}

	/**
	 * Metodo....:setCodigoRetorno
	 * Descricao.:Define o codigo de retorno do processo de obtencao do objeto DadosContestacao
	 * @param codigoRetorno	- Valor a ser definido para o objeto DadosContestacao
	 */
	public void setCodigoRetorno(String codigoRetorno) 
	{
		this.codigoRetorno = codigoRetorno;
	}

	/**
	 * Metodo...: getRetornoItemContestacaoXML
	 * Descricao: Armazena os dados da contestacao de cobranca para retornar o XML do item 
	 * @param desc_erro		- Valor da descricao do erro
	 * @param cod_erro		- Valor do codigo do erro
	 * @param status_xml	- Status do item em questao
	 * @return String		- Retorna o xml de retorno do item da contestacao de cobranca
	 */
	public String getRetornoItemContestacaoXML (String desc_erro, String cod_erro, String status_xml)
	{
		String retorno = "<?xml version=\"1.0\"?>";
		retorno = retorno + "<root>";
			retorno = retorno + "<case>";
		retorno = retorno + "<evento>" + "SFA843RN                        " + "</evento>";
				retorno = retorno + "<id_evento>"  + this.getid_evento() + "</id_evento>";
				retorno = retorno + "<codigo>" + cod_erro + "</codigo>";
				retorno = retorno + "<descricao>" + desc_erro + "</descricao>";
			retorno = retorno + "</case>";
		retorno = retorno + "</root>";
		return retorno;  
	}


}