package com.brt.gpp.comum.conexoes.tecnomen.entidade;

/**
 *	Entidade que contem informacoes de autenticacao para estabelecimento de conexoes com os servicos da Tecnomen.
 * 
 *	@author		Daniel Ferreira
 *	@since		23/02/2007
 */
public class Autenticador 
{

	/**
	 *	Identificador do servico da Tecnomen. Os servicos correspondem as interfaces com cada servidor. 
	 *	Ex: Provisioning Interface, Prepaid Interface, Payment Inteface possuem identificadores unicos.
	 */
	private short servico;
	
	/**
	 *	Identificador do usuario cadastrado no Servidor de Autenticacao.
	 */
	private int idUsuario;
	
	/**
	 *	Login do usuario cadastrado no Servidor de Autenticacao.
	 */
	private String login;
	
	/**
	 *	Tipo do usuario (nivel de permissao da Tecnomen).
	 */
	private short tipo;
	
	/**
	 *	Identificador de origem do usuario cadastrado no Servidor de Autenticacao.
	 */
	private short idOrigem;
	
	/**
	 *	Identificador de localizacao do servico da Tecnomen.
	 */
	private short idLocalizacao;
	
	/**
	 *	Chave da conexao do usuario GPP com o servico.
	 */
	private int chave;
	
	/**
	 *	IOR representando a conexao com o servico especifico da Tecnomen. Cada um dos servicos possui um IOR. Por
	 *	exemplo, existem IOR's diferentes para o Provision Server, PP Server, Payment Engine, Voucher Management, etc.
	 */
	private String ior;

	/**
	 *	Construtor da classe.
	 *
	 *	@param		servico					Identificador do servico da Tecnomen.
	 *	@param		idUsuario				Identificador do usuario cadastrado no Servidor de Autenticacao.
	 *	@param		login					Login do usuario cadastrado no Servidor de Autenticacao.
	 *	@param		tipo					Tipo do usuario cadastrado no Servidor de Autenticacao.
	 *	@param		idOrigem				Identificador de origem do usuario cadastrado no Servidor de Autenticacao.
	 *	@param		idLocalizacao			Identificador de localizacao do servico da Tecnomen.
	 *	@param		chave					Chave da conexao do usuario GPP com o servico.
	 *	@param		ior						Conexao com o servico especifico da Tecnomen.
	 */
	public Autenticador(short	servico, 
						int		idUsuario, 
						String	login, 
						short	tipo, 
						short	idOrigem, 
						short	idLocalizacao, 
						int		chave, 
						String	ior)
	{
		this.servico		= servico;
		this.idUsuario		= idUsuario;
		this.login			= login;
		this.tipo			= tipo;
		this.idOrigem		= idOrigem;
		this.idLocalizacao	= idLocalizacao;
		this.chave			= chave;
		this.ior			= ior;
	}
	
	/**
	 *	Retorna o identificador do servico da Tecnomen. Os servicos correspondem as interfaces com cada servidor. 
	 *	Ex: Provisioning Interface, Prepaid Interface, Payment Inteface possuem identificadores unicos.
	 *
	 *	@return		Identificador do servico da Tecnomen.
	 */
	public short getServico()
	{
		return this.servico;
	}
	
	/**
	 *	Retorna o dentificador do usuario cadastrado no Servidor de Autenticacao.
	 *
	 *	@return		Identificador do usuario cadastrado no Servidor de Autenticacao.
	 */
	public int getIdUsuario()
	{
		return this.idUsuario;
	}
	
	/**
	 *	Retorna o login do usuario cadastrado no Servidor de Autenticacao.
	 *
	 *	@return		Login do usuario cadastrado no Servidor de Autenticacao.
	 */
	public String getLogin()
	{
		return this.login;
	}
	
	/**
	 *	Retorna o identificador de origem do usuario cadastrado no Servidor de Autenticacao.
	 *
	 *	@return		Identificador de origem do usuario cadastrado no Servidor de Autenticacao.
	 */
	public short getIdOrigem()
	{
		return this.idOrigem;
	}
	
	/**
	 *	Retorna o tipo do usuario (nivel de permissao da Tecnomen).
	 *
	 *	@return		Tipo do usuario (nivel de permissao da Tecnomen).
	 */
	public short getTipo()
	{
		return this.tipo;
	}
	
	/**
	 *	Retorna o identificador de localizacao do servico da Tecnomen.
	 *
	 *	@return		Identificador de localizacao do servico da Tecnomen.
	 */
	public short getIdLocalizacao()
	{
		return this.idLocalizacao;
	}
	
	/**
	 *	Retorna a chave da conexao do usuario GPP com o servico.
	 *
	 *	@return		Chave da conexao do usuario GPP com o servico.
	 */
	public int getChave()
	{
		return this.chave;
	}
	
	/**
	 *	Retorna o IOR representando a conexao com o servico especifico da Tecnomen.
	 *
	 *	@return		IOR representando a conexao com o servico especifico da Tecnomen.
	 */
	public String getIor()
	{
		return this.ior;
	}
	
}
