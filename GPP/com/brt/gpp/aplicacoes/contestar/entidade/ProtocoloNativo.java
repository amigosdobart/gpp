package com.brt.gpp.aplicacoes.contestar.entidade;

/**
 * Entidade responsavel por conter os dados do ProtocoloNativo
 * 
 * @author João Paulo Galvagni
 * @since  07/03/2007
 */
public class ProtocoloNativo
{
	// Campos do XML de publicacao do Protocolo e 
	// da consulta a protocolo nativo
	private String		id;
	private String		protocoloUnico;
	private String 		protocoloNativo;
	private String 		tipo;
	private String     	codOperadora;
	
	private String		status;
	private boolean 	fechado;
	
	// Campos do XML de publicacao apenas
	private String		matriculaAgente;
	private String		numeroTerminal;
	private String		cpfCnpjCliente;
	
	// Mensagem de erro
	private String		msgErro;
	
	/**
	 * @return the codOperadora
	 */
	public String getCodOperadora()
	{
		return codOperadora;
	}
	
	/**
	 * @param codOperadora the codOperadora to set
	 */
	public void setCodOperadora(String codOperadora)
	{
		this.codOperadora = codOperadora;
	}
	
	/**
	 * @return the cpfCnpjCliente
	 */
	public String getCpfCnpjCliente()
	{
		return cpfCnpjCliente;
	}
	
	/**
	 * @param cpfCnpjCliente the cpfCnpjCliente to set
	 */
	public void setCpfCnpjCliente(String cpfCnpjCliente)
	{
		this.cpfCnpjCliente = cpfCnpjCliente;
	}
	
	/**
	 * @return the matriculaAgente
	 */
	public String getMatriculaAgente()
	{
		return matriculaAgente;
	}
	
	/**
	 * @param matriculaAgente the matriculaAgente to set
	 */
	public void setMatriculaAgente(String matriculaAgente)
	{
		this.matriculaAgente = matriculaAgente;
	}
	
	/**
	 * @return the msgErro
	 */
	public String getMsgErro()
	{
		return msgErro;
	}
	
	/**
	 * @param msgErro the msgErro to set
	 */
	public void setMsgErro(String msgErro)
	{
		this.msgErro = msgErro;
	}
	
	/**
	 * @return the numeroTerminal
	 */
	public String getNumeroTerminal()
	{
		return numeroTerminal;
	}
	
	/**
	 * @param numeroTerminal the numeroTerminal to set
	 */
	public void setNumeroTerminal(String numeroTerminal)
	{
		this.numeroTerminal = numeroTerminal;
	}
	
	/**
	 * @return the protocoloNativo
	 */
	public String getProtocoloNativo()
	{
		return protocoloNativo;
	}
	
	/**
	 * @param protocoloNativo the protocoloNativo to set
	 */
	public void setProtocoloNativo(String protocoloNativo)
	{
		this.protocoloNativo = protocoloNativo;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * @return the tipo
	 */
	public String getTipo()
	{
		return tipo;
	}
	
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	
	/**
	 * @return the fechado
	 */
	public boolean isFechado()
	{
		return fechado;
	}
	
	/**
	 * @param isFechado the fechado to set
	 */
	public void setFechado(String isFechado)
	{
		fechado = false;
		
		if (isFechado != null && !"".equals(isFechado))
			this.fechado = true;
	}
	
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * @return the protocoloUnico
	 */
	public String getProtocoloUnico()
	{
		return protocoloUnico;
	}
	
	/**
	 * @param protocoloUnico the protocoloUnico to set
	 */
	public void setProtocoloUnico(String protocoloUnico)
	{
		this.protocoloUnico = protocoloUnico;
	}
}