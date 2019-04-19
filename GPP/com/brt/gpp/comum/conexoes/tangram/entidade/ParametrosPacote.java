package com.brt.gpp.comum.conexoes.tangram.entidade;

import java.io.Serializable;

/**
 *	Classe que representa o conjunto de parâmetros de um pacote Tangram.  
 * 
 *  @author Bernardo Vergne Dias
 *  Criado em: 18/09/2007
 */
public class ParametrosPacote implements Serializable
{
	private static final long serialVersionUID = 8811522365537158141L;
	
	public static final Short INTERFACE_DESCONHECIDO = new Short((short)0);
	public static final Short INTERFACE_WEB			 = new Short((short)1);
	public static final Short INTERFACE_SMS_MO		 = new Short((short)2);
	public static final Short INTERFACE_WAP			 = new Short((short)3);
	public static final Short INTERFACE_URA			 = new Short((short)4);
	public static final Short INTERFACE_SIM_CARD	 = new Short((short)5);
	public static final Short INTERFACE_MMS			 = new Short((short)6);
	public static final Short INTERFACE_MExE		 = new Short((short)7);
	public static final Short INTERFACE_CALL_CENTER  = new Short((short)8);
	
	/**
	 * Identificador do pacote para a aplicação. 
	 * Pode ser utilizado para posterior batimento entre os dados registrados 
	 * no Tangram e os dados registrados na aplicação.
	 */
	private String idExterno;
	
	/**
	 * Nome curto do pacote.
	 */
	private String nome;
	
	/**
	 * Descrição (ou nome longo) do pacote.
	 */
	private String descricao;
	
	/**
	 * Dono do pacote. Caso não seja informado, é implícito que o dono 
	 * do pacote é o destinatário da mensagem.
	 */
	private String idtMsisdnDono;
	
	/**
	 * Interface utilizada pelo cliente para realização do pedido.
	 * Os valores possíveis estão nas constantes INTERFACE_.
	 */
	private Short interfaceCliente;
	
	/**
	 * Informações de copyright do pacote.
	 */
	private String copyright;

	/**
	 * Obtém informações de copyright do pacote.
	 */
	public String getCopyright() 
	{
		return copyright;
	}

	/**
	 * Determina informações de copyright do pacote.
	 */
	public void setCopyright(String copyright) 
	{
		this.copyright = copyright;
	}

	/**
	 * Obtém descrição (ou nome longo) do pacote.
	 */
	public String getDescricao() 
	{
		return descricao;
	}

	/**
	 * Define descrição (ou nome longo) do pacote.
	 */
	public void setDescricao(String descricao) 
	{
		this.descricao = descricao;
	}

	/**
	 * Obtém o identificador do pacote para a aplicação. 
	 */
	public String getIdExterno() 
	{
		return idExterno;
	}

	/**
	 * Define o identificador do pacote para a aplicação. 
	 * Pode ser utilizado para posterior batimento entre os dados registrados 
	 * no Tangram e os dados registrados na aplicação.
	 */
	public void setIdExterno(String idExterno) 
	{
		this.idExterno = idExterno;
	}

	/**
	 * Obtém o dono do pacote. 
	 */
	public String getIdtMsisdnDono() 
	{
		return idtMsisdnDono;
	}

	/**
	 * Define o dono do pacote. Caso não seja informado, é implícito que o dono 
	 * do pacote é o destinatário da mensagem.
	 */
	public void setIdtMsisdnDono(String idtMsisdnDono) 
	{
		this.idtMsisdnDono = idtMsisdnDono;
	}

	/**
	 * Obtém a interface utilizada pelo cliente para realização do pedido.
	 */
	public Short getInterfaceCliente() 
	{
		return interfaceCliente;
	}

	/**
	 * Define a interface utilizada pelo cliente para realização do pedido.
	 * Os valores possíveis estão nas constantes INTERFACE_.
	 */
	public void setInterfaceCliente(Short interfaceCliente) 
	{
		this.interfaceCliente = interfaceCliente;
	}

	/**
	 * Obtém nome curto do pacote.
	 */
	public String getNome() 
	{
		return nome;
	}

	/**
	 * Define nome curto do pacote.
	 */
	public void setNome(String nome) 
	{
		this.nome = nome;
	}
	
	
	
}
