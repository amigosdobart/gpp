package com.brt.gpp.comum.conexoes.toolbar;

/**
 *	Classe que encapsula as informacoes contidas em uma linha enviada/recebida na execucao da comunicacao do socket
 *	para o protocolo com o Toolbar. O Toolbar e responsavel pela geracao do numero do protocolo unico, que deve ser
 *	associado ao numero do Boletim de Sindicancia da contestacao aberta no WPP.
 *
 *	@author		Daniel Ferreira
 *	@since		21/12/2006
 */
public class ElementoProtocoloToolbar 
{
	public static final String MSG_REQ_NUM_PROT_UNICO = "getprop";
	public static final int COD_SUCESSO = 0;
	private String mensagemProtocolo;
	private String mensagemRetorno;
	private long numeroProtocolo;
	private int codigoRetorno;
	
	/**
	 *	Requisicao default de requisicao de numero do protocolo unico.
	 */
	public static final ElementoProtocoloToolbar REQ_NUM_PROT_UNICO = 
		new ElementoProtocoloToolbar(ElementoProtocoloToolbar.MSG_REQ_NUM_PROT_UNICO, 
									 ElementoProtocoloToolbar.COD_SUCESSO, 
									 null, 
									 -1);
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		mensagemProtocolo		Mensagem identificadora da execucao do protocolo de comunicacao com o Toolbar.
	 *	@param		codigoRetorno			Codigo de retorno informado pelo Toolbar.
	 *	@param		mensagemRetorno			Mensagem de retorno informada pelo Toolbar.
	 *	@param		numeroProtocolo			Numero de protocolo unico informado pelo Toolbar.  
	 */
	public ElementoProtocoloToolbar(String mensagemProtocolo, int codigoRetorno, String mensagemRetorno, long numeroProtocolo)
	{
		this.mensagemProtocolo	= mensagemProtocolo;
		this.codigoRetorno		= codigoRetorno;
		this.mensagemRetorno	= mensagemRetorno;
		this.numeroProtocolo	= numeroProtocolo; 
	}
	
	/**
	 *	Retorna a mensagem enviada/recebida durante a execucao do protocolo unico.
	 *
	 *	@return		Mensagem enviada/recebida durante a execucao do protocolo unico. 
	 */
	public String getMensagemProtocolo()
	{
		return this.mensagemProtocolo;
	}
	
	/**
	 *	Retorna o codigo de retorno informado pelo Toolbar.
	 *
	 * 	@return		Codigo de retorno informado pelo Toolbar. 
	 */
	public int getCodigoRetorno()
	{
		return this.codigoRetorno;
	}
	
	/**
	 *	Retorna a mensagem de retorno informada pelo Toolbar.
	 *
	 * 	@return		Mensagem de retorno informada pelo Toolbar. 
	 */
	public String getMensagemRetorno()
	{
		return this.mensagemRetorno;
	}
	
	/**
	 *	Retorna o numero de protocolo unico gerado durante a conexao.
	 *
	 * 	@return		Numero de protocolo unico gerado durante a conexao. 
	 */
	public long getNumeroProtocolo()
	{
		return this.numeroProtocolo;
	}
	
	/**
	 *	Indica se a mensagem retornada no protocolo e de sucesso.
	 * 
	 *	@return     True se for sucesso e false caso contrario.
	 */
	public boolean isSucesso()
	{
		return (this.codigoRetorno == ElementoProtocoloToolbar.COD_SUCESSO);
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		mensagemProtocolo		Mensagem informada em algum passo da execucao do protocolo.
	 *	@return     True se corresponder e false caso contrario.
	 */
	public boolean equals(String mensagemProtocolo)
	{
		if((this.mensagemProtocolo == null) || (mensagemProtocolo == null))
			return false;
		
		return this.mensagemProtocolo.equals(mensagemProtocolo);
	}
}