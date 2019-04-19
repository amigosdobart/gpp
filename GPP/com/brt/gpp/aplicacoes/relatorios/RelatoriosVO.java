//Definicao do Pacote
package com.brt.gpp.aplicacoes.relatorios;

/**
  *
  * Este arquivo contem a definicao da classe de RelatoriosVO
  * <P><b>Versao:</b>   	1.0
  *
  * <p><b>@Autor:</b>  		Marcelo Alves Araujo
  * <P><b>Data:</b>         30/08/2005
  *
  */

public class RelatoriosVO
{
	private int 	id;
	private String 	consulta;
	private String 	data;
	private String  arquivo;
	private String 	tipoSolicitacao;
	private String 	operador;
	private String	eMail;
	

	/**
	 * <P><b>Metodo...:</b> RelatoriosVO
	 * <P><b>Descricao:</b> Construtor Padrao
	 */
	public RelatoriosVO()
	{
	}
	
	/**
	 * Metodo...: EnvioUsuariosShutdown
	 * Descricao: Construtor 
	 * @param id		- Identificação da solicitação
	 * @param consulta	- Consulta a ser realizada
	 * @param data		- Data de agendamento da consulta
	 * @param arquivo	- Nome do arquivo
	 * @param tipo		- Tipo de solicitação: P (Permantente), T (Temporário)
	 * @param operador	- Operador que realizou a solicitação
	 * @param mail		- E-mail para onde sera mandada a mensagem ao término do processo
	 * @return
	 */
	public RelatoriosVO(int id, String consulta, String data, String arquivo, String tipo, String operador, String mail)
	{
		this.id = id;
		this.consulta = consulta;
		this.data = data;
		this.arquivo = arquivo;
		this.tipoSolicitacao = tipo;
		this.operador = operador;
		this.eMail = mail;
	}

	// Métodos Get                             
	/**
	 * <P><b>Metodo...:</b> getId
	 * <P><b>Descricao:</b> Busca o valor do Id 
	 * @return	int	- Valor do Id
	 */
	public int getId() 
	{
		return this.id;
	}

	/**
	 * <P><b>Metodo...:</b> getConsulta
	 * <P><b>Descricao:</b> Busca a Consulta 
	 * @return	String	- Consulta
	 */
	public String getConsulta() 
	{
		return this.consulta;
	}

	/**
	 * <P><b>Metodo...:</b> getData
	 * <P><b>Descricao:</b> Busca a data de agendamento da consulta 
	 * @return	String	- Data de agendamento
	 */
	public String getData() 
	{
		return this.data;
	}

	/**
	 * <P><b>Metodo...:</b> getArquivo
	 * <P><b>Descricao:</b> Busca o nome do arquivo 
	 * @return	String	- Nome do arquivo
	 */
	public String getArquivo()
	{
		return this.arquivo;
	}

	/**
	 * <P><b>Metodo...:</b> getTipoSolicitacao
	 * <P><b>Descricao:</b> Busca a data de agendamento da consulta 
	 * @return	String	- Tipo de solicitação T(Temporário)ou P(Permanente)
	 */
	public String getTipoSolicitacao() 
	{
		return this.tipoSolicitacao;
	}
	
	/**
	 * <P><b>Metodo...:</b> getOperador
	 * <P><b>Descricao:</b> Busca o operador que realizou a consulta 
	 * @return	String	- Operador
	 */
	public String getOperador()
	{
		return operador;
	}
	
	/**
	 * <P><b>Metodo...:</b> getEMail
	 * <P><b>Descricao:</b> Busca o e-mail do 
	 * @return String	- e-mail
	 */
	public String getEMail()
	{
		return this.eMail;
	}

	// Métodos Set                             
	/**
	 * <P><b>Metodo...:</b> setId
	 * <P><b>Descricao:</b> Seta o valor do Id 
	 * @param	int	- Valor do Id
	 */
	public void setId(int id) 
	{
		this.id = id;
	}

	/**
	 * <P><b>Metodo...:</b> setConsulta
	 * <P><b>Descricao:</b> Seta a consulta a ser realizada 
	 * @param	String	- Consulta
	 */
	public void setConsulta(String consulta) 
	{
		this.consulta = consulta;
	}

	/**
	 * <P><b>Metodo...:</b> setData
	 * <P><b>Descricao:</b> Seta o valor da data de agendamento 
	 * @param	String	- Valor da data
	 */
	public void setData(String data) 
	{
		this.data = data;
	}

	/**
	 * <P><b>Metodo...:</b> setArquivo
	 * <P><b>Descricao:</b> Seta o nome do arquivo 
	 * @param	String	- Nome do arquivo
	 */
	public void setArquivo(String arquivo)
	{
		this.arquivo = arquivo;
	}

	/**
	 * <P><b>Metodo...:</b> setTipoSolicitacao
	 * <P><b>Descricao:</b> Seta o valor do tipo de solicitação
	 * @param	String	- Tipo de solicitação
	 */
	public void setTipoSolicitacao(String tipoSolicitacao) 
	{
		this.tipoSolicitacao = tipoSolicitacao;
	}
	
	/**
	 * <P><b>Metodo...:</b> setOperador
	 * <P><b>Descricao:</b> Seta o nome do operador
	 * @param	String	- Operador
	 */
	public void setOperador(String operador)
	{
		this.operador = operador;
	}

	/**
	 * <P><b>Metodo...:</b> setEMail
	 * <P><b>Descricao:</b> Seta o e-mail do usuário
	 * @param	String	- mail
	 */
	public void setEMail(String mail)
	{
		this.eMail = mail;
	}	
}
